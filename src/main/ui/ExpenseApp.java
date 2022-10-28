package ui;

import model.Ledger;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;

// Expense tracker application
public class ExpenseApp {

    // Initialize
    Ledger ledger;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    DecimalFormat dec = new DecimalFormat("#0.00");
    private static final String JSON_STORE = "./data/data.json";

    // EFFECTS:  runs the tracker application
    public ExpenseApp() {
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input and responsible for termination of app
    private void runApp() {
        boolean keepGoing = true;
        String command = null;

        init();

        System.out.println("Welcome to this expense tracker app!\nDo you want to load your old data? (y to load)");
        command = input.next().toLowerCase();
        if (command.equals("y")) {
            loadLedger();
        }

        while (keepGoing) {
            displayMenu();
            command = input.next().toLowerCase();
            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("Do you want to save before quitting the app? Type y for yes.");
        command = input.next().toLowerCase();
        if (command.equals("y")) {
            saveLedger();
        }

        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS: creates the ledger, input scanner, and JSON reader and writer
    private void init() {
        ledger = new Ledger();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // EFFECTS: processes user input
    private void processCommand(String command) {
        switch (command) {
            case "add":
                processAdd();
                break;
            case "show":
                showSummary();
                break;
            case "expenses":
                showExpenses();
                break;
            case "save":
                saveLedger();
                break;
            case "load":
                loadLedger();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nYour current balance is $" + dec.format(ledger.getBalance()));
        System.out.println("\nSelect from:");
        System.out.println("\tadd -> Add (income, expense, or saving goal)");
        System.out.println("\tshow -> Show your summary");
        System.out.println("\texpenses -> Show list of all your expenses");
        System.out.println("\tsave -> Save your data");
        System.out.println("\tload -> Load your data");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: displays menu for adding or modifying our objects
    private  void processAdd() {
        System.out.println("Select from:");
        System.out.println("\tincome -> add an income");
        System.out.println("\texpense -> add an expense");
        System.out.println("\tgoal -> set a saving goal");
        System.out.println("\tcon -> contribute to saving goal");
        String toAdd = input.next();
        switch (toAdd) {
            case "income":
                addAnIncome();
                break;
            case "expense":
                addAnExpenseWithoutIncome();
                break;
            case "goal":
                addSavingGoal();
                break;
            case "con":
                addToSavingGoal();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: prompt user to add a saving goal to their ledger
    private void addSavingGoal() {
        System.out.print("\nInformation required to add a saving goal: \nName: ");
        String goalName = input.next();
        System.out.print("Goal amount: $");
        double goalAmount = input.nextDouble();
        ledger.setSavingGoal(goalName, goalAmount);
    }

    // MODIFIES: this
    // EFFECTS: prompt user to add an income to their ledger
    private void addAnIncome() {
        System.out.print("\nInformation required to add an income: \nSource: ");
        String incomeSource = input.next();
        System.out.print("Amount: $");
        double incomeAmount = input.nextDouble();
        ledger.addIncome(incomeAmount,incomeSource);
    }

    // EFFECTS: prompt user to ask if they want to add income first
    private void addAnExpenseWithoutIncome() {
        if (ledger.getBalance() <= 0) {
            System.out.println("\nYou have not added an income yet, balance is $0.");
            System.out.println("Want to add an income source first? (y to add): ");
            String addOrNot = input.next();
            addOrNot = addOrNot.toLowerCase();
            if (addOrNot.equals("y")) {
                addAnIncome();
            } else {
                addAnExpense();
            }
        } else {
            addAnExpense();
        }
    }

    // MODIFIES: this
    // EFFECTS: prompt user to add an expense to their ledger
    private void addAnExpense() {
        System.out.print("\nInformation required to add an expense: \nSource: ");
        String expenseSource = input.next();
        System.out.print("Amount: $");
        double expenseAmount = input.nextDouble();
        System.out.print("Date: ");
        String expenseDate = input.next();
        System.out.print("Note(if any): ");
        String expenseNote = input.next();
        ledger.addExpense(expenseSource, expenseAmount, expenseDate, expenseNote);
    }

    // MODIFIES: this
    // EFFECTS: prompt user for goal and contribution(value) of saving goal to add
    private void addToSavingGoal() {
        System.out.println("\nChoose a saving goal: ");
        for (int i = 0; i < (ledger.getGoals()).size(); i++) {
            System.out.println(i + ": " + ledger.getSavingGoal(i).getName());
        }
        int goal = input.nextInt();
        if (goal > ledger.getGoals().size()) {
            System.out.println("Wrong input. Please enter index of a goal from the list: ");
            goal = input.nextInt();
        }
        System.out.print("\nHow much you want to contribute to the saving goal: $");
        double value = input.nextDouble();
        ledger.addToSavingGoal(goal, value);
    }

    // ----- Methods to display info to user -----

    // EFFECTS: prints an expense object
    private String showAnExpense(Ledger ledger, int index) {
        return (ledger.getExpense(index).getTitle() + ": " + ledger.getExpense(index).getNote()
                + "\nAmount: $" + dec.format(ledger.getExpense(index).getAmount())
                + "\nDate: " + ledger.getExpense(index).getDate()
                + "\n");
    }

    // EFFECTS: prints an income object
    private String showAnIncome(Ledger ledger, int index) {
        return (ledger.getIncome(index).getSource() + ": $" + dec.format(ledger.getIncome(index).getAmount()) + "\n");
    }

    // EFFECTS: prints a saving goal object
    private String showAGoal(Ledger ledger, int index) {
        return (ledger.getSavingGoal(index).getName() + ": $"
                + dec.format(ledger.getSavingGoal(index).getCurrentAmount())
                + " out of $" + dec.format(ledger.getSavingGoal(index).getGoalAmount()));
    }

    // EFFECTS: prints out a list of all the expenses
    private void showExpenses() {
        if (ledger.getExpenses().size() == 0) {
            System.out.println("You have not added any expenses yet.");
        } else {
            for (int i = 0; i < ledger.getExpenses().size(); i++) {
                System.out.println(showAnExpense(ledger, i));
            }
        }
    }

    // EFFECTS: prints a summary of this ledger
    private void showSummary() {
        double totalIncome = 0;
        for (int i = 0; i < ledger.getIncomeList().size(); i++) {
            totalIncome += ledger.getIncome(i).getAmount();
        }
        double totalExpense = 0;
        for (int i = 0; i < ledger.getExpenses().size(); i++) {
            totalExpense += ledger.getExpense(i).getAmount();
        }
        System.out.println("\n------ SUMMARY ------");
        System.out.println("\nTotal Income: $" + dec.format(totalIncome));
        System.out.println("\nTotal expenditure : $" + dec.format(totalExpense));
        System.out.println("\nSaving Goal: ");
        if (ledger.getGoals().size() == 0) {
            System.out.println("NULL");
        } else {
            for (int i = 0; i < ledger.getGoals().size(); i++) {
                System.out.println(showAGoal(ledger, i));
            }
        }
    }

    // ----- Methods to save and load data -----

    // EFFECTS: saves the ledger to file
    private void saveLedger() {
        try {
            jsonWriter.open();
            jsonWriter.write(ledger);
            jsonWriter.close();
            System.out.println("Saved data to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads ledger from file
    private void loadLedger() {
        try {
            ledger = jsonReader.read();
            System.out.println("Loaded ledger with balance of $" + dec.format(ledger.getBalance())
                    + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
