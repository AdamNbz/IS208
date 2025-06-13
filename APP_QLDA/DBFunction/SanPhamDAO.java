package DBFunction;
import TempClass.*;
import java.sql.*;
import java.util.ArrayList;

public class SanPhamDAO {
    private Connection conn;

    public SanPhamDAO() {
        try {
        this.conn = DBConnection.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            this.conn = null;
        }
    }

    // 1. Thêm sản phẩm
    public boolean themSanPham(SanPham sp) {
        String sql = "INSERT INTO SANPHAM (MASP, TENSP, MALOAI, TONKHO, MANCC) VALUES ('', ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, sp.getTenSP());
            stmt.setString(2, sp.getMaLoaiSP());
            stmt.setInt(3, sp.getTonKho());
            stmt.setString(4, sp.getMaNCC());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); return false;
        }
    }

    // 2. Xóa sản phẩm theo mã
    public boolean xoaSanPham(String maSP) {
        String sql = "DELETE FROM SANPHAM WHERE MASP = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSP);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); return false;
        }
    }

    // 3. Sửa thông tin sản phẩm
    public boolean suaSanPham(SanPham sp) {
        String sql = "UPDATE SANPHAM SET TENSP = ?, MALOAI = ?, TONKHO = ?, MANCC = ? WHERE MASP = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, sp.getTenSP());
            stmt.setString(2, sp.getMaLoaiSP());
            stmt.setInt(3, sp.getTonKho());
            stmt.setString(4, sp.getMaNCC());
            stmt.setString(5, sp.getMaSP());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); return false;
        }
    }

    // 4. Lấy danh sách tất cả sản phẩm
    public ArrayList<SanPham> getAllSanPham() {
        ArrayList<SanPham> ds = new ArrayList<>();
        String sql = "SELECT * FROM SANPHAM";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSP(rs.getString("MASP"));
                sp.setTenSP(rs.getString("TENSP"));
                sp.setMaLoaiSP(rs.getString("MALOAI"));
                sp.setTonKho(rs.getInt("TONKHO"));                
                sp.setMaNCC(rs.getString("MANCC"));
                ds.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // 5. Tìm theo mã sản phẩm
    public SanPham timSanPhamTheoMa(String maSP) {
        String sql = "SELECT * FROM SANPHAM WHERE MASP = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, maSP);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSP(rs.getString("MASP"));
                sp.setTenSP(rs.getString("TENSP"));
                sp.setMaLoaiSP(rs.getString("MALOAI"));
                sp.setTonKho(rs.getInt("TONKHO"));
                sp.setMaNCC(rs.getString("MANCC"));
                return sp;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    // 5. Tìm số lượng loại sản phẩm
    public static int getSoLuongSanPham() {
        int value = 0;
        String sql = "SELECT COUNT(*) AS SLSP FROM SANPHAM";
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
    public static int getSoLuongtlkSanPham() {
        int value = 0;
        String sql = "SELECT SUM(TONKHO) AS SLSP FROM SANPHAM";
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
    public static ArrayList<String> getDanhSachLoaiSanPham() throws SQLException {
        ArrayList<String> dsLoai = new ArrayList<>();
        String sql = "SELECT DISTINCT MALOAI FROM SANPHAM";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                dsLoai.add(rs.getString("MALOAI"));
            }
        }

        return dsLoai;
    }
    public ArrayList<SanPham> getSanPhamTheoLoai(String MaLoaiSP) {
        ArrayList<SanPham> ds = new ArrayList<>();
        String sql = "SELECT * FROM SANPHAM WHERE MALOAI = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, MaLoaiSP);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setMaSP(rs.getString("MASP"));
                sp.setTenSP(rs.getString("TENSP"));
                sp.setMaLoaiSP(rs.getString("MALOAI"));
                sp.setTonKho(rs.getInt("TONKHO"));
                sp.setMaNCC(rs.getString("MANCC"));
                ds.add(sp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

}
