import Customize.RoundedButton;
import Customize.RoundedTextField;
import DBFunction.ChiTietNhapXuatDAO;
import DBFunction.PhieuNhapXuatDAO;
import TempClass.ChiTietNhapXuat;
import TempClass.PhieuNhapXuat;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
public class ImportGoodForManagerPanel {
    public static JPanel createMainPanel() {
        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel title = new JLabel("Phiếu Nhập Kho");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topBar.add(title, BorderLayout.WEST);

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JTextField searchField = new RoundedTextField( 20);
        searchField.setText("Search here..");
        JButton addBtn = new RoundedButton("Kiểm tra phiếu");
        addBtn.setBackground(new Color(33, 150, 243));
        addBtn.setFocusPainted(false);
        addBtn.setFont(new Font("Arial", Font.BOLD, 13));

        addBtn.setFocusPainted(false);
        addBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        

        topRight.add(searchField);
        topRight.add(addBtn);
        topBar.add(topRight, BorderLayout.EAST);
        
        // Table columns
        String[] columns = {
            "Chọn", "Mã Phiếu", "Ngày Lập", "Người Lập", "Ghi Chú","Trạng Thái"
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
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn một phiếu nhập để xem chi tiết!");
                return;
            }
            String maPhieu = model.getValueAt(selectedRow, 1).toString();
            showDetailDialog(maPhieu, model);
        });
        return centerPanel;
    }
    private static void showDetailDialog(String maPhieu, DefaultTableModel oldmodel) {
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
        JButton addDetailBtn = new RoundedButton("+ Duyệt");
        addDetailBtn.setBackground(new Color(33, 150, 243));
        addDetailBtn.setForeground(Color.WHITE);
        addDetailBtn.setFont(new Font("Arial", Font.BOLD, 13));
        
        addDetailBtn.setFocusPainted(false);
        addDetailBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        addDetailBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        

        addDetailBtn.addActionListener(ev -> {
            try {
                boolean ok = PhieuNhapXuatDAO.duyetPhieu(maPhieu);
                if (ok) {
                    JOptionPane.showMessageDialog(dialog, "Đã duyệt phiếu thành công!");
                    dialog.dispose();
                    loaddata(oldmodel);
                } else {
                    JOptionPane.showMessageDialog(dialog, "Duyệt phiếu thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Có lỗi xảy ra!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.add(addDetailBtn);

        dialog.setLayout(new BorderLayout());
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
    private static void loaddata(DefaultTableModel model){
        model.setRowCount(0);
        try {
            PhieuNhapXuatDAO dao = new PhieuNhapXuatDAO(null);
            List<PhieuNhapXuat> ds = dao.getPhieuTheoLoai("NHAP");
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