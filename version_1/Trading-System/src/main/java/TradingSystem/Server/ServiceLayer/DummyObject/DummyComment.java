package TradingSystem.Server.ServiceLayer.DummyObject;

import java.util.Map;

public class DummyComment {

    private int storeID;
    private String storeName;
    private int productID;
    private String productName;
    private String message;
    private int userID;

    public DummyComment(int storeID, String storeName, int productID, String productName, String message, int userID) {
        this.storeID = storeID;
        this.storeName = storeName;
        this.productID = productID;
        this.productName = productName;
        this.message = message;
        this.userID = userID;
    }

    public DummyComment(){
        this.storeID = -1;
        this.storeName = "";
        this.productID = -1;
        this.productName = "";
        this.message = "";
        this.userID = -1;
    }

    public DummyComment(Map<String, Object> map){
        this.storeID = (Integer) map.get("storeID");
        this.storeName = (String) map.get("storeName");
        this.productID = (Integer) map.get("productID");
        this.productName = (String) map.get("productName");
        this.message = (String) map.get("message");
        this.userID = (Integer) map.get("userID");
    }


    public int getStoreID() {
        return storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public int getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public String getMessage() {
        return message;
    }

    public int getUserID() {
        return userID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
