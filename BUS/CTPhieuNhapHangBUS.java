package BUS;

import DAO.CTPhieuNhapHangDAO;
import DTO.CTPhieuNhapHangDTO;

import java.util.ArrayList;

public class CTPhieuNhapHangBUS {
    private static ArrayList<CTPhieuNhapHangDTO> dsctpn;

    public CTPhieuNhapHangBUS() {
        if (dsctpn == null) {
            dsctpn = new ArrayList<>();
        }
    }

    public void docDSCTPN() {
        CTPhieuNhapHangDAO data = new CTPhieuNhapHangDAO();
        dsctpn = data.docDSCTPN();
    }

    public ArrayList<CTPhieuNhapHangDTO> getDSCTPN() {
        return dsctpn;
    }

    public boolean them(CTPhieuNhapHangDTO ct) {
        if (!isChiTietHopLe(ct)) {
            return false;
        }

        ct.setThanhTien(ct.getSoLuong() * ct.getDonGia());

        CTPhieuNhapHangDAO data = new CTPhieuNhapHangDAO();
        boolean ok = data.them(ct);
        if (ok) {
            dsctpn.add(ct);
        }
        return ok;
    }

    public boolean sua(CTPhieuNhapHangDTO ct) {
        if (!isChiTietHopLe(ct)) {
            return false;
        }

        ct.setThanhTien(ct.getSoLuong() * ct.getDonGia());

        CTPhieuNhapHangDAO data = new CTPhieuNhapHangDAO();
        boolean ok = data.sua(ct);
        if (!ok) {
            return false;
        }

        for (int i = 0; i < dsctpn.size(); i++) {
            CTPhieuNhapHangDTO cur = dsctpn.get(i);
            if (cur.getMaPhieuNhap() == ct.getMaPhieuNhap()
                    && cur.getMaBanh() == ct.getMaBanh()
                    && cur.getMaNVL() == ct.getMaNVL()) {
                dsctpn.set(i, ct);
                break;
            }
        }
        return true;
    }

    public boolean xoa(int maPhieuNhap, int maBanh, int maNVL) {
        if (maPhieuNhap <= 0) {
            return false;
        }

        CTPhieuNhapHangDAO data = new CTPhieuNhapHangDAO();
        boolean ok = data.xoa(maPhieuNhap, maBanh, maNVL);
        if (!ok) {
            return false;
        }

        dsctpn.removeIf(ct -> ct.getMaPhieuNhap() == maPhieuNhap
                && ct.getMaBanh() == maBanh
                && ct.getMaNVL() == maNVL);
        return true;
    }

    public ArrayList<CTPhieuNhapHangDTO> timTheoMaPhieuNhap(int maPhieuNhap) {
        ArrayList<CTPhieuNhapHangDTO> kq = new ArrayList<>();
        for (CTPhieuNhapHangDTO ct : dsctpn) {
            if (ct.getMaPhieuNhap() == maPhieuNhap) {
                kq.add(ct);
            }
        }
        return kq;
    }

    private boolean isChiTietHopLe(CTPhieuNhapHangDTO ct) {
        if (ct == null) {
            return false;
        }
        if (ct.getMaPhieuNhap() <= 0 || ct.getSoLuong() < 0 || ct.getDonGia() < 0) {
            return false;
        }
        return ct.getMaBanh() > 0 || ct.getMaNVL() > 0;
    }
}
