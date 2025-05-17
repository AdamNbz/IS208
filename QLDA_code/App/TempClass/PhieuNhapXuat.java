package TempClass;
import java.util.Date;

public class PhieuNhapXuat {
    private String maPhieu;
    private String loai;      // "NHAP" hoặc "XUAT"
    private Date ngayLap;
    private String maND;
    private String ghiChu;
    private String trangthai;

    public PhieuNhapXuat() {}

    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }
    public String gettrangthai(){
        return trangthai;
    }
    public void settrangthai(String trangthai){
        this.trangthai = trangthai;
    }
    public String getLoai() {
        return loai;
    }

    public void setLoai(String loai) {
        this.loai = loai;
    }

    public Date getNgayLap() {
        return ngayLap;
    }

    public void setNgayLap(Date ngayLap) {
        this.ngayLap = ngayLap;
    }

    public String getMaND() {
        return maND;
    }

    public void setMaND(String maND) {
        this.maND = maND;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }
}
