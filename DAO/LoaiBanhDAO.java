package DAO;

import DTO.LoaiBanhDTO;
import Database.ConnectDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class LoaiBanhDAO {

    private Connection conn;

    public LoaiBanhDAO() {
        conn = ConnectDatabase.getConnection();
    }

    // convert "L01" -> 1
    private int convertCodeToInt(String code) {
        try {
            return Integer.parseInt(code.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

   
    public ArrayList<LoaiBanhDTO> getAll() {

        ArrayList<LoaiBanhDTO> list = new ArrayList<>();

        try {

            String sql = "SELECT * FROM loaibanh";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                LoaiBanhDTO l = new LoaiBanhDTO();

                l.setMaLoai(convertCodeToInt(rs.getString("MaLoai")));
                l.setTenLoai(rs.getString("TenLoai"));

                list.add(l);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    
    public boolean insert(LoaiBanhDTO l) {

        try {

            String sql = "INSERT INTO loaibanh VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, "L" + l.getMaLoai());
            ps.setString(2, l.getTenLoai());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    
    public boolean update(LoaiBanhDTO l) {

        try {

            String sql = "UPDATE loaibanh SET TenLoai=? WHERE MaLoai=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, l.getTenLoai());
            ps.setString(2, "L" + l.getMaLoai());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

   
    public boolean delete(int maLoai) {

        try {

            String sql = "DELETE FROM loaibanh WHERE MaLoai=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, "L" + maLoai);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
