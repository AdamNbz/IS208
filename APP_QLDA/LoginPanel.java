import Customize.RoundedButton;
import Customize.RoundedPasswordField;
import Customize.RoundedTextField;
import DBFunction.NguoiDungDAO;
import java.awt.*;
import javax.swing.*;

public class LoginPanel {
    private static JFrame frame;
    public static void createMainPanel() {
        frame = new JFrame("Quản lý kho hàng");
        JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Tạo panel chứa ảnh và chữ Welcome back!
        ImageIcon icon = new ImageIcon("Icon/loginback.jpg"); // đường dẫn ảnh
        JLabel imageLabel = new JLabel(icon);
        imageLabel.setLayout(new BorderLayout());


        mainPanel.add(imageLabel, BorderLayout.WEST);
        mainPanel.add(createLoginPanel(), BorderLayout.EAST);

        frame.add(mainPanel);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1120, 780);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);        
    }
    public static void setMainContent() {
        if (frame != null) {
            frame.getContentPane().removeAll();
            frame.revalidate();
            frame.repaint();
            frame.dispose();
            createMainPanel();
        }
    }
    public static JPanel createLoginPanel() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setPreferredSize(new Dimension(350,420));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Thêm tiêu đề "Login"
    JLabel titleLabel = new JLabel("Login");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
    panel.add(titleLabel, gbc);

    gbc.gridwidth = 1;
    JLabel userLabel = new JLabel("Tên đăng nhập:");
    JTextField usernameField = new RoundedTextField(10);
    JLabel passLabel = new JLabel("Mật khẩu:");
    JPasswordField passwordField = new RoundedPasswordField(10);

    JButton loginBtn = new RoundedButton("Đăng nhập");
    loginBtn.setBackground(new Color(33, 150, 243));
    loginBtn.setForeground(Color.WHITE);
    loginBtn.setFocusPainted(false);
    loginBtn.setFont(new Font("Arial", Font.BOLD, 13));
    loginBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    loginBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

    gbc.gridx = 0; gbc.gridy = 1;
    panel.add(userLabel, gbc);
    gbc.gridx = 1; gbc.gridy = 1;
    panel.add(usernameField, gbc);
    gbc.gridx = 0; gbc.gridy = 2;
    panel.add(passLabel, gbc);
    gbc.gridx = 1; gbc.gridy = 2;
    panel.add(passwordField, gbc);
    gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
    panel.add(loginBtn, gbc);

    loginBtn.addActionListener(e -> {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        NguoiDungDAO dao = new NguoiDungDAO();
        String role = dao.kiemtraNguoiDung(username, password);

        if (role != null && !role.isEmpty()) {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(panel);
            if (frame != null) {
                if (role.equals("QLK")) frame.setContentPane(ManagementPanel.createManagementPanel(dao.kiemtramandNguoiDung(username)));
                else if (role.equals("NK")) frame.setContentPane(ImportGoodEmployeePanel.createMainPanel(dao.kiemtramandNguoiDung(username)));
                else if (role.equals("XK")) frame.setContentPane(ExportGoodEmployeePanel.createMainPanel(dao.kiemtramandNguoiDung(username)));
                frame.revalidate();
                frame.repaint();
            }
        } else {
            JOptionPane.showMessageDialog(panel, "Sai tên đăng nhập hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    });

    return panel;
}
}