package BUS;

import java.util.ArrayList;
import DTO.HoaDonDTO;
import DAO.HoaDonDAO;

public class HoaDonBUS {
    static ArrayList<HoaDonDTO> dshd;
    HoaDonBUS() {
        if (dshd == null) {
            dshd = new ArrayList<>();
        }
    }

    public void docDSHD() {
        HoaDonDAO data = new HoaDonDAO();
        dshd = data.docDSHD();
    }

    public void them(HoaDonDTO hd) {
        // TODO Kiểm tra dữ liệu hợp lệ

        // TODO Kiểm tra mã duy nhất

        if(!isMaTonTai(hd.getMaHD()))  return;

        HoaDonDAO data = new HoaDonDAO();
        data.them(hd);
        dshd.add(hd);
    }

    public void xoa(int maHD) {
        HoaDonDAO data = new HoaDonDAO();
        data.xoa(maHD);

        for(int i = 0;i < dshd.size();i++) {
            if(dshd.get(i).getMaHD() == maHD) {
                dshd.remove(i);
                break;
            }
        }
    }

    public void sua(HoaDonDTO hd) {
        HoaDonDAO data = new HoaDonDAO();
        data.sua(hd);

        for(int i = 0;i < dshd.size();i++) {
            if(dshd.get(i).getMaHD() == hd.getMaHD()) {
                dshd.set(i, hd);
                break;
            }
        }
    }

    public HoaDonDTO timKiem(int maHD) {
        for(HoaDonDTO hd : dshd) {
            if(hd.getMaHD() == maHD) {
                return hd;
            }
        }
        return null;
    }

    public boolean isMaTonTai(int maHD) {
        for(HoaDonDTO hd : dshd) {
            if(hd.getMaHD() == maHD) {
                return true;
            }
        }
        return false;
    }
}
