package TradingSystem.Server.DomainLayer.ShoppingComponent;

import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingCart {

    private final TradingSystem tradingSystem = TradingSystem.getInstance();

    private Integer userID;

    //StoreID_ShoppingBag
    private ConcurrentHashMap<Integer, ShoppingBag> shoppingBags;

    private Object payment;//?

    public ShoppingCart(Integer userID)
    {
        this.userID = userID;
        this.shoppingBags = new ConcurrentHashMap<Integer, ShoppingBag>();
    }

    public void mergeToMyCart(ShoppingCart shoppingCartToMerge)
    {
        Set<Integer> keySetToMerge = shoppingCartToMerge.shoppingBags.keySet();
        for (int key : keySetToMerge){
            ShoppingBag newShoppingBag = shoppingCartToMerge.shoppingBags.get(key);
            if(!this.shoppingBags.containsKey(key)){
                this.shoppingBags.put(key, newShoppingBag);
            }
            else {
                this.shoppingBags.get(key).mergeToMyBag(newShoppingBag);
            }
        }
    }

    public String addProductToBag(Integer productID, Integer storeID, Integer quantity)
    {
        ConcurrentHashMap<Integer, Integer> productsInTheBug = new ConcurrentHashMap<Integer, Integer>();
        if (this.shoppingBags.get(storeID) != null) {
            productsInTheBug = shoppingBags.get(storeID).getProducts();
        } else {
            shoppingBags.put(storeID, new ShoppingBag(userID, storeID));
        }
        if (tradingSystem.checkProductsExistInTheStore(productID, storeID, quantity)) {
            if (tradingSystem.checkBuyingPolicy(productID, storeID, quantity, productsInTheBug)) {
                Double priceForBug = tradingSystem.calculateBugPrice(productID, storeID, quantity, productsInTheBug);
                shoppingBags.get(storeID).addProduct(productID, quantity);
                shoppingBags.get(storeID).setFinalPrice(priceForBug);
                return "The product added successfully";
            }

            return "Adding the product is against the store policy";
        }
        return "The product is not exist in stock";
    }

    private synchronized Double calculatePrice()
    {
        Double price = 0.0;
        Iterator it = this.shoppingBags.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            ShoppingBag p = (ShoppingBag) pair.getValue();
            price = price + p.getFinalPrice();
        }
        return price;
    }

    public ConcurrentHashMap<Integer, ShoppingBag> GetInfo() {
        return this.shoppingBags;
    }

    public String Purchase() throws InterruptedException
    {
        Iterator itBug = this.shoppingBags.entrySet().iterator();
        while (itBug.hasNext()) {
            Map.Entry bugPair = (Map.Entry) itBug.next();
            int storeID = (int) bugPair.getKey();
            ShoppingBag SB = (ShoppingBag) bugPair.getValue();
            Iterator itProd = SB.getProducts().entrySet().iterator();
            while (itProd.hasNext()) {
                Map.Entry prodPair = (Map.Entry) itProd.next();
                int productID = (int) prodPair.getKey();
                int productQuantity = (int) prodPair.getValue();
                while (tradingSystem.productIsLock(productID, storeID)) { //todo check!
                    wait();
                }
                tradingSystem.lockProduct(productID, storeID);
                if (!tradingSystem.checkProductsExistInTheStore(productID, storeID, productQuantity)) {
                    releaseAllProduct();
                    String storeName = tradingSystem.getStoreName(storeID);
                    String productName = tradingSystem.getProductName(storeID, productID);
                    return productName + " in The store" + storeName + " is not exist in the stock";
                }
            }
        }
        if (paymentAprove()) {
            Buy();
            releaseAllProduct();
           // PayToTheSellers();
            addShopingHistory();
            return "The purchase was made successfully ";
        }
        releaseAllProduct();
        return "There is problem with the payment";
    }

    private void addShopingHistory()
    {
        Iterator it = this.shoppingBags.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            int storeID = (int) pair.getKey();
            ShoppingBag SB=(ShoppingBag) pair.getValue();
            ShoppingHistory SH=new ShoppingHistory(SB.getUserID(),storeID,SB.getProducts(),SB.getFinalPrice());
            tradingSystem.addHistoryToStorAnddUser(SH);
        }
    }

    private boolean paymentAprove()
    {
        return true;
    }

    private void releaseAllProduct()
    {
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

    private boolean Buy()
    {
        Iterator itBug = this.shoppingBags.entrySet().iterator();
        while (itBug.hasNext()) {
            Map.Entry bugPair = (Map.Entry) itBug.next();
            int storeID = (int) bugPair.getKey();
            ShoppingBag SB = (ShoppingBag) bugPair.getValue();
            if (!tradingSystem.reduseProducts(SB.getProducts(), storeID))//todo- check if work
            {
                return false;
            }
        }
        return true;
    }

    public Integer Purchase(Object Payment) {
        return 0;
    }

    public LinkedList<DummyProduct> ShowShoppingCart()
    {
        LinkedList<DummyProduct> DummyProducts = new LinkedList<DummyProduct>();

        Iterator itBug = this.shoppingBags.entrySet().iterator();
        while (itBug.hasNext()) {
            Map.Entry bugPair = (Map.Entry) itBug.next();
            int storeID = (int) bugPair.getKey();
            ShoppingBag SB = (ShoppingBag) bugPair.getValue();
            Iterator itProd = SB.getProducts().entrySet().iterator();
            while (itProd.hasNext()) {
                Map.Entry prodPair = (Map.Entry) itProd.next();
                int productID = (int) prodPair.getKey();
                Product p = tradingSystem.getProduct(storeID,productID);
                DummyProduct d=new DummyProduct(storeID,tradingSystem.getStoreName(storeID),productID,p.getProductName(),p.getPrice(),p.getCategory());
                DummyProducts.add(d);
            }
        }
        return DummyProducts;
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