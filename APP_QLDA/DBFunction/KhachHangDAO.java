package DBFunction;

import java.sql.*;
import java.util.ArrayList;
import TempClass.KhachHang;
public class KhachHangDAO {
    private Connection conn;

    public KhachHangDAO(Connection conn) {
        this.conn = conn;
    }

    public ArrayList<KhachHang> getAll() throws SQLException {
        ArrayList<KhachHang> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new KhachHang(
                rs.getString("MAKH"),
                rs.getString("TENKH"),
                rs.getString("SDT"),
                rs.getString("DIACHI")
            ));
        }
        return list;
    }

    public boolean insert(KhachHang kh) throws SQLException {
        String sql = "INSERT INTO KhachHang VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, kh.getMaKH());
        ps.setString(2, kh.getTenKH());
        ps.setString(3, kh.getSdt());
        ps.setString(4, kh.getDiaChi());
        return ps.executeUpdate() > 0;
    }

    public boolean update(KhachHang kh) throws SQLException {
        String sql = "UPDATE KhachHang SET TENKH=?, SDT=?, DIACHI=? WHERE MAKH=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, kh.getTenKH());
        ps.setString(2, kh.getSdt());
        ps.setString(3, kh.getDiaChi());
        ps.setString(4, kh.getMaKH());
        return ps.executeUpdate() > 0;
    }

    public boolean delete(String maKH) throws SQLException {
        String sql = "DELETE FROM KhachHang WHERE MAKH=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maKH);
        return ps.executeUpdate() > 0;
    }
}
