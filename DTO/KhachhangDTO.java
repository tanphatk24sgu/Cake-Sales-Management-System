package DTO;

public class KhachHangDTO {
    private int maKH;
    private String ho;
    private String ten;
    private String diaChi;
    private int sdt;

    // Constructor
    public KhachHangDTO() {
        this.maKH = 0;
        this.ho = "";
        this.ten = "";
        this.diaChi = "";
        this.sdt = 0;
    }

    // Getter and Setter
    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
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

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public int getSdt() {
        return sdt;
    }

    public void setSdt(int sdt) {
        this.sdt = sdt;
    }

    // toString
    @Override
    public String toString() {
        return "Mã KH: " + maKH + " | Họ: " + ho + " | Tên: " + ten + 
               " | Địa chỉ: " + diaChi + " | SĐT: " + sdt;
    }
}
