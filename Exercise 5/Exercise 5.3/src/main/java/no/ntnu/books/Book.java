package no.ntnu.books;

public class Book {
    private int id;
    private String title;
    private int year;
    private int numberOfPages;

    public Book(int id, String title, int year, int numberOfPages) throws IllegalArgumentException {
        this.setId(id);
        this.setTitle(title);
        this.setYear(year);
        this.setNumberOfPages(numberOfPages);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) throws IllegalArgumentException {
        if (id < 0) {
            throw new IllegalArgumentException("id cannot be less than 0");
        }
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) throws IllegalArgumentException {
        if (title.equals(null)) {
            throw new IllegalArgumentException("title cannot be null");
        }
        this.title = title;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) throws IllegalArgumentException {
        if (numberOfPages < 0) {
            throw new IllegalArgumentException("number of pages cannot be less than 0");
        }

        this.numberOfPages = numberOfPages;
    }
}
