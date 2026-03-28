package DAO;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import Database.ConnectDatabase;
import DTO.ChucVuDTO;

public class ChucVuDAO {
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;

    public ArrayList<ChucVuDTO> docDSCV() {
        ArrayList<ChucVuDTO> dscv = new ArrayList<>();
        try {
            conn = ConnectDatabase.getConnection();
            String qry = "SELECT * FROM chucvu";
            st = conn.createStatement();
            rs = st.executeQuery(qry);

            while (rs.next()) {
                ChucVuDTO cv = new ChucVuDTO();
                cv.setMaChucVu(rs.getInt("MaChucVu"));
                cv.setTenChucVu(rs.getString("TenChucVu"));

                dscv.add(cv);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
            } catch (Exception e) {}
            try {
                if (st != null) st.close();
            } catch (Exception e) {}
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {}
        }
        return dscv;
    }

    public void them(ChucVuDTO cv) {
        try {
            conn = ConnectDatabase.getConnection();
            String qry = "INSERT INTO chucvu(TenChucVu) VALUES(?)";
            PreparedStatement pst = conn.prepareStatement(qry);
            pst.setString(1, cv.getTenChucVu());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {}
        }
    }

    public void sua(ChucVuDTO cv) {
        try {
            conn = ConnectDatabase.getConnection();
            String qry = "UPDATE chucvu SET TenChucVu=? WHERE MaChucVu=?";
            PreparedStatement pst = conn.prepareStatement(qry);
            pst.setString(1, cv.getTenChucVu());
            pst.setInt(2, cv.getMaChucVu());
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {}
        }
    }

    public void xoa(int maChucVu) {
        try {
            conn = ConnectDatabase.getConnection();
            String qry = "DELETE FROM chucvu WHERE MaChucVu=?";
            PreparedStatement pst = conn.prepareStatement(qry);
            pst.setInt(1, maChucVu);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (Exception e) {}
        }
    }
}
