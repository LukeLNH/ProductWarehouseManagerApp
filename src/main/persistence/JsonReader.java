package persistence;

import exceptions.InvalidAmountException;
import exceptions.PreexistingProductException;
import model.PendingOrder;
import model.ProductEntry;
import model.ProductList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class JsonReader {

    public JsonReader() {
        //dummy constructor
    }

    // REQUIRES: the filePath exists
    // EFFECTS: Creates a warehouse from the JSONObject in the saved file
    public static ProductList readWarehouse(String filePath) throws IOException, ParseException,
            PreexistingProductException, InvalidAmountException {

        ProductList warehouse = new ProductList();
        FileReader file = new FileReader(filePath);
        JSONParser parser = new JSONParser();

        Object obj = parser.parse(file);
        JSONObject savedData = (JSONObject) obj;
        JSONArray productDataArray = (JSONArray) savedData.get("0");
        JSONArray pendingOrdersArray = (JSONArray) savedData.get("1");

        Iterator<JSONObject> productIterator = productDataArray.iterator();
        while (productIterator.hasNext()) {
            JSONObject productData = productIterator.next(); //(JSONObject) parser.parse(iterator.next());
            warehouse.addProductEntry(makeProductEntry(productData));
        }

        Iterator<JSONObject> pendingOrderIterator = pendingOrdersArray.iterator();
        while (pendingOrderIterator.hasNext()) {
            JSONObject pendingOrderData = pendingOrderIterator.next();
            warehouse.addPendingOrder(makePendingOrder(pendingOrderData));
        }
        return warehouse;
    }

    private static PendingOrder makePendingOrder(JSONObject data) throws InvalidAmountException {
        String name = (String) data.get("name");
        String type = (String) data.get("type");
        Long tempVal = (Long) data.get("quantity");
        int quantity = tempVal.intValue();
        String buyerID = (String) data.get("buyerID");
        PendingOrder order = new PendingOrder(name, type, quantity, buyerID);
        return order;
    }

    // EFFECTS: Creates a ProductEntry from the consumed JSONObject
    private static ProductEntry makeProductEntry(JSONObject productData) throws InvalidAmountException {
        String name = (String) productData.get("name");
        String type = (String) productData.get("type");
        double price = (double) productData.get("pricePerUnit");
        double cost = (double) productData.get("costPerUnit");
        Long tempVal = (Long) productData.get("numStock");
        int numStock = tempVal.intValue();
        tempVal = (Long) productData.get("unitsSold");
        int numUnitsSold = tempVal.intValue();

        ProductEntry temp = new ProductEntry(name, type, price, cost, numStock);
        temp.setUnitsSold(numUnitsSold);

        return temp;
    }
}
