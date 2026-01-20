package DTO;

public class LoaibanhDTO {
    private int maLoai;
    private String tenLoai;

    // Constructor
    public LoaibanhDTO() {
        this.maLoai = 0;
        this.tenLoai = "";
    }

    // Getter and Setter
    public int getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(int maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    // toString
    @Override
    public String toString() {
        return "Mã loại: " + maLoai + " | Tên loại: " + tenLoai;
    }
}
