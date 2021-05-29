package TradingSystem.Server.DomainLayer.ShoppingComponent;

import java.util.Date;

import TradingSystem.Server.DataLayer.Services.Data_Controller;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import org.springframework.beans.factory.annotation.Autowired;


import java.text.SimpleDateFormat;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingHistory {

    @Autowired
    public static Data_Controller data_controller;

    public static void setData_controller(Data_Controller data_controller) {
        ShoppingHistory.data_controller = data_controller;
    }

    private Integer userID;
    private Integer storeID;

    //product_quantity
    private final List<Product> products;

    private Date date;

    private Double finalPrice;


    public Integer getUserID() {
        return userID;
    }

    public ShoppingHistory(ShoppingBag toCopyShoppingBag, List<Product> products) {
        this.userID = toCopyShoppingBag.getUserID();
        this.storeID = toCopyShoppingBag.getStoreID();
        this.products = products;
        this.date = new Date(System.currentTimeMillis());
        this.finalPrice = toCopyShoppingBag.getFinalPrice();
    }


    public Integer getStoreID() {
        return storeID;
    }

    public List<Product> getProducts() {
        return products;
    }

    public Date getDate() {
        return date;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public boolean isProductExist(int productId){
        for(Product product: this.products){
            if(product.getProductID()==productId)
                return true;
        }
        return false;
    }
}
