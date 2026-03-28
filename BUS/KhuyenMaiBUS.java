package BUS;

import DAO.KhuyenMaiDAO;
import DTO.KhuyenMaiDTO;
import java.util.ArrayList;

public class KhuyenMaiBUS {
    private KhuyenMaiDAO dao = new KhuyenMaiDAO();

    public ArrayList<KhuyenMaiDTO> getAll() {
        return dao.getAll();
    }

    public KhuyenMaiDTO getBestByDiem(int diem) {
        ArrayList<KhuyenMaiDTO> list = dao.getAll();
        KhuyenMaiDTO best = null;

        for (KhuyenMaiDTO km : list) {
            if (diem >= km.getDieuKien()) {
                if (best == null || km.getPhanTramGiam() > best.getPhanTramGiam()) {
                    best = km;
                }
            }
        }

        return best;
    }
}