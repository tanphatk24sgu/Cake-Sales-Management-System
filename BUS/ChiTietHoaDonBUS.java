package BUS;

import java.util.ArrayList;
import DTO.ChiTietHoaDonDTO;
import DAO.ChiTietHoaDonDAO;

public class ChiTietHoaDonBUS {
    static ArrayList<ChiTietHoaDonDTO> dscthd;
    ChiTietHoaDonBUS() {
        if (dscthd == null) {
            dscthd = new ArrayList<>();
        }
    }

    public void docDSCTHD() {
        ChiTietHoaDonDAO data = new ChiTietHoaDonDAO();
        dscthd = data.docDSCTHD();
    }

    public void them(ChiTietHoaDonDTO cthd) {
        // TODO Kiểm tra dữ liệu hợp lệ

        // TODO Kiểm tra mã duy nhất

        if(!isMaTonTai(cthd.getMaHD())) return;

        ChiTietHoaDonDAO data = new ChiTietHoaDonDAO();
        data.them(cthd);
        dscthd.add(cthd);
    }

    public void xoa(int maHD, int maBanh) {
        ChiTietHoaDonDAO data = new ChiTietHoaDonDAO();
        data.xoa(maHD, maBanh);

        for(int i = 0;i < dscthd.size();i++) {
            if(dscthd.get(i).getMaHD() == maHD && dscthd.get(i).getMaBanh() == maBanh) {
                dscthd.remove(i);
                break;
            }
        }
    }

    public void sua(ChiTietHoaDonDTO cthd) {
        ChiTietHoaDonDAO data = new ChiTietHoaDonDAO();
        data.sua(cthd);

        for(int i = 0;i < dscthd.size();i++) {
            if(dscthd.get(i).getMaHD() == cthd.getMaHD() && dscthd.get(i).getMaBanh() == cthd.getMaBanh()) {
                dscthd.set(i, cthd);
                break;
            }
        }
    }

    public ChiTietHoaDonDTO timKiem(int maHD, int maBanh) {
        for(ChiTietHoaDonDTO cthd : dscthd) {
            if(cthd.getMaHD() == maHD && cthd.getMaBanh() == maBanh) {
                return cthd;
            }
        }
        return null;
    }

    public boolean isMaTonTai(int maHD) {
        for(ChiTietHoaDonDTO cthd : dscthd) {
            if(cthd.getMaHD() == maHD) {
                return true;
            }
        }
        return false;
    }
}
