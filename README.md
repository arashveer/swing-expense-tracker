# Expense Tracker

## Manage money responsibly

### Abstract

In order to start saving money, we have to understand where our income is being spent.
This expense tracker app will help to understand how the money is being spent, so we can make changes in 
spending habits and stay on top of finances. It aims to assist user in reducing their expenses and put more
money towards savings and emergency funds, and improve financial security. This application can be of use to
anyone who wants to have control of their money.

I wanted to create this app because I have always wanted to understand my spending habits and have been using
spreadsheets to simulate a budget app. This app will help me automate most of the basic budgeting and also provide an appealing user interface
to make budgeting easier.

### User Stories

- As a user, I want to add my income sources and view my available money.
- As a user, I want to see a breakdown of my spending's.
- As a user, I want to make notes on some of my transactions.
- As a user, I want to set up saving goals.
- As a user, I want to save my ledger data to a file.
- As a user, I want to load my ledger data from a file. 
- As a user, I want to delete any income, expense or saving goal.
- As a user, I want to be given the option to load my data.
- As a user, when I close the app, I want to save my data.

### Instructions for Grader

- You can generate the first required event related to adding expense to an expenses by clicking on Expenses tab.
  Once on expenses panel, click on "Add" button which will show a popup window.
  Type your info in this popup and click Yes. Now, your expense will be shown in expenses list.
  You can also choose to "Delete" an expense after clicking on an expense in the list and then confirming in the popup window.
- You can generate the second required event related to adding income to an income list by clicking on Incomes tab.
  Once on incomes panel, click on "Add" button which will show a popup window.
  Type your info in this popup and click Yes. Now, your income will be shown in incomes list.
  You can also choose to "Delete" an income after clicking on an income in the list and then confirming in the popup window.
- You can generate the third event related to adding saving goal to a goals by clicking on Saving Goals tab.
  Once on saving goals panel, click on "Add" button which will show a popup window.
  Type your info in this popup and click Yes. Now, your saving goal will be shown in saving goals list.
  Now, in the same tab, click on "Contribute" button next to "Add a Saving Goal" button.
  This will bring up another popup window, where you can choose a goal from combobox.
  Type in the amount to contribute to that goal and click OK.
  Now your saving goal will have its current amount increased by the contribution amount.
  You can also choose to "Delete" a saving goal after clicking on a goal in the list and then confirming in the popup window.
- You can locate my visual component by clicking on the dashboard tab.
  It is a logo of this application at top of the dashboard. I created this logo using an online tool called Figma.
- You can save the state of my application by clicking on "Save" in File menu.
  Your data is also synced after every creation of an object.
- You can reload the state of my application by clicking on "Load" in File menu.

### Phase 4: Task 2

- Wed Nov 30 20:02:46 PST 2022  
Added an expense 'BestBuy' of amount $24.50

- Wed Nov 30 20:03:09 PST 2022  
Added an income 'ICBC' of amount $2,534.10

- Wed Nov 30 20:04:10 PST 2022    
Added a saving goal 'TV' of goal amount $800.00

- Wed Nov 30 20:04:38 PST 2022  
Contributed $450.00 to 'TV'.

- Wed Nov 30 20:04:48 PST 2022  
Deleted saving goal 'Save for a desktop' ($1,200.00 out of $1,200.00)

- Wed Nov 30 20:05:04 PST 2022  
Deleted expense 'Save on Food' of amount $124.23

- Wed Nov 30 20:05:20 PST 2022  
Deleted income 'Birthday Gift' of amount $5,000.00

### Phase 4: Task 3

- I would have liked to extract private nested classes such as Dashboard, ExpensesPanel, IncomePanel and GoalsPanel
from ExpenseAppUI into a panels package inside ui package. This would result in clear distinction between these panels
and also make them easier to debug. 
- Further, for the panels package, I would have created an abstract panel to make it easier and faster to work on the
various panel classes.
- I would have liked to implement Single Responsibility Principle to Ledger class in model. It could be possible
to split the Ledger class into three classes to handle incomes list, expenses list and saving goals list in order to
increase cohesion. This could also help to reduce the coupling between the classes in model package.
