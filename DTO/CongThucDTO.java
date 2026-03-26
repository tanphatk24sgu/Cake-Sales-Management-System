package DTO;

public class CongThucDTO {
    private int maCongThuc;
    private int maBanh;
    private int maDVT;
    private String cachLam;

    public CongThucDTO() {
    }

    public int getMaCongThuc() {
        return maCongThuc;
    }

    public void setMaCongThuc(int maCongThuc) {
        this.maCongThuc = maCongThuc;
    }

    public int getMaBanh() {
        return maBanh;
    }

    public void setMaBanh(int maBanh) {
        this.maBanh = maBanh;
    }

    public int getMaDVT() {
        return maDVT;
    }

    public void setMaDVT(int maDVT) {
        this.maDVT = maDVT;
    }

    public String getCachLam() {
        return cachLam;
    }

    public void setCachLam(String cachLam) {
        this.cachLam = cachLam;
    }
}