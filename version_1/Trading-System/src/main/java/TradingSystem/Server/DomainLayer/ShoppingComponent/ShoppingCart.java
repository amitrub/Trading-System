package TradingSystem.Server.DomainLayer.ShoppingComponent;

import TradingSystem.Server.DomainLayer.StoreComponent.Inventory;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingCart {

    private Integer userID;

    //StoreID_ShoppingBag
    private ConcurrentHashMap<Integer, ShoppingBag> shoppingBags;
    //ShoppingBagID_price
    private ConcurrentHashMap<Integer, Double> pricePerShoppingBag;
    private Double finalPrice;

    private Object payment;//?

    Inventory inventory;



    public ShoppingCart(Integer userID) {
        this.userID = userID;
        this.shoppingBags=new ConcurrentHashMap<Integer, ShoppingBag>();
        this.pricePerShoppingBag=new ConcurrentHashMap<Integer, Double>();
        inventory=Inventory.getInstance();
    }

    public Integer addProduct(Integer productID, Integer quantity, Integer storeID, Double price){
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

    private Double calculatePrice() {
        Double price=0.0;
        Iterator it = this.pricePerShoppingBag.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Double p = (Double) pair.getValue();
            price=price+p;
        }
        return price;
    }

    public ConcurrentHashMap<Integer, ShoppingBag> GetInfo(){
        return this.shoppingBags;
    }

    public Integer Purchase(){
        return 0;
    }

    public Integer Purchase(Object Payment){
        return 0;
    }
}
