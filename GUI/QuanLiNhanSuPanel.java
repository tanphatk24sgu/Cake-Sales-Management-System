import javax.swing.*;
import javax.swing.table.*;

import BUS.NhanVienBUS;
import DTO.NhanVienDTO;

import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class QuanLiNhanSuPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;
    private JLabel lblTotal;
    private JButton btnAdd, btnEdit, btnDelete, btnRefresh;

    private final Color primaryColor = new Color(236, 72, 153);
    private final Color primaryLight = new Color(251, 207, 232);
    private final Color bgColor = new Color(249, 250, 251);
    private final Color cardColor = Color.WHITE;

    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
    private final Font normalFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font boldFont = new Font("Segoe UI", Font.BOLD, 14);

    private final NhanVienBUS bus = new NhanVienBUS();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public QuanLiNhanSuPanel() {
        setBackground(bgColor);
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        add(createHeader(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createToolbar(), BorderLayout.SOUTH);

        loadDataFromDB();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(20, 0));
        header.setBackground(bgColor);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setBackground(bgColor);

        JLabel icon = new JLabel();
        try {
            ImageIcon userIcon = new ImageIcon("img/icon/user.png");
            Image img = userIcon.getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
            icon.setIcon(new ImageIcon(img));
        } catch (Exception e) {
            icon.setText("👤");
        }

        JLabel title = new JLabel("  QUẢN LÍ NHÂN SỰ");
        title.setFont(titleFont);
        title.setForeground(primaryColor);

        titlePanel.add(icon);
        titlePanel.add(title);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setBackground(bgColor);

        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(normalFont);

        txtSearch = new JTextField(20);
        txtSearch.setPreferredSize(new Dimension(220, 38));
        txtSearch.setFont(normalFont);
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));

        JButton btnSearch = createStyledButton("Tìm", primaryColor, 80);
        btnSearch.addActionListener(e -> searchNhanVien());
        txtSearch.addActionListener(e -> searchNhanVien());

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);

        header.add(titlePanel, BorderLayout.WEST);
        header.add(searchPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(cardColor);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)));

        String[] columns = { "Mã NV", "Họ", "Tên", "Chức vụ", "Ngày sinh", "Lương cơ bản" };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(45);
        table.setFont(normalFont);
        table.setGridColor(new Color(243, 244, 246));
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(primaryLight);
        table.setSelectionForeground(Color.BLACK);

        JTableHeader header = table.getTableHeader();
        header.setFont(boldFont);
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 50));
        header.setBorder(BorderFactory.createEmptyBorder());

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setBackground(primaryColor);
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(boldFont);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        tablePanel.add(scrollPane, BorderLayout.CENTER);
        return tablePanel;
    }

    private JPanel createToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setBackground(bgColor);
        toolbar.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setBackground(bgColor);

        btnAdd = createStyledButton("+ Thêm mới", new Color(34, 197, 94), 130);
        btnEdit = createStyledButton("✎ Chỉnh sửa", new Color(59, 130, 246), 130);
        btnDelete = createStyledButton("✕ Xóa", new Color(239, 68, 68), 100);
        btnRefresh = createStyledButton("↻ Làm mới", new Color(107, 114, 128), 120);

        btnAdd.addActionListener(e -> showAddDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnDelete.addActionListener(e -> deleteSelected());
        btnRefresh.addActionListener(e -> refreshTable());

        leftPanel.add(btnAdd);
        leftPanel.add(btnEdit);
        leftPanel.add(btnDelete);
        leftPanel.add(btnRefresh);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setBackground(bgColor);

        lblTotal = new JLabel();
        lblTotal.setFont(normalFont);
        lblTotal.setForeground(new Color(107, 114, 128));
        updateTotalLabel();

        rightPanel.add(lblTotal);

        toolbar.add(leftPanel, BorderLayout.WEST);
        toolbar.add(rightPanel, BorderLayout.EAST);

        return toolbar;
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

        btn.setPreferredSize(new Dimension(width, 40));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(boldFont);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setOpaque(false);

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

    private void updateTotalLabel() {
        if (lblTotal != null) {
            lblTotal.setText("Tổng: " + tableModel.getRowCount() + " nhân viên");
        }
    }

    private void searchNhanVien() {
        String key = txtSearch.getText().trim();
        if (key.isEmpty()) {
            loadDataFromDB();
            return;
        }

        bus.docDSNV();
        tableModel.setRowCount(0);
        for (NhanVienDTO nv : bus.getDSNV()) {
            String tenDayDu = (nv.getHo() + " " + nv.getTen()).toLowerCase();
            boolean match = tenDayDu.contains(key.toLowerCase());
            if (!match) {
                try {
                    int ma = Integer.parseInt(key);
                    match = nv.getMaNV() == ma;
                } catch (NumberFormatException ignored) {
                }
            }
            if (match) {
                tableModel.addRow(toRow(nv));
            }
        }
        updateTotalLabel();
    }

    private void showAddDialog() {
        JDialog dialog = createNhanSuDialog("Thêm nhân viên mới", null, -1);
        dialog.setVisible(true);
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        NhanVienDTO nv = rowToNhanVien(selectedRow);
        if (nv == null) {
            JOptionPane.showMessageDialog(this, "Dữ liệu nhân viên không hợp lệ!", "Lỗi",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = createNhanSuDialog("Chỉnh sửa nhân viên", nv, selectedRow);
        dialog.setVisible(true);
    }

    private JDialog createNhanSuDialog(String title, NhanVienDTO data, int selectedRow) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), title, true);
        dialog.setSize(520, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);

        JTextField txtHo = new JTextField();
        JTextField txtTen = new JTextField();
        JTextField txtNgaySinh = new JTextField();
        JTextField txtChucVu = new JTextField();
        JTextField txtLuong = new JTextField();

        JTextField[] fields = { txtHo, txtTen, txtNgaySinh, txtChucVu, txtLuong };
        String[] labels = { "Họ:", "Tên:", "Ngày sinh (yyyy-MM-dd):", "Mã chức vụ:", "Lương cơ bản:" };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.4;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(normalFont);
            formPanel.add(lbl, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.6;
            fields[i].setPreferredSize(new Dimension(250, 35));
            fields[i].setFont(normalFont);
            fields[i].setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(209, 213, 219)),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)));
            formPanel.add(fields[i], gbc);
        }

        if (data != null) {
            txtHo.setText(data.getHo());
            txtTen.setText(data.getTen());
            txtNgaySinh.setText(data.getNgaySinh() != null ? dateFormat.format(data.getNgaySinh()) : "");
            txtChucVu.setText(String.valueOf(data.getChucVu()));
            txtLuong.setText(String.valueOf(data.getLuongCoBan()));
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(new Color(249, 250, 251));

        JButton btnSave = createStyledButton("💾 Lưu", new Color(34, 197, 94), 100);
        JButton btnCancel = createStyledButton("Hủy", new Color(107, 114, 128), 100);

        btnSave.addActionListener(e -> {
            try {
                String ho = txtHo.getText().trim();
                String ten = txtTen.getText().trim();
                if (ho.isEmpty() || ten.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Họ và tên không được để trống.", "Lỗi",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Date ngaySinh = dateFormat.parse(txtNgaySinh.getText().trim());
                int chucVu = Integer.parseInt(txtChucVu.getText().trim());
                double luong = Double.parseDouble(txtLuong.getText().trim());

                NhanVienDTO nv = new NhanVienDTO();
                nv.setHo(ho);
                nv.setTen(ten);
                nv.setNgaySinh(ngaySinh);
                nv.setChucVu(chucVu);
                nv.setLuongCoBan(luong);

                if (data == null) {
                    bus.them(nv);
                } else {
                    nv.setMaNV((int) tableModel.getValueAt(selectedRow, 0));
                    bus.sua(nv);
                }

                loadDataFromDB();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Lưu nhân viên thành công!", "Thông báo",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ: " + ex.getMessage(), "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        return dialog;
    }

    private void deleteSelected() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!", "Thông báo",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int maNV = (int) tableModel.getValueAt(selectedRow, 0);
        String tenNhanVien = tableModel.getValueAt(selectedRow, 2).toString();
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa \"" + tenNhanVien + "\"?",
                "Xác nhận xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            bus.xoa(maNV);
            loadDataFromDB();
            JOptionPane.showMessageDialog(this, "Xóa thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void refreshTable() {
        loadDataFromDB();
        JOptionPane.showMessageDialog(this, "Đã làm mới dữ liệu!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadDataFromDB() {
        bus.docDSNV();
        tableModel.setRowCount(0);

        ArrayList<NhanVienDTO> data = bus.getDSNV();
        if (data != null) {
            for (NhanVienDTO nv : data) {
                tableModel.addRow(toRow(nv));
            }
        }
        updateTotalLabel();
    }

    private Object[] toRow(NhanVienDTO nv) {
        return new Object[] {
                nv.getMaNV(),
                nv.getHo(),
                nv.getTen(),
                nv.getChucVu(),
                nv.getNgaySinh(),
                nv.getLuongCoBan()
        };
    }

    private NhanVienDTO rowToNhanVien(int row) {
        try {
            NhanVienDTO nv = new NhanVienDTO();
            nv.setMaNV((int) tableModel.getValueAt(row, 0));
            nv.setHo(String.valueOf(tableModel.getValueAt(row, 1)));
            nv.setTen(String.valueOf(tableModel.getValueAt(row, 2)));
            Object ngaySinhCell = tableModel.getValueAt(row, 4);
            if (ngaySinhCell instanceof Date) {
                nv.setNgaySinh((Date) ngaySinhCell);
            } else {
                nv.setNgaySinh(dateFormat.parse(String.valueOf(ngaySinhCell)));
            }
            nv.setChucVu(Integer.parseInt(String.valueOf(tableModel.getValueAt(row, 3))));
            nv.setLuongCoBan(Double.parseDouble(String.valueOf(tableModel.getValueAt(row, 5))));
            return nv;
        } catch (Exception e) {
            return null;
        }
    }
}
