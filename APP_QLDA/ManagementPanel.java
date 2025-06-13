import java.awt.*;
import javax.swing.*;

public class ManagementPanel {
    public static JPanel createManagementPanel(String Mand) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(33, 150, 243));
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("Vuon Sao Bang", SwingConstants.CENTER);
        logo.setIcon(resizeIcon("Icon/logo.png", 60, 60));
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 22));
        logo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logo);
        JButton productButton = new JButton("San Pham");
        //productButton.setIcon( new ImageIcon(scaled_product_Image));
        productButton.setIcon(resizeIcon("Icon/product.png", 24, 24));

        sidebar.add(productButton);

        // Nut import
        JButton inportButton = new JButton("Nhap kho");
        inportButton.setIcon(resizeIcon("Icon/plus.png", 24, 24));
        sidebar.add(inportButton);
        
        // Nút export
        JButton exportButton = new JButton("Xuất kho");
        exportButton.setIcon(resizeIcon("Icon/cross.png", 24, 24));
        sidebar.add(exportButton);
        // Tùy chọn thêm: hover effect (dùng thêm MouseListener nếu muốn)

        JButton supplierButton = new JButton("Nhà cung cấp");
        supplierButton.setIcon(resizeIcon("Icon/supplier.png", 24, 24));
        sidebar.add(supplierButton);

        JButton typeButton = new JButton("Loại sản phẩm");
        typeButton.setIcon(resizeIcon("Icon/boxes.png", 24, 24));
        sidebar.add(typeButton);

        JButton reportButton = new JButton("Bao cao");
        reportButton.setIcon(resizeIcon("Icon/chart.png", 24, 24));
        sidebar.add(reportButton);
        
        JButton statsButton = new JButton("Thống kê");
        statsButton.setIcon(resizeIcon("Icon/stats.png", 24, 24));
        sidebar.add(statsButton);
        
        JButton userButton = new JButton("Quan ly nguoi dung");
        userButton.setIcon(resizeIcon("Icon/user.png", 24, 24));
        sidebar.add(userButton);

        JButton logoutButton = new JButton("Đăng xuất");
        logoutButton.setIcon(resizeIcon("Icon/exit.png", 24, 24));
        sidebar.add(logoutButton);

        

        styleSidebarButton(productButton);
        styleSidebarButton(inportButton);
        styleSidebarButton(supplierButton);
        styleSidebarButton(typeButton);
        styleSidebarButton(exportButton);
        styleSidebarButton(reportButton);
        styleSidebarButton(statsButton);
        styleSidebarButton(userButton);
        styleSidebarButton(logoutButton);
        sidebar.add(Box.createVerticalGlue());
        
        mainPanel.add(sidebar, BorderLayout.WEST);
        JPanel centerPanel = new JPanel(new CardLayout());
        JPanel productPanel = ProductForManagerPanel.createMainPanel();
        JPanel importPanel = ImportGoodForManagerPanel.createMainPanel();
        JPanel exportJPanel = ExportGoodForManagerPanel.createMainPanel();
        JPanel userJPanel = UserForManagerPanel.createMainPanel();
        JPanel statsJPanel = StatisticForManagerPanel.createMainPanel();
        JPanel reportPanel = ReportForManagerPanel.createMainPanel();
        JPanel supplierPanel = SupplierForManagerPanel.createMainPanel();
        JPanel typePanel = ProductTypeManagerPanel.createMainPanel();

        centerPanel.add(productPanel,"Product");
        centerPanel.add(importPanel,"Import");
        centerPanel.add(exportJPanel, "Export");
        centerPanel.add(typePanel, "Type");
        centerPanel.add(userJPanel,"User");
        centerPanel.add(statsJPanel, "Statistic");
        centerPanel.add(reportPanel, "Report");
        centerPanel.add(supplierPanel, "Supplier");

        productButton.addActionListener(e -> switchPanel(centerPanel, "Product"));
        inportButton.addActionListener(e -> switchPanel(centerPanel, "Import"));
        exportButton.addActionListener(e -> switchPanel(centerPanel, "Export"));
        userButton.addActionListener(e -> switchPanel(centerPanel, "User"));
        reportButton.addActionListener(e -> switchPanel(centerPanel, "Report"));
        statsButton.addActionListener(e -> switchPanel(centerPanel, "Statistic"));
        supplierButton.addActionListener(e -> switchPanel(centerPanel, "Supplier"));
        typeButton.addActionListener(e -> switchPanel(centerPanel, "Type"));
        logoutButton.addActionListener(e -> {
            LoginPanel.setMainContent();
        });
        mainPanel.add(centerPanel);
        return mainPanel;
    }
    private static void switchPanel(JPanel panel, String panelName) {
        CardLayout layout = (CardLayout) panel.getLayout();
        layout.show(panel, panelName);
    }
    private static void styleSidebarButton(JButton button) {
        button.setPreferredSize(new Dimension(280, 45));
        button.setMaximumSize(new Dimension(280, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(new Color(33, 150, 243)); // Blue 600
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setIconTextGap(10);                      // Khoảng cách giữa icon và chữ
    }
    public static ImageIcon resizeIcon(String path, int width, int height) {
    ImageIcon icon = new ImageIcon(path);
    Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    return new ImageIcon(img);
}
}
