package DTO;

public class HangSanXuatDTO {
    private int maHang;
    private String tenHang;
    private String diaChi;

    // constructor
    public HangSanXuatDTO()
    {
        this.maHang=0;
        this.tenHang="";
        this.diaChi="";
    }

    // getter and setter
    public int getMaHang()
    {
        return maHang;
    }
    public void setMaHang(int maHang)
    {
        this.maHang=maHang;
    }
    public String getTenHang()
    {
        return tenHang;
    }
    public void setTenHang(String tenHang)
    {
        this.tenHang=tenHang;
    }
    public String getDiaChi()
    {
        return diaChi;
    }
    public void setDiaChi(String diaChi)
    {
        this.diaChi=diaChi;
    }

    //to String
    @Override
    public String toString()
    {
        return "Mã hãng : "+ maHang+" | Tên hãng : "+tenHang+" | Địa chỉ : "+diaChi;
    }
}
