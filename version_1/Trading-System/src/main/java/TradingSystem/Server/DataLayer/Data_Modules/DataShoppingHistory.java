package TradingSystem.Server.DataLayer.Data_Modules;

import javax.persistence.*;
import java.util.List;

@Entity(name = "ShoppingHistory")
@IdClass(DummyKeyShopping.class)
public class DataShoppingHistory {

    @Id
    @JoinColumn(table = "Subscriber", referencedColumnName = "userid")
    private Integer userID;
    @Id
    @JoinColumn(table = "store_data", referencedColumnName = "storeid")
    private Integer storeID;
    @OneToMany
    public List<DataProduct> products;
    private String date;
    private Double finalPrice;

    public DataShoppingHistory(){


    }

    public DataShoppingHistory(Integer userID, Integer storeID, List<DataProduct> products, String date, Double finalPrice){
        this.userID = userID;

        this.storeID = storeID;
        this.products = products;
        this.date = date;
        this.finalPrice = finalPrice;
    }
}
