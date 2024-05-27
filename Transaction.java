/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 11User
 */
public class Transaction {
 private static int lastTransactionId = 0;
    private int transactionId;
    private User buyer;
    private User seller;
    private Car car;
    private Date date;
    private List<Transaction> transactionHistory;

    public Transaction(User buyer, User seller, Car car) {
        this.transactionId = ++lastTransactionId;
        this.buyer = buyer;
        this.seller = seller;
        this.car = car;
        this.date = new Date();
        this.transactionHistory = new ArrayList<>();

    }

    public int getTransactionId() {
        return transactionId;
    }

    public User getBuyer() {
        return buyer;
    }

    public User getSeller() {
        return seller;
    }

    public Car getCar() {
        return car;
    }

    public Date getDate() {
        return date;
    }

    public String getFormattedDetails() {
        return String.format("Transaction ID: %d, Date: %s, Buyer: %s, Seller: %s, Car: %s",
                transactionId, date, buyer.getusername(), seller.getusername(), car.getFormattedDetails());
    }
}
