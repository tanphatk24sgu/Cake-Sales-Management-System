package DAO;

import java.sql.*;
import DTO.CongThucDTO;
import Database.ConnectDatabase;

public class CongThucDAO {
    public CongThucDTO getByMaBanh(int maBanh) {
        try (Connection conn = ConnectDatabase.getConnection()) {
            if (conn == null) {
                return null;
            }
            String sql = "SELECT * FROM congthuc WHERE MaBanh = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, maBanh);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        CongThucDTO ct = new CongThucDTO();
                        ct.setMaBanh(rs.getInt("MaBanh"));
                        ct.setMaDVT(rs.getInt("MaDVT"));
                        ct.setCachLam(rs.getString("CachLam"));
                        return ct;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(CongThucDTO ct) {
        try (Connection conn = ConnectDatabase.getConnection()) {
            if (conn == null) {
                return false;
            }
            String sql = "INSERT INTO congthuc (MaBanh, MaDVT, CachLam) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, ct.getMaBanh());
                ps.setInt(2, ct.getMaDVT());
                ps.setString(3, ct.getCachLam());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(CongThucDTO ct) {
        try (Connection conn = ConnectDatabase.getConnection()) {
            if (conn == null) {
                return false;
            }
            String sql = "UPDATE congthuc SET MaDVT = ?, CachLam = ? WHERE MaBanh = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, ct.getMaDVT());
                ps.setString(2, ct.getCachLam());
                ps.setInt(3, ct.getMaBanh());
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int maBanh) {
        try (Connection conn = ConnectDatabase.getConnection()) {
            if (conn == null) {
                return false;
            }
            String sql = "DELETE FROM congthuc WHERE MaBanh = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, maBanh);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}