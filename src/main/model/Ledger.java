package model;

import java.util.ArrayList;
import java.util.List;

// Represents a ledger responsible for creation and handling of incomes, expenses, and saving goals
public class Ledger {

    private double balance; // current balance

    // Create a list of all the expenses
    private final List<Expense> expenses;

    // Create a list of all the income sources
    private final List<Income> incomeList;

    // Create a list of all the saving goals
    private final List<SavingGoal> goals;

    // EFFECTS: Constructs lists for expenses, incomes, and goals and sets the balance to zero.
    public Ledger() {
        this.expenses = new ArrayList<>();
        this.incomeList = new ArrayList<>();
        this.goals = new ArrayList<>();
        balance = 0;
    }

    /*
     * REQUIRES: amount >= 0
     * MODIFIES: this
     * EFFECTS: Adds an expense to expenses list
     */
    public void addExpense(String title, double amount, String date, String note) {
        expenses.add(new Expense(title, amount, date, note));
        balance -= amount;
    }

    /*
     * REQUIRES: amount >= 0
     * MODIFIES: this
     * EFFECTS: Add an income to income list
     */
    public void addIncome(double amount, String source) {
        incomeList.add(new Income(amount, source));
        balance += amount;
    }

    /*
     * REQUIRES: amount >= 0
     * MODIFIES: this
     * EFFECTS: Set a saving goal and add it to goals list
     */
    public void setSavingGoal(String title, double goalAmount) {
        goals.add(new SavingGoal(title,goalAmount));
    }

    // GETTERS

    // EFFECT: returns an expense object at index in list expenses
    public Expense getExpense(int index) {
        return expenses.get(index);
    }

    // EFFECT: returns an income object at index in list incomeList
    public Income getIncome(int index) {
        return incomeList.get(index);
    }

    // EFFECT: returns a savingGoal object at index in list goals
    public SavingGoal getSavingGoal(int index) {
        return goals.get(index);
    }

    // EFFECT: returns the expenses list
    public List<Expense> getExpenses() {
        return expenses;
    }

    // EFFECT: returns the incomes list
    public List<Income> getIncomeList() {
        return incomeList;
    }

    // EFFECT: returns the saving goals list
    public List<SavingGoal> getGoals() {
        return goals;
    }

    // EFFECTS: returns the current balance
    public double getBalance() {
        return balance;
    }

    /*
     * REQUIRES: amount >= 0
     * MODIFIES: this
     * EFFECTS: Contributes to a saving goal in goals list and reduce the balance by that amount
     */
    public void addToSavingGoal(int index, double amount) {
        // If goal is complete
        if (this.goals.get(index).isComplete()) {
            System.out.println("This saving goal has already been completed. Cannot contribute!");
            return;
        }
        // If amount is negative
        if (amount < 0) {
            System.out.println("Contribution to saving goal should be more than zero.");
            return;
        }
        // If amount is less than or equal to remaining savings goal
        if (amount <= this.goals.get(index).getGoalAmount()) {
            balance -= amount;
            this.goals.get(index).addToCurrentAmount(amount);
            if (this.goals.get(index).getGoalAmount() == this.goals.get(index).getCurrentAmount()) {
                this.goals.get(index).setComplete(true);
            }
        } else {
            System.out.println("Amount cannot be larger than the remaining savings goal.");
        }
    }
}
