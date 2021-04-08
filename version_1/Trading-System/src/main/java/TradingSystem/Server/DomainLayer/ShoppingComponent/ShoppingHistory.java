package TradingSystem.Server.DomainLayer.ShoppingComponent;

import java.util.Date;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;


import java.text.SimpleDateFormat;

import java.util.concurrent.ConcurrentHashMap;

public class ShoppingHistory {

    private Integer userID;
    private Integer storeID;

    //productID_quantity
    private ConcurrentHashMap<Integer,Integer> products;

    private Date date;

    SimpleDateFormat formatter;

    private Double finalPrice;

//    public ShoppingHistory(Integer userID, Integer storeID, ConcurrentHashMap<Integer, Integer> products, Double finalPrice) {
//        this.userID = userID;
//        this.storeID = storeID;
//        this.products = products;
//        this.finalPrice = finalPrice;
//        this.formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
//        this.data= (Data) new Date(System.currentTimeMillis());
//    }

    public Integer getUserID() {
        return userID;
    }

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
    public Integer getStoreID() {
        return storeID;
    }

    public ConcurrentHashMap<Integer, Integer> getProducts() {
        return products;
    }

//    public SimpleDateFormat getFormatter() {
//        return formatter;
//    }



    public Double getFinalPrice() {
        return finalPrice;
    }
}
