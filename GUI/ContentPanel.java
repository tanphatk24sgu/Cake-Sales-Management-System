import java.awt.*;
import javax.swing.*;

// Panel nội dung trung tâm, quản lý chuyển trang bằng CardLayout
public class ContentPanel extends JPanel {
    private CardLayout cardLayout;
    private QuanLiBanHangPanel quanLiBanHangPanel;

    // Tên các trang
    public static final String TRANG_CHU = "home";
    public static final String BAN_HANG = "sale";
    public static final String KHUYEN_MAI = "promotion";
    public static final String QUAN_LI_BANH = "cake";
    public static final String NHAN_SU = "staff";
    public static final String HOA_DON = "invoice";
    public static final String THONG_KE = "stats";
    public static final String TICH_DIEM = "tich_diem";
    public static final String PHIEU_NHAP = "phieu_nhap";
    public static final String NHA_CUNG_CAP = "ncc";
    public static final String KHACH_HANG = "customer";
    public static final String CAI_DAT = "settings";

    public ContentPanel() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        setBackground(new Color(249, 250, 251));

        // Thêm các panel
        add(createTrangChuPanel(), TRANG_CHU);
        quanLiBanHangPanel = new QuanLiBanHangPanel();
        add(quanLiBanHangPanel, BAN_HANG);
        add(new QuanLiKhuyenMaiPanel(), KHUYEN_MAI);
        add(new QuanLiBanhPanel(), QUAN_LI_BANH);
        add(new QuanLiNhanSuPanel(), NHAN_SU);
        add(new QuanLiHoaDonPanel(), HOA_DON);
        add(new ThongKePanel(), THONG_KE);
        add(new TichDiemPanel(), TICH_DIEM);
        add(new QuanLiPhieuNhapPanel(), PHIEU_NHAP);
        add(new NhaCungCapPanel(), NHA_CUNG_CAP);
        add(new KhachHangPanel(), KHACH_HANG);
        add(createPlaceholderPanel("⚙️ CÀI ĐẶT", "Chức năng đang phát triển..."), CAI_DAT);

        // Hiển thị trang chủ mặc định
        showPage(TRANG_CHU);
    }

    public void showPage(String page) {
        if (BAN_HANG.equals(page) && quanLiBanHangPanel != null) {
            quanLiBanHangPanel.refreshDataFromDatabase();
        }
        cardLayout.show(this, page);
    }

    // Trang chủ với Dashboard
    private JPanel createTrangChuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(249, 250, 251));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(new Color(249, 250, 251));

        JLabel lblWelcome = new JLabel("Chào mừng đến với Sweet Children! 🎂");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblWelcome.setForeground(new Color(236, 72, 153));

        JLabel lblSubtitle = new JLabel("Hệ thống quản lí cửa hàng bánh");
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSubtitle.setForeground(new Color(107, 114, 128));

        headerPanel.add(lblWelcome);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(lblSubtitle);

        JPanel cardsPanel = new JPanel(new GridLayout(2, 3, 20, 20));
        cardsPanel.setBackground(new Color(249, 250, 251));
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        cardsPanel.add(createDashboardCard("🛒", "Đơn hàng hôm nay", "25", new Color(59, 130, 246)));
        cardsPanel.add(createDashboardCard("💰", "Doanh thu hôm nay", "5,250,000 VNĐ", new Color(34, 197, 94)));
        cardsPanel.add(createDashboardCard("🎂", "Sản phẩm", "30", new Color(236, 72, 153)));
        cardsPanel.add(createDashboardCard("👥", "Khách hàng", "50", new Color(168, 85, 247)));
        cardsPanel.add(createDashboardCard("👨‍💼", "Nhân viên", "30", new Color(249, 115, 22)));
        cardsPanel.add(createDashboardCard("📦", "Tồn kho thấp", "5", new Color(239, 68, 68)));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(cardsPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDashboardCard(String icon, String title, String value, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235)),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)));

        JLabel lblIcon = new JLabel(icon);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        lblIcon.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTitle.setForeground(new Color(107, 114, 128));
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValue.setForeground(color);
        lblValue.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lblIcon);
        card.add(Box.createVerticalStrut(10));
        card.add(lblTitle);
        card.add(Box.createVerticalStrut(5));
        card.add(lblValue);

        return card;
    }

    private JPanel createPlaceholderPanel(String title, String message) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(249, 250, 251));

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(new Color(249, 250, 251));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(new Color(236, 72, 153));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblMessage = new JLabel(message);
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblMessage.setForeground(new Color(107, 114, 128));
        lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

        content.add(lblTitle);
        content.add(Box.createVerticalStrut(15));
        content.add(lblMessage);

        panel.add(content);

        return panel;
    }
}