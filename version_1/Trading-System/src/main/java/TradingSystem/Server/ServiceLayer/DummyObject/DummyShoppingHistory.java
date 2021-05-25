package TradingSystem.Server.ServiceLayer.DummyObject;
import TradingSystem.Server.DomainLayer.ShoppingComponent.*;

import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.OneToMany;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Entity(name = "ShoppingHistory")
@IdClass(DummyKeyShopping.class)
public class DummyShoppingHistory {

    @Id
    private Integer userID;
    @Id
    private Integer storeID;
//    @OneToMany(targetEntity = DummyProduct.class, mappedBy = "products")
//    private List<DummyProduct> products;
    private String date;
    private Double finalPrice;

    public DummyShoppingHistory(){

    }

    public DummyShoppingHistory(ShoppingHistory toCopyShoppingHistory) {
        this.userID = toCopyShoppingHistory.getUserID();
        this.storeID = toCopyShoppingHistory.getStoreID();
        this.date = toCopyShoppingHistory.getDate().toString();
        this.finalPrice = toCopyShoppingHistory.getFinalPrice();
//        this.products = new ArrayList<>();
//        for (Product p : toCopyShoppingHistory.getProducts()){
//            DummyProduct dp = new DummyProduct(p);
//            this.products.add(dp);
//        }
    }

    public DummyShoppingHistory(Map<String, Object> map) {
        this.userID = (Integer) map.get("userID");
        this.storeID = (Integer) map.get("storeID");
        this.date = ((Date) map.get("date")).toString();
        Double tmpFinalPrice;
        try {
            tmpFinalPrice = (Double) map.get("finalPrice");
        }
        catch (Exception e){
            tmpFinalPrice = new Double((Integer) map.get("finalPrice"));
        }
        this.finalPrice = tmpFinalPrice;
    //    this.products = (List<DummyProduct>) map.get("products");
    }

    public Integer getUserID() {
        return userID;
    }

    public Integer getStoreID() {
        return storeID;
    }

//    public List<DummyProduct> getProducts() {
//        return products;
//    }

    /*
    public Date getDate() {
        return date;
    } */


    public Double getFinalPrice() {
        return finalPrice;
    }

    @Override
    public String toString() {
        JSONObject JO = new JSONObject();
        try {
            JO.put("userID", userID);
            JO.put("storeID", storeID);
            JSONArray prods = new JSONArray();
//            for(DummyProduct p : products) {
//                JSONObject JProduct = new JSONObject();
//                try {
//                    JProduct.put("storeID", p.getStoreID());
//                    JProduct.put("storeName", p.getStoreName());
//                    JProduct.put("productID", p.getProductID());
//                    JProduct.put("productName", p.getProductName());
//                    JProduct.put("price", p.getPrice());
//                    JProduct.put("category", p.getCategory());
//                } catch (Exception e) {
//                    System.out.println("DummyProduct toString error");
//                }
//                prods.put(JProduct);
//            }
            JO.put("products", prods);
            JO.put("date", date);
            JO.put("finalPrice", finalPrice);
        } catch (Exception e) {
            System.out.println("DummyShoppingHistory toString error");
        }
        return JO.toString();
    }
}
