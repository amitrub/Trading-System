package TradingSystem.Server.DataLayer.Data_Modules;

import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import static TradingSystem.Server.ServiceLayer.Configuration.errMsgGenerator;

@Entity(name = "Product")
@Embeddable
public class DataProduct implements Serializable {

    private int storeID;
    private String storeName;
    @Id
    private final int productID;
    private final String productName;
    private final double price;
    private final String category;
    private int quantity;

    public DataProduct(){
        this.storeID = -1;
        this.storeName = "";
        this.productID = -1;
        this.productName = "";
        this.price = -1;
        this.category = "";
    }

    public DataProduct(int storeID, String storeName, int productID, String productName, double price, String category, int quantity) {
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

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getProductID() {
        return productID;
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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
