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
        String sql = "INSERT INTO CHITIETNHAPXUAT (MACT, MAPHIEU, MASP, SOLUONG) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ct.getMaCT());
            stmt.setString(2, ct.getMaPhieu());
            stmt.setString(3, ct.getMaSP());
            stmt.setInt(4, ct.getSoLuong());
            return stmt.executeUpdate() > 0;
        }
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
