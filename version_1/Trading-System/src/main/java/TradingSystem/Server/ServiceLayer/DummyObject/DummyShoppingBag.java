package TradingSystem.Server.ServiceLayer.DummyObject;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

@Entity(name = "ShoppingBag")
@IdClass(DummyKeyShopping.class)
public class DummyShoppingBag {

    @Id
    private Integer userID;
    @Id
    private Integer storeID;
    @ElementCollection
    private HashMap<Integer,Integer> products;

    private Double finalPrice;

    public DummyShoppingBag(){

    }

    public DummyShoppingBag(int userID, int storeID, HashMap<Integer,Integer> products, double finalPrice){
        this.userID=userID;
        this.storeID=storeID;
        this.products= products;
        this.finalPrice= finalPrice;
    }
}
