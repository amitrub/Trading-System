package TradingSystem.Server.DomainLayer.StoreComponent;

public class Product {

    ;
    private Integer productID;
    private String productName;
    private String category;
    private Double price;

    public Product(Integer productID, String productName, String category, Double price) {
        this.productID=productID;
        this.productName = productName;
        this.category = category;
        this.price = price;
    }

    public Integer getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }


}
