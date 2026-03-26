package GUI;

import BUS.NhaCungCapBUS;
import DTO.NhaCungCapDTO;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;

public class NhaCungCapPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private JTextField txtTen, txtMaSoThue;
    private JButton btnThem, btnSua, btnXoa;

    private Color mainColor = new Color(231, 74, 131);
    private NhaCungCapBUS bus = new NhaCungCapBUS();

    public NhaCungCapPanel() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // ===== TITLE =====
        JLabel lblTitle = new JLabel("📦 QUẢN LÝ NHÀ CUNG CẤP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(mainColor);
        add(lblTitle, BorderLayout.NORTH);

        // ===== INPUT =====
        JPanel pnlInput = new JPanel(new GridBagLayout());
        pnlInput.setBackground(Color.WHITE);
        pnlInput.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(mainColor),
                " Thông tin chi tiết ",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                mainColor));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        pnlInput.add(new JLabel("Tên nhà cung cấp:"), gbc);

        gbc.gridx = 1;
        txtTen = new JTextField(20);
        pnlInput.add(txtTen, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        pnlInput.add(new JLabel("Mã số thuế:"), gbc);

        gbc.gridx = 1;
        txtMaSoThue = new JTextField(20);
        pnlInput.add(txtMaSoThue, gbc);

        // ===== BUTTON =====
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

        // ===== TABLE =====
        model = new DefaultTableModel(new String[] {
                "Mã NCC", "Tên Nhà Cung Cấp", "Mã Số Thuế"
        }, 0);

        table = new JTable(model);
        styleTable(table);

        JScrollPane scroll = new JScrollPane(table);

        add(left, BorderLayout.WEST);
        add(scroll, BorderLayout.CENTER);

        // ===== EVENTS =====
        addEvents();

        // LOAD DATA
        loadData();
    }

    private void addEvents() {

        // ===== THÊM =====
        btnThem.addActionListener(e -> {
            if (txtTen.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nhập tên NCC!");
                return;
            }

            NhaCungCapDTO ncc = new NhaCungCapDTO();
            ncc.setTenNCC(txtTen.getText());
            ncc.setMaSoThue(txtMaSoThue.getText());

            if (bus.add(ncc)) {
                loadData(); // reload DB
                clearForm();
            }
        });

        // ===== XÓA =====
        btnXoa.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int ma = (int) model.getValueAt(row, 0);

                int confirm = JOptionPane.showConfirmDialog(this, "Xóa?");
                if (confirm == 0) {
                    bus.delete(ma);
                    loadData();
                }
            }
        });

        // ===== SỬA =====
        btnSua.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {

                NhaCungCapDTO ncc = new NhaCungCapDTO();
                ncc.setMaNCC((int) model.getValueAt(row, 0));
                ncc.setTenNCC(txtTen.getText());
                ncc.setMaSoThue(txtMaSoThue.getText());

                bus.update(ncc);
                loadData();
            }
        });

        // ===== CLICK TABLE =====
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                txtTen.setText(model.getValueAt(row, 1).toString());
                txtMaSoThue.setText(model.getValueAt(row, 2).toString());
            }
        });
    }

    private void loadData() {
        ArrayList<NhaCungCapDTO> list = bus.getList();

        model.setRowCount(0);

        for (NhaCungCapDTO ncc : list) {
            model.addRow(new Object[] {
                    ncc.getMaNCC(),
                    ncc.getTenNCC(),
                    ncc.getMaSoThue()
            });
        }
    }

    private void clearForm() {
        txtTen.setText("");
        txtMaSoThue.setText("");
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