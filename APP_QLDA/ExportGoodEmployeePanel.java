import Customize.RoundedButton;
import Customize.RoundedComboBox;
import Customize.RoundedTextField;
import DBFunction.*;
import TempClass.*;
import java.awt.*;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
public class ExportGoodEmployeePanel {
    public static JPanel createMainPanel(String mand) {
        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel title = new JLabel("Phiếu Xuất Kho");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topBar.add(title, BorderLayout.WEST);

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JTextField searchField = new RoundedTextField( 20);
        searchField.setText("Search here..");
        JButton addBtn = new RoundedButton("+ Thêm Phiếu Xuất");
        addBtn.setBackground(new Color(33, 150, 243));
        addBtn.setFocusPainted(false);
        addBtn.setFont(new Font("Arial", Font.BOLD, 13));
        addBtn.setPreferredSize(new Dimension(200, 40));

        JButton detailButton = new RoundedButton("Add Detail");
        detailButton.setBackground(new Color(33, 150, 243));
        detailButton.setFocusPainted(false);
        detailButton.setFont(new Font("Arial", Font.BOLD, 13));
        detailButton.setPreferredSize(new Dimension(200, 40));

        JButton LogoutButton = new RoundedButton("Logout");
        LogoutButton.setBackground(new Color(220, 53, 69));
        LogoutButton.setFocusPainted(false);
        LogoutButton.setFont(new Font("Arial", Font.BOLD, 13));
        LogoutButton.setPreferredSize(new Dimension(200, 40));
        LogoutButton.addActionListener(e -> {
            LoginPanel.setMainContent();
        });

        topRight.add(searchField);
        topRight.add(LogoutButton);
        topBar.add(topRight, BorderLayout.EAST);
        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomBar.add(addBtn);
        bottomBar.add(detailButton);
        // Table columns
        String[] columns = {
            "Chọn", "Mã Phiếu", "Ngày Lập", "Người Lập", "Ghi Chú", "Trạng Thái"
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

        // Add listener for addBtn
        addBtn.addActionListener(e -> {
            JTextField ngayLapField = new RoundedTextField(20);
            JTextField ghiChuField = new RoundedTextField(20);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Ngày Lập (yyyy-MM-dd):"));
            panel.add(ngayLapField);
            panel.add(new JLabel("Ghi Chú:"));
            panel.add(ghiChuField);

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

            JDialog dialog = new JDialog((Frame) null, "Thêm Phiếu Xuất Mới", true);
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
                    PhieuNhapXuat px = new PhieuNhapXuat();
                    px.setLoai("XUAT");
                    java.util.Date utilDate = java.sql.Date.valueOf(ngayLapField.getText().trim());
                    px.setNgayLap(utilDate);
                    px.setMaND(mand);
                    px.setGhiChu(ghiChuField.getText().trim());

                    PhieuNhapXuatDAO dao = new PhieuNhapXuatDAO(null);
                    boolean success = dao.themPhieu(px);
                    if (success) {
                        loaddata(model);
                        JOptionPane.showMessageDialog(dialog, "Thêm phiếu xuất thành công!");
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Thêm phiếu xuất thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ hoặc lỗi khi thêm phiếu xuất!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelBtn.addActionListener(ev -> dialog.dispose());

            dialog.setVisible(true);
        });
        detailButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn một phiếu xuất để xem chi tiết!");
                return;
            }
            String maPhieu = model.getValueAt(selectedRow, 1).toString();
            showDetailDialog(maPhieu);
        });
        // Load data from DB
        loaddata(model);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Main content layout
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(topBar, BorderLayout.NORTH);
        centerPanel.add(tableScroll, BorderLayout.CENTER);
        centerPanel.add(bottomBar, BorderLayout.SOUTH);

