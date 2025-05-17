package Customize;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class RoundedComboBox extends JComboBox<String> {
    private int cornerRadius = 40;

    public RoundedComboBox(String[] items) {
        super(items);
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(Color.DARK_GRAY);
        setBackground(Color.WHITE);
        setOpaque(false);
        setUI(new RoundedComboBoxUI());
        setBorder(new EmptyBorder(8, 12, 8, 12)); // padding
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    private class RoundedComboBoxUI extends BasicComboBoxUI {
        @Override
        protected JButton createArrowButton() {
            JButton arrowButton = new JButton("▼");
            arrowButton.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            arrowButton.setFocusable(false);
            arrowButton.setBorder(null);
            arrowButton.setBackground(Color.WHITE);
            arrowButton.setForeground(Color.GRAY);
            return arrowButton;
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            // Không vẽ nền khi chọn (để giữ bo góc)
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = c.getWidth();
            int height = c.getHeight();

            g2.setColor(c.getBackground());
            g2.fillRoundRect(0, 0, width, height, cornerRadius, cornerRadius);

            g2.setColor(new Color(200, 200, 200));
            g2.drawRoundRect(0, 0, width - 1, height - 1, cornerRadius, cornerRadius);

            g2.dispose();
            super.paint(g, c);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Let UI handle main rendering
    }
}
