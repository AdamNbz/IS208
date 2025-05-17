
import Customize.RoundedButton;
import Customize.RoundedComboBox;
import Customize.RoundedPanel;
import Customize.RoundedTextField;
import DBFunction.PhieuNhapXuatDAO;
import DBFunction.SanPhamDAO;
import TempClass.SanPham;
import java.awt.*;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

public class ProductForManagerPanel {
    public static JPanel createMainPanel(){
        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel title = new JLabel("Products");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topBar.add(title, BorderLayout.WEST);

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JTextField searchField = new RoundedTextField(20);
        searchField.setText("Search Here...");
        JButton addBtn = new RoundedButton("+ Add New Product");
        addBtn.setBackground(new Color(33, 150, 243));
        addBtn.setFocusPainted(false);
        addBtn.setFont(new Font("Arial", Font.BOLD, 13));
        
        addBtn.setFocusPainted(false);
        addBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        topRight.add(searchField);
        topRight.add(addBtn);
        topBar.add(topRight, BorderLayout.EAST);

        JButton detailButton = new RoundedButton("Details");
        detailButton.setBackground(new Color(33, 150, 243));
        detailButton.setFocusPainted(false);
        detailButton.setFont(new Font("Arial", Font.BOLD, 13));
        
        detailButton.setFocusPainted(false);
        detailButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        detailButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        topRight.add(detailButton);
        
        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        int productype = SanPhamDAO.getSoLuongSanPham();
        statsPanel.add(createStatBox(String.valueOf(productype), "Total product types"));
        int productsum = SanPhamDAO.getSoLuongtlkSanPham();
        statsPanel.add(createStatBox(String.valueOf(productsum), "Total Products"));
        int importvalue = PhieuNhapXuatDAO.getsoluongphieunhap();
        statsPanel.add(createStatBox(String.valueOf(importvalue), "Import"));
        int exportvalue = PhieuNhapXuatDAO.getsoluongphieuxuat();
        statsPanel.add(createStatBox(String.valueOf(exportvalue), "Export"));
        
        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        JComboBox firstBox = new RoundedComboBox(new String[]{"Show: All Products", "T-shirt", "Hoodie"});
        filterPanel.add(firstBox);
        JButton filterbutton = new RoundedButton("Filter");
        filterbutton.setBackground(new Color(33, 150, 243));
        filterbutton.setFocusPainted(false);
        filterbutton.setFont(new Font("Arial", Font.BOLD, 13));
        
        filterbutton.setFocusPainted(false);
        filterbutton.setBorder(BorderFactory.createEmptyBorder(9, 12, 9, 12));
        filterbutton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        

        filterPanel.add(filterbutton);
        
        // Table panel (load from DB)
        String[] columns = {
            "Chọn", "Tên sản phẩm", "Mã SP", "Loại", "Đơn vị", "Tồn kho"
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
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        
        // Load data from DB
        loaddata(model);
        
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Main content layout
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(topBar, BorderLayout.NORTH);
        JPanel statandfilterJPanel = new JPanel(new BorderLayout());
        statandfilterJPanel.add(statsPanel, BorderLayout.NORTH);
        statandfilterJPanel.add(filterPanel, BorderLayout.CENTER);
        centerPanel.add(tableScroll, BorderLayout.SOUTH);
        centerPanel.add(statandfilterJPanel, BorderLayout.CENTER);
        
        
        addBtn.addActionListener(e -> {
            JTextField tenSPField = new RoundedTextField(15);
            JTextField loaiSPField = new RoundedTextField(15);
            JTextField donViTinhField = new RoundedTextField(15);
            JTextField tonKhoField = new RoundedTextField(15);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Tên sản phẩm:"));
            panel.add(tenSPField);
            panel.add(new JLabel("Loại sản phẩm:"));
            panel.add(loaiSPField);
            panel.add(new JLabel("Đơn vị tính:"));
            panel.add(donViTinhField);
            panel.add(new JLabel("Tồn kho:"));
            panel.add(tonKhoField);

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

            Object[] options = {okBtn, cancelBtn};

            JDialog dialog = new JDialog((Frame) null, "Thêm sản phẩm mới", true);
            dialog.setLayout(new BorderLayout());
            dialog.add(panel, BorderLayout.CENTER);

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);
            dialog.add(btnPanel, BorderLayout.SOUTH);

            dialog.setSize(350, 380);
            dialog.setLocationRelativeTo(null);

            okBtn.addActionListener(ev -> {
                try {
                    SanPham sp = new SanPham();
                    sp.setTenSP(tenSPField.getText().trim());
                    sp.setLoaiSP(loaiSPField.getText().trim());
                    sp.setDonViTinh(donViTinhField.getText().trim());
                    sp.setTonKho(Integer.parseInt(tonKhoField.getText().trim()));

                    SanPhamDAO dao = new SanPhamDAO();
                    boolean success = dao.themSanPham(sp);
                    if (success) {
                        loaddata(model);
                        JOptionPane.showMessageDialog(dialog, "Thêm sản phẩm thành công!");
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Thêm sản phẩm thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ hoặc lỗi khi thêm sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelBtn.addActionListener(ev -> dialog.dispose());

            dialog.setVisible(true);
        });
        detailButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn một sản phẩm để xem chi tiết!");
                return;
            }
            // Lấy dữ liệu từ dòng được chọn
            String tenSP = model.getValueAt(selectedRow, 1).toString();
            String maSP = model.getValueAt(selectedRow, 2).toString();
            String loaiSP = model.getValueAt(selectedRow, 3).toString();
            String donViTinh = model.getValueAt(selectedRow, 4).toString();
            String tonKho = model.getValueAt(selectedRow, 5).toString();

            // Tạo các trường nhập liệu
            JTextField tenSPField = new RoundedTextField(15);
            tenSPField.setText(tenSP);
            tenSPField.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            JTextField maSPField = new RoundedTextField(15);
            maSPField.setText(maSP);
            maSPField.setEditable(false); // Không cho sửa mã sản phẩm
            JTextField loaiSPField = new RoundedTextField(15);
            loaiSPField.setText(loaiSP);
            JTextField donViTinhField = new RoundedTextField(15);
            donViTinhField.setText(donViTinh);
            JTextField tonKhoField = new RoundedTextField(15);
            tonKhoField.setText(tonKho);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Tên sản phẩm:"));
            panel.add(tenSPField);
            panel.add(new JLabel("Mã sản phẩm:"));
            panel.add(maSPField);
            panel.add(new JLabel("Loại sản phẩm:"));
            panel.add(loaiSPField);
            panel.add(new JLabel("Đơn vị tính:"));
            panel.add(donViTinhField);
            panel.add(new JLabel("Tồn kho:"));
            panel.add(tonKhoField);

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

            JDialog dialog = new JDialog((Frame) null, "Chi tiết sản phẩm", true);
            dialog.setLayout(new BorderLayout());
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            mainPanel.add(panel, BorderLayout.CENTER);
            mainPanel.add(btnPanel, BorderLayout.SOUTH);
            dialog.add(mainPanel);
            dialog.setSize(400, 490);
            dialog.setLocationRelativeTo(null);

            // Xử lý cập nhật
            updateBtn.addActionListener(ev -> {
                try {
                    SanPham sp = new SanPham();
                    sp.setMaSP(maSPField.getText().trim());
                    sp.setTenSP(tenSPField.getText().trim());
                    sp.setLoaiSP(loaiSPField.getText().trim());
                    sp.setDonViTinh(donViTinhField.getText().trim());
                    sp.setTonKho(Integer.parseInt(tonKhoField.getText().trim()));

                    SanPhamDAO dao = new SanPhamDAO();
                    boolean success = dao.suaSanPham(sp);
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
                int confirm = JOptionPane.showConfirmDialog(dialog, "Bạn có chắc muốn xóa sản phẩm này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        SanPhamDAO dao = new SanPhamDAO();
                        boolean success = dao.xoaSanPham(maSPField.getText().trim());
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
    private static JPanel createStatBox(String value, String label) {
        JPanel panel = new RoundedPanel(20); // Provide a suitable int value for the constructor
        panel.setBackground(new Color(245, 245, 245));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 22));
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel labelLabel = new JLabel(label, SwingConstants.CENTER);
        labelLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        labelLabel.setForeground(Color.GRAY);
        labelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createVerticalGlue());
        panel.add(valueLabel);
        panel.add(labelLabel);
        panel.add(Box.createVerticalGlue());
        panel.setPreferredSize(new Dimension(80, 120));
        return panel;
    }
    private static void loaddata(DefaultTableModel model){
        model.setRowCount(0);
        SanPhamDAO sanPhamDAO = new SanPhamDAO();
        List<SanPham> ds = sanPhamDAO.getAllSanPham();
        for (SanPham sp : ds) {
            model.addRow(new Object[]{
                false,
                sp.getTenSP(),
                sp.getMaSP(),
                sp.getLoaiSP(),
                sp.getDonViTinh(),
                sp.getTonKho()
            });
        }
    }
}
