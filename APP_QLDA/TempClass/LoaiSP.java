package TempClass;

public class LoaiSP {
    private String maLoai;
    private String tenLoai;
    private String donvitinh;

    public LoaiSP() {}

    public LoaiSP(String maLoai, String tenLoai, String dvt) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.donvitinh = dvt;
    }

    public String getMaLoai() { return maLoai; }
    public void setMaLoai(String maLoai) { this.maLoai = maLoai; }

    public String getTenLoai() { return tenLoai; }
    public void setTenLoai(String tenLoai) { this.tenLoai = tenLoai; }

    public String getDVT() {return  donvitinh;}
    public void setDVT(String dvt){ this.donvitinh = dvt;}
}
