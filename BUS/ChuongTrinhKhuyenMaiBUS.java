package BUS;

import DAO.ChuongTrinhKhuyenMaiDAO;
import DTO.ChuongTrinhKhuyenMaiDTO;
import java.util.ArrayList;

// Nghiệp vụ chương trình khuyến mãi — gọi DAO, giữ danh sách trong bộ nhớ
public class ChuongTrinhKhuyenMaiBUS {

    private ArrayList<ChuongTrinhKhuyenMaiDTO> list;
    private final ChuongTrinhKhuyenMaiDAO dao;

    public ChuongTrinhKhuyenMaiBUS() {
        dao = new ChuongTrinhKhuyenMaiDAO();
        list = dao.getAll();
    }

    public ArrayList<ChuongTrinhKhuyenMaiDTO> getList() {
        return list;
    }

    public void loadData() {
        list = dao.getAll();
    }

    public int add(ChuongTrinhKhuyenMaiDTO dto) {
        int newId = dao.insert(dto);
        if (newId > 0) {
            loadData();
        }
        return newId;
    }

    public boolean update(ChuongTrinhKhuyenMaiDTO dto) {
        boolean ok = dao.update(dto);
        if (ok) {
            loadData();
        }
        return ok;
    }

    public boolean delete(int maKM) {
        boolean ok = dao.delete(maKM);
        if (ok) {
            loadData();
        }
        return ok;
    }
}
