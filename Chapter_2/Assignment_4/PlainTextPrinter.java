package Chapter_2.Assignment_4;

public class PlainTextPrinter implements Printer {

    @Override
    public void printPage(String page) {
        System.out.println(page);
    }
}