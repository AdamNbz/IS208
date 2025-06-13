import Customize.RoundedButton;
import Customize.RoundedTextField;
import DBFunction.NhaCungCapDAO;
import TempClass.NhaCungCap;
import java.awt.*;
import java.sql.Connection;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SupplierForManagerPanel {
    public static JPanel createMainPanel() {
        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel title = new JLabel("Suppliers");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topBar.add(title, BorderLayout.WEST);

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JTextField searchField = new RoundedTextField(20);
        searchField.setText("Search Here...");
        JButton addBtn = new RoundedButton("+ Add New Supplier");
        addBtn.setBackground(new Color(33, 150, 243));
        addBtn.setFocusPainted(false);
        addBtn.setFont(new Font("Arial", Font.BOLD, 13));
        addBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton detailButton = new RoundedButton("Details");
        detailButton.setBackground(new Color(33, 150, 243));
        detailButton.setFocusPainted(false);
        detailButton.setFont(new Font("Arial", Font.BOLD, 13));
        detailButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        detailButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        topRight.add(searchField);
        topRight.add(addBtn);
        topRight.add(detailButton);
        topBar.add(topRight, BorderLayout.EAST);

        // Table columns
        String[] columns = {
            "Chọn", "Mã NCC", "Tên NCC", "SĐT", "Địa chỉ", "Email"
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

        // Add Supplier
        addBtn.addActionListener(e -> {
            JTextField tenNCCField = new RoundedTextField(15);
            JTextField sdtField = new RoundedTextField(15);
            JTextField diaChiField = new RoundedTextField(15);
            JTextField emailField = new RoundedTextField(15);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Tên NCC:"));
            panel.add(tenNCCField);
            panel.add(new JLabel("Số điện thoại:"));
            panel.add(sdtField);
            panel.add(new JLabel("Địa chỉ:"));
            panel.add(diaChiField);
            panel.add(new JLabel("Email:"));
            panel.add(emailField);

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

            JDialog dialog = new JDialog((Frame) null, "Thêm nhà cung cấp mới", true);
            dialog.setLayout(new BorderLayout());
            dialog.add(panel, BorderLayout.CENTER);

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);
            dialog.add(btnPanel, BorderLayout.SOUTH);

            dialog.setSize(350, 400);
            dialog.setLocationRelativeTo(null);

            okBtn.addActionListener(ev -> {
                try {
                    NhaCungCap ncc = new NhaCungCap();
                    ncc.setMaNCC("");
                    ncc.setTenNCC(tenNCCField.getText().trim());
                    ncc.setSdt(sdtField.getText().trim());
                    ncc.setDiaChi(diaChiField.getText().trim());
                    ncc.setEmail(emailField.getText().trim());

                    // Kết nối DB, bạn có thể dùng singleton hoặc sửa lại DAO nếu cần
                    Connection conn = DBFunction.DBConnection.getConnection();
                    NhaCungCapDAO dao = new NhaCungCapDAO(conn);
                    boolean success = dao.insert(ncc);
                    if (success) {
                        loaddata(model);
                        JOptionPane.showMessageDialog(dialog, "Thêm nhà cung cấp thành công!");
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Thêm nhà cung cấp thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                    conn.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ hoặc lỗi khi thêm nhà cung cấp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelBtn.addActionListener(ev -> dialog.dispose());

            dialog.setVisible(true);
        });

        // Detail/Edit/Delete Supplier
        detailButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn một nhà cung cấp để xem chi tiết!");
                return;
            }
            String maNCC = model.getValueAt(selectedRow, 1).toString();
            String tenNCC = model.getValueAt(selectedRow, 2).toString();
            String sdt = model.getValueAt(selectedRow, 3).toString();
            String diaChi = model.getValueAt(selectedRow, 4).toString();
            String email = model.getValueAt(selectedRow, 5).toString();

            JTextField maNCCField = new RoundedTextField(15);
            maNCCField.setText(maNCC);
            maNCCField.setEditable(false);
            JTextField tenNCCField = new RoundedTextField(15);
            tenNCCField.setText(tenNCC);
            JTextField sdtField = new RoundedTextField(15);
            sdtField.setText(sdt);
            JTextField diaChiField = new RoundedTextField(15);
            diaChiField.setText(diaChi);
            JTextField emailField = new RoundedTextField(15);
            emailField.setText(email);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Mã NCC:"));
            panel.add(maNCCField);
            panel.add(new JLabel("Tên NCC:"));
            panel.add(tenNCCField);
            panel.add(new JLabel("Số điện thoại:"));
            panel.add(sdtField);
            panel.add(new JLabel("Địa chỉ:"));
            panel.add(diaChiField);
            panel.add(new JLabel("Email:"));
            panel.add(emailField);

            JButton updateBtn = new RoundedButton("Cập nhật");
            updateBtn.setBackground(new Color(33, 150, 243));
            updateBtn.setFocusPainted(false);
            updateBtn.setFont(new Font("Arial", Font.BOLD, 13));
            updateBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            updateBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JButton deleteBtn = new RoundedButton("Xóa");
            deleteBtn.setBackground(new Color(220, 53, 69));
            deleteBtn.setFocusPainted(false);
            deleteBtn.setFont(new Font("Arial", Font.BOLD, 13));
            deleteBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            deleteBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            btnPanel.add(updateBtn);
            btnPanel.add(deleteBtn);

            JDialog dialog = new JDialog((Frame) null, "Chi tiết nhà cung cấp", true);
            dialog.setLayout(new BorderLayout());
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            mainPanel.add(panel, BorderLayout.CENTER);
            mainPanel.add(btnPanel, BorderLayout.SOUTH);
            dialog.add(mainPanel);
            dialog.setSize(400, 420);
            dialog.setLocationRelativeTo(null);

            updateBtn.addActionListener(ev -> {
                try {
                    NhaCungCap ncc = new NhaCungCap();
                    ncc.setMaNCC(maNCCField.getText().trim());
                    ncc.setTenNCC(tenNCCField.getText().trim());
                    ncc.setSdt(sdtField.getText().trim());
                    ncc.setDiaChi(diaChiField.getText().trim());
                    ncc.setEmail(emailField.getText().trim());

                    Connection conn = DBFunction.DBConnection.getConnection();
                    NhaCungCapDAO dao = new NhaCungCapDAO(conn);
                    boolean success = dao.update(ncc);
                    if (success) {
                        loaddata(model);
                        JOptionPane.showMessageDialog(dialog, "Cập nhật thành công!");
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Cập nhật thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                    conn.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });

            deleteBtn.addActionListener(ev -> {
                int confirm = JOptionPane.showConfirmDialog(dialog, "Bạn có chắc muốn xóa nhà cung cấp này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        Connection conn = DBFunction.DBConnection.getConnection();
                        NhaCungCapDAO dao = new NhaCungCapDAO(conn);
                        boolean success = dao.delete(maNCCField.getText().trim());
                        if (success) {
                            loaddata(model);
                            JOptionPane.showMessageDialog(dialog, "Xóa thành công!");
                            dialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                        conn.close();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, "Có lỗi khi xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            dialog.setVisible(true);
        });

        return centerPanel;
    }

    private static void loaddata(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            Connection conn = DBFunction.DBConnection.getConnection();
            NhaCungCapDAO dao = new NhaCungCapDAO(conn);
            List<NhaCungCap> ds = dao.getAll();
            for (NhaCungCap ncc : ds) {
                model.addRow(new Object[]{
                    false,
                    ncc.getMaNCC(),
                    ncc.getTenNCC(),
                    ncc.getSdt(),
                    ncc.getDiaChi(),
                    ncc.getEmail()
                });
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}