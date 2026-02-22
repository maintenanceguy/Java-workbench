import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class BookStore {

    private JFrame frame;
    private JList<String> bookList;
    private DefaultListModel<String> model;
    private JTextField txtSearch;
    private JComboBox<String> cmbCategory;

    private JButton btnAdd, btnCart, btnTotal, btnPurchase;

    private User user;
    private Cart cart = new Cart();

    private ArrayList<String> allBooks = new ArrayList<>();

    // 🎨 Dark Theme Colors
    private final Color BG = new Color(18, 12, 35);
    private final Color CARD = new Color(40, 28, 75);
    private final Color BTN = new Color(130, 90, 255);
    private final Color BTN_HOVER = new Color(170, 130, 255);
    private final Color TEXT = Color.WHITE;

    public BookStore(User user) {
        this.user = user;
        createUI();
        loadBooks();
        refreshList();
    }

    private void createUI() {

        frame = new JFrame("📚 Lost City Books");
        frame.setSize(980, 580);
        frame.setLayout(null);
        frame.getContentPane().setBackground(BG);
        frame.setLocationRelativeTo(null);

        // ================= HEADER =================
        JPanel header = new JPanel(null);
        header.setBounds(0, 0, 980, 90);
        header.setBackground(CARD);
        frame.add(header);

        // 🖼️ LOGO
        ImageIcon icon = new ImageIcon("src/images/logo.png");
        Image img = icon.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH);
        JLabel logo = new JLabel(new ImageIcon(img));
        logo.setBounds(20, 15, 55, 55);
        header.add(logo);

        JLabel title = new JLabel("Lost City Books");
        title.setBounds(90, 25, 300, 40);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(TEXT);
        header.add(title);

        JLabel welcome = new JLabel("Welcome, " + user.getName());
        welcome.setBounds(700, 30, 250, 30);
        welcome.setForeground(Color.LIGHT_GRAY);
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        header.add(welcome);

        // ================= MAIN CARD =================
        JPanel card = new JPanel(null);
        card.setBounds(40, 120, 900, 420);
        card.setBackground(CARD);
        frame.add(card);

        // 🔍 Search
        txtSearch = new JTextField();
        txtSearch.setBounds(30, 20, 260, 35);
        styleField(txtSearch);
        txtSearch.setToolTipText("Search books...");
        card.add(txtSearch);

        // 🏷️ Category Filter
        cmbCategory = new JComboBox<>(new String[]{
                "All", "Movie", "Anime", "Emotional", "Psychology", "Technology"
        });
        cmbCategory.setBounds(310, 20, 180, 35);
        cmbCategory.setFont(new Font("Segoe UI", Font.BOLD, 14));
        card.add(cmbCategory);

        // 📚 Book List
        model = new DefaultListModel<>();
        bookList = new JList<>(model);
        bookList.setFont(new Font("Segoe UI", Font.BOLD, 15));
        bookList.setBackground(new Color(28, 20, 60));
        bookList.setForeground(TEXT);
        bookList.setSelectionBackground(BTN);

        JScrollPane scroll = new JScrollPane(bookList);
        scroll.setBounds(30, 70, 360, 320);
        card.add(scroll);

        // 🔘 Buttons
        btnAdd = createButton(" Add to Cart", 430, 90);
        btnCart = createButton(" View Cart", 430, 150);
        btnTotal = createButton(" Total Price", 430, 210);
        btnPurchase = createButton(" Purchase", 430, 270);

        card.add(btnAdd);
        card.add(btnCart);
        card.add(btnTotal);
        card.add(btnPurchase);

        // 🎯 Events
        txtSearch.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                refreshList();
            }
        });

        cmbCategory.addActionListener(e -> refreshList());

        frame.setVisible(true);
    }

    // ================= BOOK DATA =================
    private void loadBooks() {

        // 🎬 Movie
        allBooks.add("Movie | Oppenheimer ($50)");
        allBooks.add("Movie | Interstellar ($48)");
        allBooks.add("Movie | Inception ($46)");

        // 🎌 Anime
        allBooks.add("Anime | Attack on Titan ($55)");
        allBooks.add("Anime | Naruto ($42)");
        allBooks.add("Anime | Demon Slayer ($44)");
        allBooks.add("Anime | One Piece ($60)");

        // 💔 Emotional
        allBooks.add("Emotional | The Silent Goodbye ($35)");
        allBooks.add("Emotional | Letters I Never Sent ($38)");
        allBooks.add("Emotional | Midnight Tears ($33)");

        // 🧠 Psychology
        allBooks.add("Psychology | Atomic Habits ($45)");
        allBooks.add("Psychology | Mindset ($42)");
        allBooks.add("Psychology | The Power of Habit ($40)");

        // 💻 Technology
        allBooks.add("Technology | Clean Code ($45)");
        allBooks.add("Technology | Design Patterns ($55)");
        allBooks.add("Technology | Algorithms Unlocked ($50)");
    }

    private void refreshList() {

        model.clear();

        String keyword = txtSearch.getText().toLowerCase();
        String selectedCategory = cmbCategory.getSelectedItem().toString();

        for (String book : allBooks) {

            boolean matchSearch = book.toLowerCase().contains(keyword);
            boolean matchCategory = selectedCategory.equals("All") ||
                    book.startsWith(selectedCategory);

            if (matchSearch && matchCategory) {
                model.addElement(book.replaceAll(".*\\| ", ""));
            }
        }
    }

    // ================= UI HELPERS =================
    private JButton createButton(String text, int x, int y) {

        JButton btn = new JButton(text);
        btn.setBounds(x, y, 350, 45);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
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

        btn.addActionListener(e -> handleAction(text));
        return btn;
    }

    private void styleField(JTextField f) {
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        f.setBackground(new Color(30, 20, 60));
        f.setForeground(TEXT);
        f.setCaretColor(TEXT);
        f.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    // ================= BUTTON ACTIONS =================
    private void handleAction(String action) {

        int index = bookList.getSelectedIndex();

        if (action.contains("Add")) {
            if (index < 0) {
                alert("Please select a book first.");
                return;
            }
            String text = model.get(index);
            cart.add(new Book(text, extractPrice(text)));
            alert("Added to cart.");
        }

        if (action.contains("Cart")) {
            if (cart.isEmpty()) {
                alert("Cart is empty.");
                return;
            }

            StringBuilder sb = new StringBuilder("🛒 Your Cart:\n\n");
            for (Book b : cart.getBooks()) {
                sb.append("• ").append(b).append("\n");
            }
            alert(sb.toString());
        }

        if (action.contains("Total")) {
            alert("💰 Total = $" + cart.getTotal());
        }

        // 🧾 PURCHASE + RECEIPT + SAVE REPORT
        if (action.contains("Purchase")) {

            if (cart.isEmpty()) {
                alert("Cart is empty.");
                return;
            }

            StringBuilder receipt = new StringBuilder();
            receipt.append("🧾 LOST CITY BOOKS RECEIPT\n");
            receipt.append("--------------------------------\n");
            receipt.append("Customer Name : ").append(user.getName()).append("\n");
            receipt.append("Email         : ").append(user.getEmail()).append("\n");
            receipt.append("Gender        : ").append(user.getGender()).append("\n");
            receipt.append("Date          : ").append(LocalDateTime.now()).append("\n\n");

            receipt.append("Purchased Items:\n");
            for (Book b : cart.getBooks()) {
                receipt.append("• ").append(b.getTitle())
                        .append(" ($").append(b.getPrice()).append(")\n");
            }

            receipt.append("\n--------------------------------\n");
            receipt.append("TOTAL : $").append(cart.getTotal()).append("\n");
            receipt.append("--------------------------------\n");
            receipt.append("✨ Thank you for shopping at Lost City Books!");

            JTextArea area = new JTextArea(receipt.toString());
            area.setFont(new Font("Monospaced", Font.PLAIN, 13));
            area.setEditable(false);

            JScrollPane pane = new JScrollPane(area);
            pane.setPreferredSize(new Dimension(460, 360));

            JOptionPane.showMessageDialog(frame, pane, "Receipt", JOptionPane.INFORMATION_MESSAGE);

            saveSalesReport(receipt.toString());
            cart.clear();
        }
    }

    // ================= UTILITIES =================
    private double extractPrice(String text) {
        try {
            int start = text.indexOf("$") + 1;
            int end = text.indexOf(")");
            return Double.parseDouble(text.substring(start, end));
        } catch (Exception e) {
            return 0;
        }
    }

    private void alert(String msg) {
        JOptionPane.showMessageDialog(frame, msg);
    }

    // 💾 Save Sales Report
    private void saveSalesReport(String receipt) {
        try {
            FileWriter writer = new FileWriter("data/sales_report.txt", true);
            writer.write(receipt);
            writer.write("\n\n====================================\n\n");
            writer.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(frame, "Failed to save sales report.");
        }
    }
}