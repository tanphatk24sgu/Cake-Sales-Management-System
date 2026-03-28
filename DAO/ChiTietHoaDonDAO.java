package DAO;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import Database.ConnectDatabase;
import DTO.ChiTietHoaDonDTO;

public class ChiTietHoaDonDAO {
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;

    public ArrayList<ChiTietHoaDonDTO> docDSCTHD() {
        ArrayList<ChiTietHoaDonDTO> dscthd = new ArrayList<>();
        try {
            conn = ConnectDatabase.getConnection();

            String qry = "SELECT * FROM chitiethoadon";

            st = conn.createStatement();
            rs = st.executeQuery(qry);

            while(rs.next()) {
                ChiTietHoaDonDTO cthd = new ChiTietHoaDonDTO();
                cthd.setMaHD(rs.getInt("MaHD"));
                cthd.setMaBanh(rs.getInt("MaBanh"));
                cthd.setSoLuong(rs.getInt("SoLuong"));
                cthd.setDonGia(rs.getDouble("DonGia"));
                cthd.setThanhTien(rs.getDouble("ThanhTien"));
                cthd.setDiem(rs.getInt("Diem"));

                dscthd.add(cthd);
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

        return dscthd;
    }

    public void them(ChiTietHoaDonDTO cthd) {
        String qry = "INSERT INTO chitiethoadon (MaHD, MaBanh, SoLuong, DonGia, Diem) VALUES (?, ?, ?, ?, ?)";
        try {
            conn = ConnectDatabase.getConnection();
            PreparedStatement pst = conn.prepareStatement(qry);
            pst.setInt(1, cthd.getMaHD());
            pst.setInt(2, cthd.getMaBanh());
            pst.setInt(3, cthd.getSoLuong());
            pst.setDouble(4, cthd.getDonGia());
            pst.setInt(5, cthd.getDiem());
            pst.executeUpdate();
            pst.close();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        }
    }

    public void xoa(int maHD, int maBanh) {
        String qry = "DELETE FROM chitiethoadon WHERE MaHD = ? AND MaBanh = ?";
        try {
            conn = ConnectDatabase.getConnection();
            PreparedStatement pst = conn.prepareStatement(qry);
            pst.setInt(1, maHD);
            pst.setInt(2, maBanh);
            pst.executeUpdate();
            pst.close();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        }
    }

    public void sua(ChiTietHoaDonDTO cthd) {
        String qry = "UPDATE chitiethoadon SET SoLuong = ?, DonGia = ?, Diem = ? WHERE MaHD = ? AND MaBanh = ?";
        try {
            conn = ConnectDatabase.getConnection();
            PreparedStatement pst = conn.prepareStatement(qry);
            pst.setInt(1, cthd.getSoLuong());
            pst.setDouble(2, cthd.getDonGia());
            pst.setInt(3, cthd.getDiem());
            pst.setInt(4, cthd.getMaHD());
            pst.setInt(5, cthd.getMaBanh());
            pst.executeUpdate();
            pst.close();
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        }
    }

    // TODO 
    // timKiemTheoHoaDon, timKiemTheoBanh
    // thongKeTheoThanhTien

    public ArrayList<ChiTietHoaDonDTO> timKiemTheoHoaDon(String maHD) {
        ArrayList<ChiTietHoaDonDTO> dscthd = new ArrayList<>();
        String qry = "SELECT * FROM chitiethoadon WHERE MaHD = ?";
        try {
            conn = ConnectDatabase.getConnection();
            PreparedStatement pst = conn.prepareStatement(qry);
            pst.setInt(1, Integer.parseInt(maHD));
            rs = pst.executeQuery();

            while(rs.next()) {
                ChiTietHoaDonDTO cthd = new ChiTietHoaDonDTO();
                cthd.setMaHD(rs.getInt("MaHD"));
                cthd.setMaBanh(rs.getInt("MaBanh"));
                cthd.setSoLuong(rs.getInt("SoLuong"));
                cthd.setDonGia(rs.getDouble("DonGia"));
                cthd.setThanhTien(rs.getDouble("ThanhTien"));
                cthd.setDiem(rs.getInt("Diem"));

                dscthd.add(cthd);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        } finally {
            try {
                if(rs != null)  rs.close();
            } catch(Exception e) {}
            try {
                if(st != null) st.close();
            } catch(Exception e) {}
        } 
        return dscthd;
    }

    public int thongKeSoLuongBan(int maBanh) {
        int tong = 0;
        try {
            String qry = "SELECT IFNULL(SUM(SoLuong), 0) AS Tong FROM chitiethoadon WHERE MaBanh = ?";

            conn = ConnectDatabase.getConnection();
            PreparedStatement pst = conn.prepareStatement(qry);
            pst.setInt(1, maBanh);
            rs = pst.executeQuery();

            if (rs.next()) {
                tong = rs.getInt("Tong");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        }
        return tong;
    }
}
