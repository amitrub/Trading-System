package TradingSystem.Server.DomainLayer.ShoppingComponent;



import TradingSystem.Server.DomainLayer.StoreComponent.Product;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingBag {

    private static int nextShoppingBagID = 0;

    private Integer userID;
    private Integer storeID;
    //productID_quantity
    private ConcurrentHashMap<Integer,Integer> products;
    private Double finalPrice;

    public ShoppingBag(Integer userID, Integer storeID) {
        nextShoppingBagID=getnextShoppingBagID();
        this.userID = userID;
        this.storeID = storeID;
        products=new ConcurrentHashMap<Integer,Integer>();
        finalPrice=0.0;
    }

    private static synchronized int getnextShoppingBagID() {
        nextShoppingBagID++;
        return nextShoppingBagID;
    }

    public int getNextShoppingBagID() {
        return nextShoppingBagID;
    }

    public void addProduct(Integer productID, Integer quantity) {
        this.products.put(productID,quantity);
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getStoreID() {
        return storeID;
    }

    public void setStoreID(Integer storeID) {
        this.storeID = storeID;
    }

    public ConcurrentHashMap<Integer, Integer> getProducts() {
        return products;
    }

    public void setProducts(ConcurrentHashMap<Integer, Integer> products) {
        this.products = products;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }
}
