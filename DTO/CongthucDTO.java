package DTO;

public class CongthucDTO {
    private int maBanh;
    private int maDVT;
    private String cachLam;

    // Constructor
    public CongthucDTO() {
        this.maBanh = 0;
        this.maDVT = 0;
        this.cachLam = "";
    }

    // Getter and Setter
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

    // toString
    @Override
    public String toString() {
        return "Mã bánh: " + maBanh + " | Mã DVT: " + maDVT + " | Cách làm: " + cachLam;
    }
}
