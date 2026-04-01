package DAO;

import DTO.BanhDTO;
import Database.ConnectDatabase;

import java.sql.Connection;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BanhDAO {

    private Connection getConn() {
        return ConnectDatabase.getConnection();
    }

    public ArrayList<BanhDTO> getAll() {

        ArrayList<BanhDTO> list = new ArrayList<>();

        try {

            String sql = "SELECT * FROM banh";
            PreparedStatement ps = getConn().prepareStatement(sql);
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
            PreparedStatement ps = getConn().prepareStatement(sql);

            ps.setInt(1, b.getMaBanh());
            ps.setString(2, b.getTenBanh());
            ps.setInt(3, b.getSoLuong());

            ps.setInt(4, b.getMaDVT());
            ps.setInt(5, b.getMaLoai());
            ps.setInt(6, b.getMaHang());

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean update(BanhDTO b) {

        try {

            String sql = "UPDATE banh SET TenBanh=?, SoLuong=?, MaDVT=?, MaLoai=?, MaHang=? WHERE MaBanh=?";
            PreparedStatement ps = getConn().prepareStatement(sql);

            ps.setString(1, b.getTenBanh());
            ps.setInt(2, b.getSoLuong());

            ps.setInt(3, b.getMaDVT());
            ps.setInt(4, b.getMaLoai());
            ps.setInt(5, b.getMaHang());

            ps.setInt(6, b.getMaBanh());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(int maBanh) {
        Connection conn = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;

        try {
            conn = getConn();
            conn.setAutoCommit(false);

            String sql1 = "DELETE FROM congthuc WHERE MaBAnh=?";
            pst1 = conn.prepareStatement(sql1);
            pst1.setInt(1, maBanh);
            pst1.executeUpdate();

            String sql2 = "DELETE FROM banh WHERE MaBanh=?";
            pst2 = getConn().prepareStatement(sql2);
            pst2.setInt(1, maBanh);
            int res = pst2.executeUpdate();

            conn.commit();

            return res > 0;

        } catch (Exception e) {
            System.out.println("Lỗi xóa: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    public BanhDTO findById(int maBanh) {

        try {

            String sql = "SELECT * FROM banh WHERE MaBanh=?";
            PreparedStatement ps = getConn().prepareStatement(sql);

            ps.setInt(1, maBanh);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                BanhDTO b = new BanhDTO();

                b.setMaBanh(rs.getInt("MaBanh"));
                b.setTenBanh(rs.getString("TenBanh"));
                b.setSoLuong(rs.getInt("SoLuong"));

                b.setMaDVT(rs.getInt("MaDVT"));
                b.setMaLoai(rs.getInt("MaLoai"));
                b.setMaHang(rs.getInt("MaHang"));

                return b;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}