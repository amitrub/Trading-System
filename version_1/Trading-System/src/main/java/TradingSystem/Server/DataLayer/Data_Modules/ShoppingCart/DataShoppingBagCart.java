package TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserStoreProductKey;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "ShoppingBags")
@Table(
        name = "shopping_bags"
)
public class DataShoppingBagCart {

    @EmbeddedId
    private UserStoreProductKey key;

    @ManyToOne
    @MapsId("subscriberID")
    @JoinColumn(
            name = "subscriber_id",
            nullable = false,
            referencedColumnName = "userID",
            foreignKey = @ForeignKey(
                    name = "subscriber_shopping_bag_fk"
            )
    )
    private DataSubscriber subscriber;

    @ManyToOne
    @MapsId("storeID")
    @JoinColumn(
            name = "store_id",
            nullable = false,
            referencedColumnName = "storeID",
            foreignKey = @ForeignKey(
                    name = "store_shopping_bag_fk"
            )
    )
    private DataStore store;

    @OneToMany(
            mappedBy = "shoppingBag",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    List<DataShoppingBagProduct> products = new ArrayList<>();;

    @Column(
            name = "finalPrice"
//            nullable = false,
//            columnDefinition = "TEXT"
    )
    private Double finalPrice;

    public DataShoppingBagCart(){
        // DO NOT DELETE
    }

    public DataShoppingBagCart(DataSubscriber subscriber, DataStore store) {
        this.key = new UserStoreProductKey(subscriber.getUserID(), store.getStoreID());
        this.subscriber = subscriber;
        this.store = store;
    }

    public void addProduct(DataShoppingBagProduct product) {
        if (!this.products.contains(product)) {
            this.products.add(product);
        }
    }

    public List<DataShoppingBagProduct> getProducts(){
        return this.products;
    }

    public void setProducts(List<DataShoppingBagProduct> products) {
        this.products = products;
    }

    public UserStoreProductKey getKey() {
        return key;
    }

    public void setKey(UserStoreProductKey key) {
        this.key = key;
    }

    public DataSubscriber getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(DataSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    public DataStore getStore() {
        return store;
    }

    public void setStore(DataStore store) {
        this.store = store;
    }

    public Double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(Double finalPrice) {
        this.finalPrice = finalPrice;
    }

    @Override
    public String toString() {
        return "DataShoppingBagCart{" +
                "key=" + key +
                ", subscriber=" + subscriber.getName() +
                ", store=" + store.getStoreID() +
                ", products=" + products +
                ", finalPrice=" + finalPrice +
                '}';
    }
}
