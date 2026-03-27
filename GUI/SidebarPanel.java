

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.File;
import java.net.URL;

// Sidebar điều hướng các màn hình chính của ứng dụng
public class SidebarPanel extends JPanel {
    private int WIDTH = 280;

    // Đường dẫn thư mục icon
    private String iconPath = "img/icon/";

    // ContentPanel để chuyển trang
    private ContentPanel contentPanel;

    // Bảng màu hiện đại
    private Color primaryColor = new Color(236, 72, 153); // Hồng đậm
    private Color primaryDark = new Color(190, 24, 93); // Hồng tối
    private Color primaryLight = new Color(251, 207, 232); // Hồng nhạt
    private Color textColor = Color.WHITE;
    private Color hoverColor = new Color(255, 255, 255, 30); // Trắng trong suốt
    private Color activeColor = new Color(255, 255, 255, 50);

    // Font hiện đại
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 22);
    private Font menuFont = new Font("Segoe UI", Font.PLAIN, 15);
    private Font menuFontBold = new Font("Segoe UI", Font.BOLD, 15);
    private Font smallFont = new Font("Segoe UI", Font.PLAIN, 12);

    private JButton activeButton = null;

    public SidebarPanel(ContentPanel contentPanel) {
        this.contentPanel = contentPanel;

        setPreferredSize(new Dimension(WIDTH, 0));
        setBackground(primaryColor);
        setLayout(new BorderLayout());

        add(createHeader(), BorderLayout.NORTH);
        add(createMenuPanel(), BorderLayout.CENTER);
        add(createFooter(), BorderLayout.SOUTH);
    }

    // Constructor mặc định (không có ContentPanel)
    public SidebarPanel() {
        this(null);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(primaryDark);
        header.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        // Logo (icon bánh từ file)
        ImageIcon cakeIcon = loadIcon("cake.png", 60, 60);
        JLabel logo = new JLabel(cakeIcon);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Tên thương hiệu
        JLabel brandName = new JLabel("SWEET CHILDREN");
        brandName.setFont(titleFont);
        brandName.setForeground(textColor);
        brandName.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Slogan
        JLabel slogan = new JLabel("Hương vị hạnh phúc");
        slogan.setFont(smallFont);
        slogan.setForeground(primaryLight);
        slogan.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(logo);
        header.add(Box.createVerticalStrut(10));
        header.add(brandName);
        header.add(Box.createVerticalStrut(5));
        header.add(slogan);

        return header;
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(primaryColor);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        // Menu items với icons và tên trang tương ứng
        // {icon, text, pageName}
        String[][] menuItems = {
                { "home.png", "Trang chủ", ContentPanel.TRANG_CHU },
                { "grocery-store.png", "Quản lí bán hàng", ContentPanel.BAN_HANG },
                { "sale.png", "Khuyến mãi", ContentPanel.KHUYEN_MAI },
                { "cake.png", "Quản lí bánh", ContentPanel.QUAN_LI_BANH },
                { "multiple-users-silhouette.png", "Quản lí nhân sự", ContentPanel.NHAN_SU },
                { "bar-chart.png", "Thống kê", ContentPanel.THONG_KE },
                { "gift.png", "Tích điểm", ContentPanel.TICH_DIEM },
                { "recipe.png", "Công thức", ContentPanel.CONG_THUC },
                { "supplier.png", "Nhà cung cấp", ContentPanel.NHA_CUNG_CAP },
                { "setting.png", "Cài đặt", ContentPanel.CAI_DAT }
        };

        for (int i = 0; i < menuItems.length; i++) {
            ImageIcon icon = loadIcon(menuItems[i][0], 24, 24);
            String pageName = menuItems[i][2];
            JButton btn = createMenuButton(icon, menuItems[i][1], pageName);
            menuPanel.add(btn);
            menuPanel.add(Box.createVerticalStrut(8));

            // Set item đầu tiên là active
            if (i == 0) {
                activeButton = btn;
                btn.setBackground(activeColor);
                btn.setFont(menuFontBold);
            }
        }

        return menuPanel;
    }

    // Hàm load và resize icon
    private ImageIcon loadIcon(String fileName, int width, int height) {
        try {
            ImageIcon originalIcon = resolveIcon(fileName);
            if (originalIcon == null || originalIcon.getIconWidth() <= 0) {
                return new ImageIcon();
            }
            Image img = originalIcon.getImage();
            Image resizedImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(resizedImg);
        } catch (Exception e) {
            // Trả về icon rỗng nếu không load được
            return new ImageIcon();
        }
    }

    // Hỗ trợ chạy từ IDE, out/production hoặc file jar
    private ImageIcon resolveIcon(String fileName) {
        String relativePath = iconPath + fileName;

        URL resource = getClass().getClassLoader().getResource(relativePath);
        if (resource != null) {
            return new ImageIcon(resource);
        }

        String[] candidates = {
                relativePath,
                "src/" + relativePath,
                "out/production/cake/" + relativePath,
                "../" + relativePath
        };

        for (String path : candidates) {
            File file = new File(path);
            if (file.exists()) {
                return new ImageIcon(file.getAbsolutePath());
            }
        }

        return null;
    }

    private JButton createMenuButton(ImageIcon icon, String text, String pageName) {
        JButton btn = new JButton(text, icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Vẽ background bo góc
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));

                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setMaximumSize(new Dimension(WIDTH - 30, 50));
        btn.setPreferredSize(new Dimension(WIDTH - 30, 50));
        btn.setBackground(new Color(0, 0, 0, 0)); // Trong suốt
        btn.setForeground(textColor);
        btn.setFont(menuFont);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(15); // Khoảng cách giữa icon và text
        btn.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);

        // Hiệu ứng hover & click
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (btn != activeButton) {
                    btn.setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (btn != activeButton) {
                    btn.setBackground(new Color(0, 0, 0, 0));
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // Bỏ active của button cũ
                if (activeButton != null) {
                    activeButton.setBackground(new Color(0, 0, 0, 0));
                    activeButton.setFont(menuFont);
                }
                // Set button mới là active
                activeButton = btn;
                btn.setBackground(activeColor);
                btn.setFont(menuFontBold);

                // Chuyển trang
                if (contentPanel != null) {
                    contentPanel.showPage(pageName);
                }
            }
        });

        return btn;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel();
        footer.setLayout(new BorderLayout());
        footer.setBackground(primaryDark);
        footer.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        footer.setPreferredSize(new Dimension(WIDTH, 80));

        // Panel chứa avatar và thông tin user
        JPanel userInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        userInfo.setBackground(primaryDark);

        // Avatar hình tròn với icon
        ImageIcon userIcon = loadIcon("user.png", 30, 30);
        JLabel avatar = new JLabel(userIcon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(primaryLight);
                g2.fillOval(0, 0, 40, 40);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        avatar.setPreferredSize(new Dimension(40, 40));
        avatar.setHorizontalAlignment(SwingConstants.CENTER);

        // Thông tin user
        JPanel userText = new JPanel();
        userText.setLayout(new BoxLayout(userText, BoxLayout.Y_AXIS));
        userText.setBackground(primaryDark);

        JLabel userName = new JLabel("Admin");
        userName.setFont(menuFontBold);
        userName.setForeground(textColor);

        JLabel userRole = new JLabel("Quản trị viên");
        userRole.setFont(smallFont);
        userRole.setForeground(primaryLight);

        userText.add(userName);
        userText.add(userRole);

        userInfo.add(avatar);
        userInfo.add(userText);

        // Nút đăng xuất với icon
        ImageIcon logoutIcon = loadIcon("logout.png", 20, 20);
        JButton logout = new JButton(logoutIcon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        logout.setPreferredSize(new Dimension(40, 40));
        logout.setBackground(new Color(239, 68, 68)); // Đỏ
        logout.setForeground(Color.WHITE);
        logout.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logout.setBorderPainted(false);
        logout.setFocusPainted(false);
        logout.setContentAreaFilled(false);
        logout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logout.setToolTipText("Đăng xuất");

        // Hover effect cho nút logout
        logout.addMouseListener(new MouseAdapter() {
            Color originalColor = logout.getBackground();

            @Override
            public void mouseEntered(MouseEvent e) {
                logout.setBackground(new Color(220, 38, 38));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                logout.setBackground(originalColor);
            }
        });

        footer.add(userInfo, BorderLayout.WEST);
        footer.add(logout, BorderLayout.EAST);

        return footer;
    }

}
