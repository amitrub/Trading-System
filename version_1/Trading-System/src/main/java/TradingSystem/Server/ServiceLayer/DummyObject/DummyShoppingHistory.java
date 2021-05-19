package TradingSystem.Server.ServiceLayer.DummyObject;

import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DummyShoppingHistory {

    private Integer userID;
    private Integer storeID;

    //productID_quantity
    //private ConcurrentHashMap<DummyProduct,Integer> products;
    private LinkedHashMap<DummyProduct,Integer> products;

    private Date date;
    private Double finalPrice;

    public DummyShoppingHistory(ShoppingHistory toCopyShoppingHistory) {
        this.userID = toCopyShoppingHistory.getUserID();
        this.storeID = toCopyShoppingHistory.getStoreID();
        this.date = toCopyShoppingHistory.getDate();
        this.finalPrice = toCopyShoppingHistory.getFinalPrice();
        //this.products = new ConcurrentHashMap<>();
        this.products = new LinkedHashMap<>();
        for (Product p : toCopyShoppingHistory.getProducts().keySet()){
            DummyProduct dp = new DummyProduct(p);
           this.products.put(dp, toCopyShoppingHistory.getProducts().get(p));
        }
    }

    public DummyShoppingHistory(Map<String, Object> map) {
        this.userID = (Integer) map.get("userID");
        this.storeID = (Integer) map.get("storeID");
        this.date = (Date) map.get("date");
        Double tmpFinalPrice;
        try {
            tmpFinalPrice = (Double) map.get("finalPrice");
        }
        catch (Exception e){
            tmpFinalPrice = new Double((Integer) map.get("finalPrice"));
        }
        this.finalPrice = tmpFinalPrice;
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

    @Override
    public String toString() {
//        return "DummyShoppingHistory{" +
//                "userID=" + userID +
//                ", storeID=" + storeID +
//                ", products=" + products +
//                ", date=" + date +
//                ", finalPrice=" + finalPrice +
//                '}';
        JSONObject JO = new JSONObject();
        try {
            JO.put("userIDDDD", userID);
            JO.put("storeID", storeID);
            JSONArray prods = new JSONArray();
            for(DummyProduct p : products.keySet()) {
                JSONObject JProduct = new JSONObject();
                try {
                    JProduct.put("storeIDDDD", p.getStoreID());
                    JProduct.put("storeName", p.getStoreName());
                    JProduct.put("productID", p.getProductID());
                    JProduct.put("productName", p.getProductName());
                    JProduct.put("price", p.getPrice());
                    JProduct.put("category", p.getCategory());
                } catch (Exception e) {
                    System.out.println("DummyProduct toString error");
                }
                prods.put(JProduct);
            }
            JO.put("products", prods);
            JO.put("date", date);
            JO.put("finalPrice", finalPrice);
        } catch (Exception e) {
            System.out.println("DummyShoppingHistory toString error");
        }
        return JO.toString();
    }
}
