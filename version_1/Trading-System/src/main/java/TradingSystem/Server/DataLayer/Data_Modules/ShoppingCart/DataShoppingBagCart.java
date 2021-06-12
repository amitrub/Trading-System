package TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserStoreKey;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "ShoppingBags")
@Table(
        name = "shopping_bags"
)
public class DataShoppingBagCart {

    @EmbeddedId
    private UserStoreKey key;

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
            fetch = FetchType.EAGER
    )
    List<DataShoppingBagProduct> products = new ArrayList<>();

    @OneToMany(
            mappedBy = "shoppingBag",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    List<DataShoppingBagSpacialProduct> spacialProducts = new ArrayList<>();

    @Column(
            name = "finalPrice"
//            nullable = false,
//            columnDefinition = "TEXT"
    )
    private Double finalPrice=0.0;

    @Column(
            name = "finalPriceSpacialProducts"
//            nullable = false,
//            columnDefinition = "TEXT"
    )
    private int finalPriceSpacial =0;

    public DataShoppingBagCart(){
        // DO NOT DELETE
    }

    public DataShoppingBagCart(DataSubscriber subscriber, DataStore store) {
        this.key = new UserStoreKey(subscriber.getUserID(), store.getStoreID());
        this.subscriber = subscriber;
        this.store = store;
    }

    public void addProduct(DataShoppingBagProduct product) {
        if (!this.products.contains(product)) {
            this.products.add(product);
        }
    }
    public void removeProduct(DataShoppingBagProduct product) {
        if (this.products.contains(product)) {
            this.products.remove(product);
        }
    }

    public void addSpacialProduct(DataShoppingBagSpacialProduct product) {
        if (!this.spacialProducts.contains(product)) {
            this.spacialProducts.add(product);
        }
    }
    public void removeSpacialProduct(DataShoppingBagSpacialProduct product) {
        if (this.spacialProducts.contains(product)) {
            this.spacialProducts.remove(product);
        }
    }

    public List<DataShoppingBagProduct> getProducts(){
        return this.products;
    }

    public void setProducts(List<DataShoppingBagProduct> products) {
        this.products = products;
    }

    public List<DataShoppingBagSpacialProduct> getSpacialProducts() {
        return spacialProducts;
    }

    public void setSpacialProducts(List<DataShoppingBagSpacialProduct> spacialProducts) {
        this.spacialProducts = spacialProducts;
    }

    public UserStoreKey getKey() {
        return key;
    }

    public void setKey(UserStoreKey key) {
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

    public int getFinalPriceSpacial() {
        return finalPriceSpacial;
    }

    public void setFinalPriceSpacial(int finalPriceSpacial) {
        this.finalPriceSpacial = finalPriceSpacial;
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
