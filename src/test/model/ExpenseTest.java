package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseTest {
    Expense expense;

    @BeforeEach
    public void setup() {
        expense = new Expense("Rodgers Mobile", 75.60, "Sept 29", "Mobile Bill");
    }

    @Test
    public void testConstructor() {
        assertEquals("Rodgers Mobile", expense.getTitle());
        assertEquals(75.60, expense.getAmount());
        assertEquals("Sept 29", expense.getDate());
        assertEquals("Mobile Bill", expense.getNote());
    }

    @Test
    public void testChangeTitle() {
        expense.setTitle("Bell");
        assertEquals("Bell", expense.getTitle());
    }

    @Test
    public void testChangeAmount() {
        expense.setAmount(89.98);
        assertEquals(89.98, expense.getAmount());
    }

    @Test
    public void testChangeDate() {
        expense.setDate("Oct 8");
        assertEquals("Oct 8", expense.getDate());
    }

    @Test
    public void testChangeNote() {
        expense.setNote("Bell Mobile Bill");
        assertEquals("Bell Mobile Bill", expense.getNote());
    }

    @Test
    public void testChangeAll() {
        expense.setTitle("Bell");
        expense.setAmount(89.98);
        expense.setDate("Oct 8");
        expense.setNote("Bell Mobile Bill");
        assertEquals("Bell", expense.getTitle());
        assertEquals(89.98, expense.getAmount());
        assertEquals("Oct 8", expense.getDate());
        assertEquals("Bell Mobile Bill", expense.getNote());
    }
}
