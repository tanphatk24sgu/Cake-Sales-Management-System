package DTO;

import java.util.Date;

public class HoaDonDTO {
    private int maHD;
    private Date ngayLapHD;
    private int maNV;
    private int maKH;
    private double thanhTien;

    // Constructor
    public HoaDonDTO() {
        this.maHD = 0;
        this.ngayLapHD = new Date();
        this.maNV = 0;
        this.maKH = 0;
        this.thanhTien = 0;
    }

    // Getter and Setter
    public int getMaHD() {
        return maHD;
    }

    public void setMaHD(int maHD) {
        this.maHD = maHD;
    }

    public Date getNgayLapHD() {
        return ngayLapHD;
    }

    public void setNgayLapHD(Date ngayLapHD) {
        this.ngayLapHD = ngayLapHD;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    // toString
    @Override
    public String toString() {
        return "Mã HD: " + maHD + " | Ngày lập: " + ngayLapHD + " | Mã NV: " + maNV + 
               " | Mã KH: " + maKH + " | Thành tiền: " + thanhTien;
    }
}
