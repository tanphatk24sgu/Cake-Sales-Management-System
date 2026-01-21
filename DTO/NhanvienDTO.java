package DTO;

import java.util.Date;

public class NhanVienDTO {
    private int maNV;
    private String ho;
    private String ten;
    private Date ngaySinh;
    private double luongCoBan;
    private int chucVu;

    // Constructor
    public NhanVienDTO() {
        this.maNV = 0;
        this.ho = "";
        this.ten = "";
        this.ngaySinh = null;
        this.luongCoBan = 0;
        this.chucVu = 0;
    }

    // Getter and Setter
    public int getMaNV() {
        return maNV;
    }

    public void setMaNV(int maNV) {
        this.maNV = maNV;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public Date getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(Date ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public double getLuongCoBan() {
        return luongCoBan;
    }

    public void setLuongCoBan(double luongCoBan) {
        this.luongCoBan = luongCoBan;
    }

    public int getChucVu() {
        return chucVu;
    }

    public void setChucVu(int chucVu) {
        this.chucVu = chucVu;
    }

    // toString
    @Override
    public String toString() {
        return "Mã NV: " + maNV + " | Họ: " + ho + " | Tên: " + ten + 
               " | Ngày sinh: " + ngaySinh + " | Lương cơ bản: " + luongCoBan + 
               " | Chức vụ: " + chucVu;
    }
}
