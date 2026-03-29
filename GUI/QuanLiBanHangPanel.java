

import BUS.ChuongTrinhKhuyenMaiBUS;
import BUS.HoaDonBUS;
import DTO.ChiTietHoaDonDTO;
import DTO.ChuongTrinhKhuyenMaiDTO;
import DTO.HoaDonDTO;
import Database.ConnectDatabase;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class QuanLiBanHangPanel extends JPanel {

    // Components
    private JTable tableProducts, tableCart;
    private DefaultTableModel productModel, cartModel;
    private TableRowSorter<DefaultTableModel> productSorter;
    private JTextField txtSearchProduct, txtPhoneCustomer, txtCustomerName;
    private JLabel lblTotalAmount, lblDiscount, lblFinalAmount, lblCustomerPoints, lblPromotionName;
    private JButton btnPay, btnClearCart, btnSelectPromotion;

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
    private double customerDiscount = 0;
    private double promotionDiscount = 0;
    private ArrayList<PromotionOption> promotionOptions = new ArrayList<>();
    private PromotionOption selectedPromotion = null;
    private boolean isUpdatingCart = false;
    private int selectedCustomerId = -1;

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

        // Load dữ liệu sản phẩm từ DB
        loadProducts();
        refreshPromotionsFromManager();
        updatePromotionButtonState();
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
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

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
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));

        JButton btnSearch = createSmallButton("Tìm", primaryColor);
        btnSearch.addActionListener(e -> applyProductSearchFilter());
        txtSearchProduct.addActionListener(e -> applyProductSearchFilter());

        searchPanel.add(txtSearchProduct);
        searchPanel.add(btnSearch);

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        // Bảng sản phẩm
        String[] columns = { "Mã", "Tên bánh", "Đơn giá", "Tồn kho" };
        productModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableProducts = new JTable(productModel);
        productSorter = new TableRowSorter<>(productModel);
        tableProducts.setRowSorter(productSorter);
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
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        // Header
        JLabel lblTitle = new JLabel("🛒 GIỎ HÀNG");
        lblTitle.setFont(headerFont);
        lblTitle.setForeground(primaryDark);

        // Bảng giỏ hàng (cột 5: GiftMeta — C = quà từ dòng đã mua, S = dòng quà thêm;
        // ẩn khỏi JTable)
        String[] columns = { "STT", "Tên bánh", "SL", "Đơn giá", "Thành tiền", "GiftMeta" };
        cartModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2 && !isGiftRow(row); // Không cho sửa dòng quà tặng tự động
            }
        };

        tableCart = new JTable(cartModel);
        if (tableCart.getColumnModel().getColumnCount() > 5) {
            tableCart.removeColumn(tableCart.getColumnModel().getColumn(5));
        }
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
            if (!isUpdatingCart && e.getColumn() == 2) {
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
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

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
        gbc.gridx = 0;
        gbc.gridy = 0;
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
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Tên khách hàng:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        txtCustomerName = new JTextField();
        txtCustomerName.setEditable(false);
        txtCustomerName.setPreferredSize(new Dimension(200, 32));
        txtCustomerName.setBackground(new Color(243, 244, 246));
        formPanel.add(txtCustomerName, gbc);

        // Điểm tích lũy
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Điểm tích lũy:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 2;
        lblCustomerPoints = new JLabel("0 điểm");
        lblCustomerPoints.setFont(boldFont);
        lblCustomerPoints.setForeground(successColor);
        formPanel.add(lblCustomerPoints, gbc);

        // Nút khách lẻ
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
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
                BorderFactory.createEmptyBorder(15, 20, 15, 20)));

        JLabel lblTitle = new JLabel("💰 THANH TOÁN");
        lblTitle.setFont(headerFont);
        lblTitle.setForeground(primaryDark);

        // Thông tin thanh toán
        JPanel infoPanel = new JPanel(new GridLayout(4, 2, 10, 10));
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

        JLabel lbl4 = new JLabel("CT khuyến mãi:");
        lbl4.setFont(normalFont);
        lblPromotionName = new JLabel("Chưa chọn");
        lblPromotionName.setFont(boldFont);
        lblPromotionName.setForeground(new Color(59, 130, 246));
        lblPromotionName.setHorizontalAlignment(SwingConstants.RIGHT);

        infoPanel.add(lbl1);
        infoPanel.add(lblTotalAmount);
        infoPanel.add(lbl2);
        infoPanel.add(lblDiscount);
        infoPanel.add(lbl3);
        infoPanel.add(lblFinalAmount);
        infoPanel.add(lbl4);
        infoPanel.add(lblPromotionName);

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

        btnSelectPromotion = createStyledButton("🎁 Chọn / đổi KM", new Color(59, 130, 246), 160);
        btnSelectPromotion.addActionListener(e -> showPromotionPicker());

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

        buttonPanel.add(btnSelectPromotion);
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

    private void loadProducts() {
        productModel.setRowCount(0);
        String sql = "SELECT b.MaBanh, b.TenBanh, b.SoLuong, "
            + "COALESCE((SELECT cthd.DonGia FROM chitiethoadon cthd "
            + "          WHERE cthd.MaBanh = b.MaBanh "
            + "          ORDER BY cthd.MaHD DESC LIMIT 1), "
            + "         (SELECT ctpn.DonGia FROM ct_phieunhaphang ctpn "
            + "          WHERE ctpn.MaBanh = b.MaBanh "
            + "          ORDER BY ctpn.MaPhieuNhap DESC LIMIT 1), 0) AS DonGia "
            + "FROM banh b ORDER BY b.MaBanh";
        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int maBanh = rs.getInt("MaBanh");
                String tenBanh = rs.getString("TenBanh");
                double donGia = rs.getDouble("DonGia");
                int tonKho = rs.getInt("SoLuong");
                productModel.addRow(new Object[] { maBanh, tenBanh, moneyFormat.format(donGia), tonKho });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Không tải được danh sách bánh từ CSDL: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshPromotionsFromManager() {
        promotionOptions.clear();
        ChuongTrinhKhuyenMaiBUS bus = new ChuongTrinhKhuyenMaiBUS();
        for (ChuongTrinhKhuyenMaiDTO km : bus.getList()) {
            String loai = km.getLoaiKhuyenMai() != null ? km.getLoaiKhuyenMai().trim() : "";
            String type = laChuongTrinhMuaTang(km, loai) ? "BUY_GET" : "PERCENT";
            String tenMua = chuanHoaTenSanPham(km.getTenBanhMua());
            if (tenMua.isEmpty()) {
                tenMua = timTenBanhTheoMa(km.getMaBanhMua());
            }
            String tenTang = chuanHoaTenSanPham(km.getTenBanhTang());
            if (tenTang.isEmpty()) {
                tenTang = timTenBanhTheoMa(km.getMaBanhTang());
            }
            int slMua = Math.max(1, km.getSoLuongMua());
            int slTang = Math.max(1, km.getSoLuongTang());
            PromotionOption option = new PromotionOption(
                    "KM" + km.getMaKM(),
                    km.getTenCTKM(),
                    type,
                    km.getDieuKienToiThieu(),
                    km.getPhanTramGiam(),
                    tenMua,
                    tenTang,
                    slMua,
                    slTang);
            option.ngayBatDau = km.getNgayBatDau();
            option.ngayKetThuc = km.getNgayKetThuc();
            promotionOptions.add(option);
        }
    }

    // ==================== ACTIONS ====================
    private void addToCart() {
        int selectedRow = tableProducts.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = tableProducts.convertRowIndexToModel(selectedRow);

        // Lấy thông tin sản phẩm
        String tenBanh = productModel.getValueAt(modelRow, 1).toString();
        String donGia = productModel.getValueAt(modelRow, 2).toString();

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
        cartModel.addRow(new Object[] { stt, tenBanh, 1, donGia, moneyFormat.format(price), null });

        updateCartTotal();
    }

    private void applyProductSearchFilter() {
        if (productSorter == null) {
            return;
        }

        String keyword = txtSearchProduct == null ? "" : txtSearchProduct.getText().trim();
        if (keyword.isEmpty()) {
            productSorter.setRowFilter(null);
            return;
        }

        RowFilter<DefaultTableModel, Object> byIdOrName = RowFilter.regexFilter("(?i)" + Pattern.quote(keyword), 0, 1);
        productSorter.setRowFilter(byIdOrName);
    }

    private void removeFromCart() {
        int selectedRow = tableCart.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
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
        if (cartModel.getRowCount() == 0)
            return;

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
        if (isGiftRow(row)) {
            cartModel.setValueAt("0 VNĐ", row, 3);
            cartModel.setValueAt("0 VNĐ", row, 4);
            return;
        }
        int qty = Integer.parseInt(cartModel.getValueAt(row, 2).toString());
        double price = parsePrice(cartModel.getValueAt(row, 3).toString());
        double total = qty * price;
        cartModel.setValueAt(moneyFormat.format(total), row, 4);
    }

    private void updateCartTotal() {
        isUpdatingCart = true;
        removeGiftRows();
        refreshPromotionsFromManager();
        totalAmount = 0;

        for (int i = 0; i < cartModel.getRowCount(); i++) {
            updateRowTotal(i);
            totalAmount += parsePrice(cartModel.getValueAt(i, 4).toString());
        }

        if (selectedPromotion != null) {
            selectedPromotion = findPromotionByCode(selectedPromotion.code);
            if (selectedPromotion != null && !isPromotionEligible(selectedPromotion)) {
                selectedPromotion = null;
            }
        }

        applyGiftRowsFromPromotion();

        totalAmount = 0;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            updateRowTotal(i);
            totalAmount += parsePrice(cartModel.getValueAt(i, 4).toString());
        }

        promotionDiscount = selectedPromotion == null ? 0 : calculatePromotionDiscount(selectedPromotion);
        discount = customerDiscount + promotionDiscount;
        double finalAmount = Math.max(0, totalAmount - discount);

        updatePromotionButtonState();

        lblTotalAmount.setText(moneyFormat.format(totalAmount));
        lblDiscount.setText("-" + moneyFormat.format(discount));
        lblFinalAmount.setText(moneyFormat.format(finalAmount));
        lblPromotionName.setText(selectedPromotion == null ? "Chưa chọn" : selectedPromotion.code);
        isUpdatingCart = false;
    }

    private double parsePrice(String priceStr) {
        return Double.parseDouble(priceStr.replaceAll("[^0-9]", ""));
    }

    private void findCustomer() {
        String phone = txtPhoneCustomer.getText().trim();
        if (phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "SELECT MaKH, Ho, Ten FROM khachhang WHERE SDT = ? LIMIT 1";
        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    selectedCustomerId = rs.getInt("MaKH");
                    txtCustomerName.setText(rs.getString("Ho") + " " + rs.getString("Ten"));
                    lblCustomerPoints.setText("0 điểm");
                    customerDiscount = 0;
                    updateCartTotal();
                    return;
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Lỗi tìm khách hàng: " + ex.getMessage(),
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        selectedCustomerId = -1;
        JOptionPane.showMessageDialog(this,
                "Không tìm thấy khách hàng trong CSDL.",
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
        txtCustomerName.setText("");
        lblCustomerPoints.setText("0 điểm");
        customerDiscount = 0;
        updateCartTotal();
    }

    private void setGuestCustomer() {
        txtPhoneCustomer.setText("");
        txtCustomerName.setText("Khách lẻ");
        lblCustomerPoints.setText("0 điểm");
        selectedCustomerId = -1;
        customerDiscount = 0;
        updateCartTotal();
    }

    private void processPayment() {
        if (cartModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng trống!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double finalAmount = Math.max(0, totalAmount - discount);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận thanh toán?\n\nTổng tiền: " + moneyFormat.format(finalAmount),
                "Xác nhận thanh toán",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int maNV = getDefaultEmployeeId();
                if (maNV <= 0) {
                    throw new IllegalStateException("Không tìm thấy nhân viên hợp lệ để lập hóa đơn.");
                }

                ArrayList<ChiTietHoaDonDTO> dsChiTiet = buildInvoiceDetailsFromCart();
                if (dsChiTiet.isEmpty()) {
                    throw new IllegalArgumentException("Giỏ hàng chưa có sản phẩm hợp lệ để thanh toán.");
                }

                HoaDonDTO hd = new HoaDonDTO();
                hd.setNgayLapHD(new Date());
                hd.setMaNV(maNV);
                hd.setMaKH(selectedCustomerId > 0 ? selectedCustomerId : 0);
                hd.setThanhTien(finalAmount);

                HoaDonBUS bus = new HoaDonBUS();
                int maHD = bus.themKemChiTiet(hd, dsChiTiet);
                if (maHD <= 0) {
                    throw new IllegalStateException("Không tạo được hóa đơn.");
                }

                JOptionPane.showMessageDialog(this,
                        "✅ Thanh toán thành công!\n\nMã hóa đơn: " + maHD,
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);

                // Reset
                cartModel.setRowCount(0);
                txtPhoneCustomer.setText("");
                txtCustomerName.setText("");
                lblCustomerPoints.setText("0 điểm");
                selectedCustomerId = -1;
                customerDiscount = 0;
                promotionDiscount = 0;
                selectedPromotion = null;
                discount = 0;
                updateCartTotal();
                loadProducts();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Thanh toán thất bại: " + ex.getMessage(),
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private int getDefaultEmployeeId() {
        String sql = "SELECT MaNV FROM nhanvien ORDER BY MaNV LIMIT 1";
        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("MaNV");
            }
        } catch (Exception ex) {
            // Trả về 0 để flow gọi xử lý thông báo lỗi thống nhất.
        }
        return 0;
    }

    private ArrayList<ChiTietHoaDonDTO> buildInvoiceDetailsFromCart() {
        ArrayList<ChiTietHoaDonDTO> ds = new ArrayList<>();
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            String tenTrongGio = String.valueOf(cartModel.getValueAt(i, 1));
            String tenBanh = normalizeProductNameForLookup(tenTrongGio);
            int maBanh = getProductIdByName(tenBanh);
            if (maBanh <= 0) {
                throw new IllegalArgumentException("Không tìm thấy mã bánh cho sản phẩm: " + tenTrongGio);
            }

            int soLuong = Integer.parseInt(String.valueOf(cartModel.getValueAt(i, 2)));
            if (soLuong <= 0) {
                continue;
            }

            double donGia = parsePrice(String.valueOf(cartModel.getValueAt(i, 3)));
            if (isGiftRow(i)) {
                donGia = 0;
            }

            ChiTietHoaDonDTO ct = new ChiTietHoaDonDTO();
            ct.setMaBanh(maBanh);
            ct.setSoLuong(soLuong);
            ct.setDonGia(donGia);
            double thanhTien = soLuong * donGia;
            ct.setThanhTien(thanhTien);
            ct.setDiem((int) (thanhTien / 100000));
            ds.add(ct);
        }
        return ds;
    }

    private int getProductIdByName(String tenBanh) {
        String key = chuanHoaTenSanPham(tenBanh);
        for (int i = 0; i < productModel.getRowCount(); i++) {
            String ten = chuanHoaTenSanPham(String.valueOf(productModel.getValueAt(i, 1)));
            if (key.equals(ten)) {
                return Integer.parseInt(String.valueOf(productModel.getValueAt(i, 0)));
            }
        }
        return 0;
    }

    private String normalizeProductNameForLookup(String rawName) {
        if (rawName == null) {
            return "";
        }
        String name = rawName.trim();
        if (name.endsWith("(Tặng)")) {
            name = name.substring(0, name.length() - "(Tặng)".length()).trim();
        }
        return name;
    }

    private void showPromotionPicker() {
        refreshPromotionsFromManager();
        double subtotalChoKm = tinhTongTienGioChoDieuKienKm();
        List<PromotionOption> eligible = getEligiblePromotions(subtotalChoKm);
        if (eligible.isEmpty() && selectedPromotion == null) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng chưa đủ điều kiện khuyến mãi.", "Thông báo",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        ArrayList<MucChonKhuyenMai> mucChon = new ArrayList<>();
        mucChon.add(new MucChonKhuyenMai(null));
        for (PromotionOption o : eligible) {
            mucChon.add(new MucChonKhuyenMai(o));
        }

        MucChonKhuyenMai macDinh = mucChon.get(0);
        if (selectedPromotion != null) {
            for (MucChonKhuyenMai m : mucChon) {
                if (m.option != null && selectedPromotion.code.equals(m.option.code)) {
                    macDinh = m;
                    break;
                }
            }
        } else if (!eligible.isEmpty()) {
            macDinh = mucChon.get(1);
        }

        MucChonKhuyenMai chon = (MucChonKhuyenMai) JOptionPane.showInputDialog(
                this,
                "Chọn chương trình (hoặc \"Không áp dụng\" để bỏ KM):",
                "Áp dụng khuyến mãi",
                JOptionPane.QUESTION_MESSAGE,
                null,
                mucChon.toArray(),
                macDinh);

        if (chon == null) {
            return;
        }
        selectedPromotion = chon.option;
        updateCartTotal();
    }

    private List<PromotionOption> getEligiblePromotions() {
        return getEligiblePromotions(totalAmount);
    }

    private List<PromotionOption> getEligiblePromotions(double tongChoKmGiamPhanTram) {
        ArrayList<PromotionOption> eligible = new ArrayList<>();
        for (PromotionOption option : promotionOptions) {
            if (isPromotionEligible(option, tongChoKmGiamPhanTram)) {
                eligible.add(option);
            }
        }
        return eligible;
    }

    /**
     * Tổng tiền hàng (bỏ dòng quà tặng) — dùng khi mở hộp thoại KM vì totalAmount
     * có thể chưa khớp.
     */
    private double tinhTongTienGioChoDieuKienKm() {
        double t = 0;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            if (isGiftRow(i)) {
                continue;
            }
            updateRowTotal(i);
            Object cell = cartModel.getValueAt(i, 4);
            if (cell != null) {
                t += parsePrice(cell.toString());
            }
        }
        return t;
    }

    private String timTenBanhTheoMa(int maBanh) {
        if (maBanh <= 0) {
            return "";
        }
        for (int i = 0; i < productModel.getRowCount(); i++) {
            try {
                int ma = Integer.parseInt(productModel.getValueAt(i, 0).toString().trim());
                if (ma == maBanh) {
                    return chuanHoaTenSanPham(productModel.getValueAt(i, 1).toString());
                }
            } catch (Exception ignored) {
                // bỏ qua
            }
        }
        return "";
    }

    /** So khớp “Mua X tặng Y” kể cả khác khoảng trắng / dạng Unicode. */
    private static boolean laLoaiMuaXTangYTheoTen(String loai) {
        if (loai == null) {
            return false;
        }
        String compact = Normalizer.normalize(loai.trim(), Normalizer.Form.NFC).replaceAll("\\s+", "");
        return compact.equalsIgnoreCase("MuaxtặngY");
    }

    private static boolean laLoaiGiamPhanTram(String loai) {
        if (loai == null) {
            return false;
        }
        String compact = Normalizer.normalize(loai.trim(), Normalizer.Form.NFC).replaceAll("\\s+", "");
        return compact.equalsIgnoreCase("Giảmphầntrăm");
    }

    /**
     * Nhận diện KM mua/tặng: theo tên loại hoặc theo có MaBanhMua + MaBanhTang
     * (JOIN tên đôi khi rỗng).
     */
    private static boolean laChuongTrinhMuaTang(ChuongTrinhKhuyenMaiDTO km, String loaiRaw) {
        if (laLoaiMuaXTangYTheoTen(loaiRaw)) {
            return true;
        }
        return km.getMaBanhMua() > 0 && km.getMaBanhTang() > 0 && !laLoaiGiamPhanTram(loaiRaw);
    }

    private static String chuanHoaTenSanPham(String ten) {
        if (ten == null) {
            return "";
        }
        return ten.trim().replaceAll("\\s+", " ");
    }

    /**
     * So sánh theo lịch (năm-tháng-ngày), tránh KM bị loại sai khi DB lưu 00:00:00
     * ở ngày kết thúc
     * hoặc khác múi giờ so với so sánh Date tuyệt đối.
     */
    private boolean conTrongKhoangKhuyenMai(Date ngayBatDau, Date ngayKetThuc, Date lucKiemTra) {
        if (ngayBatDau == null || ngayKetThuc == null) {
            return true;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(lucKiemTra);
        int yN = cal.get(Calendar.YEAR);
        int mN = cal.get(Calendar.MONTH);
        int dN = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(ngayBatDau);
        int y0 = cal.get(Calendar.YEAR);
        int m0 = cal.get(Calendar.MONTH);
        int d0 = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(ngayKetThuc);
        int y1 = cal.get(Calendar.YEAR);
        int m1 = cal.get(Calendar.MONTH);
        int d1 = cal.get(Calendar.DAY_OF_MONTH);

        long keyN = yN * 10000L + mN * 100L + dN;
        long key0 = y0 * 10000L + m0 * 100L + d0;
        long key1 = y1 * 10000L + m1 * 100L + d1;
        return keyN >= key0 && keyN <= key1;
    }

    private boolean isPromotionEligible(PromotionOption option) {
        return isPromotionEligible(option, totalAmount);
    }

    private boolean isPromotionEligible(PromotionOption option, double tongChoKmGiamPhanTram) {
        Date now = new Date();
        if (!conTrongKhoangKhuyenMai(option.ngayBatDau, option.ngayKetThuc, now)) {
            return false;
        }

        if ("PERCENT".equals(option.type)) {
            return tongChoKmGiamPhanTram >= option.minOrderValue;
        }
        if ("BUY_GET".equals(option.type)) {
            if (chuanHoaTenSanPham(option.buyProductName).isEmpty()
                    || chuanHoaTenSanPham(option.giftProductName).isEmpty()) {
                return false;
            }
            int qty = getCartQuantity(option.buyProductName);
            return qty >= option.buyQty;
        }
        return false;
    }

    private double calculatePromotionDiscount(PromotionOption option) {
        if ("PERCENT".equals(option.type)) {
            return totalAmount * option.percentValue / 100.0;
        }
        if ("BUY_GET".equals(option.type)) {
            return option.existingGiftDiscount;
        }
        return 0;
    }

    private int getCartQuantity(String productName) {
        String key = chuanHoaTenSanPham(productName);
        if (key.isEmpty()) {
            return 0;
        }
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            if (isGiftRow(i))
                continue;
            String tenHang = chuanHoaTenSanPham(cartModel.getValueAt(i, 1).toString());
            if (key.equals(tenHang)) {
                try {
                    return Integer.parseInt(cartModel.getValueAt(i, 2).toString().trim());
                } catch (NumberFormatException ex) {
                    return 0;
                }
            }
        }
        return 0;
    }

    private void applyGiftRowsFromPromotion() {
        if (selectedPromotion == null || !"BUY_GET".equals(selectedPromotion.type))
            return;

        int buyQty = Math.max(1, selectedPromotion.buyQty);
        int giftQty = Math.max(1, selectedPromotion.giftQty);
        int qtyBuy = getCartQuantity(selectedPromotion.buyProductName);
        int times = qtyBuy / buyQty;
        int qtyGiftTotal = times * giftQty;
        if (qtyGiftTotal <= 0)
            return;

        String giftProductName = selectedPromotion.giftProductName;
        String giftDisplayName = giftProductName + " (Tặng)";
        int remainingFree = qtyGiftTotal;

        for (int i = 0; i < cartModel.getRowCount() && remainingFree > 0; i++) {
            if (isGiftRow(i))
                continue;
            String name = cartModel.getValueAt(i, 1).toString();
            if (!chuanHoaTenSanPham(giftProductName).equals(chuanHoaTenSanPham(name)))
                continue;

            int rowQty = Integer.parseInt(cartModel.getValueAt(i, 2).toString());
            if (rowQty <= remainingFree) {
                cartModel.setValueAt(giftDisplayName, i, 1);
                cartModel.setValueAt("0 VNĐ", i, 3);
                cartModel.setValueAt("0 VNĐ", i, 4);
                cartModel.setValueAt("C", i, 5);
                remainingFree -= rowQty;
            } else {
                int paidQty = rowQty - remainingFree;
                cartModel.setValueAt(remainingFree, i, 2);
                cartModel.setValueAt(giftDisplayName, i, 1);
                cartModel.setValueAt("0 VNĐ", i, 3);
                cartModel.setValueAt("0 VNĐ", i, 4);
                cartModel.setValueAt("C", i, 5);
                double unit = getProductPrice(giftProductName);
                String unitStr = moneyFormat.format(unit);
                cartModel.insertRow(i + 1, new Object[] {
                        0, giftProductName, paidQty, unitStr, moneyFormat.format(unit * paidQty), null
                });
                remainingFree = 0;
            }
        }

        if (remainingFree > 0) {
            cartModel.addRow(new Object[] { 0, giftDisplayName, remainingFree, "0 VNĐ", "0 VNĐ", "S" });
        }
        selectedPromotion.existingGiftDiscount = 0;
        refreshCartSTT();
    }

    private void removeGiftRows() {
        for (int i = cartModel.getRowCount() - 1; i >= 0; i--) {
            if (!isGiftRow(i))
                continue;
            Object meta = cartModel.getColumnCount() > 5 ? cartModel.getValueAt(i, 5) : null;
            if ("C".equals(meta)) {
                revertConvertedGiftRow(i);
            } else {
                cartModel.removeRow(i);
            }
        }
        refreshCartSTT();
    }

    private void revertConvertedGiftRow(int row) {
        if (row < 0 || row >= cartModel.getRowCount())
            return;
        String name = cartModel.getValueAt(row, 1).toString();
        if (!name.endsWith(" (Tặng)"))
            return;
        String base = name.substring(0, name.length() - " (Tặng)".length());
        cartModel.setValueAt(base, row, 1);
        for (int j = 0; j < productModel.getRowCount(); j++) {
            if (base.equals(productModel.getValueAt(j, 1).toString())) {
                String unit = productModel.getValueAt(j, 2).toString();
                cartModel.setValueAt(unit, row, 3);
                break;
            }
        }
        if (cartModel.getColumnCount() > 5) {
            cartModel.setValueAt(null, row, 5);
        }
        updateRowTotal(row);
    }

    private boolean isGiftRow(int row) {
        if (row < 0 || row >= cartModel.getRowCount())
            return false;
        if (cartModel.getColumnCount() > 5) {
            Object meta = cartModel.getValueAt(row, 5);
            if ("S".equals(meta) || "C".equals(meta)) {
                return true;
            }
        }
        Object value = cartModel.getValueAt(row, 1);
        if (value == null)
            return false;
        String s = value.toString();
        return s.endsWith("(Tặng)");
    }

    private void refreshCartSTT() {
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            cartModel.setValueAt(i + 1, i, 0);
        }
    }

    private double getProductPrice(String productName) {
        String key = chuanHoaTenSanPham(productName);
        if (key.isEmpty()) {
            return 0;
        }
        for (int i = 0; i < productModel.getRowCount(); i++) {
            if (key.equals(chuanHoaTenSanPham(productModel.getValueAt(i, 1).toString()))) {
                return parsePrice(productModel.getValueAt(i, 2).toString());
            }
        }
        return 0;
    }

    private PromotionOption findPromotionByCode(String code) {
        for (PromotionOption option : promotionOptions) {
            if (option.code.equals(code))
                return option;
        }
        return null;
    }

    private void updatePromotionButtonState() {
        if (btnSelectPromotion == null)
            return;
        String maDangChon = selectedPromotion != null ? selectedPromotion.code : null;
        refreshPromotionsFromManager();
        if (maDangChon != null) {
            selectedPromotion = findPromotionByCode(maDangChon);
        }
        boolean hasEligible = !getEligiblePromotions().isEmpty();
        boolean moDuocHopThoai = hasEligible || selectedPromotion != null;
        btnSelectPromotion.setVisible(moDuocHopThoai);
        btnSelectPromotion.setEnabled(moDuocHopThoai);
    }

    private static class MucChonKhuyenMai {
        final PromotionOption option;

        MucChonKhuyenMai(PromotionOption option) {
            this.option = option;
        }

        @Override
        public String toString() {
            return option == null ? "— Không áp dụng khuyến mãi —" : option.name;
        }
    }

    private static class PromotionOption {
        String code;
        String name;
        String type;
        double minOrderValue;
        int percentValue;
        String buyProductName;
        String giftProductName;
        int buyQty;
        int giftQty;
        Date ngayBatDau;
        Date ngayKetThuc;
        double existingGiftDiscount;

        PromotionOption(String code, String name, String type, double minOrderValue, int percentValue,
                String buyProductName, String giftProductName, int buyQty, int giftQty) {
            this.code = code;
            this.name = name;
            this.type = type;
            this.minOrderValue = minOrderValue;
            this.percentValue = percentValue;
            this.buyProductName = buyProductName;
            this.giftProductName = giftProductName;
            this.buyQty = buyQty;
            this.giftQty = giftQty;
        }

        @Override
        public String toString() {
            return name;
        }
    }

}
