package TradingSystem.Server.DataLayer.Data_Modules;

import javax.persistence.*;
import java.util.List;

@Entity(name = "ShoppingHistory")
@IdClass(DummyKeyShopping.class)
public class DataShoppingHistory {

    @Id
    @JoinColumn(table = "user", referencedColumnName = "userid")
    private Integer userID;
    @Id
    @JoinColumn(table = "store", referencedColumnName = "storeid")
    private Integer storeID;
//    @ElementCollection
//    @CollectionTable(
//            name="product",
//            joinColumns=@JoinColumn(name="productid")
//    )
//    public List<DataProduct> products;
    private String date;
    private Double finalPrice;

    public DataShoppingHistory(){

    }

    public DataShoppingHistory(Integer userID, Integer storeID, List<DataProduct> products, String date, Double finalPrice){
        this.userID = userID;

        this.storeID = storeID;
     //   this.products = products;
        this.date = date;
        this.finalPrice = finalPrice;
    }
}
