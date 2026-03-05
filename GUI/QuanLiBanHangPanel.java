import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.util.Date;
import java.text.SimpleDateFormat;

public class QuanLiBanHangPanel extends JPanel {
    
    // Components
    private JTable tableProducts, tableCart;
    private DefaultTableModel productModel, cartModel;
    private JTextField txtSearchProduct, txtPhoneCustomer, txtCustomerName;
    private JLabel lblTotalAmount, lblDiscount, lblFinalAmount, lblCustomerPoints;
    private JButton btnPay, btnClearCart;
    
    // Màu sắc
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
    
    // Format tiền
    private DecimalFormat moneyFormat = new DecimalFormat("#,### VNĐ");
    
    // Tổng tiền
    private double totalAmount = 0;
    private double discount = 0;
    
    public QuanLiBanHangPanel() {
        setBackground(bgColor);
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        add(createHeader(), BorderLayout.NORTH);
        
        // Main content (chia đôi: sản phẩm + giỏ hàng)
        add(createMainContent(), BorderLayout.CENTER);
        
        // Footer (thanh toán)
        add(createFooter(), BorderLayout.SOUTH);
        
        // Load dữ liệu mẫu
        loadSampleProducts();
    }
    
    // ==================== HEADER ====================
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(bgColor);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Tiêu đề
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setBackground(bgColor);
        
        JLabel icon = new JLabel();
        try {
            ImageIcon cartIcon = new ImageIcon("img/icon/grocery-store.png");
            Image img = cartIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            icon.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            icon.setText("🛒");
        }
        
        JLabel title = new JLabel("  QUẢN LÍ BÁN HÀNG");
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
    
    // ==================== MAIN CONTENT ====================
    private JPanel createMainContent() {
        JPanel mainContent = new JPanel(new GridLayout(1, 2, 15, 0));
        mainContent.setBackground(bgColor);
        
        // Bên trái: Danh sách sản phẩm
        mainContent.add(createProductPanel());
        
        // Bên phải: Giỏ hàng
        mainContent.add(createCartPanel());
        
        return mainContent;
    }
    
    // ==================== PRODUCT PANEL (Bên trái) ====================
    private JPanel createProductPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(cardColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout(10, 0));
        headerPanel.setBackground(cardColor);
        
        JLabel lblTitle = new JLabel("📦 DANH SÁCH SẢN PHẨM");
        lblTitle.setFont(headerFont);
        lblTitle.setForeground(primaryDark);
        
        // Tìm kiếm
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        searchPanel.setBackground(cardColor);
        
        txtSearchProduct = new JTextField(15);
        txtSearchProduct.setPreferredSize(new Dimension(150, 32));
        txtSearchProduct.setFont(normalFont);
        txtSearchProduct.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(209, 213, 219)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JButton btnSearch = createSmallButton("Tìm", primaryColor);
        
