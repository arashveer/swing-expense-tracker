package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IncomeTest {
    Income income;

    @BeforeEach
    public void setup() {
        income = new Income(4468.70,"Company XYZ");
    }

    @Test
    public void testConstructor() {
        assertEquals("Company XYZ", income.getSource());
        assertEquals(4468.70, income.getAmount());
    }

    @Test
    public void testChangeAmount() {
        income.setAmount(2002.10);
        assertEquals(2002.10, income.getAmount());
    }

    @Test
    public void testChangeSource() {
        income.setSource("Company ABC");
        assertEquals("Company ABC", income.getSource());
    }

    @Test
    public void testChangeAll() {
        income.setSource("Company ABC");
        income.setAmount(2002.10);
        assertEquals("Company ABC", income.getSource());
        assertEquals(2002.10, income.getAmount());

    }
}
