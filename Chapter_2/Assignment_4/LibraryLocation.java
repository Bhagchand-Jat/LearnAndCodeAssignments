package Chapter_2.Assignment_4;

public class LibraryLocation {
    private final String shelfNumber;
    private final String roomNumber;

    public LibraryLocation(String shelfNumber, String roomNumber) {
        this.shelfNumber = shelfNumber;
        this.roomNumber = roomNumber;
    }

    public String getLocation() {
        return "Shelf: " + shelfNumber + ", Room: " + roomNumber;
    }
}
