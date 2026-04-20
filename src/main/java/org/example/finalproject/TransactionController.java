package org.example.finalproject;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.example.finalproject.model.Transaction;
import org.example.finalproject.service.ExpenseTracker;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TransactionController
{

    @FXML
    private Button btnAdd;

    @FXML
    private Button btnClear;

    @FXML
    private Button btnDelete;

    @FXML
    private Button btnResetFilter;

    @FXML
    private Button btnApplyFilter;

    @FXML
    private Button btnUpdate;

    @FXML
    private ChoiceBox<String> cbCategory;

    @FXML
    private ChoiceBox<String> cbFilterCategory;

    @FXML
    private ChoiceBox<String> cbFilterType;

    @FXML
    private ChoiceBox<String> cbType;

    @FXML
    private TableColumn<Transaction, Double> colAmount;

    @FXML
    private TableColumn<Transaction, String> colCategory;

    @FXML
    private TableColumn<Transaction, String> colDate;

    @FXML
    private TableColumn<Transaction, String> colDescription;

    @FXML
    private TableColumn<Transaction, Integer> colId;

    @FXML
    private TableColumn<Transaction, String> colType;

    @FXML
    private TableColumn<Transaction, Double> colExpenseAmount;

    @FXML
    private TableColumn<Transaction, String> colExpenseCategory;

    @FXML
    private DatePicker dpDate;

    @FXML
    private DatePicker dpMonthSelectorEnd;

    @FXML
    private DatePicker dpMonthSelectorStart;

    @FXML
    private TableView<Transaction> tblTransactions;

    @FXML
    private TableView<Transaction> tblTotalExpenses;

    @FXML
    private TextField txtAmount;

    @FXML
    private TextField txtCurrentBalance;

    @FXML
    private TextField txtDescription;

    @FXML
    private TextField txtHighestExpense;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtTotalExpense;

    @FXML
    private TextField txtTotalIncome;

    Set<String> types = Set.of("Income", "Expense");

    Set<String> incomeCategories = Set.of("Salary", "Bonus", "Gift", "Other");

    Set<String> expenseCategories = Set.of("Food", "Rent", "Transport", "Shopping", "Bills", "Entertainment", "Other");

    Map<String, Set<String>> categoryMap = Map.of("Income", incomeCategories, "Expense", expenseCategories);

    private void showError(String message) {
        ShowAlert(Alert.AlertType.ERROR, "Error", "Error occurred", message);
    }

    void ShowAlert(Alert.AlertType type, String title, String header, String content){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void fillFormFromSelectedRow() {
        Transaction selectedTransaction = tblTransactions.getSelectionModel().getSelectedItem();

        if (selectedTransaction == null) {
            return;
        }

        txtId.setText(Integer.toString(selectedTransaction.getTransactionId()));
        dpDate.setValue(selectedTransaction.getDate());
        cbType.setValue(selectedTransaction.getType());
        cbCategory.setValue(selectedTransaction.getCategory());
        txtAmount.setText(String.valueOf(selectedTransaction.getAmount()));
        txtDescription.setText(selectedTransaction.getDescription());
    }

    private void loadTransactions() {
        var transactions = ExpenseTracker.getAllTransactions();
        tblTransactions.setItems(FXCollections.observableArrayList(transactions));
        loadReport(transactions);
    }

    private void loadExpensesTable(List<Transaction> transactions) {
        tblTotalExpenses.setItems(FXCollections.observableArrayList(transactions));
    }

    private void loadFilteredTransactions() {
        String type = cbFilterType.getValue();
        String category = cbFilterCategory.getValue();
        var startDate = dpMonthSelectorStart.getValue();
        var endDate = dpMonthSelectorEnd.getValue();

        var transactions = ExpenseTracker.filterTransactions(type, category, startDate, endDate);
        tblTransactions.setItems(FXCollections.observableArrayList(transactions));
        loadReport(transactions);
    }

    private void disableTextFields() {
        txtId.setDisable(true);
        txtTotalIncome.setEditable(false);
        txtTotalExpense.setEditable(false);
        txtCurrentBalance.setEditable(false);
        txtHighestExpense.setEditable(false);
    }

    private boolean transactionExists(int id) {
        return ExpenseTracker.idExists(id);
    }

    private void setDefaultFilterDropdownValues(){
        cbType.setValue("Select Type");
        cbCategory.getItems().addAll("Select a type first.");
        cbFilterType.setValue("Select a filter type");
        cbFilterCategory.getItems().addAll("Select a filter type first.");
        dpMonthSelectorStart.setValue(null);
        dpMonthSelectorEnd.setValue(null);
    }

    private void initDropDownItems(){
        cbType.getItems().addAll(types);
        cbFilterType.getItems().addAll(types);

        cbType.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) ->
        {
            cbCategory.getItems().setAll(categoryMap.get(newVal));
            cbCategory.setValue("");
        });

        cbFilterType.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) ->
        {
            cbFilterCategory.getItems().setAll(categoryMap.get(newVal));
            cbFilterCategory.setValue("");
        });

    }

    private void LoadTransactionsTable(){
        loadTransactions();
        colId.setCellValueFactory(data ->
                new javafx.beans.property.SimpleIntegerProperty(data.getValue().getTransactionId()).asObject());

        colDate.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getDate().toString()));

        colType.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getType()));

        colCategory.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getCategory()));

        colAmount.setCellValueFactory(data ->
                new javafx.beans.property.SimpleDoubleProperty(data.getValue().getAmount()).asObject());

        colDescription.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));
    }

    private void LoadExpensesReportTable(){
        colExpenseCategory.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getCategory()));

        colExpenseAmount.setCellValueFactory(data ->
                new javafx.beans.property.SimpleDoubleProperty(data.getValue().getAmount()).asObject());
    }

    private void loadReport(List<Transaction> transactions){
        var totalIncome = ExpenseTracker.getTotalIncome(transactions);
        var totalExpense = ExpenseTracker.getTotalExpense(transactions);
        var currentBalance = ExpenseTracker.getBalance(transactions);
        var highestExpense = ExpenseTracker.getHighestExpenseCategory(transactions);
        setReportValues(totalIncome, totalExpense, currentBalance, highestExpense);
        loadExpensesTable(transactions);
    }

    private void setReportValues(double totalIncome, double totalExpense, double currentBalance, String highestExpense){
        txtTotalIncome.setText(String.valueOf(totalIncome));
        txtTotalExpense.setText(String.valueOf(totalExpense));
        txtCurrentBalance.setText(String.valueOf(currentBalance));
        txtHighestExpense.setText(highestExpense);
    }

    @FXML
    void onAddTransactionClick(MouseEvent event) {
        var response = ExpenseTracker.IsValidTransaction(dpDate.getValue(), cbType.getValue(), cbCategory.getValue(), txtAmount.getText());
        if (!response.isValid()) {
            showError(response.message());
            return;
        }

        boolean isSaved = ExpenseTracker.AddTransaction(new Transaction(
                dpDate.getValue(), cbType.getValue(), cbCategory.getValue(), Double.parseDouble(txtAmount.getText()), txtDescription.getText()));

        if (!isSaved) {
            showError("Failed to save transaction");
            return;
        }

        loadTransactions();
        ShowAlert(Alert.AlertType.CONFIRMATION, "Success", "Successful", "Record Created Successfully");
        onClearFieldsClick(null);
    }

    @FXML
    void onApplyFilterClick(MouseEvent event) {
        loadFilteredTransactions();
    }

    @FXML
    void onResetFilterClick(MouseEvent event) {
        setDefaultFilterDropdownValues();
        loadTransactions();
    }

    @FXML
    void onClearFieldsClick(MouseEvent event) {
        txtId.clear();
        dpDate.setValue(null);
        txtDescription.clear();
        cbType.setValue("Select Type");
        cbCategory.setValue("Select a type first.");
        txtAmount.clear();
        txtDescription.clear();
    }

    @FXML
    void onDeleteTransactionClick(MouseEvent event) {
        int id = Integer.parseInt(txtId.getText());
        if (!transactionExists(id)) {
            showError("Transaction does not exist");
            return;
        }

        var isDeleted = ExpenseTracker.deleteTransaction(Integer.parseInt(txtId.getText()));
        if (!isDeleted) {
            showError("Failed to delete transaction");
            return;
        }

        loadTransactions();
        ShowAlert(Alert.AlertType.CONFIRMATION, "Success", "Successful", "Record Deleted Successfully");
        onClearFieldsClick(null);
    }

    @FXML
    void onUpdateTransactionClick(MouseEvent event) {
        int id = Integer.parseInt(txtId.getText());
        if (!transactionExists(id)) {
            showError("Transaction does not exist");
            return;
        }

        var response = ExpenseTracker.IsValidTransaction(dpDate.getValue(), cbType.getValue(), cbCategory.getValue(), txtAmount.getText());
        if (!response.isValid()) {
            showError(response.message());
            return;
        }

        boolean isSaved = ExpenseTracker.UpdateTransaction(new Transaction(
                id, dpDate.getValue(), cbType.getValue(), cbCategory.getValue(), Double.parseDouble(txtAmount.getText()), txtDescription.getText()));

        if (!isSaved) {
            showError("Failed to update transaction");
            return;
        }

        loadTransactions();
        ShowAlert(Alert.AlertType.CONFIRMATION, "Success", "Successful", "Record Updated Successfully");
        onClearFieldsClick(null);
    }

    @FXML
    void onTblTransactionsClick(MouseEvent event){
        if (event.getClickCount() == 1) {
            fillFormFromSelectedRow();
        }
    }

    @FXML
    public void initialize()
    {
        LoadTransactionsTable();
        LoadExpensesReportTable();
        disableTextFields();
        setDefaultFilterDropdownValues();
        initDropDownItems();
    }


}
