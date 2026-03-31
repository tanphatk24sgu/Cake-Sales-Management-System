package BUS;

import DAO.PhieuNhapHangDAO;
import DTO.PhieuNhapHangDTO;

import java.util.ArrayList;

public class PhieuNhapHangBUS {
    private static ArrayList<PhieuNhapHangDTO> dspn;

    public PhieuNhapHangBUS() {
        if (dspn == null) {
            dspn = new ArrayList<>();
        }
    }

    public void docDSPN() {
        PhieuNhapHangDAO data = new PhieuNhapHangDAO();
        dspn = data.docDSPN();
    }

    public ArrayList<PhieuNhapHangDTO> getDSPN() {
        return dspn;
    }

    public boolean them(PhieuNhapHangDTO pn) {
        if (pn == null || pn.getMaNV() <= 0 || pn.getMaNCC() <= 0 || pn.getNgay() == null) {
            return false;
        }

        PhieuNhapHangDAO data = new PhieuNhapHangDAO();
        int maMoi = data.them(pn);
        if (maMoi > 0) {
            pn.setMaPhieuNhap(maMoi);
            dspn.add(pn);
            return true;
        }
        return false;
    }

    public boolean sua(PhieuNhapHangDTO pn) {
        if (pn == null || pn.getMaPhieuNhap() <= 0 || pn.getMaNV() <= 0 || pn.getMaNCC() <= 0 || pn.getNgay() == null) {
            return false;
        }

        PhieuNhapHangDAO data = new PhieuNhapHangDAO();
        boolean ok = data.sua(pn);
        if (!ok) {
            return false;
        }

        for (int i = 0; i < dspn.size(); i++) {
            if (dspn.get(i).getMaPhieuNhap() == pn.getMaPhieuNhap()) {
                dspn.set(i, pn);
                break;
            }
        }

        return true;
    }

    public boolean xoa(int maPhieuNhap) {
        if (maPhieuNhap <= 0) {
            return false;
        }

        PhieuNhapHangDAO data = new PhieuNhapHangDAO();
        boolean ok = data.xoa(maPhieuNhap);
        if (!ok) {
            return false;
        }

        dspn.removeIf(pn -> pn.getMaPhieuNhap() == maPhieuNhap);
        return true;
    }

    public PhieuNhapHangDTO timKiemTheoMa(int maPhieuNhap) {
        for (PhieuNhapHangDTO pn : dspn) {
            if (pn.getMaPhieuNhap() == maPhieuNhap) {
                return pn;
            }
        }
        return null;
    }

    public boolean isMaTonTai(int maPhieuNhap) {
        return timKiemTheoMa(maPhieuNhap) != null;
    }
}
