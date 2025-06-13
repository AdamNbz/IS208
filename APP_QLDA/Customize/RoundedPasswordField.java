package Customize;
import java.awt.*;
import javax.swing.*;

public class RoundedPasswordField extends JPasswordField {
    private int cornerRadius = 40;

    public RoundedPasswordField(int columns) {
        super(columns);
        setOpaque(false); // để vẽ nền tùy chỉnh
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // padding nội bộ
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(Color.DARK_GRAY);
        setBackground(Color.WHITE);
        setCaretColor(Color.BLACK);
    }

    public void setCornerRadius(int radius) {
        this.cornerRadius = radius;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Nền bo góc
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Viền bo góc
        g2.setColor(new Color(200, 200, 200));
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        g2.dispose();
        super.paintComponent(g);
    }
}
