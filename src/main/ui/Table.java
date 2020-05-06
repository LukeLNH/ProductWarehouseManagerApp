package ui;

import model.ProductEntry;
import model.ProductList;

// Source: https://c4code.wordpress.com/2014/03/17/how-to-print-the-results-to-console-in-a-tabular-format-using-java/
// Owner of original code: Manas Ranjan Sahoo

public class Table {
    //Class for displaying the data in ProductList as a table

    //EFFECTS: Creates a simple table in the console that displays all the data in warehouse
    public static void simpleTable(ProductList warehouse) {
        // Print the list objects in tabular format.
        System.out.println("---------------------------------------------------------------------------------------"
                + "---------------------------------------------------------------");
        System.out.printf("%20s %20s %20s %20s %20s %20s %20s",
                "NAME", "TYPE", "PRICE", "COST", "STOCK", "SOLD", "PROFIT");
        System.out.println();
        System.out.println("---------------------------------------------------------------------------------------"
                + "---------------------------------------------------------------");
        for (int i = 0; i < warehouse.getNumProducts(); i++) {
            ProductEntry temp = warehouse.getProductAtIndex(i);
            System.out.format("%20s %20s %20s %20s %20s %20s %20s",
                    temp.getName(), temp.getType(), temp.getPricePerUnit(), temp.getCostPerUnit(), temp.getNumStock(),
                    temp.getUnitsSold(), temp.calculateProfit());
            System.out.println();
        }
        System.out.println("---------------------------------------------------------------------------------------"
                + "---------------------------------------------------------------");
    }

    //EFFECTS: Creates a table of a single row displaying information about one Product
    public static void simpleRow(ProductEntry p) {
        System.out.println("---------------------------------------------------------------------------------------"
                + "------------------------------------------------");
        System.out.printf("%20s %20s %20s %20s %20s %20s", "NAME", "TYPE", "PRICE", "COST", "STOCK", "SOLD");
        System.out.println();
        System.out.println("---------------------------------------------------------------------------------------"
                + "------------------------------------------------");
        System.out.format("%20s %20s %20s %20s %20s %20s",
                p.getName(), p.getType(), p.getPricePerUnit(), p.getCostPerUnit(), p.getNumStock(), p.getUnitsSold());
        System.out.println();
        System.out.println("---------------------------------------------------------------------------------------"
                + "------------------------------------------------");
    }
}

