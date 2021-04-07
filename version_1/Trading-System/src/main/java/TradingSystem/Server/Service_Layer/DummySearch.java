package TradingSystem.Server.Service_Layer;

public class DummySearch {

    private int storeID;
    private String storeName;
    private int productID;
    private String productName;
    private double price;
    private String category;

    public DummySearch(int storeID, String storeName, int productID, String productName, double price, String category) {
        this.storeID = storeID;
        this.storeName = storeName;
        this.productID = productID;
        this.productName = productName;
        this.price = price;
        this.category = category;
    }
}
