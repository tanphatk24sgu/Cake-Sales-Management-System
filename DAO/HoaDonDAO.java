package DAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import Database.ConnectDatabase;
import DTO.HoaDonDTO;

public class HoaDonDAO {

    public ArrayList<HoaDonDTO> docDSHD() {
        ArrayList<HoaDonDTO> dshd = new ArrayList<>();
        String sql = "SELECT MaHD, NgayLapHD, MaNV, MaKH, ThanhTien FROM hoadon ORDER BY MaHD";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                HoaDonDTO hd = new HoaDonDTO();
                hd.setMaHD(rs.getInt("MaHD"));
                hd.setNgayLapHD(rs.getTimestamp("NgayLapHD"));
                hd.setMaNV(rs.getInt("MaNV"));
                hd.setMaKH(rs.getInt("MaKH"));
                hd.setThanhTien(rs.getDouble("ThanhTien"));
                dshd.add(hd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dshd;
    }

    public int them(HoaDonDTO hd) {
        String sql = "INSERT INTO hoadon (NgayLapHD, MaNV, MaKH, ThanhTien) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setTimestamp(1, new java.sql.Timestamp(hd.getNgayLapHD().getTime()));
            if (hd.getMaNV() > 0) {
                ps.setInt(2, hd.getMaNV());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            if (hd.getMaKH() > 0) {
                ps.setInt(3, hd.getMaKH());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            ps.setDouble(4, hd.getThanhTien());
            int affected = ps.executeUpdate();
            if (affected <= 0) {
                return -1;
            }

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean xoa(int maHD) {
        String sqlDeleteDetail = "DELETE FROM chitiethoadon WHERE MaHD = ?";
        String sqlDeleteInvoice = "DELETE FROM hoadon WHERE MaHD = ?";
        try (Connection conn = ConnectDatabase.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psDetail = conn.prepareStatement(sqlDeleteDetail);
                    PreparedStatement psInvoice = conn.prepareStatement(sqlDeleteInvoice)) {
                psDetail.setInt(1, maHD);
                psDetail.executeUpdate();

                psInvoice.setInt(1, maHD);
                boolean ok = psInvoice.executeUpdate() > 0;
                conn.commit();
                return ok;
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sua(HoaDonDTO hd) {
        String sql = "UPDATE hoadon SET NgayLapHD = ?, MaNV = ?, MaKH = ?, ThanhTien = ? WHERE MaHD = ?";
        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, new java.sql.Timestamp(hd.getNgayLapHD().getTime()));
            if (hd.getMaNV() > 0) {
                ps.setInt(2, hd.getMaNV());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            if (hd.getMaKH() > 0) {
                ps.setInt(3, hd.getMaKH());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            ps.setDouble(4, hd.getThanhTien());
            ps.setInt(5, hd.getMaHD());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int themHoaDonKemChiTiet(HoaDonDTO hd, ArrayList<DTO.ChiTietHoaDonDTO> dsChiTiet) throws Exception {
        if (hd == null || dsChiTiet == null || dsChiTiet.isEmpty()) {
            throw new IllegalArgumentException("Hóa đơn hoặc chi tiết hóa đơn không hợp lệ.");
        }

        String insertHoaDon = "INSERT INTO hoadon (NgayLapHD, MaNV, MaKH, ThanhTien) VALUES (?, ?, ?, ?)";
        String insertChiTiet = "INSERT INTO chitiethoadon (MaHD, MaBanh, SoLuong, DonGia, Diem) VALUES (?, ?, ?, ?, ?)";
        String truTonKho = "UPDATE banh SET SoLuong = SoLuong - ? WHERE MaBanh = ? AND SoLuong >= ?";
        String updateTichDiem = "UPDATE tichdiem SET TICHDIEM = TICHDIEM + ? WHERE MaKH = ?";
        String insertTichDiem = "INSERT INTO tichdiem (MaKH, TICHDIEM) VALUES (?, ?)";

        try (Connection conn = ConnectDatabase.getConnection()) {
            boolean oldAuto = conn.getAutoCommit();
            conn.setAutoCommit(false);

                try (PreparedStatement psHD = conn.prepareStatement(insertHoaDon, Statement.RETURN_GENERATED_KEYS);
                    PreparedStatement psCT = conn.prepareStatement(insertChiTiet);
                    PreparedStatement psKho = conn.prepareStatement(truTonKho);
                        PreparedStatement psUpdateTichDiem = conn.prepareStatement(updateTichDiem);
                        PreparedStatement psInsertTichDiem = conn.prepareStatement(insertTichDiem)) {

                psHD.setTimestamp(1, new java.sql.Timestamp(hd.getNgayLapHD().getTime()));
                if (hd.getMaNV() > 0) {
                    psHD.setInt(2, hd.getMaNV());
                } else {
                    psHD.setNull(2, java.sql.Types.INTEGER);
                }
                if (hd.getMaKH() > 0) {
                    psHD.setInt(3, hd.getMaKH());
                } else {
                    psHD.setNull(3, java.sql.Types.INTEGER);
                }
                psHD.setDouble(4, hd.getThanhTien());
                int inserted = psHD.executeUpdate();
                if (inserted <= 0) {
                    throw new IllegalStateException("Không tạo được hóa đơn.");
                }

                int maHD;
                try (ResultSet keys = psHD.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new IllegalStateException("Không lấy được mã hóa đơn vừa tạo.");
                    }
                    maHD = keys.getInt(1);
                }

                int tongDiemCong = 0;
                for (DTO.ChiTietHoaDonDTO ct : dsChiTiet) {
                    if (ct.getMaBanh() <= 0 || ct.getSoLuong() <= 0) {
                        throw new IllegalArgumentException("Chi tiết hóa đơn không hợp lệ.");
                    }

                    psKho.setInt(1, ct.getSoLuong());
                    psKho.setInt(2, ct.getMaBanh());
                    psKho.setInt(3, ct.getSoLuong());
                    int updated = psKho.executeUpdate();
                    if (updated <= 0) {
                        throw new IllegalStateException("Sản phẩm mã " + ct.getMaBanh() + " không đủ tồn kho.");
                    }

                    psCT.setInt(1, maHD);
                    psCT.setInt(2, ct.getMaBanh());
                    psCT.setInt(3, ct.getSoLuong());
                    psCT.setDouble(4, ct.getDonGia());
                    psCT.setInt(5, ct.getDiem());
                    psCT.executeUpdate();

                    tongDiemCong += Math.max(0, ct.getDiem());
                }

                if (hd.getMaKH() > 0 && tongDiemCong > 0) {
                    psUpdateTichDiem.setInt(1, tongDiemCong);
                    psUpdateTichDiem.setInt(2, hd.getMaKH());
                    int updated = psUpdateTichDiem.executeUpdate();

                    if (updated <= 0) {
                        psInsertTichDiem.setInt(1, hd.getMaKH());
                        psInsertTichDiem.setInt(2, tongDiemCong);
                        psInsertTichDiem.executeUpdate();
                    }
                }

                conn.commit();
                hd.setMaHD(maHD);
                return maHD;
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(oldAuto);
            }
        }
    }

    public HoaDonDTO timKiemTheoMa(int maHD) {
        String sql = "SELECT MaHD, NgayLapHD, MaNV, MaKH, ThanhTien FROM hoadon WHERE MaHD = ?";
        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    HoaDonDTO hd = new HoaDonDTO();
                    hd.setMaHD(rs.getInt("MaHD"));
                    hd.setNgayLapHD(rs.getTimestamp("NgayLapHD"));
                    hd.setMaNV(rs.getInt("MaNV"));
                    hd.setMaKH(rs.getInt("MaKH"));
                    hd.setThanhTien(rs.getDouble("ThanhTien"));
                    return hd;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<HoaDonDTO> timKiemTheoNhanVien(int maNV) {
        ArrayList<HoaDonDTO> result = new ArrayList<>();
        String sql = "SELECT MaHD, NgayLapHD, MaNV, MaKH, ThanhTien FROM hoadon WHERE MaNV = ? ORDER BY MaHD";
        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maNV);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HoaDonDTO hd = new HoaDonDTO();
                    hd.setMaHD(rs.getInt("MaHD"));
                    hd.setNgayLapHD(rs.getTimestamp("NgayLapHD"));
                    hd.setMaNV(rs.getInt("MaNV"));
                    hd.setMaKH(rs.getInt("MaKH"));
                    hd.setThanhTien(rs.getDouble("ThanhTien"));
                    result.add(hd);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public double thongKeDoanhThuTheoNgay() {
        String sql = "SELECT IFNULL(SUM(ThanhTien), 0) AS DoanhThu FROM hoadon WHERE DATE(NgayLapHD) = CURDATE()";
        return queryDoanhThu(sql);
    }

    public double thongKeDoanhThuTheoThang(int thang, int nam) {
        String sql = "SELECT IFNULL(SUM(ThanhTien), 0) AS DoanhThu FROM hoadon WHERE MONTH(NgayLapHD) = ? AND YEAR(NgayLapHD) = ?";
        return queryDoanhThu(sql, thang, nam);
    }

    public double thongKeDoanhThuTheoQuy(int quy, int nam) {
        String sql = "SELECT IFNULL(SUM(ThanhTien), 0) AS DoanhThu FROM hoadon WHERE QUARTER(NgayLapHD) = ? AND YEAR(NgayLapHD) = ?";
        return queryDoanhThu(sql, quy, nam);
    }

    public double thongKeDoanhThuTheoNam(int nam) {
        String sql = "SELECT IFNULL(SUM(ThanhTien), 0) AS DoanhThu FROM hoadon WHERE YEAR(NgayLapHD) = ?";
        return queryDoanhThu(sql, nam);
    }

    public double thongKeDoanhThuTheoKhoangNgay(Date tuNgay, Date denNgay) {
        String sql = "SELECT IFNULL(SUM(ThanhTien), 0) AS DoanhThu FROM hoadon WHERE DATE(NgayLapHD) BETWEEN ? AND ?";
        return queryDoanhThu(sql, tuNgay, denNgay);
    }

    private double queryDoanhThu(String sql, Object... params) {
        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                Object p = params[i];
                int idx = i + 1;
                if (p instanceof Integer) {
                    ps.setInt(idx, (Integer) p);
                } else if (p instanceof Date) {
                    ps.setDate(idx, (Date) p);
                } else {
                    ps.setObject(idx, p);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("DoanhThu");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
