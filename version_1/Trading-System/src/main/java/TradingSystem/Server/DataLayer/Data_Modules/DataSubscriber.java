package TradingSystem.Server.DataLayer.Data_Modules;

import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataManagerPermissions;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataOwnerPermissions;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagCart;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingHistory.DataShoppingHistory;
import TradingSystem.Server.DomainLayer.UserComponent.User;

import javax.persistence.*;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;


@Entity(name = "Subscribers")
@Table(
        name = "subscribers",
        uniqueConstraints = {
                @UniqueConstraint(name = "subscriber_name_unique", columnNames = "name")
        }
)
public class DataSubscriber {
    @Id
    @SequenceGenerator(
            name = "USER_SEQUENCE",
            sequenceName = "USER_SEQUENCE",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "USER_SEQUENCE"
    )
    @Column(
            name = "userID"
    )
    private Integer userID;

    @Column(
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    @Column(
            name = "password",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String password;

    @OneToMany(
            mappedBy = "founder",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    private List<DataStore> storesFounder = new ArrayList<>();

    @ManyToMany(
            mappedBy = "owners",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    private Set<DataStore> storesOwner = new HashSet<>();

    @OneToMany(
            mappedBy = "subscriber",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    private List<DataOwnerPermissions> ownerPermissions= new ArrayList<>();

    @ManyToMany(
            mappedBy = "managers",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    private Set<DataStore> storesManager = new HashSet<>();

    @OneToMany(
            mappedBy = "subscriber",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    private List<DataManagerPermissions> managerPermissions= new ArrayList<>();

    @OneToMany(
            mappedBy = "subscriber",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    private List<DataShoppingBagCart> shoppingBagsCart= new ArrayList<>();

    @OneToMany(
            mappedBy = "subscriber",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    private List<DataShoppingHistory> shoppingBagsHistory= new ArrayList<>();

    public DataSubscriber() {
        // DO NOT DELETE
    }

    public DataSubscriber(String name, String password){
        this.name=name;
        this.password=password;
    }

    public DataSubscriber(User user){
        this.name=user.getUserName();
        this.password=user.getPassword();
    }

    public Integer getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public List<DataStore> getStoresFounder() {
        return storesFounder;
    }

    public Set<DataStore> getStoresOwner() {
        return storesOwner;
    }

    public Set<DataStore> getStoresManager() {
        return storesManager;
    }

    public List<DataManagerPermissions> getManagerPermissions() {
        return managerPermissions;
    }

    public List<DataShoppingBagCart> getShoppingBagsCart() {
        return shoppingBagsCart;
    }

    public List<DataOwnerPermissions> getOwnerPermissions() {
        return ownerPermissions;
    }

    public DataShoppingBagCart FindBag(Integer storeID) {
        for (DataShoppingBagCart bag : this.shoppingBagsCart){
            if(bag.getStore().getStoreID()== storeID){
                return bag;
            }
        }
        return null;
    }

    public void AddOwnerStore(DataStore store) {
        if (!this.storesOwner.contains(store)) {
            this.storesOwner.add(store);
        }
    }

    public void AddManagerStore(DataStore store) {
        if (!this.storesManager.contains(store)) {
            this.storesManager.add(store);
        }
    }

    public void RemoveOwner(DataStore store, DataOwnerPermissions ownerPermission){
        storesOwner.remove(store);
        ownerPermissions.remove(ownerPermission);
        store.RemoveOwner(this);
    }

    public void RemoveManager(DataStore store, DataManagerPermissions managerPermission){
        storesManager.remove(store);
        managerPermissions.remove(managerPermission);
        store.RemoveManager(this);
    }

    @Override
    public String toString() {
        return "DataSubscriber{" +
                "userID=" + userID +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", storesFounder=" + storesFounder +
                ", storesOwner=" + storesOwner +
                ", ownerPermissions=" + ownerPermissions +
                '}';
    }

    public List<DataShoppingHistory> getShoppingBagsHistory() {
        return shoppingBagsHistory;
    }
}
