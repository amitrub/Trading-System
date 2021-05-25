package TradingSystem.Server.DataLayer.Data_Modules;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity(name = "ShoppingHistory")
@IdClass(DummyKeyShopping.class)
public class DataShoppingHistory {

    @Id
    private Integer userID;
    @Id
    private Integer storeID;
//    @ElementCollection
//    @CollectionTable(
//            name="product",
//            joinColumns=@JoinColumn(name="productid")
//    )
//    private List<Integer> products;
    private String date;
    private Double finalPrice;

    public DataShoppingHistory(){

    }
}
