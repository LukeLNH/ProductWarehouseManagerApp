package persistence;

import model.PendingOrder;
import model.ProductEntry;
import model.ProductList;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest {
    @Test
    public void testDummyConstructor() {
        JsonReader dummy = new JsonReader();
    }

    @Test
    public void testJsonReaderNoError() {
        String filePath = "./data/JsonWriterTestData";

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

    @Test
    public void testJsonReaderIOException() {
        String filePath = "./data/InvalidWarehouse";
        try {
            JsonReader.readWarehouse(filePath);
        } catch (IOException e) {
            //all good
        } catch (Exception e) {
            fail("Unexpected Exception thrown");
        }
    }
}
