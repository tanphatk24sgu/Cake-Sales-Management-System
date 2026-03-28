package BUS;

import DAO.CongThucDAO;
import DTO.CongThucDTO;

public class CongThucBUS {
    private CongThucDAO dao = new CongThucDAO();

    public CongThucDTO getByMaBanh(int maBanh) {
        return dao.getByMaBanh(maBanh);
    }

    public boolean save(CongThucDTO ct) {
        if (ct.getCachLam().trim().isEmpty())
            return false;

        CongThucDTO check = dao.getByMaBanh(ct.getMaBanh());
        if (check != null) {
            return dao.update(ct);
        } else {
            return dao.insert(ct);
        }
    }

    public boolean delete(int maBanh) {
        if (maBanh <= 0) {
            return false;
        }
        return dao.delete(maBanh);
    }
}