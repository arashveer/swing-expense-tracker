package persistence;

import model.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {

    protected void checkExpense(String title, double amount, String date, String note, Expense expense) {
        assertEquals(title, expense.getTitle());
        assertEquals(amount, expense.getAmount());
        assertEquals(date, expense.getDate());
        assertEquals(note, expense.getNote());
    }

    protected void checkIncome(String source, double amount, Income income) {
        assertEquals(source, income.getSource());
        assertEquals(amount, income.getAmount());
    }

    protected void checkGoal(String name, double goalAmount, double currentAmount, boolean complete, SavingGoal goal) {
        assertEquals(name, goal.getName());
        assertEquals(goalAmount, goal.getGoalAmount());
        assertEquals(currentAmount, goal.getCurrentAmount());
        assertEquals(complete, goal.isComplete());
    }
}
