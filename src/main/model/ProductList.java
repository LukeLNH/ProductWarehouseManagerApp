package model;

import exceptions.*;

import java.util.ArrayList;

public class ProductList {
    //Class holding data and behavior about the entire list of Products (i.e the entire Warehouse)

    private ArrayList<ProductEntry> products;
    private ArrayList<PendingOrder> pending;

    //EFFECTS: Constructs an empty warehouse
    public ProductList() {
        products = new ArrayList<>();
        pending = new ArrayList<>();
    }

    //REQUIRES: the name of the product is not already in the list of products
    //MODIFIES: this
    //EFFECTS: adds a ProductEntry to the list of products
    public void addProductEntry(ProductEntry p) throws PreexistingProductException {
        if (hasProduct(p.getName())) {
            throw new PreexistingProductException();
        }
        products.add(p);
    }

    //MODIFIES: this
    //EFFECTS: removes the product with the given name from the product list, otherwise do nothing
    public void removeProductEntry(String productName) throws NonExistingProductException {
        products.remove(getProductOfName(productName));
    }


    //REQUIRES: products.size() >= num
    //EFFECTS: returns a list of names of the top (num) most profitable products in the order of their profit
    public ArrayList<String> mostProfitableProducts(int num) throws InsufficientListSizeException,
            InvalidAmountException {

        if (products.size() < num) {
            throw new InsufficientListSizeException();
        } else if (num < 0) {
            throw new InvalidAmountException();
        }
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            int j = i;
            ProductEntry temp = products.get(j);
            while (names.contains(temp.getName())) {
                j++;
                temp = products.get(j);
            }

            for (ProductEntry p : products) {
                if (p.calculateProfit() >= temp.calculateProfit() && !names.contains(p.getName())) {
                    temp = p;
                }
            }
            names.add(temp.getName());
        }
        return names;
    }


    //EFFECTS: returns the a list of names containing only products of the desired type
    public ArrayList<String> getProductsOfType(String productType) {
        ArrayList<String> names = new ArrayList<>();

        for (ProductEntry p : products) {
            if (p.getType().toLowerCase().equals(productType.toLowerCase())) {
                names.add(p.getName());
            }
        }
        return names;
    }

    //EFFECTS: returns the a list of names containing only products with low <= price <= high
    public ArrayList<String> getProductsInPriceRange(double low, double high) {
        ArrayList<String> names = new ArrayList<>();

        for (ProductEntry p : products) {
            if (low <= p.getPricePerUnit() && p.getPricePerUnit() <= high) {
                names.add(p.getName());
            }
        }

        return names;
    }

    //EFFECTS: returns the a list of names containing only products with low <= cost <= high
    public ArrayList<String> getProductsInCostRange(double low, double high) {
        ArrayList<String> names = new ArrayList<>();

        for (ProductEntry p : products) {
            if (low <= p.getCostPerUnit() && p.getCostPerUnit() <= high) {
                names.add(p.getName());
            }
        }

        return names;
    }

    //REQUIRES: the product is in the list of products
    //EFFECTS: returns the product with the same given name
    public ProductEntry getProductOfName(String name) throws NonExistingProductException  {
        ProductEntry product = null;
        boolean inWarehouse = false;
        for (ProductEntry temp : products) {
            if (temp.getName().equals(name)) {
                product = temp;
                inWarehouse = true;
                break;
            }
        }
        if (!inWarehouse) {
            throw new NonExistingProductException();
        }
        return product;
    }

    //methods to help with testing
    //EFFECTS: produces the size of the list of products
    public int getNumProducts() {
        return products.size();
    }

    public int getNumPendingOrders() {
        return pending.size();
    }

    //EFFECTS: produces true if a product with the consumed name is in the list of products
    public boolean hasProduct(String name) {
        for (ProductEntry p : products) {
            if (p.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    //EFFECTS: returns the product at index i
    public ProductEntry getProductAtIndex(int i) {
        return products.get(i);
    }

    //EFFECTS: returns the pending order at index i
    public PendingOrder getPendingOrderAtIndex(int i) {
        return pending.get(i);
    }

    //EFFECTS: removes all products in the warehouse
    public void removeAllProducts() {
        this.products = new ArrayList<>();
    }

    //EFFECTS: removes all of the warehouse's pending orders
    public void removeAllPendingOrders() {
        this.pending = new ArrayList<>();
    }

    //EFFECTS: adds the pending order to the warehouse
    public void addPendingOrder(PendingOrder order) {
        pending.add(order);
    }

    //Note: This won't come up in the GUI, but it's possible to process an order that isn't on the pending orders list.
    // That's why there's no implementation to remove the order from the list of orders right now. GUI table, when
    // saved, makes an entirely new list of pending orders anyway/
    //EFFECTS: removes the pending order and changes numStock and numSold accordingly
    public void resolvePendingOrder(PendingOrder order) throws NonExistingProductException, InsufficientUnitsException,
                                                                                        InvalidAmountException {
        if (!hasProduct(order.getName())) {
            throw new NonExistingProductException();
        }
        ProductEntry p = getProductOfName(order.getName());
        p.sellProduct(order.getQuantity());
    }
}
