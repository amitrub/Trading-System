package TradingSystem.Server.ServiceLayer.DummyObject;

import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.HashMap;

@Entity(name = "ShoppingBag")
@IdClass(DummyKeyShopping.class)
public class DummyShoppingBag {

    @Id
    private Integer userID;
    @Id
    private Integer storeID;
    @org.hibernate.annotations.Type(
            type = "org.hibernate.type.SerializableToBlobType",
            parameters = { @Parameter( name = "products", value = "java.util.HashMap" ) }
    )
    private HashMap<Integer,Integer> products;

    private Double finalPrice;

    public DummyShoppingBag(){

    }

    public DummyShoppingBag(int userID, int storeID, HashMap<Integer,Integer> products, double finalPrice){
        this.userID=userID;
        this.storeID=storeID;
    //    this.products= products;
        this.finalPrice= finalPrice;
    }
}
