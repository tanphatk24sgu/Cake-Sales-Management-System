import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class HangSanXuatPanel extends JPanel {

    private JTable table;
    private DefaultTableModel model;

    public HangSanXuatPanel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("QUẢN LÍ HÃNG SẢN XUẤT");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new String[]{"Mã hãng", "Tên hãng"}, 0);

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel();

        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");

        panel.add(btnAdd);
        panel.add(btnEdit);
        panel.add(btnDelete);

        add(panel, BorderLayout.SOUTH);
    }
}