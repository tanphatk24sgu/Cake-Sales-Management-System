package DAO;

import java.sql.*;
import DTO.CongThucDTO;
import Database.ConnectDatabase;

public class CongThucDAO {
    private Connection conn;

    public CongThucDAO() {
        conn = ConnectDatabase.getConnection();
    }

    public CongThucDTO getByMaBanh(int maBanh) {
        try {
            String sql = "SELECT * FROM congthuc WHERE MaBanh = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, maBanh);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                CongThucDTO ct = new CongThucDTO();
                ct.setMaCongThuc(rs.getInt("MaCongThuc"));
                ct.setMaBanh(rs.getInt("MaBanh"));
                ct.setMaDVT(rs.getInt("MaDVT"));
                ct.setCachLam(rs.getString("CachLam"));
                return ct;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insert(CongThucDTO ct) {
        try {
            String sql = "INSERT INTO congthuc (MaBanh, MaDVT, CachLam) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ct.getMaBanh());
            ps.setInt(2, ct.getMaDVT());
            ps.setString(3, ct.getCachLam());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(CongThucDTO ct) {
        try {
            String sql = "UPDATE congthuc SET CachLam = ? WHERE MaBanh = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ct.getCachLam());
            ps.setInt(2, ct.getMaBanh());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}