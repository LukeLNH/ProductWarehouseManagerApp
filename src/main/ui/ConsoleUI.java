package ui;

import exceptions.*;
import model.ProductEntry;
import model.ProductList;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.IOException;
import java.util.Scanner;

public class ConsoleUI {
    //Class that creates the Warehouse Manager UI for the user to interact with and use

    private ProductList warehouse;
    private Scanner input;
    private static String filePath;

    //EFFECTS: constructs a Product List (warehouse) with no product Entry
    public ConsoleUI() {
        makeWarehouse();
    }

    //EFFECTS: creates a warehouse that the user can interact with
    private void makeWarehouse() {

        input = new Scanner(System.in);
        boolean quitSession = false;
        boolean initializing = true;
        String userInput;

        while (!quitSession) {
            if (initializing) {
                loadWarehouse();
                initializing = false;
            }

            displayWarehouse();
            displayOptions();

            userInput = input.next();

            if (userInput.equals("0")) {
                quitSession = true;
            } else {
                processInput(userInput);
            }
        }
        System.out.println("Goodbye!");
    }

    //EFFECTS: Loads a warehouse from a saved file
    private void loadWarehouse() {

        System.out.println("Enter the name of the warehouse to load: ");
        System.out.println(); //create an empty break line - aesthetic purposes purely
        displaySavedFiles();
        String userInput = input.next();

        init(userInput);
    }

