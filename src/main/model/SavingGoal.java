package model;

// Represents a saving goal with a name, goalAmount, currentAmount and status(complete or incomplete)
public class SavingGoal {
    private double goalAmount; //
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

    public void addToCurrentAmount(double currentAmount) {
        this.currentAmount += currentAmount;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}
