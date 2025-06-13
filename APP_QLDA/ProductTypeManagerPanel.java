import Customize.RoundedButton;
import Customize.RoundedTextField;
import DBFunction.LoaiSPDAO;
import TempClass.LoaiSP;
import java.awt.*;
import java.sql.Connection;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ProductTypeManagerPanel {
    public static JPanel createMainPanel() {
        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel title = new JLabel("Quản lý Loại Sản Phẩm");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topBar.add(title, BorderLayout.WEST);

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JTextField searchField = new RoundedTextField(20);
        searchField.setText("Search Here...");
        JButton addBtn = new RoundedButton("+ Thêm Loại SP");
        addBtn.setBackground(new Color(33, 150, 243));
        addBtn.setFocusPainted(false);
        addBtn.setFont(new Font("Arial", Font.BOLD, 13));
        addBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton detailButton = new RoundedButton("Chi tiết");
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
            "Chọn", "Mã Loại", "Tên Loại", "Đơn vị tính"
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

        // Add LoaiSP
        addBtn.addActionListener(e -> {
            JTextField maLoaiField = new RoundedTextField(15);
            JTextField tenLoaiField = new RoundedTextField(15);
            JTextField dvtField = new RoundedTextField(15);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Tên Loại:"));
            panel.add(tenLoaiField);
            panel.add(new JLabel("Đơn vị tính:"));
            panel.add(dvtField);

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

            JDialog dialog = new JDialog((Frame) null, "Thêm loại sản phẩm mới", true);
            dialog.setLayout(new BorderLayout());
            dialog.add(panel, BorderLayout.CENTER);

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);
            dialog.add(btnPanel, BorderLayout.SOUTH);

            dialog.setSize(350, 300);
            dialog.setLocationRelativeTo(null);

            okBtn.addActionListener(ev -> {
                try {
                    LoaiSP loai = new LoaiSP();
                    loai.setMaLoai("");
                    loai.setTenLoai(tenLoaiField.getText().trim());
                    loai.setDVT(dvtField.getText().trim());

                    Connection conn = DBFunction.DBConnection.getConnection();
                    LoaiSPDAO dao = new LoaiSPDAO(conn);
                    boolean success = dao.insert(loai);
                    if (success) {
                        loaddata(model);
                        JOptionPane.showMessageDialog(dialog, "Thêm loại sản phẩm thành công!");
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Thêm loại sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                    conn.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ hoặc lỗi khi thêm loại sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelBtn.addActionListener(ev -> dialog.dispose());

            dialog.setVisible(true);
        });

        // Detail/Edit/Delete LoaiSP
        detailButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn một loại sản phẩm để xem chi tiết!");
                return;
            }
            String maLoai = model.getValueAt(selectedRow, 1).toString();
            String tenLoai = model.getValueAt(selectedRow, 2).toString();
            String dvt = model.getValueAt(selectedRow, 3).toString();

            JTextField maLoaiField = new RoundedTextField(15);
            maLoaiField.setText(maLoai);
            maLoaiField.setEditable(false);
            JTextField tenLoaiField = new RoundedTextField(15);
            tenLoaiField.setText(tenLoai);
            JTextField dvtField = new RoundedTextField(15);
            dvtField.setText(dvt);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Mã Loại:"));
            panel.add(maLoaiField);
            panel.add(new JLabel("Tên Loại:"));
            panel.add(tenLoaiField);
            panel.add(new JLabel("Đơn vị tính:"));
            panel.add(dvtField);

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

            JDialog dialog = new JDialog((Frame) null, "Chi tiết loại sản phẩm", true);
            dialog.setLayout(new BorderLayout());
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            mainPanel.add(panel, BorderLayout.CENTER);
            mainPanel.add(btnPanel, BorderLayout.SOUTH);
            dialog.add(mainPanel);
            dialog.setSize(350, 300);
            dialog.setLocationRelativeTo(null);

            updateBtn.addActionListener(ev -> {
                try {
                    LoaiSP loai = new LoaiSP();
                    loai.setMaLoai(maLoaiField.getText().trim());
                    loai.setTenLoai(tenLoaiField.getText().trim());
                    loai.setDVT(dvtField.getText().trim());

                    Connection conn = DBFunction.DBConnection.getConnection();
                    LoaiSPDAO dao = new LoaiSPDAO(conn);
                    boolean success = dao.update(loai);
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
                int confirm = JOptionPane.showConfirmDialog(dialog, "Bạn có chắc muốn xóa loại sản phẩm này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        Connection conn = DBFunction.DBConnection.getConnection();
                        LoaiSPDAO dao = new LoaiSPDAO(conn);
                        boolean success = dao.delete(maLoaiField.getText().trim());
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
            LoaiSPDAO dao = new LoaiSPDAO(conn);
            List<LoaiSP> ds = dao.getAll();
            for (LoaiSP loai : ds) {
                model.addRow(new Object[]{
                    false,
                    loai.getMaLoai(),
                    loai.getTenLoai(),
                    loai.getDVT()
                });
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}