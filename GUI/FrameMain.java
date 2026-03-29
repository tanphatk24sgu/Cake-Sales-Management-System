package GUI;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.*;

public class FrameMain extends JFrame {
    private int WIDTH = 1000;
    private int HEIGHT = 1300;

    public FrameMain() {
        setTitle("Hệ thống quản lí của hàng bánh");
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // Tạo ContentPanel trước
        ContentPanel contentPanel = new ContentPanel();

        // Truyền ContentPanel vào SidebarPanel để kết nối
        SidebarPanel sidebar = new SidebarPanel(contentPanel);

        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FrameMain();
        });
    }
}