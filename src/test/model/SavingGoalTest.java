package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SavingGoalTest {

    SavingGoal goal;

    @BeforeEach
    public void setup() {
        goal = new SavingGoal("Buy a new car", 14480.00);
    }

    @Test
    public void testConstructor() {
        assertEquals("Buy a new car", goal.getName());
        assertEquals(14480.00, goal.getGoalAmount());
        assertEquals(0, goal.getCurrentAmount());
        assertFalse(goal.isComplete());
    }

    @Test
    public void testSecondConstructor() {
        SavingGoal newGoal = new SavingGoal("Buy a new car", 14480.00,14480.00, true);
        assertEquals("Buy a new car", newGoal.getName());
        assertEquals(14480.00, newGoal.getGoalAmount());
        assertEquals(14480.00, newGoal.getCurrentAmount());
        assertTrue(newGoal.isComplete());
    }

    @Test
    public void testChangeAll() {
        goal.setName("Buy new laptop");
        goal.setGoalAmount(1800.50);
        assertEquals("Buy new laptop", goal.getName());
        assertEquals(1800.50, goal.getGoalAmount());
    }

    @Test
    public void testAddToCurrentAmountAlreadyComplete() {
        goal.setComplete(true);
        assertFalse(goal.addToCurrentAmount(500));
    }

    @Test
    public void testAddToCurrentAmountNegative() {
        assertFalse(goal.addToCurrentAmount(-500));
    }

    @Test
    public void testAddToCurrentAmountMoreThanNeed() {
        assertFalse(goal.addToCurrentAmount(20000.25));
    }

    @Test
    public void testAddToCurrentAmountNotComplete() {
        assertTrue(goal.addToCurrentAmount(5000.99));
        assertFalse(goal.isComplete());
    }

    @Test
    public void testAddToCurrentAmountCompleteTrue() {
        assertFalse(goal.isComplete());
        assertTrue(goal.addToCurrentAmount(14480.00));
        assertTrue(goal.isComplete());
    }
}
