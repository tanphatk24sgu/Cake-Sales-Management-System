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
                hd.setMaHD(rs.getInt("MaHD"));
                hd.setNgayLapHD(rs.getDate("NgayLapHD"));
                hd.setMaNV(rs.getInt("MaNV"));
                hd.setMaKH(rs.getInt("MaKH"));
                hd.setThanhTien(rs.getDouble("ThanhTien"));

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

    public int them(HoaDonDTO hd) {
        int maHDMoi = -1;
        try {
            java.sql.Date sqlDate = new Date(hd.getNgayLapHD().getTime());

            String qry = "INSERT INTO hoadon VALUES('";
            qry += hd.getMaHD() + "', '";
            qry += sqlDate + "', '";
            qry += hd.getMaNV() + "', '";
            qry += hd.getMaKH() + "', '";
            qry += hd.getThanhTien() + "')";

            st = conn.createStatement();
            st.executeUpdate(qry, Statement.RETURN_GENERATED_KEYS);

            rs = st.getGeneratedKeys();
            if(rs.next()) {
                maHDMoi = rs.getInt(1);
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        }
        return maHDMoi;
    }

    public void xoa(int maHD) {
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

    public HoaDonDTO timKiemTheoMa(int maHD) {
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

    public ArrayList<HoaDonDTO> timKiemTheoNhanVien(int maNV) {
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
            } catch(Exception e) {}
            try {
                if(st != null)  st.close();
            } catch(Exception e) {}
        }
        return dshd;
    }

    // TODO
    // thongKeTheoNgay, thongKeTheoQuy
    public double thongKeDoanhThuTheoNgay() {
        double doanhThu = 0;
        try {
            String qry = "SELECT SUM(TongTien) AS DoanhThu FROM hoadon WHERE DATE(NgayLapHD) = CURDATE()";
            
            st = conn.createStatement();
            rs = st.executeQuery(qry);
        
            if(rs.next()) {
                doanhThu = rs.getDouble("DoanhThu");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        }
        return doanhThu;
    }

    public double thongKeDoanhThuTheoThang(int thang, int nam) {
        double doanhThu = 0;
        try {
            String qry = "SELECT SUM(TongTien) AS DoanhThu FROM hoadon "
             + "WHERE MONTH(NgayLapHD) = " + thang
             + " AND YEAR(NgayLapHD) = " + nam;
            
            st = conn.createStatement();
            rs = st.executeQuery(qry);

            if(rs.next()) {
                doanhThu = rs.getDouble("DoanhThu");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        }
        return doanhThu;
    }

    public double thongKeDoanhThuTheoQuy(int quy, int nam) {
        double doanhThu = 0;
        try {
            String qry = "SELECT SUM(TongTien) AS DoanhThu FROM hoadon "
             + "WHERE QUARTER(NgayLapHD) = " + quy
             + " AND YEAR(NgayLapHD) = " + nam;

            st = conn.createStatement();
            rs = st.executeQuery(qry);

            if(rs.next()) {
                doanhThu = rs.getDouble("DoanhThu");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        }
        return doanhThu;
    }

    public double thongKeDoanhThuTheoNam(int nam) {
        double doanhThu = 0;
        try {
            String qry = "SELECT SUM(TongTien) AS DoanhThu FROM hoadon "
             + "WHERE YEAR(NgayLapHD) = " + nam;

            st = conn.createStatement();
            rs = st.executeQuery(qry);

            if(rs.next()) {
                doanhThu = rs.getDouble("DoanhThu");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        }
        return doanhThu;
    }

    public double thongKeDoanhThuTheoKhoangNgay(Date tuNgay, Date denNgay) {
        double doanhThu = 0;
        try {
            String qry = "SELECT IFNULL(SUM(TongTien), 0) AS DoanhThu "
             + "FROM hoadon WHERE DATE(NgayLapHD) BETWEEN '"
             + tuNgay + "' AND '" + denNgay + "'";

            st = conn.createStatement();
            rs = st.executeQuery(qry);

            if(rs.next()) {
                doanhThu = rs.getDouble("DoanhThu");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "SQL Error: " + e.getMessage());
        }
        return doanhThu;
    }

}
