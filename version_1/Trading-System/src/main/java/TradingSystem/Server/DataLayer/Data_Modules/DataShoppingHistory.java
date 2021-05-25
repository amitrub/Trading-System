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
    //    @OneToMany(targetEntity = DummyProduct.class, mappedBy = "products")
//    private List<DummyProduct> products;
    private String date;
    private Double finalPrice;

    public DataShoppingHistory(){

    }
}
