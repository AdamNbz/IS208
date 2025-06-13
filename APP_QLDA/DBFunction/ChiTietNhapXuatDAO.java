package DBFunction;
import TempClass.*;
import java.sql.*;
import java.util.ArrayList;

public class ChiTietNhapXuatDAO {
    private Connection conn;

    public ChiTietNhapXuatDAO() {
        try {
            this.conn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get database connection", e);
        }
    }

    public boolean themChiTiet(ChiTietNhapXuat ct) throws SQLException {
        // Thêm chi tiết trước
        String sltonkho = "SELECT TONKHO FROM SANPHAM WHERE MASP = ?";
        int tonkho = 0;
        try (PreparedStatement stmt = conn.prepareStatement(sltonkho)) {
            stmt.setString(1, ct.getMaSP());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tonkho = rs.getInt("TONKHO");
                }
            }
        }
        if (tonkho >= ct.getSoLuong()){
            String sql = "INSERT INTO CHITIETNHAPXUAT (MACT, MAPHIEU, MASP, SOLUONG) VALUES (?, ?, ?, ?)";
            boolean inserted = false;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, ct.getMaCT());
                stmt.setString(2, ct.getMaPhieu());
                stmt.setString(3, ct.getMaSP());
                stmt.setInt(4, ct.getSoLuong());
                inserted = stmt.executeUpdate() > 0;
            }
            if (!inserted) return false;
    
            // Kiểm tra loại phiếu
            String loai = null;
            String sqlLoai = "SELECT LOAI FROM PHIEUNHAPXUAT WHERE MAPHIEU = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlLoai)) {
                stmt.setString(1, ct.getMaPhieu());
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    loai = rs.getString("LOAI");
                }
            }
            // Nếu là phiếu xuất thì cập nhật tồn kho
            if ("XUAT".equalsIgnoreCase(loai)) {
                String sqlUpdate = "UPDATE SANPHAM SET TONKHO = TONKHO - ? WHERE MASP = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {
                    stmt.setInt(1, ct.getSoLuong());
                    stmt.setString(2, ct.getMaSP());
                    stmt.executeUpdate();
                }
            }
            else {
                String sqlUpdate = "UPDATE SANPHAM SET TONKHO = TONKHO + ? WHERE MASP = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {
                    stmt.setInt(1, ct.getSoLuong());
                    stmt.setString(2, ct.getMaSP());
                    stmt.executeUpdate();
                }
            }
            return true;
        }
        return false;
    }

    public ArrayList<ChiTietNhapXuat> getChiTietTheoPhieu(String maPhieu) throws SQLException {
        ArrayList<ChiTietNhapXuat> list = new ArrayList<>();
        String sql = "SELECT * FROM CHITIETNHAPXUAT WHERE MAPHIEU = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maPhieu);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ChiTietNhapXuat ct = new ChiTietNhapXuat();
                ct.setMaCT(rs.getString("MACT"));
                ct.setMaPhieu(rs.getString("MAPHIEU"));
                ct.setMaSP(rs.getString("MASP"));
                ct.setSoLuong(rs.getInt("SOLUONG"));
                list.add(ct);
            }
        }
        return list;
    }
}
