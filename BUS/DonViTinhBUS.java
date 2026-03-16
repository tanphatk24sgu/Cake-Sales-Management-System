package BUS;
import java.util.ArrayList;
import DTO.DonViTinhDTO;
import DAO.DonViTinhDAO;

public class DonViTinhBUS {
    static ArrayList<DonViTinhDTO> dsdvt;
    public DonViTinhBUS(){
        if(dsdvt==null){
            dsdvt= new ArrayList<>();
        }
    }
    public void docDSDVT(){
        DonViTinhDAO data = new DonViTinhDAO();
        dsdvt=data.docDSDVT();
    }
    public boolean isMaTonTai(int maDVT) {
        for (DonViTinhDTO dvt : dsdvt) {
            if (dvt.getMaDVT() == maDVT) {
                return true;
            }
        }
        return false;
    }
    public void them(DonViTinhDTO dvt){
        if(isMaTonTai(dvt.getMaDVT())) return;

        DonViTinhDAO data= new DonViTinhDAO();
        data.them(dvt);
        dsdvt.add(dvt);

    }
    public void sua(DonViTinhDTO dvt){
        DonViTinhDAO data = new DonViTinhDAO();
        data.sua(dvt);
        // Cập nhật trong danh sách
        for (int i = 0; i < dsdvt.size(); i++) {
            if (dsdvt.get(i).getMaDVT() == dvt.getMaDVT()) {
                dsdvt.set(i, dvt);
                break;
            }
        }
    }

    public void xoa(int maDVT){
        DonViTinhDAO data = new DonViTinhDAO();
        data.xoa(maDVT);
        // Xóa khỏi danh sách
        for (int i = 0; i < dsdvt.size(); i++) {
            if (dsdvt.get(i).getMaDVT() == maDVT) {
                dsdvt.remove(i);
                break;
            }
        }
    }

    public DonViTinhDTO timKiem(int maDVT) {
        for (DonViTinhDTO dvt : dsdvt) {
            if (dvt.getMaDVT() == maDVT) {
                return dvt;
            }
        }
        return null;
    }
}