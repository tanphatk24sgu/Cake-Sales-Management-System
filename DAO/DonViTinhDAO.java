package DAO;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import Database.ConnectDatabase;
import DTO.DonViTinhDTO;

public class DonViTinhDAO {
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;

    public ArrayList<DonViTinhDTO> docDSDVT() {
        ArrayList<DonViTinhDTO> dsdvt = new ArrayList<>();
        try {
            conn = ConnectDatabase.getConnection();

            String qry = "SELECT * FROM donvitinh";

            st = conn.createStatement();
            rs = st.executeQuery(qry);

            while(rs.next()) {
                DonViTinhDTO dvt = new DonViTinhDTO();
                dvt.setMaDVT(rs.getInt("MaDVT"));
                dvt.setTen(rs.getString("Ten"));

                dsdvt.add(dvt);
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch(Exception e) {}
            try {
                if(st != null)  st.close();
            } catch(Exception e) {}
        }

        return dsdvt;
    }

    public void them(DonViTinhDTO dvt) {
        try {
            conn = ConnectDatabase.getConnection();
            String qry = "INSERT INTO donvitinh (Ten) VALUES (?)";
            PreparedStatement pst = conn.prepareStatement(qry);
            pst.setString(1, dvt.getTen());
            pst.executeUpdate();
            pst.close();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        } finally {
            try {
                if(conn != null) conn.close();
            } catch(Exception e) {}
        }
    }

    public void sua(DonViTinhDTO dvt) {
        try {
            conn = ConnectDatabase.getConnection();
            String qry = "UPDATE donvitinh SET Ten=? WHERE MaDVT=?";
            PreparedStatement pst = conn.prepareStatement(qry);
            pst.setString(1, dvt.getTen());
            pst.setInt(2, dvt.getMaDVT());
            pst.executeUpdate();
            pst.close();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        } finally {
            try {
                if(conn != null) conn.close();
            } catch(Exception e) {}
        }
    }

    public void xoa(int maDVT) {
        try {
            conn = ConnectDatabase.getConnection();
            String qry = "DELETE FROM donvitinh WHERE MaDVT=?";
            PreparedStatement pst = conn.prepareStatement(qry);
            pst.setInt(1, maDVT);
            pst.executeUpdate();
            pst.close();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        } finally {
            try {
                if(conn != null) conn.close();
            } catch(Exception e) {}
        }
    }

    public DonViTinhDTO timKiemTheoMa(int maDVT) {
        DonViTinhDTO dvt = null;
        try {
            conn = ConnectDatabase.getConnection();
            String qry = "SELECT * FROM donvitinh WHERE MaDVT = ?";
            PreparedStatement pst = conn.prepareStatement(qry);
            pst.setInt(1, maDVT);
            rs = pst.executeQuery();

            if(rs.next()) {
                dvt = new DonViTinhDTO();
                dvt.setMaDVT(rs.getInt("MaDVT"));
                dvt.setTen(rs.getString("Ten"));
            }
            pst.close();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        } finally {
            try {
                if(rs != null) rs.close();
            } catch(Exception e) {}
            try {
                if(conn != null) conn.close();
            } catch(Exception e) {}
        }
        return dvt;
    }
}