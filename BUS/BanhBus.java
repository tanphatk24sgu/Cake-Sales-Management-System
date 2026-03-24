package BUS;

import DAO.BanhDAO;
import DTO.BanhDTO;
import java.util.ArrayList;

public class BanhBus {

    private ArrayList<BanhDTO> list;
    private BanhDAO dao;

    public BanhBus() {
        dao = new BanhDAO();
        list = dao.getAll();
    }

    public ArrayList<BanhDTO> getList() {
        return list;
    }

    public void loadData() {
        list = dao.getAll();
    }

    public boolean add(BanhDTO b) {
        boolean result = dao.insert(b);
        if (result) list.add(b);
        return result;
    }

    public boolean update(BanhDTO b) {
        boolean result = dao.update(b);
        if (result) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getMaBanh() == b.getMaBanh()) {
                    list.set(i, b);
                    break;
                }
            }
        }
        return result;
    }

    public boolean delete(int maBanh) {
        boolean result = dao.delete(maBanh);
        if (result) {
            list.removeIf(b -> b.getMaBanh() == maBanh);
        }
        return result;
    }

    public BanhDTO searchByID(int maBanh) {
        for (BanhDTO b : list) {
            if (b.getMaBanh() == maBanh) return b;
        }
        return null;
    }
}