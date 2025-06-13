package DBFunction;

import java.sql.*;
import java.util.ArrayList;
import TempClass.NhaCungCap;
public class NhaCungCapDAO {
    private Connection conn;

    public NhaCungCapDAO(Connection conn) {
        this.conn = conn;
    }

    public ArrayList<NhaCungCap> getAll() throws SQLException {
        ArrayList<NhaCungCap> list = new ArrayList<>();
        String sql = "SELECT * FROM NhaCungCap";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new NhaCungCap(
                rs.getString("MANCC"),
                rs.getString("TENNCC"),
                rs.getString("SDT"),
                rs.getString("DIACHI"),
                rs.getString("EMAIL")
            ));
        }
        return list;
    }

    public boolean insert(NhaCungCap ncc) throws SQLException {
        String sql = "INSERT INTO NhaCungCap VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, ncc.getMaNCC());
        ps.setString(2, ncc.getTenNCC());
        ps.setString(3, ncc.getSdt());
        ps.setString(4, ncc.getDiaChi());
        ps.setString(5, ncc.getEmail());
        return ps.executeUpdate() > 0;
    }

    public boolean update(NhaCungCap ncc) throws SQLException {
        String sql = "UPDATE NhaCungCap SET TENNCC=?, SDT=?, DIACHI=?, EMAIL=? WHERE MANCC=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, ncc.getTenNCC());
        ps.setString(2, ncc.getSdt());
        ps.setString(3, ncc.getDiaChi());
        ps.setString(4, ncc.getEmail());
        ps.setString(5, ncc.getMaNCC());
        return ps.executeUpdate() > 0;
    }

    public boolean delete(String maNCC) throws SQLException {
        String sql = "DELETE FROM NhaCungCap WHERE MANCC=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maNCC);
        return ps.executeUpdate() > 0;
    }
}
