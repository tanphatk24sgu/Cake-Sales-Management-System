import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import com.toedter.calendar.JDateChooser;

import BUS.HoaDonBUS;

public class ThongKePanel extends JPanel {
    
    // Khai báo components
    private JTable table;
    private DefaultTableModel tblModel;

    private JComboBox<String> cboLoaiThongKe;
    private JTextField txtThang, txtNam, txtQuy;
    private JDateChooser dcTuNgay, dcDenNgay;

    private JButton btnThongKe, btnRefresh;

    private HoaDonBUS hd = new HoaDonBUS();

    // Color
    private Color primaryColor = new Color(236, 72, 153);
    private Color primaryLight = new Color(251, 207, 232);
    private Color btnBlue = new Color(59, 130, 246);
    private Color btnGreen = new Color(34, 197, 94);
    private Color bgColor = new Color(249, 250, 251);
    // private Color cardColor = Color.WHITE;

    // Font
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
    private Font normalFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font boldFont = new Font("Segoe UI", Font.BOLD, 14);

    public ThongKePanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(bgColor);
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        add(createHeader(), BorderLayout.NORTH);
        add(createTable(), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);
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

    private JPanel createTable() {
        JPanel tablePanel = new JPanel(new BorderLayout());

        String[] cols = {"Loại thống kê", "Giá trị", "Doanh thu"};
        tblModel = new DefaultTableModel(cols, 0);

        table = new JTable(tblModel);
        table.setRowHeight(40);
        table.setFont(normalFont);
        table.setGridColor(new Color(243, 244, 246));
        table.setSelectionBackground(primaryLight);
        styleTableHeader(table);

        JTableHeader header = table.getTableHeader();
        header.setFont(boldFont);
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);

        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        return tablePanel;
    }

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        controlPanel.setBackground(bgColor);

        JPanel row1 = new JPanel((new FlowLayout(FlowLayout.LEFT)));
        row1.setBackground(bgColor);

        cboLoaiThongKe = new JComboBox<>(new String[]{
            "Theo tháng",
            "Theo quý",
            "Theo năm",
            "Khoảng ngày"
        });

        row1.add(new JLabel("Chọn loại:"));
        row1.add(cboLoaiThongKe);

        // Input
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row2.setBackground(bgColor);

        txtThang = new JTextField(5);
        txtThang.setPreferredSize(new Dimension(150, 32));
        txtNam = new JTextField(5);
        txtNam.setPreferredSize(new Dimension(150, 32));
        txtQuy = new JTextField(5);
        txtQuy.setPreferredSize(new Dimension(150, 32));

        dcTuNgay = new JDateChooser();
        dcDenNgay = new JDateChooser();

        dcTuNgay.setPreferredSize(new Dimension(120, 30));
        dcDenNgay.setPreferredSize(new Dimension(120, 30));
        
        dcTuNgay.setDateFormatString("dd-MM-yyyy");
        dcDenNgay.setDateFormatString("dd-MM-yyyy");

        row2.add(new JLabel("Tháng:"));
        row2.add(txtThang);
        row2.add(new JLabel("Năm:"));
        row2.add(txtNam);
        row2.add(new JLabel("Quý:"));
        row2.add(txtQuy);
        row2.add(new JLabel("Từ:"));
        row2.add(dcTuNgay);
        row2.add(new JLabel("Đến:"));
        row2.add(dcDenNgay);

        // Button
        JPanel row3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row3.setBackground(bgColor);

        btnThongKe = createStyledButton("Thống kê", btnBlue, 130);
        btnRefresh = createStyledButton("Làm mới", btnGreen, 130);

        btnThongKe.addActionListener(e -> xuLyThongKe());
        btnRefresh.addActionListener(e -> refresh());

        row3.add(btnThongKe);
        row3.add(btnRefresh);

        controlPanel.add(row1);
        controlPanel.add(row2);
        controlPanel.add(row3);

        return controlPanel;
    }

    private void xuLyThongKe() {
        tblModel.setRowCount(0);

        String loai = cboLoaiThongKe.getSelectedItem().toString();
        double res = 0;

        try {
            switch (loai) {
                case "Theo tháng":
                    int thang = Integer.parseInt(txtThang.getText());
                    int nam = Integer.parseInt(txtNam.getText());
                    res = hd.thongKeDoanhThuTheoThang(thang, nam);
                    tblModel.addRow(new Object[] {"Tháng", thang + "/" + nam, res});
                    break;
                case "Theo quý":
                    int quy = Integer.parseInt(txtQuy.getText());
                    nam = Integer.parseInt(txtQuy.getText());
                    res = hd.thongKeDoanhThuTheoQuy(quy, nam);
                    tblModel.addRow(new Object[] {"Quý", "Q" + quy + "/" + nam, res});
                    break;
                case "Theo năm":
                    nam = Integer.parseInt(txtNam.getText());
                    res = hd.thongKeDoanhThuTheoNam(nam);
                    tblModel.addRow(new Object[] {"Năm", nam, res});
                    break;
                case "Khoảng ngày":
                    java.util.Date tuNgay = dcTuNgay.getDate();
                    java.util.Date denNgay = dcDenNgay.getDate();

                    if(tuNgay == null || denNgay == null) {
                        JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày!");
                        return;
                    }

                    java.sql.Date tu = new java.sql.Date(tuNgay.getTime());
                    java.sql.Date den = new java.sql.Date(denNgay.getTime());

                    res = hd.thongKeDoanhThuTheoKhoangNgay(tu, den);
                    tblModel.addRow(new Object[] {"Khoảng", tu + " - " + den, res});
                    break;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi nhập dữ liệu!");
        }
    }

    private void refresh() {
        tblModel.setRowCount(0);
    }

    // ==================== HELPER METHODS ====================
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
        
        // Hover effect
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
