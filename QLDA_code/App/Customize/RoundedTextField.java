package Customize;
import java.awt.*;
import javax.swing.*;

public class RoundedTextField extends JTextField {
    private int cornerRadius = 35;

    public RoundedTextField(int columns) {
        super(columns);
        setOpaque(false); // để có thể vẽ custom background
        setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // padding bên trong
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Khử răng cưa
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ nền bo góc
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Vẽ border bo góc
        g2.setColor(new Color(200, 200, 200)); // màu viền
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius);

        g2.dispose();

        // Vẽ phần text như bình thường
        super.paintComponent(g);
    }
}
