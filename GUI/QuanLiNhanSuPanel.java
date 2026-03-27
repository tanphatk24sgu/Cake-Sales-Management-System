// package GUI;

import javax.swing.*;
import javax.swing.table.*;

import BUS.NhanVienBUS;
import DTO.NhanVienDTO;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class QuanLiNhanSuPanel extends JPanel {

    // Khai báo components
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;

    // Màu sắc
    private Color primaryColor = new Color(236, 72, 153);
    private Color primaryLight = new Color(251, 207, 232);
    private Color bgColor = new Color(249, 250, 251);
    private Color cardColor = Color.WHITE;

    // Font
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
    private Font normalFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font boldFont = new Font("Segoe UI", Font.BOLD, 14);

    public QuanLiNhanSuPanel() {
        setBackground(bgColor);
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Thêm các thành phần
        add(createHeader(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createToolbar(), BorderLayout.SOUTH);

        loadDataFromDB();
        // Table bắt đầu trống
    }

    // ==================== HEADER ====================
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setBackground(bgColor);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Tiêu đề bên trái
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setBackground(bgColor);

        JLabel icon = new JLabel();
        try {
            ImageIcon userIcon = new ImageIcon("img/icon/user.png");
            Image img = userIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            icon.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            icon.setText("👤");
        }

        JLabel title = new JLabel("  QUẢN LÍ NHÂN SỰ");
        title.setFont(titleFont);
        title.setForeground(primaryColor);

        titlePanel.add(icon);
        titlePanel.add(title);

        // Panel tìm kiếm bên phải
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setBackground(bgColor);

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(normalFont);

        txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(220, 38));
        txtSearch.setFont(normalFont);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        // Placeholder
        txtSearch.setText("Nhập tên nhân viên...");
        txtSearch.setForeground(Color.GRAY);
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtSearch.getText().equals("Nhập tên nhân viên...")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setText("Nhập tên nhân viên...");
                    txtSearch.setForeground(Color.GRAY);
                }
            }
        });

        JButton btnSearch = createStyledButton("Tìm", primaryColor, 80);

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(searchPanel, BorderLayout.EAST);

        return header;
    }

    // ==================== TABLE ====================
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(cardColor);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        // Định nghĩa cột
        String[] columns = { "Mã NV", "Tên nhân viên", "Chức vụ", "Số điện thoại", "Email", "Lương" };

        // Tạo model (không cho edit trực tiếp)
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Tạo table
        table = new JTable(tableModel);
        table.setRowHeight(45);
        table.setFont(normalFont);
        table.setGridColor(new Color(243, 244, 246));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(primaryLight);
        table.setSelectionForeground(Color.BLACK);

        // Style header
        JTableHeader header = table.getTableHeader();
        header.setFont(boldFont);
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 50));
        header.setBorder(BorderFactory.createEmptyBorder());

        // Căn giữa header
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setBackground(primaryColor);
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(boldFont);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        // Căn giữa các cột số
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Mã

        // Set độ rộng cột
        table.getColumnModel().getColumn(0).setPreferredWidth(80); // Mã NV
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Tên
        table.getColumnModel().getColumn(2).setPreferredWidth(130); // Chức vụ
        table.getColumnModel().getColumn(3).setPreferredWidth(130); // Số điện thoại
        table.getColumnModel().getColumn(4).setPreferredWidth(150); // Email
        table.getColumnModel().getColumn(5).setPreferredWidth(120); // Lương

        // Alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    c.setBackground(primaryLight);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(249, 250, 251));
                }

                // Căn giữa cột 0
                if (column == 0) {
                    ((JLabel) c).setHorizontalAlignment(JLabel.CENTER);
                } else {
                    ((JLabel) c).setHorizontalAlignment(JLabel.LEFT);
                }

                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    // ==================== TOOLBAR ====================
    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(bgColor);
        toolbar.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Panel nút bên trái
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(bgColor);

        btnAdd = createStyledButton("+ Thêm mới", new Color(34, 197, 94), 130);
        btnEdit = createStyledButton("✎ Chỉnh sửa", new Color(59, 130, 246), 130);
        btnDelete = createStyledButton("✕ Xóa", new Color(239, 68, 68), 100);
        btnRefresh = createStyledButton("↻ Làm mới", new Color(107, 114, 128), 120);

        // Thêm sự kiện
        btnAdd.addActionListener(e -> showAddDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnDelete.addActionListener(e -> deleteSelected());
        btnRefresh.addActionListener(e -> refreshTable());

        leftPanel.add(btnAdd);
        leftPanel.add(btnEdit);
        leftPanel.add(btnDelete);
        leftPanel.add(btnRefresh);

        // Panel thông tin bên phải
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setBackground(bgColor);

        JLabel lblTotal = new JLabel("Tổng: " + tableModel.getRowCount() + " nhân viên");
        lblTotal.setFont(normalFont);
        lblTotal.setForeground(new Color(107, 114, 128));

        rightPanel.add(lblTotal);

        toolbar.add(leftPanel, BorderLayout.WEST);
        toolbar.add(rightPanel, BorderLayout.EAST);

        return toolbar;
    }

    // ==================== HELPER METHODS ====================
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

    // ==================== ACTIONS ====================
    private void showAddDialog() {
        JDialog dialog = createNhanSuDialog("Thêm nhân viên mới", null);
        dialog.setVisible(true);
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn nhân viên cần sửa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy dữ liệu dòng đang chọn
        Object[] rowData = new Object[tableModel.getColumnCount()];
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            rowData[i] = tableModel.getValueAt(selectedRow, i);
        }

        JDialog dialog = createNhanSuDialog("Chỉnh sửa nhân viên", rowData);
        dialog.setVisible(true);
    }

    private JDialog createNhanSuDialog(String title, Object[] data) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Panel form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        String[] labels = { "Mã NV:", "Tên nhân viên:", "Chức vụ:", "Số điện thoại:", "Email:", "Lương:" };
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
            fields[i].setPreferredSize(new Dimension(250, 35));
            fields[i].setFont(normalFont);
            fields[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(209, 213, 219)),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)));

            // Điền dữ liệu nếu đang sửa
            if (data != null && i < data.length) {
                fields[i].setText(String.valueOf(data[i]));
                if (i == 0)
                    fields[i].setEditable(false); // Không sửa mã NV
            }

            formPanel.add(fields[i], gbc);
        }

        // Panel buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(249, 250, 251));

        JButton btnSave = createStyledButton("💾 Lưu", new Color(34, 197, 94), 100);
        JButton btnCancel = createStyledButton("Hủy", new Color(107, 114, 128), 100);

        btnSave.addActionListener(e -> {
            // Validate dữ liệu
            if (fields[0].getText().trim().isEmpty() ||
                fields[1].getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (data == null) {
                // Thêm mới
                Object[] newRow = new Object[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    newRow[i] = fields[i].getText();
                }
                tableModel.addRow(newRow);
                JOptionPane.showMessageDialog(dialog, "Thêm nhân viên thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Cập nhật
                int selectedRow = table.getSelectedRow();
                for (int i = 0; i < fields.length; i++) {
                    tableModel.setValueAt(fields[i].getText(), selectedRow, i);
                }
                JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            dialog.dispose();
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        return dialog;
    }

    private void deleteSelected() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn nhân viên cần xóa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String tenNhanVien = tableModel.getValueAt(selectedRow, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa \"" + tenNhanVien + "\"?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.removeRow(selectedRow);
            JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        JOptionPane.showMessageDialog(this, "Đã làm mới dữ liệu!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

        loadDataFromDB();
    }

    private void loadDataFromDB() {
        NhanVienBUS bus = new NhanVienBUS();
        bus.docDSNV();

        tableModel.setRowCount(0);

        for(NhanVienDTO nv : bus.getDSNV()) {
            Object[] row = {
                nv.getMaNV(),
                nv.getTen(),
                nv.getHo(),
                nv.getChucVu(),
                nv.getGioiTinh(),
                nv.getNgaySinh(),
                nv.getDiaChi(),
                nv.getSdt()
            };
            tableModel.addRow(row);
        }
    }
}