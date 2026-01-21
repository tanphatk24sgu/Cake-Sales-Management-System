package DTO;

public class TichDiemDTO {
    private int maKH;
    private int tichDiem;

    // Constructor
    public TichDiemDTO() {
        this.maKH = 0;
        this.tichDiem = 0;
    }

    // Getter and Setter
    public int getMaKH() {
        return maKH;
    }

    public void setMaKH(int maKH) {
        this.maKH = maKH;
    }

    public int getTichDiem() {
        return tichDiem;
    }

    public void setTichDiem(int tichDiem) {
        this.tichDiem = tichDiem;
    }

    // toString
    @Override
    public String toString() {
        return "Mã KH: " + maKH + " | Tích điểm: " + tichDiem;
    }
}
