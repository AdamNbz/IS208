package DBFunction;
import TempClass.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
public class VaiTroDAO {
    private Connection conn;

    public VaiTroDAO() {
        try {
            this.conn = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            // Optionally, rethrow as unchecked or handle as needed
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    public boolean themVaiTro(VaiTro vt) throws SQLException {
        String sql = "INSERT INTO VAITRO (MAVT, TENVT) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vt.getMaVT());
            stmt.setString(2, vt.getTenVT());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean xoaVaiTro(String maVT) throws SQLException {
        String sql = "DELETE FROM VAITRO WHERE MAVT = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maVT);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean suaVaiTro(VaiTro vt) throws SQLException {
        String sql = "UPDATE VAITRO SET TENVT = ? WHERE MAVT = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, vt.getTenVT());
            stmt.setString(2, vt.getMaVT());
            return stmt.executeUpdate() > 0;
        }
    }

    public ArrayList<VaiTro> getAllVaiTro() throws SQLException {
        ArrayList<VaiTro> list = new ArrayList<>();
        String sql = "SELECT * FROM VAITRO";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                VaiTro vt = new VaiTro();
                vt.setMaVT(rs.getString("MAVT"));
                vt.setTenVT(rs.getString("TENVT"));
                list.add(vt);
            }
        }
        return list;
    }
}
