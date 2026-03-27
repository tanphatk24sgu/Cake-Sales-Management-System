package DAO;

import DTO.BanhDTO;
import Database.ConnectDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BanhDAO {

    private Connection conn;

    public BanhDAO() {
        conn = ConnectDatabase.getConnection();
    }

    private int convertCodeToInt(String code) {
        try {
            if (code == null)
                return 0;
            return Integer.parseInt(code.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    public ArrayList<BanhDTO> getAll() {

        ArrayList<BanhDTO> list = new ArrayList<>();

        try {

            String sql = "SELECT * FROM banh";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                BanhDTO b = new BanhDTO();

                b.setMaBanh(rs.getInt("MaBanh"));
                b.setTenBanh(rs.getString("TenBanh"));
                b.setSoLuong(rs.getInt("SoLuong"));

                b.setMaDVT(rs.getInt("MaDVT"));
                b.setMaLoai(rs.getInt("MaLoai"));
                b.setMaHang(rs.getInt("MaHang"));

                list.add(b);
            }

            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insert(BanhDTO b) {

        try {

            String sql = "INSERT INTO banh (MaBanh, TenBanh, SoLuong, MaDVT, MaLoai, MaHang) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, b.getMaBanh());
            ps.setString(2, b.getTenBanh());
            ps.setInt(3, b.getSoLuong());

            ps.setString(4, "DVT" + b.getMaDVT());
            ps.setString(5, "L" + b.getMaLoai());
            ps.setString(6, "H" + b.getMaHang());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean update(BanhDTO b) {

        try {

            String sql = "UPDATE banh SET TenBanh=?, SoLuong=?, MaDVT=?, MaLoai=?, MaHang=? WHERE MaBanh=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, b.getTenBanh());
            ps.setInt(2, b.getSoLuong());

            ps.setString(3, "DVT" + b.getMaDVT());
            ps.setString(4, "L" + b.getMaLoai());
            ps.setString(5, "H" + b.getMaHang());

            ps.setInt(6, b.getMaBanh());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(int maBanh) {

        try {

            String sql = "DELETE FROM banh WHERE MaBanh=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, maBanh);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public BanhDTO findById(int maBanh) {

        try {

            String sql = "SELECT * FROM banh WHERE MaBanh=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, maBanh);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                BanhDTO b = new BanhDTO();

                b.setMaBanh(rs.getInt("MaBanh"));
                b.setTenBanh(rs.getString("TenBanh"));
                b.setSoLuong(rs.getInt("SoLuong"));

                b.setMaDVT(convertCodeToInt(rs.getString("MaDVT")));
                b.setMaLoai(convertCodeToInt(rs.getString("MaLoai")));
                b.setMaHang(convertCodeToInt(rs.getString("MaHang")));

                return b;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}