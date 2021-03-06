package TradingSystem.Server.DomainLayer.ShoppingComponent;



import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagCart;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagProduct;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagSpacialProduct;
import TradingSystem.Server.DataLayer.Services.Data_Controller;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public class ShoppingBag {

    private static TradingSystemImpl tradingSystem;


    public static void setTradingSystem(TradingSystemImpl tradingSystem) {
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
    private ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Integer,Integer> priceOfSpacialProducts = new ConcurrentHashMap<>();

    private ConcurrentHashMap<Integer,Integer> quantityOfSpacialProducts = new ConcurrentHashMap<>();

    private Double finalPrice;
    private Integer SpecialPrice;

    public ShoppingBag(Integer userID, Integer storeID) {

        this.userID = userID;
        this.storeID = storeID;
        this.finalPrice=0.0;
        this.SpecialPrice=0;
    }

    public ShoppingBag(ShoppingBag shoppingBagToCopy) {
        this.userID = shoppingBagToCopy.userID;
        this.storeID = shoppingBagToCopy.storeID;
        this.finalPrice= shoppingBagToCopy.finalPrice;
        this.SpecialPrice=shoppingBagToCopy.SpecialPrice;

        for (int productID : shoppingBagToCopy.products.keySet()) {
            Integer quantity = shoppingBagToCopy.products.get(productID);
            this.products.put(productID,quantity);
        }

        for (int productID : shoppingBagToCopy.priceOfSpacialProducts.keySet()) {
            Integer price = shoppingBagToCopy.priceOfSpacialProducts.get(productID);
            this.priceOfSpacialProducts.put(productID,price);
        }

        for (int productID : shoppingBagToCopy.quantityOfSpacialProducts.keySet()) {
            Integer quantity = shoppingBagToCopy.quantityOfSpacialProducts.get(productID);
            this.quantityOfSpacialProducts.put(productID,quantity);
        }
    }

    public ShoppingBag(DataShoppingBagCart shoppingBagCart){
        this.userID=shoppingBagCart.getSubscriber().getUserID();
        this.storeID=shoppingBagCart.getStore().getStoreID();
        List<DataShoppingBagProduct> list=shoppingBagCart.getProducts();
        for(DataShoppingBagProduct product:list){
            this.products.putIfAbsent(product.getProduct().getProductID(),product.getQuantity());
        }
        this.finalPrice= shoppingBagCart.getFinalPrice();
        List<DataShoppingBagSpacialProduct> spacialProductList = shoppingBagCart.getSpacialProducts();
        for(DataShoppingBagSpacialProduct product:spacialProductList){
            this.priceOfSpacialProducts.putIfAbsent(product.getProduct().getProductID(), product.getPrice());
            this.quantityOfSpacialProducts.putIfAbsent(product.getProduct().getProductID(), product.getQuantity());
        }
        this.SpecialPrice= shoppingBagCart.getFinalPriceSpacial();
        //todo add
       // this.SpecialPrice=shoppingBagCart.
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
       return this.products;
    }

    public ConcurrentHashMap<Integer, Integer> getAllProducts() {
        ConcurrentHashMap<Integer, Integer> list=new ConcurrentHashMap<>();
        for(int productID : this.products.keySet()){
            list.put(productID,this.products.get(productID));
        }
        for (int p : quantityOfSpacialProducts.keySet()) {
            list.put(p,this.quantityOfSpacialProducts.get(p));
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
        if(userID>=1){
            Response response= data_controller.setBagFinalPrice(userID, storeID, finalPrice);
        }
        this.finalPrice = finalPrice;
    }

    public List<Integer> getProductsList(){
        List<Integer> products=new ArrayList<>();
        for(int productID : this.products.keySet()){
            products.add(productID);
        }
        return products;
    }

    public List<Lock> getLockList(){
        List<Lock> output = new ArrayList<>();
        Set<Integer> productsSet = this.getProducts().keySet();
        for (Integer productID : productsSet){
            Lock lock = tradingSystem.getProductLock(this.storeID, productID);
            output.add(lock);
        }
        Set<Integer> specialProductsSet = this.quantityOfSpacialProducts.keySet();
        for (Integer productID : specialProductsSet){
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
                String err = "Purchase: " + productName + " in The store: " + storeName + " is not exist in the stock";
                return new Response(true, err);
            }
        }
        for (Integer productID : this.quantityOfSpacialProducts.keySet()){
            int productQuantity = this.quantityOfSpacialProducts.get(productID);
            if (!tradingSystem.validation.checkProductsExistInTheStore(storeID, productID, productQuantity)) {
                String storeName = tradingSystem.getStoreName(storeID);
                String productName = tradingSystem.getProductName(storeID, productID);
                String err = "Purchase: " + productName + " in The store: " + storeName + " is not exist in the stock";
                return new Response(true, err);
            }
        }
        return new Response();
    }

    public ShoppingHistory createShoppingHistory(){
        List productsToHistory = new ArrayList();
        Set<Integer> productIDs = this.getProducts().keySet();
        for (Integer productID: productIDs){
            Integer quantity = this.getProducts().get(productID);
            Product p = tradingSystem.getProduct(storeID,productID);
            Product newProduct = new Product(p, quantity);
            productsToHistory.add(newProduct);
        }
          //TODO: checkkkkk
        for (Integer productID: this.quantityOfSpacialProducts.keySet()){
            Integer quantity = this.quantityOfSpacialProducts.get(productID);
            int price = this.priceOfSpacialProducts.get(productID);
            Product p = tradingSystem.getProduct(storeID,productID);
            Product newProduct = new Product(p, quantity);
            newProduct.setPrice((double) price);
            productsToHistory.add(newProduct);
        }
        return new ShoppingHistory(this, productsToHistory);
    }

    public void editProductQuantity(int productID, int quantity) {
        if (userID >= 1) {
            Response response = data_controller.setBagProductQuantity(userID, storeID, productID, quantity);
            if (!response.getIsErr()) {
                this.products.remove(productID);
                this.products.put(productID, quantity);
            }
        }
    }
  
    public void RemoveProduct(int productID) {
        if(this.products.containsKey(productID)) {
            if(userID>=1){
                Response response= data_controller.RemoveBagProduct(userID, storeID, productID);
                if(!response.getIsErr()){
                    this.products.remove(productID);
                }
            }
        }
    }

    public String getStoreName() {
        return tradingSystem.stores.get(storeID).getName();
    }

    public void addSPacialProduct(int productID, Integer quantity, int productPrice) {
        data_controller.addSpacialProductToBag(userID, storeID, productID, quantity, productPrice);
        this.quantityOfSpacialProducts.put(productID,quantity);
        this.priceOfSpacialProducts.put(productID,productPrice);
    }

    public Integer calculateSpacialPrices() {
      Integer price=0;
        for (Integer productID: this.quantityOfSpacialProducts.keySet()){
            if(this.priceOfSpacialProducts.containsKey(productID)){
                Integer cal=priceOfSpacialProducts.get(productID)*quantityOfSpacialProducts.get(productID);
                price=price+cal;
            }
        }
        return price;
    }

    public ConcurrentHashMap<Integer, Integer> getPriceOfSpacialProducts() {
        return priceOfSpacialProducts;
    }

    public ConcurrentHashMap<Integer, Integer> getQuantityOfSpacialProducts() {
        return quantityOfSpacialProducts;
    }

    public void setFinalSpecialPrice(int spacialPrice) {
        data_controller.setBagSpacialFinalPrice(userID, storeID, spacialPrice);
        this.SpecialPrice=spacialPrice;
    }

    public Integer getSpecialPrice() {
        return SpecialPrice;
    }

    public void removeSpecialProductFromCart(int productID) {
        data_controller.RemoveBagSpacialProduct(userID, storeID, productID);
        if(this.quantityOfSpacialProducts.containsKey(productID)){
            this.quantityOfSpacialProducts.remove(productID);
        }
        if(this.priceOfSpacialProducts.containsKey(productID)){
            this.priceOfSpacialProducts.remove(productID);
        }
        //todo add?
        /*
         if(userID>=1){
                data_controller.RemoveBagProduct(userID, storeID, productID);
            }
         */
    }


    public List<Integer> getSpecialProductProductsList() {
        List<Integer> products=new ArrayList<>();
        for(int productID : this.quantityOfSpacialProducts.keySet()){
            products.add(productID);
        }
        return products;
    }
}
