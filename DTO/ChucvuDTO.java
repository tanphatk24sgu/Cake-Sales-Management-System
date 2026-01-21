package DTO;

public class ChucVuDTO {
    private int maChucVu;
    private String tenChucVu;

    // Constructor
    public ChucVuDTO() {
        this.maChucVu = 0;
        this.tenChucVu = "";
    }

    // Getter and Setter
    public int getMaChucVu() {
        return maChucVu;
    }

    public void setMaChucVu(int maChucVu) {
        this.maChucVu = maChucVu;
    }

    public String getTenChucVu() {
        return tenChucVu;
    }

    public void setTenChucVu(String tenChucVu) {
        this.tenChucVu = tenChucVu;
    }

    // toString
    @Override
    public String toString() {
        return "Mã chức vụ: " + maChucVu + " | Tên chức vụ: " + tenChucVu;
    }
}
