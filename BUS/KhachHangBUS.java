package BUS;

import DAO.KhachHangDAO;
import DTO.KhachHangDTO;
import java.util.ArrayList;

public class KhachHangBUS {

    private final KhachHangDAO dao = new KhachHangDAO();

    public ArrayList<KhachHangDTO> getList() {
        return dao.getAll();
    }

    public boolean add(KhachHangDTO kh) {
        if (!isValid(kh)) {
            return false;
        }
        return dao.insert(kh);
    }

    public boolean update(KhachHangDTO kh) {
        if (kh == null || kh.getMaKH() <= 0 || !isValid(kh)) {
            return false;
        }
        return dao.update(kh);
    }

    public boolean delete(int maKH) {
        if (maKH <= 0) {
            return false;
        }
        return dao.delete(maKH);
    }

    public KhachHangDTO findByPhone(String sdt) {
        if (sdt == null || sdt.trim().isEmpty()) {
            return null;
        }
        return dao.findByPhone(sdt.trim());
    }

    private boolean isValid(KhachHangDTO kh) {
        if (kh == null) {
            return false;
        }
        if (kh.getHo() == null || kh.getHo().trim().isEmpty()) {
            return false;
        }
        if (kh.getTen() == null || kh.getTen().trim().isEmpty()) {
            return false;
        }
        if (kh.getSdt() == null || kh.getSdt().trim().isEmpty()) {
            return false;
        }
        return true;
    }
}
