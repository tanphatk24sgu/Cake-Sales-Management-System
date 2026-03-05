import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class QuanLiBanhPanel extends JPanel {
    
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
    
    public QuanLiBanhPanel() {
        setBackground(bgColor);
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        // Thêm các thành phần
        add(createHeader(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createToolbar(), BorderLayout.SOUTH);
        
        // Load dữ liệu mẫu
        loadSampleData();
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
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        
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
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        
        // Định nghĩa cột
        String[] columns = {"Mã bánh", "Tên bánh", "Số lượng", "Đơn vị tính", "Loại bánh", "Hãng SX"};
        
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
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Số lượng
        
        // Set độ rộng cột
        table.getColumnModel().getColumn(0).setPreferredWidth(80);   // Mã
        table.getColumnModel().getColumn(1).setPreferredWidth(200);  // Tên
        table.getColumnModel().getColumn(2).setPreferredWidth(100);  // Số lượng
        table.getColumnModel().getColumn(3).setPreferredWidth(100);  // Đơn vị
        table.getColumnModel().getColumn(4).setPreferredWidth(120);  // Loại
        table.getColumnModel().getColumn(5).setPreferredWidth(120);  // Hãng
        
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
                
                // Căn giữa cột 0 và 2
                if (column == 0 || column == 2) {
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
        
        JLabel lblTotal = new JLabel("Tổng: " + tableModel.getRowCount() + " bánh");
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
    
    private void loadSampleData() {
        // Dữ liệu mẫu
        Object[][] data = {
            {1, "Bánh kem socola", 50, "Cái", "Bánh kem", "ABC Bakery"},
            {2, "Bánh mì bơ tỏi", 100, "Cái", "Bánh mì", "XYZ Shop"},
            {3, "Bánh su kem", 75, "Hộp", "Bánh ngọt", "ABC Bakery"},
            {4, "Bánh croissant", 30, "Cái", "Bánh mì", "Paris Bakery"},
            {5, "Bánh phô mai", 45, "Cái", "Bánh kem", "Sweet Home"},
            {6, "Bánh cupcake", 80, "Hộp", "Bánh ngọt", "ABC Bakery"},
            {7, "Bánh tiramisu", 25, "Cái", "Bánh kem", "Italy House"},
            {8, "Bánh tart trứng", 60, "Cái", "Bánh ngọt", "XYZ Shop"},
        };
        
        for (Object[] row : data) {
            tableModel.addRow(row);
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
        Object[] rowData = new Object[tableModel.getColumnCount()];
        for (int i = 0; i < tableModel.getColumnCount(); i++) {
            rowData[i] = tableModel.getValueAt(selectedRow, i);
        }
        
        JDialog dialog = createBanhDialog("Chỉnh sửa bánh", rowData);
        dialog.setVisible(true);
    }
    
    private JDialog createBanhDialog(String title, Object[] data) {
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
        
        String[] labels = {"Mã bánh:", "Tên bánh:", "Số lượng:", "Đơn vị tính:", "Loại bánh:", "Hãng SX:"};
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
            if (data == null) {
                // Thêm mới
                Object[] newRow = new Object[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    newRow[i] = fields[i].getText();
                }
                tableModel.addRow(newRow);
                JOptionPane.showMessageDialog(dialog, "Thêm bánh thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Cập nhật
                int selectedRow = table.getSelectedRow();
                for (int i = 0; i < fields.length; i++) {
                    tableModel.setValueAt(fields[i].getText(), selectedRow, i);
                }
                JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
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
                "Vui lòng chọn bánh cần xóa!", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String tenBanh = tableModel.getValueAt(selectedRow, 1).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc muốn xóa \"" + tenBanh + "\"?",
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
        loadSampleData();
        JOptionPane.showMessageDialog(this, "Đã làm mới dữ liệu!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }
    
}
