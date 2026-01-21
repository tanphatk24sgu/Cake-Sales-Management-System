package DTO;

public class ChiTietHoaDonDTO {
    private int maHD;
    private int maBanh;
    private int soLuong;
    private double donGia;
    private double thanhTien;
    private int diem;

    // Constructor
    public ChiTietHoaDonDTO() {
        this.maHD = 0;
        this.maBanh = 0;
        this.soLuong = 0;
        this.donGia = 0;
        this.thanhTien = 0;
        this.diem = 0;
    }

    // Getter and Setter
    public int getMaHD() {
        return maHD;
    }

    public void setMaHD(int maHD) {
        this.maHD = maHD;
    }

    public int getMaBanh() {
        return maBanh;
    }

    public void setMaBanh(int maBanh) {
        this.maBanh = maBanh;
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

    public int getDiem() {
        return diem;
    }

    public void setDiem(int diem) {
        this.diem = diem;
    }

    // toString
    @Override
    public String toString() {
        return "Mã HD: " + maHD + " | Mã bánh: " + maBanh + " | Số lượng: " + soLuong + 
               " | Đơn giá: " + donGia + " | Thành tiền: " + thanhTien + " | Điểm: " + diem;
    }
}
