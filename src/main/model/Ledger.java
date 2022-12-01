package model;

import org.json.JSONArray;
import org.json.JSONObject;

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
        addExpenseNoLog(title, amount, date, note);

        EventLog.getInstance().logEvent(new Event("Added an expense '" + title + "' of amount $"
                                                  + String.format("%,.2f",amount)));
    }

    public void addExpenseNoLog(String title, double amount, String date, String note) {
        expenses.add(new Expense(title, amount, date, note));
        balance -= amount;
    }

    /*
     * REQUIRES: amount >= 0
     * MODIFIES: this
     * EFFECTS: Add an income to income list
     */
    public void addIncome(double amount, String source) {
        addIncomeNoLog(amount, source);

        EventLog.getInstance().logEvent(new Event("Added an income '" + source + "' of amount $"
                                                 + String.format("%,.2f", amount)));
    }

    public void addIncomeNoLog(double amount, String source) {
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

        EventLog.getInstance().logEvent(new Event("Added a saving goal '" + title + "' of goal amount $"
                                                 + String.format("%,.2f",goalAmount)));
    }

    /*
     * REQUIRES: amount >= 0
     * MODIFIES: this
     * EFFECTS: Set a saving goal and add it to goals list
     */
    public void setOldSavingGoal(String title, double goalAmount, double currentAmount, boolean complete) {
        goals.add(new SavingGoal(title,goalAmount,currentAmount,complete));
        balance -= currentAmount;
    }

    /*
     * REQUIRES: balance >=0
     * MODIFIES: this
     * EFFECTS: Set the balance to given value
     */
    public void setBalance(double balance) {
        this.balance = balance;
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

    // EFFECTS: returns total of incomes as a double
    public double totalIncome() {
        double totalIncome = 0;
        for (int i = 0; i < getIncomeList().size(); i++) {
            totalIncome += getIncome(i).getAmount();
        }
        return totalIncome;
    }

    // EFFECTS: returns total of expenses as a double
    public double totalExpenses() {
        double totalExpense = 0;
        for (int i = 0; i < getExpenses().size(); i++) {
            totalExpense += getExpense(i).getAmount();
        }
        return totalExpense;
    }

    /*
     * REQUIRES: amount >= 0
     * MODIFIES: this
     * EFFECTS: Contributes to a saving goal in goals list and reduce the balance by that amount
     */
    public boolean addToSavingGoal(int index, double amount) {
        // If goal is complete
        if (this.goals.get(index).isComplete()) {
            System.out.println("This saving goal has already been completed. Cannot contribute!");
            return false;
        }
        // If amount is negative
        if (amount < 0) {
            System.out.println("Contribution to saving goal should be more than zero.");
            return false;
        }
        // If something else
        if (!this.goals.get(index).addToCurrentAmount(amount)) {
            System.out.println("Unable to contribute to savings.");
            return false;
        }
        balance -= amount;

        EventLog.getInstance().logEvent(new Event("Contributed $" + String.format("%,.2f",amount)
                + " to '" + getSavingGoal(index).getName() + "'."));
        return true;
    }

    /*
     *  Delete functions
     */

    // MODIFIES: incomesList (this)
    // EFFECTS: deletes an income of given index from incomeList
    public void removeIncome(int index) {
        Income income = incomeList.get(index);
        incomeList.remove(index);
        balance = balance - income.getAmount();

        EventLog.getInstance().logEvent(new Event("Deleted income '" + income.getSource()
                + "' of amount $" + String.format("%,.2f",income.getAmount())));
    }

    // MODIFIES: expenses (this)
    // EFFECTS: deletes an expense of given index from expenses
    public void removeExpense(int index) {
        Expense expense = expenses.get(index);
        expenses.remove(index);
        balance = balance + expense.getAmount();

        EventLog.getInstance().logEvent(new Event("Deleted expense '" + expense.getTitle()
                + "' of amount $" + String.format("%,.2f",expense.getAmount())));
    }

    // MODIFIES: goals (this)
    // EFFECTS: deletes a saving goal of given index from goals
    public void removeSavingGoal(int index) {
        SavingGoal goal = goals.get(index);
        goals.remove(index);
        balance = balance + goal.getCurrentAmount();

        EventLog.getInstance().logEvent(new Event("Deleted saving goal '" + goal.getName()
                + "' ($" + String.format("%,.2f",goal.getCurrentAmount())
                + " out of $" + String.format("%,.2f",goal.getGoalAmount()) + ")"));
    }

    // JSON Writer functions

    // EFFECTS: returns json object consisting of this ledger's data
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("balance", balance);
        json.put("data", listsToJson());
        return json;
    }

    // EFFECTS: returns JSON array comprising all three 3 types of our lists
    private JSONArray listsToJson() {
        JSONArray jsonData = new JSONArray();
        jsonData.put(incomesToJson());
        jsonData.put(expensesToJson());
        jsonData.put(goalsToJson());
        return jsonData;
    }

    // EFFECTS: returns expenses in this ledger as a JSON array
    private JSONObject expensesToJson() {
        JSONArray jsonArray = new JSONArray(expenses);
        return new JSONObject().put("expenses", jsonArray);
    }

    // EFFECTS: returns incomes in this ledger as a JSON array
    private JSONObject incomesToJson() {
        JSONArray jsonArray = new JSONArray(incomeList);
        return new JSONObject().put("incomes", jsonArray);
    }

    // EFFECTS: returns saving goals in this ledger as a JSON array
    private JSONObject goalsToJson() {
        JSONArray jsonArray = new JSONArray(goals);
        return new JSONObject().put("goals", jsonArray);
    }
}
