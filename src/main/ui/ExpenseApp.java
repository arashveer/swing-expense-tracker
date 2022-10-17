package ui;

import model.Ledger;

import java.util.Scanner;

// Expense tracker application
public class ExpenseApp {

    // Create a ledger
    Ledger ledger;
    private Scanner input;

    // EFFECTS:  runs the tracker application
    public ExpenseApp() {
        runApp();
    }

    private void runApp() {
        boolean keepGoing = true;
        String command = null;

        init();

        System.out.println("Welcome to this expense tracker app!");

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS: initializes ledger
    private void init() {
        ledger = new Ledger();
        input = new Scanner(System.in);
        input.useDelimiter("\n");
    }

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
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    private  void processAdd() {
        System.out.println("Select from:");
        System.out.println("\tincome -> add an income");
        System.out.println("\texpense -> add an expense");
        System.out.println("\tgoal -> set a saving goal");
        String toAdd = input.next();
        switch (toAdd) {
            case "income":
                addAnIncome();
                break;
            case "expense":
                addAnExpense();
                break;
            case "goal":
                addSavingGoal();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    private void addSavingGoal() {
        System.out.print("\nInformation required to add a saving goal: \nName: ");
        String goalName = input.next();
        System.out.print("Goal amount: $");
        double goalAmount = input.nextDouble();
        ledger.setSavingGoal(goalName, goalAmount);
    }

    private void addAnIncome() {
        System.out.print("\nInformation required to add an income: \nSource: ");
        String incomeSource = input.next();
        System.out.print("Amount: $");
        double incomeAmount = input.nextDouble();
        ledger.addIncome(incomeAmount,incomeSource);
    }

    private void addAnExpense() {
        if (ledger.getBalance() <= 0) {
            System.out.println("\nYou have not added an income yet, balance is $0.");
            System.out.println("Want to add an income source first? (y to add): ");
            String addOrNot = input.next();
            addOrNot = addOrNot.toLowerCase();
            if (addOrNot.equals("y")) {
                addAnIncome();
            }
        } else {
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
    }

    private void displayMenu() {
        System.out.println("\nYour current balance is $" + ledger.getBalance());
        System.out.println("\nSelect from:");
        System.out.println("\tadd -> Add (income, expense, or saving goal)");
        System.out.println("\tshow -> Show your summary");
        System.out.println("\texpenses -> Show list of all your expenses");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: Prints one expense with all its info
    private String showAnExpense(Ledger ledger, int index) {
        return (ledger.getExpense(index).getTitle() + ": " + ledger.getExpense(index).getNote()
                + "\nAmount: $" + ledger.getExpense(index).getAmount()
                + "\nDate: " + ledger.getExpense(index).getDate()
                + "\n");
    }

    // EFFECTS: Prints an income
    private String showAnIncome(Ledger ledger, int index) {
        return (ledger.getIncome(index).getSource() + ": $" + ledger.getIncome(index).getAmount() + "\n");
    }

    // EFFECTS: Prints a saving goal
    private String showAGoal(Ledger ledger, int index) {
        return (ledger.getSavingGoal(index).getName() + ": $" + ledger.getSavingGoal(index).getCurrentAmount()
                + "out of $" + ledger.getSavingGoal(index).getGoalAmount());
    }

    private void showExpenses() {
        if (ledger.getExpenses().size() == 0) {
            System.out.println("You have not added any expenses yet.");
        } else {
            for (int i = 0; i < ledger.getExpenses().size(); i++) {
                System.out.println(showAnExpense(ledger, i));
            }
        }
    }

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
        System.out.println("\nTotal Income: $" + totalIncome);
        System.out.println("\nTotal expenditure : $" + totalExpense);
        System.out.println("\nSaving Goal: ");
        if (ledger.getGoals().size() == 0) {
            System.out.println("NULL");
        } else {
            for (int i = 0; i < ledger.getGoals().size(); i++) {
                System.out.println(showAGoal(ledger, i));
            }
        }
    }

}
