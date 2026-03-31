package BUS;

import DAO.KhuyenMaiDAO;
import DTO.KhuyenMaiDTO;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class KhuyenMaiBUS {
    private KhuyenMaiDAO dao = new KhuyenMaiDAO();

    // Lấy tất cả khuyến mãi
    public ArrayList<KhuyenMaiDTO> getAll() {
        return dao.getAll();
    }

    // Lấy tất cả khuyến mãi phù hợp với điểm, sắp xếp giảm dần theo % giảm
    public List<KhuyenMaiDTO> getAllByDiem(int diem) {
        ArrayList<KhuyenMaiDTO> list = dao.getAll();
        List<KhuyenMaiDTO> res = new ArrayList<>();

        for (KhuyenMaiDTO km : list) {
            if (diem >= km.getDieuKien()) {
                res.add(km);
            }
        }

        // Sắp xếp giảm dần theo % giảm
        Collections.sort(res, new Comparator<KhuyenMaiDTO>() {
            @Override
            public int compare(KhuyenMaiDTO o1, KhuyenMaiDTO o2) {
                return Integer.compare(o2.getPhanTramGiam(), o1.getPhanTramGiam());
            }
        });

        return res;
    }

    // Lấy 1 KM tốt nhất theo điểm (nếu cần giữ hàm cũ)
    public KhuyenMaiDTO getBestByDiem(int diem) {
        List<KhuyenMaiDTO> list = getAllByDiem(diem);
        return list.isEmpty() ? null : list.get(0);
    }
}