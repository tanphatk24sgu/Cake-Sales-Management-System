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

    // ===== THÊM HÀM NÀY =====
    public int tinhDiemThuong(double thanhTien) {
        if (thanhTien <= 0)
            return 0;
        return (int) (thanhTien / 100000);
    }

    public void them(ChiTietHoaDonDTO cthd) {

        if (!isMaTonTai(cthd.getMaHD()))
            return;

        // ===== THÊM LOGIC Ở ĐÂY =====

        // 1. Tính thành tiền
        double thanhTien = cthd.getSoLuong() * cthd.getDonGia();
        cthd.setThanhTien(thanhTien);

        // 2. Tính điểm thưởng
        int diem = tinhDiemThuong(thanhTien);
        cthd.setDiem(diem);

        // 3. Lưu DB
        ChiTietHoaDonDAO data = new ChiTietHoaDonDAO();
        data.them(cthd);

        // 4. Lưu RAM
        dscthd.add(cthd);
    }

    public void xoa(int maHD, int maBanh) {
        ChiTietHoaDonDAO data = new ChiTietHoaDonDAO();
        data.xoa(maHD, maBanh);

        for (int i = 0; i < dscthd.size(); i++) {
            if (dscthd.get(i).getMaHD() == maHD && dscthd.get(i).getMaBanh() == maBanh) {
                dscthd.remove(i);
                break;
            }
        }
    }

    public void sua(ChiTietHoaDonDTO cthd) {
        ChiTietHoaDonDAO data = new ChiTietHoaDonDAO();
        data.sua(cthd);

        for (int i = 0; i < dscthd.size(); i++) {
            if (dscthd.get(i).getMaHD() == cthd.getMaHD() && dscthd.get(i).getMaBanh() == cthd.getMaBanh()) {
                dscthd.set(i, cthd);
                break;
            }
        }
    }

    public ChiTietHoaDonDTO timKiem(int maHD, int maBanh) {
        for (ChiTietHoaDonDTO cthd : dscthd) {
            if (cthd.getMaHD() == maHD && cthd.getMaBanh() == maBanh) {
                return cthd;
            }
        }
        return null;
    }

    public boolean isMaTonTai(int maHD) {
        for (ChiTietHoaDonDTO cthd : dscthd) {
            if (cthd.getMaHD() == maHD) {
                return true;
            }
        }
        return false;
    }

    public int thongKeSoLuongBan(int maBanh) {
        if (maBanh <= 0)
            return 0;
        ChiTietHoaDonDAO data = new ChiTietHoaDonDAO();
        return data.thongKeSoLuongBan(maBanh);
    }
}