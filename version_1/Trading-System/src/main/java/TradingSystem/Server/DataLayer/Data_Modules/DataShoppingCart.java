package TradingSystem.Server.DataLayer.Data_Modules;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "ShoppingCart")
@IdClass(DummyKeyShopping.class)
public class DataShoppingCart {
    @Id
    @JoinColumn(table = "Subscriber", referencedColumnName = "userid")
    private Integer userID;
    @Id
    @JoinColumn(table = "store_data", referencedColumnName = "storeid")
    private Integer storeID;
    @OneToMany
   // @CollectionTable(name="shopping_bag", joinColumns=@JoinColumn(name="storeid", referencedColumnName="storeid"), @JoinColumn(name="userid", referencedColumnName="userid"))
    public List<DataShoppingBag> shoppingBags;
    //StoreID
    @ElementCollection
    private Set<Integer> storesReducedProductsVain=new HashSet<>();

    public DataShoppingCart(){
    }

    public DataShoppingCart(int userID, int storeID,List<DataShoppingBag> shoppingBags,Set<Integer> storesReducedProductsVain){
        this.userID=userID;
        this.storeID=storeID;
        this.shoppingBags=shoppingBags;
        this.storesReducedProductsVain=storesReducedProductsVain;
    }
}
