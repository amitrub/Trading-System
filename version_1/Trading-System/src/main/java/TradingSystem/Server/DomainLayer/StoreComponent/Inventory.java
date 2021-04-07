package TradingSystem.Server.DomainLayer.StoreComponent;

import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;

import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Inventory {

    private final Lock lock = new ReentrantLock();

    private Integer storeID;
    private String storeName;
    private static int nextProductID=0;
    //productID_product
    private ConcurrentHashMap<Integer, Product> products;
    //productID_quantity
    private ConcurrentHashMap<Integer, Integer> productQuantity;
    //productID_quantity
    private ConcurrentHashMap<Integer, Lock> productLock;


    public Inventory(Integer storeID, String storeName)
    {
        this.storeID=storeID;
        this.storeName=storeName;
        this.products= new ConcurrentHashMap<Integer, Product>();
        this.productQuantity=new ConcurrentHashMap<Integer, Integer>();
        this.productLock=new ConcurrentHashMap<Integer, Lock>();
    }

    private static synchronized int getNextProductID()
    {
        nextProductID++;
        return nextProductID;
    }

    public LinkedList<DummyProduct> ShowStoreProducts(){
        LinkedList<DummyProduct> products= new LinkedList<>();
        for(Map.Entry<Integer,Product> productEntry:this.products.entrySet()){
            products.add(new DummyProduct(this.storeID,this.storeName,productEntry.getKey(),productEntry.getValue().getProductName(),productEntry.getValue().getPrice(),productEntry.getValue().getCategory()));
        }
        return products;
    }

    public void addProduct(String productName, String category, Double price)
    {
        Integer productID=getNextProductID();
        Product p=new Product(productID, productName, category, price);
        this.products.put(productID,p);
        this.productQuantity.put(productID,0);
        this.productLock.put(productID,new ReentrantLock());
    }

    public String deleteProduct(Integer productID,Integer storeID)
    {
            this.productQuantity.remove(productID);
            this.products.remove(productID);
            this.productLock.remove(productID);
            return "The product delete";
    }

    public Product getProduct(Integer productId)
    {
        return this.products.get(productId);
    }

    public String addQuantityProduct(Integer productId, Integer quantity)
    {
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

    public void addCommentToProduct(Integer productId,Integer userID, String comment)
    {
    this.products.get(productId).addComment(userID,comment);
    }

    public void removeCommentFromProduct(Integer productId,Integer userID)
    {
        this.products.get(productId).removeComment(userID);
    }

    public LinkedList<String> getAllCommentsForProduct(Integer productID)
    {
        return this.products.get(productID).getComments();
    }

    public boolean checkProductExist(Integer productID)
    {
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

    public Integer getProductID(Integer storeID, String productName)
    {
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

    public LinkedList<Integer> getAllTheStoreProductID(Integer storeID)
    {
        LinkedList<Integer> storeProducts=new LinkedList<>();
        Iterator it = this.products.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int PID = (int)pair.getKey();
                storeProducts.add(PID);
            }
        return storeProducts;
    }

    public LinkedList<Product> getAllTheProducts(LinkedList<Integer> productsID)
    {
        LinkedList<Product> products=new LinkedList<>();
        for (Integer i : productsID
        ) {
            Product p=products.get(i);
            products.add(p);
        }
        return products;
    }

    public String editProductDetails(Integer productId, String productName, Double price, String category)
    {
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
    public boolean reduceProduct(Integer productId, Integer quantityToReduce)
    {
        if(productQuantity.containsKey(productId)) {
            Iterator it = this.productQuantity.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                int id = (int) pair.getKey();
                int newQuantity = (int) pair.getValue() - quantityToReduce;
                if (id == productId) {
                    if (newQuantity > 0) { //todo-check
                        this.productQuantity.remove(productId);
                        this.productQuantity.put(id, newQuantity);
                        return true;
                    }
                    return false;
                            //"There is not enough products in the system";
                }
            }
        }
        return false;
        // "The product does not exist in the system";
    }

    public LinkedList<Integer> getDummySearchByName( LinkedList<Integer> FinalID,String name)
    {
        LinkedList<Integer> products=new LinkedList<>();
        if(FinalID.isEmpty()) {
            Iterator it = this.products.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                int PID = (int) pair.getKey();
                Product p = (Product) pair.getValue();
                if (p.getProductName().equals(name)) {
                    products.add(PID);
                }
            }
        }
        else{
            Iterator it = this.products.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                int PID = (int) pair.getKey();
                Product p = (Product) pair.getValue();
                if (p.getProductName().equals(name)&&FinalID.contains(PID)) {
                    products.add(PID);
                }
            }
        }
        return products;
    }

    public LinkedList<Integer> getDummySearchByCategory(LinkedList<Integer> FinalID,String category)
    {
        LinkedList<Integer> products = new LinkedList<>();
        if(FinalID.isEmpty()) {
            Iterator it = this.products.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                int PID = (int) pair.getKey();
                Product p = (Product) pair.getValue();
                if (p.getCategory().equals(category)) {
                    products.add(PID);
                }
            }
        }
        else{
            Iterator it = this.products.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                int PID = (int) pair.getKey();
                Product p = (Product) pair.getValue();
                if (p.getCategory().equals(category)&&FinalID.contains(PID)) {
                    products.add(PID);
                }
            }
        }
        return products;
    }

    public LinkedList<Integer> getDummySearchByRate( LinkedList<Integer> FinalID,int prank)
    {
        LinkedList<Integer> products=new LinkedList<>();
        if(FinalID.isEmpty()) {
            Iterator it = this.products.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                int PID = (int) pair.getKey();
                Product p = (Product) pair.getValue();
                Double R = this.products.get(PID).getRate();
                if (R >= prank) {
                    products.add(PID);
                }
            }
        }
        else{
            Iterator it = this.products.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                int PID = (int) pair.getKey();
                Product p = (Product) pair.getValue();
                Double R = this.products.get(PID).getRate();
                if (R >= prank && FinalID.contains(PID)) {
                    products.add(PID);
                }
            }
        }
        return products;
    }

    public LinkedList<Integer> getDummySearchByPrice( LinkedList<Integer> FinalID,int minprice, int maxprice)
    {
        LinkedList<Integer> products=new LinkedList<>();
        if(FinalID.isEmpty()) {
            Iterator it = this.products.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                int PID = (int) pair.getKey();
                Product p = (Product) pair.getValue();
                Double Price = p.getPrice();
                if (Price >= minprice && Price <= maxprice) {
                    products.add(PID);
                }
            }
        }
        else{
            Iterator it = this.products.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                int PID = (int) pair.getKey();
                Product p = (Product) pair.getValue();
                Double Price = p.getPrice();
                if (Price >= minprice && Price <= maxprice && FinalID.contains(PID)) {
                    products.add(PID);
                }
            }
        }
        return products;
    }

    public LinkedList<DummyProduct> getDummySearchForList(LinkedList<Integer> products)
    {
        LinkedList<DummyProduct> dummyProducts =new LinkedList<DummyProduct>();
        for (Integer i:products
             ) {
            Product p=this.products.get(i);
            DummyProduct D=new DummyProduct(this.storeID,storeName,p.getProductID(),p.getProductName(),p.getPrice(),p.getCategory());//productComments.get(i).getRate());
            dummyProducts.add(D);
        }
        return dummyProducts;
    }

    public void addRatingToProduct(Integer productID, Integer userID, Double rating)
    {
       this.products.get(productID).addRate(userID,rating);
       Double Rate=this.products.get(productID).CalculateRate();
       this.products.get(productID).setRate(Rate);
    }

    public void removeRatingFromProduct(Integer productID, Integer userID)
    {
        this.products.get(productID).removeRate(userID);
        Double Rate=this.products.get(productID).CalculateRate();
        this.products.get(productID).setRate(Rate);
    }

    public Double CalculateRateForProduct(Integer productID)
    {
        return this.products.get(productID).CalculateRate();
    }

    public void lockProduct(Integer productID) {
        this.productLock.get(productID).lock();
    }

    public void unlockProduct(Collection<Integer> productID) {
        this.productLock.get(productID).unlock();
        this.productLock.get(productID).notifyAll();
    }

    //todo check!
    public boolean productIsLock(Integer productID) {
        return this.productLock.get(productID).tryLock();
    }
}