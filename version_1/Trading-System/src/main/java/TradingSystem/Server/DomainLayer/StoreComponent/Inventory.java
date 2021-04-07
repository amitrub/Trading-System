package TradingSystem.Server.DomainLayer.StoreComponent;

import TradingSystem.Server.ServiceLayer.DummyObject.DummySearch;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Inventory {

    private Integer storeID;
    private String storeName;
    private static int nextProductID=0;
    //productID_product
    private ConcurrentHashMap<Integer, Product> products;
    //poductID_quantity
    private ConcurrentHashMap<Integer, Integer> productQuantity;
    //productID_comments
    private ConcurrentHashMap<Integer, Comment> productComments;


    public Inventory(Integer storeID, String storeName)
    {
        this.storeID=storeID;
        this.storeName=storeName;
        this.products= new ConcurrentHashMap<Integer, Product>();
        this.productQuantity=new ConcurrentHashMap<Integer, Integer>();
        this.productComments = new ConcurrentHashMap<Integer, Comment>();
    }

    private static synchronized int getNextProductID()
    {
        nextProductID++;
        return nextProductID;
    }

    public void addProduct(String productName, String category, Double price)
    {
        Integer productID=getNextProductID();
        Product p=new Product(productID, productName, category, price);
        this.products.put(productID,p);
        this.productQuantity.put(productID,0);
    }

    public String deleteProduct(Integer productID,Integer storeID)
    {
            this.productQuantity.remove(productID);
            this.products.remove(productID);
            this.productComments.remove(productID);
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
    this.productComments.put(productId,new Comment(userID,comment));
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

    public LinkedList<Product> getAllTheProducs(LinkedList<Integer> productsID)
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
    public String reduceProduct(Integer productId, Integer quantityToReduce)
    {
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
                Double R = this.productComments.get(p.getProductID()).getRate();
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
                Double R = this.productComments.get(p.getProductID()).getRate();
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

    public LinkedList<DummySearch> getDummySearchForList(LinkedList<Integer> products)
    {
        LinkedList<DummySearch> DummyProducts=new LinkedList<DummySearch>();
        for (Integer i:products
             ) {
            Product p=this.products.get(i);
            DummySearch D=new DummySearch(this.storeID,storeName,p.getProductID(),p.getProductName(),p.getPrice(),p.getCategory());//productComments.get(i).getRate());
            DummyProducts.add(D);
        }
        return DummyProducts;
    }

    public LinkedList<DummySearch> getAllProducts(){
        LinkedList<DummySearch> products= new LinkedList<>();
        for(Map.Entry<Integer,Product> productEntry:this.products.entrySet()){
            products.add(new DummySearch(this.storeID,this.storeName,productEntry.getKey(),productEntry.getValue().getProductName(),productEntry.getValue().getPrice(),productEntry.getValue().getCategory()));
        }
        return products;
    }

    public Product getProductById(int id){
        return products.get(id);
    }
}