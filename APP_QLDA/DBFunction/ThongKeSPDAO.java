package DBFunction;

import TempClass.ThongKeSP;
import java.sql.*;
import java.util.ArrayList;
public class ThongKeSPDAO {
    private Connection conn;

    public ThongKeSPDAO(Connection conn) {
        this.conn = conn;
    }

    public ArrayList<ThongKeSP> getAll() throws SQLException {
        ArrayList<ThongKeSP> list = new ArrayList<>();
        String sql = "SELECT * FROM ThongKeSP";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new ThongKeSP(
                rs.getString("MATKSP"),
                rs.getString("MATK"),
                rs.getString("MASP"),
                rs.getInt("TONKHO")
            ));
        }
        return list;
    }
    public ArrayList<ThongKeSP> getAllbyID(String id) throws SQLException {
        ArrayList<ThongKeSP> list = new ArrayList<>();
        String sql = "SELECT * FROM ThongKeSP WHERE MATK = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new ThongKeSP(
                rs.getString("MATKSP"),
                rs.getString("MATK"),
                rs.getString("MASP"),
                rs.getInt("TONKHO")
            ));
        }
        return list;
    }

    public boolean insert(ThongKeSP tksp) throws SQLException {
        String sql = "INSERT INTO ThongKeSP VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, tksp.getMaTKSP());
        ps.setString(2, tksp.getMaTK());
        ps.setString(3, tksp.getMaSP());
        ps.setInt(4, tksp.getTonKho());
        return ps.executeUpdate() > 0;
    }

    public boolean update(ThongKeSP tksp) throws SQLException {
        String sql = "UPDATE ThongKeSP SET MATK=?, MASP=?, TONKHO=? WHERE MATKSP=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, tksp.getMaTK());
        ps.setString(2, tksp.getMaSP());
        ps.setInt(3, tksp.getTonKho());
        ps.setString(4, tksp.getMaTKSP());
        return ps.executeUpdate() > 0;
    }

    public boolean delete(String matksp) throws SQLException {
        String sql = "DELETE FROM ThongKeSP WHERE MATKSP=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, matksp);
        return ps.executeUpdate() > 0;
    }
}
