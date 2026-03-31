package DAO;

import java.sql.*;
import java.util.ArrayList;
import Database.ConnectDatabase;
import DTO.KhuyenMaiDTO;

public class KhuyenMaiDAO {

    public ArrayList<KhuyenMaiDTO> getAll() {
        ArrayList<KhuyenMaiDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM chuongtrinhkhuyenmai";

        try (
                Connection conn = ConnectDatabase.getConnection(); // 🔥 lấy mới mỗi lần
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                KhuyenMaiDTO km = new KhuyenMaiDTO();

                km.setMaKM(rs.getInt("MaKM"));
                km.setTenKM(rs.getString("TenCTKM"));
                km.setPhanTramGiam(rs.getInt("PhanTramGiam"));
                km.setDieuKien((int) rs.getDouble("DieuKienToiThieu"));

                list.add(km);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}