package TempClass;
public class SanPham {
    private String maSP;
    private String tenSP;
    private String MaLoaiSP;
    private int tonKho;
    private String MaNCC;

    public SanPham() {}

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public String getMaLoaiSP() {
        return MaLoaiSP;
    }

    public void setMaLoaiSP(String MaLoaiSP) {
        this.MaLoaiSP = MaLoaiSP;
    }

    public int getTonKho() {
        return tonKho;
    }

    public void setTonKho(int tonKho) {
        this.tonKho = tonKho;
    }
    public String getMaNCC(){
        return MaNCC;
    }
    public void setMaNCC(String mancc){
        this.MaNCC = mancc;
    }
}
