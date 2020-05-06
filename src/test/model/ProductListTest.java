package model;

import exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ProductListTest {
    //Test class for ProductList class

    private ProductList warehouse;
    private ProductEntry p1;
    private ProductEntry p2;
    private ProductEntry p3;
    private ProductEntry p4;
    private ProductEntry p5;

    private PendingOrder po1;
    private  PendingOrder po2;

    @BeforeEach
    public void runBefore() throws InvalidAmountException {
        warehouse = new ProductList();
        p1 = new ProductEntry("Product 1", "Shirt", 10.00, 2.00, 50);
        p2 = new ProductEntry("Product 2", "Shirt", 15.00, 4.00, 55);
        p3 = new ProductEntry("Product 3", "Jeans", 40.00, 10.00, 70);
        p4 = new ProductEntry("Product 4", "Hat", 4.00, 1.00, 40);
        p5 = new ProductEntry("Product 5", "Gloves", 3.00, 0.50, 20);

        po1 = new PendingOrder("Order 1", "Test", 20, "test1");
        po2 = new PendingOrder("Order 2", "Test", 30, "test2");
    }

    @Test
    public void testAddProductEntry() throws InvalidAmountException {
        //test adding normally
        try {
            warehouse.addProductEntry(p1);
            assertEquals(1, warehouse.getNumProducts());
            assertTrue(warehouse.hasProduct("Product 1"));

            warehouse.addProductEntry(p2);
            assertEquals(2, warehouse.getNumProducts());
            assertTrue(warehouse.hasProduct("Product 2"));
        } catch (Exception e) {
            fail();
        }


        //test exception being thrown
        ProductEntry ptest = new ProductEntry("Product 1", "Long Sleeve", 15.00, 3.00, 70);
        try {
            warehouse.addProductEntry(ptest);
            fail();
        } catch (PreexistingProductException e) {
            assertEquals(2, warehouse.getNumProducts());
        }
    }

    @Test
    public void testRemoveProductEntry() throws PreexistingProductException {
        warehouse.addProductEntry(p1);
        warehouse.addProductEntry(p2);

        //testing to see it properly removes the product
        try {
            warehouse.removeProductEntry("Product 1");
            assertEquals(1, warehouse.getNumProducts());
            assertFalse(warehouse.hasProduct("Product 1"));


            warehouse.removeProductEntry("Product 2");
            assertEquals(0, warehouse.getNumProducts());
            assertFalse(warehouse.hasProduct("Product 2"));

        } catch (Exception e) {
            fail();
        }

        //testing that it throws an error
        try {
            warehouse.removeProductEntry("Product 4");
            fail();
        } catch (NonExistingProductException e) {
            assertEquals(0, warehouse.getNumProducts());
            assertFalse(warehouse.hasProduct("Product 4"));
        }


    }

    @Test
    public void testMostProfitableProducts() throws PreexistingProductException, InsufficientUnitsException, InsufficientListSizeException, InvalidAmountException {
        warehouse.addProductEntry(p1); //8
        warehouse.addProductEntry(p2); //11
        warehouse.addProductEntry(p3); //30
        warehouse.addProductEntry(p4); //3
        warehouse.addProductEntry(p5); //2.5

        p1.sellProduct(10);
        p2.sellProduct(10);
        p3.sellProduct(10);
        p4.sellProduct(10);
        p5.sellProduct(10);

        ArrayList<String> productNames = new ArrayList<>();
        productNames.add("Product 3");

        assertTrue(productNames.equals(warehouse.mostProfitableProducts(1)));

        productNames.add("Product 2");
        productNames.add("Product 1");



        //test no exception thrown
        try {
            assertTrue(productNames.equals(warehouse.mostProfitableProducts(3)));
        } catch (Exception e) {
            fail();
        }

        //test InsufficientListSizeException thrown
        try {
            warehouse.mostProfitableProducts(10);
            fail();
        } catch (InsufficientListSizeException e) {
            assertEquals(5, warehouse.getNumProducts());
        } catch (InvalidAmountException e) {
            fail();
        }

        //test InvalidAmountException thrown
        try {
            warehouse.mostProfitableProducts(-20);
            fail();
        } catch (InsufficientListSizeException e) {
            fail();
        } catch (InvalidAmountException e) {
            assertEquals(5, warehouse.getNumProducts());
        }
    }

    @Test
    public void testGetProductsOfType() throws PreexistingProductException {
        warehouse.addProductEntry(p1);
        warehouse.addProductEntry(p2);
        warehouse.addProductEntry(p3);
        warehouse.addProductEntry(p4);

        ArrayList<String> shirtsList = new ArrayList<>();
        shirtsList.add("Product 1");
        shirtsList.add("Product 2");
        assertTrue(shirtsList.equals(warehouse.getProductsOfType("Shirt")));

        ArrayList<String> emptyList = new ArrayList<>();
        assertTrue(emptyList.equals(warehouse.getProductsOfType("Shorts")));
    }

    @Test
    public void testGetProductsInPriceRange() throws PreexistingProductException {
        warehouse.addProductEntry(p1);
        warehouse.addProductEntry(p2);
        warehouse.addProductEntry(p3);
        warehouse.addProductEntry(p4);
        warehouse.addProductEntry(p5);

        ArrayList<String> productNames = new ArrayList<>();
        productNames.add("Product 1");
        productNames.add("Product 2");
        productNames.add("Product 4");

        assertTrue(productNames.equals(warehouse.getProductsInPriceRange(4.00, 15.00)));

    }

    @Test
    public void testGetProductsInCostRange() throws PreexistingProductException {
        warehouse.addProductEntry(p1);
        warehouse.addProductEntry(p2);
        warehouse.addProductEntry(p3);
        warehouse.addProductEntry(p4);
        warehouse.addProductEntry(p5);

        ArrayList<String> productNames = new ArrayList<>();
        productNames.add("Product 1");
        productNames.add("Product 2");
        productNames.add("Product 4");

        assertTrue(productNames.equals(warehouse.getProductsInCostRange(1.00, 4.00)));

    }

    @Test
    public void testGetProductOfName() throws PreexistingProductException, NonExistingProductException {
        warehouse.addProductEntry(p1);
        warehouse.addProductEntry(p2);
        warehouse.addProductEntry(p3);
        warehouse.addProductEntry(p4);
        warehouse.addProductEntry(p5);

        //testing exception not thrown
        try {
            assertTrue(p1 == warehouse.getProductOfName("Product 1"));
            assertTrue(p3 == warehouse.getProductOfName("Product 3"));
        } catch (Exception e) {
            fail();
        }

        //testing exception thrown
        try {
            warehouse.getProductOfName("Null");
            fail();
        } catch (NonExistingProductException e) {
            assertEquals(5, warehouse.getNumProducts());
        }
    }

    @Test
    public void testSize() throws PreexistingProductException {
        assertEquals(0, warehouse.getNumProducts());
        warehouse.addProductEntry(p1);
        warehouse.addProductEntry(p2);

        assertEquals(2, warehouse.getNumProducts());

        warehouse.addProductEntry(p3);
        warehouse.addProductEntry(p4);

        assertEquals(4, warehouse.getNumProducts());

    }

    @Test
    public void testHasProduct() throws PreexistingProductException {
        warehouse.addProductEntry(p1);
        warehouse.addProductEntry(p2);
        warehouse.addProductEntry(p3);
        warehouse.addProductEntry(p4);

        assertTrue(warehouse.hasProduct("Product 1"));
        assertFalse(warehouse.hasProduct("Product 10"));

    }

    @Test
    public void testGet() throws PreexistingProductException {
        warehouse.addProductEntry(p1);
        warehouse.addProductEntry(p2);
        warehouse.addProductEntry(p3);
        warehouse.addProductEntry(p4);

        assertTrue(p1.equals(warehouse.getProductAtIndex(0)));
        assertTrue(p4.equals(warehouse.getProductAtIndex(3)));
        assertFalse(p1.equals(warehouse.getProductAtIndex(2)));

        warehouse.removeAllProducts();
        assertEquals(0, warehouse.getNumProducts());
    }

    @Test
    public void testAddPendingOrder() {
        assertEquals(0, warehouse.getNumPendingOrders());
        warehouse.addPendingOrder(po1);
        warehouse.addPendingOrder(po2);
        assertEquals(2, warehouse.getNumPendingOrders());

        assertEquals(po1, warehouse.getPendingOrderAtIndex(0));
        assertEquals(po2, warehouse.getPendingOrderAtIndex(1));


        warehouse.removeAllPendingOrders();
        assertEquals(0, warehouse.getNumPendingOrders());
    }

    @Test
    public void testPendingOrderExceptions() throws PreexistingProductException, InvalidAmountException {
        warehouse.addProductEntry(p1);
        warehouse.addProductEntry(p2);
        warehouse.addProductEntry(p3);
        warehouse.addProductEntry(p4);

        PendingOrder o1 = new PendingOrder("Product 1", "Shirt", 60, "test1234");
        warehouse.addPendingOrder(o1);

        //test InsufficientUnits Exception being thrown
        try {
            warehouse.resolvePendingOrder(o1);
            fail();
        } catch (NonExistingProductException e) {
            fail();
        } catch (InsufficientUnitsException e) {
            //all good
        }

        //test NonExistingProductException being thrown
        try {
            warehouse.resolvePendingOrder(po1);
            fail();
        } catch (NonExistingProductException e) {
            //all good
        } catch (InsufficientUnitsException e) {
            fail();
        }

        //test no exception being thrown
        PendingOrder o2 = new PendingOrder("Product 1", "Shirt", 40, "test1234");
        warehouse.addPendingOrder(o2);
        try {
            warehouse.resolvePendingOrder(warehouse.getPendingOrderAtIndex(1));
            assertEquals(50-40, p1.getNumStock());
            assertEquals(40, p1.getUnitsSold());
        } catch (Exception e) {
            fail();
        }
    }
}
