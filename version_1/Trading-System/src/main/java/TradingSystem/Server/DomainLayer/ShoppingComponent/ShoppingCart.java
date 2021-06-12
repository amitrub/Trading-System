package TradingSystem.Server.DomainLayer.ShoppingComponent;

import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagCart;
import TradingSystem.Server.DataLayer.Services.Data_Controller;
import TradingSystem.Server.DomainLayer.ExternalServices.*;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public class ShoppingCart {

    private static TradingSystemImpl tradingSystem;
    public static void setTradingSystem(TradingSystemImpl tradingSystem) {
        ShoppingCart.tradingSystem = tradingSystem;
    }

    @Autowired
    public static Data_Controller data_controller;
    public static void setData_controller(Data_Controller data_controller) {

        ShoppingCart.data_controller = data_controller;
    }

    private final Integer userID;
    //StoreID_ShoppingBag
    private ConcurrentHashMap<Integer, ShoppingBag> shoppingBags = new ConcurrentHashMap<>();
    //StoreID
    private Set<Integer> storesReducedProductsVain=new HashSet<>();

    public ShoppingCart(Integer userID){
        this.userID = userID;
    }

    public ShoppingCart(ShoppingCart shoppingCartToCopy){
        this.userID = shoppingCartToCopy.userID;
        Set<Integer> shoppingBagsSet = shoppingCartToCopy.shoppingBags.keySet();
        for (Integer storeID : shoppingBagsSet) {
            ShoppingBag shoppingBag = this.shoppingBags.get(storeID);
            this.shoppingBags.put(storeID, new ShoppingBag(shoppingBag));
        }
    }

    public ShoppingCart(Integer userID, ConcurrentHashMap<Integer, ShoppingBag> shoppingBags) {
        this.userID = userID;
        this.shoppingBags = shoppingBags;
    }

    public ShoppingCart(Integer userID, List<DataShoppingBagCart> shoppingBagsCart){
        this.userID = userID;
        for (DataShoppingBagCart dataShoppingBag: shoppingBagsCart){
            ShoppingBag bag = new ShoppingBag(dataShoppingBag);
            this.shoppingBags.put(bag.getStoreID(), bag);
        }
    }

    public Integer getUserID() {
        return userID;
    }

    public ConcurrentHashMap<Integer, ShoppingBag> getShoppingBags() {
        return shoppingBags;
    }


    @Override
    public String toString() {
        return "ShoppingCart{" +
                "shoppingBags=" + shoppingBags +
                '}';
    }

    public void mergeToMyCart(ShoppingCart shoppingCartToMerge) {
        Set<Integer> keySetToMerge = shoppingCartToMerge.shoppingBags.keySet();
        for (int storeID : keySetToMerge) {
            ShoppingBag newShoppingBag = shoppingCartToMerge.shoppingBags.get(storeID);
            if (!this.shoppingBags.containsKey(storeID)) {
                this.shoppingBags.put(storeID, newShoppingBag);
            } else {
                this.shoppingBags.get(storeID).mergeToMyBag(newShoppingBag);
            }
        }
    }

    public void AddShoppingBagIfNotExist(ShoppingBag shoppingBag){
        shoppingBags.putIfAbsent(shoppingBag.getStoreID(), shoppingBag);
    }

    /**
     /**
     * @requirement 2.7
     *
     * @param storeID
     * @param productID
     * @param quantity
     *
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */    
    public Response addProductToBag(Integer storeID, Integer productID, Integer quantity, boolean isGuset){
        if(!this.shoppingBags.containsKey(storeID)){
            if (!tradingSystem.validation.checkProductsExistInTheStore(storeID, productID, quantity)) {
                return new Response(true, "AddProductToCart: The product " + productID + " doesn't exist in the store");
            }
            this.shoppingBags.put(storeID, new ShoppingBag(this.userID,storeID));
        }
        else {
            if(this.shoppingBags.get(storeID).getSpecialProductProductsList().contains(productID)){
                return new Response(true, "AddProductToCart: The product " + productID + " in your special products so you cant add it to cart");
            }
            int oldQuantity = this.shoppingBags.get(storeID).getProductQuantity(productID);
            if (!tradingSystem.validation.checkProductsExistInTheStore(storeID, productID, quantity + oldQuantity)) {
                  return new Response(true, "AddProductToCart: The quantity from the product is not in stock");
            }
        }

        //todo - change!
        if(!isGuset){
            //Adds to the db
            data_controller.addProductToBag(getUserID(), storeID, productID, quantity);
        }

        this.shoppingBags.get(storeID).addProduct(productID, quantity);
        ConcurrentHashMap<Integer,Integer> products=this.shoppingBags.get(storeID).getProducts();
        if (!tradingSystem.validation.checkBuyingPolicy(this.userID, storeID,products)) {
            this.shoppingBags.get(storeID).RemoveProduct(productID);
            return new Response(true, "Adding the product "+productID+" is against the store policy");
        }
        Double priceForBag = tradingSystem.calculateBugPrice(productID, storeID, products);
        shoppingBags.get(storeID).setFinalPrice(priceForBag);
        Response res =new Response("The product added successfully");
        return res;
    }


    public Response AddSpacialProductForCart(int productID, int storeID, int productPrice, Integer quantity) {
        if(!this.shoppingBags.containsKey(storeID)){
            if (!tradingSystem.validation.checkProductsExistInTheStore(storeID, productID, quantity)) {
                return new Response(true, " The product " + productID + " doesn't exist in the store");
            }
            this.shoppingBags.put(storeID, new ShoppingBag(this.userID,storeID));
        }
        else {
            if (!tradingSystem.validation.checkProductsExistInTheStore(storeID, productID, quantity)) {
                return new Response(true, " The quantity from the product is not in stock");
            }
        }
        ConcurrentHashMap<Integer,Integer> tmpProducts=this.shoppingBags.get(storeID).getProducts();
        if(tmpProducts.get(productID)!=null){
            return new Response(true, "The product "+productID+" is exist in the bag already");
        }
        tmpProducts.put(productID,quantity);
        if (!tradingSystem.validation.checkBuyingPolicy(this.userID, storeID,tmpProducts)) {
            return new Response(true, "Adding the product "+productID+" is against the store policy");
        }
        tmpProducts.remove(productID);
        this.shoppingBags.get(storeID).addSPacialProduct(productID, quantity,productPrice);
        //Double priceForBag = tradingSystem.calculateBugPrice(productID, storeID, this.shoppingBags.get(storeID).getProducts());
        Integer spacialPrice=  this.shoppingBags.get(storeID).calculateSpacialPrices();
        shoppingBags.get(storeID).setFinalSpecialPrice(spacialPrice);
        Response res =new Response("The product added successfully");
        return res;
    }



    private synchronized Double calculatePrice(){
        double price = 0.0;
        Set<Integer> shoppingBagsSet = this.shoppingBags.keySet();
        for (Integer key : shoppingBagsSet) {
            ShoppingBag p = this.shoppingBags.get(key);
            price = price + p.getFinalPrice();

        }
        return price;
    }

    private synchronized Integer calculateSpecialPrice(){
        int price = 0;
        Set<Integer> shoppingBagsSet = this.shoppingBags.keySet();
        for (Integer key : shoppingBagsSet) {
            ShoppingBag p = this.shoppingBags.get(key);
            price = price + p.getSpecialPrice();

        }
        return price;
    }

    public ConcurrentHashMap<Integer, ShoppingBag> GetInfo() {
        return this.shoppingBags;
    }

    public Response Purchase(boolean isGuest, String name, String credit_number, String month, String year, String cvv, String ID, String address, String city, String country, String zip){
        if (shoppingBags.size()==0){
            return new Response(true, "Purchase: There is no products in the shopping cart");
        }
        List<Lock> lockList = this.getLockList();
        Response productInStock = this.checkInventoryAndLockProduct(lockList);
        if (productInStock.getIsErr()){
            return productInStock;
        }
        Set<Integer> shoppingBagsSet = this.shoppingBags.keySet();
        for (Integer storeID : shoppingBagsSet) {
            ConcurrentHashMap<Integer, Integer> products = this.shoppingBags.get(storeID).getProducts();
            if (!tradingSystem.validation.checkBuyingPolicy(this.userID, storeID, products)) {
                this.releaseLocks(lockList);
                return new Response(true, "Purchase in the store "+ storeID+" is against the store policy");
            }
        }
        PaymentInfo paymentInfo = new PaymentInfo(credit_number, month, year, name, cvv, ID);
        AddressInfo addressInfo = new AddressInfo(name, country, city, address, zip);
        Response supplyResponse = TradingSystemImpl.getSupplySystem().purchase(paymentInfo, addressInfo);
        if(supplyResponse.getIsErr()){
            this.releaseLocks(lockList);
            return supplyResponse;
        }
        Response paymentResponse = TradingSystemImpl.getPaymentSystem().purchase(paymentInfo, addressInfo);
        if(paymentResponse.getIsErr()){
            TradingSystemImpl.getSupplySystem().Cancel(supplyResponse.getMessage());
            this.releaseLocks(lockList);
            return paymentResponse;
        }

        Response res = Buy(isGuest);
        if(res.getIsErr()) {
            this.releaseLocks(lockList);
            return res;
        }
        //TODO add charge to a payment
        //TODO Add payment to each store for the products

        addShoppingHistory(isGuest);
        this.storesReducedProductsVain=new HashSet<>();
        this.shoppingBags = new ConcurrentHashMap<>();
        this.releaseLocks(lockList);
        return new Response("The purchase was made successfully");
    }

    private List<Lock> getLockList() {
        List<Lock> output = new ArrayList<>();
        Set<Integer> shoppingBagsSet = this.shoppingBags.keySet();
        for (Integer storeID : shoppingBagsSet) {
            ShoppingBag shoppingBag = this.shoppingBags.get(storeID);
            output.addAll(shoppingBag.getLockList());
        }
        return output;
    }

    private boolean tryLockList(List<Lock> lockList) {
        List<Lock> succeededToLock = new ArrayList<>();
        for (Lock lock : lockList) {
            if (lock.tryLock()) {
                succeededToLock.add(lock);
            } else {
                for (Lock lockedLock : succeededToLock) {
                    lockedLock.unlock();
                }
                return false;
            }
        }
        return true;
    }

    private void releaseLocks(List<Lock> lockList) {
        for (Lock lock : lockList) {
            lock.unlock();
        }
    }


    private Response checkInventoryAndLockProduct(List<Lock> lockList){
        boolean succeededToLock = false;
        Set<Integer> shoppingBagsSet = this.shoppingBags.keySet();
        while (!succeededToLock) {
            synchronized (this) {
                for (Integer storeID : shoppingBagsSet) {
                    ShoppingBag shoppingBag = this.shoppingBags.get(storeID);
                    Response res = shoppingBag.checkInventory();
                    if (res.getIsErr()){
                        return res;
                    }
                }
                succeededToLock = this.tryLockList(lockList);
            }
        }
        return new Response();
    }

    private Response Buy(boolean isGuest){
        Response res;
        Set<Integer> shoppingBagsSet = this.shoppingBags.keySet();
        for (Integer storeID : shoppingBagsSet) {
            ShoppingBag SB = this.shoppingBags.get(storeID);
            res = tradingSystem.reduceProducts(SB.getAllProducts(), storeID);
            if (res.getIsErr()) {
                this.cancelReduceProducts();
                this.storesReducedProductsVain=new HashSet<>();
                return res;
            }
            this.storesReducedProductsVain.add(storeID);
        }

        if (!isGuest){
            for (Integer storeID : shoppingBagsSet) {
                Response response= data_controller.deleteSubscriberBag(userID, storeID);
                if(response.getIsErr()){
                    return response;
                }
            }
        }
        res=new Response(false, "Purchase: The reduction was made successfully ");
        return res;
    }

    private void cancelReduceProducts() {
        for (Integer storeID : this.storesReducedProductsVain) {
            tradingSystem.cancelReduceProducts(storeID,this.shoppingBags.get(storeID).getAllProducts());
        }
    }

    private void addShoppingHistory(boolean isGuest) {
        Set<Integer> shoppingBagsSet = this.shoppingBags.keySet();
        for (Integer storeID : shoppingBagsSet) {
            ShoppingBag shoppingBag = this.shoppingBags.get(storeID);
            ShoppingHistory shoppingHistory = shoppingBag.createShoppingHistory();
            tradingSystem.addHistoryToStoreAndUser(shoppingHistory, isGuest);
        }
    }

    private void PayToTheSellers() {
        Set<Integer> shoppingBagsSet = this.shoppingBags.keySet();
        for (Integer storeID : shoppingBagsSet) {
            ShoppingBag SB = this.shoppingBags.get(storeID);
            tradingSystem.PayToTheSellers(SB.getFinalPrice(), storeID);
        }
    }


    /**
     * @requirement 2.8
     *
     * @return List<DummyProduct>
     */
    public List<DummyProduct> ShowShoppingCart(){
        List<DummyProduct> outputList = new ArrayList<>();
        Set<Integer> shoppingBagsSet = this.shoppingBags.keySet();
        for (Integer storeID : shoppingBagsSet) {
            ShoppingBag SB = this.shoppingBags.get(storeID);
            Set<Integer> productSet = SB.getProducts().keySet();
            for (Integer productID : productSet) {
                int quantity = SB.getProducts().get(productID);
                Product p = tradingSystem.getProduct(storeID, productID);
                System.out.println("---------------Product---------------");
                System.out.println(p);
                System.out.println("---------------Product---------------");
                DummyProduct d = new DummyProduct(storeID, tradingSystem.getStoreName(storeID), productID, p.getProductName(), p.getPrice(), p.getCategory(), quantity);
                outputList.add(d);
            }
        }
        return outputList;
    }

    public List<DummyProduct> ShowSpecialProductInShoppingCart(){
        List<DummyProduct> outputList = new ArrayList<>();
        Set<Integer> shoppingBagsSet = this.shoppingBags.keySet();
        for (Integer storeID : shoppingBagsSet) {
            ShoppingBag SB = this.shoppingBags.get(storeID);
            Set<Integer> productSet = SB.getQuantityOfSpacialProducts().keySet();
            for (Integer productID : productSet) {
                if (SB.getPriceOfSpacialProducts().containsKey(productID)) {
                    int quantity = SB.getQuantityOfSpacialProducts().get(productID);
                    Integer price=SB.getPriceOfSpacialProducts().get(productID);
                    Product p = tradingSystem.getProduct(storeID, productID);
                    DummyProduct d = new DummyProduct(storeID, tradingSystem.getStoreName(storeID), productID, p.getProductName(), price, p.getCategory(), quantity);
                    outputList.add(d);
                }
            }
        }
        return outputList;
    }

    /*for (Integer productID : SB.getQuantityOfSpacialProducts().keySet()) {
                if (SB.getPriceOfSpacialProducts().containsKey(productID)) {
                    int quantity = SB.getQuantityOfSpacialProducts().get(productID);
                    Double price=SB.getPriceOfSpacialProducts().get(productID);
                    Product p = tradingSystem.getProduct(storeID, productID);
                    DummyProduct d = new DummyProduct(storeID, tradingSystem.getStoreName(storeID), productID, p.getProductName(), price, p.getCategory(), quantity);
                    outputList.add(d);
                }
     */

    
      /**
     * @requirement 2.8
     *
     * @param storeID
     * @param productID
     * @param quantity
     *
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  }
     *
     */
    public Response editProductQuantityFromCart(int storeID, int productID, int quantity) {
        boolean isGuest = userID<1;
        if(this.shoppingBags.isEmpty()){
            return new Response(true,"EditCart: The shoppingCart empty, cannot be edited");
        }
        if(this.shoppingBags.get(storeID)==null||
        !this.shoppingBags.get(storeID).getProductsList().contains(productID)){
            return new Response(true,"EditCart: The product isn't in the shoppingCart, so it cannot be edited");
        }
        if(!tradingSystem.validation.checkProductsExistInTheStore(storeID,productID,quantity)){
            return new Response(true,"EditCart: The product isn't in the stock, so it cannot be edited");
        }
        if(shoppingBags.get(storeID).getQuantityOfSpacialProducts().get(productID)!=null){
            return new Response(true,"EditCart: The product is a special product, so it cannot be edited");
        }
        Integer preQuantity = this.shoppingBags.get(storeID).getProductQuantity(productID);
        this.shoppingBags.get(storeID).editProductQuantity(productID, quantity);
        if(!tradingSystem.validation.checkBuyingPolicy(userID,storeID,this.shoppingBags.get(storeID).getProducts())){
            this.shoppingBags.get(storeID).editProductQuantity(productID, preQuantity);
            return new Response(true,"EditCart: The quantity of the product is against tha store policy, so it cannot be edited");
        }
        else{
            this.shoppingBags.get(storeID).editProductQuantity(productID, quantity);
            Double priceForBug = tradingSystem.calculateBugPrice(userID, storeID, this.shoppingBags.get(storeID).getProducts());
            shoppingBags.get(storeID).setFinalPrice(priceForBug);
        }
        return new Response(false,"EditCart: The quantity of the product update successfully");
}

      /**
     * @requirement 2.8
     *
     * @param storeID
     * @param productID
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     * }
     */
    public Response RemoveProductFromCart(int storeID, int productID) {
        if (this.shoppingBags.get(storeID) == null ||
                !this.shoppingBags.get(storeID).getProductsList().contains(productID)) {
            return new Response(true, "RemoveFromCart: product that does not exist in the cart cannot be removed");
        }
        else {
            ShoppingBag shoppingBag = this.shoppingBags.get(storeID);
            shoppingBag.RemoveProduct(productID);
            ConcurrentHashMap<Integer, Integer> productsInTheBug = shoppingBag.getProducts();
            Double priceForBug = tradingSystem.calculateBugPrice(userID, storeID, productsInTheBug);
            shoppingBags.get(storeID).setFinalPrice(priceForBug);;

        }
        return new Response(false, "RemoveFromCart: product removed successfully");
    }


    public Response removeSpecialProductFromCart(int storeID, int productID) {
        if (this.shoppingBags.get(storeID) == null ||
                !this.shoppingBags.get(storeID).getSpecialProductProductsList().contains(productID)) {
            return new Response(true, "removeSpecialProductFromCart: product that does not exist in the cart cannot be removed");
        }
        else {
            ShoppingBag shoppingBag = this.shoppingBags.get(storeID);
            shoppingBag.removeSpecialProductFromCart(productID);
            ConcurrentHashMap<Integer, Integer> productsInTheBug = shoppingBag.getProducts();
            Integer priceForBug = shoppingBag.calculateSpacialPrices();
            shoppingBags.get(storeID).setFinalSpecialPrice(priceForBug);;

        }
        return new Response(false, "RemoveFromCart: product removed successfully");
    }

}
/*
    public Integer addProduct(Integer productID, Integer quantity, Integer storeID, Double price)
    {
            if(!this.shoppingBags.containsKey(storeID)){
                ShoppingBag SB=new ShoppingBag(this.userID,storeID);
                SB.addProduct(productID,quantity);
                SB.setFinalPrice(price);
                this.shoppingBags.put(storeID, SB);
                this.pricePerShoppingBag.put(SB.getNextShoppingBagID(),price);
                this.finalPrice=calculatePrice();
                return 0;
                }
            else {
                Iterator it = this.shoppingBags.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();
                    int id = (int) pair.getKey();
                    ShoppingBag SB = (ShoppingBag) pair.getValue();
                    if (id == storeID) {
                        SB.addProduct(productID,quantity);
                        SB.setFinalPrice(price);
                        this.pricePerShoppingBag.remove(SB.getNextShoppingBagID());
                        this.pricePerShoppingBag.put(SB.getNextShoppingBagID(),price);
                        this.finalPrice=calculatePrice();
                        return 0;
                    }
            }

        }
        String err = "The product is not exist in the system";
        return -1;
    }
*/