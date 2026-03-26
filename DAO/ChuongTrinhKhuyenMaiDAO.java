package DAO;

import DTO.ChuongTrinhKhuyenMaiDTO;
import Database.ConnectDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;

// Truy cập bảng chương trình khuyến mãi (JOIN banh để lấy tên bánh mua/tặng)
public class ChuongTrinhKhuyenMaiDAO {

    private Connection conn;

    public ChuongTrinhKhuyenMaiDAO() {
        conn = ConnectDatabase.getConnection();
    }

    private ChuongTrinhKhuyenMaiDTO mapRow(ResultSet rs) throws Exception {
        ChuongTrinhKhuyenMaiDTO d = new ChuongTrinhKhuyenMaiDTO();
        d.setMaKM(rs.getInt("MaKM"));
        d.setTenCTKM(rs.getString("TenCTKM"));
        Timestamp tsBd = rs.getTimestamp("NgayBatDau");
        d.setNgayBatDau(tsBd != null ? new Date(tsBd.getTime()) : new Date());
        Timestamp tsKt = rs.getTimestamp("NgayKetThuc");
        d.setNgayKetThuc(tsKt != null ? new Date(tsKt.getTime()) : new Date());
        String gc = rs.getString("GhiChu");
        d.setGhiChu(gc != null ? gc : "");

        String loai = rs.getString("LoaiKhuyenMai");
        if (loai != null) {
            loai = loai.trim();
        }
        d.setLoaiKhuyenMai(loai != null && !loai.isEmpty() ? loai : "Giảm phần trăm");
        d.setPhanTramGiam(rs.getInt("PhanTramGiam"));
        d.setDieuKienToiThieu(rs.getDouble("DieuKienToiThieu"));
        d.setSoLuongMua(rs.getInt("SoLuongMua"));
        d.setSoLuongTang(rs.getInt("SoLuongTang"));

        int mbm = rs.getInt("MaBanhMua");
        d.setMaBanhMua(rs.wasNull() ? 0 : mbm);
        int mbt = rs.getInt("MaBanhTang");
        d.setMaBanhTang(rs.wasNull() ? 0 : mbt);

        String tbm = rs.getString("TenBanhMuaJT");
        d.setTenBanhMua(tbm != null ? tbm : "");
        String tbt = rs.getString("TenBanhTangJT");
        d.setTenBanhTang(tbt != null ? tbt : "");

        return d;
    }

