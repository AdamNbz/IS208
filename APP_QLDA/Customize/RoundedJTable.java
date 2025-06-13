package Customize;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class RoundedJTable extends JTable {
    private int cornerRadius = 20;

    // Constructor dùng Object[][] và Object[] (như Default JTable)
    public RoundedJTable(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
        applyStyle();
    }

    // Constructor dùng TableModel
    public RoundedJTable(TableModel model) {
        super(model);
        applyStyle();
    }

    // Cài đặt giao diện hiện đại
    private void applyStyle() {
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setRowHeight(36);
        setShowHorizontalLines(true);
        setShowVerticalLines(false);
        setGridColor(new Color(230, 230, 230));
        setSelectionBackground(new Color(33, 150, 243));
        setSelectionForeground(Color.WHITE);
        setBackground(Color.WHITE);
        setForeground(Color.DARK_GRAY);
        setFillsViewportHeight(true);
        setOpaque(false);

        // Header
        JTableHeader header = getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setBackground(new Color(63, 81, 181));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        header.setOpaque(true);

        // Striped rows
        setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(245, 245, 245) : Color.WHITE);
                }
                return c;
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);
        g2.dispose();
        super.paintComponent(g);
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }
}
