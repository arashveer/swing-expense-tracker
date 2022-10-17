package model;

// Represents an expense with a title, amount, date, and note.
public class Expense {
    private String title;
    private double amount;
    private String date;
    private String note;

    // EFFECTS: Constructs an expense object with associated title, amount, date, and a note.
    public Expense(String title, double amount, String date, String note) {
        this.title = title;
        this.amount = amount;
        this.date = date;
        this.note = note;
    }

    // GETTERS

    public String getTitle() {
        return title;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

    //SETTERS

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
