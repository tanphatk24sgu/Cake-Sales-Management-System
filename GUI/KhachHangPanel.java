import BUS.KhachHangBUS;
import DTO.KhachHangDTO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class KhachHangPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtHo;
    private JTextField txtTen;
    private JTextField txtDiaChi;
    private JTextField txtSDT;
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;

    private final Color mainColor = new Color(231, 74, 131);
    private final KhachHangBUS bus = new KhachHangBUS();

    public KhachHangPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("👥 QUẢN LÝ KHÁCH HÀNG");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(mainColor);
        add(lblTitle, BorderLayout.NORTH);

        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(mainColor),
                " Thông tin khách hàng ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                mainColor));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlInput.add(new JLabel("Họ:"), gbc);

        gbc.gridx = 1;
        txtHo = new JTextField(20);
        pnlInput.add(txtHo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        pnlInput.add(new JLabel("Tên:"), gbc);

        gbc.gridx = 1;
        txtTen = new JTextField(20);
        pnlInput.add(txtTen, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        pnlInput.add(new JLabel("Địa chỉ:"), gbc);

        gbc.gridx = 1;
        txtDiaChi = new JTextField(20);
        pnlInput.add(txtDiaChi, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        pnlInput.add(new JLabel("SĐT:"), gbc);

        gbc.gridx = 1;
        txtSDT = new JTextField(20);
        pnlInput.add(txtSDT, gbc);

        JPanel pnlBtns = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        pnlBtns.setBackground(Color.WHITE);

        btnThem = createBtn("Thêm mới", new Color(40, 167, 69));
        btnSua = createBtn("Cập nhật", new Color(0, 123, 255));
        btnXoa = createBtn("Xóa", new Color(220, 53, 69));

        pnlBtns.add(btnThem);
        pnlBtns.add(btnSua);
        pnlBtns.add(btnXoa);

        JPanel left = new JPanel(new BorderLayout());
        left.setOpaque(false);
        left.add(pnlInput, BorderLayout.NORTH);
        left.add(pnlBtns, BorderLayout.CENTER);

        model = new DefaultTableModel(new String[] { "Mã KH", "Họ", "Tên", "Địa chỉ", "SĐT" }, 0);
        table = new JTable(model);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);

        add(left, BorderLayout.WEST);
        add(scroll, BorderLayout.CENTER);

        addEvents();
        loadData();
    }

    private void addEvents() {
        btnThem.addActionListener(e -> {
            KhachHangDTO kh = buildFromForm();
            if (kh == null) {
                return;
            }

            if (bus.add(kh)) {
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thất bại.");
            }
        });

        btnSua.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa.");
                return;
            }

            KhachHangDTO kh = buildFromForm();
            if (kh == null) {
                return;
            }
            kh.setMaKH((int) model.getValueAt(row, 0));

            if (bus.update(kh)) {
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thất bại.");
            }
        });

        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa.");
                return;
            }

            int maKH = (int) model.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Xóa khách hàng này?", "Xác nhận",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (bus.delete(maKH)) {
                    loadData();
                    clearForm();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa khách hàng thất bại.");
                }
            }
        });

        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtHo.setText(String.valueOf(model.getValueAt(row, 1)));
                txtTen.setText(String.valueOf(model.getValueAt(row, 2)));
                txtDiaChi.setText(String.valueOf(model.getValueAt(row, 3)));
                txtSDT.setText(String.valueOf(model.getValueAt(row, 4)));
            }
        });
    }

    private KhachHangDTO buildFromForm() {
        String ho = txtHo.getText().trim();
        String ten = txtTen.getText().trim();
        String diaChi = txtDiaChi.getText().trim();
        String sdt = txtSDT.getText().trim();

        if (ho.isEmpty() || ten.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Họ, tên và SĐT là bắt buộc.");
            return null;
        }

        KhachHangDTO kh = new KhachHangDTO();
        kh.setHo(ho);
        kh.setTen(ten);
        kh.setDiaChi(diaChi);
        kh.setSdt(sdt);
        return kh;
    }

    private void loadData() {
        ArrayList<KhachHangDTO> list = bus.getList();
        model.setRowCount(0);

        for (KhachHangDTO kh : list) {
            model.addRow(new Object[] {
                    kh.getMaKH(),
                    kh.getHo(),
                    kh.getTen(),
                    kh.getDiaChi(),
                    kh.getSdt()
            });
        }
    }

    private void clearForm() {
        txtHo.setText("");
        txtTen.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
    }

    private JButton createBtn(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
        return btn;
    }

    private void styleTable(JTable t) {
        t.setRowHeight(35);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        t.getTableHeader().setBackground(mainColor);
        t.getTableHeader().setForeground(Color.WHITE);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        t.setSelectionBackground(new Color(245, 183, 204));
    }
}
