package DAO;

import java.sql.*;
import java.util.ArrayList;
import DTO.KhuyenMaiDTO;
import Database.ConnectDatabase;

public class KhuyenMaiDAO {
    private Connection conn;

    public KhuyenMaiDAO() {
        conn = ConnectDatabase.getConnection();
    }

    public java.util.ArrayList<DTO.KhuyenMaiDTO> getAll() {
        java.util.ArrayList<DTO.KhuyenMaiDTO> list = new java.util.ArrayList<>();

        try {
            String sql = "SELECT * FROM chuongtrinhkhuyenmai";
            java.sql.PreparedStatement ps = conn.prepareStatement(sql);
            java.sql.ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                DTO.KhuyenMaiDTO km = new DTO.KhuyenMaiDTO();

                km.setMaKM(rs.getInt("MaKM"));
                km.setTenKM(rs.getString("TenCTKM")); // ✅ ĐÚNG

                km.setPhanTramGiam(rs.getInt("PhanTramGiam")); // ✅ ĐÚNG

                km.setDieuKien((int) rs.getDouble("DieuKienToiThieu")); // ✅ ĐÚNG

                list.add(km);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}