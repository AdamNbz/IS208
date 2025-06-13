package TempClass;

public class NhaCungCap {
    private String maNCC;
    private String tenNCC;
    private String sdt;
    private String diaChi;
    private String email;

    public NhaCungCap() {}

    public NhaCungCap(String maNCC, String tenNCC, String sdt, String diaChi, String email) {
        this.maNCC = maNCC;
        this.tenNCC = tenNCC;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.email = email;
    }

    // Getters and Setters
    public String getMaNCC() { return maNCC; }
    public void setMaNCC(String maNCC) { this.maNCC = maNCC; }

    public String getTenNCC() { return tenNCC; }
    public void setTenNCC(String tenNCC) { this.tenNCC = tenNCC; }

    public String getSdt() { return sdt; }
    public void setSdt(String sdt) { this.sdt = sdt; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
