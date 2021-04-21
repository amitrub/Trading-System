package TradingSystem.Server.DomainLayer.ShoppingComponent;

import TradingSystem.Server.DomainLayer.ExternalServices.PaymentSystem;
import TradingSystem.Server.DomainLayer.ExternalServices.SupplySystem;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;

import TradingSystem.Server.ServiceLayer.LoggerController;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import TradingSystem.Server.ServiceLayer.LoggerController;



import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public class ShoppingCart {

    private final TradingSystem tradingSystem = TradingSystem.getInstance();
    private final PaymentSystem paymentSystem = PaymentSystem.getInstance();
    private final SupplySystem supplySystem = SupplySystem.getInstance();

    private final Integer userID;
    
    private static final LoggerController loggerController=LoggerController.getInstance();
    //StoreID_ShoppingBag
    private ConcurrentHashMap<Integer, ShoppingBag> shoppingBags = new ConcurrentHashMap<>();

    public ShoppingCart(Integer userID){

        this.userID = userID;
    }

    public ShoppingCart(ShoppingCart shoppingCartToCopy){
        this.userID = shoppingCartToCopy.userID;
        this.shoppingBags = new ConcurrentHashMap<>();
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
    public Response addProductToBag(Integer storeID, Integer productID, Integer quantity){
        if(!this.shoppingBags.containsKey(storeID)){
            if (!tradingSystem.validation.checkProductsExistInTheStore(storeID, productID, quantity)) {
                loggerController.WriteErrorMsg("User "+userID+" try to add product " +productID+ " from store "+storeID+" to cart but failed. the product is not in the stock");
                return new Response(true, "The product or quantity is not in stock");
            }
            this.shoppingBags.put(storeID, new ShoppingBag(this.userID,storeID));
        }
        else {
            int oldQuantity = this.shoppingBags.get(storeID).getProductQuantity(productID);
            if (!tradingSystem.validation.checkProductsExistInTheStore(storeID, productID, quantity + oldQuantity)) {
                loggerController.WriteErrorMsg("User " + userID + " try to add product " + productID + " from store " + storeID + " to cart but failed. the product is not in the stock");
                return new Response(true, "The product or quantity is not in stock");
            }
        }

//        TODO: checkBuyingPolicy
//        if (!tradingSystem.validation.checkBuyingPolicy(productID, storeID, quantity, productsInTheBug)) {
//            loggerController.WriteErrorMsg("User "+userID+" try to add product " +productID+ " from store "+storeID+" to cart but failed. Adding the product is against the store policy");
//            return new Response(true, "Adding the product is against the store policy");
//        }
//        TODO: calculateBugPrice
//        Double priceForBug = tradingSystem.calculateBugPrice(productID, storeID, productsInTheBug);
//        shoppingBags.get(storeID).setFinalPrice(priceForBug);
        this.shoppingBags.get(storeID).addProduct(productID, quantity);
        loggerController.WriteLogMsg("User "+userID+" added product " +productID+ " from store "+storeID+" to cart successfully");
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

    public ConcurrentHashMap<Integer, ShoppingBag> GetInfo() {
        return this.shoppingBags;
    }

    public Response Purchase(boolean isGuest,String name, String credit_number, String phone_number, String address){
        if (shoppingBags.size()==0){
            return new Response(true, "There is on products in shopping cart");
        }
        List<Lock> lockList = this.getLockList();
        Response productInStock = this.checkInventoryAndLockProduct(lockList);
        if (productInStock.getIsErr()){
            return productInStock;
        }
//        TODO: add BuyingPolicy and DiscountPolicy
        if (!supplySystem.canSupply(address)) {
            this.releaseLocks(lockList);
            return new Response(true,"The Supply is not approve");
        }
        if (!paymentSystem.checkCredit(name, credit_number, phone_number)) {
            this.releaseLocks(lockList);
            return new Response(true,"The payment is not approve");
        }
        Response res = Buy();
        if(res.getIsErr()) {
            this.releaseLocks(lockList);
            return res;
        }
        addShoppingHistory(isGuest);
        this.shoppingBags = new ConcurrentHashMap<>();
        return new Response("The purchase was made successfully ");

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

    private Response Buy(){
        Response res=new Response("The reduction was made successfully ");
        Set<Integer> shoppingBagsSet = this.shoppingBags.keySet();
        for (Integer storeID : shoppingBagsSet) {
            ShoppingBag SB = this.shoppingBags.get(storeID);
            res = tradingSystem.reduceProducts(SB.getProducts(), storeID);
            if (res.getIsErr()) {
                return res;
            }
            PayToTheSellers();
        }
        return res;
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
                DummyProduct d = new DummyProduct(storeID, tradingSystem.getStoreName(storeID), productID, p.getProductName(), p.getPrice(), p.getCategory(), quantity);
                outputList.add(d);
            }
        }
        return outputList;
    }

    
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
        if(this.shoppingBags.isEmpty()){
            loggerController.WriteErrorMsg("user "+userID+" try to edit product quantity but the shoppingCart is empty");
            return new Response(true,"The shoppingCart empty, cannot be edited");
        }
        else if(this.shoppingBags.get(storeID)==null||
        !this.shoppingBags.get(storeID).getProductsList().contains(productID)){
            loggerController.WriteErrorMsg("user "+userID+" try to edit product quantity ("+productID+", from store "+storeID+"), but the product isn't in the shoppingCart");
            return new Response(true,"The product isn't in the shoppingCart, so it cannot be edited");
        }
        else if(!tradingSystem.validation.checkProductsExistInTheStore(storeID,productID,quantity)){
            loggerController.WriteErrorMsg("user "+userID+" try to change product quantity ("+productID+", from store "+storeID+"), to "+quantity+" but the product isn't in the stock");
            return new Response(true,"The product isn't in the stock, so it cannot be edited");
        }
        else if(!tradingSystem.validation.checkBuyingPolicy(productID,storeID,quantity,this.shoppingBags.get(storeID).getProducts())){
            loggerController.WriteErrorMsg("user "+userID+" try to change product quantity ("+productID+", from store "+storeID+"), to "+quantity+" but it's against the store policy");
            return new Response(true,"The quantity of the product is against tha store policy, so it cannot be edited");
        }
        else{
            this.shoppingBags.get(storeID).editProductQuantity(productID, quantity);
            tradingSystem.calculateBugPrice(productID,storeID, this.shoppingBags.get(storeID).getProducts());
        }
        loggerController.WriteLogMsg("user "+userID+" change product quantity ("+productID+", from store "+storeID+"), to "+quantity+".");
        return new Response("The quantity of the product update successfully");
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
            loggerController.WriteErrorMsg("user " + userID + " try to remove product " + productID + " (Store " + storeID + "), but the product is not in the cart.");
            return new Response(true, "product that does not exist in the cart cannot be removed");
        } else {
            ShoppingBag shoppingBag = this.shoppingBags.get(storeID);
            shoppingBag.RemoveProduct(productID);
            ConcurrentHashMap<Integer, Integer> productsInTheBug = shoppingBag.getProducts();
            Double priceForBug = tradingSystem.calculateBugPrice(userID, storeID, productsInTheBug);
            shoppingBags.get(storeID).setFinalPrice(priceForBug);
        }
        loggerController.WriteLogMsg("user " + userID + " remove product " + productID + " (Store " + storeID + ") successfully");
        return new Response("product remove successfully");
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