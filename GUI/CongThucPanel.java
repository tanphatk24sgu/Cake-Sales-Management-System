import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import BUS.CongThucBUS;
import BUS.BanhBus;
import DTO.CongThucDTO;
import DTO.BanhDTO;

public class CongThucPanel extends JPanel {
    private JList<String> listBanhUI; // Thay ComboBox bằng JList cho chuyên nghiệp
    private DefaultListModel<String> listModel;
    private JTextArea txtCachLam;
    private JLabel lblTenBanhDangChon;

    private CongThucBUS bus = new CongThucBUS();
    private BanhBus banhBUS = new BanhBus();
    private ArrayList<BanhDTO> listBanh;

    // Màu sắc chủ đạo từ logo của bạn
    Color primaryColor = new Color(231, 74, 131); // Màu hồng Sweet Children
    Color secondaryColor = new Color(248, 249, 250);

    public CongThucPanel() {
        setLayout(new BorderLayout(15, 0));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // --- LEFT PANEL: DANH SÁCH BÁNH ---
        JPanel pnlLeft = new JPanel(new BorderLayout(10, 10));
        pnlLeft.setPreferredSize(new Dimension(280, 0));
        pnlLeft.setBackground(Color.WHITE);
        pnlLeft.setBorder(new TitledBorder(new LineBorder(primaryColor), " 🎂 Danh sách bánh "));

        listModel = new DefaultListModel<>();
        listBanhUI = new JList<>(listModel);
        listBanhUI.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        listBanhUI.setSelectionBackground(primaryColor);
        listBanhUI.setSelectionForeground(Color.WHITE);
        listBanhUI.setFixedCellHeight(40); // Cho mỗi item cao ráo, dễ bấm

        JScrollPane scrollList = new JScrollPane(listBanhUI);
        scrollList.setBorder(null);
        pnlLeft.add(scrollList, BorderLayout.CENTER);

        // --- RIGHT PANEL: CHI TIẾT CÔNG THỨC ---
        JPanel pnlRight = new JPanel(new BorderLayout(15, 15));
        pnlRight.setBackground(Color.WHITE);

        // Header của bên phải
        JPanel pnlHeader = new JPanel(new BorderLayout());
        pnlHeader.setBackground(Color.WHITE);
        lblTenBanhDangChon = new JLabel("Chọn một loại bánh để xem công thức");
        lblTenBanhDangChon.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTenBanhDangChon.setForeground(primaryColor);
        pnlHeader.add(lblTenBanhDangChon, BorderLayout.WEST);
        pnlHeader.add(new JSeparator(), BorderLayout.SOUTH);

        txtCachLam = new JTextArea();
        txtCachLam.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtCachLam.setLineWrap(true);
        txtCachLam.setWrapStyleWord(true);
        txtCachLam.setMargin(new Insets(10, 10, 10, 10));

        JScrollPane scrollText = new JScrollPane(txtCachLam);
        scrollText.setBorder(new LineBorder(new Color(230, 230, 230)));

        JButton btnLuu = new JButton("💾 LƯU CÔNG THỨC");
        btnLuu.setBackground(new Color(40, 167, 69));
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLuu.setPreferredSize(new Dimension(0, 45));
        btnLuu.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pnlRight.add(pnlHeader, BorderLayout.NORTH);
        pnlRight.add(scrollText, BorderLayout.CENTER);
        pnlRight.add(btnLuu, BorderLayout.SOUTH);

        // Add 2 panel chính
        add(pnlLeft, BorderLayout.WEST);
        add(pnlRight, BorderLayout.CENTER);

        // --- EVENTS ---

        // Sự kiện khi click chọn bánh trong List
        listBanhUI.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showCongThuc();
            }
        });

        // Sự kiện lưu
        btnLuu.addActionListener(e -> {
            int index = listBanhUI.getSelectedIndex();
            if (index < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn bánh trước!");
                return;
            }

            CongThucDTO ct = new CongThucDTO();
            ct.setMaBanh(listBanh.get(index).getMaBanh());
            ct.setMaDVT(1);
            ct.setCachLam(txtCachLam.getText());

            if (bus.save(ct)) {
                JOptionPane.showMessageDialog(this, "Đã lưu công thức cho " + listBanh.get(index).getTenBanh());
            }
        });

        loadBanh();
    }

    private void loadBanh() {
        banhBUS.loadData();
        listBanh = banhBUS.getList();
        listModel.clear();
        for (BanhDTO b : listBanh) {
            listModel.addElement(b.getTenBanh());
        }
        if (!listBanh.isEmpty()) {
            listBanhUI.setSelectedIndex(0);
        }
    }

    private void showCongThuc() {
        int index = listBanhUI.getSelectedIndex();
        if (index < 0)
            return;

        BanhDTO banhChon = listBanh.get(index);
        lblTenBanhDangChon.setText("📖 Công thức: " + banhChon.getTenBanh());

        CongThucDTO ct = bus.getByMaBanh(banhChon.getMaBanh());
        if (ct != null) {
            txtCachLam.setText(ct.getCachLam());
        } else {
            txtCachLam.setText(""); // Nếu chưa có công thức thì để trống để nhập mới
            txtCachLam.setToolTipText("Bánh này chưa có công thức, hãy nhập vào đây.");
        }
    }
}