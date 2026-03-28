package BUS;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import DTO.CTPhieuNhapHangDTO;
import DTO.ChiTietHoaDonDTO;
import DTO.ExcelImportResultDTO;
import DTO.HoaDonDTO;
import DTO.PhieuNhapHangDTO;
import Database.ConnectDatabase;

public class ExcelImportExportBUS {

    private static final DateTimeFormatter DATE_TIME_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String[] HD_HEADERS = { "MaHD", "NgayLapHD", "MaNV", "MaKH", "ThanhTien" };
    private static final String[] CTHD_HEADERS = { "MaHD", "MaBanh", "SoLuong", "DonGia", "Diem" };
    private static final String[] PN_HEADERS = { "MaPhieuNhap", "Ngay", "MaNV", "MaNCC" };
    private static final String[] CTPN_HEADERS = { "MaPhieuNhap", "MaBanh", "MaNVL", "SoLuong", "DonGia", "TinhTrang" };

    public void exportHoaDonToExcel(File file) throws Exception {
        try (Workbook wb = new XSSFWorkbook(); Connection conn = ConnectDatabase.getConnection()) {
            Sheet shHd = wb.createSheet("HoaDon");
            Sheet shCt = wb.createSheet("ChiTietHoaDon");

            createHeader(shHd, HD_HEADERS);
            createHeader(shCt, CTHD_HEADERS);

            int rowIndex = 1;
            String sqlHd = "SELECT MaHD, NgayLapHD, MaNV, MaKH, ThanhTien FROM hoadon ORDER BY MaHD";
            try (PreparedStatement ps = conn.prepareStatement(sqlHd); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Row row = shHd.createRow(rowIndex++);
                    row.createCell(0).setCellValue(rs.getInt("MaHD"));
                    Timestamp ts = rs.getTimestamp("NgayLapHD");
                    row.createCell(1).setCellValue(ts == null ? "" : ts.toLocalDateTime().format(DATE_TIME_FMT));
                    row.createCell(2).setCellValue(rs.getInt("MaNV"));
                    row.createCell(3).setCellValue(rs.getInt("MaKH"));
                    row.createCell(4).setCellValue(rs.getDouble("ThanhTien"));
                }
            }

            rowIndex = 1;
            String sqlCt = "SELECT MaHD, MaBanh, SoLuong, DonGia, Diem FROM chitiethoadon ORDER BY MaHD, MaBanh";
            try (PreparedStatement ps = conn.prepareStatement(sqlCt); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Row row = shCt.createRow(rowIndex++);
                    row.createCell(0).setCellValue(rs.getInt("MaHD"));
                    row.createCell(1).setCellValue(rs.getInt("MaBanh"));
                    row.createCell(2).setCellValue(rs.getInt("SoLuong"));
                    row.createCell(3).setCellValue(rs.getDouble("DonGia"));
                    row.createCell(4).setCellValue(rs.getInt("Diem"));
                }
            }

