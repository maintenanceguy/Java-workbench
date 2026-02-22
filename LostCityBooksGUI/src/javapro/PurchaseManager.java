import java.io.*;
import java.time.LocalDateTime;

public class PurchaseManager {

    public void savePurchase(User user, Cart cart) {

        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter("data/purchases.txt", true))) {

            writer.write("Customer: " + user.getName());
            writer.newLine();
            writer.write("Date: " + LocalDateTime.now());
            writer.newLine();

            for (Book book : cart.getBooks()) {
                writer.write(" - " + book.getTitle() + " ($" + book.getPrice() + ")");
                writer.newLine();
            }

            writer.write("Total: $" + cart.getTotal());
            writer.newLine();
            writer.write("-------------------------------------");
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
