package TradingSystem.Server.DomainLayer.ShoppingComponent;

import java.util.Date;

import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;


import java.text.SimpleDateFormat;

import java.util.concurrent.ConcurrentHashMap;

public class ShoppingHistory {

    private Integer userID;
    private Integer storeID;

    //product_quantity
    private final ConcurrentHashMap<Product, Integer> products;

    private Date date;

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

    public ShoppingHistory(ShoppingBag toCopyShoppingBag, ConcurrentHashMap<Product, Integer> products) {
        this.userID = toCopyShoppingBag.getUserID();
        this.storeID = toCopyShoppingBag.getStoreID();
        this.products = products;
        this.date = new Date(System.currentTimeMillis());
        this.finalPrice = toCopyShoppingBag.getFinalPrice();

    }


    public Integer getStoreID() {
        return storeID;
    }

    public ConcurrentHashMap<Product, Integer> getProducts() {
        return products;
    }

    public Date getDate() {
        return date;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }
}
