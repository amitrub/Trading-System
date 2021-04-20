package TradingSystem.Server.DomainLayer.ShoppingComponent;

import TradingSystem.Server.DomainLayer.ExternalServices.PaymentSystem;
import TradingSystem.Server.DomainLayer.ExternalServices.SupplySystem;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.NewResponse;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public class ShoppingCart {

    private final TradingSystem tradingSystem = TradingSystem.getInstance();
    private final PaymentSystem paymentSystem = PaymentSystem.getInstance();
    private final SupplySystem supplySystem = SupplySystem.getInstance();

    private final Integer userID;
    //StoreID_ShoppingBag
    private ConcurrentHashMap<Integer, ShoppingBag> shoppingBags = new ConcurrentHashMap<>();

    public ShoppingCart(Integer userID){
        this.userID = userID;
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

    public void mergeToMyCart(ShoppingCart shoppingCartToMerge){
        Set<Integer> keySetToMerge = shoppingCartToMerge.shoppingBags.keySet();
        for (int storeID : keySetToMerge){
            ShoppingBag newShoppingBag = shoppingCartToMerge.shoppingBags.get(storeID);
            if(!this.shoppingBags.containsKey(storeID)){
                this.shoppingBags.put(storeID, newShoppingBag);
            }
            else {
                this.shoppingBags.get(storeID).mergeToMyBag(newShoppingBag);
            }
        }
    }
    public NewResponse addProductToBag(Integer storeID, Integer productID, Integer quantity){
        ConcurrentHashMap<Integer, Integer> productsInTheBug = new ConcurrentHashMap<Integer, Integer>();
        productsInTheBug.put(productID, quantity);
        if(this.shoppingBags.containsKey(storeID)){
            ShoppingBag shoppingBag = this.shoppingBags.get(storeID);
            Set<Integer> productSet = shoppingBag.getProducts().keySet();
            for (Integer key : productSet){
                if(key == productID)
                    productsInTheBug.put(key, shoppingBag.getProducts().get(key) + quantity);
                else
                    productsInTheBug.put(key, shoppingBag.getProducts().get(key));
            }
        }
        if (tradingSystem.validation.checkProductsExistInTheStore(storeID, productID, productsInTheBug.get(productID))) {
            if (tradingSystem.validation.checkBuyingPolicy(productID, storeID, quantity, productsInTheBug)) {
                if(!this.shoppingBags.containsKey(storeID)){
                    this.shoppingBags.put(storeID, new ShoppingBag(this.userID,storeID));
                }
                this.shoppingBags.get(storeID).addProduct(productID, quantity);
                Double priceForBug = tradingSystem.calculateBugPrice(productID, storeID, quantity, productsInTheBug);
                shoppingBags.get(storeID).setFinalPrice(priceForBug);
                return new NewResponse( "The product added successfully");
            }

            return new NewResponse(true, "Adding the product is against the store policy");
        }
        return new NewResponse(true, "The product or quantity is not in stock");
    }
    private synchronized Double calculatePrice(){
        double price = 0.0;
        Set<Integer> shoppingBagsSet = this.shoppingBags.keySet();
        for (Integer key : shoppingBagsSet){
            ShoppingBag p = this.shoppingBags.get(key);
            price = price + p.getFinalPrice();

        }
        return price;
    }

    public ConcurrentHashMap<Integer, ShoppingBag> GetInfo() {
        return this.shoppingBags;
    }

    public NewResponse Purchase(boolean isGuest,String name, String credit_number, String phone_number, String address){
        if (shoppingBags.size()==0){
            return new NewResponse(true, "There is on products in shopping cart");
        }
        List<Lock> lockList = this.getLockList();
        NewResponse productInStock = this.checkInventoryAndLockProduct(lockList);
        if (productInStock.getIsErr()){
            return productInStock;
        }
//        TODO: add BuyingPolicy and DiscountPolicy
        if (!supplySystem.canSupply(address)) {
            this.releaseLocks(lockList);
            return new NewResponse(true,"The Supply is not approve");
        }
        if (!paymentSystem.checkCredit(name, credit_number, phone_number)){
            this.releaseLocks(lockList);
            return new NewResponse(true,"The payment is not approve");
        }
        NewResponse res = Buy();
        if(res.getIsErr()) {
            this.releaseLocks(lockList);
            return res;
        }
        addShoppingHistory(isGuest);
        this.shoppingBags = new ConcurrentHashMap<>();
        return new NewResponse("The purchase was made successfully ");
    }

    private List<Lock> getLockList(){
        List<Lock> output = new ArrayList<>();
        Set<Integer> shoppingBagsSet = this.shoppingBags.keySet();
        for (Integer storeID : shoppingBagsSet) {
            ShoppingBag shoppingBag = this.shoppingBags.get(storeID);
            output.addAll(shoppingBag.getLockList());
        }
        return output;
    }
    private boolean tryLockList(List<Lock> lockList){
        List<Lock> succeededToLock = new ArrayList<>();
        for (Lock lock : lockList){
            if (lock.tryLock()){
                succeededToLock.add(lock);
            }
            else {
                for (Lock lockedLock : succeededToLock){
                    lockedLock.unlock();
                }
                return false;
            }
        }
        return true;
    }
    private void releaseLocks(List<Lock> lockList){
        for (Lock lock : lockList){
            lock.unlock();
        }
    }

    private NewResponse checkInventoryAndLockProduct(List<Lock> lockList){
        boolean succeededToLock = false;
        Set<Integer> shoppingBagsSet = this.shoppingBags.keySet();
        while (!succeededToLock){
            synchronized (this){
                for (Integer storeID : shoppingBagsSet){
                    ShoppingBag shoppingBag = this.shoppingBags.get(storeID);
                    NewResponse res = shoppingBag.checkInventory();
                    if (res.getIsErr()){
                        return res;
                    }
                }
                succeededToLock = this.tryLockList(lockList);
            }
        }
        return new NewResponse();
    }
    private NewResponse Buy(){
        NewResponse res=new NewResponse("The reduction was made successfully ");
        Set<Integer> shoppingBagsSet = this.shoppingBags.keySet();
        for (Integer storeID : shoppingBagsSet){
            ShoppingBag SB = this.shoppingBags.get(storeID);
            res = tradingSystem.reduseProducts(SB.getProducts(), storeID);
            if (res.getIsErr()){
                return res;
            }
            PayToTheSellers();
        }
        return res;
    }
    private void addShoppingHistory(boolean isGuest){
        Set<Integer> shoppingBagsSet = this.shoppingBags.keySet();
        for (Integer storeID : shoppingBagsSet){
            ShoppingBag shoppingBag= this.shoppingBags.get(storeID);
            ShoppingHistory shoppingHistory = shoppingBag.createShoppingHistory();
            tradingSystem.addHistoryToStoreAndUser(shoppingHistory, isGuest);
        }
    }
    private void PayToTheSellers() {
        Set<Integer> shoppingBagsSet = this.shoppingBags.keySet();
        for (Integer storeID : shoppingBagsSet) {
            ShoppingBag SB = this.shoppingBags.get(storeID);
            tradingSystem.PayToTheSellers(SB.getFinalPrice(),storeID);
        }
    }

    public List<DummyProduct> ShowShoppingCart(){
        List<DummyProduct> outputList = new ArrayList<>();
        Set<Integer> shoppingBagsSet = this.shoppingBags.keySet();
        for (Integer storeID : shoppingBagsSet){
            ShoppingBag SB = this.shoppingBags.get(storeID);
            Set<Integer> productSet = SB.getProducts().keySet();
            for (Integer productID: productSet){
                int quantity = SB.getProducts().get(productID);
                Product p = tradingSystem.getProduct(storeID,productID);
                DummyProduct d = new DummyProduct(storeID,tradingSystem.getStoreName(storeID),productID,p.getProductName(),p.getPrice(),p.getCategory(), quantity);
                outputList.add(d);
            }
        }
        return outputList;
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