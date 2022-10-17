package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LedgerTest {
    Ledger ledger;

    @BeforeEach
    public void setup() {
        ledger = new Ledger();
    }

    @Test
    public void testConstructor() {
        assertEquals(0, ledger.getExpenses().size());
        assertEquals(0, ledger.getIncomeList().size());
        assertEquals(0, ledger.getGoals().size());
        assertEquals(0,ledger.getBalance());
    }

    @Test
    public void testAddToIncomeList() {
        ledger.addIncome(4468.70,"Company XYZ");
        ledger.addIncome(4168.71,"Company ABC");

        assertEquals(2, ledger.getIncomeList().size());
        assertEquals(4168.71, ledger.getIncome(1).getAmount());
        assertEquals("Company XYZ", ledger.getIncome(0).getSource());
    }

    @Test
    public void testAddToExpenseList() {
        ledger.addExpense("Rodgers Mobile", 75.60, "Sept 29", "Mobile Bill");

        assertEquals(75.60, ledger.getExpense(0).getAmount());
        assertEquals("Sept 29", ledger.getExpense(0).getDate());
        assertEquals("Rodgers Mobile", ledger.getExpense(0).getTitle());
        assertEquals("Mobile Bill", ledger.getExpense(0).getNote());
        assertEquals(1, ledger.getExpenses().size());
    }

    @Test
    public void testAddToSavingGoalList() {
        ledger.setSavingGoal("Get a new car", 14800.99);

        assertEquals(1, ledger.getGoals().size());
        assertEquals("Get a new car", ledger.getSavingGoal(0).getName());
        assertEquals(14800.99, ledger.getSavingGoal(0).getGoalAmount());
        assertEquals(0, ledger.getSavingGoal(0).getCurrentAmount());
        assertFalse(ledger.getSavingGoal(0).isComplete());
    }

    @Test
    public void testChangeInBalance() {
        ledger.addIncome(4468.70,"Company XYZ");
        assertEquals(4468.70, ledger.getBalance());

        ledger.addExpense("Rodgers Mobile", 75.20, "Sept 29", "Mobile Bill");
        assertEquals(4393.50, ledger.getBalance());
    }

    @Test
    public void testAddToSavingGoalWhenAlreadyComplete() {
        ledger.setSavingGoal("Get a new car", 14800.99);
        ledger.getSavingGoal(0).setComplete(true);
        assertFalse(ledger.addToSavingGoal(0,2000));
    }


    @Test
    public void testAddToSavingGoalWhenAddMoreThanGoal() {
        ledger.setSavingGoal("Get a new car", 14800.99);
        assertFalse(ledger.addToSavingGoal(0,20000.99));
    }

    @Test
    public void testAddToSavingGoal() {
        ledger.setSavingGoal("Get a new car", 14800.99);
        assertTrue(ledger.addToSavingGoal(0,4400));
        assertEquals(10400.99,
                ledger.getSavingGoal(0).getGoalAmount()-ledger.getSavingGoal(0).getCurrentAmount());
    }


}
