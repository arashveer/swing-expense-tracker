package model;

// Represents a saving goal with a name, goalAmount, currentAmount and status(complete or incomplete)
public class SavingGoal {
    private double goalAmount;
    private double currentAmount;
    private String name;
    private boolean complete;

    // EFFECTS: creates a saving goal with its name, goal, being incomplete and current contribution being zero
    public SavingGoal(String name, double goalAmount) {
        this.name = name;
        this.goalAmount = goalAmount;
        currentAmount = 0;
        complete = false;
    }

    // EFFECTS: used when parsing data from json and creating a new goal
    public SavingGoal(String name, double goalAmount, double currentAmount, boolean complete) {
        this.name = name;
        this.goalAmount = goalAmount;
        this.currentAmount = currentAmount;
        this.complete = complete;
    }

    // GETTERS

    public String getName() {
        return name;
    }

    public double getGoalAmount() {
        return goalAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public boolean isComplete() {
        return complete;
    }

    // SETTERS

    public void setName(String name) {
        this.name = name;
    }

    public void setGoalAmount(double goalAmount) {
        this.goalAmount = goalAmount;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    /* MODIFIES: this
     * EFFECTS: sets the current amount of this goal to provided value and returns true.
     *          returns false, if goal is complete, amount is negative or contribution is more than amount left.
     *          sets the complete boolean true if current amount reaches the goal amount.
     */
    public boolean addToCurrentAmount(double amount) {
        // If goal is complete
        if (complete || amount <= 0) {
            return false;
        }
        // If amount is less than or equal to remaining savings goal
        if (amount <= (goalAmount - currentAmount)) {
            currentAmount += amount;
            if (currentAmount == goalAmount) {
                complete = true;
            }
            return true;
        }
        return false;
    }
}
