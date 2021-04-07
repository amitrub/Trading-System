package TradingSystem.Server.ServiceLayer.DummyObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static TradingSystem.Server.ServiceLayer.Configuration.errMsgGenerator;

public class DummyProduct {

    private int storeID;
    private String storeName;
    private int productID;
    private String productName;
    private double price;
    private String category;

    public DummyProduct(){
        this.storeID = -1;
        this.storeName = "";
        this.productID = -1;
        this.productName = "";
        this.price = -1;
        this.category = "";
    }

    public DummyProduct(int storeID, String storeName, int productID, String productName, double price, String category) {
        this.storeID = storeID;
        this.storeName = storeName;
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.category = category;
    }

    public static ArrayList<DummyProduct> makeDummySearchFromJSON(JSONArray jsonArray) {
        ArrayList dummySearchArr = new ArrayList();
            try {
                for (int i=0;i<jsonArray.length();i++) {
                    JSONObject jsonResponse = jsonArray.getJSONObject(i);
                    int storeID = jsonResponse.getInt("storeID");
                    String storeName = jsonResponse.getString("storeName");
                    int productID = jsonResponse.getInt("productID");
                    String productName = jsonResponse.getString("productName");
                    double price = jsonResponse.getDouble("price");
                    String category = jsonResponse.getString("category");
                    DummyProduct dummyProduct = new DummyProduct(storeID, storeName, productID, productName, price, category);
                    dummySearchArr.add(dummyProduct);
                }

                return dummySearchArr;
            } catch (Exception e) {
                System.out.println(errMsgGenerator("Service", "DummySearch", "36", "error in making dummySearch from JSON object"));
            }
        return dummySearchArr;
    }
}
