import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

import BUS.BanhBus;
import BUS.CongThucBUS;
import DTO.BanhDTO;
import DTO.CongThucDTO;

public class QuanLiBanhPanel extends JPanel {

    // Khai báo components
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;
    private JLabel lblTotal;
    private final BanhBus bus = new BanhBus();
    private final CongThucBUS congThucBus = new CongThucBUS();

    // Màu sắc
    private Color primaryColor = new Color(236, 72, 153);
    private Color primaryLight = new Color(251, 207, 232);
    private Color bgColor = new Color(249, 250, 251);
    private Color cardColor = Color.WHITE;

    // Font
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
    private Font normalFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font boldFont = new Font("Segoe UI", Font.BOLD, 14);

    public QuanLiBanhPanel() {
        setBackground(bgColor);
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Thêm các thành phần
        add(createHeader(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createToolbar(), BorderLayout.SOUTH);

        loadDataFromDB();
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
            ImageIcon cakeIcon = new ImageIcon("img/icon/cake.png");
            Image img = cakeIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            icon.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            icon.setText("🎂");
        }

        JLabel title = new JLabel("  QUẢN LÍ BÁNH");
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
        txtSearch.setText("Nhập tên bánh...");
        txtSearch.setForeground(Color.GRAY);
        txtSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtSearch.getText().equals("Nhập tên bánh...")) {
                    txtSearch.setText("");
                    txtSearch.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtSearch.getText().isEmpty()) {
                    txtSearch.setText("Nhập tên bánh...");
                    txtSearch.setForeground(Color.GRAY);
                }
            }
        });

        JButton btnSearch = createStyledButton("Tìm", primaryColor, 80);
        btnSearch.addActionListener(e -> searchBanh());
        txtSearch.addActionListener(e -> searchBanh());

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
        String[] columns = { "Mã bánh", "Tên bánh", "Số lượng", "Đơn vị tính", "Loại bánh", "Hãng SX", "☰" };

        // Tạo model (không cho edit trực tiếp)
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
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
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Số lượng

        // Set độ rộng cột
        table.getColumnModel().getColumn(0).setPreferredWidth(80); // Mã
        table.getColumnModel().getColumn(1).setPreferredWidth(200); // Tên
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Số lượng
        table.getColumnModel().getColumn(3).setPreferredWidth(100); // Đơn vị
        table.getColumnModel().getColumn(4).setPreferredWidth(120); // Loại
        table.getColumnModel().getColumn(5).setPreferredWidth(120); // Hãng
        table.getColumnModel().getColumn(6).setPreferredWidth(40); // Action
        table.getColumnModel().getColumn(6).setMaxWidth(40);
        table.getColumnModel().getColumn(6).setCellRenderer(new FormulaActionRenderer());
        table.getColumnModel().getColumn(6).setCellEditor(new FormulaActionEditor(table));

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

                // Căn giữa cột số và cột action
                if (column == 0 || column == 2 || column == 6) {
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

        lblTotal = new JLabel("Tổng: " + tableModel.getRowCount() + " bánh");
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

    private void loadDataFromDB() {
        bus.loadData();
        tableModel.setRowCount(0);
        for (BanhDTO b : bus.getList()) {
            tableModel.addRow(new Object[] {
                    b.getMaBanh(),
                    b.getTenBanh(),
                    b.getSoLuong(),
                    b.getMaDVT(),
                    b.getMaLoai(),
                    b.getMaHang(),
                    "☰"
            });
        }
        if (lblTotal != null) {
            lblTotal.setText("Tổng: " + tableModel.getRowCount() + " bánh");
        }
    }

    private void searchBanh() {
        String key = txtSearch.getText().trim().toLowerCase();
        if (key.isEmpty() || key.equals("nhập tên bánh...")) {
            loadDataFromDB();
            return;
        }

        bus.loadData();
        tableModel.setRowCount(0);
        for (BanhDTO b : bus.getList()) {
            boolean matchName = b.getTenBanh() != null && b.getTenBanh().toLowerCase().contains(key);
            boolean matchId = false;
            try {
                matchId = b.getMaBanh() == Integer.parseInt(key);
            } catch (NumberFormatException ignored) {
            }
            if (matchName || matchId) {
                tableModel.addRow(new Object[] {
                        b.getMaBanh(),
                        b.getTenBanh(),
                        b.getSoLuong(),
                        b.getMaDVT(),
                        b.getMaLoai(),
                        b.getMaHang(),
                        "☰"
                });
            }
        }
        if (lblTotal != null) {
            lblTotal.setText("Tổng: " + tableModel.getRowCount() + " bánh");
        }
    }

    // ==================== ACTIONS ====================
    private void showAddDialog() {
        JDialog dialog = createBanhDialog("Thêm bánh mới", null);
        dialog.setVisible(true);
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn bánh cần sửa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Lấy dữ liệu dòng đang chọn
        Object[] rowData = new Object[6];
        for (int i = 0; i < 6; i++) {
            rowData[i] = tableModel.getValueAt(selectedRow, i);
        }

        JDialog dialog = createBanhDialog("Chỉnh sửa bánh", rowData);
        dialog.setVisible(true);
    }

    private JDialog createBanhDialog(String title, Object[] data) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setSize(520, 560);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Panel form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        String[] labels = { "Mã bánh:", "Tên bánh:", "Số lượng:", "Đơn vị tính:", "Loại bánh:", "Hãng SX:" };
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
                fields[i].setText(String.valueOf(data[i]));
                if (i == 0)
                    fields[i].setEditable(false); // Không sửa mã
            }

            formPanel.add(fields[i], gbc);
        }

        // Khu vực công thức khi thêm/sửa bánh tự sản xuất
        JPanel formulaPanel = new JPanel(new BorderLayout(6, 6));
        formulaPanel.setBackground(Color.WHITE);
        formulaPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        JLabel lblFormula = new JLabel("Công thức (bánh tự sản xuất):");
        lblFormula.setFont(boldFont);
        JTextArea txtCongThuc = new JTextArea(5, 30);
        txtCongThuc.setFont(normalFont);
        txtCongThuc.setLineWrap(true);
        txtCongThuc.setWrapStyleWord(true);
        txtCongThuc.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        if (data != null) {
            int maBanh = Integer.parseInt(String.valueOf(data[0]));
            CongThucDTO ct = congThucBus.getByMaBanh(maBanh);
            if (ct != null && ct.getCachLam() != null) {
                txtCongThuc.setText(ct.getCachLam());
            }
        }

        formulaPanel.add(lblFormula, BorderLayout.NORTH);
        formulaPanel.add(new JScrollPane(txtCongThuc), BorderLayout.CENTER);

        JPanel centerWrap = new JPanel(new BorderLayout(0, 10));
        centerWrap.setBackground(Color.WHITE);
        centerWrap.add(formPanel, BorderLayout.NORTH);
        centerWrap.add(formulaPanel, BorderLayout.CENTER);

        // Panel buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(249, 250, 251));

        JButton btnSave = createStyledButton("💾 Lưu", new Color(34, 197, 94), 100);
        JButton btnCancel = createStyledButton("Hủy", new Color(107, 114, 128), 100);

        btnSave.addActionListener(e -> {
            try {
                BanhDTO b = new BanhDTO();
                b.setTenBanh(fields[1].getText().trim());
                b.setSoLuong(Integer.parseInt(fields[2].getText().trim()));
                b.setMaDVT(Integer.parseInt(fields[3].getText().trim()));
                b.setMaLoai(Integer.parseInt(fields[4].getText().trim()));
                b.setMaHang(Integer.parseInt(fields[5].getText().trim()));

                boolean ok;
                if (data == null) {
                    b.setMaBanh(Integer.parseInt(fields[0].getText().trim()));
                    ok = bus.add(b);
                } else {
                    b.setMaBanh(Integer.parseInt(fields[0].getText().trim()));
                    ok = bus.update(b);
                }

                if (!ok) {
                    JOptionPane.showMessageDialog(dialog, "Lưu dữ liệu thất bại!", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                saveFormulaIfAny(b.getMaBanh(), b.getMaDVT(), txtCongThuc.getText().trim());

                loadDataFromDB();
                JOptionPane.showMessageDialog(dialog, "Lưu thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ: " + ex.getMessage(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        dialog.add(centerWrap, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        return dialog;
    }

    private void saveFormulaIfAny(int maBanh, int maDvt, String cachLam) {
        if (cachLam == null || cachLam.isEmpty()) {
            return;
        }

        CongThucDTO ct = new CongThucDTO();
        ct.setMaBanh(maBanh);
        ct.setMaDVT(maDvt <= 0 ? 1 : maDvt);
        ct.setCachLam(cachLam);
        congThucBus.save(ct);
    }

    private void showCongThucDialog(int maBanh, String tenBanh, int maDvt) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Công thức bánh", true);
        dialog.setSize(620, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));
        JLabel lblTitle = new JLabel("🎂 " + tenBanh + " (Mã: " + maBanh + ")");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(primaryColor);
        header.add(lblTitle, BorderLayout.WEST);

        JTextArea txtFormula = new JTextArea();
        txtFormula.setFont(normalFont);
        txtFormula.setLineWrap(true);
        txtFormula.setWrapStyleWord(true);
        txtFormula.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        CongThucDTO current = congThucBus.getByMaBanh(maBanh);
        if (current != null && current.getCachLam() != null) {
            txtFormula.setText(current.getCachLam());
        }

        JScrollPane scroll = new JScrollPane(txtFormula);
        scroll.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttons.setBackground(new Color(249, 250, 251));
        JButton btnSave = createStyledButton("💾 Lưu", new Color(34, 197, 94), 95);
        JButton btnDelete = createStyledButton("🗑 Xóa", new Color(239, 68, 68), 95);
        JButton btnClose = createStyledButton("Đóng", new Color(107, 114, 128), 95);

        btnSave.addActionListener(e -> {
            String text = txtFormula.getText().trim();
            if (text.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Công thức đang trống.", "Thông báo",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            CongThucDTO ct = new CongThucDTO();
            ct.setMaBanh(maBanh);
            ct.setMaDVT(maDvt <= 0 ? 1 : maDvt);
            ct.setCachLam(text);
            if (congThucBus.save(ct)) {
                JOptionPane.showMessageDialog(dialog, "Đã lưu công thức!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dialog, "Lưu công thức thất bại!", "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dialog,
                    "Bạn có chắc muốn xóa công thức của bánh này?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                if (congThucBus.delete(maBanh)) {
                    txtFormula.setText("");
                    JOptionPane.showMessageDialog(dialog, "Đã xóa công thức.", "Thông báo",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Không có công thức để xóa.", "Thông báo",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        btnClose.addActionListener(e -> dialog.dispose());

        buttons.add(btnSave);
        buttons.add(btnDelete);
        buttons.add(btnClose);

        dialog.add(header, BorderLayout.NORTH);
        dialog.add(scroll, BorderLayout.CENTER);
        dialog.add(buttons, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private class FormulaActionRenderer extends JButton implements TableCellRenderer {
        public FormulaActionRenderer() {
            setText("☰");
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            setForeground(primaryColor.darker());
            return this;
        }
    }

    private class FormulaActionEditor extends DefaultCellEditor {
        private final JButton button;

        public FormulaActionEditor(JTable table) {
            super(new JCheckBox());
            button = new JButton("☰");
            button.setFont(new Font("Segoe UI", Font.BOLD, 16));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);
            button.addActionListener(e -> {
                int row = table.getEditingRow();
                if (row >= 0) {
                    int maBanh = Integer.parseInt(String.valueOf(table.getValueAt(row, 0)));
                    String tenBanh = String.valueOf(table.getValueAt(row, 1));
                    int maDvt = Integer.parseInt(String.valueOf(table.getValueAt(row, 3)));
                    SwingUtilities.invokeLater(() -> showCongThucDialog(maBanh, tenBanh, maDvt));
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

    private void deleteSelected() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn bánh cần xóa!",
                    "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int maBanh = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());
        String tenBanh = tableModel.getValueAt(selectedRow, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa \"" + tenBanh + "\"?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (bus.delete(maBanh)) {
                loadDataFromDB();
                JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void refreshTable() {
        loadDataFromDB();
        JOptionPane.showMessageDialog(this, "Đã làm mới dữ liệu!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

}
