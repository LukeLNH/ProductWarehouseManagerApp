package model;

import exceptions.InvalidAmountException;

public class PendingOrder {
    private String name;
    private String type;
    private int quantity;
    private String buyerID;

    public PendingOrder(String name, String type, int quantity, String buyerID) throws InvalidAmountException {
        if (quantity <= 0) {
            throw new InvalidAmountException();
        }
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.buyerID = buyerID;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getBuyerID() {
        return buyerID;
    }
}
