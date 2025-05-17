package DBFunction;

import TempClass.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class NguoiDungDAO {
    private Connection conn;

    public NguoiDungDAO() {
        try {
            this.conn = DBConnection.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to establish database connection", e);
        }
    }

    public boolean themNguoiDung(NguoiDung nd) throws SQLException {
        String sql = "INSERT INTO NGUOIDUNG VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nd.getMaND());
            stmt.setString(2, nd.getTenND());
            stmt.setString(3, nd.getTenDangNhap());
            stmt.setString(4, nd.getMatKhau());
            stmt.setString(5, nd.getMaVT());
            stmt.setInt(6, nd.getTrangThai());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean xoaNguoiDung(String maND) throws SQLException {
        String sql = "DELETE FROM NGUOIDUNG WHERE MAND = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maND);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean suaNguoiDung(NguoiDung nd) throws SQLException {
        String sql = "UPDATE NGUOIDUNG SET TENND=?, TENDANGNHAP=?, MATKHAU=?, MAVT=?, TRANGTHAI=? WHERE MAND=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nd.getTenND());
            stmt.setString(2, nd.getTenDangNhap());
            stmt.setString(3, nd.getMatKhau());
            stmt.setString(4, nd.getMaVT());
            stmt.setInt(5, nd.getTrangThai());
            stmt.setString(6, nd.getMaND());
            return stmt.executeUpdate() > 0;
        }
    }

    public ArrayList<NguoiDung> getAllNguoiDung() throws SQLException {
        ArrayList<NguoiDung> list = new ArrayList<>();
        String sql = "SELECT * FROM NGUOIDUNG";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                NguoiDung nd = new NguoiDung();
                nd.setMaND(rs.getString("MAND"));
                nd.setTenND(rs.getString("TENND"));
                nd.setTenDangNhap(rs.getString("TENDANGNHAP"));
                nd.setMatKhau(rs.getString("MATKHAU"));
                nd.setMaVT(rs.getString("MAVT"));
                nd.setTrangThai(rs.getInt("TRANGTHAI"));
                list.add(nd);
            }
        }
        return list;
    }
    public String kiemtraNguoiDung(String username, String passwork) {
        String sql = "SELECT * FROM NGUOIDUNG WHERE TENDANGNHAP = ? AND MATKHAU = ?";
        String vaitro = "None";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, passwork);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                NguoiDung ng = new NguoiDung();
                vaitro = rs.getString("MAVT");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vaitro;
    }
}
