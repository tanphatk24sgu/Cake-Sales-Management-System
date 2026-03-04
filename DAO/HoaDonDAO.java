package DAO;

import java.sql.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Database.ConnectDatabase;

import DTO.HoaDonDTO;

public class HoaDonDAO {
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;

    public ArrayList<HoaDonDTO> docDSHD() {
        ArrayList<HoaDonDTO> dshd = new ArrayList<>();
        try {
            conn = ConnectDatabase.getInstance().getConnection();
            
            String qry = "SELECT * FROM hoadon";     
            
            st = conn.createStatement();
            rs = st.executeQuery(qry);

            while(rs.next()) {
                HoaDonDTO hd = new HoaDonDTO();
                hd.setMaHD(Integer.parseInt("MaHD"));
                hd.setNgayLapHD(rs.getDate("NgaySinh"));
                hd.setMaNV(Integer.parseInt("MaNV"));
                hd.setMaKH(Integer.parseInt("MaKH"));
                hd.setThanhTien(Integer.parseInt("ThanhTien"));

                dshd.add(hd);
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

        return dshd;
    }

    public void them(HoaDonDTO hd) {
        try {
            java.sql.Date sqlDate = new Date(hd.getNgayLapHD().getTime());

            String qry = "ÍNERT INTO hoadon VALUES('";
            qry += hd.getMaHD() + "', '";
            qry += sqlDate + "', '";
            qry += hd.getMaNV() + "', '";
            qry += hd.getMaKH() + "', '";
            qry += hd.getThanhTien() + "')";

            st = conn.createStatement();
            st.executeUpdate(qry);
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        }
    }

    public void xoa(String maHD) {
        try {
            String qry = "DELETE FROM hoadon WHERE MaHD = '" + maHD + "'";
            st = conn.createStatement();
            st.executeUpdate(qry);
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        }
    }

    public void sua(HoaDonDTO hd) {
        try {
            java.sql.Date sqlDate = new Date(hd.getNgayLapHD().getTime());

            String qry = "UPDATE hoadon SET ";
            qry += "NgayLapHD = '" + sqlDate + "', ";
            qry += "MaNV = '" + hd.getMaNV() + "', ";
            qry += "MaKH = '" + hd.getMaKH() + "', ";
            qry += "ThanhTien = '" + hd.getThanhTien() + "', ";

            st = conn.createStatement();
            st.executeUpdate(qry);
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        }
    }

    public HoaDonDTO timKiemTheoMa(String maHD) {
        HoaDonDTO hd = null;
        try {
            String qry = "SELECT * FROM hoadon WHERE MaHD = '" + maHD + "'";
            
            st = conn.createStatement();
            rs = st.executeQuery(qry);

            if(rs.next()) {
                hd = new HoaDonDTO();
                hd.setMaHD(Integer.parseInt("MaHD"));
                hd.setNgayLapHD(rs.getDate("NgayLapHoaDon"));
                hd.setMaNV(Integer.parseInt("MaNV"));
                hd.setMaKH(Integer.parseInt("MaKH"));
                hd.setThanhTien(Integer.parseInt("ThanhTien"));
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        }
        return hd;
    }

    public ArrayList<HoaDonDTO> timKiemTheoNhanVien(String maNV) {
        ArrayList<HoaDonDTO> dshd = new ArrayList<>();
        try {
            String qry = "SELECT * FROM hoadon WHERE MaNV = '" + maNV + "'";

            st = conn.createStatement();
            rs = st.executeQuery(qry);

            while(rs.next()) {
                HoaDonDTO hd = new HoaDonDTO();
                hd.setMaHD(Integer.parseInt("MaHD"));
                hd.setNgayLapHD(rs.getDate("NgayLapHD"));
                hd.setMaNV(Integer.parseInt("MaNv"));
                hd.setMaKH(Integer.parseInt("MaKH"));
                hd.setThanhTien(Integer.parseInt("ThanhTien"));
            
                dshd.add(hd);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } cactch(Exception e) {}
            try {
                if(st != null)  st.close();
            } catch(Exception e) {}
        }
        return dshd;
    }
}
