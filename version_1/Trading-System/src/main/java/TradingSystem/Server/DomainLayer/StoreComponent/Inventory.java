package TradingSystem.Server.DomainLayer.StoreComponent;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Inventory {

    Integer storeID;
    private static int nextProductID=0;
    //productID_product
    private ConcurrentHashMap<Integer, Product> products;
    //poductID_quantity
    private ConcurrentHashMap<Integer, Integer> productQuantity;
    //productID_comments
    private ConcurrentHashMap<Integer, Comment> productComments;


    public Inventory(Integer storeID)
    {
        this.storeID=storeID;
        this.products= new ConcurrentHashMap<Integer, Product>();
        this.productQuantity=new ConcurrentHashMap<Integer, Integer>();
        this.productComments = new ConcurrentHashMap<Integer, Comment>();
    }

    private static synchronized int getNextProductID() {
        nextProductID++;
        return nextProductID;
    }

    public void addProduct(String productName, String category, Double price) {
        Integer productID=getNextProductID();
        Product p=new Product(productID, productName, category, price);
        this.products.put(productID,p);
        this.productQuantity.put(productID,0);
    }

    public String deleteProduct(Integer productID,Integer storeID){
            this.productQuantity.remove(productID);
            this.products.remove(productID);
            this.productComments.remove(productID);
            return "The product delete";
    }

    public Product getProduct(Integer productId) {
        return this.products.get(productId);
    }

    public String addQuentityProduct(Integer productId, Integer quantity) {
        Iterator it = this.productQuantity.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int id = (int)pair.getKey();
            int newQuantity=(int)pair.getValue()+quantity;
            if(id==productId) {
                this.productQuantity.remove(productId);
                this.productQuantity.put(id, newQuantity);
                return "The Inventory update";
            }
        }
        return "The product does not exist in the system";
        }

    public void addCommentToProduct(Integer productId,Integer userID, String comment){
    this.productComments.put(productId,new Comment(userID,comment));
    }

    public boolean checkProductExist(Integer productID) {
        Iterator it = this.products.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int id = (int)pair.getKey();
            if(id==productID) {
                return true;
            }
        }
        return false;
    }

    public Integer getProductID(Integer storeID, String productName) {
        LinkedList<Integer> storeProducts=getAllTheStoreProductID(storeID);
        for (Integer i : storeProducts
             ) {
            Product p=products.get(i);
            if(p.getProductName().equals(productName)){
                return i;
            }
        }
        return -1;
    }

    public LinkedList<Integer> getAllTheStoreProductID(Integer storeID){
        LinkedList<Integer> storeProducts=new LinkedList<>();
        Iterator it = this.products.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int PID = (int)pair.getKey();
                storeProducts.add(PID);
            }
        return storeProducts;
    }

    public LinkedList<Product> getAllTheProducs(LinkedList<Integer> productsID){
        LinkedList<Product> products=new LinkedList<>();
        for (Integer i : productsID
        ) {
            Product p=products.get(i);
            products.add(p);
        }
        return products;
    }


    public String editProductDetails(Integer productId, String productName, Double price, String category) {
        Iterator it = this.products.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int id = (int)pair.getKey();
            if(id==productId) {
                Product p= new Product(productId,productName,category,price);
                this.products.remove(productId);
                this.products.put(id, p);
                return "The product update";
            }
        }
        return "The product does not exist in the system";
    }

    //todo- syncronize!
    public String reduceProduct(Integer productId, Integer quantityToReduce) {
        if(productQuantity.containsKey(productId)) {
            Iterator it = this.productQuantity.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                int id = (int) pair.getKey();
                int newQuantity = (int) pair.getValue() - quantityToReduce;
                if (id == productId) {
                    if (newQuantity > 0) {
                        this.productQuantity.remove(productId);
                        this.productQuantity.put(id, newQuantity);
                        return "The quantity update";
                    }
                    return "There is not enough products in the system";
                }
            }
        }
        return "The product does not exist in the system";
    }
}