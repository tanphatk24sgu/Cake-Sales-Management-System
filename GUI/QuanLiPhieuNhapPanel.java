import javax.swing.*;
import javax.swing.table.*;

import BUS.ExcelImportExportBUS;
import DTO.ExcelImportResultDTO;
import Database.ConnectDatabase;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class QuanLiPhieuNhapPanel extends JPanel {
    private JTable tblPhieuNhap;
    private DefaultTableModel phieuNhapModel;
    private JButton btnExportErrors;

    private final List<String> lastImportErrors = new ArrayList<>();

    private final Color primaryColor = new Color(236, 72, 153);
    private final Color primaryDark = new Color(190, 24, 93);
    private final Color primaryLight = new Color(251, 207, 232);
    private final Color bgColor = new Color(249, 250, 251);

    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 22);
    private final Font headerFont = new Font("Segoe UI", Font.BOLD, 16);
    private final Font normalFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font boldFont = new Font("Segoe UI", Font.BOLD, 14);

    public QuanLiPhieuNhapPanel() {
        setBackground(bgColor);
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);

        loadPhieuNhap();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setBackground(bgColor);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setBackground(bgColor);

        JLabel icon = new JLabel("📥");
        JLabel title = new JLabel("QUẢN LÍ PHIẾU NHẬP HÀNG");
        title.setFont(titleFont);
        title.setForeground(primaryColor);

        titlePanel.add(icon);
        titlePanel.add(title);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        JLabel lblDate = new JLabel("📅 " + sdf.format(new Date()));
        lblDate.setFont(normalFont);
        lblDate.setForeground(new Color(107, 114, 128));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        right.setBackground(bgColor);
        right.add(lblDate);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    private JPanel createMainContent() {
        JPanel main = new JPanel(new BorderLayout(0, 12));
        main.setBackground(bgColor);

        JPanel topActions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        topActions.setBackground(bgColor);

        JButton btnAdd = createStyledButton("+ Thêm PN", new Color(34, 197, 94), 120);
        JButton btnEdit = createStyledButton("✎ Sửa PN", new Color(59, 130, 246), 110);
        JButton btnDelete = createStyledButton("✕ Xóa PN", new Color(239, 68, 68), 110);
        JButton btnRefresh = createStyledButton("↻ Làm mới", new Color(107, 114, 128), 115);
        JButton btnExportExcel = createStyledButton("⬇ Excel", new Color(16, 185, 129), 105);
        JButton btnImportExcel = createStyledButton("⬆ Excel", new Color(245, 158, 11), 105);
        JButton btnTemplateExcel = createStyledButton("📄 Mẫu", new Color(8, 145, 178), 95);
        btnExportErrors = createStyledButton("⚠ Xuất lỗi", new Color(185, 28, 28), 110);
        btnExportErrors.setEnabled(false);

        btnAdd.addActionListener(e -> showPhieuNhapDialog(null));
        btnEdit.addActionListener(e -> {
            int row = tblPhieuNhap.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu nhập cần sửa.", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Object[] data = new Object[] {
                    phieuNhapModel.getValueAt(row, 0),
                    phieuNhapModel.getValueAt(row, 1),
                    phieuNhapModel.getValueAt(row, 2),
                    phieuNhapModel.getValueAt(row, 3)
            };
            showPhieuNhapDialog(data);
        });
        btnDelete.addActionListener(e -> deletePhieuNhapSelected());

        btnRefresh.addActionListener(e -> {
            loadPhieuNhap();
            JOptionPane.showMessageDialog(this, "Đã làm mới dữ liệu.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });
        btnExportExcel.addActionListener(e -> exportPhieuNhapExcel());
        btnImportExcel.addActionListener(e -> importPhieuNhapExcel());
        btnTemplateExcel.addActionListener(e -> exportPhieuNhapTemplate());
        btnExportErrors.addActionListener(e -> exportImportErrors(lastImportErrors));

        topActions.add(btnAdd);
        topActions.add(btnEdit);
        topActions.add(btnDelete);
        topActions.add(btnRefresh);
        topActions.add(btnExportExcel);
        topActions.add(btnImportExcel);
        topActions.add(btnTemplateExcel);
        topActions.add(btnExportErrors);

        main.add(topActions, BorderLayout.NORTH);
        main.add(createPhieuNhapPanel(), BorderLayout.CENTER);
        return main;
    }

    private JPanel createPhieuNhapPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)));

        JLabel title = new JLabel("📄 DANH SÁCH PHIẾU NHẬP");
        title.setFont(headerFont);
        title.setForeground(primaryDark);

        String[] cols = { "Mã PN", "Ngày", "Mã NV", "Mã NCC", "Tổng tiền", "≡" };
        phieuNhapModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        tblPhieuNhap = new JTable(phieuNhapModel);
        tblPhieuNhap.setRowHeight(36);
        tblPhieuNhap.setFont(normalFont);
        tblPhieuNhap.setGridColor(new Color(243, 244, 246));
        tblPhieuNhap.setSelectionBackground(primaryLight);
        styleTableHeader(tblPhieuNhap);
        tblPhieuNhap.getColumnModel().getColumn(5).setPreferredWidth(40);
        tblPhieuNhap.getColumnModel().getColumn(5).setMaxWidth(40);
        tblPhieuNhap.getColumnModel().getColumn(5).setCellRenderer(new ActionRenderer());
        tblPhieuNhap.getColumnModel().getColumn(5).setCellEditor(new ActionEditor(tblPhieuNhap));

        JScrollPane scroll = new JScrollPane(tblPhieuNhap);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        panel.add(title, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private void styleTableHeader(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setFont(boldFont);
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 36));

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setHorizontalAlignment(JLabel.CENTER);
        renderer.setBackground(primaryColor);
        renderer.setForeground(Color.WHITE);
        renderer.setFont(boldFont);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(renderer);
        }
    }

    private JButton createStyledButton(String text, Color bgColor, int width) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setPreferredSize(new Dimension(width, 36));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(boldFont);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color original = bgColor;
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(original.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(original);
            }
        });
        return btn;
    }

    private void loadPhieuNhap() {
        phieuNhapModel.setRowCount(0);

        String sql = "SELECT p.MaPhieuNhap, p.Ngay, p.MaNV, p.MaNCC, IFNULL(SUM(c.ThanhTien), 0) AS TongTien "
                + "FROM phieunhaphang p "
                + "LEFT JOIN ct_phieunhaphang c ON p.MaPhieuNhap = c.MaPhieuNhap "
                + "GROUP BY p.MaPhieuNhap, p.Ngay, p.MaNV, p.MaNCC "
                + "ORDER BY p.MaPhieuNhap";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                phieuNhapModel.addRow(new Object[] {
                        rs.getInt("MaPhieuNhap"),
                        rs.getTimestamp("Ngay"),
                        rs.getInt("MaNV"),
                        rs.getInt("MaNCC"),
                        rs.getDouble("TongTien"),
                        "≡"
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không tải được phiếu nhập: " + ex.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showPhieuNhapDialog(Object[] data) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                data == null ? "Thêm phiếu nhập" : "Sửa phiếu nhập", true);
        dialog.setSize(920, 650);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel container = new JPanel(new BorderLayout(12, 12));
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(16, 16, 10, 16));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtMaPN = new JTextField();
        JTextField txtNgay = new JTextField();
        JTextField txtMaNV = new JTextField();
        JTextField txtMaNCC = new JTextField();
        txtMaPN.setEditable(false);
        if (data == null) {
            txtMaPN.setText("Tự động");
            txtNgay.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
        } else {
            txtMaPN.setText(String.valueOf(data[0]));
            txtNgay.setText(String.valueOf(data[1]));
            txtMaNV.setText(String.valueOf(data[2]));
            txtMaNCC.setText(String.valueOf(data[3]));
        }

        JTextField[] fields = { txtMaPN, txtNgay, txtMaNV, txtMaNCC };
        String[] labels = { "Mã phiếu nhập:", "Ngày (yyyy-MM-dd HH:mm:ss):", "Mã NV:", "Mã NCC:" };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.35;
            form.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.65;
            fields[i].setPreferredSize(new Dimension(220, 30));
            form.add(fields[i], gbc);
        }

        JPanel detailPanel = new JPanel(new BorderLayout(8, 8));
        detailPanel.setBackground(Color.WHITE);
        detailPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Chi tiết phiếu nhập"),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        JPanel detailInput = new JPanel(new GridBagLayout());
        detailInput.setBackground(Color.WHITE);
        GridBagConstraints dgbc = new GridBagConstraints();
        dgbc.insets = new Insets(4, 4, 4, 4);
        dgbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<String> cmbLoai = new JComboBox<>(new String[] { "Bánh", "Nguyên liệu" });
        JComboBox<String> cmbMaBanh = createBanhSuggestionCombo();
        JTextField txtMaNVL = new JTextField();
        JTextField txtSoLuong = new JTextField();
        JTextField txtDonGia = new JTextField();
        JTextField txtTinhTrang = new JTextField();

        JLabel[] dLabels = {
                new JLabel("Loại nhập:"),
                new JLabel("Mã bánh:"),
                new JLabel("Mã NVL:"),
                new JLabel("Số lượng:"),
                new JLabel("Đơn giá:"),
                new JLabel("Tình trạng:")
        };
            JComponent[] dFields = { cmbLoai, cmbMaBanh, txtMaNVL, txtSoLuong, txtDonGia, txtTinhTrang };

        for (int i = 0; i < dLabels.length; i++) {
            dgbc.gridx = 0;
            dgbc.gridy = i;
            dgbc.weightx = 0.2;
            detailInput.add(dLabels[i], dgbc);

            dgbc.gridx = 1;
            dgbc.weightx = 0.8;
            dFields[i].setPreferredSize(new Dimension(220, 30));
            detailInput.add(dFields[i], dgbc);
        }

        applyLoaiSelection(cmbLoai, cmbMaBanh, txtMaNVL);
        cmbLoai.addActionListener(e -> applyLoaiSelection(cmbLoai, cmbMaBanh, txtMaNVL));

        String[] detailCols = { "Loại", "Mã bánh", "Tên bánh", "Mã NVL", "Tên NVL", "Số lượng", "Đơn giá",
                "Tình trạng" };
        DefaultTableModel detailModel = new DefaultTableModel(detailCols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tblDetailInput = new JTable(detailModel);
        tblDetailInput.setRowHeight(30);
        styleTableHeader(tblDetailInput);

        if (data != null) {
            int maPN = Integer.parseInt(String.valueOf(data[0]));
            List<Object[]> existingDetails = getChiTietByPhieuNhap(maPN);
            for (Object[] row : existingDetails) {
                String loai = row[0] != null ? "Bánh" : "Nguyên liệu";
                detailModel.addRow(new Object[] { loai, row[0], row[1], row[2], row[3], row[4], row[5], row[6] });
            }
        }

        JPanel detailButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        detailButtons.setBackground(Color.WHITE);
        JButton btnAddDetail = createStyledButton("+ Thêm dòng", new Color(34, 197, 94), 120);
        JButton btnUpdateDetail = createStyledButton("✎ Cập nhật dòng", new Color(59, 130, 246), 145);
        JButton btnRemoveDetail = createStyledButton("✕ Xóa dòng", new Color(239, 68, 68), 120);

        btnAddDetail.addActionListener(e -> {
            try {
                Object[] detailRow = buildDetailRowFromInput(cmbLoai, cmbMaBanh, txtMaNVL, txtSoLuong, txtDonGia,
                        txtTinhTrang);
                detailModel.addRow(detailRow);
                clearDetailInputs(cmbLoai, cmbMaBanh, txtMaNVL, txtSoLuong, txtDonGia, txtTinhTrang);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Lỗi dữ liệu chi tiết",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        btnUpdateDetail.addActionListener(e -> {
            int idx = tblDetailInput.getSelectedRow();
            if (idx < 0) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn dòng chi tiết cần cập nhật.", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                Object[] detailRow = buildDetailRowFromInput(cmbLoai, cmbMaBanh, txtMaNVL, txtSoLuong, txtDonGia,
                        txtTinhTrang);
                for (int i = 0; i < detailRow.length; i++) {
                    detailModel.setValueAt(detailRow[i], idx, i);
                }
                clearDetailInputs(cmbLoai, cmbMaBanh, txtMaNVL, txtSoLuong, txtDonGia, txtTinhTrang);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, ex.getMessage(), "Lỗi dữ liệu chi tiết",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        btnRemoveDetail.addActionListener(e -> {
            int idx = tblDetailInput.getSelectedRow();
            if (idx < 0) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn dòng chi tiết cần xóa.", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            detailModel.removeRow(idx);
        });

        tblDetailInput.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) {
                return;
            }
            int idx = tblDetailInput.getSelectedRow();
            if (idx < 0) {
                return;
            }
            String loai = String.valueOf(detailModel.getValueAt(idx, 0));
            cmbLoai.setSelectedItem(loai);
                setSelectedMaBanh(cmbMaBanh,
                    detailModel.getValueAt(idx, 1) == null ? null
                        : Integer.valueOf(String.valueOf(detailModel.getValueAt(idx, 1))));
                txtMaNVL.setText(
                    detailModel.getValueAt(idx, 3) == null ? "" : String.valueOf(detailModel.getValueAt(idx, 3)));
            txtSoLuong.setText(String.valueOf(detailModel.getValueAt(idx, 5)));
            txtDonGia.setText(String.valueOf(detailModel.getValueAt(idx, 6)));
            txtTinhTrang.setText(
                    detailModel.getValueAt(idx, 7) == null ? "" : String.valueOf(detailModel.getValueAt(idx, 7)));
            applyLoaiSelection(cmbLoai, cmbMaBanh, txtMaNVL);
        });

        detailButtons.add(btnAddDetail);
        detailButtons.add(btnUpdateDetail);
        detailButtons.add(btnRemoveDetail);

        detailPanel.add(detailInput, BorderLayout.WEST);
        detailPanel.add(new JScrollPane(tblDetailInput), BorderLayout.CENTER);
        detailPanel.add(detailButtons, BorderLayout.SOUTH);

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        bottom.setBackground(new Color(249, 250, 251));
        JButton btnSave = createStyledButton("💾 Lưu", new Color(34, 197, 94), 90);
        JButton btnCancel = createStyledButton("Hủy", new Color(107, 114, 128), 90);

        btnSave.addActionListener(e -> {
            try {
                Timestamp ngay = Timestamp.valueOf(txtNgay.getText().trim());
                int maNV = Integer.parseInt(txtMaNV.getText().trim());
                int maNCC = Integer.parseInt(txtMaNCC.getText().trim());
                List<Object[]> details = collectDetailsFromModel(detailModel);

                if (data == null) {
                    if (details.isEmpty()) {
                        throw new IllegalArgumentException("Vui lòng nhập tối thiểu 1 dòng CTPN khi thêm phiếu nhập.");
                    }
                    insertPhieuNhapWithDetails(ngay, maNV, maNCC, details);
                } else {
                    int maPN = Integer.parseInt(txtMaPN.getText().trim());
                    if (details.isEmpty()) {
                        throw new IllegalArgumentException("Phiếu nhập phải có ít nhất 1 dòng CTPN.");
                    }
                    updatePhieuNhapWithDetails(maPN, ngay, maNV, maNCC, details);
                }

                loadPhieuNhap();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ: " + ex.getMessage(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        bottom.add(btnSave);
        bottom.add(btnCancel);
        container.add(form, BorderLayout.NORTH);
        container.add(detailPanel, BorderLayout.CENTER);
        dialog.add(container, BorderLayout.CENTER);
        dialog.add(bottom, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void insertPhieuNhapWithDetails(Timestamp ngay, int maNV, int maNCC, List<Object[]> details)
            throws Exception {
        String insertPN = "INSERT INTO phieunhaphang (Ngay, MaNV, MaNCC) VALUES (?, ?, ?)";
        String insertCT = "INSERT INTO ct_phieunhaphang (MaPhieuNhap, MaBanh, MaNVL, SoLuong, DonGia, TinhTrang) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectDatabase.getConnection()) {
            boolean oldAuto = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement psPN = conn.prepareStatement(insertPN, Statement.RETURN_GENERATED_KEYS);
                    PreparedStatement psCT = conn.prepareStatement(insertCT)) {
                psPN.setTimestamp(1, ngay);
                psPN.setInt(2, maNV);
                psPN.setInt(3, maNCC);
                psPN.executeUpdate();

                int maPN;
                try (ResultSet rs = psPN.getGeneratedKeys()) {
                    if (!rs.next()) {
                        throw new IllegalStateException("Không lấy được mã phiếu nhập vừa tạo.");
                    }
                    maPN = rs.getInt(1);
                }

                for (Object[] detail : details) {
                    psCT.setInt(1, maPN);
                    setNullableInt(psCT, 2, (Integer) detail[0]);
                    setNullableInt(psCT, 3, (Integer) detail[1]);
                    psCT.setInt(4, (Integer) detail[2]);
                    psCT.setDouble(5, (Double) detail[3]);
                    psCT.setString(6, (String) detail[4]);
                    psCT.executeUpdate();

                    capNhatTonKhoBanh(conn, (Integer) detail[0], (Integer) detail[2]);
                }

                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(oldAuto);
            }
        }
    }

    private void updatePhieuNhapWithDetails(int maPN, Timestamp ngay, int maNV, int maNCC, List<Object[]> details)
            throws Exception {
        String updatePN = "UPDATE phieunhaphang SET Ngay = ?, MaNV = ?, MaNCC = ? WHERE MaPhieuNhap = ?";
        String deleteCT = "DELETE FROM ct_phieunhaphang WHERE MaPhieuNhap = ?";
        String insertCT = "INSERT INTO ct_phieunhaphang (MaPhieuNhap, MaBanh, MaNVL, SoLuong, DonGia, TinhTrang) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectDatabase.getConnection()) {
            boolean oldAuto = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement psUpdatePN = conn.prepareStatement(updatePN);
                    PreparedStatement psDeleteCT = conn.prepareStatement(deleteCT);
                    PreparedStatement psInsertCT = conn.prepareStatement(insertCT)) {
                Map<Integer, Integer> tonCuTheoBanh = laySoLuongBanhTheoPhieuNhap(conn, maPN);

                psUpdatePN.setTimestamp(1, ngay);
                psUpdatePN.setInt(2, maNV);
                psUpdatePN.setInt(3, maNCC);
                psUpdatePN.setInt(4, maPN);
                psUpdatePN.executeUpdate();

                psDeleteCT.setInt(1, maPN);
                psDeleteCT.executeUpdate();

                for (Object[] detail : details) {
                    psInsertCT.setInt(1, maPN);
                    setNullableInt(psInsertCT, 2, (Integer) detail[0]);
                    setNullableInt(psInsertCT, 3, (Integer) detail[1]);
                    psInsertCT.setInt(4, (Integer) detail[2]);
                    psInsertCT.setDouble(5, (Double) detail[3]);
                    psInsertCT.setString(6, (String) detail[4]);
                    psInsertCT.executeUpdate();
                }

                Map<Integer, Integer> tonMoiTheoBanh = tongHopSoLuongBanh(details);
                capNhatTonKhoTheoChenhLech(conn, tonCuTheoBanh, tonMoiTheoBanh);

                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(oldAuto);
            }
        }
    }

    private List<Object[]> getChiTietByPhieuNhap(int maPhieuNhap) {
        List<Object[]> rows = new ArrayList<>();
        String sql = "SELECT c.MaBanh, b.TenBanh, c.MaNVL, n.Ten AS TenNVL, c.SoLuong, c.DonGia, c.TinhTrang "
                + "FROM ct_phieunhaphang c "
                + "LEFT JOIN banh b ON c.MaBanh = b.MaBanh "
                + "LEFT JOIN nguyenlieu n ON c.MaNVL = n.MaNL "
                + "WHERE c.MaPhieuNhap = ? ORDER BY c.MaBanh, c.MaNVL";
        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maPhieuNhap);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Object[] {
                            rs.getObject("MaBanh") == null ? null : rs.getInt("MaBanh"),
                            rs.getString("TenBanh"),
                            rs.getObject("MaNVL") == null ? null : rs.getInt("MaNVL"),
                            rs.getString("TenNVL"),
                            rs.getInt("SoLuong"),
                            rs.getDouble("DonGia"),
                            rs.getString("TinhTrang")
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Không tải được chi tiết phiếu nhập: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
        return rows;
    }

    private void applyLoaiSelection(JComboBox<String> cmbLoai, JComboBox<String> cmbMaBanh, JTextField txtMaNVL) {
        boolean isBanh = "Bánh".equals(String.valueOf(cmbLoai.getSelectedItem()));
        cmbMaBanh.setEnabled(isBanh);
        txtMaNVL.setEnabled(!isBanh);
        if (isBanh) {
            txtMaNVL.setText("");
        } else {
            cmbMaBanh.setSelectedIndex(0);
        }
    }

    private Object[] buildDetailRowFromInput(JComboBox<String> cmbLoai, JComboBox<String> cmbMaBanh, JTextField txtMaNVL,
            JTextField txtSoLuong, JTextField txtDonGia, JTextField txtTinhTrang) {
        String loai = String.valueOf(cmbLoai.getSelectedItem());
        Integer maBanh = getSelectedMaBanh(cmbMaBanh);
        Integer maNvl = parseOptionalInt(txtMaNVL.getText().trim());
        Integer soLuong = parseOptionalInt(txtSoLuong.getText().trim());
        Double donGia = parseOptionalDouble(txtDonGia.getText().trim());
        String tinhTrang = txtTinhTrang.getText().trim();

        if ("Bánh".equals(loai)) {
            maNvl = null;
        } else {
            maBanh = null;
        }

        validateChiTietInput(maBanh, maNvl, soLuong, donGia);
        String tenBanh = maBanh == null ? "" : lookupTenBanh(maBanh);
        String tenNvl = maNvl == null ? "" : lookupTenNvl(maNvl);
        return new Object[] { loai, maBanh, tenBanh, maNvl, tenNvl, soLuong, donGia, tinhTrang };
    }

    private void clearDetailInputs(JComboBox<String> cmbLoai, JComboBox<String> cmbMaBanh, JTextField txtMaNVL,
            JTextField txtSoLuong, JTextField txtDonGia, JTextField txtTinhTrang) {
        cmbLoai.setSelectedIndex(0);
        cmbMaBanh.setSelectedIndex(0);
        txtMaNVL.setText("");
        txtSoLuong.setText("");
        txtDonGia.setText("");
        txtTinhTrang.setText("");
        applyLoaiSelection(cmbLoai, cmbMaBanh, txtMaNVL);
    }

    private List<Object[]> collectDetailsFromModel(DefaultTableModel model) {
        List<Object[]> details = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            Integer maBanh = model.getValueAt(i, 1) == null ? null
                    : Integer.parseInt(String.valueOf(model.getValueAt(i, 1)));
            Integer maNvl = model.getValueAt(i, 3) == null ? null
                    : Integer.parseInt(String.valueOf(model.getValueAt(i, 3)));
            Integer soLuong = Integer.parseInt(String.valueOf(model.getValueAt(i, 5)));
            Double donGia = Double.parseDouble(String.valueOf(model.getValueAt(i, 6)));
            String tinhTrang = model.getValueAt(i, 7) == null ? "" : String.valueOf(model.getValueAt(i, 7));
            validateChiTietInput(maBanh, maNvl, soLuong, donGia);
            details.add(new Object[] { maBanh, maNvl, soLuong, donGia, tinhTrang });
        }
        return details;
    }

    private void deletePhieuNhapSelected() {
        int row = tblPhieuNhap.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu nhập cần xóa.", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int maPN = Integer.parseInt(String.valueOf(phieuNhapModel.getValueAt(row, 0)));
        int confirm = JOptionPane.showConfirmDialog(this,
                "Xóa phiếu nhập " + maPN + " và toàn bộ chi tiết của phiếu này?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        String deleteDetails = "DELETE FROM ct_phieunhaphang WHERE MaPhieuNhap = ?";
        String deleteHeader = "DELETE FROM phieunhaphang WHERE MaPhieuNhap = ?";
        try (Connection conn = ConnectDatabase.getConnection()) {
            boolean oldAuto = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(deleteDetails);
                    PreparedStatement ps2 = conn.prepareStatement(deleteHeader)) {
                Map<Integer, Integer> tonCuTheoBanh = laySoLuongBanhTheoPhieuNhap(conn, maPN);

                ps1.setInt(1, maPN);
                ps1.executeUpdate();
                ps2.setInt(1, maPN);
                ps2.executeUpdate();

                for (Map.Entry<Integer, Integer> entry : tonCuTheoBanh.entrySet()) {
                    capNhatTonKhoBanh(conn, entry.getKey(), -entry.getValue());
                }

                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(oldAuto);
            }

            loadPhieuNhap();
            JOptionPane.showMessageDialog(this, "Đã xóa phiếu nhập.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Xóa phiếu nhập thất bại: " + ex.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private DefaultTableModel buildChiTietModel(int maPhieuNhap) {
        String[] cols = { "Mã PN", "Mã bánh", "Tên bánh", "Mã NVL", "Tên NVL", "Số lượng", "Đơn giá",
                "Thành tiền", "Tình trạng" };
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        String sql = "SELECT c.MaPhieuNhap, c.MaBanh, b.TenBanh, c.MaNVL, n.Ten AS TenNVL, c.SoLuong, c.DonGia, c.ThanhTien, c.TinhTrang "
                + "FROM ct_phieunhaphang c "
                + "LEFT JOIN banh b ON c.MaBanh = b.MaBanh "
                + "LEFT JOIN nguyenlieu n ON c.MaNVL = n.MaNL "
                + "WHERE c.MaPhieuNhap = ? ORDER BY c.MaBanh, c.MaNVL";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maPhieuNhap);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[] {
                            rs.getInt("MaPhieuNhap"),
                            rs.getObject("MaBanh"),
                            rs.getString("TenBanh"),
                            rs.getObject("MaNVL"),
                            rs.getString("TenNVL"),
                            rs.getInt("SoLuong"),
                            rs.getDouble("DonGia"),
                            rs.getDouble("ThanhTien"),
                            rs.getString("TinhTrang")
                    });
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không tải được chi tiết phiếu nhập: " + ex.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }

        return model;
    }

    private void showChiTietDialog(int maPhieuNhap) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Chi tiết phiếu nhập #" + maPhieuNhap, true);
        dialog.setSize(940, 560);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(BorderFactory.createEmptyBorder(10, 12, 0, 12));
        JLabel lbl = new JLabel("📦 Chi tiết phiếu nhập mã: " + maPhieuNhap);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setForeground(primaryColor);
        top.add(lbl, BorderLayout.WEST);

        DefaultTableModel model = buildChiTietModel(maPhieuNhap);
        JTable table = new JTable(model);
        table.setRowHeight(34);
        table.setFont(normalFont);
        table.setGridColor(new Color(243, 244, 246));
        table.setSelectionBackground(primaryLight);
        styleTableHeader(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        actions.setBackground(bgColor);
        JButton btnAdd = createStyledButton("+ Thêm CT", new Color(34, 197, 94), 110);
        JButton btnEdit = createStyledButton("✎ Sửa CT", new Color(59, 130, 246), 110);
        JButton btnDelete = createStyledButton("✕ Xóa CT", new Color(239, 68, 68), 110);
        JButton btnPrint = createStyledButton("🖨 In phiếu", new Color(14, 116, 144), 120);
        JButton btnReload = createStyledButton("↻ Làm mới", new Color(107, 114, 128), 110);
        JButton btnClose = createStyledButton("Đóng", new Color(75, 85, 99), 90);

        btnAdd.addActionListener(e -> {
            if (showChiTietCrudDialog(dialog, maPhieuNhap, null)) {
                reloadChiTietModel(model, maPhieuNhap);
                loadPhieuNhap();
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn dòng chi tiết cần sửa.", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            Object[] existing = new Object[] {
                    model.getValueAt(row, 1),
                    model.getValueAt(row, 3),
                    model.getValueAt(row, 5),
                    model.getValueAt(row, 6),
                    model.getValueAt(row, 8)
            };
            if (showChiTietCrudDialog(dialog, maPhieuNhap, existing)) {
                reloadChiTietModel(model, maPhieuNhap);
                loadPhieuNhap();
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn dòng chi tiết cần xóa.", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Integer maBanh = parseOptionalInt(String.valueOf(model.getValueAt(row, 1)));
            Integer maNvl = parseOptionalInt(String.valueOf(model.getValueAt(row, 3)));
            int confirm = JOptionPane.showConfirmDialog(dialog,
                    "Xóa chi tiết (Mã bánh " + (maBanh == null ? "trống" : maBanh)
                            + ", Mã NVL " + (maNvl == null ? "trống" : maNvl) + ")?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteChiTiet(maPhieuNhap, maBanh, maNvl);
                reloadChiTietModel(model, maPhieuNhap);
                loadPhieuNhap();
            }
        });

        btnPrint.addActionListener(e -> {
            MessageFormat header = new MessageFormat("PHIEU NHAP HANG MA " + maPhieuNhap);
            MessageFormat footer = new MessageFormat("Trang {0}");
            try {
                table.print(JTable.PrintMode.FIT_WIDTH, header, footer, true, null, true, null);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "In phiếu nhập thất bại: " + ex.getMessage(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnReload.addActionListener(e -> reloadChiTietModel(model, maPhieuNhap));
        btnClose.addActionListener(e -> dialog.dispose());

        actions.add(btnAdd);
        actions.add(btnEdit);
        actions.add(btnDelete);
        actions.add(btnPrint);
        actions.add(btnReload);
        actions.add(btnClose);

        dialog.add(top, BorderLayout.NORTH);
        dialog.add(scroll, BorderLayout.CENTER);
        dialog.add(actions, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void reloadChiTietModel(DefaultTableModel model, int maPhieuNhap) {
        model.setRowCount(0);
        DefaultTableModel fresh = buildChiTietModel(maPhieuNhap);
        for (int i = 0; i < fresh.getRowCount(); i++) {
            Object[] row = new Object[fresh.getColumnCount()];
            for (int j = 0; j < fresh.getColumnCount(); j++) {
                row[j] = fresh.getValueAt(i, j);
            }
            model.addRow(row);
        }
    }

    private boolean showChiTietCrudDialog(Window owner, int maPhieuNhap, Object[] existing) {
        JDialog dialog = new JDialog(owner, existing == null ? "Thêm chi tiết phiếu nhập" : "Sửa chi tiết phiếu nhập",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(430, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(16, 16, 12, 16));
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtMaPN = new JTextField(String.valueOf(maPhieuNhap));
        txtMaPN.setEditable(false);
        JComboBox<String> cmbLoai = new JComboBox<>(new String[] { "Bánh", "Nguyên liệu" });
        JComboBox<String> cmbMaBanh = createBanhSuggestionCombo();
        JTextField txtMaNVL = new JTextField(
                existing == null ? "" : (existing[1] == null ? "" : String.valueOf(existing[1])));
        JTextField txtSoLuong = new JTextField(existing == null ? "" : String.valueOf(existing[2]));
        JTextField txtDonGia = new JTextField(existing == null ? "" : String.valueOf(existing[3]));
        JTextField txtTinhTrang = new JTextField(existing == null ? "" : String.valueOf(existing[4]));

        if (existing != null) {
            setSelectedMaBanh(cmbMaBanh,
                existing[0] == null ? null : Integer.valueOf(String.valueOf(existing[0])));
        }

        if (existing != null) {
            cmbLoai.setSelectedItem(existing[0] == null ? "Nguyên liệu" : "Bánh");
        }

        applyLoaiSelection(cmbLoai, cmbMaBanh, txtMaNVL);
        cmbLoai.addActionListener(e -> applyLoaiSelection(cmbLoai, cmbMaBanh, txtMaNVL));

        if (existing != null) {
            cmbMaBanh.setEnabled(false);
            txtMaNVL.setEditable(false);
            cmbLoai.setEnabled(false);
        }

        JComponent[] fields = { txtMaPN, cmbLoai, cmbMaBanh, txtMaNVL, txtSoLuong, txtDonGia, txtTinhTrang };
        String[] labels = { "Mã PN:", "Loại nhập:", "Mã bánh:", "Mã NVL:", "Số lượng:", "Đơn giá:", "Tình trạng:" };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.35;
            form.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.65;
            fields[i].setPreferredSize(new Dimension(210, 30));
            form.add(fields[i], gbc);
        }

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        bottom.setBackground(new Color(249, 250, 251));
        JButton btnSave = createStyledButton("💾 Lưu", new Color(34, 197, 94), 90);
        JButton btnCancel = createStyledButton("Hủy", new Color(107, 114, 128), 90);

        final boolean[] ok = { false };
        btnSave.addActionListener(e -> {
            try {
                Integer maBanh = getSelectedMaBanh(cmbMaBanh);
                Integer maNvl = parseOptionalInt(txtMaNVL.getText().trim());
                int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
                double donGia = Double.parseDouble(txtDonGia.getText().trim());
                String tinhTrang = txtTinhTrang.getText().trim();
                validateChiTietInput(maBanh, maNvl, soLuong, donGia);

                if (existing == null) {
                    insertChiTiet(maPhieuNhap, maBanh, maNvl, soLuong, donGia, tinhTrang);
                } else {
                    updateChiTiet(maPhieuNhap, maBanh, maNvl, soLuong, donGia, tinhTrang);
                }

                ok[0] = true;
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ: " + ex.getMessage(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCancel.addActionListener(e -> dialog.dispose());

        bottom.add(btnSave);
        bottom.add(btnCancel);
        dialog.add(form, BorderLayout.CENTER);
        dialog.add(bottom, BorderLayout.SOUTH);
        dialog.setVisible(true);
        return ok[0];
    }

    private void insertChiTiet(int maPN, Integer maBanh, Integer maNvl, int soLuong, double donGia, String tinhTrang)
            throws Exception {
        String sql = "INSERT INTO ct_phieunhaphang (MaPhieuNhap, MaBanh, MaNVL, SoLuong, DonGia, TinhTrang) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectDatabase.getConnection()) {
            boolean oldAuto = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, maPN);
                setNullableInt(ps, 2, maBanh);
                setNullableInt(ps, 3, maNvl);
                ps.setInt(4, soLuong);
                ps.setDouble(5, donGia);
                ps.setString(6, tinhTrang);
                ps.executeUpdate();

                capNhatTonKhoBanh(conn, maBanh, soLuong);
                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(oldAuto);
            }
        }
    }

    private void updateChiTiet(int maPN, Integer maBanh, Integer maNvl, int soLuong, double donGia, String tinhTrang)
            throws Exception {
        String sqlLayCu = "SELECT SoLuong FROM ct_phieunhaphang WHERE MaPhieuNhap = ? "
                + "AND ((? IS NULL AND MaBanh IS NULL) OR MaBanh = ?) "
                + "AND ((? IS NULL AND MaNVL IS NULL) OR MaNVL = ?)";
        String sql = "UPDATE ct_phieunhaphang SET SoLuong = ?, DonGia = ?, TinhTrang = ? "
                + "WHERE MaPhieuNhap = ? "
                + "AND ((? IS NULL AND MaBanh IS NULL) OR MaBanh = ?) "
                + "AND ((? IS NULL AND MaNVL IS NULL) OR MaNVL = ?)";
        try (Connection conn = ConnectDatabase.getConnection()) {
            boolean oldAuto = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                int soLuongCu = 0;
                try (PreparedStatement psCu = conn.prepareStatement(sqlLayCu)) {
                    psCu.setInt(1, maPN);
                    setNullableInt(psCu, 2, maBanh);
                    setNullableInt(psCu, 3, maBanh);
                    setNullableInt(psCu, 4, maNvl);
                    setNullableInt(psCu, 5, maNvl);
                    try (ResultSet rs = psCu.executeQuery()) {
                        if (rs.next()) {
                            soLuongCu = rs.getInt("SoLuong");
                        } else {
                            throw new IllegalStateException("Không tìm thấy chi tiết phiếu nhập cần sửa.");
                        }
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, soLuong);
                    ps.setDouble(2, donGia);
                    ps.setString(3, tinhTrang);
                    ps.setInt(4, maPN);
                    setNullableInt(ps, 5, maBanh);
                    setNullableInt(ps, 6, maBanh);
                    setNullableInt(ps, 7, maNvl);
                    setNullableInt(ps, 8, maNvl);
                    ps.executeUpdate();
                }

                capNhatTonKhoBanh(conn, maBanh, soLuong - soLuongCu);
                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(oldAuto);
            }
        }
    }

    private void deleteChiTiet(int maPN, Integer maBanh, Integer maNvl) {
        String sqlLayCu = "SELECT SoLuong FROM ct_phieunhaphang WHERE MaPhieuNhap = ? "
                + "AND ((? IS NULL AND MaBanh IS NULL) OR MaBanh = ?) "
                + "AND ((? IS NULL AND MaNVL IS NULL) OR MaNVL = ?)";
        String sql = "DELETE FROM ct_phieunhaphang WHERE MaPhieuNhap = ? "
                + "AND ((? IS NULL AND MaBanh IS NULL) OR MaBanh = ?) "
                + "AND ((? IS NULL AND MaNVL IS NULL) OR MaNVL = ?)";
        try (Connection conn = ConnectDatabase.getConnection()) {
            boolean oldAuto = conn.getAutoCommit();
            conn.setAutoCommit(false);
            try {
                int soLuongCu = 0;
                try (PreparedStatement psCu = conn.prepareStatement(sqlLayCu)) {
                    psCu.setInt(1, maPN);
                    setNullableInt(psCu, 2, maBanh);
                    setNullableInt(psCu, 3, maBanh);
                    setNullableInt(psCu, 4, maNvl);
                    setNullableInt(psCu, 5, maNvl);
                    try (ResultSet rs = psCu.executeQuery()) {
                        if (rs.next()) {
                            soLuongCu = rs.getInt("SoLuong");
                        }
                    }
                }

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, maPN);
                    setNullableInt(ps, 2, maBanh);
                    setNullableInt(ps, 3, maBanh);
                    setNullableInt(ps, 4, maNvl);
                    setNullableInt(ps, 5, maNvl);
                    ps.executeUpdate();
                }

                capNhatTonKhoBanh(conn, maBanh, -soLuongCu);
                conn.commit();
            } catch (Exception ex) {
                conn.rollback();
                throw ex;
            } finally {
                conn.setAutoCommit(oldAuto);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Xóa chi tiết thất bại: " + ex.getMessage(), "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private JComboBox<String> createBanhSuggestionCombo() {
        JComboBox<String> combo = new JComboBox<>();
        combo.addItem("");
        combo.setEditable(true);

        String sql = "SELECT MaBanh, TenBanh FROM banh ORDER BY MaBanh";
        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                combo.addItem(rs.getInt("MaBanh") + " - " + rs.getString("TenBanh"));
            }
        } catch (Exception ex) {
            // If loading suggestion fails, user can still type manual code.
        }
        return combo;
    }

    private Integer getSelectedMaBanh(JComboBox<String> combo) {
        Object selected = combo.getEditor().getItem();
        if (selected == null) {
            return null;
        }
        String text = String.valueOf(selected).trim();
        if (text.isEmpty()) {
            return null;
        }

        int dash = text.indexOf("-");
        String ma = dash >= 0 ? text.substring(0, dash).trim() : text;
        return Integer.parseInt(ma);
    }

    private void setSelectedMaBanh(JComboBox<String> combo, Integer maBanh) {
        if (maBanh == null) {
            combo.setSelectedIndex(0);
            return;
        }
        String prefix = maBanh + " -";
        for (int i = 0; i < combo.getItemCount(); i++) {
            String item = String.valueOf(combo.getItemAt(i));
            if (item.startsWith(prefix)) {
                combo.setSelectedIndex(i);
                return;
            }
        }
        combo.getEditor().setItem(String.valueOf(maBanh));
    }

    private Map<Integer, Integer> laySoLuongBanhTheoPhieuNhap(Connection conn, int maPN) throws Exception {
        Map<Integer, Integer> data = new HashMap<>();
        String sql = "SELECT MaBanh, SUM(SoLuong) AS TongSoLuong FROM ct_phieunhaphang "
                + "WHERE MaPhieuNhap = ? AND MaBanh IS NOT NULL GROUP BY MaBanh";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maPN);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    data.put(rs.getInt("MaBanh"), rs.getInt("TongSoLuong"));
                }
            }
        }
        return data;
    }

    private Map<Integer, Integer> tongHopSoLuongBanh(List<Object[]> details) {
        Map<Integer, Integer> data = new HashMap<>();
        for (Object[] detail : details) {
            Integer maBanh = (Integer) detail[0];
            Integer soLuong = (Integer) detail[2];
            if (maBanh == null || soLuong == null) {
                continue;
            }
            data.put(maBanh, data.getOrDefault(maBanh, 0) + soLuong);
        }
        return data;
    }

    private void capNhatTonKhoTheoChenhLech(Connection conn, Map<Integer, Integer> tonCuTheoBanh,
            Map<Integer, Integer> tonMoiTheoBanh) throws Exception {
        Set<Integer> allKeys = new HashSet<>();
        allKeys.addAll(tonCuTheoBanh.keySet());
        allKeys.addAll(tonMoiTheoBanh.keySet());

        for (Integer maBanh : allKeys) {
            int cu = tonCuTheoBanh.getOrDefault(maBanh, 0);
            int moi = tonMoiTheoBanh.getOrDefault(maBanh, 0);
            int delta = moi - cu;
            capNhatTonKhoBanh(conn, maBanh, delta);
        }
    }

    private void capNhatTonKhoBanh(Connection conn, Integer maBanh, int deltaSoLuong) throws Exception {
        if (maBanh == null || deltaSoLuong == 0) {
            return;
        }

        String sql = "UPDATE banh SET SoLuong = SoLuong + ? WHERE MaBanh = ? AND SoLuong + ? >= 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, deltaSoLuong);
            ps.setInt(2, maBanh);
            ps.setInt(3, deltaSoLuong);
            int updated = ps.executeUpdate();
            if (updated <= 0) {
                throw new IllegalStateException("Không thể cập nhật tồn kho cho mã bánh " + maBanh
                        + ". Vui lòng kiểm tra số lượng còn lại.");
            }
        }
    }

    private void setNullableInt(PreparedStatement ps, int index, Integer value) throws Exception {
        if (value == null) {
            ps.setNull(index, java.sql.Types.INTEGER);
        } else {
            ps.setInt(index, value);
        }
    }

    private Integer parseOptionalInt(String text) {
        if (text == null || text.trim().isEmpty() || "null".equalsIgnoreCase(text.trim())) {
            return null;
        }
        return Integer.parseInt(text.trim());
    }

    private String lookupTenBanh(Integer maBanh) {
        String sql = "SELECT TenBanh FROM banh WHERE MaBanh = ?";
        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maBanh);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("TenBanh");
                }
            }
        } catch (Exception ex) {
            // Keep UI flow smooth; unknown code will be handled at save by FK constraints.
        }
        return "";
    }

    private String lookupTenNvl(Integer maNvl) {
        String sql = "SELECT Ten FROM nguyenlieu WHERE MaNL = ?";
        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, maNvl);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Ten");
                }
            }
        } catch (Exception ex) {
            // Keep UI flow smooth; unknown code will be handled at save by FK constraints.
        }
        return "";
    }

    private Double parseOptionalDouble(String text) {
        if (text == null || text.trim().isEmpty()) {
            return null;
        }
        return Double.parseDouble(text.trim());
    }

    private void validateChiTietInput(Integer maBanh, Integer maNvl, Integer soLuong, Double donGia) {
        if ((maBanh == null && maNvl == null) || (maBanh != null && maNvl != null)) {
            throw new IllegalArgumentException("Chi tiết phiếu nhập phải chọn đúng 1 trong 2: Mã bánh hoặc Mã NVL.");
        }
        if (soLuong == null || soLuong <= 0) {
            throw new IllegalArgumentException("Số lượng phải > 0.");
        }
        if (donGia == null || donGia < 0) {
            throw new IllegalArgumentException("Đơn giá phải >= 0.");
        }
    }

    private class ActionRenderer extends JButton implements TableCellRenderer {
        public ActionRenderer() {
            setText("≡");
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            setForeground(primaryDark);
            return this;
        }
    }

    private class ActionEditor extends DefaultCellEditor {
        private final JButton button;

        public ActionEditor(JTable table) {
            super(new JCheckBox());
            button = new JButton("≡");
            button.setFont(new Font("Segoe UI", Font.BOLD, 16));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.addActionListener(e -> {
                int row = table.getEditingRow();
                if (row >= 0) {
                    int maPN = Integer.parseInt(String.valueOf(table.getValueAt(row, 0)));
                    SwingUtilities.invokeLater(() -> showChiTietDialog(maPN));
                }
                fireEditingStopped();
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                int column) {
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "≡";
        }
    }

    private void exportPhieuNhapExcel() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn nơi lưu Excel Phiếu nhập");
        chooser.setSelectedFile(new File("PhieuNhap_Export.xlsx"));
        int option = chooser.showSaveDialog(this);
        if (option != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = ensureXlsxExtension(chooser.getSelectedFile());
        try {
            ExcelImportExportBUS excelBus = new ExcelImportExportBUS();
            excelBus.exportPhieuNhapToExcel(file);
            JOptionPane.showMessageDialog(this,
                    "Xuất Excel thành công:\n" + file.getAbsolutePath(),
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Xuất Excel thất bại: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void importPhieuNhapExcel() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn file Excel Phiếu nhập để import");
        int option = chooser.showOpenDialog(this);
        if (option != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();
        try {
            ExcelImportExportBUS excelBus = new ExcelImportExportBUS();
            ExcelImportResultDTO rs = excelBus.importPhieuNhapFromExcel(file);

            if (rs.isSuccess()) {
                lastImportErrors.clear();
                setExportErrorButtonEnabled(false);
                loadPhieuNhap();
                JOptionPane.showMessageDialog(this,
                        "Import thành công " + rs.getSuccessRows() + "/" + rs.getTotalRows() + " dòng.",
                        "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("Import thất bại. Tổng dòng đọc: ").append(rs.getTotalRows()).append("\n");
            int max = Math.min(12, rs.getErrors().size());
            for (int i = 0; i < max; i++) {
                sb.append("- ").append(rs.getErrors().get(i)).append("\n");
            }
            if (rs.getErrors().size() > max) {
                sb.append("... và ").append(rs.getErrors().size() - max).append(" lỗi khác.");
            }

            lastImportErrors.clear();
            lastImportErrors.addAll(rs.getErrors());
            setExportErrorButtonEnabled(!lastImportErrors.isEmpty());

            JTextArea area = new JTextArea(sb.toString(), 14, 70);
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            area.setEditable(false);
            JOptionPane.showMessageDialog(this, new JScrollPane(area), "Kết quả import", JOptionPane.WARNING_MESSAGE);

            if (!lastImportErrors.isEmpty()) {
                int choose = JOptionPane.showConfirmDialog(this,
                        "Bạn có muốn xuất file lỗi import để kiểm tra nhanh không?",
                        "Xuất lỗi import",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (choose == JOptionPane.YES_OPTION) {
                    exportImportErrors(lastImportErrors);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Import Excel thất bại: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportPhieuNhapTemplate() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Lưu file mẫu import Phiếu nhập");
        chooser.setSelectedFile(new File("PhieuNhap_Import_Template.xlsx"));
        int option = chooser.showSaveDialog(this);
        if (option != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = ensureXlsxExtension(chooser.getSelectedFile());
        try {
            ExcelImportExportBUS excelBus = new ExcelImportExportBUS();
            excelBus.exportPhieuNhapTemplate(file);
            JOptionPane.showMessageDialog(this,
                    "Đã tạo file mẫu:\n" + file.getAbsolutePath(),
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Tạo file mẫu thất bại: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private File ensureXlsxExtension(File file) {
        String path = file.getAbsolutePath();
        if (!path.toLowerCase().endsWith(".xlsx")) {
            return new File(path + ".xlsx");
        }
        return file;
    }

    private void setExportErrorButtonEnabled(boolean enabled) {
        if (btnExportErrors != null) {
            btnExportErrors.setEnabled(enabled);
        }
    }

    private void exportImportErrors(List<String> errors) {
        if (errors == null || errors.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Chưa có lỗi import để xuất.",
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] options = { "TXT", "XLSX" };
        String type = (String) JOptionPane.showInputDialog(
                this,
                "Chọn định dạng file lỗi:",
                "Xuất lỗi import",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (type == null) {
            return;
        }

        JFileChooser chooser = new JFileChooser();
        if ("XLSX".equals(type)) {
            chooser.setSelectedFile(new File("Import_PhieuNhap_Errors.xlsx"));
        } else {
            chooser.setSelectedFile(new File("Import_PhieuNhap_Errors.txt"));
        }

        int option = chooser.showSaveDialog(this);
        if (option != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File target = chooser.getSelectedFile();
        try {
            if ("XLSX".equals(type)) {
                if (!target.getAbsolutePath().toLowerCase().endsWith(".xlsx")) {
                    target = new File(target.getAbsolutePath() + ".xlsx");
                }
                exportErrorsToXlsx(target, errors);
            } else {
                if (!target.getAbsolutePath().toLowerCase().endsWith(".txt")) {
                    target = new File(target.getAbsolutePath() + ".txt");
                }
                exportErrorsToTxt(target, errors);
            }

            JOptionPane.showMessageDialog(this,
                    "Đã xuất file lỗi:\n" + target.getAbsolutePath(),
                    "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Xuất file lỗi thất bại: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportErrorsToTxt(File file, List<String> errors) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("Danh sách lỗi import phiếu nhập").append(System.lineSeparator());
        sb.append("Tổng lỗi: ").append(errors.size()).append(System.lineSeparator()).append(System.lineSeparator());
        for (int i = 0; i < errors.size(); i++) {
            sb.append(i + 1).append(". ").append(errors.get(i)).append(System.lineSeparator());
        }
        Files.write(file.toPath(), sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    private void exportErrorsToXlsx(File file, List<String> errors) throws Exception {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("ImportErrors");
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("STT");
            header.createCell(1).setCellValue("Noi dung loi");

            for (int i = 0; i < errors.size(); i++) {
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(i + 1);
                row.createCell(1).setCellValue(errors.get(i));
            }

            sheet.autoSizeColumn(0);
            sheet.setColumnWidth(1, 24000);

            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
            }
        }
    }
}
