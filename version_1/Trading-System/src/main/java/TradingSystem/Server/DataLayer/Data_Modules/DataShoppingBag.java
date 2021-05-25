package TradingSystem.Server.DataLayer.Data_Modules;

import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import java.util.HashMap;

@Entity(name = "ShoppingBag")
@IdClass(DummyKeyShopping.class)
public class DataShoppingBag {

    @Id
    @JoinColumn(table = "user", referencedColumnName = "userid")
    private Integer userID;
    @Id
    @JoinColumn(table = "store", referencedColumnName = "storeid")
    private Integer storeID;
    @org.hibernate.annotations.Type(
            type = "org.hibernate.type.SerializableToBlobType",
            parameters = { @Parameter( name = "products", value = "java.util.HashMap" ) }
    )
    private HashMap<Integer,Integer> products;

    private Double finalPrice;

    public DataShoppingBag(){

    }

    public DataShoppingBag(int userID, int storeID, HashMap<Integer,Integer> products, double finalPrice){
        this.userID=userID;
        this.storeID=storeID;
        this.products= products;
        this.finalPrice= finalPrice;
    }
}
