package persistence;

import model.Ledger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonReaderTest extends JsonTest {
    Ledger ledger;

    @BeforeEach
    void setup() {
        ledger = new Ledger();
    }

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ledger = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyLedger() {
        JsonReader reader = new JsonReader("./data/testEmptyLedger.json");
        try {
            ledger = reader.read();
            assertEquals(0, ledger.getBalance());
            assertEquals(0,ledger.getIncomeList().size());
            assertEquals(0,ledger.getExpenses().size());
            assertEquals(0,ledger.getGoals().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralLedger() {
        JsonReader reader = new JsonReader("./data/testGeneralLedger.json");
        try {
            ledger = reader.read();assertEquals(25622.78, ledger.getBalance());
            checkExpense("Uber Eats", 67.34, "Oct 27", "Food Delivery", ledger.getExpense(0));
            checkIncome("ABC Corp.", 28990.12, ledger.getIncome(0));
            checkGoal("Buy a new car", 12500, 3300, false, ledger.getSavingGoal(0));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
