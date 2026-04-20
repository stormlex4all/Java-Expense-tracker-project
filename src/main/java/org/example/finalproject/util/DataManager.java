package org.example.finalproject.util;

import org.example.finalproject.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class DataManager {
    // Create (Insert)
    public static void addTransactions(LocalDate date, String type, String category, double amount, String description) {
        String sql = "INSERT INTO transactions (transaction_date, type, category, amount, description) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(date));
            stmt.setString(2, type);
            stmt.setString(3, category);
            stmt.setDouble(4, amount);
            stmt.setString(5, description);
            stmt.executeUpdate();

            System.out.println("Transaction added!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //READ (Select)
    public static List<String> getTransactions() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String record = rs.getInt("transaction_id") + " - " +
                        rs.getDate("transaction_date") + " - " +
                        rs.getString("type") + " - " +
                        rs.getString("category") + " - " +
                        rs.getDouble("amount") + " - " +
                        rs.getString("description");
                list.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE
    public static void updateTransactions(int id, LocalDate date, String type, String category, double amount, String description) {
        String sql = "UPDATE transactions SET transaction_date=?, type=?, category=?, amount=?, description=? WHERE transaction_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(date));
            stmt.setString(2, type);
            stmt.setString(3, category);
            stmt.setDouble(4, amount);
            stmt.setString(5, description);
            stmt.setInt(6, id);

            stmt.executeUpdate();
            System.out.println("Transaction updated!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //DELETE
    public static void deleteTransactions(int id) {
        String sql = "DELETE FROM transactions WHERE transaction_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

            System.out.println("Transaction deleted!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}