import Customize.RoundedButton;
import Customize.RoundedTextField;
import DBFunction.ThongKeDAO;
import DBFunction.ThongKeSPDAO;
import TempClass.ThongKe;
import TempClass.ThongKeSP;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class StatisticForManagerPanel {
    public static JPanel createMainPanel() {
        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel title = new JLabel("Thống kê tổng hợp");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topBar.add(title, BorderLayout.WEST);

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JTextField searchField = new RoundedTextField(20);
        searchField.setText("Search Here...");
        JButton addBtn = new RoundedButton("+ Thêm thống kê hôm nay");
        addBtn.setBackground(new Color(33, 150, 243));
        addBtn.setFocusPainted(false);
        addBtn.setFont(new Font("Arial", Font.BOLD, 13));
        addBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton detailButton = new RoundedButton("Chi tiết SP");
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
            "Chọn", "Mã TK", "Nội dung", "Ngày", "Tổng PX", "Tổng PN", "Tổng SP nhập", "Tổng SP xuất"
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

        // Insert ThongKe (gọi procedure)
        addBtn.addActionListener(e -> {
            JTextField noiDungField = new RoundedTextField(15);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Nội dung thống kê:"));
            panel.add(noiDungField);

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

            JDialog dialog = new JDialog((Frame) null, "Thêm thống kê hôm nay", true);
            dialog.setLayout(new BorderLayout());
            dialog.add(panel, BorderLayout.CENTER);

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);
            dialog.add(btnPanel, BorderLayout.SOUTH);

            dialog.setSize(350, 200);
            dialog.setLocationRelativeTo(null);

            okBtn.addActionListener(ev -> {
                try {
                    Connection conn = DBFunction.DBConnection.getConnection();
                    ThongKeDAO dao = new ThongKeDAO(conn);
                    boolean success = dao.callInsertThongKeToday(noiDungField.getText().trim());
                    if (success) {
                        loaddata(model);
                        JOptionPane.showMessageDialog(dialog, "Thêm thống kê thành công!");
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Thêm thống kê thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                    conn.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Lỗi khi thêm thống kê!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelBtn.addActionListener(ev -> dialog.dispose());

            dialog.setVisible(true);
        });

        // Detail ThongKeSP by MATK
        detailButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn một thống kê để xem chi tiết!");
                return;
            }
            String maTK = model.getValueAt(selectedRow, 1).toString();

            // Tạo bảng chi tiết ThongKeSP
            String[] spColumns = {"Mã TKSP", "Mã TK", "Mã SP", "Tồn kho"};
            DefaultTableModel spModel = new DefaultTableModel(spColumns, 0);
            JTable spTable = new JTable(spModel);
            spTable.setRowHeight(35);

            try {
                Connection conn = DBFunction.DBConnection.getConnection();
                ThongKeSPDAO spDao = new ThongKeSPDAO(conn);
                List<ThongKeSP> ds = spDao.getAllbyID(maTK);
                for (ThongKeSP tksp : ds) {
                    spModel.addRow(new Object[]{
                        tksp.getMaTKSP(),
                        tksp.getMaTK(),
                        tksp.getMaSP(),
                        tksp.getTonKho()
                    });
                }
                conn.close();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Lỗi khi tải chi tiết thống kê SP!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }

            JScrollPane spScroll = new JScrollPane(spTable);
            spScroll.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JDialog detailDialog = new JDialog((Frame) null, "Chi tiết sản phẩm thống kê", true);
            detailDialog.setLayout(new BorderLayout());
            detailDialog.add(spScroll, BorderLayout.CENTER);
            detailDialog.setSize(600, 400);
            detailDialog.setLocationRelativeTo(null);
            detailDialog.setVisible(true);
        });
        JButton deleteButton = new RoundedButton("Xóa");
        deleteButton.setBackground(new Color(220, 53, 69));
        deleteButton.setFocusPainted(false);
        deleteButton.setFont(new Font("Arial", Font.BOLD, 13));
        deleteButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        topRight.add(deleteButton);

        // ...existing code...

        deleteButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn một thống kê để xóa!");
                return;
            }
            String maTK = model.getValueAt(selectedRow, 1).toString();
            int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa thống kê này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Connection conn = DBFunction.DBConnection.getConnection();
                    ThongKeDAO dao = new ThongKeDAO(conn);
                    // Xóa các bản ghi ThongKeSP liên quan trước (nếu có)
                    ThongKeSPDAO spDao = new ThongKeSPDAO(conn);
                    List<ThongKeSP> ds = spDao.getAllbyID(maTK);
                    for (ThongKeSP tksp : ds) {
                        spDao.delete(tksp.getMaTKSP());
                    }
                    // Xóa thống kê
                    String sql = "DELETE FROM ThongKe WHERE MATK = ?";
                    PreparedStatement ps = conn.prepareStatement(sql);
                    ps.setString(1, maTK);
                    int result = ps.executeUpdate();
                    conn.close();
                    if (result > 0) {
                        loaddata(model);
                        JOptionPane.showMessageDialog(null, "Xóa thành công!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Xóa thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Có lỗi khi xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        return centerPanel;
    }

    private static void loaddata(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            Connection conn = DBFunction.DBConnection.getConnection();
            ThongKeDAO dao = new ThongKeDAO(conn);
            List<ThongKe> ds = dao.getAll();
            for (ThongKe tk : ds) {
                model.addRow(new Object[]{
                    false,
                    tk.getMaTK(),
                    tk.getNoiDung(),
                    tk.getNgay(),
                    tk.getTongPhieuXuat(),
                    tk.getTongPhieuNhap(),
                    tk.getTongSPNhap(),
                    tk.getTongSPXuat()
                });
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}