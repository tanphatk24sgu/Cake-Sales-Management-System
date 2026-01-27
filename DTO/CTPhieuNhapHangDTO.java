package DTO;

public class CTPhieuNhapHangDTO {
    private int maPhieuNhap;
    private int maBanh;
    private int maNVL;
    private int soLuong;
    private double donGia;
    private double thanhTien;
    private String tinhTrang;

    // Constructor
    public CTPhieuNhapHangDTO() {
        this.maPhieuNhap = 0;
        this.maBanh = 0;
        this.maNVL = 0;
        this.soLuong = 0;
        this.donGia = 0;
        this.thanhTien = 0;
        this.tinhTrang = "";
    }

    // Getter and Setter
    public int getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public void setMaPhieuNhap(int maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public int getMaBanh() {
        return maBanh;
    }

    public void setMaBanh(int maBanh) {
        this.maBanh = maBanh;
    }

    public int getMaNVL() {
        return maNVL;
    }

    public void setMaNVL(int maNVL) {
        this.maNVL = maNVL;
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

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    // toString
    @Override
    public String toString() {
        return "Mã phiếu nhập: " + maPhieuNhap + " | Mã bánh: " + maBanh + " | Mã NVL: " + maNVL + 
               " | Số lượng: " + soLuong + " | Đơn giá: " + donGia + " | Thành tiền: " + thanhTien + 
               " | Tình trạng: " + tinhTrang;
    }
}
