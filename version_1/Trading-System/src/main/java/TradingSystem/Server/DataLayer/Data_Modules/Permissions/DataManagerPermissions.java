package TradingSystem.Server.DataLayer.Data_Modules.Permissions;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.PermissionKey;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserStoreKey;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagProduct;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "ManagerPermissions")
@Table(
        name = "manager_permissions"
)
public class DataManagerPermissions {

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

    @ManyToOne
    @JoinColumn(
            name = "appointment_id",
            nullable = false,
            referencedColumnName = "userID",
            foreignKey = @ForeignKey(
                    name = "appointment_manager_permissions_fk"
            )
    )
    private DataSubscriber appointment;

    @OneToMany(
            mappedBy = "managerPermissions",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    List<DataManagerPermissionType> permissions = new ArrayList<>();


    public DataManagerPermissions(){
        // DO NOT DELETE
    }

    public DataManagerPermissions(DataSubscriber subscriber, DataStore store, DataSubscriber appointment) {
        this.key = new UserStoreKey(subscriber.getUserID(), store.getStoreID());
        this.subscriber = subscriber;
        this.store = store;
        this.appointment = appointment;
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

    public DataSubscriber getAppointment() {
        return appointment;
    }

    public void setAppointment(DataSubscriber appointment) {
        this.appointment = appointment;
    }

    public List<DataManagerPermissionType> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<DataManagerPermissionType> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "DataManagerPermissions{" +
                "key=" + key +
                ", subscriber=" + subscriber.getUserID() +
                ", store=" + store.getStoreID() +
                '}';
    }
}
