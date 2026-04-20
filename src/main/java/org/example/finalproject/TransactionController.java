package org.example.finalproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.example.finalproject.model.Transaction;
import org.example.finalproject.service.ExpenseTracker;

import java.time.LocalDate;
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
    private ChoiceBox<String> cbMonth;

    @FXML
    private ChoiceBox<String> cbYear;

    @FXML
    private TableColumn<Transaction, Float> colAmount;

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
    private TableColumn<Transaction, Float> colExpenseAmount;

    @FXML
    private TableColumn<Transaction, String> colExpenseCategory;

    @FXML
    private DatePicker dpDate;

    @FXML
    private DatePicker dpMonthSelector;

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

    Set<Integer> years = Set.copyOf(java.util.stream.IntStream.rangeClosed(1900, 2030)
        .boxed()
        .toList());

    Set<String> months = Set.of("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December");

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

    @FXML
    void onAddTransactionClick(MouseEvent event) {
        var response = ExpenseTracker.IsValidTransaction(dpDate.getValue(), cbType.getValue(), cbCategory.getValue(), txtAmount.getText());
        if (!response.isValid()) {
            showError(response.message());
            return;
        }

        boolean isSaved = ExpenseTracker.AddTransaction(new Transaction(
                dpDate.getValue(), cbType.getValue(), cbCategory.getValue(), Float.parseFloat(txtAmount.getText()), txtDescription.getText()));

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
                id, dpDate.getValue(), cbType.getValue(), cbCategory.getValue(), Float.parseFloat(txtAmount.getText()), txtDescription.getText()));

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
        tblTransactions.setItems(FXCollections.observableArrayList(ExpenseTracker.getAllTransactions()));
    }

    private void loadTotalExpenses() {
        tblTotalExpenses.setItems(FXCollections.observableArrayList(ExpenseTracker.getAllTransactions()));
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
        cbFilterType.setValue("Select a filter type");
        cbFilterCategory.setValue("Select a filter type first.");
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
                new javafx.beans.property.SimpleFloatProperty(data.getValue().getAmount()).asObject());

        colDescription.setCellValueFactory(data ->
                new javafx.beans.property.SimpleStringProperty(data.getValue().getDescription()));
    }

    @FXML
    public void initialize()
    {
        LoadTransactionsTable();
        disableTextFields();

        cbType.getItems().addAll(types);
        cbFilterType.getItems().addAll(types);
        cbType.setValue("Select Type");
        cbCategory.setValue("Select a type first.");
        setDefaultFilterDropdownValues();

        cbType.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) ->
        {
            cbCategory.getItems().setAll(categoryMap.get(newVal));
            cbCategory.setValue("");
        });

        cbFilterType.getSelectionModel().selectedItemProperty().addListener((_, _, newVal) ->
        {
            cbFilterCategory.getItems().setAll(categoryMap.get(newVal));
            cbFilterType.setValue("");
        });
    }


}
