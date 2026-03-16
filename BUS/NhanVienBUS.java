package BUS;
import java.util.ArrayList;
import DTO.NhanVienDTO;
import DAO.NhanVienDAO;

public class NhanVienBUS {
    static ArrayList<NhanVienDTO> dsnv;
    public NhanVienBUS(){
        if(dsnv==null){
            dsnv= new ArrayList<>();
        }
    }
    public void docDSNV(){
        NhanVienDAO data = new NhanVienDAO();
        dsnv=data.docDSNV();
    }
    public boolean isMaTonTai(int maNV) {
        for (NhanVienDTO nv : dsnv) {
            if (nv.getMaNV() == maNV) {
                return true;
            }
        }
        return false;
    }
    public void them(NhanVienDTO nv){
        if(isMaTonTai(nv.getMaNV())) return;

        NhanVienDAO data= new NhanVienDAO();
        data.them(nv);
        dsnv.add(nv);

    }
    public void sua(NhanVienDTO nv){
        NhanVienDAO data = new NhanVienDAO();
        data.sua(nv);
        // Cập nhật trong danh sách
        for (int i = 0; i < dsnv.size(); i++) {
            if (dsnv.get(i).getMaNV() == nv.getMaNV()) {
                dsnv.set(i, nv);
                break;
            }
        }
    }

    public void xoa(int maNV){
        NhanVienDAO data = new NhanVienDAO();
        data.xoa(maNV);
        // Xóa khỏi danh sách
        for (int i = 0; i < dsnv.size(); i++) {
            if (dsnv.get(i).getMaNV() == maNV) {
                dsnv.remove(i);
                break;
            }
        }
    }

    public NhanVienDTO timKiemTheoMa(int maNV) {
        for (NhanVienDTO nv : dsnv) {
            if (nv.getMaNV() == maNV) {
                return nv;
            }
        }
        return null;
    }
    public NhanVienDTO timKiemTheoTen(String tenNV){
        for (NhanVienDTO nv : dsnv){
            if(nv.getTen()==tenNV){
                return nv;
            }
        }
        return null;
    }
}   
