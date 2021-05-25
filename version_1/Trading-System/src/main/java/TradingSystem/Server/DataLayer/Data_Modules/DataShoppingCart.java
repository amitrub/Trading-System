package TradingSystem.Server.DataLayer.Data_Modules;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "ShoppingCart")
@IdClass(DummyKeyShopping.class)
public class DataShoppingCart {
    @Id
    @JoinColumn(table = "user", referencedColumnName = "userid")
    private Integer userID;
    @Id
    @JoinColumn(table = "store", referencedColumnName = "storeid")
    private Integer storeID;
    //    @OneToMany(mappedBy="store")
//    @MapKeyJoinColumn(name="ShoppingBag")
//    //StoreID_ShoppingBag
//    private HashMap<Integer, DummyShoppingBag> shoppingBags = new HashMap<>();
    //StoreID
    @ElementCollection
    private Set<Integer> storesReducedProductsVain=new HashSet<>();

    public DataShoppingCart(){
    }

    public DataShoppingCart(int userID, int storeID,Set<Integer> storesReducedProductsVain){
        this.userID=userID;
        this.storeID=storeID;
        //  this.shoppingBags=shoppingBags;
        this.storesReducedProductsVain=storesReducedProductsVain;
    }
}
