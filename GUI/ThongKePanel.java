import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser;

import BUS.ThongKeBUS;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Date;
import java.util.Calendar;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ThongKePanel extends JPanel {

    private final ThongKeBUS thongKeBUS = new ThongKeBUS();

    private JTable tblDoanhThu;
    private JTable tblThuNhap;
    private JTable tblNhap;
    private JTable tblBan;
    private JTabbedPane tabbedPane;

    private DefaultTableModel modelDoanhThu;
    private DefaultTableModel modelThuNhap;
    private DefaultTableModel modelNhap;
    private DefaultTableModel modelBan;

    private JDateChooser dcTuNgay;
    private JDateChooser dcDenNgay;
    private JButton btnThongKe;
    private JButton btnRefresh;
    private JButton btnExportTabExcel;

    private final Color primaryColor = new Color(236, 72, 153);
    private final Color primaryLight = new Color(251, 207, 232);
    private final Color btnBlue = new Color(59, 130, 246);
    private final Color btnGreen = new Color(34, 197, 94);
    private final Color bgColor = new Color(249, 250, 251);

    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
    private final Font normalFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font boldFont = new Font("Segoe UI", Font.BOLD, 14);

    public ThongKePanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(bgColor);
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        add(createHeader(), BorderLayout.NORTH);
        add(createCenterContent(), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);

        initDefaultDates();
        thongKeTatCa();
    }

    private JPanel createHeader() {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setBackground(bgColor);

        JLabel icon = new JLabel();
        try {
            ImageIcon chartIcon = new ImageIcon("img/icon/bar-chart.png");
            Image img = chartIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            icon.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            icon.setText("📊");
        }

        JLabel title = new JLabel("THỐNG KÊ");
        title.setFont(titleFont);
        title.setForeground(primaryColor);

        titlePanel.add(icon);
        titlePanel.add(title);
        return titlePanel;
    }

    private JComponent createCenterContent() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(boldFont);
        tabbedPane.setBackground(Color.WHITE);

        modelDoanhThu = new DefaultTableModel(new String[] { "Ngày", "Số hóa đơn", "Doanh thu" }, 0);
        tblDoanhThu = createTable(modelDoanhThu);
        tabbedPane.addTab("Doanh thu", createTableContainer(tblDoanhThu, "Bảng 1 - Thống kê doanh thu"));

        modelThuNhap = new DefaultTableModel(new String[] { "Ngày", "Doanh thu", "Chi phí nhập", "Thu nhập" }, 0);
        tblThuNhap = createTable(modelThuNhap);
        tabbedPane.addTab("Thu nhập", createTableContainer(tblThuNhap, "Bảng 2 - Thống kê thu nhập"));

        modelNhap = new DefaultTableModel(new String[] { "Mã bánh", "Tên bánh", "SL nhập", "Giá trị nhập" }, 0);
        tblNhap = createTable(modelNhap);
        tabbedPane.addTab("Số lượng nhập", createTableContainer(tblNhap, "Bảng 3 - Thống kê số lượng nhập hàng"));

        modelBan = new DefaultTableModel(new String[] { "Mã bánh", "Tên bánh", "SL bán", "Doanh thu bán" }, 0);
        tblBan = createTable(modelBan);
        tabbedPane.addTab("Số lượng bán", createTableContainer(tblBan, "Bảng 4 - Thống kê số lượng bán ra"));

        return tabbedPane;
    }

    private JPanel createTableContainer(JTable table, String subtitle) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(bgColor);

        JLabel lblSub = new JLabel(subtitle);
        lblSub.setFont(boldFont);
        lblSub.setForeground(new Color(107, 114, 128));

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(new Color(229, 231, 235)));

        panel.add(lblSub, BorderLayout.NORTH);
        panel.add(sp, BorderLayout.CENTER);
        return panel;
    }

    private JTable createTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setRowHeight(38);
        table.setFont(normalFont);
        table.setGridColor(new Color(243, 244, 246));
        table.setSelectionBackground(primaryLight);
        styleTableHeader(table);

        return table;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        controlPanel.setBackground(bgColor);

        dcTuNgay = new JDateChooser();
        dcDenNgay = new JDateChooser();
        dcTuNgay.setDateFormatString("dd-MM-yyyy");
        dcDenNgay.setDateFormatString("dd-MM-yyyy");
        dcTuNgay.setPreferredSize(new Dimension(140, 32));
        dcDenNgay.setPreferredSize(new Dimension(140, 32));

        btnThongKe = createStyledButton("Thống kê", btnBlue, 130);
        btnRefresh = createStyledButton("Làm mới", btnGreen, 130);
        btnExportTabExcel = createStyledButton("⬇ Excel tab", new Color(16, 185, 129), 130);

        btnThongKe.addActionListener(e -> thongKeTatCa());
        btnRefresh.addActionListener(e -> {
            initDefaultDates();
            thongKeTatCa();
        });
        btnExportTabExcel.addActionListener(e -> exportCurrentTabToExcel());

        controlPanel.add(new JLabel("Từ ngày:"));
        controlPanel.add(dcTuNgay);
        controlPanel.add(new JLabel("Đến ngày:"));
        controlPanel.add(dcDenNgay);
        controlPanel.add(btnThongKe);
        controlPanel.add(btnRefresh);
        controlPanel.add(btnExportTabExcel);

        return controlPanel;
    }

    private void initDefaultDates() {
        Calendar cal = Calendar.getInstance();
        java.util.Date denNgay = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        java.util.Date tuNgay = cal.getTime();

        dcTuNgay.setDate(tuNgay);
        dcDenNgay.setDate(denNgay);
    }

    private void thongKeTatCa() {
        java.util.Date tuUtil = dcTuNgay.getDate();
        java.util.Date denUtil = dcDenNgay.getDate();

        if (tuUtil == null || denUtil == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ từ ngày và đến ngày.", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Date tuNgay = new Date(tuUtil.getTime());
        Date denNgay = new Date(denUtil.getTime());

        if (tuNgay.after(denNgay)) {
            JOptionPane.showMessageDialog(this, "Từ ngày không được lớn hơn đến ngày.", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        modelDoanhThu.setRowCount(0);
        for (Object[] row : thongKeBUS.thongKeDoanhThu(tuNgay, denNgay)) {
            modelDoanhThu.addRow(row);
        }

        modelThuNhap.setRowCount(0);
        for (Object[] row : thongKeBUS.thongKeThuNhap(tuNgay, denNgay)) {
            modelThuNhap.addRow(row);
        }

        modelNhap.setRowCount(0);
        for (Object[] row : thongKeBUS.thongKeSoLuongNhap(tuNgay, denNgay)) {
            modelNhap.addRow(row);
        }

        modelBan.setRowCount(0);
        for (Object[] row : thongKeBUS.thongKeSoLuongBan(tuNgay, denNgay)) {
            modelBan.addRow(row);
        }
    }

    private void exportCurrentTabToExcel() {
        if (tabbedPane == null) {
            return;
        }

        int idx = tabbedPane.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Không xác định được tab thống kê hiện tại.", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JTable table;
        String tabName;
        if (idx == 0) {
            table = tblDoanhThu;
            tabName = "ThongKe_DoanhThu";
        } else if (idx == 1) {
            table = tblThuNhap;
            tabName = "ThongKe_ThuNhap";
        } else if (idx == 2) {
            table = tblNhap;
            tabName = "ThongKe_SoLuongNhap";
        } else {
            table = tblBan;
            tabName = "ThongKe_SoLuongBan";
        }

        if (table.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "Tab hiện tại chưa có dữ liệu để xuất.",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Chọn nơi lưu Excel thống kê");
        chooser.setSelectedFile(new File(tabName + ".xlsx"));
        int option = chooser.showSaveDialog(this);
        if (option != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = ensureXlsxExtension(chooser.getSelectedFile());
        try {
            writeTableToExcel(file, tabbedPane.getTitleAt(idx), table);
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

    private void writeTableToExcel(File file, String sheetName, JTable table) throws Exception {
        try (Workbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet(sheetName);

            Row headerRow = sheet.createRow(0);
            for (int c = 0; c < table.getColumnCount(); c++) {
                headerRow.createCell(c).setCellValue(table.getColumnName(c));
            }

            for (int r = 0; r < table.getRowCount(); r++) {
                Row row = sheet.createRow(r + 1);
                for (int c = 0; c < table.getColumnCount(); c++) {
                    Object value = table.getValueAt(r, c);
                    if (value instanceof Number) {
                        row.createCell(c).setCellValue(((Number) value).doubleValue());
                    } else {
                        row.createCell(c).setCellValue(value == null ? "" : String.valueOf(value));
                    }
                }
            }

            for (int i = 0; i < table.getColumnCount(); i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(file)) {
                wb.write(fos);
            }
        }
    }

    private File ensureXlsxExtension(File file) {
        String path = file.getAbsolutePath();
        if (!path.toLowerCase().endsWith(".xlsx")) {
            return new File(path + ".xlsx");
        }
        return file;
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

        btn.setPreferredSize(new Dimension(width, 40));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(boldFont);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);

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
}
