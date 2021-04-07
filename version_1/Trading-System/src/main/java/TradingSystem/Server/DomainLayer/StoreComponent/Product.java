package TradingSystem.Server.DomainLayer.StoreComponent;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Product {

    ;
    private Integer productID;
    private String productName;
    private String category;
    private Double price;
    private Double rate;
    //userID_comments
    private ConcurrentHashMap<Integer,String> productComments;
    //userID_Rate
    private ConcurrentHashMap<Integer, Double> productRating;

    public Product(Integer productID, String productName, String category, Double price) {
        this.productID=productID;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.productComments=new ConcurrentHashMap<Integer, String>();
        this.productRating=new ConcurrentHashMap<Integer, Double>();
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

    public synchronized Double getRate() {
        return rate;
    }

    public synchronized void setRate(Double rate) {
        this.rate = rate;
    }

    public void addComment(Integer userID, String comment)
    {
        this.productComments.put(userID,comment);
    }

    public void removeComment(Integer userID)
    {
        this.productComments.remove(userID);
    }

    public LinkedList<String> getComments()
    {
        LinkedList<String> Comments=new LinkedList<>();
        Iterator it = this.productComments.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            String s = (String)pair.getValue();
            Comments.add(s);
        }
        return Comments;

    }

    public void addRate(Integer userID, Double rating) {
        this.productRating.put(userID,rating);
    }

    public void removeRate(Integer userID) {
        this.productRating.remove(userID);
    }

    public Double CalculateRate()
    {
        Integer NumOfUsaers=0;
        Double SumOfRating=0.0;
        Iterator it = this.productRating.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Double Rate=(Double) pair.getValue();
            NumOfUsaers++;
            SumOfRating=SumOfRating+Rate;
        }
        return SumOfRating/NumOfUsaers;
    }
}
