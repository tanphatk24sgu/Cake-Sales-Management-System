package BUS;

import DAO.NhaCungCapDAO;
import DTO.NhaCungCapDTO;
import java.util.ArrayList;

public class NhaCungCapBUS {

    private NhaCungCapDAO dao = new NhaCungCapDAO();

    public ArrayList<NhaCungCapDTO> getList() {
        return dao.getAll(); // luôn load mới từ DB
    }

    public boolean add(NhaCungCapDTO ncc) {
        if (ncc.getTenNCC().trim().isEmpty())
            return false;
        return dao.insert(ncc);
    }

    public boolean update(NhaCungCapDTO ncc) {
        return dao.update(ncc);
    }

    public boolean delete(int ma) {
        return dao.delete(ma);
    }
}