            autosize(shHd, HD_HEADERS.length);
            autosize(shCt, CTHD_HEADERS.length);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
            }
        }
    }

    public void exportPhieuNhapToExcel(File file) throws Exception {
        try (Workbook wb = new XSSFWorkbook(); Connection conn = ConnectDatabase.getConnection()) {
            Sheet shPn = wb.createSheet("PhieuNhapHang");
            Sheet shCt = wb.createSheet("CTPhieuNhapHang");

            createHeader(shPn, PN_HEADERS);
            createHeader(shCt, CTPN_HEADERS);

            int rowIndex = 1;
            String sqlPn = "SELECT MaPhieuNhap, Ngay, MaNV, MaNCC FROM phieunhaphang ORDER BY MaPhieuNhap";
            try (PreparedStatement ps = conn.prepareStatement(sqlPn); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Row row = shPn.createRow(rowIndex++);
                    row.createCell(0).setCellValue(rs.getInt("MaPhieuNhap"));
                    Timestamp ts = rs.getTimestamp("Ngay");
                    row.createCell(1).setCellValue(ts == null ? "" : ts.toLocalDateTime().format(DATE_TIME_FMT));
                    row.createCell(2).setCellValue(rs.getInt("MaNV"));
                    row.createCell(3).setCellValue(rs.getInt("MaNCC"));
                }
            }

            rowIndex = 1;
            String sqlCt = "SELECT MaPhieuNhap, MaBanh, MaNVL, SoLuong, DonGia, TinhTrang FROM ct_phieunhaphang ORDER BY MaPhieuNhap, MaBanh, MaNVL";
            try (PreparedStatement ps = conn.prepareStatement(sqlCt); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Row row = shCt.createRow(rowIndex++);
                    row.createCell(0).setCellValue(rs.getInt("MaPhieuNhap"));
                    row.createCell(1).setCellValue(rs.getInt("MaBanh"));
                    row.createCell(2).setCellValue(rs.getInt("MaNVL"));
                    row.createCell(3).setCellValue(rs.getInt("SoLuong"));
                    row.createCell(4).setCellValue(rs.getDouble("DonGia"));
                    row.createCell(5).setCellValue(rs.getString("TinhTrang"));
                }
            }

            autosize(shPn, PN_HEADERS.length);
            autosize(shCt, CTPN_HEADERS.length);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
            }
        }
    }

    public void exportHoaDonTemplate(File file) throws Exception {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet shHd = wb.createSheet("HoaDon");
            Sheet shCt = wb.createSheet("ChiTietHoaDon");
            createHeader(shHd, HD_HEADERS);
            createHeader(shCt, CTHD_HEADERS);
            autosize(shHd, HD_HEADERS.length);
            autosize(shCt, CTHD_HEADERS.length);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
            }
        }
    }

    public void exportPhieuNhapTemplate(File file) throws Exception {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet shPn = wb.createSheet("PhieuNhapHang");
            Sheet shCt = wb.createSheet("CTPhieuNhapHang");
            createHeader(shPn, PN_HEADERS);
            createHeader(shCt, CTPN_HEADERS);
            autosize(shPn, PN_HEADERS.length);
            autosize(shCt, CTPN_HEADERS.length);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
            }
        }
    }

    public ExcelImportResultDTO importHoaDonFromExcel(File file) {
        ExcelImportResultDTO result = new ExcelImportResultDTO();

        ArrayList<HoaDonDTO> hoaDons = new ArrayList<>();
        ArrayList<ChiTietHoaDonDTO> chiTiets = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);
                Workbook wb = new XSSFWorkbook(fis);
                Connection conn = ConnectDatabase.getConnection()) {

            Sheet shHd = wb.getSheet("HoaDon");
            Sheet shCt = wb.getSheet("ChiTietHoaDon");

            if (shHd == null || shCt == null) {
                result.addError("Thiếu sheet bắt buộc: HoaDon hoặc ChiTietHoaDon.");
                return result;
            }

            validateHeaders(shHd, HD_HEADERS, result);
            validateHeaders(shCt, CTHD_HEADERS, result);
            if (result.hasErrors()) {
                return result;
            }

            DataFormatter formatter = new DataFormatter();
            Set<Integer> hdIdsInFile = new HashSet<>();
            Map<Integer, HoaDonDTO> hdMap = new HashMap<>();

            for (int r = 1; r <= shHd.getLastRowNum(); r++) {
                Row row = shHd.getRow(r);
                if (isEmptyRow(row)) {
                    continue;
                }

                try {
                    int maHD = parseInt(row.getCell(0), formatter, "HoaDon", r, "MaHD", result);
                    Timestamp ngayLap = parseTimestamp(row.getCell(1), formatter, "HoaDon", r, "NgayLapHD", result);
                    int maNV = parseInt(row.getCell(2), formatter, "HoaDon", r, "MaNV", result);
                    int maKH = parseInt(row.getCell(3), formatter, "HoaDon", r, "MaKH", result);
                    double thanhTien = parseDouble(row.getCell(4), formatter, "HoaDon", r, "ThanhTien", result);

                    if (!hdIdsInFile.add(maHD)) {
                        result.addError("[HoaDon row " + (r + 1) + "] Trùng MaHD trong file: " + maHD);
                    }

                    if (!existsById(conn, "nhanvien", "MaNV", maNV)) {
                        result.addError("[HoaDon row " + (r + 1) + "] MaNV không tồn tại: " + maNV);
                    }
                    if (!existsById(conn, "khachhang", "MaKH", maKH)) {
                        result.addError("[HoaDon row " + (r + 1) + "] MaKH không tồn tại: " + maKH);
                    }

                    HoaDonDTO hd = new HoaDonDTO();
                    hd.setMaHD(maHD);
                    hd.setNgayLapHD(ngayLap);
                    hd.setMaNV(maNV);
                    hd.setMaKH(maKH);
                    hd.setThanhTien(thanhTien);
                    hoaDons.add(hd);
                    hdMap.put(maHD, hd);
                } catch (Exception ignore) {
                    // lỗi đã được gom vào result
                }
            }

            Set<String> ctKeys = new HashSet<>();
            for (int r = 1; r <= shCt.getLastRowNum(); r++) {
                Row row = shCt.getRow(r);
                if (isEmptyRow(row)) {
                    continue;
                }

                try {
                    int maHD = parseInt(row.getCell(0), formatter, "ChiTietHoaDon", r, "MaHD", result);
                    int maBanh = parseInt(row.getCell(1), formatter, "ChiTietHoaDon", r, "MaBanh", result);
                    int soLuong = parseInt(row.getCell(2), formatter, "ChiTietHoaDon", r, "SoLuong", result);
                    double donGia = parseDouble(row.getCell(3), formatter, "ChiTietHoaDon", r, "DonGia", result);
                    int diem = parseInt(row.getCell(4), formatter, "ChiTietHoaDon", r, "Diem", result);

                    String key = maHD + "-" + maBanh;
                    if (!ctKeys.add(key)) {
                        result.addError("[ChiTietHoaDon row " + (r + 1) + "] Trùng khóa MaHD-MaBanh: " + key);
                    }

                    boolean hasParent = hdMap.containsKey(maHD) || existsById(conn, "hoadon", "MaHD", maHD);
                    if (!hasParent) {
                        result.addError("[ChiTietHoaDon row " + (r + 1) + "] MaHD không tồn tại: " + maHD);
                    }
                    if (!existsById(conn, "banh", "MaBanh", maBanh)) {
                        result.addError("[ChiTietHoaDon row " + (r + 1) + "] MaBanh không tồn tại: " + maBanh);
                    }
                    if (soLuong < 0 || donGia < 0) {
                        result.addError("[ChiTietHoaDon row " + (r + 1) + "] SoLuong/DonGia phải >= 0.");
                    }

                    ChiTietHoaDonDTO ct = new ChiTietHoaDonDTO();
                    ct.setMaHD(maHD);
                    ct.setMaBanh(maBanh);
                    ct.setSoLuong(soLuong);
                    ct.setDonGia(donGia);
                    ct.setThanhTien(soLuong * donGia);
                    ct.setDiem(diem);
                    chiTiets.add(ct);
                } catch (Exception ignore) {
                    // lỗi đã được gom vào result
                }
            }

            result.setTotalRows(hoaDons.size() + chiTiets.size());
            if (result.hasErrors()) {
                result.setSuccess(false);
                return result;
            }

            applyImportHoaDon(conn, hoaDons, chiTiets);

            result.setSuccessRows(result.getTotalRows());
            result.setSuccess(true);
            return result;
        } catch (Exception ex) {
            result.addError("Lỗi import hóa đơn: " + ex.getMessage());
            result.setSuccess(false);
            return result;
        }
    }

    public ExcelImportResultDTO importPhieuNhapFromExcel(File file) {
        ExcelImportResultDTO result = new ExcelImportResultDTO();

        ArrayList<PhieuNhapHangDTO> phieuNhaps = new ArrayList<>();
        ArrayList<CTPhieuNhapHangDTO> chiTiets = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(file);
                Workbook wb = new XSSFWorkbook(fis);
                Connection conn = ConnectDatabase.getConnection()) {

            Sheet shPn = wb.getSheet("PhieuNhapHang");
            Sheet shCt = wb.getSheet("CTPhieuNhapHang");

            if (shPn == null || shCt == null) {
                result.addError("Thiếu sheet bắt buộc: PhieuNhapHang hoặc CTPhieuNhapHang.");
                return result;
            }

            validateHeaders(shPn, PN_HEADERS, result);
            validateHeaders(shCt, CTPN_HEADERS, result);
            if (result.hasErrors()) {
                return result;
            }

            DataFormatter formatter = new DataFormatter();
            Set<Integer> pnIdsInFile = new HashSet<>();
            Set<Integer> pnMap = new HashSet<>();

            for (int r = 1; r <= shPn.getLastRowNum(); r++) {
                Row row = shPn.getRow(r);
                if (isEmptyRow(row)) {
                    continue;
                }

                try {
                    int maPn = parseInt(row.getCell(0), formatter, "PhieuNhapHang", r, "MaPhieuNhap", result);
                    Timestamp ngay = parseTimestamp(row.getCell(1), formatter, "PhieuNhapHang", r, "Ngay", result);
                    int maNV = parseInt(row.getCell(2), formatter, "PhieuNhapHang", r, "MaNV", result);
                    int maNCC = parseInt(row.getCell(3), formatter, "PhieuNhapHang", r, "MaNCC", result);

                    if (!pnIdsInFile.add(maPn)) {
                        result.addError("[PhieuNhapHang row " + (r + 1) + "] Trùng MaPhieuNhap trong file: " + maPn);
                    }

                    if (!existsById(conn, "nhanvien", "MaNV", maNV)) {
                        result.addError("[PhieuNhapHang row " + (r + 1) + "] MaNV không tồn tại: " + maNV);
                    }
                    if (!existsById(conn, "nhacungcap", "MaNCC", maNCC)) {
                        result.addError("[PhieuNhapHang row " + (r + 1) + "] MaNCC không tồn tại: " + maNCC);
                    }

                    PhieuNhapHangDTO pn = new PhieuNhapHangDTO();
                    pn.setMaPhieuNhap(maPn);
                    pn.setNgay(ngay);
                    pn.setMaNV(maNV);
                    pn.setMaNCC(maNCC);
                    phieuNhaps.add(pn);
                    pnMap.add(maPn);
                } catch (Exception ignore) {
                    // lỗi đã được gom vào result
                }
            }

            Set<String> ctKeys = new HashSet<>();
            for (int r = 1; r <= shCt.getLastRowNum(); r++) {
                Row row = shCt.getRow(r);
                if (isEmptyRow(row)) {
                    continue;
                }

                try {
                    int maPn = parseInt(row.getCell(0), formatter, "CTPhieuNhapHang", r, "MaPhieuNhap", result);
                    int maBanh = parseInt(row.getCell(1), formatter, "CTPhieuNhapHang", r, "MaBanh", result);
                    int maNVL = parseInt(row.getCell(2), formatter, "CTPhieuNhapHang", r, "MaNVL", result);
                    int soLuong = parseInt(row.getCell(3), formatter, "CTPhieuNhapHang", r, "SoLuong", result);
                    double donGia = parseDouble(row.getCell(4), formatter, "CTPhieuNhapHang", r, "DonGia", result);
                    String tinhTrang = formatter.formatCellValue(row.getCell(5)).trim();

                    String key = maPn + "-" + maBanh + "-" + maNVL;
                    if (!ctKeys.add(key)) {
                        result.addError("[CTPhieuNhapHang row " + (r + 1) + "] Trùng khóa MaPhieuNhap-MaBanh-MaNVL: " + key);
                    }

                    boolean hasParent = pnMap.contains(maPn) || existsById(conn, "phieunhaphang", "MaPhieuNhap", maPn);
                    if (!hasParent) {
                        result.addError("[CTPhieuNhapHang row " + (r + 1) + "] MaPhieuNhap không tồn tại: " + maPn);
                    }
                    if (!existsById(conn, "banh", "MaBanh", maBanh)) {
                        result.addError("[CTPhieuNhapHang row " + (r + 1) + "] MaBanh không tồn tại: " + maBanh);
                    }
                    if (!existsById(conn, "nguyenlieu", "MaNL", maNVL)) {
                        result.addError("[CTPhieuNhapHang row " + (r + 1) + "] MaNVL không tồn tại: " + maNVL);
                    }
                    if (soLuong < 0 || donGia < 0) {
                        result.addError("[CTPhieuNhapHang row " + (r + 1) + "] SoLuong/DonGia phải >= 0.");
                    }

                    CTPhieuNhapHangDTO ct = new CTPhieuNhapHangDTO();
                    ct.setMaPhieuNhap(maPn);
                    ct.setMaBanh(maBanh);
                    ct.setMaNVL(maNVL);
                    ct.setSoLuong(soLuong);
                    ct.setDonGia(donGia);
                    ct.setThanhTien(soLuong * donGia);
                    ct.setTinhTrang(tinhTrang);
                    chiTiets.add(ct);
                } catch (Exception ignore) {
                    // lỗi đã được gom vào result
                }
            }

            result.setTotalRows(phieuNhaps.size() + chiTiets.size());
            if (result.hasErrors()) {
                result.setSuccess(false);
                return result;
            }

            applyImportPhieuNhap(conn, phieuNhaps, chiTiets);

            result.setSuccessRows(result.getTotalRows());
            result.setSuccess(true);
            return result;
        } catch (Exception ex) {
            result.addError("Lỗi import phiếu nhập: " + ex.getMessage());
            result.setSuccess(false);
            return result;
        }
    }

    private void applyImportHoaDon(Connection conn, ArrayList<HoaDonDTO> hoaDons, ArrayList<ChiTietHoaDonDTO> chiTiets)
            throws Exception {
        boolean oldAutoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);
        try {
            String upsertHd = "INSERT INTO hoadon (MaHD, NgayLapHD, MaNV, MaKH, ThanhTien) VALUES (?, ?, ?, ?, ?) "
                    + "ON DUPLICATE KEY UPDATE NgayLapHD = VALUES(NgayLapHD), MaNV = VALUES(MaNV), "
                    + "MaKH = VALUES(MaKH), ThanhTien = VALUES(ThanhTien)";

            try (PreparedStatement ps = conn.prepareStatement(upsertHd)) {
                for (HoaDonDTO hd : hoaDons) {
                    ps.setInt(1, hd.getMaHD());
                    ps.setTimestamp(2, new Timestamp(hd.getNgayLapHD().getTime()));
                    ps.setInt(3, hd.getMaNV());
                    ps.setInt(4, hd.getMaKH());
                    ps.setDouble(5, hd.getThanhTien());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            String deleteCt = "DELETE FROM chitiethoadon WHERE MaHD = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteCt)) {
                for (HoaDonDTO hd : hoaDons) {
                    ps.setInt(1, hd.getMaHD());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            String insertCt = "INSERT INTO chitiethoadon (MaHD, MaBanh, SoLuong, DonGia, Diem) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertCt)) {
                for (ChiTietHoaDonDTO ct : chiTiets) {
                    ps.setInt(1, ct.getMaHD());
                    ps.setInt(2, ct.getMaBanh());
                    ps.setInt(3, ct.getSoLuong());
                    ps.setDouble(4, ct.getDonGia());
                    ps.setInt(5, ct.getDiem());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
        } catch (Exception ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(oldAutoCommit);
        }
    }

    private void applyImportPhieuNhap(Connection conn, ArrayList<PhieuNhapHangDTO> phieuNhaps,
            ArrayList<CTPhieuNhapHangDTO> chiTiets) throws Exception {
        boolean oldAutoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);
        try {
            String upsertPn = "INSERT INTO phieunhaphang (MaPhieuNhap, Ngay, MaNV, MaNCC) VALUES (?, ?, ?, ?) "
                    + "ON DUPLICATE KEY UPDATE Ngay = VALUES(Ngay), MaNV = VALUES(MaNV), MaNCC = VALUES(MaNCC)";

            try (PreparedStatement ps = conn.prepareStatement(upsertPn)) {
                for (PhieuNhapHangDTO pn : phieuNhaps) {
                    ps.setInt(1, pn.getMaPhieuNhap());
                    ps.setTimestamp(2, new Timestamp(pn.getNgay().getTime()));
                    ps.setInt(3, pn.getMaNV());
                    ps.setInt(4, pn.getMaNCC());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            String deleteCt = "DELETE FROM ct_phieunhaphang WHERE MaPhieuNhap = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteCt)) {
                for (PhieuNhapHangDTO pn : phieuNhaps) {
                    ps.setInt(1, pn.getMaPhieuNhap());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            String insertCt = "INSERT INTO ct_phieunhaphang (MaPhieuNhap, MaBanh, MaNVL, SoLuong, DonGia, TinhTrang) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertCt)) {
                for (CTPhieuNhapHangDTO ct : chiTiets) {
                    ps.setInt(1, ct.getMaPhieuNhap());
                    ps.setInt(2, ct.getMaBanh());
                    ps.setInt(3, ct.getMaNVL());
                    ps.setInt(4, ct.getSoLuong());
                    ps.setDouble(5, ct.getDonGia());
                    ps.setString(6, ct.getTinhTrang());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            conn.commit();
        } catch (Exception ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(oldAutoCommit);
        }
    }

    private boolean existsById(Connection conn, String table, String idCol, int value) throws Exception {
        String sql = "SELECT 1 FROM " + table + " WHERE " + idCol + " = ? LIMIT 1";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private void createHeader(Sheet sheet, String[] headers) {
        Row header = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            header.createCell(i).setCellValue(headers[i]);
        }
    }

    private void autosize(Sheet sheet, int colCount) {
        for (int i = 0; i < colCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }

    private void validateHeaders(Sheet sheet, String[] expected, ExcelImportResultDTO result) {
        Row row = sheet.getRow(0);
        if (row == null) {
            result.addError("Sheet " + sheet.getSheetName() + " không có dòng header.");
            return;
        }

        DataFormatter formatter = new DataFormatter();
        for (int i = 0; i < expected.length; i++) {
            String actual = formatter.formatCellValue(row.getCell(i)).trim();
            if (!expected[i].equalsIgnoreCase(actual)) {
                result.addError("Sheet " + sheet.getSheetName() + " sai header cột " + (i + 1)
                        + ": cần '" + expected[i] + "', thực tế '" + actual + "'.");
            }
        }
    }

    private boolean isEmptyRow(Row row) {
        if (row == null) {
            return true;
        }
        int last = row.getLastCellNum();
        if (last < 0) {
            return true;
        }

        DataFormatter formatter = new DataFormatter();
        for (int i = 0; i < last; i++) {
            Cell cell = row.getCell(i);
            if (cell != null && !formatter.formatCellValue(cell).trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private int parseInt(Cell cell, DataFormatter formatter, String sheet, int rowIdx, String col,
            ExcelImportResultDTO result) {
        String raw = formatter.formatCellValue(cell).trim();
        if (raw.isEmpty()) {
            result.addError("[" + sheet + " row " + (rowIdx + 1) + "] " + col + " không được để trống.");
            throw new IllegalArgumentException();
        }
        try {
            return Integer.parseInt(raw.replace(",", ""));
        } catch (NumberFormatException ex) {
            result.addError("[" + sheet + " row " + (rowIdx + 1) + "] " + col + " phải là số nguyên.");
            throw ex;
        }
    }

    private double parseDouble(Cell cell, DataFormatter formatter, String sheet, int rowIdx, String col,
            ExcelImportResultDTO result) {
        String raw = formatter.formatCellValue(cell).trim();
        if (raw.isEmpty()) {
            result.addError("[" + sheet + " row " + (rowIdx + 1) + "] " + col + " không được để trống.");
            throw new IllegalArgumentException();
        }
        try {
            String normalized = raw.replace(",", "");
            return Double.parseDouble(normalized);
        } catch (NumberFormatException ex) {
            result.addError("[" + sheet + " row " + (rowIdx + 1) + "] " + col + " phải là số.");
            throw ex;
        }
    }

    private Timestamp parseTimestamp(Cell cell, DataFormatter formatter, String sheet, int rowIdx, String col,
            ExcelImportResultDTO result) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            result.addError("[" + sheet + " row " + (rowIdx + 1) + "] " + col + " không được để trống.");
            throw new IllegalArgumentException();
        }

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return new Timestamp(cell.getDateCellValue().getTime());
        }

        String raw = formatter.formatCellValue(cell).trim();
        if (raw.isEmpty()) {
            result.addError("[" + sheet + " row " + (rowIdx + 1) + "] " + col + " không được để trống.");
            throw new IllegalArgumentException();
        }

        try {
            LocalDateTime ldt = LocalDateTime.parse(raw, DATE_TIME_FMT);
            return Timestamp.valueOf(ldt);
        } catch (DateTimeParseException ex) {
            result.addError("[" + sheet + " row " + (rowIdx + 1) + "] " + col
                    + " sai định dạng, cần yyyy-MM-dd HH:mm:ss.");
            throw ex;
        }
    }
}
