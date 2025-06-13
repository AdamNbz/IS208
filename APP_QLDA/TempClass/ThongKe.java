package TempClass;

import java.sql.Date;

public class ThongKe {
    private String maTK;
    private String noiDung;
    private Date ngay;
    private int tongPhieuXuat;
    private int tongPhieuNhap;
    private int tongSPNhap;
    private int tongSPXuat;

    public ThongKe() {}

    public ThongKe(String maTK, String noiDung, Date ngay, int tongPhieuXuat, int tongPhieuNhap, int tongSPNhap, int tongSPXuat) {
        this.maTK = maTK;
        this.noiDung = noiDung;
        this.ngay = ngay;
        this.tongPhieuXuat = tongPhieuXuat;
        this.tongPhieuNhap = tongPhieuNhap;
        this.tongSPNhap = tongSPNhap;
        this.tongSPXuat = tongSPXuat;
    }

    public String getMaTK() {
        return maTK;
    }

    public void setMaTK(String maTK) {
        this.maTK = maTK;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public Date getNgay() {
        return ngay;
    }

    public void setNgay(Date ngay) {
        this.ngay = ngay;
    }

    public int getTongPhieuXuat() {
        return tongPhieuXuat;
    }

    public void setTongPhieuXuat(int tongPhieuXuat) {
        this.tongPhieuXuat = tongPhieuXuat;
    }

    public int getTongPhieuNhap() {
        return tongPhieuNhap;
    }

    public void setTongPhieuNhap(int tongPhieuNhap) {
        this.tongPhieuNhap = tongPhieuNhap;
    }

    public int getTongSPNhap() {
        return tongSPNhap;
    }

    public void setTongSPNhap(int tongSPNhap) {
        this.tongSPNhap = tongSPNhap;
    }

    public int getTongSPXuat() {
        return tongSPXuat;
    }

    public void setTongSPXuat(int tongSPXuat) {
        this.tongSPXuat = tongSPXuat;
    }
}
