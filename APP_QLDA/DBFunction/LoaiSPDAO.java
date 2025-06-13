package DBFunction;

import TempClass.LoaiSP;
import java.sql.*;
import java.util.ArrayList;

public class LoaiSPDAO {
    private Connection conn;

    public LoaiSPDAO(Connection conn) {
        this.conn = conn;
    }

    public ArrayList<LoaiSP> getAll() throws SQLException {
        ArrayList<LoaiSP> list = new ArrayList<>();
        String sql = "SELECT * FROM LoaiSP";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            list.add(new LoaiSP(rs.getString("MALOAI"), rs.getString("TENLOAI"), rs.getString("DONVITINH")));
        }
        return list;
    }
    public LoaiSP getAllbyID(String maloai) throws SQLException {
        LoaiSP list = new LoaiSP();
        String sql = "SELECT * FROM LoaiSP WHERE MALOAI = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maloai);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            list = new LoaiSP(rs.getString("MALOAI"), rs.getString("TENLOAI"), rs.getString("DONVITINH"));
        }
        return list;
    }

    public boolean insert(LoaiSP loai) throws SQLException {
        String sql = "INSERT INTO LoaiSP VALUES (?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, loai.getMaLoai());
        ps.setString(2, loai.getTenLoai());
        ps.setString(3,loai.getDVT());
        return ps.executeUpdate() > 0;
    }

    public boolean update(LoaiSP loai) throws SQLException {
        String sql = "UPDATE LoaiSP SET TENLOAI=?, DONVITINH = ? WHERE MALOAI=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, loai.getTenLoai());
        ps.setString(2,loai.getDVT());
        ps.setString(3, loai.getMaLoai());
        return ps.executeUpdate() > 0;
    }

    public boolean delete(String maLoai) throws SQLException {
        String sql = "DELETE FROM LoaiSP WHERE MALOAI=?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, maLoai);
        return ps.executeUpdate() > 0;
    }
}
