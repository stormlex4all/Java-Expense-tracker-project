package org.example.finalproject.util;

import org.example.finalproject.model.Transaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    // Create (Insert)
    public static boolean InsertTransaction(Transaction t) {
        String sql = "INSERT INTO transactions(date, type, category, amount, description) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(t.getDate()));
            ps.setString(2, t.getType());
            ps.setString(3, t.getCategory());
            ps.setDouble(4, t.getAmount());
            ps.setString(5, t.getDescription());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Insert failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean UpdateTransaction(Transaction t) {
        String sql =
                "UPDATE transactions " +
                        "SET date =?, type =?, category =?, amount =?, description =? " +
                        "WHERE transactionId =?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(t.getDate()));
            ps.setString(2, t.getType());
            ps.setString(3, t.getCategory());
            ps.setDouble(4, t.getAmount());
            ps.setString(5, t.getDescription());
            ps.setInt(6, t.getTransactionId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    //READ (Select)
    public static List<Transaction> GetAllTransactions() {
        List<Transaction> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY date DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //DELETE
    public static boolean DeleteTransaction(int id) {
        String sql = "DELETE FROM transactions WHERE transactionId =?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Delete failed: " + e.getMessage());
            return false;
        }
    }

    public static boolean IdExists(int id) {
        String sql = "SELECT 1 FROM transactions WHERE transactionId =?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            return false;
        }
    }

    private static Transaction mapRow(ResultSet rs) throws SQLException {
        double amount = rs.getDouble("amount");
        amount = Math.round(amount * 100.0) / 100.0;

        return new Transaction(
                rs.getInt("transactionId"),
                rs.getDate("date").toLocalDate(),
                rs.getString("type"),
                rs.getString("category"),
                rs.getDouble("amount"),
                rs.getString("description")
        );
    }
}