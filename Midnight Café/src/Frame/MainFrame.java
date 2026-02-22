package Frame;

import Entity.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.*;

public class MainFrame extends JFrame implements ActionListener {

    private JPanel mainPanel, menuPanel, orderPanel, customerPanel, topPanel;
    private JTextArea taOrder, taReceipt;
    private JTextField tfName, tfDiscount, tfCash;
    private JButton btRemove, btClear, btConfirm, btTotal, btDiscount;

    private JRadioButton rbMale, rbFemale, rbMonkey;
    private ButtonGroup bgGender;

    private Order currentOrder;
    private Customer customer;
    private MenuManager menuManager;
    private OrderService orderService;

    private final Color BG = new Color(18,18,25);
    private final Color CARD = new Color(28,28,40);
    private final Color ACCENT = new Color(155,89,255);
    private final Color TEXT = new Color(230,230,240);

    private final double AUTO_LIMIT = 1000;
    private final double AUTO_RATE = 10;

    public MainFrame() {

        super("Midnight Café — Where Nights Taste Better");
        setSize(1650,920);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainPanel = new JPanel(new BorderLayout(15,15));
        mainPanel.setBackground(BG);
        mainPanel.setBorder(new EmptyBorder(15,15,15,15));
        add(mainPanel);

        initServices();
        buildTopPanel();
        buildMenuPanel();
        buildOrderPanel();
        buildCustomerPanel();

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(orderPanel, BorderLayout.CENTER);
        mainPanel.add(customerPanel, BorderLayout.EAST);
    }

