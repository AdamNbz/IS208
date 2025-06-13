
import Customize.RoundedButton;
import Customize.RoundedComboBox;
import Customize.RoundedPanel;
import DBFunction.PhieuNhapXuatDAO;
import DBFunction.SanPhamDAO;
import TempClass.SanPham;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

public class ReportForManagerPanel {
    private static JPanel statsPanel;
    private static JPanel chartsPanel;
    private static ChartPanel categoryChartPanel;
    private static ChartPanel movementChartPanel;

    public static JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Reports & Analytics");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topBar.add(title, BorderLayout.WEST);

        // Date range panel
        JPanel dateRangePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JComboBox<String> periodBox = new RoundedComboBox(new String[]{"Today", "This Week", "This Month", "This Year", "All Time"});
        //JComboBox<String> periodCombo = new JComboBox<>(new String[]{"Today", "This Week", "This Month", "This Year", "All Time"});
        dateRangePanel.add(new JLabel("Period:"));
        dateRangePanel.add(periodBox);
        topBar.add(dateRangePanel, BorderLayout.EAST);

        // Initialize panels
        statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        chartsPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        chartsPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Initial data load
        refreshData();

        // Add components to main panel
        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(statsPanel, BorderLayout.CENTER);
        mainPanel.add(chartsPanel, BorderLayout.CENTER);

        // Period combo box action listener
        // periodCombo.addActionListener(e -> {
        //     String selectedPeriod = (String) periodCombo.getSelectedItem();
        //     refreshData();
        // });
        periodBox.addActionListener(e -> {
            String selectedPeriod = (String) periodBox.getSelectedItem();
            // Here you can implement logic to filter data based on the selected period
            // For now, we will just refresh the data
            refreshData();
        });

        return mainPanel;
    }

    public static void refreshData() {
        // Clear existing components
        statsPanel.removeAll();
        chartsPanel.removeAll();

        // Get updated statistics
        int totalProducts = SanPhamDAO.getSoLuongSanPham();
        int totalStock = SanPhamDAO.getSoLuongtlkSanPham();
        int totalImports = PhieuNhapXuatDAO.getsoluongphieunhap();
        int totalExports = PhieuNhapXuatDAO.getsoluongphieuxuat();

        // Update stats panel
        statsPanel.add(createStatBox(String.valueOf(totalProducts), "Total Product Types"));
        statsPanel.add(createStatBox(String.valueOf(totalStock), "Total Stock"));
        statsPanel.add(createStatBox(String.valueOf(totalImports), "Total Imports"));
        statsPanel.add(createStatBox(String.valueOf(totalExports), "Total Exports"));

        // Update category chart
        DefaultPieDataset categoryDataset = new DefaultPieDataset();
        try {
            ArrayList<String> categories = SanPhamDAO.getDanhSachLoaiSanPham();
            for (String category : categories) {
                List<SanPham> products = new SanPhamDAO().getSanPhamTheoLoai(category);
                int total = 0;
                for (SanPham sp : products) {
                    total += sp.getTonKho();
                }
                categoryDataset.setValue(category, total);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFreeChart categoryChart = ChartFactory.createPieChart(
            "Product Category Distribution",
            categoryDataset,
            true,
            true,
            false
        );
        categoryChartPanel = new ChartPanel(categoryChart);
        categoryChartPanel.setPreferredSize(new Dimension(400, 300));

        // Update movement chart
        DefaultCategoryDataset movementDataset = new DefaultCategoryDataset();
        movementDataset.addValue(totalImports, "Imports", "Total");
        movementDataset.addValue(totalExports, "Exports", "Total");

        JFreeChart movementChart = ChartFactory.createBarChart(
            "Stock Movement",
            "Type",
            "Quantity",
            movementDataset,
            org.jfree.chart.plot.PlotOrientation.VERTICAL,
            true,
            true,
            false
        );
        movementChartPanel = new ChartPanel(movementChart);
        movementChartPanel.setPreferredSize(new Dimension(400, 300));

        // Add updated charts
        chartsPanel.add(categoryChartPanel);
        chartsPanel.add(movementChartPanel);

        // Refresh the panels
        statsPanel.revalidate();
        statsPanel.repaint();
        chartsPanel.revalidate();
        chartsPanel.repaint();
    }

    private static JPanel createStatBox(String value, String label) {
        JPanel panel = new RoundedPanel(20);
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
        
        panel.setPreferredSize(new Dimension(200, 120));
        return panel;
    }
} 