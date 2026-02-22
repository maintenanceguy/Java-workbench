import java.util.ArrayList;

public class Cart {

    private ArrayList<Book> books = new ArrayList<>();

    public void add(Book book) {
        books.add(book);
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public boolean isEmpty() {
        return books.isEmpty();
    }

    public double getTotal() {
        double total = 0;
        for (Book b : books) {
            total += b.getPrice();
        }
        return total;
    }

    public void clear() {
        books.clear();
    }
}
