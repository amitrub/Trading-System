package TradingSystem.Server.DomainLayer.StoreComponent;

public class Bid {

    private Integer userID;
    private Integer productID;
    private Double price;

    public Bid(Integer userID, Integer productID, Double price) {
        this.productID = productID;
        this.price = price;
        this.userID=userID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getProductID() { return productID; }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

}
