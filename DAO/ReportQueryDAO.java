package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import Database.ConnectDatabase;

public class ReportQueryDAO {

    public Object[] getHoaDonHeader(int maHD) {
        String sql = "SELECT h.MaHD, h.NgayLapHD, h.ThanhTien, "
                + "nv.MaNV, CONCAT(nv.Ho, ' ', nv.Ten) AS TenNhanVien, "
                + "kh.MaKH, CONCAT(kh.Ho, ' ', kh.Ten) AS TenKhachHang, kh.SDT, kh.DiaChi "
                + "FROM hoadon h "
                + "LEFT JOIN nhanvien nv ON h.MaNV = nv.MaNV "
                + "LEFT JOIN khachhang kh ON h.MaKH = kh.MaKH "
                + "WHERE h.MaHD = ?";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Object[] {
                            rs.getInt("MaHD"),
                            rs.getTimestamp("NgayLapHD"),
                            rs.getDouble("ThanhTien"),
                            rs.getInt("MaNV"),
                            rs.getString("TenNhanVien"),
                            rs.getInt("MaKH"),
                            rs.getString("TenKhachHang"),
                            rs.getString("SDT"),
                            rs.getString("DiaChi")
                    };
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Object[]> getHoaDonDetails(int maHD) {
        ArrayList<Object[]> rows = new ArrayList<>();
        String sql = "SELECT c.MaHD, c.MaBanh, b.TenBanh, c.SoLuong, c.DonGia, c.ThanhTien, c.Diem "
                + "FROM chitiethoadon c "
                + "LEFT JOIN banh b ON c.MaBanh = b.MaBanh "
                + "WHERE c.MaHD = ? "
                + "ORDER BY c.MaBanh";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[] {
                            rs.getInt("MaHD"),
                            rs.getInt("MaBanh"),
                            rs.getString("TenBanh"),
                            rs.getInt("SoLuong"),
                            rs.getDouble("DonGia"),
                            rs.getDouble("ThanhTien"),
                            rs.getInt("Diem")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

    public Object[] getPhieuNhapHeader(int maPhieuNhap) {
        String sql = "SELECT p.MaPhieuNhap, p.Ngay, "
                + "nv.MaNV, CONCAT(nv.Ho, ' ', nv.Ten) AS TenNhanVien, "
                + "ncc.MaNCC, ncc.TenNCC, ncc.MaSoThue "
                + "FROM phieunhaphang p "
                + "LEFT JOIN nhanvien nv ON p.MaNV = nv.MaNV "
                + "LEFT JOIN nhacungcap ncc ON p.MaNCC = ncc.MaNCC "
                + "WHERE p.MaPhieuNhap = ?";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maPhieuNhap);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Object[] {
                            rs.getInt("MaPhieuNhap"),
                            rs.getTimestamp("Ngay"),
                            rs.getInt("MaNV"),
                            rs.getString("TenNhanVien"),
                            rs.getInt("MaNCC"),
                            rs.getString("TenNCC"),
                            rs.getString("MaSoThue")
                    };
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public ArrayList<Object[]> getPhieuNhapDetails(int maPhieuNhap) {
        ArrayList<Object[]> rows = new ArrayList<>();
        String sql = "SELECT c.MaPhieuNhap, c.MaBanh, b.TenBanh, c.MaNVL, nl.Ten AS TenNguyenLieu, "
                + "c.SoLuong, c.DonGia, c.ThanhTien, c.TinhTrang "
                + "FROM ct_phieunhaphang c "
                + "LEFT JOIN banh b ON c.MaBanh = b.MaBanh "
                + "LEFT JOIN nguyenlieu nl ON c.MaNVL = nl.MaNL "
                + "WHERE c.MaPhieuNhap = ? "
                + "ORDER BY c.MaBanh, c.MaNVL";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maPhieuNhap);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[] {
                            rs.getInt("MaPhieuNhap"),
                            rs.getInt("MaBanh"),
                            rs.getString("TenBanh"),
                            rs.getInt("MaNVL"),
                            rs.getString("TenNguyenLieu"),
                            rs.getInt("SoLuong"),
                            rs.getDouble("DonGia"),
                            rs.getDouble("ThanhTien"),
                            rs.getString("TinhTrang")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }

    public ArrayList<Object[]> getHoaDonListByDateRange(Timestamp tuNgay, Timestamp denNgay) {
        ArrayList<Object[]> rows = new ArrayList<>();
        String sql = "SELECT h.MaHD, h.NgayLapHD, "
                + "CONCAT(IFNULL(kh.Ho, ''), ' ', IFNULL(kh.Ten, '')) AS TenKhachHang, "
                + "CONCAT(IFNULL(nv.Ho, ''), ' ', IFNULL(nv.Ten, '')) AS TenNhanVien, "
                + "h.ThanhTien "
                + "FROM hoadon h "
                + "LEFT JOIN khachhang kh ON h.MaKH = kh.MaKH "
                + "LEFT JOIN nhanvien nv ON h.MaNV = nv.MaNV "
                + "WHERE h.NgayLapHD BETWEEN ? AND ? "
                + "ORDER BY h.NgayLapHD, h.MaHD";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, tuNgay);
            ps.setTimestamp(2, denNgay);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[] {
                            rs.getInt("MaHD"),
                            rs.getTimestamp("NgayLapHD"),
                            rs.getString("TenKhachHang"),
                            rs.getString("TenNhanVien"),
                            rs.getDouble("ThanhTien")
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rows;
    }
}
