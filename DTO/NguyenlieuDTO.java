package DTO;

public class NguyenLieuDTO {
    private int maNL;
    private String ten;
    private int soLuong;
    private double donGia;
    private int maDVT;

    // Constructor
    public NguyenLieuDTO() {
        this.maNL = 0;
        this.ten = "";
        this.soLuong = 0;
        this.donGia = 0;
        this.maDVT = 0;
    }

    // Getter and Setter
    public int getMaNL() {
        return maNL;
    }

    public void setMaNL(int maNL) {
        this.maNL = maNL;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getDonGia() {
        return donGia;
    }

    public void setDonGia(double donGia) {
        this.donGia = donGia;
    }

    public int getMaDVT() {
        return maDVT;
    }

    public void setMaDVT(int maDVT) {
        this.maDVT = maDVT;
    }

    // toString
    @Override
    public String toString() {
        return "Mã NL: " + maNL + " | Tên: " + ten + " | Số lượng: " + soLuong + 
               " | Đơn giá: " + donGia + " | Mã DVT: " + maDVT;
    }
}
