package TradingSystem.Server.ServiceLayer.DummyObject;

import TradingSystem.Server.DomainLayer.StoreComponent.Bid;

import java.util.Map;

public class DummyBid {

    private Integer userID;
    private Integer productID;
    private Integer quantity;
    private Integer productPrice;

    public DummyBid(Bid bid) {
        this.userID = bid.getUserID();
        this.productID = bid.getProductID();
        this.productPrice = bid.getPrice();
        this.quantity=bid.getQuantity();
    }

    public DummyBid(Integer userID, Integer productID, Integer price,Integer quantity) {
        this.userID = userID;
        this.productID = productID;
        this.productPrice = price;
        this.quantity=quantity;
    }

    public DummyBid(Map<String, Object> map) {
        this.userID = (Integer) map.get("userID");
        this.productID = (Integer) map.get("productID");
        this.quantity = (Integer) map.get("quantity");
        this.productPrice=(Integer) map.get("productPrice");
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Integer getPrice() {
        return productPrice;
    }

    public void setPrice(Integer price) {
        this.productPrice = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
