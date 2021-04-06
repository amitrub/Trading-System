package TradingSystem.Server.DomainLayer.StoreComponent;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Inventory {

    //productID_product
    private ConcurrentHashMap<Integer, Product> products;
    //productID_storeID
    private ConcurrentHashMap<Integer, Integer> productPerStore;
    //productID_comments
    private ConcurrentHashMap<Integer, String> productComments;



    private static Inventory inventory = null;

    private Inventory()
    {
        this.products= new ConcurrentHashMap<Integer, Product>();
        this.productPerStore = new ConcurrentHashMap<Integer, Integer>();
        this.productComments = new ConcurrentHashMap<Integer, String>();
    }

    public static Inventory getInstance()
    {
        if (inventory == null)
            inventory = new Inventory();

        return inventory;
    }

    public void addProduct(Product p, Integer storeID){
        products.put(p.getProductID(),p);
        productPerStore.put(p.getProductID(),storeID);
    }

    public void deleteProduct(Integer productID,Integer storeID){

    }

    public Product getProduct(Integer productId) {
        return this.products.get(productId);
    }

    public void changeDetails(Integer productId, String productName, Double price, String category) {
        Iterator it = this.products.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int id = (int)pair.getKey();
            if(id==productId) {
                Product p= new Product(productId,productName,category,price);
                this.products.remove(pair);
                this.products.put(id, p);
            }
        }
        String err="The product does not exist in the system";
    }

    public void addCommentToProduct(Integer productId, String comment){
    this.productComments.put(productId,comment);
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

    public int getStoreID(Integer productID) {
        return this.productPerStore.get(productID);
    }
}