import DBFunction.*;
import TempClass.*;
import java.awt.*;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
public class ImportGoodEmployeePanel {
    public static JPanel createMainPanel() {
        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel title = new JLabel("Phiếu Nhập Kho");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topBar.add(title, BorderLayout.WEST);

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JTextField searchField = new JTextField("Search here", 20);
        JButton addBtn = new JButton("+ Thêm Phiếu Nhập");
        addBtn.setBackground(new Color(33, 150, 243));
        addBtn.setFocusPainted(false);
        addBtn.setFont(new Font("Arial", Font.BOLD, 13));
        addBtn.setPreferredSize(new Dimension(200, 30));

        JButton detailButton = new JButton("Add Detail");
        detailButton.setBackground(new Color(33, 150, 243));
        detailButton.setFocusPainted(false);
        detailButton.setFont(new Font("Arial", Font.BOLD, 13));
        detailButton.setPreferredSize(new Dimension(200, 30));


        topRight.add(searchField);
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
            JTextField ngayLapField = new JTextField();
            JTextField maNDField = new JTextField();
            JTextField ghiChuField = new JTextField();

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Ngày Lập (yyyy-MM-dd):"));
            panel.add(ngayLapField);
            panel.add(new JLabel("Mã Người Lập:"));
            panel.add(maNDField);
            panel.add(new JLabel("Ghi Chú:"));
            panel.add(ghiChuField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Thêm Phiếu Nhập Mới",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    PhieuNhapXuat px = new PhieuNhapXuat();
                    px.setLoai("NHAP");
                    java.util.Date utilDate = java.sql.Date.valueOf(ngayLapField.getText().trim());
                    px.setNgayLap(utilDate);
                    px.setMaND(maNDField.getText().trim());
                    px.setGhiChu(ghiChuField.getText().trim());

                    PhieuNhapXuatDAO dao = new PhieuNhapXuatDAO(null);
                    boolean success = dao.themPhieu(px);
                    if (success) {
                        // Add to table
                        loaddata(model);
                        JOptionPane.showMessageDialog(null, "Thêm phiếu nhập thành công!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Thêm phiếu nhập thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Dữ liệu không hợp lệ hoặc lỗi khi thêm phiếu nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        detailButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn một phiếu nhập để xem chi tiết!");
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
            ArrayList<PhieuNhapXuat> ds = dao.getPhieuTheoLoai("NHAP");
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
        JDialog dialog = new JDialog((Frame) null, "Chi tiết phiếu nhập: " + maPhieu, true);
        dialog.setSize(700, 400);
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
        JButton addDetailBtn = new JButton("+ Thêm chi tiết");
        addDetailBtn.setBackground(new Color(33, 150, 243));
        addDetailBtn.setForeground(Color.WHITE);
        addDetailBtn.setFocusPainted(false);
        addDetailBtn.setFont(new Font("Arial", Font.BOLD, 13));
        addDetailBtn.addActionListener(ev -> {
            JTextField maSPField = new JTextField();
            JTextField soLuongField = new JTextField();
            JTextField donGiaField = new JTextField();

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Mã Sản Phẩm:"));
            panel.add(maSPField);
            panel.add(new JLabel("Số lượng:"));
            panel.add(soLuongField);
            panel.add(new JLabel("Đơn giá:"));
            panel.add(donGiaField);

            int result = JOptionPane.showConfirmDialog(dialog, panel, "Thêm chi tiết phiếu nhập",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    ChiTietNhapXuat ct = new ChiTietNhapXuat();
                    ct.setMaPhieu(maPhieu);
                    ct.setMaSP(maSPField.getText().trim());
                    ct.setSoLuong(Integer.parseInt(soLuongField.getText().trim()));

                    ChiTietNhapXuatDAO dao = new ChiTietNhapXuatDAO();
                    boolean success = dao.themChiTiet(ct);
                    if (success) {
                        loadDetailData(maPhieu, detailModel);
                        JOptionPane.showMessageDialog(dialog, "Thêm chi tiết thành công!");
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Thêm chi tiết thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(dialog, "Dữ liệu không hợp lệ hoặc lỗi khi thêm chi tiết!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
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
