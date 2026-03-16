package DAO;

import java.sql.*;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import Database.ConnectDatabase;
import DTO.NhanVienDTO;

public class NhanVienDAO {
    Connection conn = null;
    Statement st = null;
    ResultSet rs = null;

    public ArrayList<NhanVienDTO> docDSNV() {
        ArrayList<NhanVienDTO> dsnv = new ArrayList<>();
        try {
            conn = ConnectDatabase.getInstance().getConnection();
            
            String qry = "SELECT * FROM nhanvien";     
            
            st = conn.createStatement();
            rs = st.executeQuery(qry);

            while(rs.next()) {
                NhanVienDTO nv = new NhanVienDTO();
                nv.setMaNV(rs.getInt("MaNV"));
                nv.setHo(rs.getString("Ho"));
                nv.setTen(rs.getString("Ten"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setLuongCoBan(rs.getDouble("LuongCoBan"));
                nv.setChucVu(rs.getInt("ChucVu"));

                dsnv.add(nv);
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

        return dsnv;
    }

    public void them(NhanVienDTO nv) {
        try {
            conn = ConnectDatabase.getInstance().getConnection();
            String qry = "INSERT INTO nhanvien (Ho, Ten, NgaySinh, LuongCoBan, ChucVu) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(qry);
            pst.setString(1, nv.getHo());
            pst.setString(2, nv.getTen());
            pst.setDate(3, new java.sql.Date(nv.getNgaySinh().getTime()));
            pst.setDouble(4, nv.getLuongCoBan());
            pst.setInt(5, nv.getChucVu());
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

    public void sua(NhanVienDTO nv) {
        try {
            conn = ConnectDatabase.getInstance().getConnection();
            String qry = "UPDATE nhanvien SET Ho=?, Ten=?, NgaySinh=?, LuongCoBan=?, ChucVu=? WHERE MaNV=?";
            PreparedStatement pst = conn.prepareStatement(qry);
            pst.setString(1, nv.getHo());
            pst.setString(2, nv.getTen());
            pst.setDate(3, new java.sql.Date(nv.getNgaySinh().getTime()));
            pst.setDouble(4, nv.getLuongCoBan());
            pst.setInt(5, nv.getChucVu());
            pst.setInt(6, nv.getMaNV());
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

    public void xoa(int maNV) {
        try {
            conn = ConnectDatabase.getInstance().getConnection();
            String qry = "DELETE FROM nhanvien WHERE MaNV=?";
            PreparedStatement pst = conn.prepareStatement(qry);
            pst.setInt(1, maNV);
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

    public NhanVienDTO timKiemTheoMa(int maNV) {
        NhanVienDTO nv = null;
        try {
            conn = ConnectDatabase.getInstance().getConnection();
            String qry = "SELECT * FROM nhanvien WHERE MaNV = ?";
            PreparedStatement pst = conn.prepareStatement(qry);
            pst.setInt(1, maNV);
            rs = pst.executeQuery();

            if(rs.next()) {
                nv = new NhanVienDTO();
                nv.setMaNV(rs.getInt("MaNV"));
                nv.setHo(rs.getString("Ho"));
                nv.setTen(rs.getString("Ten"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setLuongCoBan(rs.getDouble("LuongCoBan"));
                nv.setChucVu(rs.getInt("ChucVu"));
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
        return nv;
    }
    public NhanVienDTO timKiemTheoTen(String tenNV){
        NhanVienDTO nv = null;
        try{
            conn=ConnectDatabase.getInstance().getConnection();
            String qry="SELECT* FROM nhanvien where tenNV=?";
            PreparedStatement pst=conn.prepareStatement(qry);
            pst.setString(1, tenNV);
            rs =pst.executeQuery();
        
            if(rs.next()){
                nv =new NhanVienDTO();
                nv.setMaNV(rs.getInt("MaNV"));
                nv.setHo(rs.getString("Ho"));
                nv.setTen(rs.getString("Ten"));
                nv.setNgaySinh(rs.getDate("NgaySinh"));
                nv.setChucVu(rs.getInt("ChucVu"));
                nv.setLuongCoBan(rs.getDouble("LuongCoBan"));
                pst.close();
             }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "SQL Error:" + e.getMessage());
        } finally{
            try{
                if(rs!=null) rs.close();
            } catch(Exception e){}
            try{
                if(rs!=null) conn.close();
            } catch(Exception e){}
        }
        return nv;
    }
}
