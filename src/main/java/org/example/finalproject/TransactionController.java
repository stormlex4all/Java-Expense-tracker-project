package org.example.finalproject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.example.finalproject.model.Transaction;

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
    private Button btnShowAll;

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
    private TableColumn<Transaction, Float> colAmount;

    @FXML
    private TableColumn<Transaction, String> colCategory;

    @FXML
    private TableColumn<Transaction, LocalDate> colDate;

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

    Set<String> types = Set.of("Income", "Expense");

    Set<String> incomeCategories = Set.of("Salary", "Bonus", "Gift", "Other");

    Set<String> expenseCategories = Set.of("Food", "Rent", "Transport", "Shopping", "Bills", "Entertainment", "Other");

    Map<String, Set<String>> categoryMap = Map.of("Income", incomeCategories, "Expense", expenseCategories);

    @FXML
    public void initialize()
    {
        cbType.getItems().addAll(types);
        cbCategory.getItems().addAll("Select a type first.");
        cbFilterType.getItems().addAll(types);
        cbFilterCategory.getItems().addAll("Select a filter type first.");

        cbType.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) ->
        {
            cbCategory.getItems().setAll(categoryMap.get(newVal));
            cbCategory.setValue("Other");
        });

        cbFilterType.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) ->
        {
            cbFilterCategory.getItems().setAll(categoryMap.get(newVal));
            cbFilterType.setValue("Other");
        });
    }
}
