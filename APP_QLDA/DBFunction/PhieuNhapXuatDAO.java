package DBFunction;
import TempClass.*;
import java.sql.*;
import java.util.ArrayList;

public class PhieuNhapXuatDAO {
    private Connection conn;

    public PhieuNhapXuatDAO(Connection conn) throws SQLException {
        this.conn = DBConnection.getConnection();
    }

    public boolean themPhieu(PhieuNhapXuat px) throws SQLException {
        String sql = "INSERT INTO PHIEUNHAPXUAT (MAPHIEU, LOAI, NGAYLAP, MAND, GHICHU, TRANGTHAI) VALUES (?, ?, ?, ?, ?, 'Chờ duyệt')";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, px.getMaPhieu());
            stmt.setString(2, px.getLoai());
            stmt.setDate(3, new java.sql.Date(px.getNgayLap().getTime()));
            stmt.setString(4, px.getMaND());
            stmt.setString(5, px.getGhiChu());
            return stmt.executeUpdate() > 0;
        }
    }

    public ArrayList<PhieuNhapXuat> getPhieuTheoLoai(String loai) throws SQLException {
        ArrayList<PhieuNhapXuat> list = new ArrayList<>();
        String sql = "SELECT * FROM PHIEUNHAPXUAT WHERE LOAI = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, loai);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PhieuNhapXuat px = new PhieuNhapXuat();
                px.setMaPhieu(rs.getString("MAPHIEU"));
                px.setLoai(rs.getString("LOAI"));
                px.setNgayLap(rs.getDate("NGAYLAP"));
                px.setMaND(rs.getString("MAND"));
                px.setGhiChu(rs.getString("GHICHU"));
                px.settrangthai(rs.getString("TRANGTHAI"));
                list.add(px);
            }
        }
        return list;
    }
    public static int getsoluongphieunhap() {
        int value = 0;
        String sql = "SELECT COUNT(*) AS SLSP FROM PHIEUNHAPXUAT WHERE LOAI = 'NHAP'";
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                value = rs.getInt("SLSP");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }
    public static int getsoluongphieuxuat() {
        int value = 0;
        String sql = "SELECT COUNT(*) AS SLSP FROM PHIEUNHAPXUAT WHERE LOAI = 'XUAT'";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                value = rs.getInt("SLSP");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }
    public static boolean duyetPhieu(String maPhieu) {
        String sql = "UPDATE PHIEUNHAPXUAT SET TRANGTHAI = N'Đã duyệt' WHERE MAPHIEU = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);) {
            stmt.setString(1, maPhieu);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}
