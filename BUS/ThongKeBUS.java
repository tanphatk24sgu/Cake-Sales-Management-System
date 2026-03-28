package BUS;

import java.sql.Date;
import java.util.ArrayList;

import DAO.ThongKeDAO;

public class ThongKeBUS {
    private final ThongKeDAO dao;

    public ThongKeBUS() {
        dao = new ThongKeDAO();
    }

    public ArrayList<Object[]> thongKeDoanhThu(Date tuNgay, Date denNgay) {
        if (!isValidRange(tuNgay, denNgay)) {
            return new ArrayList<>();
        }
        return dao.thongKeDoanhThu(tuNgay, denNgay);
    }

    public ArrayList<Object[]> thongKeThuNhap(Date tuNgay, Date denNgay) {
        if (!isValidRange(tuNgay, denNgay)) {
            return new ArrayList<>();
        }
        return dao.thongKeThuNhap(tuNgay, denNgay);
    }

    public ArrayList<Object[]> thongKeSoLuongNhap(Date tuNgay, Date denNgay) {
        if (!isValidRange(tuNgay, denNgay)) {
            return new ArrayList<>();
        }
        return dao.thongKeSoLuongNhap(tuNgay, denNgay);
    }

    public ArrayList<Object[]> thongKeSoLuongBan(Date tuNgay, Date denNgay) {
        if (!isValidRange(tuNgay, denNgay)) {
            return new ArrayList<>();
        }
        return dao.thongKeSoLuongBan(tuNgay, denNgay);
    }

    private boolean isValidRange(Date tuNgay, Date denNgay) {
        return tuNgay != null && denNgay != null && !tuNgay.after(denNgay);
    }
}
