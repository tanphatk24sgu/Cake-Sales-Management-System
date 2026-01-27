package DTO;

public class NhaCungCapDTO {
    private int maNCC;
    private String tenNCC;
    private String maSoThue;

    // Constructor
    public NhaCungCapDTO() {
        this.maNCC = 0;
        this.tenNCC = "";
        this.maSoThue = "";
    }

    // Getter and Setter
    public int getMaNCC() {
        return maNCC;
    }

    public void setMaNCC(int maNCC) {
        this.maNCC = maNCC;
    }

    public String getTenNCC() {
        return tenNCC;
    }

    public void setTenNCC(String tenNCC) {
        this.tenNCC = tenNCC;
    }

    public String getMaSoThue() {
        return maSoThue;
    }

    public void setMaSoThue(String maSoThue) {
        this.maSoThue = maSoThue;
    }

    // toString
    @Override
    public String toString() {
        return "Mã NCC: " + maNCC + " | Tên NCC: " + tenNCC + " | Mã số thuế: " + maSoThue;
    }
}
