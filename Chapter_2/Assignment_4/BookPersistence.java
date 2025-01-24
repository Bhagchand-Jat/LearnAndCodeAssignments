package Chapter_2.Assignment_4;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BookPersistence {
    public void save(Book book) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(getFileName(book)))) {
            out.writeObject(book);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save book: " + book.getTitle(), e);
        }
    }

    public Book load(String fileName) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Book) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load book from file: " + fileName, e);
        }
    }

    private String getFileName(Book book) {
        return "documents/" + book.getTitle().replaceAll(" ", "_") + "_" + book.getAuthor().replaceAll(" ", "_")
                + ".ser";
    }
}
