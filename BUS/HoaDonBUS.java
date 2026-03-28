package BUS;

import java.util.ArrayList;
import DTO.HoaDonDTO;
import DTO.ChiTietHoaDonDTO;
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
    
    public ArrayList<HoaDonDTO> getDSHD() {
        return dshd;
    }

    public void them(HoaDonDTO hd) {
        HoaDonDAO data = new HoaDonDAO();
        int maMoi = data.them(hd);
        if (maMoi > 0) {
            hd.setMaHD(maMoi);
            dshd.add(hd);
        }
    }

    public int themKemChiTiet(HoaDonDTO hd, ArrayList<ChiTietHoaDonDTO> dsChiTiet) throws Exception {
        HoaDonDAO data = new HoaDonDAO();
        int maMoi = data.themHoaDonKemChiTiet(hd, dsChiTiet);
        if (maMoi > 0) {
            hd.setMaHD(maMoi);
            dshd.add(hd);
        }
        return maMoi;
    }

    public void xoa(int maHD) {
        HoaDonDAO data = new HoaDonDAO();
        boolean ok = data.xoa(maHD);
        if (!ok) {
            return;
        }

        for(int i = 0;i < dshd.size();i++) {
            if(dshd.get(i).getMaHD() == maHD) {
                dshd.remove(i);
                break;
            }
        }
    }

    public void sua(HoaDonDTO hd) {
        HoaDonDAO data = new HoaDonDAO();
        boolean ok = data.sua(hd);
        if (!ok) {
            return;
        }

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
