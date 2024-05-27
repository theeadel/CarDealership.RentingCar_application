import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TransactionHistoryGui {

    public TransactionHistoryGui(List<Transaction> transactionHistory) {
        createTransactionHistoryGui(transactionHistory);
    }

    private void createTransactionHistoryGui(List<Transaction> transactionHistory) {
        JFrame frame = new JFrame("Transaction History");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultListModel<String> transactionListModel = new DefaultListModel<>();
        JList<String> transactionList = new JList<>(transactionListModel);

        // Populate the transaction list
        for (Transaction transaction : transactionHistory) {
            transactionListModel.addElement(transaction.toString());
        }

        JScrollPane scrollPane = new JScrollPane(transactionList);
        frame.add(scrollPane);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> frame.dispose());

        frame.add(closeButton, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    // Assuming you have a Transaction class with appropriate toString() method
    private static class Transaction {
        private String description;
        private double amount;

        public Transaction(String description, double amount) {
            this.description = description;
            this.amount = amount;
        }

        @Override
        public String toString() {
            return description + ": $" + amount;
        }
    }

    public static void main(String[] args) {
        // Example usage:
        // You need to replace this with the actual transaction history of the user
        List<Transaction> transactionHistory = List.of(
                new Transaction("Deposit", 1000),
                new Transaction("Withdrawal", 500),
                new Transaction("Transfer to UserX", 200)
        );

        new TransactionHistoryGui(transactionHistory);
    }
}
