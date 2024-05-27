import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

        JComboBox<String> roleComboBox = new JComboBox<>(new String[] { "buy", "rent" });
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
                    selectedFile = fileChooser.getSelectedFile(); // Assign the selected file to the field
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
            try (PreparedStatement statement = DataStructuresProject.connection.prepareStatement(insertSQL)) {
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
            JOptionPane.showMessageDialog(null, "An error occurred while selling the car.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false; // Return false if an error occurs
        }
    }

    private byte[] getImageBytesFromFile(File file) {
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // try and catch for null pictures
    }
}