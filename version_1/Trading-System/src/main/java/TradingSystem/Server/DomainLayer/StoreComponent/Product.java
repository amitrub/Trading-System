package TradingSystem.Server.DomainLayer.StoreComponent;


import TradingSystem.Server.DataLayer.Data_Modules.DataComment;
import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagProduct;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingHistory.DataHistoryProduct;
import TradingSystem.Server.DataLayer.Services.Data_Controller;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Product {

    @Autowired
    public static Data_Controller data_controller;
    public static void setData_controller(Data_Controller data_controller) {
        Product.data_controller = data_controller;
    }
    @Autowired
    private static TradingSystemImplRubin tradingSystem;
    public static void setTradingSystem(TradingSystemImplRubin tradingSystem) {
        Product.tradingSystem = tradingSystem;
    }

    private Integer storeID;
    private String storeName;
    private Integer productID;
    private String productName;
    private String category;
    private Double price;
    private Double rate;
    private Integer quantity;

    private final Lock lock = new ReentrantLock();
    //userID_comments
    private ConcurrentHashMap<Integer,String> productComments = new ConcurrentHashMap<>();
    //userID_Rate
    private ConcurrentHashMap<Integer, Double> productRating = new ConcurrentHashMap<>();

    public Product(Integer storeID, String storeName, Integer productID, String productName, String category, Double price) {
        this.storeID = storeID;
        this.storeName = storeName;
        this.productID = productID;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.quantity = 0;
    }

    public Product(Integer storeID, String storeName, Integer productID, String productName, String category, Double price, int quantity) {
        this.storeID = storeID;
        this.storeName = storeName;
        this.productID = productID;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public Product(Product toCopyProduct, int quantity) {
        this.storeID = toCopyProduct.storeID;
        this.storeName = toCopyProduct.storeName;
        this.productID = toCopyProduct.productID;
        this.productName = toCopyProduct.productName;
        this.category = toCopyProduct.category;
        this.price = toCopyProduct.price;
        this.rate = toCopyProduct.rate;
        this.quantity = quantity;
    }

    public Product(DataProduct product){
        System.out.println("==================================");
        System.out.println(product.getComments());
        System.out.println("==================================");
        this.storeID=product.getStore().getStoreID();
        this.storeName=product.getStore().getStoreName();
        this.productID=product.getProductID();
        this.productName=product.getProductName();
        this.category=product.getCategory();
        this.price=product.getPrice();
        //TODO add rate
        this.quantity = product.getQuantity();
        for (DataComment comment: product.getComments()){
            try {
                productComments.putIfAbsent(comment.getSubscriber().getUserID(),comment.getComment());
            }
            catch (Exception e){

            }
        }
    }

    public Product(DataHistoryProduct product, int storeID, String storename){
        this.storeID=storeID;
        this.storeName=storename;
        this.productID= product.getProductID();
        this.productName=product.getProductName();
        this.category=product.getCategory();
        this.price=product.getPrice();
        this.quantity = product.getQuantity();
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
        data_controller.setQuantity(productID, quantity);
        this.quantity = quantity;
    }

    public Response addComment(Integer userID, String comment)
    {
        if(productComments.containsKey(userID)){
            return new Response(true, "User can not post more than one comment on a product");
        }
        data_controller.addCommentToProduct(productID, userID, comment);
        this.productComments.put(userID,comment);
        return new Response("The response writing was performed successfully");
    }

    public Response removeComment(Integer userID)
    {
        if(productComments.containsKey(userID)){
            this.productComments.remove(userID);
            return new Response("The comment has been successfully deleted");
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

    public boolean isUserComment(int userID){
        return productComments.containsKey(userID);
    }


    public int getStoreID() {
        return this.storeID;
    }

    public String getStoreName() {
        return this.storeName;
    }
}