        return centerPanel;
    }
    private static void loaddata(DefaultTableModel model){
        model.setRowCount(0);
        try {
            PhieuNhapXuatDAO dao = new PhieuNhapXuatDAO(null);
            ArrayList<PhieuNhapXuat> ds = dao.getPhieuTheoLoai("XUAT");
            for (PhieuNhapXuat px : ds) {
                model.addRow(new Object[]{
                    false,
                    px.getMaPhieu(),
                    px.getNgayLap(),
                    px.getMaND(),
                    px.getGhiChu(),
                    px.gettrangthai()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void showDetailDialog(String maPhieu) {
        JDialog dialog = new JDialog((Frame) null, "Chi tiết phiếu xuất: " + maPhieu, true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(null);

        // Table columns
        String[] columns = {"Mã Phiếu", "Mã SP", "Số lượng"};
        DefaultTableModel detailModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable detailTable = new JTable(detailModel);
        detailTable.setRowHeight(30);

        // Load data
        loadDetailData(maPhieu, detailModel);

        JScrollPane scrollPane = new JScrollPane(detailTable);

        // Add new record button
        JButton addDetailBtn = new RoundedButton("+ Thêm chi tiết");
        addDetailBtn.setBackground(new Color(33, 150, 243));
        addDetailBtn.setForeground(Color.WHITE);
        addDetailBtn.setFocusPainted(false);
        addDetailBtn.setFont(new Font("Arial", Font.BOLD, 13));
        addDetailBtn.setPreferredSize(new Dimension(200,30));
        addDetailBtn.addActionListener(ev -> {
            // Lấy danh sách sản phẩm từ DB
            ArrayList<String> sanPhamList = new ArrayList<>();
            ArrayList<String> maSPList = new ArrayList<>();
            try {
                SanPhamDAO spDAO = new SanPhamDAO();
                for (SanPham sp : spDAO.getAllSanPham()) {
                    sanPhamList.add(sp.getMaSP() + " - " + sp.getTenSP());
                    maSPList.add(sp.getMaSP());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            JComboBox<String> maSPField = new RoundedComboBox(sanPhamList.toArray(new String[0]));
            JTextField soLuongField = new RoundedTextField(20);
            JTextField donGiaField = new RoundedTextField(20);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Mã Sản Phẩm:"));
            panel.add(maSPField);
            panel.add(new JLabel("Số lượng:"));
            panel.add(soLuongField);
            panel.add(new JLabel("Đơn giá:"));
            panel.add(donGiaField);

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

            JDialog detailDialog = new JDialog((Frame) null, "Thêm chi tiết phiếu xuất", true);
            detailDialog.setLayout(new BorderLayout());
            detailDialog.add(panel, BorderLayout.CENTER);

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);
            detailDialog.add(btnPanel, BorderLayout.SOUTH);

            detailDialog.setSize(350, 350);
            detailDialog.setLocationRelativeTo(null);

            okBtn.addActionListener(ev2 -> {
                try {
                    ChiTietNhapXuat ct = new ChiTietNhapXuat();
                    ct.setMaPhieu(maPhieu);
                    int selectedIndex = maSPField.getSelectedIndex();
                    if (selectedIndex >= 0 && selectedIndex < maSPList.size()) {
                        ct.setMaSP(maSPList.get(selectedIndex));
                    } else {
                        ct.setMaSP("");
                    }
                    ct.setSoLuong(Integer.parseInt(soLuongField.getText().trim()));

                    ChiTietNhapXuatDAO dao = new ChiTietNhapXuatDAO();
                    boolean success = dao.themChiTiet(ct);
                    if (success) {
                        loadDetailData(maPhieu, detailModel);
                        JOptionPane.showMessageDialog(detailDialog, "Thêm chi tiết thành công!");
                        detailDialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(detailDialog, "Thêm chi tiết thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(detailDialog, "Dữ liệu không hợp lệ hoặc lỗi khi thêm chi tiết!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelBtn.addActionListener(ev2 -> detailDialog.dispose());

            detailDialog.setVisible(true);
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(addDetailBtn);

        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // --- Hàm load dữ liệu chi tiết ---
    private static void loadDetailData(String maPhieu, DefaultTableModel detailModel) {
        detailModel.setRowCount(0);
        try {
            ChiTietNhapXuatDAO dao = new ChiTietNhapXuatDAO();
            ArrayList<ChiTietNhapXuat> ds = dao.getChiTietTheoPhieu(maPhieu);
            for (ChiTietNhapXuat ct : ds) {
                detailModel.addRow(new Object[]{
                    ct.getMaPhieu(),
                    ct.getMaSP(),
                    ct.getSoLuong()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
