package TradingSystem.Server.ServiceLayer.DummyObject;

import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class DummyShoppingHistory {

    private Integer userID;
    private Integer storeID;

    //productID_quantity
    private ConcurrentHashMap<DummyProduct,Integer> products;

    private Date date;
    private Double finalPrice;

    public DummyShoppingHistory(ShoppingHistory toCopyShoppingHistory) {
        this.userID = toCopyShoppingHistory.getUserID();
        this.storeID = toCopyShoppingHistory.getStoreID();
        this.date = toCopyShoppingHistory.getDate();
        this.finalPrice = toCopyShoppingHistory.getFinalPrice();
        this.products = new ConcurrentHashMap<>();
        for (Product p : toCopyShoppingHistory.getProducts().keySet()){
            this.products.put(new DummyProduct(p), toCopyShoppingHistory.getProducts().get(p));
        }
    }

    public Integer getUserID() {
        return userID;
    }

    public Integer getStoreID() {
        return storeID;
    }

    public ConcurrentHashMap<DummyProduct, Integer> getProducts() {
        return products;
    }

    public Date getDate() {
        return date;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }
}
