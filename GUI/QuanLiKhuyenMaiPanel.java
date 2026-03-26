import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import BUS.BanhBus;
import BUS.ChuongTrinhKhuyenMaiBUS;
import DTO.BanhDTO;
import DTO.ChuongTrinhKhuyenMaiDTO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

// Panel quản lý thông tin chương trình khuyến mãi
public class QuanLiKhuyenMaiPanel extends JPanel {
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final JTextField txtMaKM;
    private final JTextField txtTenCT;
    private final JComboBox<String> cboLoaiKM;
    private final JSpinner spnPhanTram;
    private final JSpinner spnDieuKien;
    private final JSpinner spnSoLuongMua;
    private final JSpinner spnSoLuongTang;
    private final JComboBox<ItemBanh> cboBanhMua;
    private final JComboBox<ItemBanh> cboBanhTang;
    private final DatePickerField dtNgayBatDau;
    private final DatePickerField dtNgayKetThuc;
    private final JPanel pnLoaiKhuyenMai;
    private final CardLayout cardLoaiKhuyenMai;
    private final JButton btnThem;
    private final JButton btnSua;
    private final JButton btnXoa;
    private final JButton btnLamMoi;
    private final JButton btnTaiLaiBanh;

    private final Color primaryColor = new Color(236, 72, 153);
    private final Color primaryLight = new Color(251, 207, 232);
    private final Color bgColor = new Color(249, 250, 251);
    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
    private final Font normalFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font boldFont = new Font("Segoe UI", Font.BOLD, 14);

    private final ArrayList<KhuyenMaiVM> dsKhuyenMai = new ArrayList<>();
    private final ArrayList<ItemBanh> dsBanh = new ArrayList<>();
    private final ChuongTrinhKhuyenMaiBUS chuongTrinhKhuyenMaiBus = new ChuongTrinhKhuyenMaiBUS();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final DecimalFormat moneyFormat = new DecimalFormat("#,###");

    public QuanLiKhuyenMaiPanel() {
        setBackground(bgColor);
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        add(createHeader(), BorderLayout.NORTH);

        txtMaKM = new JTextField();
        txtTenCT = new JTextField();
        cboLoaiKM = new JComboBox<>(new String[]{"Giảm phần trăm", "Mua X tặng Y"});
        spnPhanTram = new JSpinner(new SpinnerNumberModel(10, 1, 100, 1));
        spnDieuKien = new JSpinner(new SpinnerNumberModel(100000, 0, 999999999, 10000));
        spnSoLuongMua = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        spnSoLuongTang = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        cboBanhMua = new JComboBox<>();
        cboBanhTang = new JComboBox<>();
        dtNgayBatDau = new DatePickerField();
        dtNgayKetThuc = new DatePickerField();
        cardLoaiKhuyenMai = new CardLayout();
        pnLoaiKhuyenMai = new JPanel(cardLoaiKhuyenMai);
        btnThem = createStyledButton("+ Thêm", new Color(34, 197, 94), 110);
        btnSua = createStyledButton("✎ Sửa", new Color(59, 130, 246), 110);
        btnXoa = createStyledButton("✕ Xóa", new Color(239, 68, 68), 100);
        btnLamMoi = createStyledButton("↻ Làm mới", new Color(107, 114, 128), 120);
        btnTaiLaiBanh = createStyledButton("↻ Tải lại bánh", new Color(249, 115, 22), 140);
        tableModel = buildTableModel();
        table = buildTable();

        initMainUI();
        loadDanhSachBanh();
        bindDanhSachBanhToCombobox();
        bindEvents();
        loadDataFromDatabase();
        renderTable();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        header.setBackground(bgColor);

        JLabel title = new JLabel("🏷 QUẢN LÍ MÃ KHUYẾN MÃI");
        title.setFont(titleFont);
        title.setForeground(primaryColor);
        header.add(title);
        return header;
    }

    private JPanel createMainContent() {
        JPanel main = new JPanel(new BorderLayout(0, 10));
        main.setBackground(bgColor);
        return main;
    }

