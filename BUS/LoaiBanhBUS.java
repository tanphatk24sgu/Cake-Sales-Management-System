package BUS;

import DAO.LoaiBanhDAO;
import DTO.LoaiBanhDTO;
import java.util.ArrayList;

public class LoaiBanhBUS {

    private ArrayList<LoaiBanhDTO> list;
    private LoaiBanhDAO dao;

    public LoaiBanhBUS() {
        dao = new LoaiBanhDAO();
        list = dao.getAll();
    }

    public ArrayList<LoaiBanhDTO> getList() {
        return list;
    }

    public void loadData() {
        list = dao.getAll();
    }

    public boolean add(LoaiBanhDTO l) {
        boolean result = dao.insert(l);
        if (result) list.add(l);
        return result;
    }

    public boolean update(LoaiBanhDTO l) {
        boolean result = dao.update(l);
        if (result) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getMaLoai() == l.getMaLoai()) {
                    list.set(i, l);
                    break;
                }
            }
        }
        return result;
    }

    public boolean delete(int maLoai) {
        boolean result = dao.delete(maLoai);
        if (result) {
            list.removeIf(x -> x.getMaLoai() == maLoai);
        }
        return result;
    }
}