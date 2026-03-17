package DAO;

import DTO.HangSanXuatDTO;
import Database.ConnectDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class HangSanXuatDAO {

    private Connection conn;

    public HangSanXuatDAO() {
        conn = ConnectDatabase.getConnection();
    }

    private int convertCodeToInt(String code) {
        try {
            return Integer.parseInt(code.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    
    public ArrayList<HangSanXuatDTO> getAll() {

        ArrayList<HangSanXuatDTO> list = new ArrayList<>();

        try {

            String sql = "SELECT * FROM hangsanxuat";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                HangSanXuatDTO h = new HangSanXuatDTO();

                h.setMaHang(convertCodeToInt(rs.getString("MaHang")));
                h.setTenHang(rs.getString("TenHang"));

                list.add(h);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean insert(HangSanXuatDTO h) {

        try {

            String sql = "INSERT INTO hangsanxuat VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, "H" + h.getMaHang());
            ps.setString(2, h.getTenHang());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

   
    public boolean update(HangSanXuatDTO h) {

        try {

            String sql = "UPDATE hangsanxuat SET TenHang=? WHERE MaHang=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, h.getTenHang());
            ps.setString(2, "H" + h.getMaHang());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(int maHang) {

        try {

            String sql = "DELETE FROM hangsanxuat WHERE MaHang=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, "H" + maHang);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
