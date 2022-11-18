package ui;

import model.Expense;
import model.Income;
import model.Ledger;
import model.SavingGoal;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExpenseAppUI extends JFrame {
    private static final int WIDTH = 700; // width of the app
    private static final int HEIGHT = 600; // height of the app
    private JPanel panel;
    private Dashboard dash;
    private ExpensesPanel expensesPanel;
    private IncomePanel incomePanel;
    private GoalsPanel goalsPanel;
    private JTabbedPane tabs;
    private JMenuBar menuBar;
    private JLabel balance;
    Ledger ledger;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/data.json";
    ImageIcon logo = new ImageIcon("./data/logo.png");
    ImageIcon smallLogo = new ImageIcon(logo.getImage().getScaledInstance(620,150, Image.SCALE_DEFAULT));

    public ExpenseAppUI() {
        init();

        setSize(WIDTH, HEIGHT);
        setTitle("Expense Tracker");
        setBackground(ColorUIResource.DARK_GRAY);
        setContentPane(panel);

        createTabs();
        createMenuBar();
        revalidate();
        repaint();

        setLocationRelativeTo(null);  // centers the frame
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        getData();
    }

    private void init() {
        panel = new JPanel();
        tabs = new JTabbedPane();
        tabs.setBackground(ColorUIResource.GRAY);
        tabs.setForeground(ColorUIResource.BLACK);
        menuBar = new JMenuBar();
        ledger = new Ledger();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        balance = new JLabel("0.00");
    }

    private void createTabs() {
        dash = new Dashboard();
        tabs.addTab("Dashboard",dash);

        expensesPanel = new ExpensesPanel();
        tabs.addTab("Expenses",expensesPanel);

        incomePanel = new IncomePanel();
        tabs.addTab("Incomes",incomePanel);

        goalsPanel = new GoalsPanel();
        tabs.addTab("Saving Goals",goalsPanel);

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

        setJMenuBar(menuBar);
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
                int confirmExit = JOptionPane.showConfirmDialog(null,
                        "Do you want to save your file before quitting the app?",
                        "Save before exit",
                        JOptionPane.YES_NO_CANCEL_OPTION);

                if (confirmExit == JOptionPane.YES_OPTION) {
                    jsonWrite();
                }
                if (confirmExit == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    /**
     * Represents the action to be taken when the user wants to create a new ledger
     */
    private class NewFileAction extends AbstractAction {

        NewFileAction() {
            super("Reset");
        }

        @Override
        public void actionPerformed(ActionEvent evt) {
            int confirmNewFile = JOptionPane.showConfirmDialog(null,
                    "Are you sure?",
                    "Create new file",
                    JOptionPane.YES_NO_CANCEL_OPTION);
            if (confirmNewFile == JOptionPane.YES_OPTION) {
                ledger = new Ledger();
                update();
            }
        }
    }

    /**
     * Represents the action to be taken when the user wants to load a ledger
     */
    private class LoadFileAction extends AbstractAction {

        LoadFileAction() {
            super("Sync");
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
                            "Loaded ledger with balance of $"
                                    + String.format("%,.2f",Double.parseDouble(balance.getText()))
                            + " from " + JSON_STORE);
                    update();
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
            super("Save");
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

    public class Dashboard extends JPanel {
        JLabel welcome;
        JLabel info;
        JLabel goalInfo;
        JLabel logoLabel;

        Dashboard() {
            setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
            setBorder(new EmptyBorder(5, 15, 10, 10));
            logoLabel = new JLabel(smallLogo);
            welcomeLabel();
            infoLabel();
            goalLabel();
            addComponents();
        }

        private void welcomeLabel() {
            welcome = new JLabel("<html><h1>Welcome !</h1></html>");
        }

        private void infoLabel() {
            info = new JLabel(homeHtml(
                    String.format("%,.2f",Double.parseDouble(balance.getText())),
                    String.format("%,.2f",ledger.totalIncome()),
                    String.format("%,.2f",ledger.totalExpenses())
            ));
        }

        private String homeHtml(String balance, String income, String expense) {
            return "<html><table width='500px'><tr><td width='33%'>"
                    + "<span style='font-size:14px; color:#8a8a8a;'>Balance</span><br>"
                    + "<span style='font-size:36px;'>$" + balance + "</span></td></tr><tr>"
                    + "<td width='50%'> <span style='font-size:14px; color:#8a8a8a;'>Income</span><br>"
                    + "<span style='font-size:36px;'>$" + income + "</span></td>"
                    + "<td width='50%'> <span style='font-size:14px; color:#8a8a8a;'>Expenses</span><br>"
                    + "<span style='font-size:36px;'>$" + expense + "</span></td></tr></table></html>";
        }

        private void goalLabel() {
            goalInfo = new JLabel();
            if (ledger.getGoals().size() == 0) {
                goalInfo.setText("<html><h2>Latest Saving Goal:</h2><br>You have not created any saving goals.</html>");
                return;
            }
            String data = "<html><h2>Latest Saving Goal: </h2><br>"
                    + ledger.getSavingGoal(ledger.getGoals().size() - 1);
            goalInfo.setText(data);
        }

        public void setInfoLabel() {
            info.setText(homeHtml(
                    String.format("%,.2f",Double.parseDouble(balance.getText())),
                    String.format("%,.2f",ledger.totalIncome()),
                    String.format("%,.2f",ledger.totalExpenses()
                    )
            ));
        }

        private void setGoalLabel() {
            goalInfo.setText("<html><h2>Latest Saving Goal: </h2><br>"
                    + ledger.getSavingGoal((ledger.getGoals()).size() - 1).getName() + ": $"
                    + String.format("%,.2f",ledger.getSavingGoal((ledger.getGoals()).size() - 1).getCurrentAmount())
                    + " out of $"
                    + String.format("%,.2f",ledger.getSavingGoal((ledger.getGoals()).size() - 1).getGoalAmount()));
        }

        private void addComponents() {
            this.add(logoLabel);
            this.add(welcome);
            this.add(info);
            this.add(Box.createRigidArea(new Dimension(0,20)));
            this.add(goalInfo);
        }
    }

    public class IncomePanel extends JPanel {
        JLabel welcome;
        JButton addIncomeBtn;
        JPanel incomesPanel;
        JScrollPane scrollPane;
        GridBagConstraints constraints;
        String[] columnNames = {"Source", "Amount ($)"};
        Object[][] incomesData;

        IncomePanel() {
            setLayout(new GridBagLayout());
            constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            scrollPane = new JScrollPane();
            incomesPanel = new JPanel(new GridLayout());
            setBorder(new EmptyBorder(5, 15, 10, 10));
            welcomeLabel();
            addIncomeButton();
            showIncomes();
        }

        private void welcomeLabel() {
            welcome = new JLabel("<html><h1>List of Incomes</h1></html>");

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 0.5;
            constraints.gridheight = 1;

            this.add(welcome, constraints);
        }

        private void addIncomeButton() {
            addIncomeBtn = new JButton("Add an Income");
            addIncomeBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addIncomePopUp();
                }
            });
            constraints.gridx = 2;
            constraints.gridy = 0;
            constraints.weightx = 0;
            constraints.weighty = 0;

            this.add(addIncomeBtn, constraints);
        }

        private void addIncomePopUp() {
            JPanel panel = new JPanel(new GridLayout(0, 1));
            JTextField source = new JTextField();
            JTextField amount = new JTextField();

            panel.add(new JLabel("Source"));
            panel.add(source);
            panel.add(new JLabel("Amount in ($)"));
            panel.add(amount);
            int result = JOptionPane.showConfirmDialog(null, panel, "Add an Income",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                if (source.getText().isEmpty() || amount.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Adding income failed, please try again.");
                }
                ledger.addIncome(Double.parseDouble(amount.getText()), source.getText());
                jsonWriteNoMsg();
                JOptionPane.showMessageDialog(null, "Income added successfully!");
            }
        }

        private void showIncomes() {
            if (scrollPane.getParent() == incomesPanel) {
                incomesPanel.remove(scrollPane);
            }
            if (ledger.getIncomeList().size() != 0) {
                incomesData = new Object[ledger.getIncomeList().size()][2];
                addIncomesToTable(incomesData);
                JTable table = new JTable(incomesData, columnNames);
                table.setDefaultEditor(Object.class, null);
                table.setRowHeight(30);
                scrollPane = new JScrollPane(table);
                table.setFillsViewportHeight(true);
                incomesPanel.add(scrollPane);
                incomesPanel.revalidate();
                incomesPanel.repaint();
                constraints.gridwidth = 3;
                constraints.gridx = 0;
                constraints.gridy = 1;
                constraints.weighty = 1;
                constraints.weightx = 1;
            }
            this.add(incomesPanel, constraints);
        }

        private void addIncomesToTable(Object[][] incomesData) {
            for (int r = 0; r < ledger.getIncomeList().size(); r++) {
                Income income = ledger.getIncome(r);
                incomesData[r][0] = income.getSource();
                incomesData[r][1] = income.getAmount();
            }
        }
    }

    public class ExpensesPanel extends JPanel {
        JLabel welcome;
        JButton addExpenseBtn;
        JPanel expensesPanel;
        GridBagConstraints constraints;
        JScrollPane scrollPane;
        String[] columnNames = {"Title", "Amount ($)", "Date", "Notes"};
        Object[][] expensesData;

        ExpensesPanel() {
            setLayout(new GridBagLayout());
            constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            scrollPane = new JScrollPane();
            expensesPanel = new JPanel(new GridLayout());
            setBorder(new EmptyBorder(5, 15, 10, 10));
            welcomeLabel();
            addExpenseButton();
            showExpenses();
        }

        private void welcomeLabel() {
            welcome = new JLabel("<html><h1>List of Expenses</h1></html>");

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 0.5;
            constraints.gridheight = 1;

            this.add(welcome, constraints);
        }

        private void addExpenseButton() {
            addExpenseBtn = new JButton("Add an Expense");
            addExpenseBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addExpensePopUp();
                }
            });
            constraints.gridx = 2;
            constraints.gridy = 0;
            constraints.weightx = 0;
            constraints.weighty = 0;

            this.add(addExpenseBtn, constraints);
        }

        private void addExpensePopUp() {
            JPanel panel = new JPanel(new GridLayout(0, 1));
            JTextField name = new JTextField();
            JTextField amount = new JTextField();
            JTextField date = new JTextField();
            JTextField note = new JTextField();
            panel.add(new JLabel("Source"));
            panel.add(name);
            panel.add(new JLabel("Amount in ($)"));
            panel.add(amount);
            panel.add(new JLabel("Date (e.g. 25 Dec, 2022)"));
            panel.add(date);
            panel.add(new JLabel("Note"));
            panel.add(note);
            int result = JOptionPane.showConfirmDialog(null, panel, "Add an Expense",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                if (name.getText().isEmpty() || amount.getText().isEmpty() || date.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Adding expense failed, please try again.");
                }
                ledger.addExpense(name.getText(), Double.parseDouble(amount.getText()),date.getText(),note.getText());
                jsonWriteNoMsg();
                JOptionPane.showMessageDialog(null, "Expense added successfully!");
            }
        }

        private void showExpenses() {
            if (scrollPane.getParent() == expensesPanel) {
                expensesPanel.remove(scrollPane);
            }
            if (ledger.getExpenses().size() != 0) {
                expensesData = new Object[ledger.getExpenses().size()][4];
                addExpensesToTable(expensesData);
                JTable table = new JTable(expensesData, columnNames);
                table.setDefaultEditor(Object.class, null);
                table.setRowHeight(30);
                scrollPane = new JScrollPane(table);
                table.setFillsViewportHeight(true);
                expensesPanel.add(scrollPane);
                expensesPanel.revalidate();
                expensesPanel.repaint();
                constraints.gridwidth = 3;
                constraints.gridx = 0;
                constraints.gridy = 1;
                constraints.weighty = 1;
                constraints.weightx = 1;
            }
            this.add(expensesPanel, constraints);
        }

        private void addExpensesToTable(Object[][] expensesData) {
            for (int r = 0; r < ledger.getExpenses().size(); r++) {
                Expense expense = ledger.getExpense(r);
                expensesData[r][0] = expense.getTitle();
                expensesData[r][1] = expense.getAmount();
                expensesData[r][2] = expense.getDate();
                expensesData[r][3] = expense.getNote();
            }
        }

    }

    public class GoalsPanel extends JPanel {
        JLabel welcome;
        JButton addGoalBtn;
        JButton contributeBtn;
        JPanel goalsPanel;
        JComboBox<String> goalsList;
        JScrollPane scrollPane;
        GridBagConstraints constraints;
        int temp;
        String[] columnNames = {"Title", "Current Amount ($)", "Goal Amount ($)", "Status"};
        Object[][] goalsData;

        GoalsPanel() {
            setLayout(new GridBagLayout());
            constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            scrollPane = new JScrollPane();
            goalsPanel = new JPanel(new GridLayout());
            setBorder(new EmptyBorder(5, 15, 10, 10));
            welcomeLabel();
            addGoalButton();
            contributeButton();
            showGoals();
        }

        private void welcomeLabel() {
            welcome = new JLabel("<html><h1>Saving Goals</h1></html>");

            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weightx = 0.5;
            constraints.gridheight = 1;

            this.add(welcome, constraints);
        }

        private void addGoalButton() {
            addGoalBtn = new JButton("Add a Saving Goal");
            addGoalBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addGoalPopUp();
                }
            });
            constraints.gridx = 1;
            constraints.gridy = 0;
            constraints.weightx = 0;
            constraints.weighty = 0;

            this.add(addGoalBtn, constraints);
        }

        private void contributeButton() {
            contributeBtn = new JButton("Contribute");
            contributeBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    contributePopUp();
                }
            });
            constraints.gridx = 2;
            constraints.gridy = 0;
            constraints.weightx = 0;
            constraints.weighty = 0;

            this.add(contributeBtn, constraints);
        }

        private void addGoalPopUp() {
            JPanel panel = new JPanel(new GridLayout(0, 1));
            JTextField name = new JTextField();
            JTextField goalAmount = new JTextField();

            panel.add(new JLabel("Goal Name"));
            panel.add(name);
            panel.add(new JLabel("Goal Amount in ($)"));
            panel.add(goalAmount);
            int result = JOptionPane.showConfirmDialog(null, panel, "Add a Saving Goal",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                if (name.getText().isEmpty() || goalAmount.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Adding saving goal failed, please try again.");
                }
                ledger.setSavingGoal(name.getText(), Double.parseDouble(goalAmount.getText()));
                jsonWriteNoMsg();
                JOptionPane.showMessageDialog(null, "Saving goal added successfully!");
            }
        }

        private void contributeCombo() {
            String[] chooseGoalData = new String[ledger.getGoals().size()];
            for (int r = 0; r < ledger.getGoals().size(); r++) {
                chooseGoalData[r] = ledger.getSavingGoal(r).getName();
            }
            goalsList = new JComboBox<>(chooseGoalData);
            goalsList.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JComboBox comboBox = (JComboBox) e.getSource();
                    temp = comboBox.getSelectedIndex();
                }
            });
        }

        private void contributePopUp() {
            JPanel panel = new JPanel(new GridLayout(0, 1));
            JTextField amount = new JTextField();
            contributeCombo();
            panel.add(new JLabel("Goal"));
            panel.add(goalsList);
            panel.add(new JLabel("Contribution in ($)"));
            panel.add(amount);
            System.out.println(temp);
            int result = JOptionPane.showConfirmDialog(null, panel, "Contribute to a Goal",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                if (amount.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Contributing to goal failed, please try again.");
                }
                boolean success = ledger.addToSavingGoal(temp, Double.parseDouble(amount.getText()));
                if (!success) {
                    JOptionPane.showMessageDialog(null, "Failed to add contribution");
                    return;
                }
                jsonWriteNoMsg();
                JOptionPane.showMessageDialog(null, "Contribution done successfully!");
            }
        }

        private void showGoals() {
            if (scrollPane.getParent() == goalsPanel) {
                goalsPanel.remove(scrollPane);
            }
            if (ledger.getGoals().size() != 0) {
                goalsData = new Object[ledger.getGoals().size()][4];
                addGoalsToTable(goalsData);
                JTable table = new JTable(goalsData, columnNames);
                table.setDefaultEditor(Object.class, null);
                table.setRowHeight(30);
                scrollPane = new JScrollPane(table);
                table.setFillsViewportHeight(true);
                goalsPanel.add(scrollPane);
                goalsPanel.revalidate();
                goalsPanel.repaint();
                constraints.gridwidth = 3;
                constraints.gridx = 0;
                constraints.gridy = 1;
                constraints.weighty = 1;
                constraints.weightx = 1;
            }
            this.add(goalsPanel, constraints);
        }

        private void addGoalsToTable(Object[][] goalsData) {
            for (int r = 0; r < ledger.getGoals().size(); r++) {
                SavingGoal goal = ledger.getSavingGoal(r);
                goalsData[r][0] = goal.getName();
                goalsData[r][1] = goal.getCurrentAmount();
                goalsData[r][2] = goal.getGoalAmount();
                if (goal.isComplete()) {
                    goalsData[r][3] = "Completed";
                } else {
                    goalsData[r][3] = "Ongoing";
                }
            }
        }
    }


    // EFFECTS: saves the ledger to file
    private void jsonWrite() {
        try {
            jsonWriter.open();
            jsonWriter.write(ledger);
            jsonWriter.close();
            update();
            JOptionPane.showMessageDialog(null,"Saved data to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(null,"Unable to write to file: " + JSON_STORE);
        }
    }

    // EFFECTS: for writing ledger to json without any popups showing
    private void jsonWriteNoMsg() {
        try {
            jsonWriter.open();
            jsonWriter.write(ledger);
            jsonWriter.close();
            update();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    private void getData() {
        try {
            ledger = jsonReader.read();
            update();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Unable to read from file: " + JSON_STORE);
        }
    }

    /*
     * Updates the components to display new data
     */
    private void update() {
        balance.setText(String.valueOf(ledger.getBalance()));
        dash.setInfoLabel();
        if (ledger.getGoals().size() != 0) {
            dash.setGoalLabel();
        }
        expensesPanel.showExpenses();
        incomePanel.showIncomes();
        goalsPanel.showGoals();
    }
}
