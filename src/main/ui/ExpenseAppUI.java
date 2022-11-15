package ui;

import model.Ledger;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

public class ExpenseAppUI extends JFrame {
    private static final int WIDTH = 700; // width of the app
    private static final int HEIGHT = 600; // height of the app
    private JPanel panel;
    private JFrame frame; // main frame
    private JTabbedPane tabs;
    private JMenuBar menuBar;
    Ledger ledger;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    DecimalFormat dec = new DecimalFormat("#0.00");
    private static final String JSON_STORE = "./data/data.json";

    public ExpenseAppUI() {
        init();

        frame.setSize(WIDTH, HEIGHT);
        setContentPane(panel);

        panel.setLayout(new BorderLayout());
        createTabs();
        createMenuBar();

        frame.add(panel,BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);  // centers the frame
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void init() {
        frame = new JFrame("Expense Tracker");
        panel = new JPanel();
        tabs = new JTabbedPane();
        menuBar = new JMenuBar();

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    private void createTabs() {
        JComponent dash = makeTextPanel("Dashboard");
        tabs.addTab("Dashboard",dash);

        JComponent expensesPanel = makeTextPanel("Expenses");
        tabs.addTab("Expenses",expensesPanel);

        tabs.setPreferredSize(new Dimension(670,550));
        panel.add(tabs);
    }

    /**
     *  MENU BAR
     */
    private void createMenuBar() {
        JMenu file = new JMenu("File");
        JMenu exit = new JMenu("Exit");
        file.setToolTipText("View options to load, save and create new ledger.");
        exit.setToolTipText("Exit application");
        file.setMnemonic('N');
        exit.setMnemonic('E');

        addMenuItem(file, new NewFileAction(),
                KeyStroke.getKeyStroke("control N"));
        addMenuItem(file, new LoadFileAction(),
                KeyStroke.getKeyStroke("control L"));
        addMenuItem(file, new SaveFileAction(),
                KeyStroke.getKeyStroke("control S"));
        menuExit(exit);
        // add menu items to menu bar
        menuBar.add(file);
        menuBar.add(exit);

        frame.setJMenuBar(menuBar);
    }

    /**
     * Adds an item with given handler to the given menu
     * @param theMenu  menu to which new item is added
     * @param action   handler for new menu item
     * @param accelerator    keystroke accelerator for this menu item
     */
    private void addMenuItem(JMenu theMenu, AbstractAction action, KeyStroke accelerator) {
        JMenuItem menuItem = new JMenuItem(action);
        menuItem.setMnemonic(menuItem.getText().charAt(0));
        menuItem.setAccelerator(accelerator);
        theMenu.add(menuItem);
    }

    /*
     * Represents the action to be taken when the user clicks exit on menu bar
     */
    private void menuExit(JMenu exit) {
        exit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int confirmNewFile = JOptionPane.showConfirmDialog(null,
                        "Do you want to save your file before quitting the app?",
                        "Save before exit",
                        JOptionPane.YES_NO_OPTION);

                if (confirmNewFile == JOptionPane.YES_OPTION) {
                    jsonWrite();
                }
                System.exit(0);
            }
        });
    }

    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

    /**
     * Represents the action to be taken when the user wants to create a new ledger
     */
    private class NewFileAction extends AbstractAction {

        NewFileAction() {
            super("New file");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            int confirmNewFile = JOptionPane.showConfirmDialog(null,
                    "Are you sure?",
                    "Create new file",
                    JOptionPane.YES_NO_CANCEL_OPTION);
            if (confirmNewFile == JOptionPane.YES_OPTION) {
                ledger = new Ledger();
            }
        }
    }

    /**
     * Represents the action to be taken when the user wants to load a ledger
     */
    private class LoadFileAction extends AbstractAction {

        LoadFileAction() {
            super("Load file");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            int confirmLoadFile = JOptionPane.showConfirmDialog(null,
                    "Do you want to load your data?",
                    "Load data",
                    JOptionPane.YES_NO_CANCEL_OPTION);
            if (confirmLoadFile == JOptionPane.YES_OPTION) {
                try {
                    ledger = jsonReader.read();
                    JOptionPane.showMessageDialog(null,
                            "Loaded ledger with balance of $" + dec.format(ledger.getBalance())
                            + " from " + JSON_STORE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,"Unable to read from file: " + JSON_STORE);
                }
            }

        }
    }

    /**
     * Represents the action to be taken when the user wants to save a ledger
     */
    private class SaveFileAction extends AbstractAction {

        SaveFileAction() {
            super("Save file");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            int confirmNewFile = JOptionPane.showConfirmDialog(null,
                    "Do you want to save your file",
                    "Save data",
                    JOptionPane.YES_NO_CANCEL_OPTION);

            if (confirmNewFile == JOptionPane.YES_OPTION) {
                jsonWrite();
            }
        }
    }

    // EFFECTS: saves the ledger to file
    private void jsonWrite() {
        try {
            jsonWriter.open();
            jsonWriter.write(ledger);
            jsonWriter.close();
            JOptionPane.showMessageDialog(null,"Saved data to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"Unable to write to file: " + JSON_STORE);
        }
    }

    public static void main(String[] args) {
        new ExpenseAppUI();
    }
}
