import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class BuyGui {

    /**
     *
     */
    private final DataStructuresProject dataStructuresProject;
    public JFrame buyFrame;
    public JPanel cardsPanel;

    public BuyGui(DataStructuresProject dataStructuresProject) {
        this.dataStructuresProject = dataStructuresProject;
        int sortOrder = displaySortOrderDialog(); // Show dialog to get user's sort order preference
        sortAndCreateBuyGui(sortOrder);
    }

    private int displaySortOrderDialog() {
        String[] sortOrderOptions = { "Sort by Price (Cheapest to Most Expensive)",
                "Sort by Price (Most Expensive to Cheapest)", "No Sorting" };
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
        Comparator<Object[]> priceComparator = Comparator.comparingInt(carData -> (int) carData[3]); // Assuming
                                                                                                     // price is at
                                                                                                     // index 3

        switch (sortOrder) {
            case 0:
                // Sort by price from cheapest to most expensive using Collections.sort and
                // priceComparator
                Collections.sort(data, priceComparator);
                break;
            case 1:
                // Sort by price from most expensive to cheapest using Collections.sort and
                // reversed priceComparator
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
        DataStructuresProject.backbutton = new JButton("Back");
        DataStructuresProject.backbutton.setBounds(700, 300, 80, 25);
        buyFrame.add(DataStructuresProject.backbutton);
        DataStructuresProject.backbutton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buyFrame.dispose();
                this.dataStructuresProject.new WelcomeGui(); // Change this line to go back to the login GUI
            }
        });

        cardsPanel = new JPanel();
        int cardWidth = 200; // Set the width of each card
        cardsPanel.setLayout(new GridLayout(0, buyFrame.getWidth() / cardWidth)); // Adjust the layout as per your
                                                                                  // preference

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
        carCard.setLayout(new BorderLayout()); // Set BorderLayout for carCard

        carCard.setPreferredSize(new Dimension(cardWidth, (int) (buyFrame.getHeight() * 0.75))); // Modify this line
                                                                                                 // to adjust the
                                                                                                 // card height

        JTextArea carInfoTextArea = new JTextArea();
        carInfoTextArea.setEditable(false);
        for (int i = 1; i < carData.length - 1; i++) {
            carInfoTextArea.append(columnNames[i] + ": " + carData[i] + "\n");
        }

        ImageIcon carImageIcon = new ImageIcon(
                getResizedImage(this.dataStructuresProject.getImageFromBytes((byte[]) carData[5]), cardWidth, buyFrame.getHeight() / 4));
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

    private void addCarToUserOwnedCars(Connection connection, String username, String carModel)
            throws SQLException {
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
        String buyerName = this.dataStructuresProject.currentUser.getusername();

        if (buyerName != null && !buyerName.isEmpty()) {
            String carModel = (String) carData[1];
            double carPrice = (int) carData[3];

            boolean success = isCarAvailable(carModel);
            double balance = this.dataStructuresProject.getBalanceForCurrentUser(DataStructuresProject.connection);
            if (success) {
                try {

                    if (balance > carPrice) {
                        DataStructuresProject.updateBalance(DataStructuresProject.connection, buyerName, -carPrice);
                        addCarToUserOwnedCars(DataStructuresProject.connection, buyerName, carModel);
                        JOptionPane.showMessageDialog(null, "Congratulations! You've bought the car.");
                        removeCarFromDisplay(carData);
                        markCarAsSold(carModel, buyerName);

                        buyFrame.setVisible(false);
                        this.dataStructuresProject.new WelcomeGui();
                    } else {
                        this.dataStructuresProject.showInsufficientBalanceMessage();
                        buyFrame.setVisible(false);
                        this.dataStructuresProject.new WelcomeGui();

                    }

                } catch (SQLException e) {
                    e.printStackTrace();

                }

            } else {
                JOptionPane.showMessageDialog(null, "The car you're trying to buy is already sold.");
                buyFrame.setVisible(false);
                this.dataStructuresProject.new WelcomeGui();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Invalid buyer name. Please try again.");
            buyFrame.setVisible(false);
            this.dataStructuresProject.new WelcomeGui();
        }
    }

    private boolean isCarAvailable(String carModel) {
        try {
            String selectQuery = "SELECT COUNT(*) FROM cars WHERE model = ? AND status = 'Available'";
            try (PreparedStatement selectStatement = DataStructuresProject.connection.prepareStatement(selectQuery)) {
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
            try (PreparedStatement updateStatement = DataStructuresProject.connection.prepareStatement(updateQuery)) {
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
            try (PreparedStatement deleteStatement = DataStructuresProject.connection.prepareStatement(deleteQuery)) {
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

    private static final String[] columnNames = { "For", "Model", "Year", "Price", "Info", "Image" };

    private ArrayList<Object[]> getAvailableCarsData() {
        ArrayList<Object[]> data = new ArrayList<>();

        try {
            String query = "SELECT c.carID, c.model, c.year, c.price, c.info, c.image, c.role "
                    + "FROM cars c ";

            try (PreparedStatement statement = DataStructuresProject.connection.prepareStatement(query)) {
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