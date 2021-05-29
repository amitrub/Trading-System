package TradingSystem.Server.DomainLayer.ShoppingComponent;



import TradingSystem.Server.DataLayer.Services.Data_Controller;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public class ShoppingBag {

    private static TradingSystemImplRubin tradingSystem;

    public static void setTradingSystem(TradingSystemImplRubin tradingSystem) {
        ShoppingBag.tradingSystem = tradingSystem;
    }

    @Autowired
    public static Data_Controller data_controller;

    public static void setData_controller(Data_Controller data_controller) {
        ShoppingBag.data_controller = data_controller;
    }


    private Integer userID;
    private Integer storeID;

    //productID_quantity
    private ConcurrentHashMap<Integer,Integer> products;

    private ConcurrentHashMap<Integer,Double> priceOfSpacialProducts;

    private ConcurrentHashMap<Integer,Integer> quantityOfSpacialProducts;

    private Double finalPrice;


    public ShoppingBag(Integer userID, Integer storeID) {

        this.userID = userID;
        this.storeID = storeID;
        this.products=new ConcurrentHashMap<Integer,Integer>();
        this.priceOfSpacialProducts=new ConcurrentHashMap<Integer,Double>();
        this.quantityOfSpacialProducts=new ConcurrentHashMap<Integer,Integer>();
        this.finalPrice=0.0;
    }

    public ShoppingBag(ShoppingBag shoppingBagToCopy) {
        this.userID = shoppingBagToCopy.userID;
        this.storeID = shoppingBagToCopy.storeID;
        this.products=new ConcurrentHashMap<Integer,Integer>();
        this.finalPrice= shoppingBagToCopy.finalPrice;

        for (int productID : shoppingBagToCopy.products.keySet()) {
            Integer quantity = shoppingBagToCopy.products.get(productID);
            this.products.put(productID,quantity);
        }

        for (int productID : shoppingBagToCopy.priceOfSpacialProducts.keySet()) {
            Double price = shoppingBagToCopy.priceOfSpacialProducts.get(productID);
            this.priceOfSpacialProducts.put(productID,price);
        }

        for (int productID : shoppingBagToCopy.quantityOfSpacialProducts.keySet()) {
            Integer quantity = shoppingBagToCopy.quantityOfSpacialProducts.get(productID);
            this.quantityOfSpacialProducts.put(productID,quantity);
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
        if (this.quantityOfSpacialProducts.containsKey(productID)){
            return this.quantityOfSpacialProducts.get(productID);
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
       return this.products;
    }

    public ConcurrentHashMap<Integer, Integer> getAllProducts() {
        ConcurrentHashMap<Integer, Integer> list=new ConcurrentHashMap<>();
        for(int productID : this.products.keySet()){
            list.put(productID,this.products.get(productID));
        }
        for (int productID : quantityOfSpacialProducts.keySet()) {
            list.put(productID,this.quantityOfSpacialProducts.get(productID));
        }

        return list;
    }

    public void setProducts(ConcurrentHashMap<Integer, Integer> products) {
        this.products = products;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        data_controller.setBagFinalPrice(userID, storeID, finalPrice);
        this.finalPrice = finalPrice;
    }

    public List<Integer> getProductsList(){
        List<Integer> products=new ArrayList<>();
        for(int productID : this.products.keySet()){
            products.add(productID);
        }
        //todo check!
        for (int productID : quantityOfSpacialProducts.keySet()) {
            products.add(productID);
        }
        return products;
    }

    public List<Lock> getLockList(){
        List<Lock> output = new ArrayList<>();
        Set<Integer> productsSet = this.getAllProducts().keySet();
        for (Integer productID : productsSet){
            Lock lock = tradingSystem.getProductLock(this.storeID, productID);
            output.add(lock);
        }
        return output;
    }

    public Response checkInventory(){
        Set<Integer> productsSet = this.getProducts().keySet();
        for (Integer productID : productsSet){
            int productQuantity = this.getProducts().get(productID);
            if (!tradingSystem.validation.checkProductsExistInTheStore(storeID, productID, productQuantity)) {
                String storeName = tradingSystem.getStoreName(storeID);
                String productName = tradingSystem.getProductName(storeID, productID);
// =======
//         ConcurrentHashMap<Integer, Integer> productsSet = this.getAllProducts();
//         for (Integer productID : productsSet.keySet()){
//             int productQuantity = productsSet.get(productID);
//             if (!tradingSystemImpl.validation.checkProductsExistInTheStore(storeID, productID, productQuantity)) {
//                 String storeName = tradingSystemImpl.getStoreName(storeID);
//                 String productName = tradingSystemImpl.getProductName(storeID, productID);
// >>>>>>> Version-3
                String err = "Purchase: " + productName + " in The store: " + storeName + " is not exist in the stock";
                return new Response(true, err);
            }
        }
        return new Response();
    }

    public ShoppingHistory createShoppingHistory(){
        List productsToHistory = new ArrayList();
// <<<<<<< DB-Rubin-to-merge
        Set<Integer> productIDs = this.getProducts().keySet();
        for (Integer productID: productIDs){
            Integer quantity = this.getProducts().get(productID);
            Product p = tradingSystem.getProduct(storeID,productID);
            Product newProduct = new Product(p);
            newProduct.setQuantity(quantity);
            productsToHistory.add(newProduct);
        }
// =======
//         for (Integer productID: products.keySet()){
//             Integer quantity = products.get(productID);
//             Product p = tradingSystemImpl.getProduct(storeID,productID);
//             Product newProduct = new Product(p);
//             newProduct.setQuantity(quantity);
//             productsToHistory.add(newProduct);
//         }
          //TODO: checkkkkk
        for (Integer productID: this.quantityOfSpacialProducts.keySet()){
            Integer quantity = this.quantityOfSpacialProducts.get(productID);
            Product p = tradingSystem.getProduct(storeID,productID);
            Product newProduct = new Product(p);
            newProduct.setQuantity(quantity);
            productsToHistory.add(newProduct);
        }
        return new ShoppingHistory(this, productsToHistory);
    }

    public void editProductQuantity(int productID, int quantity) {
        this.products.remove(productID);
        this.products.put(productID,quantity);
    }
  
    public void RemoveProduct(int productID) {
        if(this.products.containsKey(productID)) {
            this.products.remove(productID);
        }
        else if(this.quantityOfSpacialProducts.containsKey(productID)&&this.priceOfSpacialProducts.containsKey(productID)){
            this.priceOfSpacialProducts.remove(productID);
            this.quantityOfSpacialProducts.remove(productID);
        }
    }

    public String getStoreName() {
        return tradingSystem.stores.get(storeID).getName();
    }

    public void addSPacialProduct(int productID, Integer quantity, double productPrice) {
        this.quantityOfSpacialProducts.put(productID,quantity);
        this.priceOfSpacialProducts.put(productID,productPrice);
    }

    public Double calculateSpacialPrices() {
      Double price=0.0;
        for (Integer productID: this.quantityOfSpacialProducts.keySet()){
            if(this.priceOfSpacialProducts.containsKey(productID)){
                Double cal=priceOfSpacialProducts.get(productID)*quantityOfSpacialProducts.get(productID);
                price=price+cal;
            }
        }
        return price;
    }

    public ConcurrentHashMap<Integer, Double> getPriceOfSpacialProducts() {
        return priceOfSpacialProducts;
    }

    public ConcurrentHashMap<Integer, Integer> getQuantityOfSpacialProducts() {
        return quantityOfSpacialProducts;
    }
}
