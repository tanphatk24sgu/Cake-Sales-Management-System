package BUS;

import java.util.ArrayList;
import DTO.HoaDonDTO;
import DAO.HoaDonDAO;
import java.sql.Date;

public class HoaDonBUS {
    static ArrayList<HoaDonDTO> dshd;
    public HoaDonBUS() {
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

        if(isMaTonTai(hd.getMaHD()))  return;

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

    public HoaDonDTO timKiemTheoMa(int maHD) {
        for(HoaDonDTO hd : dshd) {
            if(hd.getMaHD() == maHD) {
                return hd;
            }
        }
        return null;
    }

    public HoaDonDTO timKiemTheoNhanVien(int maNV) {
        for(HoaDonDTO hd : dshd) {
            if(hd.getMaNV() == maNV) {
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

    public double thongKeDoanhThuTheoThang(int thang, int nam) {
        if(thang < 1 || thang > 12 || nam < 0)  return 0;
        HoaDonDAO data = new HoaDonDAO();
        return data.thongKeDoanhThuTheoThang(thang, nam);
    }

    public double thongKeDoanhThuTheoQuy(int quy, int nam) {
        if(quy < 1 || quy > 4 || nam < 0)  return 0;
        HoaDonDAO data = new HoaDonDAO();
        return data.thongKeDoanhThuTheoQuy(quy, nam);
    }

    public double thongKeDoanhThuTheoNam(int nam) {
        if(nam < 0)    return 0;
        HoaDonDAO data = new HoaDonDAO();
        return data.thongKeDoanhThuTheoNam(nam);
    }

    public double thongKeDoanhThuTheoKhoangNgay(Date tuNgay, Date denNgay) {
        if(tuNgay == null || denNgay == null || tuNgay.after(denNgay)) {
            return 0;
        }
        HoaDonDAO data = new HoaDonDAO();
        return data.thongKeDoanhThuTheoKhoangNgay(tuNgay, denNgay);
    }
}