    private void initMainUI() {
        JPanel content = createMainContent();
        JPanel formPanel = createFormPanel();
        JPanel tablePanel = createTablePanel();

        JPanel formWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        formWrapper.setBackground(bgColor);
        formPanel.setPreferredSize(new Dimension(1000, formPanel.getPreferredSize().height));
        formWrapper.add(formPanel);

        content.add(tablePanel, BorderLayout.CENTER);
        content.add(formWrapper, BorderLayout.SOUTH);
        add(content, BorderLayout.CENTER);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
            BorderFactory.createEmptyBorder(16, 18, 14, 18)
        ));

        txtMaKM.setFont(normalFont);
        txtMaKM.setEditable(false);
        txtTenCT.setFont(normalFont);
        cboLoaiKM.setFont(normalFont);
        spnPhanTram.setFont(normalFont);
        spnDieuKien.setFont(normalFont);
        spnSoLuongMua.setFont(normalFont);
        spnSoLuongTang.setFont(normalFont);
        cboBanhMua.setFont(normalFont);
        cboBanhTang.setFont(normalFont);
        dtNgayBatDau.setFont(normalFont);
        dtNgayKetThuc.setFont(normalFont);
        setupInputComponentSizes();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        addFormRow(panel, gbc, 0, "Mã khuyến mãi:", txtMaKM);
        addFormRow(panel, gbc, 1, "Tên chương trình:", txtTenCT);
        addFormRow(panel, gbc, 2, "Loại khuyến mãi:", cboLoaiKM);
        addFormRow(panel, gbc, 3, "", createLoaiKhuyenMaiPanel());
        addFormRow(panel, gbc, 4, "Ngày bắt đầu:", dtNgayBatDau);
        addFormRow(panel, gbc, 5, "Ngày kết thúc:", dtNgayKetThuc);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.add(btnThem);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);
        btnPanel.add(btnLamMoi);
        btnPanel.add(btnTaiLaiBanh);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 5, 0, 5);
        panel.add(btnPanel, gbc);

        return panel;
    }

    private void setupInputComponentSizes() {
        Dimension inputSize = new Dimension(280, 32);
        txtMaKM.setPreferredSize(inputSize);
        txtTenCT.setPreferredSize(inputSize);
        cboLoaiKM.setPreferredSize(inputSize);
        spnPhanTram.setPreferredSize(inputSize);
        spnDieuKien.setPreferredSize(inputSize);
        spnSoLuongMua.setPreferredSize(inputSize);
        spnSoLuongTang.setPreferredSize(inputSize);
        cboBanhMua.setPreferredSize(inputSize);
        cboBanhTang.setPreferredSize(inputSize);
    }

    private JPanel createLoaiKhuyenMaiPanel() {
        pnLoaiKhuyenMai.setOpaque(false);
        pnLoaiKhuyenMai.add(createPanelGiamPhanTram(), "PERCENT");
        pnLoaiKhuyenMai.add(createPanelMuaTang(), "BUY_GET");
        cardLoaiKhuyenMai.show(pnLoaiKhuyenMai, "PERCENT");
        return pnLoaiKhuyenMai;
    }

    private JPanel createPanelGiamPhanTram() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 8, 6));
        panel.setOpaque(false);
        panel.add(new JLabel("Phần trăm giảm (%):"));
        panel.add(spnPhanTram);
        panel.add(new JLabel("Điều kiện hóa đơn (>=):"));
        panel.add(spnDieuKien);
        return panel;
    }

    private JPanel createPanelMuaTang() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 8, 6));
        panel.setOpaque(false);
        panel.add(new JLabel("Bánh mua:"));
        panel.add(cboBanhMua);
        panel.add(new JLabel("Số lượng mua:"));
        panel.add(spnSoLuongMua);
        panel.add(new JLabel("Bánh tặng:"));
        panel.add(cboBanhTang);
        panel.add(new JLabel("Số lượng tặng:"));
        panel.add(spnSoLuongTang);
        return panel;
    }

    private void loadDanhSachBanh() {
        dsBanh.clear();
        try {
            BanhBus banhBus = new BanhBus();
            for (BanhDTO banhDTO : banhBus.getList()) {
                dsBanh.add(new ItemBanh(banhDTO.getMaBanh(), banhDTO.getTenBanh()));
            }
        } catch (Exception e) {
            // Giữ rỗng và xử lý fallback bên dưới
        }

        if (dsBanh.isEmpty()) {
            dsBanh.add(new ItemBanh(1, "Bánh kem socola"));
            dsBanh.add(new ItemBanh(2, "Bánh mì bơ tỏi"));
            dsBanh.add(new ItemBanh(3, "Bánh su kem"));
            dsBanh.add(new ItemBanh(4, "Bánh croissant"));
            dsBanh.add(new ItemBanh(5, "Bánh phô mai"));
            dsBanh.add(new ItemBanh(6, "Bánh cupcake"));
            dsBanh.add(new ItemBanh(7, "Bánh tiramisu"));
            dsBanh.add(new ItemBanh(8, "Bánh tart trứng"));
            dsBanh.add(new ItemBanh(9, "Bánh mousse"));
            dsBanh.add(new ItemBanh(10, "Bánh bông lan"));
        }
    }

    private void bindDanhSachBanhToCombobox() {
        DefaultComboBoxModel<ItemBanh> modelBanhMua = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<ItemBanh> modelBanhTang = new DefaultComboBoxModel<>();
        for (ItemBanh itemBanh : dsBanh) {
            modelBanhMua.addElement(itemBanh);
            modelBanhTang.addElement(itemBanh);
        }
        cboBanhMua.setModel(modelBanhMua);
        cboBanhTang.setModel(modelBanhTang);
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent comp) {
        gbc.gridy = row;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0.30;
        JLabel lbl = new JLabel(label.isEmpty() ? " " : label);
        lbl.setFont(normalFont);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.70;
        panel.add(comp, gbc);
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(229, 231, 235), 1, true),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private DefaultTableModel buildTableModel() {
        return new DefaultTableModel(
            new String[]{"Mã KM", "Chương trình", "Loại", "Nội dung", "Ngày bắt đầu", "Ngày kết thúc", "Tình trạng"},
            0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    private JTable buildTable() {
        JTable tbl = new JTable(tableModel);
        tbl.setRowHeight(42);
        tbl.setFont(normalFont);
        tbl.setGridColor(new Color(243, 244, 246));
        tbl.setShowVerticalLines(false);
        tbl.setSelectionBackground(primaryLight);
        tbl.setSelectionForeground(Color.BLACK);

        JTableHeader header = tbl.getTableHeader();
        header.setFont(boldFont);
        header.setBackground(primaryColor);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 45));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setBackground(primaryColor);
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(boldFont);
        for (int i = 0; i < tbl.getColumnCount(); i++) {
            tbl.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tbl.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tbl.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
        tbl.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
        tbl.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

        tbl.getColumnModel().getColumn(0).setPreferredWidth(65);
        tbl.getColumnModel().getColumn(1).setPreferredWidth(220);
        tbl.getColumnModel().getColumn(2).setPreferredWidth(120);
        tbl.getColumnModel().getColumn(3).setPreferredWidth(170);
        tbl.getColumnModel().getColumn(4).setPreferredWidth(110);
        tbl.getColumnModel().getColumn(5).setPreferredWidth(110);
        tbl.getColumnModel().getColumn(6).setPreferredWidth(100);

        return tbl;
    }

    private void bindEvents() {
        btnThem.addActionListener(e -> themKhuyenMai());
        btnSua.addActionListener(e -> suaKhuyenMai());
        btnXoa.addActionListener(e -> xoaKhuyenMai());
        btnLamMoi.addActionListener(e -> lamMoiForm());
        btnTaiLaiBanh.addActionListener(e -> taiLaiDanhSachBanh());
        cboLoaiKM.addActionListener(e -> updateLoaiKhuyenMaiUI());

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    fillFormFromRow(row);
                }
            }
        });
    }

    private void taiLaiDanhSachBanh() {
        ItemBanh banhMuaCu = (ItemBanh) cboBanhMua.getSelectedItem();
        ItemBanh banhTangCu = (ItemBanh) cboBanhTang.getSelectedItem();
        int maBanhMuaCu = banhMuaCu != null ? banhMuaCu.maBanh : -1;
        int maBanhTangCu = banhTangCu != null ? banhTangCu.maBanh : -1;

        loadDanhSachBanh();
        bindDanhSachBanhToCombobox();
        selectBanhByMa(cboBanhMua, maBanhMuaCu);
        selectBanhByMa(cboBanhTang, maBanhTangCu);

        JOptionPane.showMessageDialog(this, "Đã tải lại danh sách bánh.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateLoaiKhuyenMaiUI() {
        if (cboLoaiKM.getSelectedIndex() == 0) {
            cardLoaiKhuyenMai.show(pnLoaiKhuyenMai, "PERCENT");
        } else {
            cardLoaiKhuyenMai.show(pnLoaiKhuyenMai, "BUY_GET");
        }
    }

    private void themKhuyenMai() {
        String err = validateForm();
        if (err != null) {
            JOptionPane.showMessageDialog(this, err, "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ChuongTrinhKhuyenMaiDTO dto = readFormDTO();
        int newId = chuongTrinhKhuyenMaiBus.add(dto);
        if (newId <= 0) {
            JOptionPane.showMessageDialog(this,
                    "Không lưu được xuống CSDL. Kiểm tra MySQL và bảng CHUONGTRINHKHUYENMAI (đủ cột như trong java__cake.sql — tạo DB bằng cách import file đó một lần).",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        reloadDsFromBus();
        renderTable();
        selectByMa(newId);
        JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void suaKhuyenMai() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn mã khuyến mãi cần sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String err = validateForm();
        if (err != null) {
            JOptionPane.showMessageDialog(this, err, "Lỗi dữ liệu", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int maKM = Integer.parseInt(txtMaKM.getText().trim());
        ChuongTrinhKhuyenMaiDTO dto = readFormDTO();
        dto.setMaKM(maKM);
        if (!chuongTrinhKhuyenMaiBus.update(dto)) {
            JOptionPane.showMessageDialog(this, "Không cập nhật được CSDL.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        reloadDsFromBus();
        renderTable();
        selectByMa(maKM);
        JOptionPane.showMessageDialog(this, "Cập nhật khuyến mãi thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void xoaKhuyenMai() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn mã khuyến mãi cần xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        KhuyenMaiVM km = dsKhuyenMai.get(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc muốn xóa chương trình \"" + km.tenCT + "\"?",
            "Xác nhận xóa",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        if (!chuongTrinhKhuyenMaiBus.delete(km.maKM)) {
            JOptionPane.showMessageDialog(this, "Không xóa được trên CSDL.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        reloadDsFromBus();
        renderTable();
        lamMoiForm();
        JOptionPane.showMessageDialog(this, "Đã xóa chương trình khuyến mãi.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void fillFormFromRow(int row) {
        if (row < 0 || row >= dsKhuyenMai.size()) return;
        KhuyenMaiVM km = dsKhuyenMai.get(row);
        txtMaKM.setText(String.valueOf(km.maKM));
        txtTenCT.setText(km.tenCT);
        cboLoaiKM.setSelectedItem(km.loaiKhuyenMai);
        spnPhanTram.setValue(km.phanTramGiam);
        spnDieuKien.setValue((int) km.dieuKienToiThieu);
        spnSoLuongMua.setValue(km.soLuongMua);
        spnSoLuongTang.setValue(km.soLuongTang);
        selectBanhByMa(cboBanhMua, km.maBanhMua);
        selectBanhByMa(cboBanhTang, km.maBanhTang);
        updateLoaiKhuyenMaiUI();
        dtNgayBatDau.setDate(km.ngayBatDau);
        dtNgayKetThuc.setDate(km.ngayKetThuc);
    }

    private void lamMoiForm() {
        table.clearSelection();
        txtMaKM.setText("");
        txtTenCT.setText("");
        cboLoaiKM.setSelectedIndex(0);
        spnPhanTram.setValue(10);
        spnDieuKien.setValue(100000);
        spnSoLuongMua.setValue(1);
        spnSoLuongTang.setValue(1);
        if (cboBanhMua.getItemCount() > 0) cboBanhMua.setSelectedIndex(0);
        if (cboBanhTang.getItemCount() > 0) cboBanhTang.setSelectedIndex(0);
        updateLoaiKhuyenMaiUI();
        dtNgayBatDau.setDate(new Date());
        dtNgayKetThuc.setDate(new Date());
    }

    private String validateForm() {
        if (txtTenCT.getText().trim().isEmpty()) {
            return "Tên chương trình không được để trống.";
        }

        String loaiKM = cboLoaiKM.getSelectedItem().toString();
        if ("Mua X tặng Y".equals(loaiKM)) {
            int soMua = (Integer) spnSoLuongMua.getValue();
            int soTang = (Integer) spnSoLuongTang.getValue();
            if (soMua <= 0 || soTang <= 0) {
                return "Số lượng mua/tặng phải lớn hơn 0.";
            }
            if (cboBanhMua.getSelectedItem() == null || cboBanhTang.getSelectedItem() == null) {
                return "Vui lòng chọn bánh mua và bánh tặng.";
            }
        }

        Date ngayBD = dtNgayBatDau.getDate();
        Date ngayKT = dtNgayKetThuc.getDate();
        if (ngayKT.before(ngayBD)) {
            return "Ngày kết thúc không hợp lệ!";
        }
        return null;
    }

    private ChuongTrinhKhuyenMaiDTO readFormDTO() {
        ChuongTrinhKhuyenMaiDTO d = new ChuongTrinhKhuyenMaiDTO();
        d.setTenCTKM(txtTenCT.getText().trim());
        d.setLoaiKhuyenMai(cboLoaiKM.getSelectedItem().toString());
        d.setPhanTramGiam((Integer) spnPhanTram.getValue());
        d.setDieuKienToiThieu(((Number) spnDieuKien.getValue()).doubleValue());
        d.setSoLuongMua((Integer) spnSoLuongMua.getValue());
        d.setSoLuongTang((Integer) spnSoLuongTang.getValue());
        ItemBanh banhMua = (ItemBanh) cboBanhMua.getSelectedItem();
        ItemBanh banhTang = (ItemBanh) cboBanhTang.getSelectedItem();
        d.setMaBanhMua(banhMua != null ? banhMua.maBanh : 0);
        d.setTenBanhMua(banhMua != null ? banhMua.tenBanh : "");
        d.setMaBanhTang(banhTang != null ? banhTang.maBanh : 0);
        d.setTenBanhTang(banhTang != null ? banhTang.tenBanh : "");
        d.setNgayBatDau(normalizeToStartOfDay(dtNgayBatDau.getDate()));
        d.setNgayKetThuc(normalizeToEndOfDay(dtNgayKetThuc.getDate()));
        d.setGhiChu("");
        return d;
    }

    private Date normalizeToStartOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private Date normalizeToEndOfDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    private void renderTable() {
        tableModel.setRowCount(0);
        for (KhuyenMaiVM km : dsKhuyenMai) {
            tableModel.addRow(new Object[]{
                km.maKM,
                km.tenCT,
                km.loaiKhuyenMai,
                getNoiDungKhuyenMai(km),
                dateFormat.format(km.ngayBatDau),
                dateFormat.format(km.ngayKetThuc),
                getTinhTrang(km)
            });
        }
    }

    private String getNoiDungKhuyenMai(KhuyenMaiVM km) {
        if ("Mua X tặng Y".equals(km.loaiKhuyenMai)) {
            return "Mua " + km.soLuongMua + " " + km.tenBanhMua + " tặng " + km.soLuongTang + " " + km.tenBanhTang;
        }
        return "Giảm " + km.phanTramGiam + "% cho hóa đơn >= " + moneyFormat.format(km.dieuKienToiThieu);
    }

    private void selectBanhByMa(JComboBox<ItemBanh> comboBox, int maBanh) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            ItemBanh item = comboBox.getItemAt(i);
            if (item.maBanh == maBanh) {
                comboBox.setSelectedIndex(i);
                return;
            }
        }
    }

    private String getTinhTrang(KhuyenMaiVM km) {
        Date now = new Date();
        if (now.before(km.ngayBatDau)) return "Chưa hiệu lực";
        if (now.after(km.ngayKetThuc)) return "Không hiệu lực";
        return "Có hiệu lực";
    }

    private void selectByMa(int maKM) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if ((Integer) tableModel.getValueAt(i, 0) == maKM) {
                table.setRowSelectionInterval(i, i);
                fillFormFromRow(i);
                return;
            }
        }
    }

    private void seedSampleProgramsIfEmpty() {
        Date now = new Date();
        ItemBanh banhMua = dsBanh.get(0);
        ItemBanh banhTang = dsBanh.size() > 1 ? dsBanh.get(1) : dsBanh.get(0);

        ChuongTrinhKhuyenMaiDTO km1 = new ChuongTrinhKhuyenMaiDTO();
        km1.setTenCTKM("Khuyến mãi khai trương");
        km1.setLoaiKhuyenMai("Giảm phần trăm");
        km1.setPhanTramGiam(10);
        km1.setDieuKienToiThieu(150000);
        km1.setSoLuongMua(0);
        km1.setSoLuongTang(0);
        km1.setMaBanhMua(0);
        km1.setMaBanhTang(0);
        km1.setNgayBatDau(normalizeToStartOfDay(now));
        km1.setNgayKetThuc(normalizeToEndOfDay(addDays(now, 30)));
        chuongTrinhKhuyenMaiBus.add(km1);

        ChuongTrinhKhuyenMaiDTO km2 = new ChuongTrinhKhuyenMaiDTO();
        km2.setTenCTKM("Mua 1 tặng 1 cuối tuần");
        km2.setLoaiKhuyenMai("Mua X tặng Y");
        km2.setPhanTramGiam(0);
        km2.setDieuKienToiThieu(0);
        km2.setSoLuongMua(1);
        km2.setSoLuongTang(1);
        km2.setMaBanhMua(banhMua.maBanh);
        km2.setMaBanhTang(banhTang.maBanh);
        km2.setNgayBatDau(normalizeToStartOfDay(addDays(now, -7)));
        km2.setNgayKetThuc(normalizeToEndOfDay(addDays(now, 15)));
        chuongTrinhKhuyenMaiBus.add(km2);

        ChuongTrinhKhuyenMaiDTO km3 = new ChuongTrinhKhuyenMaiDTO();
        km3.setTenCTKM("Giảm sâu cuối tháng");
        km3.setLoaiKhuyenMai("Giảm phần trăm");
        km3.setPhanTramGiam(30);
        km3.setDieuKienToiThieu(500000);
        km3.setSoLuongMua(0);
        km3.setSoLuongTang(0);
        km3.setMaBanhMua(0);
        km3.setMaBanhTang(0);
        km3.setNgayBatDau(normalizeToStartOfDay(addDays(now, -40)));
        km3.setNgayKetThuc(normalizeToEndOfDay(addDays(now, -10)));
        chuongTrinhKhuyenMaiBus.add(km3);

        chuongTrinhKhuyenMaiBus.loadData();
    }

    private void loadDataFromDatabase() {
        chuongTrinhKhuyenMaiBus.loadData();
        reloadDsFromBus();
        if (dsKhuyenMai.isEmpty()) {
            seedSampleProgramsIfEmpty();
            reloadDsFromBus();
        }
    }

    private void reloadDsFromBus() {
        dsKhuyenMai.clear();
        for (ChuongTrinhKhuyenMaiDTO d : chuongTrinhKhuyenMaiBus.getList()) {
            dsKhuyenMai.add(dtoToVM(d));
        }
    }

    private KhuyenMaiVM dtoToVM(ChuongTrinhKhuyenMaiDTO d) {
        KhuyenMaiVM vm = new KhuyenMaiVM();
        vm.maKM = d.getMaKM();
        vm.tenCT = d.getTenCTKM();
        vm.loaiKhuyenMai = d.getLoaiKhuyenMai();
        vm.phanTramGiam = d.getPhanTramGiam();
        vm.dieuKienToiThieu = d.getDieuKienToiThieu();
        vm.soLuongMua = d.getSoLuongMua();
        vm.soLuongTang = d.getSoLuongTang();
        vm.maBanhMua = d.getMaBanhMua();
        vm.tenBanhMua = d.getTenBanhMua() != null ? d.getTenBanhMua() : "";
        vm.maBanhTang = d.getMaBanhTang();
        vm.tenBanhTang = d.getTenBanhTang() != null ? d.getTenBanhTang() : "";
        vm.ngayBatDau = d.getNgayBatDau();
        vm.ngayKetThuc = d.getNgayKetThuc();
        return vm;
    }

    private Date addDays(Date source, int days) {
        long millis = source.getTime() + days * 24L * 60L * 60L * 1000L;
        return new Date(millis);
    }

    private JButton createStyledButton(String text, Color color, int width) {
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
        btn.setPreferredSize(new Dimension(width, 38));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(boldFont);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color base = color;
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(base.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(base);
            }
        });
        return btn;
    }

    private static class KhuyenMaiVM {
        int maKM;
        String tenCT;
        String loaiKhuyenMai;
        int phanTramGiam;
        double dieuKienToiThieu;
        int soLuongMua;
        int soLuongTang;
        int maBanhMua;
        String tenBanhMua;
        int maBanhTang;
        String tenBanhTang;
        Date ngayBatDau;
        Date ngayKetThuc;

        KhuyenMaiVM() {
        }

        KhuyenMaiVM(int maKM, String tenCT, String loaiKhuyenMai, int phanTramGiam, double dieuKienToiThieu,
                    int soLuongMua, int soLuongTang, int maBanhMua, String tenBanhMua,
                    int maBanhTang, String tenBanhTang, Date ngayBatDau, Date ngayKetThuc) {
            this.maKM = maKM;
            this.tenCT = tenCT;
            this.loaiKhuyenMai = loaiKhuyenMai;
            this.phanTramGiam = phanTramGiam;
            this.dieuKienToiThieu = dieuKienToiThieu;
            this.soLuongMua = soLuongMua;
            this.soLuongTang = soLuongTang;
            this.maBanhMua = maBanhMua;
            this.tenBanhMua = tenBanhMua;
            this.maBanhTang = maBanhTang;
            this.tenBanhTang = tenBanhTang;
            this.ngayBatDau = ngayBatDau;
            this.ngayKetThuc = ngayKetThuc;
        }
    }

    private static class ItemBanh {
        int maBanh;
        String tenBanh;

        ItemBanh(int maBanh, String tenBanh) {
            this.maBanh = maBanh;
            this.tenBanh = tenBanh;
        }

        @Override
        public String toString() {
            return maBanh + " - " + tenBanh;
        }
    }

    private class DatePickerField extends JPanel {
        private final JTextField txtDate;
        private final JButton btnPick;
        private Date selectedDate;

        DatePickerField() {
            setLayout(new BorderLayout(4, 0));
            setOpaque(false);

            txtDate = new JTextField();
            txtDate.setEditable(false);
            txtDate.setHorizontalAlignment(JTextField.LEFT);
            txtDate.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(209, 213, 219), 1, true),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)
            ));

            btnPick = new JButton("...");
            btnPick.setPreferredSize(new Dimension(34, 30));
            btnPick.setFocusPainted(false);
            btnPick.setCursor(new Cursor(Cursor.HAND_CURSOR));

            add(txtDate, BorderLayout.CENTER);
            add(btnPick, BorderLayout.EAST);

            setDate(new Date());
            btnPick.addActionListener(e -> openDateDialog());
        }

        @Override
        public void setFont(Font font) {
            super.setFont(font);
            if (txtDate != null) {
                txtDate.setFont(font);
            }
            if (btnPick != null) {
                btnPick.setFont(font);
            }
        }

        Date getDate() {
            return selectedDate;
        }

        void setDate(Date date) {
            selectedDate = date;
            txtDate.setText(dateFormat.format(date));
        }

        private void openDateDialog() {
            Window owner = SwingUtilities.getWindowAncestor(QuanLiKhuyenMaiPanel.this);
            JDialog dialog = new JDialog(owner, "Chọn ngày", Dialog.ModalityType.APPLICATION_MODAL);
            dialog.setSize(320, 300);
            dialog.setLocationRelativeTo(QuanLiKhuyenMaiPanel.this);
            dialog.setLayout(new BorderLayout(8, 8));

            Calendar cal = Calendar.getInstance();
            cal.setTime(selectedDate);

			JComboBox<String> cboMonth = createMonthComboBox(cal);
			JSpinner spnYear = createYearSpinner(cal);
			JPanel top = createTopPanel(cboMonth, spnYear);
			JPanel calendarPanel = createCalendarPanel();
			JPanel bottom = createBottomPanel(dialog);

			Runnable renderCalendar = () -> renderCalendar(calendarPanel, cboMonth, spnYear, dialog);

			cboMonth.addActionListener(e -> renderCalendar.run());
			spnYear.addChangeListener(e -> renderCalendar.run());
			renderCalendar.run();

			dialog.add(top, BorderLayout.NORTH);
			dialog.add(calendarPanel, BorderLayout.CENTER);
			dialog.add(bottom, BorderLayout.SOUTH);
			dialog.setVisible(true);
		}

		private JComboBox<String> createMonthComboBox(Calendar currentCal) {
			JComboBox<String> cboMonth = new JComboBox<>(new String[]{
				"Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6",
				"Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
			});
			cboMonth.setSelectedIndex(currentCal.get(Calendar.MONTH));
			return cboMonth;
		}

		private JSpinner createYearSpinner(Calendar currentCal) {
			SpinnerNumberModel yearModel = new SpinnerNumberModel(currentCal.get(Calendar.YEAR), 2000, 2100, 1);
            JSpinner spnYear = new JSpinner(yearModel);
            spnYear.setPreferredSize(new Dimension(80, 28));
            JSpinner.NumberEditor yearEditor = new JSpinner.NumberEditor(spnYear, "#");
            spnYear.setEditor(yearEditor);
			return spnYear;
		}

		private JPanel createTopPanel(JComboBox<String> cboMonth, JSpinner spnYear) {
			JPanel top = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 8));
            top.add(cboMonth);
            top.add(spnYear);
			return top;
		}

		private JPanel createCalendarPanel() {
            JPanel calendarPanel = new JPanel(new GridLayout(7, 7, 2, 2));
            calendarPanel.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
			return calendarPanel;
		}

		private void renderCalendar(JPanel calendarPanel, JComboBox<String> cboMonth, JSpinner spnYear, JDialog dialog) {
			calendarPanel.removeAll();
			renderCalendarHeader(calendarPanel);
			renderCalendarDays(calendarPanel, cboMonth, spnYear, dialog);
			fillCalendarPlaceholders(calendarPanel);
			calendarPanel.revalidate();
			calendarPanel.repaint();
		}

		private void renderCalendarHeader(JPanel calendarPanel) {
			String[] headers = {"CN", "T2", "T3", "T4", "T5", "T6", "T7"};
			for (String headerText : headers) {
				JLabel lbl = new JLabel(headerText, SwingConstants.CENTER);
				lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
				lbl.setForeground(new Color(75, 85, 99));
				calendarPanel.add(lbl);
			}
		}

		private void renderCalendarDays(JPanel calendarPanel, JComboBox<String> cboMonth, JSpinner spnYear, JDialog dialog) {
			Calendar view = Calendar.getInstance();
			view.set(Calendar.YEAR, (Integer) spnYear.getValue());
			view.set(Calendar.MONTH, cboMonth.getSelectedIndex());
			view.set(Calendar.DAY_OF_MONTH, 1);

			int firstDay = view.get(Calendar.DAY_OF_WEEK);
			int maxDay = view.getActualMaximum(Calendar.DAY_OF_MONTH);

			for (int i = 1; i < firstDay; i++) {
				calendarPanel.add(new JLabel(""));
			}

			for (int day = 1; day <= maxDay; day++) {
				calendarPanel.add(createDayButton(day, cboMonth, spnYear, dialog));
			}
		}

		private JButton createDayButton(int day, JComboBox<String> cboMonth, JSpinner spnYear, JDialog dialog) {
			JButton btnDay = new JButton(String.valueOf(day));
			btnDay.setFocusPainted(false);
			btnDay.setMargin(new Insets(0, 0, 0, 0));
			btnDay.setBackground(Color.WHITE);
			btnDay.setCursor(new Cursor(Cursor.HAND_CURSOR));
			btnDay.addActionListener(e -> {
				Date pickedDate = buildDate((Integer) spnYear.getValue(), cboMonth.getSelectedIndex(), day);
				setDate(pickedDate);
				dialog.dispose();
			});
			return btnDay;
		}

		private Date buildDate(int year, int monthIndex, int day) {
			Calendar picked = Calendar.getInstance();
			picked.set(Calendar.YEAR, year);
			picked.set(Calendar.MONTH, monthIndex);
			picked.set(Calendar.DAY_OF_MONTH, day);
			return picked.getTime();
		}

		private void fillCalendarPlaceholders(JPanel calendarPanel) {
			while (calendarPanel.getComponentCount() < 49) {
				calendarPanel.add(new JLabel(""));
			}
		}

		private JPanel createBottomPanel(JDialog dialog) {
			JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
            JButton btnToday = new JButton("Hôm nay");
            JButton btnClose = new JButton("Đóng");
            btnToday.addActionListener(e -> {
                setDate(new Date());
                dialog.dispose();
            });
            btnClose.addActionListener(e -> dialog.dispose());
            bottom.add(btnToday);
            bottom.add(btnClose);
			return bottom;
        }
    }
}
