/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

import java.awt.BorderLayout;

import java.awt.Component;

import java.awt.Dimension;
import java.awt.Graphics2D;

import java.awt.Image;
import javax.swing.*;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.ArrayList;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import javax.imageio.ImageIO;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class DataStructuresProject implements ActionListener {

    private static JButton backbutton;
    private static JLabel success;
    private static JLabel success2;
    private static JLabel success3;
    private static JLabel Phonenum;
    private static JLabel addressLabel;
    private static JFrame welcomeFrame;
    private static JTextField addressText;
    private static JTextField PhonenumText;
    private static JLabel Name;
    private static JLabel ID;
    private User currentUser;
    private static JPanel welcomePanel;
    private static JLabel Nameval;
    private static JLabel IDval;
    private static JLabel addressValueLabel;
    private static JLabel phoneValueLabel;
    private static JLabel welcomeLabel;
    private static JButton aboutButton;
    private static JButton buyButton;
    private static JButton rentButton;
    private static JButton sellButton;
    private static JButton backButton;
    private static JFrame aboutFrame;
    private JTextField modelField;
    private JTextField yearField;
    private JTextField priceField;
    private JTextField infoField;
    private JLabel balanceLabel;
    private JLabel balanceValueLabel;
    private JButton balanceButton;

//  String randomId = UUID.randomUUID().toString().substring(0, 8);
    private static JLabel userLabel;
    private static JTextField userText;
    private static JLabel passwordLabel;
    private static JPasswordField passwordText;
    private static JButton signUpButton;
    private static JButton loginButton;
    private static JFrame signUpFrame;
    private static JFrame loginFrame;
    private JFrame transferBalanceFrame;
    private JPanel transferBalancePanel;
    private JTextField amountField;
    private JTextField recipientUsernameField;

    private static Connection connection;

    public static void main(String[] args) {
        DataStructuresProject project = new DataStructuresProject();
        project.createDatabase();
        project.createSignUpGui();

        // Close the connection when the application exits
        Runtime.getRuntime().addShutdownHook(new Thread(DataStructuresProject::closeConnection));
    }

    private static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createDatabase() {
        try {
            // Connect to your existing database
            connection = DriverManager.getConnection("jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12668498", "sql12668498", "L4b7cIJtc2");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    @Override
////    public void actionPerformed(ActionEvent e) {
////        if (e.getSource() == signUpButton) {
////            // Handle sign-up logic here
////            String username = userText.getText();
////            String password = new String(passwordText.getPassword());
////            String address = addressText.getText();
////            String phoneNumber = PhonenumText.getText();
////
////            if (username.isEmpty() || password.isEmpty()) {
////                success.setText("Username and password cannot be empty.");
////            } else {
////                if (isUserInDatabase(username)) {
////                    success.setText("User already exists. Please log in.");
////                } else {
////                    // Create a new User object and add it to the database
////                    currentUser = new User(username, address, phoneNumber, 0);
////                    addUserToDatabase(username, password, address, phoneNumber);
////                    success.setText("Sign-up Successful!");
////                    signUpFrame.dispose();
////                    createLoginGui();
////                }
////            }
//        } else if (e.getSource() == loginButton) {
//            // Handle login logic here
//            String username = userText.getText();
//            String password = new String(passwordText.getPassword());
//
//            if (username.isEmpty() || password.isEmpty()) {
//                success.setText("Username and password cannot be empty.");
//            } else {
//                if (checkUserCredentials(username, password)) {
//                    // Load user data from the database based on the username
//                    currentUser = loadUserFromDatabase(username);
//                    success.setText("Login Successful!");
//                    loginFrame.dispose();
//
//                    // Create and display the WelcomeGui instead of MainGui
//                    new WelcomeGui();
//                } else {
//                    success.setText("Invalid username or password.");
//                }
//
//            }
//        }
//    }

    private User loadUserFromDatabase(String username) {
        try {
            String loadUserSQL = "SELECT * FROM users WHERE username = ?";
            try ( PreparedStatement statement = connection.prepareStatement(loadUserSQL)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String name = resultSet.getString("username");
                    String address = resultSet.getString("address");
                    String phoneNumber = resultSet.getString("phonenum");

                    User loadedUser = new User(name, address, phoneNumber, 0); // Initial balance is set to 0

                    // Load transaction history if needed
                    // loadedUser.setTransactionHistory(loadTransactionHistoryFromDatabase(userId));
                    return loadedUser;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isUserInDatabase(String username) {
        // Check if the username already exists in the database
        try {
            String checkUserSQL = "SELECT * FROM users WHERE username = ?";
            try ( PreparedStatement statement = connection.prepareStatement(checkUserSQL)) {
                statement.setString(1, username);
                ResultSet resultSet = statement.executeQuery();
                return resultSet.next(); // Return true if user exists, false otherwise
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void addUserToDatabase(String username, String password, String address, String phoneNumber) {
        try {
            String insertSQL = "INSERT INTO users (username, password, address, phonenum) VALUES (?, ?, ?, ?)";
            try ( PreparedStatement statement = connection.prepareStatement(insertSQL)) {
                statement.setString(1, username);
                statement.setString(2, password);
                statement.setString(3, address);
                statement.setString(4, phoneNumber);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean checkUserCredentials(String username, String password) {
        // Check if the username and password match in the database
        try {
            String checkCredentialsSQL = "SELECT * FROM users WHERE username = ? AND password = ?";
            try ( PreparedStatement statement = connection.prepareStatement(checkCredentialsSQL)) {
                statement.setString(1, username);
                statement.setString(2, password);
                ResultSet resultSet = statement.executeQuery();
                return resultSet.next(); // Return true if credentials match, false otherwise
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void createSignUpGui() {
        signUpFrame = new JFrame("Sign Up");
        JPanel panel = new JPanel();
        signUpFrame.setSize(350, 300);
        signUpFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        signUpFrame.add(panel);

        panel.setLayout(null);

        userLabel = new JLabel("Username");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        userText = new JTextField();
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordText = new JPasswordField();
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        addressLabel = new JLabel("Address");
        addressLabel.setBounds(10, 80, 80, 25);
        panel.add(addressLabel);

        addressText = new JTextField();
        addressText.setBounds(100, 80, 165, 25);
        panel.add(addressText);

        Phonenum = new JLabel("Phone");
        Phonenum.setBounds(10, 110, 80, 25);
        panel.add(Phonenum);

        PhonenumText = new JTextField();
        PhonenumText.setBounds(100, 110, 165, 25);
        panel.add(PhonenumText);

        signUpButton = new JButton("Sign Up");
        signUpButton.setBounds(10, 140, 80, 25);
        signUpButton.addActionListener(this); // Use the ActionListener from the main class
        panel.add(signUpButton);

        loginButton = new JButton("Already have an account?");
        loginButton.setBounds(100, 140, 180, 25);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUpFrame.dispose();
                createLoginGui();
            }
        });
        panel.add(loginButton);

        success = new JLabel("");
        success.setBounds(10, 160, 300, 25);
        panel.add(success);

        signUpFrame.setVisible(true);
    }

    private void createLoginGui() {
        loginFrame = new JFrame("Login");
        JPanel panel = new JPanel();
        loginFrame.setSize(350, 300);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.add(panel);

        panel.setLayout(null);

        userLabel = new JLabel("Username");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        userText = new JTextField();
        userText.setBounds(100, 20, 165, 25);
        panel.add(userText);

        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordText = new JPasswordField();
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        loginButton.addActionListener(this);
        panel.add(loginButton);

        signUpButton = new JButton("Don't have an account?");
        signUpButton.setBounds(100, 80, 180, 25);

        success = new JLabel("");
        success.setBounds(10, 110, 300, 25);
        panel.add(success);

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginFrame.dispose();
                createSignUpGui();
            }
        });
        panel.add(signUpButton);

        loginFrame.setVisible(true);
    }

    public class WelcomeGui {

        public WelcomeGui() {
            createWelcomeGui();
        }

        private void createWelcomeGui() {
            welcomeFrame = new JFrame("Welcome to Our Car Shop");
            welcomePanel = new JPanel();
            welcomeFrame.setSize(400, 200);
            welcomeFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            welcomeFrame.add(welcomePanel);

            welcomePanel.setLayout(null);

            welcomeLabel = new JLabel("Welcome to Our Car Shop!");
            welcomeLabel.setBounds(110, 20, 300, 25);
            welcomePanel.add(welcomeLabel);

            aboutButton = new JButton("About");
            aboutButton.setBounds(30, 70, 100, 25);
            welcomePanel.add(aboutButton);

            buyButton = new JButton("Buy");
            buyButton.setBounds(140, 70, 100, 25);
            welcomePanel.add(buyButton);

            sellButton = new JButton("Sell");
            sellButton.setBounds(250, 70, 100, 25);
            welcomePanel.add(sellButton);

            rentButton = new JButton("Rent");
            rentButton.setBounds(30, 110, 100, 25);
            welcomePanel.add(rentButton);

            balanceButton = new JButton("Balance");
            balanceButton.setBounds(140, 110, 100, 25);
            welcomePanel.add(balanceButton);

            backButton = new JButton("Back");
            backButton.setBounds(250, 110, 100, 25);
            welcomePanel.add(backButton);

            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    welcomeFrame.dispose();
                    createLoginGui(); // Change this line to go back to the login GUI
                }
            });

            aboutButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Add code to navigate to MainGui
                    welcomeFrame.dispose(); // Close the WelcomeGui
                    AboutGui(); // Assuming MainGui() is the method to go to the main screen
                }
            });
            rentButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Handle action for the "Rent" button
                    // Assuming new RentGui() is the constructor to go to the rent screen
                    welcomeFrame.dispose();
                    RentGui();
                }
            });

            buyButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Add code to navigate to BuyGui
                    welcomeFrame.dispose(); // Close the WelcomeGui
                    new BuyGui();
                }
            });

            sellButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    welcomeFrame.dispose();
                    new SellGui(); // Assuming SellGui is the constructor that takes a Connection parameter
                }
            });
            balanceButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    welcomeFrame.dispose();
                    openBalanceScreen();

                }
            });

            welcomeFrame.setVisible(true);
        }

    }

    private void RentGui() {

    }

    private void openBalanceScreen() {
        JFrame balanceFrame = new JFrame("Balance Screen");
        JPanel balancePanel = new JPanel();
        balanceFrame.setSize(400, 200);
        balanceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the balance screen, not the entire application
        balanceFrame.add(balancePanel);

        // Database access logic to retrieve balance and username
        String username = currentUser.getusername();
        double balance = getBalanceForCurrentUser(connection);

        // Components for balance screen
        JLabel usernameLabel = new JLabel("Welcome, " + username + "!");
        JLabel balanceLabel = new JLabel("Your balance is: $" + balance);

        JButton addBalanceButton = new JButton("Add Balance");
        addBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openAddBalanceWindow();
            }
        });

        JButton transferBalanceButton = new JButton("Transfer Balance");
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                balanceFrame.setVisible(false);
                new WelcomeGui(); // Change this line to go back to the login GUI
            }
        });

        transferBalanceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open a new window for balance transfer
                JFrame transferFrame = new JFrame("Transfer Balance");
                JPanel transferPanel = new JPanel();
                transferFrame.setSize(300, 150);
                transferFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the transfer screen, not the entire application
                transferFrame.add(transferPanel);

                // Components for balance transfer screen
                JLabel amountLabel = new JLabel("Enter Amount:");
                JTextField amountField = new JTextField(10);

                JLabel recipientLabel = new JLabel("Recipient Username:");
                JTextField recipientField = new JTextField(10);

                JButton transferButton = new JButton("Transfer");
                transferButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // Handle the action for transferring balance
                        double amount = Double.parseDouble(amountField.getText());
                        String recipientUsername = recipientField.getText();

                        // Perform balance transfer
                        performBalanceTransfer(amount, getUsernameFromDatabase(getDatabaseConnection()), recipientUsername);

                        // Close the transfer screen
                        transferFrame.dispose();
                    }
                });

                // Layout components on the transfer screen
                transferPanel.setLayout(new GridLayout(4, 1));
                transferPanel.add(amountLabel);
                transferPanel.add(amountField);
                transferPanel.add(recipientLabel);
                transferPanel.add(recipientField);
                transferPanel.add(transferButton);

                transferFrame.setVisible(true);
            }
        });

        // Layout components on the balance screen
        balancePanel.setLayout(new GridLayout(4, 1));
        balancePanel.add(usernameLabel);
        balancePanel.add(balanceLabel);
        balancePanel.add(addBalanceButton);
        balancePanel.add(transferBalanceButton);
        balancePanel.add(backButton);

        balanceFrame.setVisible(true);
    }

    private void openAddBalanceWindow() {
        JFrame addBalanceFrame = new JFrame("Add Balance");
        JPanel addBalancePanel = new JPanel();

        JLabel amountLabel = new JLabel("Enter amount to add:");
        JTextField amountTextField = new JTextField(10);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the action for confirming the balance addition
                double amountToAdd = Double.parseDouble(amountTextField.getText());
                updateBalance(amountToAdd); // Update the balance in the database
                addBalanceFrame.dispose(); // Close the add balance window
            }
        });

        addBalancePanel.add(amountLabel);
        addBalancePanel.add(amountTextField);
        addBalancePanel.add(confirmButton);

        addBalanceFrame.add(addBalancePanel);
        addBalanceFrame.setSize(300, 150);
        addBalanceFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close only the add balance window
        addBalanceFrame.setVisible(true);

    }

