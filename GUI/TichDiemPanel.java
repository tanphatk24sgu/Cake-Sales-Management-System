package GUI;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;

public class TichDiemPanel extends JPanel {
    private JTable tblKM;
    private JTextField txtDiem;
    java.awt.Color mainColor = new java.awt.Color(231, 74, 131);

    public TichDiemPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Khu vực cấu hình điểm
        JPanel pnlDiem = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlDiem.setOpaque(false);
        JLabel lbl = new JLabel("⭐ Điểm tối thiểu để đổi quà: ");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtDiem = new JTextField("100", 10);
        txtDiem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtDiem.setForeground(mainColor);
        pnlDiem.add(lbl);
        pnlDiem.add(txtDiem);

        // Bảng KM
        String[] cols = { "Mã KM", "Tên Chương Trình", "Ưu đãi", "Hạn dùng" };
        DefaultTableModel m = new DefaultTableModel(cols, 0);
        tblKM = new JTable(m);
        styleTable(tblKM);

        JPanel pnlMain = new JPanel(new BorderLayout());
        pnlMain.setBackground(Color.WHITE);
        pnlMain.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(mainColor),
                " Chương trình KM đang áp dụng "));
        pnlMain.add(new JScrollPane(tblKM), BorderLayout.CENTER);

        add(pnlDiem, BorderLayout.NORTH);
        add(pnlMain, BorderLayout.CENTER);
    }

    private void styleTable(JTable t) {
        t.setRowHeight(35);
        t.getTableHeader().setBackground(mainColor);
        t.getTableHeader().setForeground(Color.WHITE);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
    }
}