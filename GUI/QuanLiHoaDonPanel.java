import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.*;
import javax.swing.text.SimpleAttributeSet;

import BUS.ChiTietHoaDonBUS;
import BUS.HoaDonBUS;
import DAO.ChiTietHoaDonDAO;
import DTO.ChiTietHoaDonDTO;
import DTO.HoaDonDTO;

import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class QuanLiHoaDonPanel extends JPanel {
    // Khai báo components
    private JTable tblInvoice, tblDetail;
    private DefaultTableModel invoiceModel, detailModel;
    private JTextField txtSearch;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;

    // Color
    private Color primaryColor = new Color(236, 72, 153);
    private Color primaryDark = new Color(190, 24, 93);
    private Color primaryLight = new Color(251, 207, 232);
    private Color successColor = new Color(34, 197, 94);
    private Color bgColor = new Color(249, 250, 251);
    private Color cardColor = Color.WHITE;

    // Font
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 22);
    private Font headerFont = new Font("Segoe UI", Font.BOLD, 16);
    private Font normalFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font boldFont = new Font("Segoe UI", Font.BOLD, 14);
    private Font priceFont = new Font("Segoe UI", Font.BOLD, 18);

    public QuanLiHoaDonPanel() {
        setBackground(bgColor);
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        add(createHeader(), BorderLayout.NORTH);
        add(createMainContent(), BorderLayout.CENTER);
        
        loadInvoiceFromDB();
        loadDetailFromDB();
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
        JPanel mainContent = new JPanel(new GridLayout(1, 2, 15, 0));
        mainContent.setBackground(bgColor);

        mainContent.add(createInvoicePanel());
        mainContent.add(createDetailPanel());

        return mainContent;
    }

    private JPanel createInvoicePanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(bgColor);
        panel.setBorder((BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        )));

        JLabel lblTitle = new JLabel("🧾 DANH SÁCH HÓA ĐƠN");
        lblTitle.setFont(headerFont);
        lblTitle.setForeground(primaryDark);

        String[] cols = {"Mã HD", "Ngày lập HD", "Mã nhân viên", "Mã khách hàng", "Thành Tiền"};
        invoiceModel = new DefaultTableModel(cols, 0);

        tblInvoice = new JTable(invoiceModel);
        tblInvoice.setRowHeight(40);
        tblInvoice.setFont(normalFont);
        tblInvoice.setGridColor(new Color(243, 244, 246));
        tblInvoice.setSelectionBackground(primaryLight);
        styleTableHeader(tblInvoice);

        tblInvoice.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblInvoice.getColumnModel().getColumn(1).setPreferredWidth(70);
        tblInvoice.getColumnModel().getColumn(2).setPreferredWidth(50);
        tblInvoice.getColumnModel().getColumn(3).setPreferredWidth(70);
        tblInvoice.getColumnModel().getColumn(4).setPreferredWidth(120);
        
        JScrollPane scrollPane = new JScrollPane(tblInvoice);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setBackground(cardColor);

        btnAdd = createStyledButton("+ Thêm mới", new Color(34, 197, 94), 130);
        btnEdit = createStyledButton("✎ Chỉnh sửa", new Color(59, 130, 246), 130);
        btnDelete = createStyledButton("✕ Xóa", new Color(239, 68, 68), 100);
        btnRefresh = createStyledButton("↻ Làm mới", new Color(107, 114, 128), 120);

        btnAdd.addActionListener(e -> showAddDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnDelete.addActionListener(e -> deleteSelected());
        btnRefresh.addActionListener(e -> refreshTable());

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRefresh);

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitle = new JLabel("🧾 DANH SÁCH CHI TIẾT");
        lblTitle.setFont(headerFont);
        lblTitle.setForeground(primaryDark);

        String[] cols = {"Mã HD", "Mã bánh", "Số lượng", "Đơn giá", "Thành tiền", "Điểm"};
        detailModel = new DefaultTableModel(cols, 0);

        tblDetail = new JTable(detailModel);
        tblDetail.setRowHeight(40);
        tblDetail.setFont(normalFont);
        tblDetail.setGridColor(new Color(243, 244, 246));
        tblDetail.setSelectionBackground(primaryLight);
        styleTableHeader(tblDetail);

        tblDetail.getColumnModel().getColumn(0).setPreferredWidth(40);
        tblDetail.getColumnModel().getColumn(1).setPreferredWidth(70);
        tblDetail.getColumnModel().getColumn(2).setPreferredWidth(70);
        tblDetail.getColumnModel().getColumn(3).setPreferredWidth(60);
        tblDetail.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblDetail.getColumnModel().getColumn(5).setPreferredWidth(50);

        JScrollPane scrollPane = new JScrollPane(tblDetail);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setBackground(cardColor);

        btnAdd = createStyledButton("+ Thêm mới", new Color(34, 197, 94), 130);
        btnEdit = createStyledButton("✎ Chỉnh sửa", new Color(59, 130, 246), 130);
        btnDelete = createStyledButton("✕ Xóa", new Color(239, 68, 68), 100);
        btnRefresh = createStyledButton("↻ Làm mới", new Color(107, 114, 128), 120);

        btnAdd.addActionListener(e -> showAddDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnDelete.addActionListener(e -> deleteSelected());
        btnRefresh.addActionListener(e -> refreshTable());

        btnPanel.add(btnAdd);
        btnPanel.add(btnEdit);
        btnPanel.add(btnDelete);
        btnPanel.add(btnRefresh);

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
        
        String[] labels = {"Mã HD:", "Ngày lập HD:", "Mã nhân viên:", "Mã khách hàng:", "Thành tiền:"};
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
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));
            
            // Điền dữ liệu nếu đang sửa
            if (data != null && i < data.length) {
                fields[i].setText(String.valueOf(data[i]));
                if (i == 0) fields[i].setEditable(false); // Không sửa mã
            }
            
            formPanel.add(fields[i], gbc);
        }
        
        // Panel buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(249, 250, 251));
        
        JButton btnSave = createStyledButton("💾 Lưu", new Color(34, 197, 94), 100);
        JButton btnCancel = createStyledButton("Hủy", new Color(107, 114, 128), 100);
        
        btnSave.addActionListener(e -> {
            // TODO: Validate và lưu dữ liệu
            try {
                for (int i = 1; i < fields.length;i++) {
                    if(fields[i].getText().trim().isEmpty()) {
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

                Object[] rowData;

                if(data == null) {
                    rowData = new Object[]{
                        invoiceModel.getRowCount() + 1,
                        sdf.format(ngayLap),
                        maNV,
                        maKH,
                        thanhTien
                    };

                    invoiceModel.addRow(rowData);
                    JOptionPane.showMessageDialog(dialog, "Thêm hóa đơn thành công!");
                } else {
                    int selectedRow = tblInvoice.getSelectedRow();

                    invoiceModel.setValueAt(sdf.format(ngayLap), selectedRow, 1);
                    invoiceModel.setValueAt(maNV, selectedRow, 2);
                    invoiceModel.setValueAt(maKH, selectedRow, 3);
                    invoiceModel.setValueAt(thanhTien, selectedRow, 4);

                    JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
                }

                dialog.dispose();

                // JDialog cthdDialog = createCTHDDialog(maHDMoi)
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
        
        String maHD = invoiceModel.getValueAt(selectedRow, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn xóa \"" + maHD + "\"?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            invoiceModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void loadInvoiceFromDB() {
        HoaDonBUS bus = new HoaDonBUS();
        bus.docDSHD();

        invoiceModel.setRowCount(0);

        for(HoaDonDTO hd : bus.getDSHD()) {
            Object[] row = {
                hd.getMaHD(),
                hd.getNgayLapHD(),
                hd.getMaNV(),
                hd.getMaKH(),
                hd.getThanhTien()
            };
            invoiceModel.addRow(row);
        }
    }

    private void loadDetailFromDB() {
        ChiTietHoaDonBUS bus = new ChiTietHoaDonBUS();
        bus.docDSCTHD();

        detailModel.setRowCount(0);

        for(ChiTietHoaDonDTO ct : bus.getDSCTHD()) {
            Object[] row = {
                ct.getMaHD(),
                ct.getMaBanh(),
                ct.getSoLuong(),
                ct.getDonGia(),
                ct.getThanhTien(),
                ct.getDiem()
            };
            detailModel.addRow(row);
        }
    }

    private void refreshTable() {
        invoiceModel.setRowCount(0);
        loadInvoiceFromDB();
        loadDetailFromDB();
        JOptionPane.showMessageDialog(this, "Đã làm mới dữ liệu!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
}
