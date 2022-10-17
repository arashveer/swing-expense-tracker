package model;

// Constructs an income object with its amount and source.
public class Income {
    private double amount;
    private String source;

    // Constructs an income object with its source and amount.
    public Income(double amount, String source) {
        this.amount = amount;
        this.source = source;
    }

    // GETTERS

    public double getAmount() {
        return amount;
    }

    public String getSource() {
        return source;
    }

    // SETTERS

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
