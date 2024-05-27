/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.List;

/**
 *
 * @author 11User
 */
public class CarManager {
       private List<Car> cars;
    private List<User> users;
    private List<Transaction> transactions;

    public CarManager(List<Car> cars, List<User> users, List<Transaction> transactions) {
        this.cars = cars;
        this.users = users;
        this.transactions = transactions;
    }

    public void addCar(Car car) {
        cars.add(car);
    }

    public void removeCar(Car car) {
        cars.remove(car);
    }

    public List<Car> getAllCars() {
        return cars;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public List<User> getAllUsers() {
        return users;
    }

    public void processCarPurchase(User buyer, User seller, Car car) {
        
        Transaction transaction = new Transaction(buyer, seller, car);
        transactions.add(transaction);
        buyer.addTransactionToHistory(transaction);
        seller.addTransactionToHistory(transaction);
        buyer.deductBalance((int) car.getPrice());
        seller.addBalance((int) car.getPrice());
        removeCar(car);
    }
}
