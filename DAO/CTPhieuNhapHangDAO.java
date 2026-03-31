package DAO;

import Database.ConnectDatabase;
import DTO.CTPhieuNhapHangDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CTPhieuNhapHangDAO {

    public ArrayList<CTPhieuNhapHangDTO> docDSCTPN() {
        ArrayList<CTPhieuNhapHangDTO> dsctpn = new ArrayList<>();
        String sql = "SELECT MaPhieuNhap, MaBanh, MaNVL, SoLuong, DonGia, ThanhTien, TinhTrang "
                + "FROM ct_phieunhaphang ORDER BY MaPhieuNhap, MaBanh, MaNVL";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CTPhieuNhapHangDTO ct = new CTPhieuNhapHangDTO();
                ct.setMaPhieuNhap(rs.getInt("MaPhieuNhap"));
                ct.setMaBanh(rs.getInt("MaBanh"));
                ct.setMaNVL(rs.getInt("MaNVL"));
                ct.setSoLuong(rs.getInt("SoLuong"));
                ct.setDonGia(rs.getDouble("DonGia"));
                ct.setThanhTien(rs.getDouble("ThanhTien"));
                ct.setTinhTrang(rs.getString("TinhTrang"));
                dsctpn.add(ct);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dsctpn;
    }

    public boolean them(CTPhieuNhapHangDTO ct) {
        String sql = "INSERT INTO ct_phieunhaphang (MaPhieuNhap, MaBanh, MaNVL, SoLuong, DonGia, TinhTrang) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ct.getMaPhieuNhap());
            if (ct.getMaBanh() > 0) {
                ps.setInt(2, ct.getMaBanh());
            } else {
                ps.setNull(2, java.sql.Types.INTEGER);
            }
            if (ct.getMaNVL() > 0) {
                ps.setInt(3, ct.getMaNVL());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            ps.setInt(4, ct.getSoLuong());
            ps.setDouble(5, ct.getDonGia());
            ps.setString(6, ct.getTinhTrang());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sua(CTPhieuNhapHangDTO ct) {
        String sql = "UPDATE ct_phieunhaphang SET SoLuong = ?, DonGia = ?, TinhTrang = ? "
                + "WHERE MaPhieuNhap = ? "
                + "AND ((? > 0 AND MaBanh = ?) OR (? <= 0 AND MaBanh IS NULL)) "
                + "AND ((? > 0 AND MaNVL = ?) OR (? <= 0 AND MaNVL IS NULL))";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, ct.getSoLuong());
            ps.setDouble(2, ct.getDonGia());
            ps.setString(3, ct.getTinhTrang());
            ps.setInt(4, ct.getMaPhieuNhap());

            ps.setInt(5, ct.getMaBanh());
            ps.setInt(6, ct.getMaBanh());
            ps.setInt(7, ct.getMaBanh());

            ps.setInt(8, ct.getMaNVL());
            ps.setInt(9, ct.getMaNVL());
            ps.setInt(10, ct.getMaNVL());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoa(int maPhieuNhap, int maBanh, int maNVL) {
        String sql = "DELETE FROM ct_phieunhaphang WHERE MaPhieuNhap = ? "
                + "AND ((? > 0 AND MaBanh = ?) OR (? <= 0 AND MaBanh IS NULL)) "
                + "AND ((? > 0 AND MaNVL = ?) OR (? <= 0 AND MaNVL IS NULL))";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maPhieuNhap);

            ps.setInt(2, maBanh);
            ps.setInt(3, maBanh);
            ps.setInt(4, maBanh);

            ps.setInt(5, maNVL);
            ps.setInt(6, maNVL);
            ps.setInt(7, maNVL);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<CTPhieuNhapHangDTO> timTheoMaPhieuNhap(int maPhieuNhap) {
        ArrayList<CTPhieuNhapHangDTO> dsctpn = new ArrayList<>();
        String sql = "SELECT MaPhieuNhap, MaBanh, MaNVL, SoLuong, DonGia, ThanhTien, TinhTrang "
                + "FROM ct_phieunhaphang WHERE MaPhieuNhap = ? ORDER BY MaBanh, MaNVL";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maPhieuNhap);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CTPhieuNhapHangDTO ct = new CTPhieuNhapHangDTO();
                    ct.setMaPhieuNhap(rs.getInt("MaPhieuNhap"));
                    ct.setMaBanh(rs.getInt("MaBanh"));
                    ct.setMaNVL(rs.getInt("MaNVL"));
                    ct.setSoLuong(rs.getInt("SoLuong"));
                    ct.setDonGia(rs.getDouble("DonGia"));
                    ct.setThanhTien(rs.getDouble("ThanhTien"));
                    ct.setTinhTrang(rs.getString("TinhTrang"));
                    dsctpn.add(ct);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dsctpn;
    }
}
