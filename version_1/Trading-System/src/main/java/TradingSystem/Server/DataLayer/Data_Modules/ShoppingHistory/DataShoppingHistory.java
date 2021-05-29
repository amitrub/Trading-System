package TradingSystem.Server.DataLayer.Data_Modules.ShoppingHistory;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserStoreProductKey;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "ShoppingHistories")
@Table(
        name = "shopping_histories"
)
public class DataShoppingHistory {

    @Id
    @SequenceGenerator(
            name = "SHOPPING_HISTORY_SEQUENCE",
            sequenceName = "SHOPPING_HISTORY_SEQUENCE",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "SHOPPING_HISTORY_SEQUENCE"
    )
    @Column(
            name = "shoppingHistoryID"
    )
    private Integer shoppingHistoryID;

    @ManyToOne
    @JoinColumn(
            name = "subscriber_id",
//            nullable = false,
            referencedColumnName = "userID",
            foreignKey = @ForeignKey(
                    name = "subscriber_shopping_history_fk"
            )
    )
    private DataSubscriber subscriber;

    @ManyToOne
    @JoinColumn(
            name = "store_id",
            nullable = false,
            referencedColumnName = "storeID",
            foreignKey = @ForeignKey(
                    name = "store_shopping_history_fk"
            )
    )
    private DataStore store;

    @Column(
            name = "date",
            nullable = false,
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE"
    )
    private Date date;

    @Column(
            name = "finalPrice"
//            nullable = false,
//            columnDefinition = "TEXT"
    )
    private Double finalPrice;

    @OneToMany(
            mappedBy = "shoppingHistory",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<DataHistoryProduct> products = new ArrayList<>();

    public DataShoppingHistory(){
        // DO NOT DELETE
    }

    public DataShoppingHistory(DataSubscriber subscriber, DataStore store, Date date, Double finalPrice) {
        this.subscriber = subscriber;
        this.store = store;
        this.date = date;
        this.finalPrice = finalPrice;
    }

    public void addProduct(DataHistoryProduct product) {
        if (!this.products.contains(product)) {
            this.products.add(product);
            product.setShoppingHistory(this);
        }
    }

    @Override
    public String toString() {
        return "DataShoppingHistory{" +
                "shoppingHistoryID=" + shoppingHistoryID +
                ", subscriber=" + subscriber.getUserID() +
                ", store=" + store.getStoreID() +
                ", date=" + date +
                ", finalPrice=" + finalPrice +
                ", products=" + products +
                '}';
    }

    public DataStore getStore(){
        return this.store;
    }
    public int getid(){
        return this.shoppingHistoryID;
    }
    public DataSubscriber getSubscriber(){
        return this.subscriber;
    }
    public Date getDate(){
        return this.date;
    }
    public List<DataHistoryProduct> getproducts(){
        return this.products;
    }

    public double getprice(){
        return this.finalPrice;
    }
}
