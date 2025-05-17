import java.awt.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.table.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProductManagementScreen().setVisible(true));
    }
}

class ProductManagementScreen extends JFrame {
    public ProductManagementScreen() {
        setTitle("Quan ly kho hang");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);

        // Layout chính: chia 2 phần sidebar và main
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(33, 150, 243));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        JLabel logo = new JLabel("Vuon Sao Bang", SwingConstants.CENTER);
        logo.setFont(new Font("Arial", Font.BOLD, 22));
        logo.setForeground(Color.WHITE);
        logo.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logo);

        String[] menuItems = {
            "SanPham", "Nhap kho", "Xuat kho", "Kiem ke", "Bao cao",
            "Quan ly nguoi dung", "Dang xuat"
        };
        for (String item : menuItems) {
            JButton btn = new JButton(item);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            btn.setFocusPainted(false);
            btn.setBackground(item.equals("San pham") ? new Color(21, 101, 192) : new Color(33, 150, 243));
            btn.setForeground(Color.WHITE);
            btn.setBorderPainted(false);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            sidebar.add(btn);
        }

        sidebar.add(Box.createVerticalGlue());

        // User info
        JPanel userPanel = new JPanel();
        userPanel.setBackground(new Color(33, 150, 243));
        userPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel avatar = new JLabel(new ImageIcon(new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB)));
        JLabel userName = new JLabel("Admin");
        userName.setForeground(Color.WHITE);
        userPanel.add(avatar);
        userPanel.add(userName);
        sidebar.add(userPanel);

        add(sidebar, BorderLayout.WEST);

        // Main content
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Top bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel title = new JLabel("Products");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        topBar.add(title, BorderLayout.WEST);

        JPanel topRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        JTextField searchField = new JTextField("Search here", 20);
        JButton addBtn = new JButton("+ Add New Product");
        addBtn.setBackground(new Color(33, 150, 243));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setFont(new Font("Arial", Font.BOLD, 13));
        topRight.add(searchField);
        topRight.add(addBtn);
        topBar.add(topRight, BorderLayout.EAST);

        mainPanel.add(topBar, BorderLayout.NORTH);

        // Stats panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        statsPanel.setBackground(Color.WHITE);

        statsPanel.add(createStatBox("11,207", "Total products"));
        statsPanel.add(createStatBox("09,107", "Total Sales Products"));
        statsPanel.add(createStatBox("1997", "Available Products"));
        statsPanel.add(createStatBox("103", "Return products"));

        mainPanel.add(statsPanel, BorderLayout.CENTER);

        // Filter panel
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        filterPanel.setBackground(Color.WHITE);

        filterPanel.add(new JComboBox<>(new String[]{"Show: All Products", "T-shirt", "Hoodie"}));
        filterPanel.add(new JComboBox<>(new String[]{"Show Price: All", "$0-$50", "$50-$100", "$100-$120"}));
        filterPanel.add(new JComboBox<>(new String[]{"Show: All Status", "Active", "Inactive"}));
        filterPanel.add(new JButton("Sort by"));
        filterPanel.add(new JButton("Filters"));

        mainPanel.add(filterPanel, BorderLayout.SOUTH);

        // Table panel
        String[] columns = {
            "", "Product List", "ID Number", "Last Uploaded", "In Stocks", "Variation", "Status", "Price", "Action"
        };
        Object[][] data = {
            {false, "Droop Shoulder T-shirt", "GD36457", "24 Jan 2025", "50/100", "005", true, "$120.50", "..."},
            {false, "T-shirt Slim-fit", "GD36457", "24 Jan 2025", "50/100", "005", true, "$120.50", "..."},
            {false, "Winter Hoodie", "GD36457", "24 Jan 2025", "50/100", "005", true, "$120.50", "..."},
            {false, "Casual Hoodie", "GD36457", "24 Jan 2025", "50/100", "005", true, "$120.50", "..."},
            {false, "Printed Hoodie", "GD36457", "24 Jan 2025", "50/100", "005", false, "$120.50", "..."},
            {false, "Hoodie Slim-fit", "GD36457", "24 Jan 2025", "50/100", "005", true, "$120.50", "..."},
            {false, "Winter Sweet Hoodie", "GD36457", "24 Jan 2025", "50/100", "005", true, "$120.50", "..."},
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) return Boolean.class;
                if (column == 6) return Boolean.class;
                return String.class;
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0 || column == 6;
            }
        };
        JTable table = new JTable(model);
        table.setRowHeight(40);
        table.getColumnModel().getColumn(0).setPreferredWidth(30);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Main content layout
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(statsPanel, BorderLayout.NORTH);
        centerPanel.add(filterPanel, BorderLayout.CENTER);
        centerPanel.add(tableScroll, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createStatBox(String value, String label) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
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
        return panel;
    }
}
