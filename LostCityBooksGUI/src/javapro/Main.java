import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        // Ensure data folder exists
        new java.io.File("data").mkdirs();

        SwingUtilities.invokeLater(() -> new Login());
    }
}