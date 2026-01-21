package DTO;

public class CTChuongTrinhKhuyenMaiDTO {
    private int maKM;
    private int maBanh;
    private int phamTramGiam;

    // Constructor
    public CTChuongTrinhKhuyenMaiDTO() {
        this.maKM = 0;
        this.maBanh = 0;
        this.phamTramGiam = 0;
    }

    // Getter and Setter
    public int getMaKM() {
        return maKM;
    }

    public void setMaKM(int maKM) {
        this.maKM = maKM;
    }

    public int getMaBanh() {
        return maBanh;
    }

    public void setMaBanh(int maBanh) {
        this.maBanh = maBanh;
    }

    public int getPhamTramGiam() {
        return phamTramGiam;
    }

    public void setPhamTramGiam(int phamTramGiam) {
        this.phamTramGiam = phamTramGiam;
    }

    // toString
    @Override
    public String toString() {
        return "Mã KM: " + maKM + " | Mã bánh: " + maBanh + " | Phần trăm giảm: " + phamTramGiam + "%";
    }
}
