package TradingSystem.Server.DomainLayer.ShoppingComponent;

import javax.xml.crypto.Data;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingHistory {

    private Integer userID;
    private Integer storeID;

    //productID_quantity
    private ConcurrentHashMap<Integer,Integer> products;

    SimpleDateFormat formatter;
    private Data data;
    private Double finalPrice;

    public ShoppingHistory(Integer userID, Integer storeID, ConcurrentHashMap<Integer, Integer> products, Double finalPrice) {
        this.userID = userID;
        this.storeID = storeID;
        this.products = products;
        this.finalPrice = finalPrice;
        this.formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        this.data= (Data) new Date(System.currentTimeMillis());
    }

    public Integer getUserID() {
        return userID;
    }

    public Integer getStoreID() {
        return storeID;
    }

    public ConcurrentHashMap<Integer, Integer> getProducts() {
        return products;
    }

    public SimpleDateFormat getFormatter() {
        return formatter;
    }

    public Data getData() {
        return data;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }
}
