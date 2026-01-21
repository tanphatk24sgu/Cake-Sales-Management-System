package DTO;

import java.util.Date;

public class ChuongTrinhKhuyenMaiDTO {
    private int maKM;
    private String tenCTKM;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String ghiChu;

    // Constructor
    public ChuongTrinhKhuyenMaiDTO() {
        this.maKM = 0;
        this.tenCTKM = "";
        this.ngayBatDau = new Date();
        this.ngayKetThuc = new Date();
        this.ghiChu = "";
    }

    // Getter and Setter
    public int getMaKM() {
        return maKM;
    }

    public void setMaKM(int maKM) {
        this.maKM = maKM;
    }

    public String getTenCTKM() {
        return tenCTKM;
    }

    public void setTenCTKM(String tenCTKM) {
        this.tenCTKM = tenCTKM;
    }

    public Date getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(Date ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public Date getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(Date ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    // toString
    @Override
    public String toString() {
        return "Mã KM: " + maKM + " | Tên CTKM: " + tenCTKM + " | Ngày bắt đầu: " + ngayBatDau + 
               " | Ngày kết thúc: " + ngayKetThuc + " | Ghi chú: " + ghiChu;
    }
}