        searchPanel.add(txtSearchProduct);
        searchPanel.add(btnSearch);
        
        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);
        
        // Bảng sản phẩm
        String[] columns = {"Mã", "Tên bánh", "Đơn giá", "Tồn kho"};
        productModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableProducts = new JTable(productModel);
        tableProducts.setRowHeight(45);
        tableProducts.setFont(normalFont);
        tableProducts.setGridColor(new Color(243, 244, 246));
        tableProducts.setSelectionBackground(primaryLight);
        styleTableHeader(tableProducts);
        
        // Set column width
        tableProducts.getColumnModel().getColumn(0).setPreferredWidth(50);
        tableProducts.getColumnModel().getColumn(1).setPreferredWidth(180);
        tableProducts.getColumnModel().getColumn(2).setPreferredWidth(100);
        tableProducts.getColumnModel().getColumn(3).setPreferredWidth(70);
        
        // Double click để thêm vào giỏ
        tableProducts.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    addToCart();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableProducts);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Nút thêm vào giỏ
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(cardColor);
        
        JButton btnAddToCart = createStyledButton("+ Thêm vào giỏ", successColor, 150);
        btnAddToCart.addActionListener(e -> addToCart());
        
        buttonPanel.add(btnAddToCart);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // ==================== CART PANEL (Bên phải) ====================
    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(cardColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Header
        JLabel lblTitle = new JLabel("🛒 GIỎ HÀNG");
        lblTitle.setFont(headerFont);
        lblTitle.setForeground(primaryDark);
        
        // Bảng giỏ hàng
        String[] columns = {"STT", "Tên bánh", "SL", "Đơn giá", "Thành tiền"};
        cartModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Chỉ cho sửa số lượng
            }
        };
        
        tableCart = new JTable(cartModel);
        tableCart.setRowHeight(40);
        tableCart.setFont(normalFont);
        tableCart.setGridColor(new Color(243, 244, 246));
        tableCart.setSelectionBackground(primaryLight);
        styleTableHeader(tableCart);
        
        // Set column width
        tableCart.getColumnModel().getColumn(0).setPreferredWidth(40);
        tableCart.getColumnModel().getColumn(1).setPreferredWidth(150);
        tableCart.getColumnModel().getColumn(2).setPreferredWidth(50);
        tableCart.getColumnModel().getColumn(3).setPreferredWidth(90);
        tableCart.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        // Listener khi thay đổi số lượng
        cartModel.addTableModelListener(e -> {
            if (e.getColumn() == 2) {
                updateCartTotal();
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tableCart);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setBackground(cardColor);
        
        JButton btnRemove = createStyledButton("- Xóa sản phẩm", new Color(239, 68, 68), 140);
        btnClearCart = createStyledButton("🗑 Xóa giỏ", new Color(107, 114, 128), 120);
        
        btnRemove.addActionListener(e -> removeFromCart());
        btnClearCart.addActionListener(e -> clearCart());
        
        buttonPanel.add(btnRemove);
        buttonPanel.add(btnClearCart);
        
        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // ==================== FOOTER (Thanh toán) ====================
    private JPanel createFooter() {
        JPanel footer = new JPanel(new GridLayout(1, 2, 15, 0));
        footer.setBackground(bgColor);
        footer.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Bên trái: Thông tin khách hàng
        footer.add(createCustomerPanel());
        
        // Bên phải: Thanh toán
        footer.add(createPaymentPanel());
        
        return footer;
    }
    
    // ==================== CUSTOMER PANEL ====================
    private JPanel createCustomerPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(cardColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel lblTitle = new JLabel("👤 THÔNG TIN KHÁCH HÀNG");
        lblTitle.setFont(headerFont);
        lblTitle.setForeground(primaryDark);
        
        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(cardColor);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // SĐT
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Số điện thoại:"), gbc);
        
        gbc.gridx = 1;
        txtPhoneCustomer = new JTextField(12);
        txtPhoneCustomer.setPreferredSize(new Dimension(150, 32));
        formPanel.add(txtPhoneCustomer, gbc);
        
        gbc.gridx = 2;
        JButton btnFindCustomer = createSmallButton("Tìm", primaryColor);
        btnFindCustomer.addActionListener(e -> findCustomer());
        formPanel.add(btnFindCustomer, gbc);
        
        // Tên khách
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Tên khách hàng:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        txtCustomerName = new JTextField();
        txtCustomerName.setEditable(false);
        txtCustomerName.setPreferredSize(new Dimension(200, 32));
        txtCustomerName.setBackground(new Color(243, 244, 246));
        formPanel.add(txtCustomerName, gbc);
        
        // Điểm tích lũy
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        formPanel.add(new JLabel("Điểm tích lũy:"), gbc);
        
        gbc.gridx = 1; gbc.gridwidth = 2;
        lblCustomerPoints = new JLabel("0 điểm");
        lblCustomerPoints.setFont(boldFont);
        lblCustomerPoints.setForeground(successColor);
        formPanel.add(lblCustomerPoints, gbc);
        
        // Nút khách lẻ
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3;
        JButton btnGuestCustomer = createStyledButton("👤 Khách lẻ (không cần SĐT)", new Color(107, 114, 128), 220);
        btnGuestCustomer.addActionListener(e -> setGuestCustomer());
        formPanel.add(btnGuestCustomer, gbc);
        
        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ==================== PAYMENT PANEL ====================
    private JPanel createPaymentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(primaryLight);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(primaryColor, 2),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        JLabel lblTitle = new JLabel("💰 THANH TOÁN");
        lblTitle.setFont(headerFont);
        lblTitle.setForeground(primaryDark);
        
        // Thông tin thanh toán
        JPanel infoPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        infoPanel.setBackground(primaryLight);
        
        JLabel lbl1 = new JLabel("Tổng tiền:");
        lbl1.setFont(normalFont);
        lblTotalAmount = new JLabel("0 VNĐ");
        lblTotalAmount.setFont(priceFont);
        lblTotalAmount.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JLabel lbl2 = new JLabel("Giảm giá:");
        lbl2.setFont(normalFont);
        lblDiscount = new JLabel("0 VNĐ");
        lblDiscount.setFont(boldFont);
        lblDiscount.setForeground(new Color(239, 68, 68));
        lblDiscount.setHorizontalAlignment(SwingConstants.RIGHT);
        
        JLabel lbl3 = new JLabel("THÀNH TIỀN:");
        lbl3.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lbl3.setForeground(primaryDark);
        lblFinalAmount = new JLabel("0 VNĐ");
        lblFinalAmount.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblFinalAmount.setForeground(primaryDark);
        lblFinalAmount.setHorizontalAlignment(SwingConstants.RIGHT);
        
        infoPanel.add(lbl1);
        infoPanel.add(lblTotalAmount);
        infoPanel.add(lbl2);
        infoPanel.add(lblDiscount);
        infoPanel.add(lbl3);
        infoPanel.add(lblFinalAmount);
        
        // Nút thanh toán
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(primaryLight);
        
        btnPay = new JButton("💳 THANH TOÁN") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnPay.setPreferredSize(new Dimension(200, 50));
        btnPay.setBackground(successColor);
        btnPay.setForeground(Color.WHITE);
        btnPay.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnPay.setBorderPainted(false);
        btnPay.setFocusPainted(false);
        btnPay.setContentAreaFilled(false);
        btnPay.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnPay.addActionListener(e -> processPayment());
        
        // Hover effect
        btnPay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnPay.setBackground(successColor.darker());
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnPay.setBackground(successColor);
            }
        });
        
        buttonPanel.add(btnPay);
        
        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
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
    
    private JButton createSmallButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(60, 32));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(normalFont);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
    private void loadSampleProducts() {
        Object[][] data = {
            {1, "Bánh kem socola", 150000, 50},
            {2, "Bánh mì bơ tỏi", 25000, 100},
            {3, "Bánh su kem", 35000, 75},
            {4, "Bánh croissant", 45000, 30},
            {5, "Bánh phô mai", 120000, 45},
            {6, "Bánh cupcake", 30000, 80},
            {7, "Bánh tiramisu", 180000, 25},
            {8, "Bánh tart trứng", 40000, 60},
            {9, "Bánh mousse", 160000, 35},
            {10, "Bánh bông lan", 55000, 90},
        };
        
        for (Object[] row : data) {
            // Format giá tiền
            row[2] = moneyFormat.format(row[2]);
            productModel.addRow(row);
        }
    }
    
    // ==================== ACTIONS ====================
    private void addToCart() {
        int selectedRow = tableProducts.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Lấy thông tin sản phẩm
        String maBanh = productModel.getValueAt(selectedRow, 0).toString();
        String tenBanh = productModel.getValueAt(selectedRow, 1).toString();
        String donGia = productModel.getValueAt(selectedRow, 2).toString();
        
        // Kiểm tra đã có trong giỏ chưa
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            if (cartModel.getValueAt(i, 1).toString().equals(tenBanh)) {
                // Tăng số lượng
                int currentQty = Integer.parseInt(cartModel.getValueAt(i, 2).toString());
                cartModel.setValueAt(currentQty + 1, i, 2);
                updateRowTotal(i);
                updateCartTotal();
                return;
            }
        }
        
        // Thêm mới vào giỏ
        int stt = cartModel.getRowCount() + 1;
        double price = parsePrice(donGia);
        cartModel.addRow(new Object[]{stt, tenBanh, 1, donGia, moneyFormat.format(price)});
        
        updateCartTotal();
    }
    
    private void removeFromCart() {
        int selectedRow = tableCart.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        cartModel.removeRow(selectedRow);
        
        // Cập nhật lại STT
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            cartModel.setValueAt(i + 1, i, 0);
        }
        
        updateCartTotal();
    }
    
    private void clearCart() {
        if (cartModel.getRowCount() == 0) return;
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa toàn bộ giỏ hàng?", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            cartModel.setRowCount(0);
            updateCartTotal();
        }
    }
    
    private void updateRowTotal(int row) {
        int qty = Integer.parseInt(cartModel.getValueAt(row, 2).toString());
        double price = parsePrice(cartModel.getValueAt(row, 3).toString());
        double total = qty * price;
        cartModel.setValueAt(moneyFormat.format(total), row, 4);
    }
    
    private void updateCartTotal() {
        totalAmount = 0;
        
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            updateRowTotal(i);
            totalAmount += parsePrice(cartModel.getValueAt(i, 4).toString());
        }
        
        double finalAmount = totalAmount - discount;
        
        lblTotalAmount.setText(moneyFormat.format(totalAmount));
        lblDiscount.setText("-" + moneyFormat.format(discount));
        lblFinalAmount.setText(moneyFormat.format(finalAmount));
    }
    
    private double parsePrice(String priceStr) {
        return Double.parseDouble(priceStr.replaceAll("[^0-9]", ""));
    }
    
    private void findCustomer() {
        String phone = txtPhoneCustomer.getText().trim();
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Giả lập tìm khách hàng
        if (phone.equals("0123456789")) {
            txtCustomerName.setText("Nguyễn Văn A");
            lblCustomerPoints.setText("150 điểm");
            discount = 15000; // Giảm giá theo điểm
            updateCartTotal();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Không tìm thấy khách hàng!\nBạn có thể tạo khách hàng mới.", 
                "Thông báo", 
                JOptionPane.INFORMATION_MESSAGE);
            txtCustomerName.setText("");
            lblCustomerPoints.setText("0 điểm");
        }
    }
    
    private void setGuestCustomer() {
        txtPhoneCustomer.setText("");
        txtCustomerName.setText("Khách lẻ");
        lblCustomerPoints.setText("0 điểm");
        discount = 0;
        updateCartTotal();
    }
    
    private void processPayment() {
        if (cartModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng trống!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        double finalAmount = totalAmount - discount;
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Xác nhận thanh toán?\n\nTổng tiền: " + moneyFormat.format(finalAmount),
            "Xác nhận thanh toán",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, 
                "✅ Thanh toán thành công!\n\nMã hóa đơn: HD" + System.currentTimeMillis() % 10000,
                "Thành công",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Reset
            cartModel.setRowCount(0);
            txtPhoneCustomer.setText("");
            txtCustomerName.setText("");
            lblCustomerPoints.setText("0 điểm");
            discount = 0;
            updateCartTotal();
        }
    }
    
}
