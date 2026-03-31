import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;

import BUS.KhuyenMaiBUS;
import DTO.KhuyenMaiDTO;

public class TichDiemPanel extends JPanel {
    private JTable tblKM;
    private JTextField txtDiem;
    java.awt.Color mainColor = new java.awt.Color(231, 74, 131);

    private KhuyenMaiBUS kmBUS = new KhuyenMaiBUS();

    public TichDiemPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Khu vực nhập điểm
        JPanel pnlDiem = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlDiem.setOpaque(false);

        JLabel lbl = new JLabel("⭐ Điểm tối thiểu để đổi quà: ");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));

        txtDiem = new JTextField("100", 10);
        txtDiem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtDiem.setForeground(mainColor);

        JButton btnXem = new JButton("🎁 Xem khuyến mãi");

        pnlDiem.add(lbl);
        pnlDiem.add(txtDiem);
        pnlDiem.add(btnXem);

        // Bảng KM
        String[] cols = { "Mã KM", "Tên Chương Trình", "Ưu đãi", "Hạn dùng" };
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        tblKM = new JTable(m);
        styleTable(tblKM);

        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(mainColor),
                " Chương trình KM có thể dùng "));

        pnlMain.add(new JScrollPane(tblKM), BorderLayout.CENTER);

        add(pnlDiem, BorderLayout.NORTH);
        add(pnlMain, BorderLayout.CENTER);

        // SỰ KIỆN NÚT
        btnXem.addActionListener(e -> {
            try {
                int diem = Integer.parseInt(txtDiem.getText());
                loadKhuyenMai(diem);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Nhập điểm hợp lệ!");
            }
        });

        // Load mặc định
        loadKhuyenMai(Integer.parseInt(txtDiem.getText()));
    }

    private void loadKhuyenMai(int diem) {
        DefaultTableModel model = (DefaultTableModel) tblKM.getModel();
        model.setRowCount(0);

        // 🔥 LẤY TẤT CẢ KM PHÙ HỢP
        java.util.List<KhuyenMaiDTO> list = kmBUS.getAllByDiem(diem);

        if (list != null && !list.isEmpty()) {
            for (KhuyenMaiDTO km : list) {
                model.addRow(new Object[] {
                        km.getMaKM(),
                        km.getTenKM(),
                        km.getPhanTramGiam() + "%",
                        ">= " + km.getDieuKien() + " điểm"
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, "Không có khuyến mãi phù hợp!");
        }
    }

    private void styleTable(JTable t) {
        t.setRowHeight(35);
        t.getTableHeader().setBackground(mainColor);
        t.getTableHeader().setForeground(Color.WHITE);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
    }
}