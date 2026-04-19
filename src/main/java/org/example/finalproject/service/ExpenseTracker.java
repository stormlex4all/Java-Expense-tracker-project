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

    public boolean AddTransaction(Transaction t) {
        return DataManager.InsertTransaction(t);
    }

    public boolean UpdateTransaction(Transaction t) {
        return DataManager.UpdateTransaction(t);
    }

    public boolean deleteTransaction(int id) {
        return DataManager.DeleteTransaction(id);
    }

    public List<Transaction> getAllTransactions() {
        return DataManager.GetAllTransactions();
    }

    public boolean idExists(int id) {
        return DataManager.IdExists(id);
    }

    // Totals

    public double getTotalIncome() {
        return getAllTransactions().stream()
                .filter(t -> "Income".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount).sum();
    }

    public double getTotalExpense() {
        return getAllTransactions().stream()
                .filter(t -> "Expense".equalsIgnoreCase(t.getType()))
                .mapToDouble(Transaction::getAmount).sum();
    }

    public double getBalance() {
        return getTotalIncome() - getTotalExpense();
    }

    // ── Filtering

    public List<Transaction> filterByType(String type) {
        return getAllTransactions().stream()
                .filter(t -> t.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    public List<Transaction> filterByCategory(String category) {
        return getAllTransactions().stream()
                .filter(t -> t.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public List<Transaction> filterByTypeAndCategory(String type, String category) {
        return getAllTransactions().stream()
                .filter(t -> t.getType().equalsIgnoreCase(type)
                        && t.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    // ── Category summary (expenses only)

    public Map<String, Float> getCategorySummary() {
        Map<String, Float> summary = new LinkedHashMap<>();
        getAllTransactions().stream()
                .filter(t -> "Expense".equalsIgnoreCase(t.getType()))
                .forEach(t -> summary.merge(t.getCategory(), t.getAmount(), Float::sum));
        return summary;
    }

    // ── Monthly summary

    public double getMonthlyIncome(int year, Month month) {
        return getAllTransactions().stream()
                .filter(t -> "Income".equalsIgnoreCase(t.getType())
                        && t.getDate().getYear() == year
                        && t.getDate().getMonth() == month)
                .mapToDouble(Transaction::getAmount).sum();
    }

    public double getMonthlyExpense(int year, Month month) {
        return getAllTransactions().stream()
                .filter(t -> "Expense".equalsIgnoreCase(t.getType())
                        && t.getDate().getYear() == year
                        && t.getDate().getMonth() == month)
                .mapToDouble(Transaction::getAmount).sum();
    }

    public double getMonthlyBalance(int year, Month month) {
        return getMonthlyIncome(year, month) - getMonthlyExpense(year, month);
    }

    // ── Highest expense category

    public String getHighestExpenseCategory() {
        return getCategorySummary().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
    }

    // Validations

    public ValidationResult IsValidTransaction(LocalDate date,
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

        float amount;
        try {
            amount = Float.parseFloat(amountText);
        } catch (NumberFormatException e) {
            return new ValidationResult(false, "Amount must be numeric.");
        }

        if (amount <= 0) {
            return new ValidationResult(false, "Amount must be greater than 0.");
        }

        return new ValidationResult(true, "Valid transaction.");
    }
}
