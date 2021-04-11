package TradingSystem.Server.DomainLayer.StoreComponent;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.*;
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

    public Product(Product toCopyProduct) {
        this.productID=toCopyProduct.productID;
        this.productName = toCopyProduct.productName;
        this.category = toCopyProduct.category;
        this.price = toCopyProduct.price;
        this.rate = toCopyProduct.rate;
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

    public Response addComment(Integer userID, String comment)
    {
        if(productComments.containsKey(userID)){
            return new Response(true, "User can not post more than one comment on a product");
        }
        this.productComments.put(userID,comment);
        return new Response(false, "The response writing was performed successfully");
    }

    public Response removeComment(Integer userID)
    {
        if(productComments.containsKey(userID)){
            this.productComments.remove(userID);
            return new Response(false, "The comment has been successfully deleted");
        }
        return new Response(true, "The user has no comment for this product");

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
        Integer NumOfUsaers = 0;
        Double SumOfRating = 0.0;
        Set<Integer> RatingSet = this.productRating.keySet();
        for (Integer id : RatingSet) {
            Double Rate = this.productRating.get(id);
            NumOfUsaers++;
            SumOfRating = SumOfRating + Rate;
        }
        return SumOfRating / NumOfUsaers;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + productID +
                ", name='" + productName + '\'' +
                ", category=" + category + '\'' +
                ", price=" + price +
                '}';
    }

    public List<String> getCommentsForProduct(int productID) {
        LinkedList<String> Comments=new LinkedList<String>();
        Set<Integer> userId = this.productComments.keySet();
        for (Integer id : userId) {
            String com=this.productComments.get(id);
            Comments.add(com);
        }
        return Comments;
    }

}
