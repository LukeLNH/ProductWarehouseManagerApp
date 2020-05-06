package model;

import exceptions.InsufficientUnitsException;
import exceptions.InvalidAmountException;

public class ProductEntry {
    //Class holding data and behavior of a single product in the list of products

    private String name;
    private String type;
    private double pricePerUnit;
    private double costPerUnit;
    private int unitsInStock;

    private int unitsSold;

    //REQUIRES: price, cost and units all >= 0
    //EFFECTS: Creates a ProductEntry with no units sold.
    public ProductEntry(String name, String type, double price, double cost, int units) throws InvalidAmountException {
        if (price < 0 || cost < 0 || units < 0) {
            throw new InvalidAmountException();
        }

        this.name = name;
        this.type = type;
        this.pricePerUnit = price; //selling price per unit
        this.costPerUnit = cost; //cost to make per unit
        this.unitsInStock = units;

        this.unitsSold = 0;
    }

    //EFFECTS: returns the name of the product
    public String getName() {
        return name;
    }

    //EFFECTS: returns the type of the product
    public String getType() {
        return type;
    }

    //EFFECTS: returns the selling price per unit of the product
    public double getPricePerUnit() {
        return pricePerUnit;
    }

    //EFFECTS: returns the production cost per unit of the product
    public double getCostPerUnit() {
        return costPerUnit;
    }

    //EFFECTS: returns the number of units in stock
    public int getNumStock() {
        return unitsInStock;
    }

    //EFFECTS: returns the number of units sold
    public int getUnitsSold() {
        return unitsSold;
    }

    //TODO: make tests
    public void setUnitsSold(int amount) throws InvalidAmountException {
        if (amount < 0) {
            throw new InvalidAmountException();
        } else {
            this.unitsSold = amount;
        }
    }

    //REQUIRES: amount >= 0
    //MODIFIES: this
    //EFFECTS: changes the selling price per unit of product to amount
    public void setPricePerUnit(double amount) throws InvalidAmountException {
        if (amount < 0) {
            throw new InvalidAmountException();
        }
        this.pricePerUnit = amount;
    }

    //REQUIRES: amount >= 0
    //MODIFIES: this
    //EFFECTS: changes the production per unit of product to amount
    public void setCostPerUnit(double amount) throws InvalidAmountException {
        if (amount < 0) {
            throw new InvalidAmountException();
        }
        this.costPerUnit = amount;
    }

    //MODIFIES: this
    //EFFECTS: adds num to units in stock
    public void addUnits(int num) {
        unitsInStock += num;
    }

    //REQUIRES: unitsInStock >= num
    //MODIFIES: this
    //EFFECTS: adds num to units in stock
    public void removeUnits(int num) throws InsufficientUnitsException, InvalidAmountException {
        if (num > unitsInStock) {
            throw new InsufficientUnitsException();
        } else if (num < 0) {
            throw new InvalidAmountException();
        }
        unitsInStock -= num;
    }

    // REQUIRES: unitsInStock >= num
    // MODIFIES: this
    // EFFECTS: moves num units in stock to units sold
    public void sellProduct(int num) throws InsufficientUnitsException, InvalidAmountException {
        if (num > unitsInStock) {
            throw new InsufficientUnitsException();
        } else if (num < 0) {
            throw new InvalidAmountException();
        }
        this.unitsSold += num;
        this.unitsInStock -= num;
    }

    // EFFECTS: Calculates the total profit earned from selling this product
    public double calculateProfit() {
        return (pricePerUnit - costPerUnit) * unitsSold;
    }
}
