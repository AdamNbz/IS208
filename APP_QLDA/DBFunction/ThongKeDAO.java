package DBFunction;

import TempClass.ThongKe;
import java.sql.*;
import java.util.ArrayList;
public class ThongKeDAO {
    private Connection conn;

    public ThongKeDAO(Connection conn) {
        this.conn = conn;
    }

    public ArrayList<ThongKe> getAll() throws SQLException {
        ArrayList<ThongKe> list = new ArrayList<>();
        String sql = "SELECT * FROM ThongKe";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new ThongKe(
                rs.getString("MATK"),
                rs.getString("NOIDUNG"),
                rs.getDate("NGAY"),
                rs.getInt("TONGPHIEUXUAT"),
                rs.getInt("TONGPHIEUNHAP"),
                rs.getInt("TONGSPNHAP"),
                rs.getInt("TONGSPXUAT")
            ));
        }
        return list;
    }
    
    public boolean insert(ThongKe tk) throws SQLException {
        String sql = "INSERT INTO ThongKe VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, tk.getMaTK());
        ps.setString(2, tk.getNoiDung());
        ps.setDate(3, tk.getNgay());
        ps.setInt(4, tk.getTongPhieuXuat());
        ps.setInt(5, tk.getTongPhieuNhap());
        ps.setInt(6, tk.getTongSPNhap());
        ps.setInt(7, tk.getTongSPXuat());
        return ps.executeUpdate() > 0;
    }
    public boolean callInsertThongKeToday(String noidung) throws SQLException {
        String sql = "{call INSERT_THONGKE_TODAY(?)}";
        try (CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, noidung);
            cs.execute();
            return true;
        }
    }
    public boolean update(ThongKe tk) throws SQLException {
        String sql = "UPDATE ThongKe SET NOIDUNG=?, NGAY=?, TONGPHIEUXUAT=?, TONGPHIEUNHAP=?, TONGSPNHAP=?, TONGSPXUAT=? WHERE MATK=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, tk.getNoiDung());
        ps.setDate(2, tk.getNgay());
        ps.setInt(3, tk.getTongPhieuXuat());
        ps.setInt(4, tk.getTongPhieuNhap());
        ps.setInt(5, tk.getTongSPNhap());
        ps.setInt(6, tk.getTongSPXuat());
        ps.setString(7, tk.getMaTK());
        return ps.executeUpdate() > 0;
    }

    public boolean delete(String matk) throws SQLException {
        String sql = "DELETE FROM ThongKe WHERE MATK=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, matk);
        return ps.executeUpdate() > 0;
    }
}
