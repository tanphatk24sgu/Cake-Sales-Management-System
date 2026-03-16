package BUS;
import java.util.ArrayList;
import DTO.ChucVuDTO;
import DAO.ChucVuDAO;

public class ChucVuBUS {
    static ArrayList<ChucVuDTO> dscv;
    public ChucVuBUS(){
        if(dscv==null){
            dscv = new ArrayList<>();
        }
    }
    public boolean isMaTonTai(int maChucVu){
        for(ChucVuDTO cv:dscv){
            if(cv.getMaChucVu()== maChucVu){
                return true;
            }
        }
        return false;
    }
    public void them(ChucVuDTO cv){
        if(isMaTonTai(cv.getMaChucVu())) return;
        ChucVuDAO data= new ChucVuDAO();
        data.them(cv);
        dscv.add(cv);

    }
    public void docDSCV(){
        ChucVuDAO data= new ChucVuDAO();
        dscv =data.docDSCV();

    }
    public void sua(ChucVuDTO cv){
        ChucVuDAO data = new ChucVuDAO();
        data.sua(cv);
        // Cập nhật trong danh sách
        for (int i = 0; i < dscv.size(); i++) {
            if (dscv.get(i).getMaChucVu() == cv.getMaChucVu()) {
                dscv.set(i, cv);
                break;
            }
        }
    }

    public void xoa(int maChucVu){
        ChucVuDAO data = new ChucVuDAO();
        data.xoa(maChucVu);
        // Xóa khỏi danh sách
        for (int i = 0; i < dscv.size(); i++) {
            if (dscv.get(i).getMaChucVu() == maChucVu) {
                dscv.remove(i);
                break;
            }
        }
    }
    public ChucVuDTO timKiem(int maChucVu){
        for(ChucVuDTO cv:dscv){
            if(cv.getMaChucVu()==maChucVu){
                return cv;
            }
        }
        return null;
    }
}
