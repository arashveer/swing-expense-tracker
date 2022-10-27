package persistence;

import model.Ledger;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import static java.awt.Event.TAB;

public class JsonWriter {
    private PrintWriter writer;
    private String location;

    public JsonWriter(String location) {
        this.location = location;
    }

    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(location));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of workroom to file
    public void write(Ledger ledger) {
        JSONObject json = ledger.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }
}