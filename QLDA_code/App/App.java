import javax.swing.JFrame;

public class App {
    public static void main(String[] args) {
        //LoginPanel.createMainPanel();
        JFrame frame = new JFrame("Quản lý kho hàng");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(ManagementPanel.createProductManagementPanel());
        frame.setVisible(true);
    }
}
