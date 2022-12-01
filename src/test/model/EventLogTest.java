package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventLogTest {

    Ledger ledger;
    EventLog eventLog;
    List<Event> eventsList;

    @BeforeEach
    public void setup() {
        ledger = new Ledger();
        eventsList = new ArrayList<>();
        eventLog = EventLog.getInstance();
        eventLog.clear();
    }

    @Test
    public void testLogWhenAdding() {
        ledger.addExpense("Mobile 1", 75.60, "Sept 29", "Mobile Bill");
        ledger.addIncome(4468.70,"Company XYZ");
        ledger.setSavingGoal("Get a new car", 14800.99);

        for (Event next : eventLog) {
            eventsList.add(next);
        }
        assertEquals("Event log cleared."
                , eventsList.get(0).getDescription());
        assertEquals("Added an expense 'Mobile 1' of amount $75.60"
                , eventsList.get(1).getDescription());
        assertEquals("Added an income 'Company XYZ' of amount $4,468.70"
                , eventsList.get(2).getDescription());
        assertEquals("Added a saving goal 'Get a new car' of goal amount $14,800.99"
                , eventsList.get(3).getDescription());
    }

    @Test
    public void testLogWhenContributeToSavingGoal() {
        ledger.setSavingGoal("Get a new car", 14800.99);
        ledger.addToSavingGoal(0,5000);

        for (Event next : eventLog) {
            eventsList.add(next);
        }
        assertEquals("Event log cleared."
                , eventsList.get(0).getDescription());
        assertEquals("Added a saving goal 'Get a new car' of goal amount $14,800.99"
                , eventsList.get(1).getDescription());
        assertEquals("Contributed $5,000.00 to 'Get a new car'."
                , eventsList.get(2).getDescription());

    }

    @Test
    public void testLogWhenDelete() {
        ledger.addExpense("Mobile 1", 75.60, "Sept 29", "Mobile Bill");
        ledger.addIncome(4468.70,"Company XYZ");
        ledger.setSavingGoal("Get a new car", 14800.99);

        for (Event next : eventLog) {
            eventsList.add(next);
        }

        assertEquals(4,eventsList.size());
        eventsList.clear(); // clear the eventLogs list

        ledger.removeExpense(0);
        ledger.removeIncome(0);
        ledger.removeSavingGoal(0);

        for (Event next : eventLog) {
            eventsList.add(next);
        }

        assertEquals(7,eventsList.size());

        assertEquals("Event log cleared."
                , eventsList.get(0).getDescription());
        assertEquals("Added an expense 'Mobile 1' of amount $75.60"
                , eventsList.get(1).getDescription());
        assertEquals("Added an income 'Company XYZ' of amount $4,468.70"
                , eventsList.get(2).getDescription());
        assertEquals("Added a saving goal 'Get a new car' of goal amount $14,800.99"
                , eventsList.get(3).getDescription());
        assertEquals("Deleted expense 'Mobile 1' of amount $75.60"
                , eventsList.get(4).getDescription());
        assertEquals("Deleted income 'Company XYZ' of amount $4,468.70"
                , eventsList.get(5).getDescription());
        assertEquals("Deleted saving goal 'Get a new car' ($0.00 out of $14,800.99)"
                , eventsList.get(6).getDescription());
    }
}
