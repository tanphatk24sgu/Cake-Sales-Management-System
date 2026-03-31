package DAO;

import Database.ConnectDatabase;
import DTO.PhieuNhapHangDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class PhieuNhapHangDAO {

    public ArrayList<PhieuNhapHangDTO> docDSPN() {
        ArrayList<PhieuNhapHangDTO> dspn = new ArrayList<>();
        String sql = "SELECT MaPhieuNhap, Ngay, MaNV, MaNCC FROM phieunhaphang ORDER BY MaPhieuNhap";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PhieuNhapHangDTO pn = new PhieuNhapHangDTO();
                pn.setMaPhieuNhap(rs.getInt("MaPhieuNhap"));
                pn.setNgay(rs.getTimestamp("Ngay"));
                pn.setMaNV(rs.getInt("MaNV"));
                pn.setMaNCC(rs.getInt("MaNCC"));
                dspn.add(pn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dspn;
    }

    public int them(PhieuNhapHangDTO pn) {
        String sql = "INSERT INTO phieunhaphang (Ngay, MaNV, MaNCC) VALUES (?, ?, ?)";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setTimestamp(1, new java.sql.Timestamp(pn.getNgay().getTime()));
            ps.setInt(2, pn.getMaNV());
            ps.setInt(3, pn.getMaNCC());

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

    public boolean sua(PhieuNhapHangDTO pn) {
        String sql = "UPDATE phieunhaphang SET Ngay = ?, MaNV = ?, MaNCC = ? WHERE MaPhieuNhap = ?";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTimestamp(1, new java.sql.Timestamp(pn.getNgay().getTime()));
            ps.setInt(2, pn.getMaNV());
            ps.setInt(3, pn.getMaNCC());
            ps.setInt(4, pn.getMaPhieuNhap());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoa(int maPhieuNhap) {
        String sqlDeleteDetails = "DELETE FROM ct_phieunhaphang WHERE MaPhieuNhap = ?";
        String sqlDeleteHeader = "DELETE FROM phieunhaphang WHERE MaPhieuNhap = ?";

        try (Connection conn = ConnectDatabase.getConnection()) {
            boolean oldAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);

            try (PreparedStatement psDetails = conn.prepareStatement(sqlDeleteDetails);
                    PreparedStatement psHeader = conn.prepareStatement(sqlDeleteHeader)) {

                psDetails.setInt(1, maPhieuNhap);
                psDetails.executeUpdate();

                psHeader.setInt(1, maPhieuNhap);
                boolean ok = psHeader.executeUpdate() > 0;

                conn.commit();
                return ok;
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(oldAutoCommit);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public PhieuNhapHangDTO timKiemTheoMa(int maPhieuNhap) {
        String sql = "SELECT MaPhieuNhap, Ngay, MaNV, MaNCC FROM phieunhaphang WHERE MaPhieuNhap = ?";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maPhieuNhap);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PhieuNhapHangDTO pn = new PhieuNhapHangDTO();
                    pn.setMaPhieuNhap(rs.getInt("MaPhieuNhap"));
                    pn.setNgay(rs.getTimestamp("Ngay"));
                    pn.setMaNV(rs.getInt("MaNV"));
                    pn.setMaNCC(rs.getInt("MaNCC"));
                    return pn;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
