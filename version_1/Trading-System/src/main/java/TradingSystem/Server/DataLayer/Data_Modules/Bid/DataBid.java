package TradingSystem.Server.DataLayer.Data_Modules.Bid;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserProductKey;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagProduct;

import javax.persistence.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Entity(name = "Bids")
@Table(
        name = "bids"
)
public class DataBid {

    @EmbeddedId
    private UserProductKey key;

    @ManyToOne
    @MapsId("subscriberID")
    @JoinColumn(
            name = "subscriber_id",
            nullable = false,
            referencedColumnName = "userID",
            foreignKey = @ForeignKey(
                    name = "subscriber_comment_fk"
            )
    )
    private DataSubscriber subscriber;

    @ManyToOne
    @MapsId("productID")
    @JoinColumn(
            name = "product_id",
            nullable = false,
            referencedColumnName = "productID",
            foreignKey = @ForeignKey(
                    name = "product_comment_fk"
            )
    )
    private DataProduct product;

    @Column(
            name = "price",
            nullable = false
//            columnDefinition = "TEXT"
    )
    private int price;

    @Column(
            name = "quantity"
    )
    private int quantity;

    @OneToMany(
            mappedBy = "bid",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    List<DataBidManagerApproves> ownerAndManagerApprovals = new ArrayList<>();;

    public DataBid(){
        // DO NOT DELETE
    }

    public DataBid(DataSubscriber subscriber, DataProduct product, int price, int quantity) {
        this.key = new UserProductKey(subscriber.getUserID(), product.getProductID());
        this.subscriber = subscriber;
        this.product = product;
        this.price = price;
        this.quantity = quantity;
    }

    public UserProductKey getKey() {
        return key;
    }

    public DataSubscriber getSubscriber() {
        return subscriber;
    }

    public DataProduct getProduct() {
        return product;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public List<DataBidManagerApproves> getOwnerAndManagerApprovals() {
        return ownerAndManagerApprovals;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void RemoveManagerApprovals(){
        this.ownerAndManagerApprovals = new ArrayList<>();
    }
}
