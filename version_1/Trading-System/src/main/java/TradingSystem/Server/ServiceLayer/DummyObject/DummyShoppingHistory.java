package TradingSystem.Server.ServiceLayer.DummyObject;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class DummyShoppingHistory {

    private Integer userID;
    private Integer storeID;

    //productID_quantity
    private ConcurrentHashMap<Integer,Integer> products;

    private Date date;
    private Double finalPrice;

    public DummyShoppingHistory(Integer userID, Integer storeID, ConcurrentHashMap<Integer, Integer> products, Date date, Double finalPrice) {
        this.userID = userID;
        this.storeID = storeID;
        this.products = products;
        this.date = date;
        this.finalPrice = finalPrice;
    }
}
