package TradingSystem.Server.DataLayer.Data_Modules.Permissions;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.PermissionKey;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserStoreKey;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagProduct;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "OwnerPermissions")
@Table(
        name = "owner_permissions"
)
public class DataOwnerPermissions {

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
//            nullable = false,
            referencedColumnName = "userID",
            foreignKey = @ForeignKey(
                    name = "appointment_owner_permissions_fk"
            )
    )
    private DataSubscriber appointment;

    @OneToMany(
            mappedBy = "ownerPermissions",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    List<DataOwnerPermissionType> permissions = new ArrayList<>();


    public DataOwnerPermissions(){
        // DO NOT DELETE
    }

    public DataOwnerPermissions(DataSubscriber subscriber, DataStore store, DataSubscriber appointment) {
        this.key = new UserStoreKey(subscriber.getUserID(), store.getStoreID());
        this.subscriber = subscriber;
        this.store = store;
        this.appointment = appointment;
    }

    public void AddNewPermission(DataOwnerPermissionType newPermission) {
        if (!this.permissions.contains(newPermission)) {
            this.permissions.add(newPermission);
        }
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

    public List<DataOwnerPermissionType> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<DataOwnerPermissionType> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "DataOwnerPermissions{" +
                "key=" + key +
                ", appointment=" + appointment.getUserID() +
                ", permissions=" + permissions +
                '}';
    }
}