// Update the balance in the database
private void updateBalance(double amountToAdd) {
    Connection connection = getDatabaseConnection();

    if (connection != null) {
        try {
            String username = currentUser.getusername(); // Replace with your method to get the username
            String updateQuery = "UPDATE users SET balance = balance + ? WHERE username = ?";
            
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setDouble(1, amountToAdd);
                updateStatement.setString(2, username);
                updateStatement.executeUpdate();

                
                
                String successMessage = "Successfully added! Please refresh" ;
                showSuccessMessage(successMessage);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
        }
    }
}

private void showSuccessMessage(String message) {
    // Implement logic to display the success message in a panel or popup.
    // This can be a JOptionPane, JavaFX Alert, or any other GUI component.
    // For example, using JOptionPane in a Swing application:
    JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
}


    private static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Perform balance transfer and update the database
    public static Connection getDatabaseConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:mysql://sql12.freemysqlhosting.net:3306/sql12668498", "sql12668498", "L4b7cIJtc2");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void performBalanceTransfer(double amount, String senderUsername, String recipientUsername) {
        Connection connection = getDatabaseConnection();
        if (connection != null) {
            try {
                connection.setAutoCommit(false); // Start transaction

                // Check if the sender has sufficient balance
                double senderBalance = getBalanceFromDatabase(connection);
                if (senderBalance < amount) {
                    // If balance is insufficient, show a message to the user
                    showInsufficientBalanceMessage();
                    return; // Abort the transaction
                }

                // Deduct the amount from the sender's balance
                updateBalance(connection, senderUsername, -amount);

                // Add the amount to the recipient's balance
                updateBalance(connection, recipientUsername, amount);

                connection.commit(); // Commit the transaction
            } catch (SQLException e) {
                try {
                    connection.rollback(); // Rollback the transaction in case of an exception
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

// Method to display a message for insufficient balance and open a new window for adding balance
    private void showInsufficientBalanceMessage() {
        int choice = JOptionPane.showOptionDialog(null,
         "Insufficient balance. Do you want to add balance to your account?",
         "Insufficient Balance", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
         null, null, null);

        if (choice == JOptionPane.YES_OPTION) {
            openAddBalanceWindow();
        }
    }

// Method to open a new window for adding balance
    // Update the balance in the database
    private static void updateBalance(Connection connection, String username, double amount) throws SQLException {
        String updateQuery = "UPDATE users SET balance = balance + ? WHERE username = ?";
        try ( PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
            updateStatement.setDouble(1, amount);
            updateStatement.setString(2, username);
            updateStatement.executeUpdate();
        }
    }
    // Mock methods for demonstration (replace with your actual database access logic)

    public String getUsernameFromDatabase(Connection connection) {
        String username = null;

        try {
            // Prepare the SQL query to retrieve the first username
            String query = "SELECT username FROM users LIMIT 1";

            // Create a PreparedStatement with the query
            try ( PreparedStatement statement = connection.prepareStatement(query)) {
                // Execute the query and retrieve the results
                try ( ResultSet resultSet = statement.executeQuery()) {
                    // Check if there is a result (username)
                    if (resultSet.next()) {
                        // Retrieve the username from the result set
                        username = resultSet.getString("username");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
        }

        return username;
    }

    public double getBalanceFromDatabase(Connection connection) {
        double balance = 0.0;

        try {
            // Prepare the SQL query to retrieve the first balance
            String query = "SELECT balance FROM users LIMIT 1";

            // Create a PreparedStatement with the query
            try ( PreparedStatement statement = connection.prepareStatement(query)) {
                // Execute the query and retrieve the results
                try ( ResultSet resultSet = statement.executeQuery()) {
                    // Check if there is a result (balance)
                    if (resultSet.next()) {
                        // Retrieve the balance from the result set
                        balance = resultSet.getDouble("balance");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
        }

        return balance;
    }

    public double getBalanceForCurrentUser(Connection connection) {
        double balance = 0.0;
        String username = currentUser.getusername(); // Assuming currentUser is an instance of a class with a getUsername method

        try {
            // Prepare the SQL query to retrieve the balance for the current user
            String query = "SELECT balance FROM users WHERE username = ?";

            // Create a PreparedStatement with the query
            try ( PreparedStatement statement = connection.prepareStatement(query)) {
                // Set the username parameter in the prepared statement
                statement.setString(1, username);

                // Execute the query and retrieve the results
                try ( ResultSet resultSet = statement.executeQuery()) {
                    // Check if there is a result (balance)
                    if (resultSet.next()) {
                        // Retrieve the balance from the result set
                        balance = resultSet.getDouble("balance");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately in your application
        }

        return balance;
    }

    private void AboutGui() {
        aboutFrame = new JFrame("My Account");
        JPanel newpanel = new JPanel();
        aboutFrame.setSize(350, 400);
        aboutFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        aboutFrame.add(newpanel);

        newpanel.setLayout(null);

        Name = new JLabel("Name:");
        Name.setBounds(10, 20, 80, 25);
        newpanel.add(Name);

        Nameval = new JLabel(currentUser.getusername());
        Nameval.setBounds(100, 20, 165, 25);
        newpanel.add(Nameval);

        ID = new JLabel("ID:");
        ID.setBounds(10, 40, 80, 25);
        newpanel.add(ID);

        int userPId = currentUser.getUserIdFromDatabase(connection);
        IDval = new JLabel(String.valueOf(userPId));
        IDval.setBounds(100, 40, 165, 25);
        newpanel.add(IDval);

        addressLabel = new JLabel("Address:");
        addressLabel.setBounds(10, 60, 80, 25);
        newpanel.add(addressLabel);

        addressValueLabel = new JLabel(currentUser.getAddress());
        addressValueLabel.setBounds(100, 60, 165, 25);
        newpanel.add(addressValueLabel);

        Phonenum = new JLabel("Phone:");
        Phonenum.setBounds(10, 80, 80, 25);
        newpanel.add(Phonenum);

        phoneValueLabel = new JLabel(currentUser.getContactInfo());
        phoneValueLabel.setBounds(100, 80, 165, 25);
        newpanel.add(phoneValueLabel);

        // Add balance label
        balanceLabel = new JLabel("Balance:");
        balanceLabel.setBounds(10, 100, 80, 25);
        newpanel.add(balanceLabel);

        // Retrieve and display the user's balance
        double userBalance = getBalanceForCurrentUser(connection);
        balanceValueLabel = new JLabel(String.valueOf(userBalance));
        balanceValueLabel.setBounds(100, 100, 165, 25);
        newpanel.add(balanceValueLabel);

        backbutton = new JButton("Back");
        backbutton.setBounds(130, 240, 80, 25);
        newpanel.add(backbutton);

        success2 = new JLabel("");
        success2.setBounds(10, 300, 300, 25);
        newpanel.add(success2);

        success3 = new JLabel("");
        success3.setBounds(10, 320, 300, 25);
        newpanel.add(success3);

        backbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aboutFrame.dispose();
                new WelcomeGui(); // Change this line to go back to the login GUI
            }
        });

        aboutFrame.setVisible(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel label = new JLabel();
        if (value instanceof byte[]) {
            byte[] imageBytes = (byte[]) value;
            ImageIcon imageIcon = new ImageIcon(getImageFromBytes(imageBytes));
            label.setIcon(imageIcon);
        } else {
            label.setText(value.toString());
        }

        return label;
    }

    public Image getImageFromBytes(byte[] imageData) {
        try ( ByteArrayInputStream bis = new ByteArrayInputStream(imageData)) {
            return ImageIO.read(bis);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    } // for getting the images to get displayed

    public class BuyGui {

        public JFrame buyFrame;
        public JPanel cardsPanel;

        public BuyGui() {
            int sortOrder = displaySortOrderDialog(); // Show dialog to get user's sort order preference
            sortAndCreateBuyGui(sortOrder);
        }

        private int displaySortOrderDialog() {
            String[] sortOrderOptions = {"Sort by Price (Cheapest to Most Expensive)", "Sort by Price (Most Expensive to Cheapest)", "No Sorting"};
            int defaultOptionIndex = 0;
            int sortOrder = JOptionPane.showOptionDialog(null,
             "Choose the sort order for the cars:",
             "Sort Order",
             JOptionPane.DEFAULT_OPTION,
             JOptionPane.PLAIN_MESSAGE,
             null,
             sortOrderOptions,
             sortOrderOptions[defaultOptionIndex]);
            return sortOrder;
        }

        private void sortAndCreateBuygui(int sortOrder) {
            ArrayList<Object[]> data = getAvailableCarsData();

            switch (sortOrder) {
                case 0:
                    // Sort by price from cheapest to most expensive using a Queue
                    Queue<Object[]> queue = new LinkedList<>(data);
                    data.clear();
                    while (!queue.isEmpty()) {
                        data.add(queue.poll());
                    }
                    break;
                case 1:
                    // Sort by price from most expensive to cheapest using a Stack
                    Stack<Object[]> stack = new Stack<>();
                    stack.addAll(data);
                    data.clear();
                    while (!stack.isEmpty()) {
                        data.add(stack.pop());
                    }
                    break;
                // case 2: // No Sorting - Do nothing
            }

            createBuyGui(data);
        }

        private void sortAndCreateBuyGui(int sortOrder) {
            ArrayList<Object[]> data = getAvailableCarsData();

            // Comparator for sorting by price
            Comparator<Object[]> priceComparator = Comparator.comparingInt(carData -> (int) carData[3]); // Assuming price is at index 3

            switch (sortOrder) {
                case 0:
                    // Sort by price from cheapest to most expensive using Collections.sort and priceComparator
                    Collections.sort(data, priceComparator);
                    break;
                case 1:
                    // Sort by price from most expensive to cheapest using Collections.sort and reversed priceComparator
                    Collections.sort(data, Collections.reverseOrder(priceComparator));
                    break;
                // case 2: // No Sorting - Do nothing
            }

            createBuyGui(data);
        }

        private void createBuyGui(ArrayList<Object[]> data) {
            buyFrame = new JFrame("Buy or Rent Cars");
            buyFrame.setSize(800, 400);
            buyFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            backbutton = new JButton("Back");
backbutton.setBounds(700, 300, 80, 25);
buyFrame.add(backbutton);
backbutton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        buyFrame.dispose();
        new WelcomeGui(); // Change this line to go back to the login GUI
    }
});

            cardsPanel = new JPanel();
            int cardWidth = 200; // Set the width of each card
            cardsPanel.setLayout(new GridLayout(0, buyFrame.getWidth() / cardWidth)); // Adjust the layout as per your preference

            for (Object[] carData : data) {
                JPanel carCard = createCarCard(carData, cardWidth);
                cardsPanel.add(carCard);
            }

            JScrollPane scrollPane = new JScrollPane(cardsPanel);
            buyFrame.add(scrollPane);

            buyFrame.setVisible(true);
        }

        private JPanel createCarCard(Object[] carData, int cardWidth) {
    JPanel carCard = new JPanel();
    carCard.setLayout(new BorderLayout());  // Set BorderLayout for carCard

    carCard.setPreferredSize(new Dimension(cardWidth, (int) (buyFrame.getHeight() * 0.75))); // Modify this line to adjust the card height

    JTextArea carInfoTextArea = new JTextArea();
    carInfoTextArea.setEditable(false);
    for (int i = 1; i < carData.length - 1; i++) {
        carInfoTextArea.append(columnNames[i] + ": " + carData[i] + "\n");
    }

    ImageIcon carImageIcon = new ImageIcon(getResizedImage(getImageFromBytes((byte[]) carData[5]), cardWidth, buyFrame.getHeight() / 4));
    carCard.add(new JLabel(carImageIcon), BorderLayout.NORTH);
    carCard.add(carInfoTextArea, BorderLayout.CENTER);

    // Create Buy and Rent buttons based on the role
    String role = (String) carData[6]; // Assuming role is at index 6

    if ("buy".equalsIgnoreCase(role)) {
        JButton buyButton = new JButton("Buy");
        buyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the Buy action for the specific car (carData)
                handleBuyAction(carData);
            }
        });
        carCard.add(buyButton, BorderLayout.SOUTH);
    } else if ("rent".equalsIgnoreCase(role)) {
        JButton rentButton = new JButton("Rent");
        rentButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle the Rent action for the specific car (carData)
                handleRentAction(carData);
            }
        });
        carCard.add(rentButton, BorderLayout.SOUTH);
    }

    return carCard;
}


private void addCarToUserOwnedCars(Connection connection, String username, String carModel) throws SQLException {
    String selectQuery = "SELECT user_owned_cars FROM users WHERE username = ?";
    String updateQuery = "UPDATE users SET user_owned_cars = ? WHERE username = ?";

    try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
         PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

        // Retrieve current user_owned_cars value
        selectStatement.setString(1, username);
        ResultSet resultSet = selectStatement.executeQuery();

        if (resultSet.next()) {
            String currentOwnedCars = resultSet.getString("user_owned_cars");
            String updatedOwnedCars = (currentOwnedCars == null) ? carModel : currentOwnedCars + "," + carModel;

            // Update user_owned_cars column in the users table
            updateStatement.setString(1, updatedOwnedCars);
            updateStatement.setString(2, username);
            updateStatement.executeUpdate();
        }
    }
}



        private void handleBuyAction(Object[] carData) {
            String buyerName = currentUser.getusername();

            if (buyerName != null && !buyerName.isEmpty()) {
                String carModel = (String) carData[1];
                double carPrice = (int) carData[3];

                boolean success = isCarAvailable(carModel);
                double balance =getBalanceForCurrentUser(connection);
                if (success) {
                    try {
                        
                        if (balance> carPrice) {
                            updateBalance(connection, buyerName, -carPrice);
                            addCarToUserOwnedCars(connection, buyerName, carModel);
                            JOptionPane.showMessageDialog(null, "Congratulations! You've bought the car.");
                            removeCarFromDisplay(carData);
                            markCarAsSold(carModel, buyerName);

                            buyFrame.setVisible(false);
                            new WelcomeGui();
                        } 
                        else {
                            showInsufficientBalanceMessage();
                            buyFrame.setVisible(false);
                            new WelcomeGui();

                        }

                    } catch (SQLException e) {
                        e.printStackTrace();

                    }

                } else {
                    JOptionPane.showMessageDialog(null, "The car you're trying to buy is already sold.");
                    buyFrame.setVisible(false);
                    new WelcomeGui();
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid buyer name. Please try again.");
                buyFrame.setVisible(false);
                new WelcomeGui();
            }
        }
        private boolean isCarAvailable(String carModel) {
    try {
        String selectQuery = "SELECT COUNT(*) FROM cars WHERE model = ? AND status = 'Available'";
        try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
            selectStatement.setString(1, carModel);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; // If count is greater than 0, the car is available
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return false; // Return false if an error occurs
    }

    return false; // Return false if no rows are affected (should not reach this point)
}

        private boolean markCarAsSold(String carModel, String buyerName) {
            try {
                String updateQuery = "UPDATE cars SET status = 'Sold', buyerName = ? WHERE model = ? AND status = 'Available'";
                try ( PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setString(1, buyerName);
                    updateStatement.setString(2, carModel);
                    int rowsAffected = updateStatement.executeUpdate();

                    return rowsAffected > 0; // If rows are affected, the update was successful
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false; // Return false if an error occurs
            }
        }

        private void removeCarFromDisplay(Object[] carData) {
            // Assuming carData[1] is the model and is stored as a String
            String carModel = (String) carData[1];

            deleteCarFromDatabase(carModel);

        }

        private void deleteCarFromDatabase(String carModel) {
            try {
                String deleteQuery = "DELETE FROM cars WHERE model = ?";
                try ( PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                    deleteStatement.setString(1, carModel);
                    deleteStatement.executeUpdate();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception appropriately in your application
            }
        }

        private void handleRentAction(Object[] carData) {
            // Implement the logic for renting the car
            // You can access car details using carData array
            // For example, carData[0] is the CarID, carData[1] is the Model, etc.
            // Perform the necessary database updates or actions
            JOptionPane.showMessageDialog(null, "Renting car: " + carData[1]);
        }

        private Image getResizedImage(Image originalImage, int targetWidth, int targetHeight) {
            BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = resizedImage.createGraphics();
            graphics.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
            graphics.dispose();
            return resizedImage;
        }

        private static final String[] columnNames = {"For", "Model", "Year", "Price", "Info", "Image"};

        private ArrayList<Object[]> getAvailableCarsData() {
            ArrayList<Object[]> data = new ArrayList<>();

            try {
                String query = "SELECT c.carID, c.model, c.year, c.price, c.info, c.image, c.role "
                 + "FROM cars c ";

                try ( PreparedStatement statement = connection.prepareStatement(query)) {
                    ResultSet resultSet = statement.executeQuery();

                    while (resultSet.next()) {
                        Object[] row = new Object[7];

                        String model = resultSet.getString("model");
                        int year = resultSet.getInt("year");
                        int price = resultSet.getInt("price");
                        String info = resultSet.getString("info");
                        byte[] imageBytes = resultSet.getBytes("image");
                        String role = resultSet.getString("role");
                        

                        row[0] = role;
                        row[1] = model;
                        row[2] = year;
                        row[3] = price;
                        row[4] = info;
                        row[5] = imageBytes;
                        row[6] = role;
                        
                        data.add(row);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return data;
        }
    }

    public class SellGui {

        private File selectedFile;

        public SellGui() {
            createSellGui();
        }

        private void createSellGui() {
            JFrame sellFrame = new JFrame("Sell Car");
            JPanel sellPanel = new JPanel();
            sellFrame.setSize(400, 300);
            sellFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            sellFrame.add(sellPanel);

            sellPanel.setLayout(null);

            JLabel modelLabel = new JLabel("Model:");
            modelLabel.setBounds(10, 20, 80, 25);
            sellPanel.add(modelLabel);

            JTextField modelText = new JTextField();
            modelText.setBounds(100, 20, 165, 25);
            sellPanel.add(modelText);

            JLabel yearLabel = new JLabel("Year:");
            yearLabel.setBounds(10, 50, 80, 25);
            sellPanel.add(yearLabel);

            JTextField yearText = new JTextField();
            yearText.setBounds(100, 50, 165, 25);
            sellPanel.add(yearText);

            JLabel priceLabel = new JLabel("Price:");
            priceLabel.setBounds(10, 80, 80, 25);
            sellPanel.add(priceLabel);

            JTextField priceText = new JTextField();
            priceText.setBounds(100, 80, 165, 25);
            sellPanel.add(priceText);

            JLabel infoLabel = new JLabel("Info:");
            infoLabel.setBounds(10, 110, 80, 25);
            sellPanel.add(infoLabel);

            JTextField infoText = new JTextField();
            infoText.setBounds(100, 110, 165, 25);
            sellPanel.add(infoText);

            JLabel roleLabel = new JLabel("Role:");
            roleLabel.setBounds(10, 140, 150, 25);
            sellPanel.add(roleLabel);

            JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"buy", "rent"});
            roleComboBox.setBounds(100, 140, 150, 25);
            sellPanel.add(roleComboBox);

            JButton uploadButton = new JButton("Upload Image");
            uploadButton.setBounds(10, 170, 170, 25);
            sellPanel.add(uploadButton);

            JFileChooser fileChooser = new JFileChooser();
            uploadButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int result = fileChooser.showOpenDialog(null);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        selectedFile = fileChooser.getSelectedFile();  // Assign the selected file to the field
                        try {
                            // Read the selected image file into bytes
                            byte[] imageBytes = Files.readAllBytes(selectedFile.toPath());
                            // You can store or use the imageBytes as needed
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });

            JButton sellButton = new JButton("Sell");
            sellButton.setBounds(10, 200, 80, 25);
            sellPanel.add(sellButton);

            sellButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Retrieve details from the GUI
                    String model = modelText.getText();
                    int year = Integer.parseInt(yearText.getText());
                    int price = Integer.parseInt(priceText.getText());
                    String info = infoText.getText();
                    String role = (String) roleComboBox.getSelectedItem();

                    // Retrieve the image bytes from the file
                    byte[] imageBytes = getImageBytesFromFile(selectedFile);

                    // Invoke the sellCar method
                    boolean success = sellCar(model, year, price, info, imageBytes, role);

                    // Return to the WelcomeGui
                    if (success) {
                        sellFrame.dispose();
                        new WelcomeGui();
                    } else {
                        // Handle the case where database update fails
                        JOptionPane.showMessageDialog(null, "Failed to sell the car. Please try again.");
                    }
                }
            });

            sellFrame.setVisible(true);
        }

        public boolean sellCar(String model, int year, int price, String info, byte[] imageBytes, String role) {
            try {
                // Validate input

                // Your database update logic here
                // insertion to DB
                String insertSQL = "INSERT INTO cars (model, year, price, info, image, role) VALUES (?, ?, ?, ?, ?, ?)";
                try ( PreparedStatement statement = connection.prepareStatement(insertSQL)) {
                    statement.setString(1, model);
                    statement.setInt(2, year);
                    statement.setInt(3, price);
                    statement.setString(4, info);
                    statement.setBytes(5, imageBytes);
                    statement.setString(6, role);
                    statement.executeUpdate();
                }

                return true; // Return true if the database update was successful
            } catch (SQLException e) {
                e.printStackTrace();
                // Show an error message using JOptionPane
                JOptionPane.showMessageDialog(null, "An error occurred while selling the car.", "Error", JOptionPane.ERROR_MESSAGE);
                return false; // Return false if an error occurs
            }
        }

        private byte[] getImageBytesFromFile(File file) {
            try {
                return Files.readAllBytes(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;  //try and catch for null pictures
        }
    }

}
