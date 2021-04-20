package TradingSystem.Server.DomainLayer.StoreComponent;

import TradingSystem.Server.ServiceLayer.DummyObject.NewResponse;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Product {


    private Integer productID;
    private String productName;
    private String category;
    private Double price;
    private Double rate;
    private Integer quantity;

    private final Lock lock = new ReentrantLock();
    //userID_comments
    private ConcurrentHashMap<Integer,String> productComments;
    //userID_Rate
    private ConcurrentHashMap<Integer, Double> productRating;

    public Product(Integer productID, String productName, String category, Double price) {
        this.productID = productID;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.productComments = new ConcurrentHashMap<Integer, String>();
        this.productRating = new ConcurrentHashMap<Integer, Double>();
        this.quantity = 0;
    }

    public Product(Integer productID, String productName, String category, Double price, int quantity) {
        this.productID = productID;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.productComments = new ConcurrentHashMap<Integer, String>();
        this.productRating = new ConcurrentHashMap<Integer, Double>();
        this.quantity = quantity;
    }

    public Product(Product toCopyProduct) {
        this.productID = toCopyProduct.productID;
        this.productName = toCopyProduct.productName;
        this.category = toCopyProduct.category;
        this.price = toCopyProduct.price;
        this.rate = toCopyProduct.rate;
        this.productComments=new ConcurrentHashMap<Integer, String>();
        this.productRating=new ConcurrentHashMap<Integer, Double>();
        this.quantity = toCopyProduct.quantity;
    }

    public Lock getLock() {
        return lock;
    }

    public void lockProduct() {
        this.lock.lock();
    }
    public void unlockProduct(){
        this.lock.unlock();
    }

    public boolean productIsLock() {
        return this.lock.tryLock();
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public NewResponse addComment(Integer userID, String comment)
    {
        if(productComments.containsKey(userID)){
            return new NewResponse(true, "User can not post more than one comment on a product");
        }
        this.productComments.put(userID,comment);
        return new NewResponse("The response writing was performed successfully");
    }

    public Response removeComment(Integer userID)
    {
        if(productComments.containsKey(userID)){
            this.productComments.remove(userID);
            return new Response(false, "The comment has been successfully deleted");
        }
        return new Response(true, "The user has no comment for this product");

    }

    public List<String> getComments()
    {
        List<String> Comments=new ArrayList<>();
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
        List<String> Comments=new ArrayList<String>();
        Set<Integer> userId = this.productComments.keySet();
        for (Integer id : userId) {
            String com=this.productComments.get(id);
            Comments.add(com);
        }
        return Comments;
    }


}
