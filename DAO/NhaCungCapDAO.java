package DAO;

import DTO.NhaCungCapDTO;
import Database.ConnectDatabase;
import java.sql.*;
import java.util.ArrayList;

public class NhaCungCapDAO {

    public ArrayList<NhaCungCapDTO> getAll() {
        ArrayList<NhaCungCapDTO> list = new ArrayList<>();
        try {
            Connection conn = ConnectDatabase.getConnection();
            String sql = "SELECT * FROM nhacungcap";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                NhaCungCapDTO ncc = new NhaCungCapDTO();
                ncc.setMaNCC(rs.getInt("MaNCC"));
                ncc.setTenNCC(rs.getString("TenNCC"));
                ncc.setMaSoThue(rs.getString("MaSoThue"));
                list.add(ncc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean insert(NhaCungCapDTO ncc) {
        try {
            Connection conn = ConnectDatabase.getConnection();
            String sql = "INSERT INTO nhacungcap (TenNCC, MaSoThue) VALUES (?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, ncc.getTenNCC());
            ps.setString(2, ncc.getMaSoThue());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(NhaCungCapDTO ncc) {
        try {
            Connection conn = ConnectDatabase.getConnection();
            String sql = "UPDATE nhacungcap SET TenNCC=?, MaSoThue=? WHERE MaNCC=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, ncc.getTenNCC());
            ps.setString(2, ncc.getMaSoThue());
            ps.setInt(3, ncc.getMaNCC());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int ma) {
        try {
            Connection conn = ConnectDatabase.getConnection();
            String sql = "DELETE FROM nhacungcap WHERE MaNCC=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, ma);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}