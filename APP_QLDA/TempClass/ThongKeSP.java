package TempClass;
public class ThongKeSP {
    private String maTKSP;
    private String maTK;
    private String maSP;
    private int tonKho;

    public ThongKeSP() {}

    public ThongKeSP(String maTKSP, String maTK, String maSP, int tonKho) {
        this.maTKSP = maTKSP;
        this.maTK = maTK;
        this.maSP = maSP;
        this.tonKho = tonKho;
    }

    public String getMaTKSP() {
        return maTKSP;
    }

    public void setMaTKSP(String maTKSP) {
        this.maTKSP = maTKSP;
    }

    public String getMaTK() {
        return maTK;
    }

    public void setMaTK(String maTK) {
        this.maTK = maTK;
    }

    public String getMaSP() {
        return maSP;
    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    public int getTonKho() {
        return tonKho;
    }

    public void setTonKho(int tonKho) {
        this.tonKho = tonKho;
    }
}
