package model;

import exceptions.InsufficientUnitsException;
import exceptions.InvalidAmountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductEntryTest {
    //Test class for ProductEntry class

    private ProductEntry product;
    private String name;
    private String type;
    private double price;
    private double cost;
    private int units;

    @BeforeEach
    public void runBefore() throws InvalidAmountException {
        name = "Prototype1";
        type = "Mouse";
        price = 90.00;
        cost = 60.00;
        units = 100;
        product = new ProductEntry(name, type, price, cost, units);
    }

    @Test
    public void testConstructor() {
        ProductEntry temp = null;
        assertEquals(0, product.getUnitsSold());

        try {
            temp = new ProductEntry("temp", "temp", -10, 0, 0);
        } catch (InvalidAmountException e) {
            assertEquals(temp, null);
        }

        try {
            temp = new ProductEntry("temp", "temp", 0, -20, 0);
        } catch (InvalidAmountException e) {
            assertEquals(temp, null);
        }

        try {
            temp = new ProductEntry("temp", "temp", 0, 0, -30);
        } catch (InvalidAmountException e) {
            assertEquals(temp, null);
        }
    }

    @Test
    public void testAddUnits() {
        product.addUnits(50);
        assertEquals(100 + 50, product.getNumStock());

        product.addUnits(100);
        assertEquals(100 + 50 + 100, product.getNumStock());
    }

    @Test
    public void testRemoveUnits() throws InsufficientUnitsException, InvalidAmountException {
        product.removeUnits(20);
        assertEquals(100 - 20, product.getNumStock());

        product.removeUnits(80);
        assertEquals(100 - 20 - 80, product.getNumStock());

        try {
            product.removeUnits(50);
            fail();
        } catch (InsufficientUnitsException e) {
            assertEquals(100 - 20 - 80, product.getNumStock());
        }

        try {
            product.removeUnits(-120);
            fail();
        } catch (InvalidAmountException e) {
            assertEquals(100 - 20 - 80, product.getNumStock());
        }
    }

    @Test
    public void testSellProduct() throws InsufficientUnitsException, InvalidAmountException {
        product.sellProduct(10);
        assertEquals(100 - 10, product.getNumStock());
        assertEquals(10, product.getUnitsSold());

        product.sellProduct(90);
        assertEquals(100 - 10 - 90, product.getNumStock());
        assertEquals(100, product.getUnitsSold());

        try {
            product.sellProduct(50);
            fail();
        } catch (InsufficientUnitsException e) {
            assertEquals(100 - 10 - 90, product.getNumStock());
        }

        try {
            product.sellProduct(-300);
            fail() ;
        } catch (InvalidAmountException e) {
            assertEquals(100 - 10 - 90, product.getNumStock());
        }
    }

    @Test
    public void testCalculateProfit() throws InsufficientUnitsException, InvalidAmountException {
        assertEquals(0, product.calculateProfit());

        product.sellProduct(20);
        assertEquals(20 * (90.00 - 60.00), product.calculateProfit());

        product.sellProduct(50);
        assertEquals(70 * (90.00 - 60.00), product.calculateProfit());

        product.sellProduct(30);
        assertEquals(100 * (90.00 - 60.00), product.calculateProfit());

        product.addUnits(60);
        product.sellProduct(50);
        assertEquals(150 * (90.00 - 60.00), product.calculateProfit());
    }

    @Test
    public void testSetPricePerUnt() throws InvalidAmountException {
        product.setPricePerUnit(99.00);
        assertEquals(99.00, product.getPricePerUnit());

        try {
            product.setPricePerUnit(-50);
            fail();
        } catch (InvalidAmountException e) {
            assertEquals(99.00, product.getPricePerUnit());
        }
    }

    @Test
    public void testSetCostPerUnt() throws InvalidAmountException {
        product.setCostPerUnit(36.00);
        assertEquals(36.00, product.getCostPerUnit());

        try {
            product.setCostPerUnit(-20);
            fail();
        } catch (InvalidAmountException e) {
            assertEquals(36.00, product.getCostPerUnit());
        }
    }

    @Test
    public void testSetUnitsSold() {
        try {
            product.setUnitsSold(60);
            assertEquals(60, product.getUnitsSold());
        } catch (InvalidAmountException e) {
            fail("Unexpected IO exception");
        }

        try {
            product.setUnitsSold(-30);
            fail("Expected an exception");
        } catch (InvalidAmountException e) {
            //all good
        }
    }
}