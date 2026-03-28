package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import Database.ConnectDatabase;

public class ThongKeDAO {

    public ArrayList<Object[]> thongKeDoanhThu(Date tuNgay, Date denNgay) {
        ArrayList<Object[]> rows = new ArrayList<>();
        String sql = "SELECT DATE(h.NgayLapHD) AS Ngay, COUNT(*) AS SoHoaDon, IFNULL(SUM(h.ThanhTien), 0) AS DoanhThu "
                + "FROM hoadon h "
                + "WHERE DATE(h.NgayLapHD) BETWEEN ? AND ? "
                + "GROUP BY DATE(h.NgayLapHD) "
                + "ORDER BY DATE(h.NgayLapHD)";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, tuNgay);
            ps.setDate(2, denNgay);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[] {
                            rs.getDate("Ngay"),
                            rs.getInt("SoHoaDon"),
                            rs.getDouble("DoanhThu")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

    public ArrayList<Object[]> thongKeThuNhap(Date tuNgay, Date denNgay) {
        ArrayList<Object[]> rows = new ArrayList<>();
        String sql = "SELECT t.Ngay, t.DoanhThu, t.ChiPhiNhap, (t.DoanhThu - t.ChiPhiNhap) AS ThuNhap "
                + "FROM ( "
                + "    SELECT d.Ngay, "
                + "           IFNULL(dt.DoanhThu, 0) AS DoanhThu, "
                + "           IFNULL(cp.ChiPhiNhap, 0) AS ChiPhiNhap "
                + "    FROM ( "
                + "        SELECT DATE(?) + INTERVAL seq.i DAY AS Ngay "
                + "        FROM ( "
                + "            SELECT a.i + b.i * 10 + c.i * 100 AS i "
                + "            FROM (SELECT 0 i UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a "
                + "            CROSS JOIN (SELECT 0 i UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b "
                + "            CROSS JOIN (SELECT 0 i UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) c "
                + "        ) seq "
                + "        WHERE DATE(?) + INTERVAL seq.i DAY <= DATE(?) "
                + "    ) d "
                + "    LEFT JOIN ( "
                + "        SELECT DATE(NgayLapHD) AS Ngay, SUM(ThanhTien) AS DoanhThu "
                + "        FROM hoadon "
                + "        WHERE DATE(NgayLapHD) BETWEEN ? AND ? "
                + "        GROUP BY DATE(NgayLapHD) "
                + "    ) dt ON d.Ngay = dt.Ngay "
                + "    LEFT JOIN ( "
                + "        SELECT DATE(p.Ngay) AS Ngay, SUM(c.ThanhTien) AS ChiPhiNhap "
                + "        FROM phieunhaphang p "
                + "        JOIN ct_phieunhaphang c ON p.MaPhieuNhap = c.MaPhieuNhap "
                + "        WHERE DATE(p.Ngay) BETWEEN ? AND ? "
                + "        GROUP BY DATE(p.Ngay) "
                + "    ) cp ON d.Ngay = cp.Ngay "
                + ") t "
                + "ORDER BY t.Ngay";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, tuNgay);
            ps.setDate(2, tuNgay);
            ps.setDate(3, denNgay);
            ps.setDate(4, tuNgay);
            ps.setDate(5, denNgay);
            ps.setDate(6, tuNgay);
            ps.setDate(7, denNgay);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[] {
                            rs.getDate("Ngay"),
                            rs.getDouble("DoanhThu"),
                            rs.getDouble("ChiPhiNhap"),
                            rs.getDouble("ThuNhap")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

    public ArrayList<Object[]> thongKeSoLuongNhap(Date tuNgay, Date denNgay) {
        ArrayList<Object[]> rows = new ArrayList<>();
        String sql = "SELECT c.MaBanh, IFNULL(b.TenBanh, '') AS TenBanh, "
                + "       IFNULL(SUM(c.SoLuong), 0) AS TongSoLuongNhap, "
                + "       IFNULL(SUM(c.ThanhTien), 0) AS TongGiaTriNhap "
                + "FROM phieunhaphang p "
                + "JOIN ct_phieunhaphang c ON p.MaPhieuNhap = c.MaPhieuNhap "
                + "LEFT JOIN banh b ON c.MaBanh = b.MaBanh "
                + "WHERE DATE(p.Ngay) BETWEEN ? AND ? "
                + "GROUP BY c.MaBanh, b.TenBanh "
                + "ORDER BY TongSoLuongNhap DESC, c.MaBanh";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, tuNgay);
            ps.setDate(2, denNgay);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[] {
                            rs.getInt("MaBanh"),
                            rs.getString("TenBanh"),
                            rs.getInt("TongSoLuongNhap"),
                            rs.getDouble("TongGiaTriNhap")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

    public ArrayList<Object[]> thongKeSoLuongBan(Date tuNgay, Date denNgay) {
        ArrayList<Object[]> rows = new ArrayList<>();
        String sql = "SELECT c.MaBanh, IFNULL(b.TenBanh, '') AS TenBanh, "
                + "       IFNULL(SUM(c.SoLuong), 0) AS TongSoLuongBan, "
                + "       IFNULL(SUM(c.ThanhTien), 0) AS TongDoanhThuBan "
                + "FROM hoadon h "
                + "JOIN chitiethoadon c ON h.MaHD = c.MaHD "
                + "LEFT JOIN banh b ON c.MaBanh = b.MaBanh "
                + "WHERE DATE(h.NgayLapHD) BETWEEN ? AND ? "
                + "GROUP BY c.MaBanh, b.TenBanh "
                + "ORDER BY TongSoLuongBan DESC, c.MaBanh";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, tuNgay);
            ps.setDate(2, denNgay);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[] {
                            rs.getInt("MaBanh"),
                            rs.getString("TenBanh"),
                            rs.getInt("TongSoLuongBan"),
                            rs.getDouble("TongDoanhThuBan")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }
}
