package TradingSystem.Server.ServiceLayer.DummyObject;

import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;

import java.util.LinkedHashMap;
import java.util.Map;

public class DummyShoppingHistory {

    private Integer userID;
    private Integer storeID;

    //productID_quantity
    //private ConcurrentHashMap<DummyProduct,Integer> products;
    private LinkedHashMap<DummyProduct,Integer> products;

    //private Date date;
    private Double finalPrice;

    public DummyShoppingHistory(ShoppingHistory toCopyShoppingHistory) {
        this.userID = toCopyShoppingHistory.getUserID();
        this.storeID = toCopyShoppingHistory.getStoreID();
        //this.date = toCopyShoppingHistory.getDate();
        this.finalPrice = toCopyShoppingHistory.getFinalPrice();
        //this.products = new ConcurrentHashMap<>();
        this.products = new LinkedHashMap<>();
        for (Product p : toCopyShoppingHistory.getProducts().keySet()){
            this.products.put(new DummyProduct(p), toCopyShoppingHistory.getProducts().get(p));
        }
    }

    public DummyShoppingHistory(Map<String, Object> map) {
        this.userID = (Integer) map.get("userID");
        this.storeID = (Integer) map.get("storeID");
        //this.date = (Date) map.get("date");
        Double tmpFinalPrice;
        try {
            tmpFinalPrice = (Double) map.get("finalPrice");
        }
        catch (Exception e){
            tmpFinalPrice = new Double((Integer) map.get("finalPrice"));
        }
        this.finalPrice = tmpFinalPrice;
        //this.products = (ConcurrentHashMap<DummyProduct, Integer>) map.get("products");
        this.products = (LinkedHashMap<DummyProduct, Integer>) map.get("products");
    }

    public Integer getUserID() {
        return userID;
    }

    public Integer getStoreID() {
        return storeID;
    }

    /*
    public ConcurrentHashMap<DummyProduct, Integer> getProducts() {
        return products;
    }

     */
    public LinkedHashMap<DummyProduct, Integer> getProducts() {
        return products;
    }

    /*
    public Date getDate() {
        return date;
    }

     */

    public Double getFinalPrice() {
        return finalPrice;
    }
}
