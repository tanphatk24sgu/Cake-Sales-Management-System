package DTO;

public class BanhDTO {
    private int maBanh;
    private String tenBanh;
    private int soLuong;
    private int maDVT;
    private int maLoai;
    private int maHang;

    // Constructor
    public BanhDTO() {
        this.maBanh = 0;
        this.tenBanh = "";
        this.soLuong = 0;
        this.maDVT = 0;
        this.maLoai = 0;
        this.maHang = 0;
    }

    // Getter and Setter
    public int getMaBanh() {
        return maBanh;
    }

    public void setMaBanh(int maBanh) {
        this.maBanh = maBanh;
    }

    public String getTenBanh() {
        return tenBanh;
    }

    public void setTenBanh(String tenBanh) {
        this.tenBanh = tenBanh;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getMaDVT() {
        return maDVT;
    }

    public void setMaDVT(int maDVT) {
        this.maDVT = maDVT;
    }

    public int getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(int maLoai) {
        this.maLoai = maLoai;
    }

    public int getMaHang() {
        return maHang;
    }

    public void setMaHang(int maHang) {
        this.maHang = maHang;
    }

    // toString
    @Override
    public String toString() {
        return "Mã bánh: " + maBanh + " | Tên bánh: " + tenBanh + " | Số lượng: " + soLuong + 
               " | Mã DVT: " + maDVT + " | Mã loại: " + maLoai + " | Mã hãng: " + maHang;
    }
}