    public ArrayList<ChuongTrinhKhuyenMaiDTO> getAll() {
        ArrayList<ChuongTrinhKhuyenMaiDTO> list = new ArrayList<>();
        if (conn == null) {
            return list;
        }
        String sql = "SELECT c.MaKM, c.TenCTKM, c.NgayBatDau, c.NgayKetThuc, c.GhiChu, "
                + "c.LoaiKhuyenMai, c.PhanTramGiam, c.DieuKienToiThieu, c.SoLuongMua, c.SoLuongTang, "
                + "c.MaBanhMua, c.MaBanhTang, "
                + "bm.TenBanh AS TenBanhMuaJT, bt.TenBanh AS TenBanhTangJT "
                + "FROM chuongtrinhkhuyenmai c "
                + "LEFT JOIN banh bm ON c.MaBanhMua = bm.MaBanh "
                + "LEFT JOIN banh bt ON c.MaBanhTang = bt.MaBanh "
                + "ORDER BY c.MaKM";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            if (laLoiThieuCot(e)) {
                return getAllBanCu5Cot();
            }
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private static boolean laLoiThieuCot(SQLException e) {
        String msg = e.getMessage();
        return msg != null && (msg.contains("Unknown column") || msg.contains("doesn't exist"));
    }

    /**
     * Bảng KM thiếu cột mở rộng (schema cũ) — chỉ đọc 5 cột gốc, còn lại mặc định.
     */
    private ArrayList<ChuongTrinhKhuyenMaiDTO> getAllBanCu5Cot() {
        ArrayList<ChuongTrinhKhuyenMaiDTO> list = new ArrayList<>();
        if (conn == null) {
            return list;
        }
        String sql = "SELECT MaKM, TenCTKM, NgayBatDau, NgayKetThuc, GhiChu FROM chuongtrinhkhuyenmai ORDER BY MaKM";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ChuongTrinhKhuyenMaiDTO d = new ChuongTrinhKhuyenMaiDTO();
                d.setMaKM(rs.getInt("MaKM"));
                d.setTenCTKM(rs.getString("TenCTKM"));
                Timestamp tsBd = rs.getTimestamp("NgayBatDau");
                d.setNgayBatDau(tsBd != null ? new Date(tsBd.getTime()) : new Date());
                Timestamp tsKt = rs.getTimestamp("NgayKetThuc");
                d.setNgayKetThuc(tsKt != null ? new Date(tsKt.getTime()) : new Date());
                String gc = rs.getString("GhiChu");
                d.setGhiChu(gc != null ? gc : "");
                d.setLoaiKhuyenMai("Giảm phần trăm");
                d.setPhanTramGiam(0);
                d.setDieuKienToiThieu(0);
                d.setSoLuongMua(0);
                d.setSoLuongTang(0);
                d.setMaBanhMua(0);
                d.setMaBanhTang(0);
                d.setTenBanhMua("");
                d.setTenBanhTang("");
                list.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insert(ChuongTrinhKhuyenMaiDTO dto) {
        if (conn == null) {
            return -1;
        }
        String sql = "INSERT INTO chuongtrinhkhuyenmai (TenCTKM, NgayBatDau, NgayKetThuc, GhiChu, "
                + "LoaiKhuyenMai, PhanTramGiam, DieuKienToiThieu, SoLuongMua, SoLuongTang, MaBanhMua, MaBanhTang) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, dto.getTenCTKM());
            ps.setTimestamp(2, new Timestamp(dto.getNgayBatDau().getTime()));
            ps.setTimestamp(3, new Timestamp(dto.getNgayKetThuc().getTime()));
            ps.setString(4, dto.getGhiChu() != null ? dto.getGhiChu() : "");
            ps.setString(5, dto.getLoaiKhuyenMai());
            ps.setInt(6, dto.getPhanTramGiam());
            ps.setDouble(7, dto.getDieuKienToiThieu());
            ps.setInt(8, dto.getSoLuongMua());
            ps.setInt(9, dto.getSoLuongTang());
            setNullableInt(ps, 10, dto.getMaBanhMua());
            setNullableInt(ps, 11, dto.getMaBanhTang());
            int n = ps.executeUpdate();
            if (n <= 0) {
                return -1;
            }
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean update(ChuongTrinhKhuyenMaiDTO dto) {
        if (conn == null) {
            return false;
        }
        String sql = "UPDATE chuongtrinhkhuyenmai SET TenCTKM=?, NgayBatDau=?, NgayKetThuc=?, GhiChu=?, "
                + "LoaiKhuyenMai=?, PhanTramGiam=?, DieuKienToiThieu=?, SoLuongMua=?, SoLuongTang=?, "
                + "MaBanhMua=?, MaBanhTang=? WHERE MaKM=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, dto.getTenCTKM());
            ps.setTimestamp(2, new Timestamp(dto.getNgayBatDau().getTime()));
            ps.setTimestamp(3, new Timestamp(dto.getNgayKetThuc().getTime()));
            ps.setString(4, dto.getGhiChu() != null ? dto.getGhiChu() : "");
            ps.setString(5, dto.getLoaiKhuyenMai());
            ps.setInt(6, dto.getPhanTramGiam());
            ps.setDouble(7, dto.getDieuKienToiThieu());
            ps.setInt(8, dto.getSoLuongMua());
            ps.setInt(9, dto.getSoLuongTang());
            setNullableInt(ps, 10, dto.getMaBanhMua());
            setNullableInt(ps, 11, dto.getMaBanhTang());
            ps.setInt(12, dto.getMaKM());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int maKM) {
        if (conn == null) {
            return false;
        }
        try {
            try (PreparedStatement psCt = conn.prepareStatement(
                    "DELETE FROM ct_chuongtrinhkhuyenmai WHERE MaKM=?")) {
                psCt.setInt(1, maKM);
                psCt.executeUpdate();
            } catch (SQLException ignored) {
                try (PreparedStatement psCt2 = conn.prepareStatement(
                        "DELETE FROM CT_CHUONGTRINHKHUYENMAI WHERE MaKM=?")) {
                    psCt2.setInt(1, maKM);
                    psCt2.executeUpdate();
                } catch (SQLException ignored2) {
                    // Bỏ qua nếu không có bảng chi tiết / tên khác phiên bản
                }
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM chuongtrinhkhuyenmai WHERE MaKM=?")) {
                ps.setInt(1, maKM);
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM CHUONGTRINHKHUYENMAI WHERE MaKM=?")) {
                ps.setInt(1, maKM);
                return ps.executeUpdate() > 0;
            } catch (SQLException e2) {
                e2.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void setNullableInt(PreparedStatement ps, int index, int value) throws Exception {
        if (value <= 0) {
            ps.setNull(index, Types.INTEGER);
        } else {
            ps.setInt(index, value);
        }
    }
}