    private void initServices() {
        try {
            menuManager = new MenuManager();
            orderService = new OrderServiceImpl(menuManager);
            customer = new Customer();
            currentOrder = orderService.createOrder(customer);
        } catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    // ================= TOP =================
    private void buildTopPanel() {

        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(CARD);
        topPanel.setBorder(new EmptyBorder(12,18,12,18));

        JLabel logo = new JLabel(loadIcon("logo.png", 45, 45));

        JLabel title = new JLabel("  Midnight Café  |  Where Nights Taste Better");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT,12,0));
        left.setOpaque(false);
        left.add(logo);
        left.add(title);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT,12,0));
        btnPanel.setOpaque(false);

        btRemove = makeButton("Remove");
        btClear = makeButton("Clear");
        btTotal = makeButton("Show Total");
        btConfirm = makeAccentButton("Confirm");

        btnPanel.add(btRemove);
        btnPanel.add(btClear);
        btnPanel.add(btTotal);
        btnPanel.add(btConfirm);

        topPanel.add(left, BorderLayout.WEST);
        topPanel.add(btnPanel, BorderLayout.EAST);
    }

    // ================= MENU =================
    private void buildMenuPanel() {

        JPanel menuCard = makeCard("Menu");
        menuCard.setLayout(new GridLayout(0,2,18,18));

        List<Entity.MenuItem> items = menuManager.getAvailableItems();

        for(Entity.MenuItem item : items){

            String imgName = item.getName().toLowerCase().replace(" ", "_") + ".jpg";
            ImageIcon icon = loadIcon(imgName, 95, 85);

            JButton b = new JButton(
                    "<html><center><b>"+item.getName()+"</b><br>"
                            + item.getPrice()+" TK</center></html>",
                    icon
            );

            b.setHorizontalTextPosition(SwingConstants.CENTER);
            b.setVerticalTextPosition(SwingConstants.BOTTOM);
            b.setBackground(new Color(40,40,60));
            b.setForeground(TEXT);
            b.setFocusPainted(false);
            b.setBorder(new LineBorder(ACCENT,2));
            b.setPreferredSize(new Dimension(190,160));
            b.putClientProperty("menuItem", item);
            b.addActionListener(this);

            menuCard.add(b);
        }

        JScrollPane sp = new JScrollPane(menuCard);
        sp.setBorder(null);
        sp.getVerticalScrollBar().setUnitIncrement(22);

        menuPanel = new JPanel(new BorderLayout());
        menuPanel.add(sp);
        menuPanel.setPreferredSize(new Dimension(450,0));
    }

    // ================= ORDER =================
    private void buildOrderPanel() {

        orderPanel = makeCard("Current Order");
        orderPanel.setLayout(new BorderLayout(12,12));

        JPanel dPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dPanel.setOpaque(false);

        JLabel dl = new JLabel("Extra Discount %:");
        dl.setForeground(TEXT);

        tfDiscount = new JTextField(7);
        btDiscount = makeButton("Apply");

        dPanel.add(dl);
        dPanel.add(tfDiscount);
        dPanel.add(btDiscount);

        taOrder = new JTextArea();
        taOrder.setEditable(false);
        taOrder.setBackground(BG);
        taOrder.setForeground(TEXT);
        taOrder.setFont(new Font("Consolas", Font.PLAIN, 15));

        orderPanel.add(dPanel, BorderLayout.NORTH);
        orderPanel.add(new JScrollPane(taOrder), BorderLayout.CENTER);

        refreshSummary();
    }

    // ================= CUSTOMER =================
    private void buildCustomerPanel() {

        customerPanel = makeCard("Customer & Receipt");
        customerPanel.setLayout(new BorderLayout(12,12));
        customerPanel.setPreferredSize(new Dimension(450,0));

        JPanel info = new JPanel(new GridBagLayout());
        info.setOpaque(false);

        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8,8,8,8);
        gc.fill = GridBagConstraints.HORIZONTAL;

        gc.gridx=0; gc.gridy=0; gc.weightx=0;
        info.add(makeLabel("Name"), gc);

        tfName = new JTextField();
        gc.gridx=1; gc.gridy=0; gc.weightx=1;
        info.add(tfName, gc);

        gc.gridx=0; gc.gridy=1; gc.weightx=0;
        info.add(makeLabel("Gender"), gc);

        JPanel gPanel = new JPanel(new FlowLayout(FlowLayout.LEFT,15,0));
        gPanel.setOpaque(false);

        rbMale = new JRadioButton("Male");
        rbFemale = new JRadioButton("Female");
        rbMonkey = new JRadioButton("Monkey");

        for(JRadioButton r : new JRadioButton[]{rbMale, rbFemale, rbMonkey}){
            r.setForeground(TEXT);
            r.setBackground(CARD);
        }

        bgGender = new ButtonGroup();
        bgGender.add(rbMale);
        bgGender.add(rbFemale);
        bgGender.add(rbMonkey);

        gPanel.add(rbMale);
        gPanel.add(rbFemale);
        gPanel.add(rbMonkey);

        gc.gridx=1; gc.gridy=1; gc.weightx=1;
        info.add(gPanel, gc);

        gc.gridx=0; gc.gridy=2; gc.weightx=0;
        info.add(makeLabel("Cash"), gc);

        tfCash = new JTextField();
        gc.gridx=1; gc.gridy=2; gc.weightx=1;
        info.add(tfCash, gc);

        customerPanel.add(info, BorderLayout.NORTH);

        taReceipt = new JTextArea();
        taReceipt.setEditable(false);
        taReceipt.setBackground(BG);
        taReceipt.setForeground(ACCENT);
        taReceipt.setFont(new Font("Consolas", Font.PLAIN, 15));

        customerPanel.add(new JScrollPane(taReceipt), BorderLayout.CENTER);
    }

    // ================= HELPERS =================
    private JPanel makeCard(String title){
        JPanel p = new JPanel();
        p.setBackground(CARD);
        p.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(ACCENT,2),
                title,
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 15),
                TEXT));
        return p;
    }

    private JButton makeButton(String t){
        JButton b = new JButton(t);
        b.setBackground(new Color(70,70,110));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.addActionListener(this);
        return b;
    }

    private JButton makeAccentButton(String t){
        JButton b = new JButton(t);
        b.setBackground(ACCENT);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.addActionListener(this);
        return b;
    }

    private JLabel makeLabel(String t){
        JLabel l = new JLabel(t);
        l.setForeground(TEXT);
        return l;
    }

    // ================= DISCOUNT =================
    private double getAutoDiscount(double total){
        if(total >= AUTO_LIMIT) return total * AUTO_RATE / 100.0;
        return 0;
    }

    private double getExtraDiscount(double total){
        try{
            double p = Double.parseDouble(tfDiscount.getText());
            return total * p / 100.0;
        }catch(Exception e){ return 0; }
    }

    private void refreshSummary(){

        double subtotal = currentOrder.getTotalPrice();
        double autoDis = getAutoDiscount(subtotal);
        double extraDis = getExtraDiscount(subtotal);
        double payable = subtotal - autoDis - extraDis;

        taOrder.setText("Items:\n\n");
        for(OrderItem it : currentOrder.getItems()){
            taOrder.append(it.getItem().getName()
                    +" x"+it.getQuantity()
                    +" = "+it.getTotalPrice()+" TK\n");
        }

        taOrder.append("\n-----------------------------\n");
        taOrder.append("Subtotal: "+subtotal+" TK\n");
        if(autoDis>0) taOrder.append("Auto Discount(10%): -"+autoDis+" TK\n");
        if(extraDis>0) taOrder.append("Extra Discount: -"+extraDis+" TK\n");
        taOrder.append("Payable: "+payable+" TK");
    }

    // ================= FILE SAVE =================
    private void saveData(String receiptText, double payable){

        try{
            File dir = new File("data");
            if(!dir.exists()) dir.mkdir();

            // CUSTOMER
            FileWriter cw = new FileWriter("data/customers.txt", true);
            cw.write("Name: "+tfName.getText()+"\n");
            cw.write("Gender: "+(rbMale.isSelected()?"Male":rbFemale.isSelected()?"Female":"Monkey")+"\n");
            cw.write("Date: "+LocalDate.now()+"\n------------------\n");
            cw.close();

            // ORDER
            FileWriter ow = new FileWriter("data/orders.txt", true);
            ow.write(receiptText+"\n\n");
            ow.close();

            // REPORT
            FileWriter rw = new FileWriter("data/report.txt", true);
            rw.write("Date: "+LocalDate.now()+"  Sale: "+payable+" TK\n");
            rw.close();

        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"File Save Error!");
        }
    }

    // ================= EVENTS =================
    public void actionPerformed(ActionEvent e){

        if(e.getSource()==btDiscount){
            refreshSummary();
        }

        else if(e.getSource()==btRemove){
            orderService.removeItemFromOrder(currentOrder,"");
            refreshSummary();
        }

        else if(e.getSource()==btClear){
            currentOrder.clear();
            refreshSummary();
        }
	
	else if(e.getSource() == btTotal) {

    double subtotal = currentOrder.getTotalPrice();
    double autoDis = getAutoDiscount(subtotal);
    double extraDis = getExtraDiscount(subtotal);
    double payable = subtotal - autoDis - extraDis;

    JOptionPane.showMessageDialog(
        this,
        "Subtotal: " + subtotal + " TK\n"
      + "Auto Discount: " + autoDis + " TK\n"
      + "Extra Discount: " + extraDis + " TK\n"
      + "----------------------\n"
      + "Payable: " + payable + " TK",
        "ZenOrder Total",
        JOptionPane.INFORMATION_MESSAGE
    );
}


        else if(e.getSource()==btConfirm){

            if(tfName.getText().trim().isEmpty() || tfCash.getText().trim().isEmpty() || currentOrder.getItems().isEmpty()){
                JOptionPane.showMessageDialog(this,"Fill all info & order first!");
                return;
            }

            taReceipt.setText("");

            double subtotal = currentOrder.getTotalPrice();
            double autoDis = getAutoDiscount(subtotal);
            double extraDis = getExtraDiscount(subtotal);
            double payable = subtotal - autoDis - extraDis;

            String gender = rbMale.isSelected()?"Male": rbFemale.isSelected()?"Female":"Monkey";
            int orderNo = new Random().nextInt(9000)+1000;

            taReceipt.append("        MIDNIGHT CAFÉ\n");
	    taReceipt.append("   Where Nights Taste Better\n");
	    taReceipt.append("       Powered By Aninda\n");
            taReceipt.append("--------------------------------\n");
            taReceipt.append("Order No: "+orderNo+"\n");
            taReceipt.append("Date: "+LocalDate.now()+"\n");
            taReceipt.append("Customer: "+tfName.getText()+"\n");
            taReceipt.append("Gender: "+gender+"\n");
            taReceipt.append("--------------------------------\n");

            for(OrderItem it : currentOrder.getItems()){
                taReceipt.append(it.getItem().getName()+" x"+it.getQuantity()+"\n");
            }

            taReceipt.append("--------------------------------\n");
            taReceipt.append("Subtotal: "+subtotal+" TK\n");
            if(autoDis>0) taReceipt.append("Auto Discount: -"+autoDis+" TK\n");
            if(extraDis>0) taReceipt.append("Extra Discount: -"+extraDis+" TK\n");
            taReceipt.append("Payable: "+payable+" TK\n");

            double cash = Double.parseDouble(tfCash.getText());
            taReceipt.append("Cash: "+cash+" TK\n");
            taReceipt.append("Change: "+(cash - payable)+" TK\n");
            taReceipt.append("--------------------------------\n");
            taReceipt.append("     Thank you! Visit Again  \n");

            saveData(taReceipt.getText(), payable);
        }

        else if(e.getSource() instanceof JButton){
            JButton b=(JButton)e.getSource();
            Entity.MenuItem item=(Entity.MenuItem)b.getClientProperty("menuItem");
            if(item!=null){
                orderService.addItemToOrder(currentOrder,item,1);
                refreshSummary();
            }
        }
    }

    private ImageIcon loadIcon(String filename, int w, int h) {
        try {
            String path = System.getProperty("user.dir")+File.separator+"images"+File.separator+filename;
            File f = new File(path);
            if(!f.exists()) return null;
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(w,h,Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        } catch(Exception e){ return null; }
    }
}
