<<<<<<< HEAD

import Database.ConnectDatabase;

=======
>>>>>>> 17eb172b3db081e59ec37843caeabcbdb82b1b7c
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import BUS.KhuyenMaiBUS;
import DTO.KhuyenMaiDTO;

public class TichDiemPanel extends JPanel {
    private JTable tblKhachHang;
    private JTable tblKM;
    private JTextField txtDiem;
    private DefaultTableModel khachModel;
    private DefaultTableModel kmModel;
    private final Color mainColor = new Color(231, 74, 131);

    private KhuyenMaiBUS kmBUS = new KhuyenMaiBUS();

    public TichDiemPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

<<<<<<< HEAD
        JPanel pnlTop = new JPanel(new BorderLayout(10, 10));
        pnlTop.setOpaque(false);

        JLabel lblTitle = new JLabel("⭐ QUẢN LÝ TÍCH ĐIỂM KHÁCH HÀNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(mainColor);

        JPanel pnlDiem = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        pnlDiem.setOpaque(false);
        JLabel lbl = new JLabel("Điểm tối thiểu để đổi quà:");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtDiem = new JTextField("100", 8);
        txtDiem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        txtDiem.setForeground(mainColor);

        JButton btnReload = new JButton("Làm mới");
        btnReload.setBackground(new Color(59, 130, 246));
        btnReload.setForeground(Color.WHITE);
        btnReload.setFocusPainted(false);

        pnlDiem.add(lbl);
        pnlDiem.add(txtDiem);
        pnlDiem.add(btnReload);
=======
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
>>>>>>> 17eb172b3db081e59ec37843caeabcbdb82b1b7c

        pnlTop.add(lblTitle, BorderLayout.NORTH);
        pnlTop.add(pnlDiem, BorderLayout.SOUTH);

        khachModel = new DefaultTableModel(
                new String[] { "Mã KH", "Họ tên", "SĐT", "Điểm hiện tại", "Trạng thái" },
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblKhachHang = new JTable(khachModel);
        styleTable(tblKhachHang);

        kmModel = new DefaultTableModel(
                new String[] { "Mã KM", "Tên chương trình", "Loại", "Ưu đãi", "Hiệu lực" },
                0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblKM = new JTable(kmModel);
        styleTable(tblKM);

<<<<<<< HEAD
        JPanel pnlKhach = new JPanel(new BorderLayout());
        pnlKhach.setBackground(Color.WHITE);
        pnlKhach.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(mainColor),
                " Danh sách điểm khách hàng "));
        pnlKhach.add(new JScrollPane(tblKhachHang), BorderLayout.CENTER);

        JPanel pnlKm = new JPanel(new BorderLayout());
        pnlKm.setBackground(Color.WHITE);
        pnlKm.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(mainColor),
                " Chương trình khuyến mãi còn hiệu lực "));
        pnlKm.add(new JScrollPane(tblKM), BorderLayout.CENTER);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pnlKhach, pnlKm);
        split.setResizeWeight(0.58);
        split.setBorder(null);

        add(pnlTop, BorderLayout.NORTH);
        add(split, BorderLayout.CENTER);

        txtDiem.addActionListener(e -> loadKhachHangTichDiem());
        btnReload.addActionListener(e -> reloadData());

        reloadData();
    }

    private void reloadData() {
        loadKhachHangTichDiem();
        loadKhuyenMaiDangApDung();
    }

    private void loadKhachHangTichDiem() {
        khachModel.setRowCount(0);
        int nguongDoiQua = parseNguongDiem();

        String sql = "SELECT kh.MaKH, kh.Ho, kh.Ten, kh.SDT, IFNULL(td.TICHDIEM, 0) AS Diem "
                + "FROM khachhang kh "
                + "LEFT JOIN tichdiem td ON kh.MaKH = td.MaKH "
                + "ORDER BY Diem DESC, kh.MaKH";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int diem = rs.getInt("Diem");
                String trangThai = diem >= nguongDoiQua ? "Du diem doi qua" : "Chua du diem";

                khachModel.addRow(new Object[] {
                        rs.getInt("MaKH"),
                        (rs.getString("Ho") == null ? "" : rs.getString("Ho")) + " "
                                + (rs.getString("Ten") == null ? "" : rs.getString("Ten")),
                        rs.getString("SDT"),
                        diem,
                        trangThai
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Khong tai duoc du lieu tich diem: " + ex.getMessage(),
                    "Loi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadKhuyenMaiDangApDung() {
        kmModel.setRowCount(0);

        String sql = "SELECT MaKM, TenCTKM, LoaiKhuyenMai, PhanTramGiam, SoLuongMua, SoLuongTang, "
                + "NgayBatDau, NgayKetThuc "
                + "FROM chuongtrinhkhuyenmai "
                + "WHERE NOW() BETWEEN NgayBatDau AND NgayKetThuc "
                + "ORDER BY NgayKetThuc";

        try (Connection conn = ConnectDatabase.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String loai = rs.getString("LoaiKhuyenMai");
                int ptg = rs.getInt("PhanTramGiam");
                int slMua = rs.getInt("SoLuongMua");
                int slTang = rs.getInt("SoLuongTang");

                String uuDai;
                if (ptg > 0) {
                    uuDai = "Giam " + ptg + "%";
                } else if (slMua > 0 && slTang > 0) {
                    uuDai = "Mua " + slMua + " tang " + slTang;
                } else {
                    uuDai = "Theo chuong trinh";
                }

                String hieuLuc = rs.getTimestamp("NgayBatDau") + " -> " + rs.getTimestamp("NgayKetThuc");

                kmModel.addRow(new Object[] {
                        rs.getInt("MaKM"),
                        rs.getString("TenCTKM"),
                        loai,
                        uuDai,
                        hieuLuc
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Khong tai duoc danh sach khuyen mai: " + ex.getMessage(),
                    "Loi",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private int parseNguongDiem() {
        String text = txtDiem.getText().trim();
        try {
            int value = Integer.parseInt(text);
            return Math.max(0, value);
        } catch (Exception ex) {
            txtDiem.setText("100");
            return 100;
=======
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
>>>>>>> 17eb172b3db081e59ec37843caeabcbdb82b1b7c
        }
    }

    private void styleTable(JTable t) {
        t.setRowHeight(32);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.getTableHeader().setBackground(mainColor);
        t.getTableHeader().setForeground(Color.WHITE);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.setSelectionBackground(new Color(245, 183, 204));
    }
}