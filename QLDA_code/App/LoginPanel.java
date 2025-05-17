import DBFunction.NguoiDungDAO;
import java.awt.*;
import javax.swing.*;

public class LoginPanel {
    public static void createMainPanel() {
        logindialog();
    }

    private static void logindialog() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(0, 1, 5, 5));
        panel.add(new JLabel("Tên đăng nhập:"));
        panel.add(usernameField);
        panel.add(new JLabel("Mật khẩu:"));
        panel.add(passwordField);

        int result;
        do {
            result = JOptionPane.showConfirmDialog(
                null, panel, "Đăng nhập", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());

                NguoiDungDAO dao = new NguoiDungDAO();
                String role = dao.kiemtraNguoiDung(username, password);

                if (role != null && !role.isEmpty()) {
                    // Đăng nhập thành công, mở giao diện chính
                    JFrame frame = new JFrame("Quản lý kho hàng");
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setSize(1200, 700);
                    frame.setLocationRelativeTo(null);
                    if (role.equals("QLK")) frame.setContentPane(ManagementPanel.createProductManagementPanel());
                    else if (role.equals("NK")) frame.setContentPane(ImportGoodEmployeePanel.createMainPanel()); 
                    else if (role.equals("XK")) frame.setContentPane(ExportGoodEmployeePanel.createMainPanel());
                    frame.setVisible(true);
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "Sai tên đăng nhập hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                System.exit(0);
            }
        } while (true);
    }
}