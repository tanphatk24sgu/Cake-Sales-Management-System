package DTO;

import java.util.Date;

// DTO chương trình khuyến mãi — khớp bảng chuongtrinhkhuyenmai (cột mở rộng cho UI)
public class ChuongTrinhKhuyenMaiDTO {
    private int maKM;
    private String tenCTKM;
    private Date ngayBatDau;
    private Date ngayKetThuc;
    private String ghiChu;
    private String loaiKhuyenMai;
    private int phanTramGiam;
    private double dieuKienToiThieu;
    private int soLuongMua;
    private int soLuongTang;
    private int maBanhMua;
    private int maBanhTang;
    private String tenBanhMua;
    private String tenBanhTang;

    public ChuongTrinhKhuyenMaiDTO() {
        this.maKM = 0;
        this.tenCTKM = "";
        this.ngayBatDau = new Date();
        this.ngayKetThuc = new Date();
        this.ghiChu = "";
        this.loaiKhuyenMai = "Giảm phần trăm";
        this.phanTramGiam = 0;
        this.dieuKienToiThieu = 0;
        this.soLuongMua = 0;
        this.soLuongTang = 0;
        this.maBanhMua = 0;
        this.maBanhTang = 0;
        this.tenBanhMua = "";
        this.tenBanhTang = "";
    }

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

    public String getLoaiKhuyenMai() {
        return loaiKhuyenMai;
    }

    public void setLoaiKhuyenMai(String loaiKhuyenMai) {
        this.loaiKhuyenMai = loaiKhuyenMai;
    }

    public int getPhanTramGiam() {
        return phanTramGiam;
    }

    public void setPhanTramGiam(int phanTramGiam) {
        this.phanTramGiam = phanTramGiam;
    }

    public double getDieuKienToiThieu() {
        return dieuKienToiThieu;
    }

    public void setDieuKienToiThieu(double dieuKienToiThieu) {
        this.dieuKienToiThieu = dieuKienToiThieu;
    }

    public int getSoLuongMua() {
        return soLuongMua;
    }

    public void setSoLuongMua(int soLuongMua) {
        this.soLuongMua = soLuongMua;
    }

    public int getSoLuongTang() {
        return soLuongTang;
    }

    public void setSoLuongTang(int soLuongTang) {
        this.soLuongTang = soLuongTang;
    }

    public int getMaBanhMua() {
        return maBanhMua;
    }

    public void setMaBanhMua(int maBanhMua) {
        this.maBanhMua = maBanhMua;
    }

    public int getMaBanhTang() {
        return maBanhTang;
    }

    public void setMaBanhTang(int maBanhTang) {
        this.maBanhTang = maBanhTang;
    }

    public String getTenBanhMua() {
        return tenBanhMua;
    }

    public void setTenBanhMua(String tenBanhMua) {
        this.tenBanhMua = tenBanhMua;
    }

    public String getTenBanhTang() {
        return tenBanhTang;
    }

    public void setTenBanhTang(String tenBanhTang) {
        this.tenBanhTang = tenBanhTang;
    }

    @Override
    public String toString() {
        return "Mã KM: " + maKM + " | Tên CTKM: " + tenCTKM + " | Ngày bắt đầu: " + ngayBatDau
                + " | Ngày kết thúc: " + ngayKetThuc + " | Ghi chú: " + ghiChu;
    }
}
