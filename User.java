/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.List;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author 11User
 */

public class User {

    private static int lastUserId = 0;
    private int userId;
    private String name;
    private String address;
    private String contactInfo;
    private double balance;
    private List<Transaction> transactionHistory;

    public User(String name, String address, String contactInfo, int balance) {
        
        this.name = name;
        this.address = address;
        this.contactInfo = contactInfo;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
    }
    

    public int getUserIdFromDatabase(Connection connection) {
        int userId = -1; // Default value

        String query = "SELECT ID FROM users WHERE username = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, this.getusername());
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                userId = resultSet.getInt("ID");
                
            } else {
                
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Replace with appropriate logging
            // Handle the exception or rethrow a custom exception if needed
        }

        return userId;
    }


    public String getusername() {
        return name;
    }

    public void setusername(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        if (balance >= 0) {
            this.balance = balance;
        }
    }

    public void addBalance(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    public void deductBalance(double amount) {
        this.balance -= amount;
    }

    public boolean isBalanceEnough(double amount) {
        return amount > 0 && amount <= balance;
    }

    public void transferTo(User recipient, double amount) {
        if (amount > 0 && amount <= balance) {
            this.deductBalance(amount);
            recipient.addBalance(amount);
        }
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void addTransactionToHistory(Transaction transaction) {
        transactionHistory.add(transaction);
    }
}
