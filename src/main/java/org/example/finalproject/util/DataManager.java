package org.example.finalproject.util;

import org.example.finalproject.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    // Create (Insert)
    public static void addTransaction(String description, double amount) {
        String sql = "INSERT INTO transactions (description, amount) VALUES (?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, description);
            stmt.setDouble(2, amount);
            stmt.executeUpdate();

            System.out.println("Transaction added!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //READ (Select)
    public static List<String> getTransaction() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT * FROM transactions";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String record = rs.getInt("id") + " - " + rs.getString("description") + " - $" + rs.getDouble("amount");
                list.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // UPDATE
    public static void updateTransaction(int id, String description, double amount) {
        String sql = "UPDATE transactions SET description=?, amount=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, description);
            stmt.setDouble(2, amount);
            stmt.setInt(3, id);
            stmt.executeUpdate();

            System.out.println("Transaction updated!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //DELETE
    public static void deleteTransaction(int id) {
        String sql = "DELETE FROM transactions WHERE id=?";

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