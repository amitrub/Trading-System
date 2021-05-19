package TradingSystem.Server.ServiceLayer.DummyObject;

import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import static TradingSystem.Server.ServiceLayer.Configuration.errMsgGenerator;

public class DummyProduct {

    private final int storeID;
    private final String storeName;
    private final int productID;
    private final String productName;
    private final double price;
    private final String category;
    private int quantity;

    public DummyProduct(){
        this.storeID = -1;
        this.storeName = "";
        this.productID = -1;
        this.productName = "";
        this.price = -1;
        this.category = "";
    }

    public DummyProduct(Product product){
        this.storeID = -1;
        this.storeName = "";
        this.productID = product.getProductID();
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.category = product.getCategory();
    }

    public DummyProduct(Map<String, Object> map) {
        this.storeID = (Integer) map.get("storeID");
        this.storeName = (String) map.get("storeName");
        this.productID = (Integer) map.get("productID");
        this.productName = (String) map.get("productName");
        Double tmpPrice;
        try {
            tmpPrice = (Double) map.get("price");
        }
        catch (Exception e){
            tmpPrice = new Double((Integer) map.get("price"));
        }
        this.price = tmpPrice;
        this.category = (String) map.get("category");
        this.quantity = (Integer) map.get("quantity");
    }

    public DummyProduct(int storeID, String storeName, int productID, String productName, double price, String category, int quantity) {
        this.storeID = storeID;
        this.storeName = storeName;
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
    }



    public int getStoreID() {
        return storeID;
    }

    public int getProductID() {
        return productID;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getProductName() {
        return productName;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
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
                    int quantity = jsonResponse.getInt("quantity");
                    DummyProduct dummyProduct = new DummyProduct(storeID, storeName, productID, productName, price, category, quantity);
                    dummySearchArr.add(dummyProduct);
                }

                return dummySearchArr;
            } catch (Exception e) {
                System.out.println(errMsgGenerator("Service", "DummySearch", "36", "error in making dummySearch from JSON object"));
            }
        return dummySearchArr;
    }

    @Override
    public String toString() {
//        return "DummyProduct{" +
//                "storeID=" + storeID +
//                ", storeName='" + storeName + '\'' +
//                ", productID=" + productID +
//                ", productName='" + productName + '\'' +
//                ", price=" + price +
//                ", category='" + category + '\'' +
//                '}';
        JSONObject JO = new JSONObject();
        try {
            JO.put("storeID", storeID);
            JO.put("storeName", storeName);
            JO.put("productID", productID);
            JO.put("productName", productName);
            JO.put("price", price);
            JO.put("category", category);
        } catch (Exception e) {
            System.out.println("DummyProduct toString error");
        }
        return JO.toString();
    }
}
