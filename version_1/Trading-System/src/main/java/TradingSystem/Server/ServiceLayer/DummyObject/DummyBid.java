package TradingSystem.Server.ServiceLayer.DummyObject;

import TradingSystem.Server.DomainLayer.StoreComponent.Bid;

import java.util.Map;

public class DummyBid {

    private Integer userID;
    private Integer productID;
    private Integer quantity;
    private Double price;

    public DummyBid(Bid bid) {
        this.userID = bid.getUserID();
        this.productID = bid.getProductID();
        this.price = bid.getPrice();
        this.quantity=bid.getQuantity();
    }

    public DummyBid(Integer userID, Integer productID, Double price,Integer quantity) {
        this.userID = userID;
        this.productID = productID;
        this.price = price;
        this.quantity=quantity;
    }

    public DummyBid(Map<String, Object> map) {
        this.userID = (Integer) map.get("userID");
        this.productID = (Integer) map.get("productID");
        this.quantity = (Integer) map.get("quantity");
        Double tmpPrice;
        try {
            tmpPrice = (Double) map.get("price");
        }
        catch (Exception e){
            tmpPrice = new Double((Integer) map.get("price"));
        }
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
