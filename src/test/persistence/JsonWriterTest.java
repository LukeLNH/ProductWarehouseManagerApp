package persistence;

import exceptions.InvalidAmountException;
import exceptions.PreexistingProductException;
import model.PendingOrder;
import model.ProductEntry;
import model.ProductList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonWriterTest {
    private ProductEntry p1;
    private ProductEntry p2;
    private PendingOrder o1;
    private PendingOrder o2;
    private ProductList warehouse;
    private String filePath;

    @BeforeEach
    public void runBefore() throws InvalidAmountException, PreexistingProductException {
        p1 = new ProductEntry("Product 1", "Test", 50.0, 20.0, 70);
        p1.setUnitsSold(30);
        p2 = new ProductEntry("Product 2", "Test", 30.0, 7.0, 60);
        p2.setUnitsSold(40);
        o1 = new PendingOrder("Product 1", "Test", 20, "buyer1");
        o2 = new PendingOrder("Product 1", "Test", 30, "buyer2");



        warehouse = new ProductList();
        warehouse.addProductEntry(p1);
        warehouse.addProductEntry(p2);
        warehouse.addPendingOrder(o1);
        warehouse.addPendingOrder(o2);
        filePath = "./data/JsonWriterTestData";

    }

    @Test
    public void testDummyConstructor() {
        JsonWriter dummy = new JsonWriter();
    }

    @Test
    public void testJsonWrite() {
        try {
            JsonWriter.write(warehouse, filePath);
        } catch (IOException e) {
            fail("Unexpected IO exception");
        }

        try {
            ProductList products = JsonReader.readWarehouse(filePath);
            assertEquals(2, products.getNumProducts());

            ProductEntry temp1 = products.getProductAtIndex(0);
            assertEquals("Product 1", temp1.getName());
            assertEquals("Test", temp1.getType());
            assertEquals(50.0, temp1.getPricePerUnit());
            assertEquals(20.0, temp1.getCostPerUnit());
            assertEquals(70, temp1.getNumStock());
            assertEquals(30, temp1.getUnitsSold());

            ProductEntry temp2 = products.getProductAtIndex(1);
            assertEquals("Product 2", temp2.getName());
            assertEquals("Test", temp2.getType());
            assertEquals(30.0, temp2.getPricePerUnit());
            assertEquals(7.0, temp2.getCostPerUnit());
            assertEquals(60, temp2.getNumStock());
            assertEquals(40, temp2.getUnitsSold());

            PendingOrder temp3 = products.getPendingOrderAtIndex(0);
            assertEquals("Product 1", temp3.getName());
            assertEquals("Test", temp3.getType());
            assertEquals(20, temp3.getQuantity());
            assertEquals("buyer1", temp3.getBuyerID());

            PendingOrder temp4 = products.getPendingOrderAtIndex(1);
            assertEquals("Product 1", temp4.getName());
            assertEquals("Test", temp4.getType());
            assertEquals(30, temp4.getQuantity());
            assertEquals("buyer2", temp4.getBuyerID());

        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }
}
