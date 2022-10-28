package persistence;

import model.Ledger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {
    Ledger ledger;

    @BeforeEach
    void setup() {
        ledger = new Ledger();
    }

    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyLedger() {
        try {
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyLedger.json");
            writer.open();
            writer.write(ledger);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyLedger.json");
            ledger = reader.read();
            assertEquals(0, ledger.getBalance());
            assertEquals(0,ledger.getIncomeList().size());
            assertEquals(0,ledger.getExpenses().size());
            assertEquals(0,ledger.getGoals().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralLedger() {
        try {
            ledger.addIncome(28990.12,"ABC Corp.");
            ledger.addExpense("Uber Eats", 67.34, "Oct 27", "Food Delivery");
            ledger.setSavingGoal("Buy a new car", 12500);
            ledger.addToSavingGoal(0,3300);
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralLedger.json");
            writer.open();
            writer.write(ledger);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralLedger.json");
            ledger = reader.read();
            assertEquals(25622.78, ledger.getBalance());
            checkExpense("Uber Eats", 67.34, "Oct 27", "Food Delivery", ledger.getExpense(0));
            checkIncome("ABC Corp.", 28990.12, ledger.getIncome(0));
            checkGoal("Buy a new car", 12500, 3300, false, ledger.getSavingGoal(0));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
