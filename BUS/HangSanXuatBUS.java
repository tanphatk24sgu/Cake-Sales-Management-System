package BUS;

import DAO.HangSanXuatDAO;
import DTO.HangSanXuatDTO;
import java.util.ArrayList;

public class HangSanXuatBUS {

    private ArrayList<HangSanXuatDTO> list;
    private HangSanXuatDAO dao;

    public HangSanXuatBUS() {
        dao = new HangSanXuatDAO();
        list = dao.getAll();
    }

    public ArrayList<HangSanXuatDTO> getList() {
        return list;
    }

    public void loadData() {
        list = dao.getAll();
    }

    public boolean add(HangSanXuatDTO h) {
        boolean result = dao.insert(h);
        if (result) list.add(h);
        return result;
    }

    public boolean update(HangSanXuatDTO h) {
        boolean result = dao.update(h);
        if (result) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getMaHang() == h.getMaHang()) {
                    list.set(i, h);
                    break;
                }
            }
        }
        return result;
    }

    public boolean delete(int maHang) {
        boolean result = dao.delete(maHang);
        if (result) {
            list.removeIf(x -> x.getMaHang() == maHang);
        }
        return result;
    }
}