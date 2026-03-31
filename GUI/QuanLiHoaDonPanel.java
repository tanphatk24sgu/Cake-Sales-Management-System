import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;

import BUS.ChiTietHoaDonBUS;
import BUS.ExcelImportExportBUS;
import BUS.HoaDonBUS;
import DAO.ReportQueryDAO;
import DTO.ChiTietHoaDonDTO;
import DTO.ExcelImportResultDTO;
import DTO.HoaDonDTO;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.MessageFormat;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class QuanLiHoaDonPanel extends JPanel {
    // Khai báo components
    private JTable tblInvoice;
    private DefaultTableModel invoiceModel;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;
    private JButton btnExportErrors;

    // Color
    private Color primaryColor = new Color(236, 72, 153);
    private Color primaryDark = new Color(190, 24, 93);
    private Color primaryLight = new Color(251, 207, 232);
    private Color bgColor = new Color(249, 250, 251);
    private Color cardColor = Color.WHITE;

    // Font
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 22);
    private Font headerFont = new Font("Segoe UI", Font.BOLD, 16);
    private Font normalFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font boldFont = new Font("Segoe UI", Font.BOLD, 14);
    private final List<String> lastImportErrors = new ArrayList<>();

    public QuanLiHoaDonPanel() {
        setBackground(bgColor);
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);

        loadInvoiceFromDB();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setBackground(bgColor);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setBackground(bgColor);

        JLabel icon = new JLabel();
        try {
            ImageIcon invoiceIcon = new ImageIcon("img/icon/invoice.png");
            Image img = invoiceIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            icon.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            icon.setText("🧾");
        }

        JLabel title = new JLabel("QUẢN LÍ HÓA ĐƠN");
        title.setFont(titleFont);
        title.setForeground(primaryColor);

        titlePanel.add(icon);
        titlePanel.add(title);

        // Ngày giờ
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        datePanel.setBackground(bgColor);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        JLabel lblDate = new JLabel("📅 " + sdf.format(new Date()));
        lblDate.setFont(normalFont);
        lblDate.setForeground(new Color(107, 114, 128));

        datePanel.add(lblDate);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(datePanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createMainContent() {
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(bgColor);

        mainContent.add(createInvoicePanel(), BorderLayout.CENTER);
        return mainContent;
    }

    private JPanel createInvoicePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(bgColor);
        panel.setBorder((BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(15, 15, 15, 15))));

        JLabel lblTitle = new JLabel("🧾 DANH SÁCH HÓA ĐƠN");
        lblTitle.setFont(headerFont);
        lblTitle.setForeground(primaryDark);

        String[] cols = { "Mã HD", "Ngày lập HD", "Mã nhân viên", "Mã khách hàng", "Thành tiền", "☰" };
        invoiceModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5;
            }
        };

        tblInvoice = new JTable(invoiceModel);
        tblInvoice.setRowHeight(40);
        tblInvoice.setFont(normalFont);
        tblInvoice.setGridColor(new Color(243, 244, 246));
        tblInvoice.setSelectionBackground(primaryLight);
        styleTableHeader(tblInvoice);

        tblInvoice.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblInvoice.getColumnModel().getColumn(1).setPreferredWidth(180);
        tblInvoice.getColumnModel().getColumn(2).setPreferredWidth(50);
        tblInvoice.getColumnModel().getColumn(3).setPreferredWidth(70);
        tblInvoice.getColumnModel().getColumn(4).setPreferredWidth(120);
        tblInvoice.getColumnModel().getColumn(5).setPreferredWidth(40);
        tblInvoice.getColumnModel().getColumn(5).setMaxWidth(40);
        tblInvoice.getColumnModel().getColumn(5).setCellRenderer(new MenuButtonRenderer());
        tblInvoice.getColumnModel().getColumn(5).setCellEditor(new MenuButtonEditor(tblInvoice));

        JScrollPane scrollPane = new JScrollPane(tblInvoice);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setBackground(cardColor);

        btnAdd = createStyledButton("+ Thêm mới", new Color(34, 197, 94), 130);
        btnEdit = createStyledButton("✎ Chỉnh sửa", new Color(59, 130, 246), 130);
        btnDelete = createStyledButton("✕ Xóa", new Color(239, 68, 68), 100);
        btnRefresh = createStyledButton("↻ Làm mới", new Color(107, 114, 128), 120);
        JButton btnExportExcel = createStyledButton("⬇ Excel", new Color(16, 185, 129), 105);
        JButton btnImportExcel = createStyledButton("⬆ Excel", new Color(245, 158, 11), 105);
        JButton btnTemplateExcel = createStyledButton("📄 Mẫu", new Color(8, 145, 178), 95);
        btnExportErrors = createStyledButton("⚠ Xuất lỗi", new Color(185, 28, 28), 110);
        btnExportErrors.setEnabled(false);

        btnAdd.addActionListener(e -> showAddDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnDelete.addActionListener(e -> deleteSelected());
        btnRefresh.addActionListener(e -> refreshTable());
        btnExportExcel.addActionListener(e -> exportHoaDonExcel());
        btnImportExcel.addActionListener(e -> importHoaDonExcel());
        btnTemplateExcel.addActionListener(e -> exportHoaDonTemplate());
        btnExportErrors.addActionListener(e -> exportImportErrors(lastImportErrors));

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRefresh);
        btnPanel.add(btnExportExcel);
        btnPanel.add(btnImportExcel);
        btnPanel.add(btnTemplateExcel);
        btnPanel.add(btnExportErrors);

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void styleTableHeader(JTable table) {
        JTableHeader header = table.getTableHeader();
        header.setFont(boldFont);
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setBackground(primaryColor);
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(boldFont);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
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

        btn.setPreferredSize(new Dimension(width, 38));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(boldFont);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color originalColor = bgColor;
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(originalColor.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(originalColor);
            }
        });

        return btn;
    }

    private void showAddDialog() {
        JDialog dialog = createHoaDonDialog("Thêm hóa đơn mới", null);
        dialog.setVisible(true);
    }

    private void showEditDialog() {
        int selectedRow = tblInvoice.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn hóa đơn cần sửa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy dữ liệu dòng đang chọn
        Object[] rowData = new Object[invoiceModel.getColumnCount()];
        for (int i = 0; i < invoiceModel.getColumnCount(); i++) {
            rowData[i] = invoiceModel.getValueAt(selectedRow, i);
        }

        JDialog dialog = createHoaDonDialog("Chỉnh sửa hóa đơn", rowData);
        dialog.setVisible(true);
    }

    private JDialog createHoaDonDialog(String title, Object[] data) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Panel form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        String[] labels = { "Mã HD:", "Ngày lập HD:", "Mã nhân viên:", "Mã khách hàng:", "Thành tiền:" };
        JTextField[] fields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.3;

            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(normalFont);
            formPanel.add(lbl, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;

            fields[i] = new JTextField();
            fields[i].setPreferredSize(new Dimension(200, 35));
            fields[i].setFont(normalFont);
            fields[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(209, 213, 219)),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)));

            // Điền dữ liệu nếu đang sửa
            if (data != null && i < data.length) {
                if (i == 1 && data[i] instanceof java.util.Date) {
                    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                    fields[i].setText(sdf.format((java.util.Date) data[i]));
                } else {
                    fields[i].setText(String.valueOf(data[i]));
                }
                if (i == 0)
                    fields[i].setEditable(false); // Không sửa mã
            }

            formPanel.add(fields[i], gbc);
        }

        if (data == null) {
            fields[0].setText("Tự động");
            fields[0].setEditable(false);
        }

        // Panel buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(249, 250, 251));

        JButton btnSave = createStyledButton("💾 Lưu", new Color(34, 197, 94), 100);
        JButton btnCancel = createStyledButton("Hủy", new Color(107, 114, 128), 100);

        btnSave.addActionListener(e -> {
            try {
                for (int i = 1; i < fields.length; i++) {
                    if (fields[i].getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin!");
                        return;
                    }
                }

                int maNV = Integer.parseInt(fields[2].getText().trim());
                int maKH = Integer.parseInt(fields[3].getText().trim());
                double thanhTien = Double.parseDouble(fields[4].getText().trim());

                String ngayLapStr = fields[1].getText().trim();
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                java.util.Date ngayLap = sdf.parse(ngayLapStr);

                HoaDonDTO hd = new HoaDonDTO();
                hd.setNgayLapHD(ngayLap);
                hd.setMaNV(maNV);
                hd.setMaKH(maKH);
                hd.setThanhTien(thanhTien);

                HoaDonBUS bus = new HoaDonBUS();
                if (data == null) {
                    bus.them(hd);
                    if (hd.getMaHD() <= 0) {
                        JOptionPane.showMessageDialog(dialog, "Không thêm được hóa đơn vào CSDL!");
                        return;
                    }
                    JOptionPane.showMessageDialog(dialog, "Thêm hóa đơn thành công!");
                } else {
                    int maHD = Integer.parseInt(fields[0].getText().trim());
                    hd.setMaHD(maHD);
                    bus.sua(hd);
                    JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
                }

                loadInvoiceFromDB();
                dialog.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Mã NV, KH, và thành tiền phải là số!");
            } catch (java.text.ParseException ex) {
                JOptionPane.showMessageDialog(dialog, "Ngày phải đúng format yyyy-MM-dd");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Lỗi: " + ex.getMessage());
            }
        });
        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        return dialog;
    }

    private void deleteSelected() {
        int selectedRow = tblInvoice.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn bánh cần xóa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int maHD = Integer.parseInt(invoiceModel.getValueAt(selectedRow, 0).toString());
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa \"" + maHD + "\"?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            HoaDonBUS bus = new HoaDonBUS();
            bus.xoa(maHD);
            loadInvoiceFromDB();
            JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void loadInvoiceFromDB() {
        HoaDonBUS bus = new HoaDonBUS();
        bus.docDSHD();

        invoiceModel.setRowCount(0);

        for (HoaDonDTO hd : bus.getDSHD()) {
            Object[] row = {
                    hd.getMaHD(),
                    hd.getNgayLapHD(),
                    hd.getMaNV(),
                    hd.getMaKH(),
                    hd.getThanhTien(),
                    "☰"
            };
            invoiceModel.addRow(row);
        }
    }

    private void refreshTable() {
        invoiceModel.setRowCount(0);
        loadInvoiceFromDB();
        JOptionPane.showMessageDialog(this, "Đã làm mới dữ liệu!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private JTable buildInvoiceDetailTable(int maHD) {
        String[] columns = { "Mã HD", "Mã bánh", "Tên bánh", "Số lượng", "Đơn giá", "Thành tiền", "Điểm" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        ReportQueryDAO reportDAO = new ReportQueryDAO();
        for (Object[] row : reportDAO.getHoaDonDetails(maHD)) {
            model.addRow(row);
        }

        JTable table = new JTable(model);
        table.setFont(normalFont);
        table.setRowHeight(28);
        styleTableHeader(table);
        return table;
    }

    private void printJTable(JTable table, MessageFormat header, MessageFormat footer, String errorMessage) {
        try {
            boolean complete = table.print(JTable.PrintMode.FIT_WIDTH, header, footer, true, null, true, null);
            if (complete) {
                JOptionPane.showMessageDialog(this, "In thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    errorMessage + "\nChi tiết: " + ex.getMessage(),
                    "Lỗi in ấn",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportHoaDonExcel() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn nơi lưu Excel Hóa đơn");
        chooser.setSelectedFile(new File("HoaDon_Export.xlsx"));
        int option = chooser.showSaveDialog(this);
        if (option != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = ensureXlsxExtension(chooser.getSelectedFile());
        try {
            ExcelImportExportBUS excelBus = new ExcelImportExportBUS();
            excelBus.exportHoaDonToExcel(file);
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

    private void importHoaDonExcel() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn file Excel Hóa đơn để import");
        int option = chooser.showOpenDialog(this);
        if (option != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();
        try {
            ExcelImportExportBUS excelBus = new ExcelImportExportBUS();
            ExcelImportResultDTO rs = excelBus.importHoaDonFromExcel(file);

            if (rs.isSuccess()) {
                lastImportErrors.clear();
                setExportErrorButtonEnabled(false);
                loadInvoiceFromDB();
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

    private void exportHoaDonTemplate() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Lưu file mẫu import Hóa đơn");
        chooser.setSelectedFile(new File("HoaDon_Import_Template.xlsx"));
        int option = chooser.showSaveDialog(this);
        if (option != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = ensureXlsxExtension(chooser.getSelectedFile());
        try {
            ExcelImportExportBUS excelBus = new ExcelImportExportBUS();
            excelBus.exportHoaDonTemplate(file);
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
            chooser.setSelectedFile(new File("Import_Errors.xlsx"));
        } else {
            chooser.setSelectedFile(new File("Import_Errors.txt"));
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
        sb.append("Danh sách lỗi import").append(System.lineSeparator());
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

    private void showInvoiceDetailDialog(int maHD) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Chi tiết hóa đơn #" + maHD, true);
        dialog.setSize(920, 560);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 10, 6));
        infoPanel.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        infoPanel.setBackground(Color.WHITE);

        ReportQueryDAO reportDAO = new ReportQueryDAO();
        Object[] header = reportDAO.getHoaDonHeader(maHD);
        String ngayLap = header != null ? String.valueOf(header[1]) : "";
        String tenNV = header != null ? String.valueOf(header[4]) : "";
        String tenKH = header != null ? String.valueOf(header[6]) : "";
        String tongTien = header != null ? String.valueOf(header[2]) : "";

        infoPanel.add(new JLabel("Mã hóa đơn: " + maHD));
        infoPanel.add(new JLabel("Ngày lập: " + ngayLap));
        infoPanel.add(new JLabel("Nhân viên: " + tenNV));
        infoPanel.add(new JLabel("Khách hàng: " + tenKH + " | Tổng tiền: " + tongTien));

        JTable detailTable = buildInvoiceDetailTable(maHD);
        JScrollPane scroll = new JScrollPane(detailTable);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        btnPanel.setBackground(cardColor);

        JButton btnAddDetail = createStyledButton("+ Thêm CT", new Color(34, 197, 94), 110);
        JButton btnEditDetail = createStyledButton("✎ Sửa CT", new Color(59, 130, 246), 110);
        JButton btnDeleteDetail = createStyledButton("✕ Xóa CT", new Color(239, 68, 68), 110);
        JButton btnPrintDetail = createStyledButton("🖨 In hóa đơn", new Color(14, 116, 144), 130);
        JButton btnReloadDetail = createStyledButton("↻ Làm mới", new Color(107, 114, 128), 110);
        JButton btnClose = createStyledButton("Đóng", new Color(75, 85, 99), 90);

        btnAddDetail.addActionListener(e -> {
            if (showDetailCRUDDialog(dialog, maHD, null)) {
                reloadDetailTable(detailTable, maHD);
                loadInvoiceFromDB();
            }
        });

        btnEditDetail.addActionListener(e -> {
            int selectedRow = detailTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn dòng chi tiết cần sửa.", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            ChiTietHoaDonDTO dto = new ChiTietHoaDonDTO();
            dto.setMaHD(maHD);
            dto.setMaBanh(Integer.parseInt(String.valueOf(detailTable.getValueAt(selectedRow, 1))));
            dto.setSoLuong(Integer.parseInt(String.valueOf(detailTable.getValueAt(selectedRow, 3))));
            dto.setDonGia(Double.parseDouble(String.valueOf(detailTable.getValueAt(selectedRow, 4))));
            dto.setThanhTien(Double.parseDouble(String.valueOf(detailTable.getValueAt(selectedRow, 5))));
            dto.setDiem(Integer.parseInt(String.valueOf(detailTable.getValueAt(selectedRow, 6))));

            if (showDetailCRUDDialog(dialog, maHD, dto)) {
                reloadDetailTable(detailTable, maHD);
                loadInvoiceFromDB();
            }
        });

        btnDeleteDetail.addActionListener(e -> {
            int selectedRow = detailTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng chọn dòng chi tiết cần xóa.", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int maBanh = Integer.parseInt(String.valueOf(detailTable.getValueAt(selectedRow, 1)));
            int confirm = JOptionPane.showConfirmDialog(dialog,
                    "Xóa chi tiết bánh mã " + maBanh + " khỏi hóa đơn " + maHD + "?",
                    "Xác nhận xóa",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                ChiTietHoaDonBUS bus = new ChiTietHoaDonBUS();
                bus.docDSCTHD();
                bus.xoa(maHD, maBanh);
                reloadDetailTable(detailTable, maHD);
                loadInvoiceFromDB();
            }
        });

        btnPrintDetail.addActionListener(e -> {
            MessageFormat headerPrint = new MessageFormat("HOA DON MA " + maHD + " | Ngay lap: " + ngayLap);
            MessageFormat footerPrint = new MessageFormat("Trang {0}");
            printJTable(detailTable, headerPrint, footerPrint, "Không thể in hóa đơn này.");
        });

        btnReloadDetail.addActionListener(e -> reloadDetailTable(detailTable, maHD));
        btnClose.addActionListener(e -> dialog.dispose());

        btnPanel.add(btnAddDetail);
        btnPanel.add(btnEditDetail);
        btnPanel.add(btnDeleteDetail);
        btnPanel.add(btnPrintDetail);
        btnPanel.add(btnReloadDetail);
        btnPanel.add(btnClose);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(bgColor);
        top.add(infoPanel, BorderLayout.CENTER);

        dialog.add(top, BorderLayout.NORTH);
        dialog.add(scroll, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void reloadDetailTable(JTable detailTable, int maHD) {
        DefaultTableModel model = (DefaultTableModel) detailTable.getModel();
        model.setRowCount(0);
        ReportQueryDAO reportDAO = new ReportQueryDAO();
        for (Object[] row : reportDAO.getHoaDonDetails(maHD)) {
            model.addRow(row);
        }
    }

    private boolean showDetailCRUDDialog(Window owner, int maHD, ChiTietHoaDonDTO existing) {
        JDialog dialog = new JDialog(owner, existing == null ? "Thêm chi tiết hóa đơn" : "Sửa chi tiết hóa đơn",
                Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setSize(420, 320);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtMaHD = new JTextField(String.valueOf(maHD));
        txtMaHD.setEditable(false);
        JTextField txtMaBanh = new JTextField(existing == null ? "" : String.valueOf(existing.getMaBanh()));
        JTextField txtSoLuong = new JTextField(existing == null ? "" : String.valueOf(existing.getSoLuong()));
        JTextField txtDonGia = new JTextField(existing == null ? "" : String.valueOf(existing.getDonGia()));
        JTextField txtDiem = new JTextField(existing == null ? "" : String.valueOf(existing.getDiem()));

        if (existing != null) {
            txtMaBanh.setEditable(false);
        }

        JTextField[] fields = { txtMaHD, txtMaBanh, txtSoLuong, txtDonGia, txtDiem };
        String[] labels = { "Mã HD:", "Mã bánh:", "Số lượng:", "Đơn giá:", "Điểm (để trống = tự tính):" };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.35;
            form.add(new JLabel(labels[i]), gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.65;
            fields[i].setPreferredSize(new Dimension(220, 32));
            form.add(fields[i], gbc);
        }

        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        bottom.setBackground(new Color(249, 250, 251));
        JButton btnSave = createStyledButton("💾 Lưu", new Color(34, 197, 94), 90);
        JButton btnCancel = createStyledButton("Hủy", new Color(107, 114, 128), 90);

        final boolean[] result = { false };
        btnSave.addActionListener(e -> {
            try {
                int maBanh = Integer.parseInt(txtMaBanh.getText().trim());
                int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
                double donGia = Double.parseDouble(txtDonGia.getText().trim());

                ChiTietHoaDonDTO dto = new ChiTietHoaDonDTO();
                dto.setMaHD(maHD);
                dto.setMaBanh(maBanh);
                dto.setSoLuong(soLuong);
                dto.setDonGia(donGia);
                dto.setThanhTien(soLuong * donGia);

                String diemText = txtDiem.getText().trim();
                if (diemText.isEmpty()) {
                    dto.setDiem((int) ((soLuong * donGia) / 100000));
                } else {
                    dto.setDiem(Integer.parseInt(diemText));
                }

                ChiTietHoaDonBUS bus = new ChiTietHoaDonBUS();
                bus.docDSCTHD();
                if (existing == null) {
                    bus.them(dto);
                } else {
                    bus.sua(dto);
                }

                result[0] = true;
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog,
                        "Dữ liệu không hợp lệ: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        btnCancel.addActionListener(e -> dialog.dispose());

        bottom.add(btnSave);
        bottom.add(btnCancel);

        dialog.add(form, BorderLayout.CENTER);
        dialog.add(bottom, BorderLayout.SOUTH);
        dialog.setVisible(true);
        return result[0];
    }

    private class MenuButtonRenderer extends JButton implements TableCellRenderer {
        public MenuButtonRenderer() {
            setText("☰");
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            setForeground(primaryDark);
            setBackground(isSelected ? primaryLight : Color.WHITE);
            return this;
        }
    }

    private class MenuButtonEditor extends DefaultCellEditor {
        private final JButton button;

        public MenuButtonEditor(JTable table) {
            super(new JCheckBox());
            this.button = new JButton("☰");
            this.button.setFont(new Font("Segoe UI", Font.BOLD, 16));
            this.button.setFocusPainted(false);
            this.button.setBorderPainted(false);
            this.button.setContentAreaFilled(false);
            this.button.addActionListener(e -> {
                int row = table.getEditingRow();
                if (row >= 0) {
                    int maHD = Integer.parseInt(String.valueOf(table.getValueAt(row, 0)));
                    SwingUtilities.invokeLater(() -> showInvoiceDetailDialog(maHD));
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
            return "☰";
        }
    }

}