    //TODO: add cases to init() method
    // EFFECTS: Initializes the warehouse and the filePath
    private void init(String userInput) {

        filePath = "./data/" + userInput;
        try {
            //warehouse = JsonReader.readWarehouse(filePath);
            warehouse = JsonReader.readWarehouse("./data/TechWarehouse");
        } catch (IOException e) {
            System.out.println("This warehouse does not exist. Select an existing warehouse");
            System.out.println();
            loadWarehouse();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //EFFECTS: displays the different operations that can be done on the warehouse
    private void displayOptions() {
        System.out.println("Select an operations on warehouse: ");
        System.out.println("1: Add or Remove Product");
        System.out.println("2: Filter products");
        System.out.println("3: Find the most profitable products");
        System.out.println("4: Edit and Manage a product");
        System.out.println("5: Save");
        System.out.println("6: Load a different warehouse");
        System.out.println("0: Exit");
    }

    //EFFECTS: Determines what to do with the user input
    private void processInput(String userInput) {
        switch (userInput) {
            case "1":
                addOrRemove();
                break;

            case "2":
                filterProducts();
                break;

            case "3":
                findMostProfitable();
                break;

            case "4":
                editProduct();
                break;

            case "5":
                save();
                break;

            case"6":
                loadWarehouse();
                break;

            default:
                System.out.println("Invalid Input");

        }
    }

    //EFFECTS: saves the warehouse to the file path as a json
    private void save() {
        try {
            JsonWriter.write(warehouse, filePath);
            System.out.println("Warehouse saved successfully");
        } catch (IOException e) {
            System.out.println("Error: could not save file");
        }
    }

    //EFFECTS: determines whether to add or remove a product
    private void addOrRemove() {
        System.out.println("Select one of the following options: ");
        System.out.println("1: Add a product");
        System.out.println("2: Remove a product");

        String userInput = input.next();
        switch (userInput) {
            case "1":
                add();
                break;

            case "2":
                remove();
                break;

            default:
                System.out.println("Invalid Input");
        }
    }

    //MODIFIES: this
    //EFFECTS: adds a new product - specified by the user - to warehouse
    private void add() {
        System.out.println("Enter the name of the product as a String");
        String name = input.next();

        System.out.println("Enter the type of the product as a String:");
        String type = input.next();

        System.out.print("Enter the selling price of the product as a Double: $");
        double pricePerUnit = input.nextDouble();

        System.out.print("Enter the production cost of the product as a Double: $");
        double costPerUnit = input.nextDouble();

        System.out.println("Enter the units in stock of the product as an int:");
        int unitsInStock = input.nextInt();

        ProductEntry temp = null;
        try {
            temp = new ProductEntry(name, type, pricePerUnit, costPerUnit, unitsInStock);
            warehouse.addProductEntry(temp);
        } catch (InvalidAmountException e) {
            System.out.println("Invalid: Negative amount entered");
        } catch (PreexistingProductException e) {
            System.out.println("A product with this name already exists in the warehouse");
        }
    }

    //MODIFIES: this
    //EFFECTS: removes the specified product from warehouse
    private void remove() {
        System.out.println("Enter the name of the product to remove");
        String name = input.next();
        try {
            warehouse.removeProductEntry(name);
        } catch (NonExistingProductException e) {
            System.out.println("Invalid: Product does not exist in warehouse");
        }
    }

    //EFFECTS: determines how a product is filtered and filters warehouse appropriately
    private void filterProducts() {
        System.out.println("Select a filter option: ");
        System.out.println("1: Filter product by type");
        System.out.println("2: Filter product by selling price range");
        System.out.println("3: Filter product by production cost range");

        String userInput = input.next();
        switch (userInput) {
            case "1":
                System.out.println("Enter the type of product to filter by");
                userInput = input.next();
                System.out.println("Products of the desired type: " + warehouse.getProductsOfType(userInput));
                break;

            case "2":
                priceOrCostFilterHelper("price");
                break;

            case "3":
                priceOrCostFilterHelper("cost");
                break;

            default:
                System.out.println("Invalid Input");
        }
    }

    //REQUIRES: operation = "price" or "cost" ONLY
    //EFFECTS: filters warehouse according to the specified range
    private void priceOrCostFilterHelper(String operation) {
        boolean validOrder = false;
        double low = 0;
        double high = 0;
        while (!validOrder) {
            System.out.print("Enter the low end of the price/cost range as a double: $");
            low = input.nextDouble();
            System.out.print("Enter the high end of the price/cost range as a double: $");
            high = input.nextDouble();

            if (low <= high) {
                validOrder = true;
            } else {
                System.out.println("Invalid: the low end of the range is greater than the high end");
            }
        }

        if (operation.equals("price")) {
            System.out.println("Products in price range: " + warehouse.getProductsInPriceRange(low, high));
        } else if (operation.equals("cost")) {
            System.out.println("Products in cost range: " + warehouse.getProductsInCostRange(low, high));
        }
    }

    //EFFECTS: returns the top most profitable products
    private void findMostProfitable() {
        System.out.print("Input the number of top most profitable products to display as an int: ");
        int num = input.nextInt();
        try {
            System.out.println("Most profitable products: " + warehouse.mostProfitableProducts(num));
        } catch (InsufficientListSizeException e) {
            System.out.println("The warehouse does not have that many products");
        } catch (InvalidAmountException e) {
            System.out.println("Invalid: Entered a negative value");
        }
    }



    //EFFECTS: Determines what product to edit
    private void editProduct() {
        System.out.println("Enter the name of the product to edit: ");
        String userInput = input.next();
        try {
            manageProduct(warehouse.getProductOfName(userInput));
        } catch (NonExistingProductException e) {
            System.out.println("Invalid: Product does not exist in warehouse");
        }

    }

    //EFFECTS: determines what to do with the consumed product
    private void manageProduct(ProductEntry p) {
        Table.simpleRow(p);
        System.out.println("Select one of the following options for managing the product: ");
        System.out.println("1: Change the selling price or production cost");
        System.out.println("2: Add or remove units in stock");
        System.out.println("3: Sell the product");

        String userInput = input.next();
        switch (userInput) {
            case "1":
                setPriceOrCostHelper(p);
                break;

            case "2":
                setNumUnitsHelper(p);
                break;

            case "3":
                System.out.println("Enter the number of units to sell as an int: ");
                int numSell = input.nextInt();
                removeOrSellHelper(p, numSell, "sell");
                break;

            default:
                System.out.println("Invalid Input");
        }
    }

    //MODIFIES: this
    //EFFECTS: Determines whether to set the price or cost of the product
    private void setPriceOrCostHelper(ProductEntry p) {
        System.out.println("Select one of the following options: ");
        System.out.println("1: change the selling price");
        System.out.println("2: change the production cost");

        String userInput = input.next();
        switch (userInput) {
            case "1":
                setPriceHelper(p);
                break;

            case "2":
                setCostHelper(p);
                break;

            default:
                System.out.println("Invalid Input");
        }
    }

    //EFFECTS: Sets the price of the product
    private void setPriceHelper(ProductEntry p) {
        System.out.print("Enter the new selling price of the product as a double: $");
        double newPrice = input.nextDouble();
        try {
            p.setPricePerUnit(newPrice);
        } catch (InvalidAmountException e) {
            System.out.println("Invalid: Entered a negative value.");
        }
    }

    //EFFECTS: sets the cost of the product
    private void setCostHelper(ProductEntry p) {
        System.out.print("Enter the new production cost of the product as a double: $");
        double newCost = input.nextDouble();
        try {
            p.setCostPerUnit(newCost);
        } catch (InvalidAmountException e) {
            System.out.println("Invalid: Entered a negative value.");
        }
    }

    //MODIFIES: this
    //EFFECTS: changes the number of units in stock per product
    private void setNumUnitsHelper(ProductEntry p) {
        System.out.println("Select one of the following options: ");
        System.out.println("1: Add units to product");
        System.out.println("2: Remove units from product");

        String userInput = input.next();
        switch (userInput) {
            case "1":
                System.out.println("Enter the number of units to add as an int: ");
                int numUnitsAdd = input.nextInt();
                p.addUnits(numUnitsAdd);
                break;

            case "2":
                System.out.println("Enter the number of units to remove as an int: ");
                int numUnitsRemove = input.nextInt();
                removeOrSellHelper(p, numUnitsRemove, "remove");
                break;

            default:
                System.out.println("Invalid Input");
        }
    }

    //REQUIRES: operation = "sell" or "remove" ONLY
    //MODIFIES: this
    //EFFECTS: removes units from the product or sells the product
    private void removeOrSellHelper(ProductEntry p, int num, String operation) {
        if (operation.equals("sell")) {
            try {
                p.sellProduct(num);
            } catch (InsufficientUnitsException e) {
                System.out.println("Not enough units in stock");
            } catch (InvalidAmountException e) {
                System.out.println("Invalid: Entered a negative value.");
            }

        } else if (operation.equals("remove")) {
            try {
                p.removeUnits(num);
            } catch (InsufficientUnitsException e) {
                System.out.println("Not enough units in stock");
            } catch (InvalidAmountException e) {
                System.out.println("Invalid: Entered a negative value.");
            }
        }
    }

    //EFFECTS: displays the warehouse as a table
    private void displayWarehouse() {
        Table.simpleTable(warehouse);
    }


    //EFFECTS: display the names of all saved files.
    public void displaySavedFiles() {
        System.out.println("TechWarehouse");
        System.out.println("SportsWarehouse");
        System.out.println("ClothesWarehouse");
        //System.out.println("To create a new warehouse, enter the word 'new'");
    }
}
