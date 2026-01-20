package DTO;

import java.util.Date;

public class PhieunhaphangDTO {
    private int maPhieuNhap;
    private Date ngay;
    private int maNV;
    private int maNCC;

    // Constructor
    public PhieunhaphangDTO() {
        this.maPhieuNhap = 0;
        this.ngay = new Date();
        this.maNV = 0;
        this.maNCC = 0;
    }

    // Getter and Setter
    public int getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public void setMaPhieuNhap(int maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public Date getNgay() {
        return ngay;
    }

    public void setNgay(Date ngay) {
        this.ngay = ngay;
    }

    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public int getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(int maNCC) {
        this.maNCC = maNCC;
    }

    // toString
    @Override
    public String toString() {
        return "Mã phiếu nhập: " + maPhieuNhap + " | Ngày: " + ngay + 
               " | Mã NV: " + maNV + " | Mã NCC: " + maNCC;
    }
}
