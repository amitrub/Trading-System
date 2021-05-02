package TradingSystem.Server.DomainLayer.ShoppingComponent;



import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public class ShoppingBag {

    private final TradingSystemImpl tradingSystemImpl = TradingSystemImpl.getInstance();

    private Integer userID;
    private Integer storeID;

    //productID_quantity
    private ConcurrentHashMap<Integer,Integer> products;

    private Double finalPrice;


    public ShoppingBag(Integer userID, Integer storeID) {

        this.userID = userID;
        this.storeID = storeID;
        this.products=new ConcurrentHashMap<Integer,Integer>();
        this.finalPrice=0.0;
    }

    public ShoppingBag(ShoppingBag shoppingBagToCopy) {
        this.userID = shoppingBagToCopy.userID;
        this.storeID = shoppingBagToCopy.storeID;
        this.products=new ConcurrentHashMap<Integer,Integer>();
        this.finalPrice= shoppingBagToCopy.finalPrice;
        Set<Integer> productsSet = shoppingBagToCopy.products.keySet();
        for (int productID : productsSet) {
            Integer quantity = shoppingBagToCopy.products.get(productID);
            this.products.put(productID,quantity);
        }
    }

    @Override
    public String toString() {
        return "ShoppingBag{" +
                "products=" + products +
                '}';
    }


    public void mergeToMyBag(ShoppingBag shoppingBagToMerge){
        Set<Integer> productsToMerge = shoppingBagToMerge.products.keySet();
        for (int productID : productsToMerge){
            Integer newProductQuantity = shoppingBagToMerge.products.get(productID);
            if(!this.products.containsKey(productID)){
                this.products.put(productID, newProductQuantity);
            }
            else {
                Integer oldQuantity = this.products.get(productID);
                this.products.put(productID,oldQuantity+newProductQuantity);
            }
        }

    }

    public void addProduct(Integer productID, Integer quantity) {
        if (this.products.containsKey(productID)){
            this.products.put(productID,this.products.get(productID) + quantity);
        }
        else
            this.products.put(productID,quantity);
    }
    public int getProductQuantity(Integer productID) {
        if (this.products.containsKey(productID)){
            return this.products.get(productID);
        }
        else
            return 0;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getStoreID() {
        return storeID;
    }

    public void setStoreID(Integer storeID) {
        this.storeID = storeID;
    }

    public ConcurrentHashMap<Integer, Integer> getProducts() {
        return products;
    }

    public void setProducts(ConcurrentHashMap<Integer, Integer> products) {
        this.products = products;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

    public List<Integer> getProductsList(){
        List<Integer> products=new ArrayList<>();
        for(Map.Entry<Integer,Integer> map: this.products.entrySet()){
            products.add(map.getKey());
        }
        return products;
    }

    public List<Lock> getLockList(){
        List<Lock> output = new ArrayList<>();
        Set<Integer> productsSet = this.getProducts().keySet();
        for (Integer productID : productsSet){
            Lock lock = tradingSystemImpl.getProductLock(this.storeID, productID);
            output.add(lock);
        }
        return output;
    }

    public Response checkInventory(){
        Set<Integer> productsSet = this.getProducts().keySet();
        for (Integer productID : productsSet){
            int productQuantity = this.getProducts().get(productID);
            if (!tradingSystemImpl.validation.checkProductsExistInTheStore(storeID, productID, productQuantity)) {
                String storeName = tradingSystemImpl.getStoreName(storeID);
                String productName = tradingSystemImpl.getProductName(storeID, productID);
                String err = "Purchase: " + productName + " in The store" + storeName + " is not exist in the stock";
                return new Response(true, err);
            }
        }
        return new Response();
    }

    public ShoppingHistory createShoppingHistory(){
        ConcurrentHashMap<Product, Integer> productsToHistory = new ConcurrentHashMap<>();
        Set<Integer> productQuantitySet = this.getProducts().keySet();
        for (Integer productID: productQuantitySet){
            Integer quantity = this.getProducts().get(productID);
            Product p = tradingSystemImpl.getProduct(storeID,productID);
            Product newProduct = new Product(p);
            productsToHistory.put(newProduct, quantity);
        }
        return new ShoppingHistory(this,productsToHistory);
    }

    public void editProductQuantity(int productID, int quantity) {
        this.products.remove(productID);
        this.products.put(productID,quantity);
    }
  
    public void RemoveProduct(int productID) {
        this.products.remove(productID);
    }
}
