package org.example.finalproject.service;

import org.example.finalproject.model.Transaction;
import org.example.finalproject.model.ValidationResult;
import org.example.finalproject.util.DataManager;

import java.time.LocalDate;
import java.time.Month;
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

    // Totals

    public static double getTotalIncome() {
        return getAllTransactions().stream()
                .filter(t -> "Income".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount).sum();
    }

    public static double getTotalExpense() {
        return getAllTransactions().stream()
                .filter(t -> "Expense".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount).sum();
    }

    public static double getBalance() {
        return getTotalIncome() - getTotalExpense();
    }

    // ── Filtering

    public static List<Transaction> filterTransactions(String type, String category, int year, Month month) {
        return getAllTransactions().stream()
                .filter(t -> type == null || type.isBlank() || t.getType().equalsIgnoreCase(type))
                .filter(t -> category == null || category.isBlank() || t.getCategory().equalsIgnoreCase(category))
                .filter(t -> year == 0 || t.getDate().getYear() == year)
                .filter(t -> month == null || t.getDate().getMonth() == month)
                .collect(Collectors.toList());
    }

    // ── Category summary (expenses only)

    public static Map<String, Double> getCategorySummary() {
        Map<String, Double> summary = new LinkedHashMap<>();
        getAllTransactions().stream()
                .filter(t -> "Expense".equalsIgnoreCase(t.getType()))
                .forEach(t -> summary.merge(t.getCategory(), t.getAmount(), Double::sum));
        return summary;
    }

    // ── Monthly summary

    public static double getMonthlyIncome(int year, Month month) {
        return getAllTransactions().stream()
                .filter(t -> "Income".equalsIgnoreCase(t.getType())
                        && t.getDate().getYear() == year
                        && t.getDate().getMonth() == month)
                .mapToDouble(Transaction::getAmount).sum();
    }

    public static double getMonthlyExpense(int year, Month month) {
        return getAllTransactions().stream()
                .filter(t -> "Expense".equalsIgnoreCase(t.getType())
                        && t.getDate().getYear() == year
                        && t.getDate().getMonth() == month)
                .mapToDouble(Transaction::getAmount).sum();
    }

    public static double getMonthlyBalance(int year, Month month) {
        return getMonthlyIncome(year, month) - getMonthlyExpense(year, month);
    }

    // ── Highest expense category

    public static String getHighestExpenseCategory() {
        return getCategorySummary().entrySet().stream()
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

        if (type == null || type.isBlank() || type.equalsIgnoreCase("Select Type")) {
            return new ValidationResult(false, "Type must be selected.");
        }

        if (category == null || category.isBlank() || category.equalsIgnoreCase("Select Category")) {
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
}
