import Customize.RoundedButton;
import Customize.RoundedComboBox;
import Customize.RoundedTextField;
import DBFunction.NguoiDungDAO;
import TempClass.NguoiDung;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class UserForManagerPanel {
    public static JPanel createMainPanel() {
        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel title = new JLabel("Quản Lý Người Dùng");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topBar.add(title, BorderLayout.WEST);

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JTextField searchField = new RoundedTextField(20);
        searchField.setText("Search here...");
        JButton addBtn = new RoundedButton("+ Thêm Người Dùng");
        addBtn.setBackground(new Color(33, 150, 243));
        addBtn.setFocusPainted(false);
        addBtn.setFont(new Font("Arial", Font.BOLD, 13));
        addBtn.setFocusPainted(false);
        addBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        

        topRight.add(searchField);
        topRight.add(addBtn);
        topBar.add(topRight, BorderLayout.EAST);

        JButton detailBtn = new RoundedButton("Detail");
        detailBtn.setBackground(new Color(33, 150, 243));
        detailBtn.setFocusPainted(false);
        detailBtn.setFont(new Font("Arial", Font.BOLD, 13));
        detailBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        detailBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        topRight.add(detailBtn);


        // Table columns
        String[] columns = {
            "Chọn", "Mã ND", "Tên ND", "Tên Đăng Nhập", "Mật Khẩu", "Mã Vai Trò", "Trạng Thái"
        };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) return Boolean.class;
                return String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);

        // Load data from DB
        loaddata(model);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Main content layout
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(topBar, BorderLayout.NORTH);
        centerPanel.add(tableScroll, BorderLayout.CENTER);
        addBtn.addActionListener(e -> {
            JTextField tenNDField = new RoundedTextField(10);
            JTextField tenDangNhapField = new RoundedTextField(10);
            JTextField matKhauField = new RoundedTextField(10);
            JComboBox<String> maVTField = new RoundedComboBox(new String[]{"NK", "XK", "QLK"});
            JComboBox<String> trangThaiBox = new RoundedComboBox(new String[]{"Hoạt động", "Khóa"});

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Tên ND:"));
            panel.add(tenNDField);
            panel.add(new JLabel("Tên Đăng Nhập:"));
            panel.add(tenDangNhapField);
            panel.add(new JLabel("Mật Khẩu:"));
            panel.add(matKhauField);
            panel.add(new JLabel("Mã Vai Trò:"));
            panel.add(maVTField);
            panel.add(new JLabel("Trạng Thái:"));
            panel.add(trangThaiBox);

            // Tạo custom button
            JButton okBtn = new RoundedButton("OK");
            okBtn.setBackground(new Color(33, 150, 243));
            okBtn.setForeground(Color.WHITE);
            okBtn.setFocusPainted(false);
            okBtn.setFont(new Font("Arial", Font.BOLD, 13));
            okBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            okBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JButton cancelBtn = new RoundedButton("Cancel");
            cancelBtn.setBackground(new Color(220, 53, 69));
            cancelBtn.setForeground(Color.WHITE);
            cancelBtn.setFocusPainted(false);
            cancelBtn.setFont(new Font("Arial", Font.BOLD, 13));
            cancelBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            cancelBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JDialog dialog = new JDialog((Frame) null, "Thêm Người Dùng Mới", true);
            dialog.setLayout(new BorderLayout());
            dialog.add(panel, BorderLayout.CENTER);

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);
            dialog.add(btnPanel, BorderLayout.SOUTH);

            dialog.setSize(350, 490);
            dialog.setLocationRelativeTo(null);

            okBtn.addActionListener(ev -> {
                try {
                    NguoiDung nd = new NguoiDung();
                    nd.setTenND(tenNDField.getText().trim());
                    nd.setTenDangNhap(tenDangNhapField.getText().trim());
                    nd.setMatKhau(matKhauField.getText().trim());
                    nd.setMaVT(maVTField.getSelectedItem().toString());
                    nd.setTrangThai(trangThaiBox.getSelectedIndex() == 0 ? 1 : 0);

                    NguoiDungDAO dao = new NguoiDungDAO();
                    boolean success = dao.themNguoiDung(nd);
                    if (success) {
                        loaddata(model);
                        JOptionPane.showMessageDialog(dialog, "Thêm người dùng thành công!");
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Thêm người dùng thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ hoặc lỗi khi thêm người dùng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelBtn.addActionListener(ev -> dialog.dispose());

            dialog.setVisible(true);
        });
        detailBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn một người dùng để xem chi tiết!");
                return;
            }
            // Lấy dữ liệu từ dòng được chọn
            String maND = model.getValueAt(selectedRow, 1).toString();
            String tenND = model.getValueAt(selectedRow, 2).toString();
            String tenDangNhap = model.getValueAt(selectedRow, 3).toString();
            String matKhau = model.getValueAt(selectedRow, 4).toString();
            String maVT = model.getValueAt(selectedRow, 5).toString();
            String trangThai = model.getValueAt(selectedRow, 6).toString();

            JTextField maNDField = new RoundedTextField(10);
            maNDField.setText(maND);
            maNDField.setEditable(false);
            JTextField tenNDField = new RoundedTextField(10);
            tenNDField.setText(tenND);
            JTextField tenDangNhapField = new RoundedTextField(10);
            tenDangNhapField.setText(tenDangNhap);
            JTextField matKhauField = new RoundedTextField(10);
            matKhauField.setText(matKhau);
            JComboBox<String> maVTField = new RoundedComboBox(new String[]{"NK", "XK", "QLK"});
            maVTField.setSelectedItem(maVT);
            JComboBox<String> trangThaiBox = new RoundedComboBox(new String[]{"Hoạt động", "Khóa"});
            trangThaiBox.setSelectedIndex(trangThai.equals("Hoạt động") ? 0 : 1);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Mã ND:"));
            panel.add(maNDField);
            panel.add(new JLabel("Tên ND:"));
            panel.add(tenNDField);
            panel.add(new JLabel("Tên Đăng Nhập:"));
            panel.add(tenDangNhapField);
            panel.add(new JLabel("Mật Khẩu:"));
            panel.add(matKhauField);
            panel.add(new JLabel("Mã Vai Trò:"));
            panel.add(maVTField);
            panel.add(new JLabel("Trạng Thái:"));
            panel.add(trangThaiBox);

            JButton updateBtn = new RoundedButton("Cập nhật");
            updateBtn.setBackground(new Color(33, 150, 243));
            updateBtn.setForeground(Color.WHITE);
            updateBtn.setFocusPainted(false);
            updateBtn.setFont(new Font("Arial", Font.BOLD, 13));
            updateBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            updateBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JButton deleteBtn = new RoundedButton("Xóa");
            deleteBtn.setBackground(new Color(220, 53, 69));
            deleteBtn.setForeground(Color.WHITE);
            deleteBtn.setFocusPainted(false);
            deleteBtn.setFont(new Font("Arial", Font.BOLD, 13));
            deleteBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            deleteBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            btnPanel.add(updateBtn);
            btnPanel.add(deleteBtn);

            JDialog dialog = new JDialog((Frame) null, "Chi tiết người dùng", true);
            dialog.setLayout(new BorderLayout());
            dialog.add(panel, BorderLayout.CENTER);
            dialog.add(btnPanel, BorderLayout.SOUTH);
            dialog.setSize(350, 600);
            dialog.setLocationRelativeTo(null);

            // Xử lý cập nhật
            updateBtn.addActionListener(ev -> {
                try {
                    NguoiDung nd = new NguoiDung();
                    nd.setMaND(maNDField.getText().trim());
                    nd.setTenND(tenNDField.getText().trim());
                    nd.setTenDangNhap(tenDangNhapField.getText().trim());
                    nd.setMatKhau(matKhauField.getText().trim());
                    nd.setMaVT(maVTField.getSelectedItem().toString());
                    nd.setTrangThai(trangThaiBox.getSelectedIndex() == 0 ? 1 : 0);

                    NguoiDungDAO dao = new NguoiDungDAO();
                    boolean success = dao.suaNguoiDung(nd);
                    if (success) {
                        loaddata(model);
                        JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });

            // Xử lý xóa
            deleteBtn.addActionListener(ev -> {
                int confirm = JOptionPane.showConfirmDialog(dialog, "Bạn có chắc muốn xóa người dùng này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        NguoiDungDAO dao = new NguoiDungDAO();
                        boolean success = dao.xoaNguoiDung(maNDField.getText().trim());
                        if (success) {
                            loaddata(model);
                            JOptionPane.showMessageDialog(dialog, "Xóa thành công!");
                            dialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, "Có lỗi khi xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            dialog.setVisible(true);
        });
        return centerPanel;
    }
    private static void loaddata(DefaultTableModel model){
        model.setRowCount(0);
        try {
            NguoiDungDAO dao = new NguoiDungDAO();
            List<NguoiDung> ds = dao.getAllNguoiDung();
            for (NguoiDung nd : ds) {
                model.addRow(new Object[]{
                    false,
                    nd.getMaND(),
                    nd.getTenND(),
                    nd.getTenDangNhap(),
                    nd.getMatKhau(),
                    nd.getMaVT(),
                    nd.getTrangThai() == 1 ? "Hoạt động" : "Khóa"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}