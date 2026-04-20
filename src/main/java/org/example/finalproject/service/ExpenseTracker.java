package org.example.finalproject.service;

import org.example.finalproject.model.Transaction;
import org.example.finalproject.model.ValidationResult;
import org.example.finalproject.util.DataManager;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ExpenseTracker {

    public static boolean AddTransaction(Transaction t) {
        return DataManager.InsertTransaction(t);
    }

    public static boolean UpdateTransaction(Transaction t) {
        return DataManager.UpdateTransaction(t);
    }

    public static boolean deleteTransaction(int id) {
        return DataManager.DeleteTransaction(id);
    }

    public static List<Transaction> getAllTransactions() {
        return DataManager.GetAllTransactions();
    }

    public static boolean idExists(int id) {
        return DataManager.IdExists(id);
    }

    //Totals Report
    public static double getTotalIncome(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> "Income".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount).sum();
    }

    public static double getTotalExpense(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> "Expense".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount).sum();
    }

    public static double getBalance(List<Transaction> transactions) {
        return getTotalIncome(transactions) - getTotalExpense(transactions);
    }

    // ── Filtering

    public static List<Transaction> filterTransactions(String type, String category, LocalDate startDate, LocalDate endDate) {
        return getAllTransactions().stream()
                .filter(t -> typeIsInvalid(type) || t.getType().equalsIgnoreCase(type))
                .filter(t -> categoryIsInvalid(category) || t.getCategory().equalsIgnoreCase(category))
                .filter(t -> startDate == null || !t.getDate().isBefore(startDate))
                .filter(t -> endDate == null || !t.getDate().isAfter(endDate))
                .collect(Collectors.toList());
    }

    // ── Category summary (expenses)

    public static Map<String, Double> getCategorySummary(List<Transaction> transactions) {
        Map<String, Double> summary = new LinkedHashMap<>();
        transactions.stream()
                .filter(t -> "Expense".equalsIgnoreCase(t.getType()))
                .forEach(t -> summary.merge(t.getCategory(), t.getAmount(), Double::sum));
        return summary;
    }

    // ── Highest expense category

    public static String getHighestExpenseCategory(List<Transaction> transactions) {
        return getCategorySummary(transactions).entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
    }

    // Validations

    public static ValidationResult IsValidTransaction(LocalDate date,
       String type,
       String category,
       String amountText) {

        if (date == null) {
            return new ValidationResult(false, "Date cannot be empty.");
        }

        if (typeIsInvalid(type)) {
            return new ValidationResult(false, "Type must be selected.");
        }

        if (categoryIsInvalid(category)) {
            return new ValidationResult(false, "Category must be selected.");
        }

        if (amountText == null || amountText.isBlank()) {
            return new ValidationResult(false, "Amount cannot be empty.");
        }

        double amount;
        try {
            amount = Double.parseDouble(amountText);
        } catch (NumberFormatException e) {
            return new ValidationResult(false, "Amount must be numeric.");
        }

        if (amount <= 0) {
            return new ValidationResult(false, "Amount must be greater than 0.");
        }

        return new ValidationResult(true, "Valid transaction.");
    }

    private static boolean typeIsInvalid(String type){
        return type == null || type.isBlank() || type.startsWith("Select");
    }

    private static boolean categoryIsInvalid(String category){
        return category == null || category.isBlank() || category.startsWith("Select");
    }
}
