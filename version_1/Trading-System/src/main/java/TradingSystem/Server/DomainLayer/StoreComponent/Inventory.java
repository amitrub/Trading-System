package TradingSystem.Server.DomainLayer.StoreComponent;

import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import com.fasterxml.jackson.core.PrettyPrinter;

import java.security.KeyPair;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import java.util.concurrent.ConcurrentHashMap;

public class Inventory {

    private final Integer storeID;
    private final String storeName;
    private int nextProductID=0;
    //productID_product
    private ConcurrentHashMap<Integer, Product> products;
    //productID_quantity
//    private ConcurrentHashMap<Integer, Integer> productQuantity;
    //productID_Lock
//    private ConcurrentHashMap<Integer, Lock> productLock;


    public Inventory(Integer storeID, String storeName)
    {
        this.storeID=storeID;
        this.storeName=storeName;
        this.products= new ConcurrentHashMap<Integer, Product>();
//        this.productQuantity=new ConcurrentHashMap<Integer, Integer>();
//        this.productLock=new ConcurrentHashMap<Integer, Lock>();
    }

    private synchronized int getNextProductID(){
        this.nextProductID++;
        return this.nextProductID;
    }

    public LinkedList<DummyProduct> ShowStoreProducts(){
        LinkedList<DummyProduct> products= new LinkedList<>();
        Set<Integer> productSet = this.products.keySet();
        for (Integer key : productSet){
            Product product = this.products.get(key);
            products.add(new DummyProduct(this.storeID,this.storeName,key,product.getProductName(),product.getPrice(),product.getCategory(), product.getQuantity()));
        }
        return products;
    }

    public Response addProduct(String productName, String category, Double price, int quantity){
        if (!IsProductNameExist(productName)){
            Integer productID=getNextProductID();
            Product p=new Product(productID, productName, category, price, quantity);
            this.products.put(productID,p);
//            this.productQuantity.put(productID,0);
//            this.productLock.put(productID,new ReentrantLock());
            return new Response(false,  "Add Product was successful");
        }
        else
            return new Response(true, "Error Product name is taken");

    }

    public boolean IsProductNameExist(String productName) {
        Set<Integer> productSet = this.products.keySet();
        for (Integer id : productSet) {
            Product product = this.products.get(id);
            if (productName.equals(product.getProductName()))
                return true;
        }
        return false;
    }

    public boolean checkProductsExistInTheStore(Integer productID, Integer quantity){
        return this.products.containsKey(productID) &&  this.products.get(productID).getQuantity() >= quantity;
    }

    public Response addQuantityProduct(Integer productId, Integer quantity){
        if(this.products.containsKey(productId)){
            Product product = this.products.get(productId);
            Integer oldQuantity = product.getQuantity();
            product.setQuantity(quantity+oldQuantity);
            return new Response(false,  "Add Product to Inventory was successful");
        }
        else
            return new Response(true, "The product does not exist in the system");
    }

    public Response deleteProduct(Integer productID){
        if(this.products.containsKey(productID)) {
//            this.productQuantity.remove(productID);
            this.products.remove(productID);
//            this.productLock.remove(productID);
            return new Response(false, "Remove Product from the Inventory was successful");
        }
        else
            return new Response(true, "The product does not exist in the system");
    }

    public Product getProduct(Integer productId)
    {
        return this.products.get(productId);
    }

    //todo- syncronize!
    public Response reduceProducts(ConcurrentHashMap<Integer, Integer> products_quantity) {
        System.out.println("storeID = "+ storeID);
        Set<Integer> PQ = products_quantity.keySet();
        for (Integer PID : PQ){
            int quantityToReduce =  products_quantity.get(PID);
            System.out.println("quantityToReduce = "+ quantityToReduce);
            if(products.containsKey(PID)) {
                Product product = this.products.get(PID);
                int quantity= product.getQuantity();
                int newQuantity = quantity - quantityToReduce;
                if (newQuantity <0) {
                    return new Response(true, "There are not enough units from a product "+PID +" In store "+storeID);
                }
                product.setQuantity(newQuantity);
//                this.productQuantity.remove(PID);
//                this.productQuantity.put(PID, newQuantity);
            }
            else {
                return new Response(true, "The product "+PID +" In store "+storeID +" does not exist in the system");
            }
        }
        return new Response(false, "Product inventory successfully updated");
    }

    public Response addCommentToProduct(Integer productId,Integer userID, String comment) {
        if (this.products.containsKey(productId)){
            return  this.products.get(productId).addComment(userID, comment);
        }
        else
            return new Response(true, "The product does not exist in the system");
    }

    public Response removeCommentFromProduct(Integer productId,Integer userID)
    {
        if (this.products.containsKey(productId)){
            return this.products.get(productId).removeComment(userID);
        }
        else
            return new Response(true, "The product does not exist in the system");

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

    public String editProductDetails(Integer productId, String productName, Double price, String category){
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

    public LinkedList<Integer> getDummySearchByName( LinkedList<Integer> FinalID,String name){
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
        for (Integer i : products) {
            Product p=this.products.get(i);
            DummyProduct D=new DummyProduct(this.storeID,storeName,p.getProductID(),p.getProductName(),p.getPrice(),p.getCategory(),p.getQuantity());//productComments.get(i).getRate());
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

    public void lockProduct(Integer productID){
        this.products.get(productID).lockProduct();
    }

    public Lock getProductLock(int productID) {
        return this.products.get(productID).getLock();
    }

    public void unlockProduct(Collection<Integer> productID){
        this.products.get(productID).unlockProduct();
    }

    public boolean productIsLock(Integer productID) {
        return  this.products.get(productID).productIsLock();
    }

    public LinkedList<Product> getProducts() {
        LinkedList<Product> products=new LinkedList<Product>();
        Set<Integer> productSet = this.products.keySet();
        for (Integer id : productSet){
             products.add(this.products.get(id));
        }
        return products;
    }

    public int getQuantity(Integer productID) {
      if(this.products.containsKey(productID)){
          return this.products.get(productID).getQuantity();
      }
      return 0;
    }

    public List<String> getCommentsForProduct(int productID) {
        if(this.products.get(productID)!=null) {
            return this.products.get(productID).getCommentsForProduct(productID);
        }
        return null;
    }


}