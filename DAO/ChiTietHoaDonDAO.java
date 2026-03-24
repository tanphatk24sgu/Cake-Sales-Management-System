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
            conn = ConnectDatabase.getInstance().getConnection();

            String qry = "SELECT * FROM chitiethoa";

            st = conn.createStatement();
            rs = st.executeQuery(qry);

            while(rs.next()) {
                ChiTietHoaDonDTO cthd = new ChiTietHoaDonDTO();
                cthd.setMaHD(Integer.parseInt("MaHD"));
                cthd.setMaBanh(Integer.parseInt("MaBanh"));
                cthd.setSoLuong(Integer.parseInt("SoLuong"));
                cthd.setDonGia(Double.parseDouble("DonGia"));
                cthd.setThanhTien(Double.parseDouble("ThanhTien"));
                cthd.setDiem(Integer.parseInt("Diem"));

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
        try {
            String qry = "INSERT INTO chitiethoadon VALUES('";
            qry += cthd.getMaHD() + "', ";
            qry += cthd.getMaBanh() + "', ";
            qry += cthd.getSoLuong() + "', ";
            qry += cthd.getDonGia() + "', ";
            qry += cthd.getThanhTien() + "', ";
            qry += cthd.getDiem() + "')";

            st = conn.createStatement();
            st.executeUpdate(qry);
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        }
    }

    public void xoa(int maHD, int maBanh) {
        try {
            String qry = "DELETE FROM chitiethoadon WHERE MaHD = '" + maHD + "' AND MaBanh = '" + maBanh + "'";
            st = conn.createStatement();
            st.executeUpdate(qry);
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        }
    }

    public void sua(ChiTietHoaDonDTO cthd) {
        try {
            String qry = "UPDATE chitiethoadon SET ";

            st = conn.createStatement();
            st.executeUpdate(qry);
        } catch(SQLException e) {}
    }

    // TODO 
    // timKiemTheoHoaDon, timKiemTheoBanh
    // thongKeTheoThanhTien

    public ArrayList<ChiTietHoaDonDTO> timKiemTheoHoaDon(String maHD) {
        ArrayList<ChiTietHoaDonDTO> dscthd = new ArrayList<>();
        try {
            String qry = "SELECT * FROM chitiethoadon WHERE maHD = '" + maHD + "'";

            st = conn.createStatement();
            rs = st.executeQuery(qry);

            while(rs.next()) {
                ChiTietHoaDonDTO cthd = new ChiTietHoaDonDTO();
                cthd.setMaHD(Integer.parseInt("MaHD"));
                cthd.setMaBanh(Integer.parseInt("MaBanh"));
                cthd.setSoLuong(Integer.parseInt("SoLuong"));
                cthd.setDonGia(Double.parseDouble("DonGia"));
                cthd.setThanhTien(Double.parseDouble("ThanhTien"));
                cthd.setDiem(Integer.parseInt("Diem"));

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
}