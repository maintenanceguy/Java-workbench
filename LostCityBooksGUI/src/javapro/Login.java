import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Login implements ActionListener {

    private JFrame frame;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginBtn, registerBtn;

    private static final String FILE_NAME = "data/Customer_Data.txt";

    // 🎨 Theme Colors
    private final Color BG = new Color(18,12,35);
    private final Color CARD = new Color(40,28,75);
    private final Color BTN = new Color(130,90,255);
    private final Color BTN_HOVER = new Color(170,130,255);
    private final Color TEXT = Color.WHITE;

    public Login() {

        frame = new JFrame("Lost City Books — Login");
        frame.setSize(500, 360);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(BG);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel card = new JPanel(null);
        card.setBounds(40, 30, 420, 280);
        card.setBackground(CARD);
        frame.add(card);

        // 🖼️ Logo (left of title)
        ImageIcon icon = new ImageIcon("src/images/logo.png");
        Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(img));
        logo.setBounds(85, 15, 40, 40);
        card.add(logo);

        // 🏷️ Title
        JLabel title = new JLabel("Lost City Books Login");
        title.setBounds(135, 15, 240, 40);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT);
        card.add(title);

        JLabel emailLabel = createLabel("Email", 40, 70);
        card.add(emailLabel);

        emailField = createField(40, 95);
        card.add(emailField);

        JLabel passLabel = createLabel("Password", 40, 135);
        card.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(40, 160, 340, 32);
        styleField(passwordField);
        card.add(passwordField);

        loginBtn = createButton("Login", 40, 215);
        registerBtn = createButton("Register", 220, 215);

        card.add(loginBtn);
        card.add(registerBtn);

        frame.setVisible(true);
    }

    private JLabel createLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 200, 22);
        lbl.setForeground(TEXT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return lbl;
    }

    private JTextField createField(int x, int y) {
        JTextField txt = new JTextField();
        txt.setBounds(x, y, 340, 32);
        styleField(txt);
        return txt;
    }

    private void styleField(JTextField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBackground(new Color(30,20,60));
        f.setForeground(TEXT);
        f.setCaretColor(TEXT);
        f.setBorder(BorderFactory.createEmptyBorder(5,10,5,10));
    }

    private JButton createButton(String text, int x, int y) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 160, 38);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(BTN);
        btn.setForeground(TEXT);
        btn.setFocusPainted(false);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(BTN_HOVER);
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(BTN);
            }
        });

        btn.addActionListener(this);
        return btn;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginBtn) {
            login();
        } else {
            frame.dispose();
            new Registration();
        }
    }

    // ✅ Input validation
    private void login() {

        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "⚠️ Please enter email and password.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {

            String line;
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(",");
                String name = parts[0].split(":")[1].trim();
                String fileEmail = parts[1].split(":")[1].trim();
                String filePassword = parts[2].split(":")[1].trim();
                String gender = parts[3].split(":")[1].trim();

                if (fileEmail.equals(email) && filePassword.equals(password)) {
                    JOptionPane.showMessageDialog(frame, "✨ Welcome " + name);
                    frame.dispose();
                    new BookStore(new User(name, email, password, gender));
                    return;
                }
            }

            JOptionPane.showMessageDialog(frame, "❌ Invalid credentials.");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "User file not found.");
        }
    }
}
