package TradingSystem.Server.DomainLayer.ShoppingComponent;

import java.util.Date;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingHistory {

    private Integer userID;
    private Integer storeID;

    //productID_quantity
    private ConcurrentHashMap<Integer,Integer> products;

    private Date date;

    private Double finalPrice;


    public ShoppingHistory(Integer userID, Integer storeID, ConcurrentHashMap<Integer, Integer> products, Date date, Double finalPrice) {
        this.userID = userID;
        this.storeID = storeID;
        this.products = products;
        this.date = date;
        this.finalPrice = finalPrice;
    }

    public DummyShoppingHistory createDummy(){
        return new DummyShoppingHistory(userID,storeID,products,date,finalPrice);
    }
}
