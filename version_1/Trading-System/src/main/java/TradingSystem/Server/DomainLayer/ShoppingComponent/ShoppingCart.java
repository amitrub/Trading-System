package TradingSystem.Server.DomainLayer.ShoppingComponent;

import TradingSystem.Server.DomainLayer.StoreComponent.Inventory;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingCart {

    private final TradingSystem tradingSystem = TradingSystem.getInstance();

    private Integer userID;

    //StoreID_ShoppingBag
    private ConcurrentHashMap<Integer, ShoppingBag> shoppingBags;

    //ShoppingBagID_price
    private ConcurrentHashMap<Integer, Double> pricePerShoppingBag;

    private Double finalPrice;

    private Object payment;//?

    public ShoppingCart(Integer userID)
    {
        this.userID = userID;
        this.shoppingBags=new ConcurrentHashMap<Integer, ShoppingBag>();
        this.pricePerShoppingBag=new ConcurrentHashMap<Integer, Double>();
    }

    public String addProductToBag(Integer productID, Integer storeID, Integer quantity)
    {
        ConcurrentHashMap<Integer,Integer> productsInTheBug=new ConcurrentHashMap<Integer,Integer>();
        if(this.shoppingBags.get(storeID)!=null)
        {
            productsInTheBug = shoppingBags.get(storeID).getProducts();
        }
        else
        {
          shoppingBags.put(storeID,new ShoppingBag(userID,storeID));
        }
        if (tradingSystem.checkProductsExistInTheStore(productID, storeID, quantity))
        {
            if (tradingSystem.checkBuyingPolicy(productID, storeID, quantity, productsInTheBug))
            {
                Double priceForBug=tradingSystem.calculateBugPrice(productID, storeID, quantity, productsInTheBug);
                shoppingBags.get(storeID).addProduct(productID,quantity);
                shoppingBags.get(storeID).setFinalPrice(priceForBug);
                Double price=this.calculatePrice();
                this.setFinalPrice(price);
                return "The product added successfully";
            }

         return "Adding the product is against the store policy";
        }
        return "The product is not exist in stock";
    }

    private synchronized Double calculatePrice() {
        Double price=0.0;
        Iterator it = this.pricePerShoppingBag.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Double p = (Double) pair.getValue();
            price=price+p;
        }
        return price;
    }

    public synchronized void setFinalPrice(Double finalPrice)
    {
        this.finalPrice = finalPrice;
    }

    public ConcurrentHashMap<Integer, ShoppingBag> GetInfo(){
        return this.shoppingBags;
    }

    public String Purchase() throws InterruptedException {
        Iterator itBug = this.shoppingBags.entrySet().iterator();
        while (itBug.hasNext())
        {
            Map.Entry bugPair = (Map.Entry) itBug.next();
            int storeID = (int) bugPair.getKey();
            ShoppingBag SB = (ShoppingBag) bugPair.getValue();
            Iterator itProd = SB.getProducts().entrySet().iterator();
                while (itProd.hasNext())
                {
                    Map.Entry prodPair = (Map.Entry) itProd.next();
                    int productID = (int) prodPair.getKey();
                    int productQuantity = (int) prodPair.getValue();
                    while(tradingSystem.productIsLock(productID, storeID)){ //todo check!
                        wait();
                    }
                    tradingSystem.lockProduct(productID, storeID);
                    if (!tradingSystem.checkProductsExistInTheStore(productID, storeID, productQuantity))
                    {
                        releaseAllProduct();
                        String storeName= tradingSystem.getStoreName(storeID);
                        String productName=tradingSystem.getProductName(storeID,productID);
                        return productName +" in The store" +storeName+" is not exist in the stock";
                    }
                }
        }
        if(paymentAprove()){
            Buy();
            releaseAllProduct();
            return "The purchase was made successfully ";
        }
        releaseAllProduct();
        return "There is problem with the payment";
    }

    private boolean paymentAprove() {
        return true;
    }

    private void releaseAllProduct() {
        Iterator itBug = this.shoppingBags.entrySet().iterator();
        while (itBug.hasNext()) {
            Map.Entry bugPair = (Map.Entry) itBug.next();
            int storeID = (int) bugPair.getKey();
            ShoppingBag SB = (ShoppingBag) bugPair.getValue();
            tradingSystem.unLockProducts(SB.getProducts().values(), storeID); //todo- check if work
            /*
            Iterator itProd = SB.getProducts().entrySet().iterator();
            while (itProd.hasNext())
            {
                Map.Entry prodPair = (Map.Entry) itProd.next();
                int productID = (int) prodPair.getKey();
                tradingSystem.unLockProducts(productID, storeID);
                }
            }
            */
        }
    }

    private boolean Buy() {
        Iterator itBug = this.shoppingBags.entrySet().iterator();
        while (itBug.hasNext()) {
            Map.Entry bugPair = (Map.Entry) itBug.next();
            int storeID = (int) bugPair.getKey();
            ShoppingBag SB = (ShoppingBag) bugPair.getValue();
           if(!tradingSystem.reduseProduct(SB.getProducts().values(), storeID))//todo- check if work
           {
               return false;
           }
        }
       return true;
    }

    public Integer Purchase(Object Payment){
        return 0;
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
                        this.pricePerShoppingBag.remove(SB.getNextShoppingBagID());//todo check if work
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