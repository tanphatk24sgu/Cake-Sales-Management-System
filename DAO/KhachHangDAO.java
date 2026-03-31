package DAO;

import DTO.KhachHangDTO;
import Database.ConnectDatabase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class KhachHangDAO {

    public ArrayList<KhachHangDTO> getAll() {
        ArrayList<KhachHangDTO> list = new ArrayList<>();
        String sql = "SELECT MaKH, Ho, Ten, DiaChi, SDT FROM khachhang ORDER BY MaKH";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                KhachHangDTO kh = new KhachHangDTO();
                kh.setMaKH(rs.getInt("MaKH"));
                kh.setHo(rs.getString("Ho"));
                kh.setTen(rs.getString("Ten"));
                kh.setDiaChi(rs.getString("DiaChi"));
                kh.setSdt(rs.getString("SDT"));
                list.add(kh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insert(KhachHangDTO kh) {
        String sql = "INSERT INTO khachhang (Ho, Ten, DiaChi, SDT) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getHo());
            ps.setString(2, kh.getTen());
            ps.setString(3, kh.getDiaChi());
            ps.setString(4, kh.getSdt());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(KhachHangDTO kh) {
        String sql = "UPDATE khachhang SET Ho = ?, Ten = ?, DiaChi = ?, SDT = ? WHERE MaKH = ?";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kh.getHo());
            ps.setString(2, kh.getTen());
            ps.setString(3, kh.getDiaChi());
            ps.setString(4, kh.getSdt());
            ps.setInt(5, kh.getMaKH());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int maKH) {
        String sql = "DELETE FROM khachhang WHERE MaKH = ?";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, maKH);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public KhachHangDTO findByPhone(String sdt) {
        String sql = "SELECT MaKH, Ho, Ten, DiaChi, SDT FROM khachhang WHERE SDT = ? LIMIT 1";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, sdt);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    KhachHangDTO kh = new KhachHangDTO();
                    kh.setMaKH(rs.getInt("MaKH"));
                    kh.setHo(rs.getString("Ho"));
                    kh.setTen(rs.getString("Ten"));
                    kh.setDiaChi(rs.getString("DiaChi"));
                    kh.setSdt(rs.getString("SDT"));
                    return kh;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
