/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author 11User
 */
public class Car {
    private static int lastCarId = 0;
    private int Carid;
    private String make;
    private String model;
    private int year;
    private double price;
    private String role;

    public Car(String make, String model, int year, double price, String role) {
        // Validation checks
        if (year < 0 || price < 0) {
            throw new IllegalArgumentException("Invalid input values for year or price");
        }

        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
        this.role = role;
        this.Carid = ++lastCarId;
    }

    Car() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public int getCarid() {
        return Carid;
    }

    public void setCarid(int Carid) {
        this.Carid = Carid;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFormattedDetails() {
        return String.format("Car ID: %d, %d %s %s - $%.2f, Role: %s", Carid, year, make, model, price, role);
    }

    
}
