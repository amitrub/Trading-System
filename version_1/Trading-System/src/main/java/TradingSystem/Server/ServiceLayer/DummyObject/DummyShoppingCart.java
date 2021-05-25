package TradingSystem.Server.ServiceLayer.DummyObject;

import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingBag;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Entity(name = "ShoppingCart")
public class DummyShoppingCart {

    @Id
    private Integer userID;
    @OneToMany(mappedBy="store")
    @MapKeyJoinColumn(name="ShoppingBag")
    //StoreID_ShoppingBag
    private HashMap<Integer, DummyShoppingBag> shoppingBags = new HashMap<>();
    //StoreID
    @ElementCollection
    private Set<Integer> storesReducedProductsVain=new HashSet<>();

    public DummyShoppingCart(){

    }
    public DummyShoppingCart(int userID, HashMap<Integer,DummyShoppingBag> shoppingBags,Set<Integer> storesReducedProductsVain){
        this.userID=userID;
        this.shoppingBags=shoppingBags;
        this.storesReducedProductsVain=storesReducedProductsVain;
    }
}
