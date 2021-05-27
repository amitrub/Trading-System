package TradingSystem.Server.DataLayer.Data_Modules;

import javax.persistence.*;
import java.util.List;

@Entity(name = "ShoppingBag")
public class DataShoppingBag {

    @Id
    @Column(
            name = "shoppingbagid"
    )
    @SequenceGenerator(
            name = "ShoppingBag_SEQUENCE",
            sequenceName = "ShoppingBag_SEQUENCE",
            allocationSize = 1
    )
    int id;
    @JoinColumn(table = "Subscriber", referencedColumnName = "userid")
    private int userID;
    @JoinColumn(table = "store_data", referencedColumnName = "storeid")
    private Integer storeID;
    @OneToMany(
            mappedBy = "shopping_bag",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    List<DataProduct> products;
    @ManyToOne
    @JoinColumn(
            name = "subscriber",
//            nullable = false,
            referencedColumnName = "userid",
            foreignKey = @ForeignKey(
                    name = "shopping_bag_id_FK"
            )
    )
    DataSubscriber subscriber;

    private Double finalPrice;

    public DataShoppingBag(){

    }

    public DataShoppingBag(int userID, int storeID, List<DataProduct> products, double finalPrice){
        this.userID=userID;
        this.storeID=storeID;
     //   this.products= products;
        this.finalPrice= finalPrice;
    }
}
