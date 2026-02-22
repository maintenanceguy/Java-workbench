import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Registration {

    private JFrame frame;
    private JPanel card;

    private JTextField nameField, emailField;
    private JPasswordField passwordField;
    private JComboBox<String> genderBox;

    private static final String FILE_NAME = "data/Customer_Data.txt";

    // 🎨 Theme Colors
    private final Color BG = new Color(18,12,35);
    private final Color CARD = new Color(40,28,75);
    private final Color BTN = new Color(130,90,255);
    private final Color BTN_HOVER = new Color(170,130,255);
    private final Color TEXT = Color.WHITE;

    public Registration() {

        frame = new JFrame("Lost City Books — Register");
        frame.setSize(520, 440);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(BG);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        card = new JPanel(null);
        card.setBounds(40, 30, 440, 360);
        card.setBackground(CARD);
        frame.add(card);

        // 🖼️ Logo (left of title)
        ImageIcon icon = new ImageIcon("src/images/logo.png");
        Image img = icon.getImage().getScaledInstance(42, 42, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(img));
        logo.setBounds(95, 15, 42, 42);
        card.add(logo);

        // 🏷️ Title
        JLabel title = new JLabel("Create Account");
        title.setBounds(145, 15, 260, 42);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(TEXT);
        card.add(title);

        int y = 80;

        nameField = createField("Name", y);       y += 55;
        emailField = createField("Email", y);     y += 55;

        JLabel passLabel = createLabel("Password", 40, y);
        card.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(40, y+22, 360, 32);
        styleField(passwordField);
        card.add(passwordField);
        y += 60;

        JLabel genderLabel = createLabel("Gender", 40, y);
        card.add(genderLabel);

        genderBox = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        genderBox.setBounds(40, y+22, 360, 32);
        card.add(genderBox);
        y += 65;

        JButton registerBtn = createButton("Register", 40, y);
        JButton loginBtn = createButton("Back to Login", 220, y);

        registerBtn.addActionListener(e -> register());
        loginBtn.addActionListener(e -> {
            frame.dispose();
            new Login();
        });

        card.add(registerBtn);
        card.add(loginBtn);

        frame.setVisible(true);
    }

    private JTextField createField(String label, int y) {
        JLabel lbl = createLabel(label, 40, y);
        card.add(lbl);

        JTextField txt = new JTextField();
        txt.setBounds(40, y+22, 360, 32);
        styleField(txt);
        card.add(txt);

        return txt;
    }

    private JLabel createLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 200, 22);
        lbl.setForeground(TEXT);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return lbl;
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
        btn.setBounds(x, y, 170, 38);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(BTN);
        btn.setForeground(TEXT);
        btn.setFocusPainted(false);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(BTN_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(BTN);
            }
        });
        return btn;
    }

    // ✅ Input validation
    private void register() {

        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "⚠️ Please fill all fields before registering.");
            return;
        }

        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {

            writer.write("Name : " + name + ", ");
            writer.write("Email : " + email + ", ");
            writer.write("Password : " + password + ", ");
            writer.write("Gender : " + genderBox.getSelectedItem() + "\n");

            JOptionPane.showMessageDialog(frame, "✅ Registration Successful!");
            frame.dispose();
            new Login();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "File error.");
        }
    }
}
