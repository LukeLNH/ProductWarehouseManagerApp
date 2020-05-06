package persistence;

import model.PendingOrder;
import model.ProductEntry;
import model.ProductList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.*;

public class JsonWriter {
    public JsonWriter() {
        //dummy constructor
    }

    // EFFECTS: Writes the warehouse to a JSON data file as a JSONObject
    public static void write(ProductList warehouse, String filePath) throws IOException {
        JSONObject saveData = new JSONObject();
        JSONArray productDataArray = new JSONArray();
        JSONArray pendingOrdersArray = new JSONArray();

        for (int i = 0; i < warehouse.getNumProducts(); i++) {
            JSONObject temp = makeJsonProductObject(warehouse.getProductAtIndex(i));
            productDataArray.add(temp);
        }

        for (int i = 0; i < warehouse.getNumPendingOrders(); i++) {
            JSONObject temp = makeJsonPendingOrder(warehouse.getPendingOrderAtIndex(i));
            pendingOrdersArray.add(temp);
        }
        saveData.put(0, productDataArray);
        saveData.put(1, pendingOrdersArray);

        FileWriter fileWriter = new FileWriter(filePath);
        fileWriter.write(saveData.toString());
        fileWriter.flush();
    }

    // EFFECTS: Converts the consumed ProductEntry into a JSONObject
    private static JSONObject makeJsonProductObject(ProductEntry p) {
        JSONObject temp = new JSONObject();
        temp.put("name", p.getName());
        temp.put("type", p.getType());
        temp.put("pricePerUnit", p.getPricePerUnit());
        temp.put("costPerUnit", p.getCostPerUnit());
        temp.put("numStock", p.getNumStock());
        temp.put("unitsSold", p.getUnitsSold());
        return temp;
    }

    private static JSONObject makeJsonPendingOrder(PendingOrder pendingOrder) {
        JSONObject temp = new JSONObject();
        temp.put("name", pendingOrder.getName());
        temp.put("type", pendingOrder.getType());
        temp.put("quantity", pendingOrder.getQuantity());
        temp.put("buyerID", pendingOrder.getBuyerID());
        return temp;
    }

}
