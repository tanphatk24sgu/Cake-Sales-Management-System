package DTO;

public class DonViTinhDTO {
    private int maDVT;
    private String ten;

    // Constructor 
    public DonViTinhDTO()
    {
        this.maDVT=0;
        this.ten="";
    }


    // getter and setter
    public int getMaDVT()
    {
        return maDVT;
    }

    public void setMaDVT(int maDVT)
    {
        this.maDVT=maDVT;
    }

    public String getTen()
    {
        return ten;
    }
    public void setTen(String ten)
    {
        this.ten=ten;
    }

    // toString 
    @Override
    public String toString()
    {
        return "Mã Đơn Vị Tính : "+maDVT+" | Tên : "+ten;
    }
}
