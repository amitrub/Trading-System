package TradingSystem.Server.DataLayer.Data_Modules;



import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataOwnerPermissions;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagCart;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingHistory.DataShoppingHistory;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Stores")
@Table(
        name = "stores",
        uniqueConstraints = {
                @UniqueConstraint(name = "store_name_unique", columnNames = "storeName")
        }
)
public class DataStore {
    @Id
    @SequenceGenerator(
            name = "STORE_SEQUENCE",
            sequenceName = "STORE_SEQUENCE",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "STORE_SEQUENCE"
    )
    @Column(
            name = "storeID"
    )
    private Integer storeID;

    @Column(

            name = "storeName",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String storeName;

    @Column(
            name = "storeRate",
            nullable = false
//            columnDefinition = "TEXT"
    )
    private Double storeRate;

    @Column(
            name = "date",
            nullable = false,
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE"
    )
    private Date date;

    @ManyToOne
    @JoinColumn(
            name = "founder_id",
            nullable = false,
            referencedColumnName = "userID",
            foreignKey = @ForeignKey(
                    name = "subscriber_store_fk"
            )
    )
    private DataSubscriber founder;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "store_owners",
            joinColumns = {@JoinColumn(name = "store_id", referencedColumnName = "storeID",
                    nullable = false, updatable = false, foreignKey = @ForeignKey(
                    name = "store_owners_fk"
            ))},
            inverseJoinColumns = {@JoinColumn(name = "owner_id", referencedColumnName = "userID",
                    nullable = false, updatable = false, foreignKey = @ForeignKey(
                    name = "owners_store_fk"))}
    )
    private Set<DataSubscriber> owners = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "store_managers",
            joinColumns = {@JoinColumn(name = "store_id", referencedColumnName = "storeID",
                    nullable = false, updatable = false, foreignKey = @ForeignKey(
                    name = "store_managers_fk"
            ))},
            inverseJoinColumns = {@JoinColumn(name = "manager_id", referencedColumnName = "userID",
                    nullable = false, updatable = false, foreignKey = @ForeignKey(
                    name = "managers_store_fk"))}
    )
    private Set<DataSubscriber> managers = new HashSet<>();

    @OneToMany(
            mappedBy = "store",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    private List<DataProduct> products = new ArrayList<>();

//    @OneToMany(
//            mappedBy = "store",
//            orphanRemoval = true,
//            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
//            fetch = FetchType.EAGER
//    )
//    private List<DataShoppingBagCart> shoppingBagsCart= new ArrayList<>();

    @OneToMany(
            mappedBy = "store",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    private List<DataShoppingHistory> shoppingBagsHistory= new ArrayList<>();


    public DataStore() {
        // DO NOT DELETE
    }

    public DataStore(String name){
        this.storeName = name;
        this.storeRate = 5.0;
        this.date = new Date();
    }

    public Integer getStoreID() {
        return storeID;
    }

    public void setStoreID(Integer storeID) {
        this.storeID = storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setStoreRate(Double storeRate) {
        this.storeRate = storeRate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<DataSubscriber> getOwners() {
        return owners;
    }

    public Set<DataSubscriber> getManagers() {
        return managers;
    }

    public Double getStoreRate() {
        return storeRate;
    }

    public List<DataShoppingHistory> getShoppingBagsHistory() {
        return shoppingBagsHistory;
    }

    public DataSubscriber getFounder() {
        return founder;
    }

    public void setFounder(DataSubscriber founder) {
        this.founder = founder;
    }

    public List<DataProduct> getProducts() {
        return products;
    }

    public void setProducts(List<DataProduct> products) {
        this.products = products;
    }

    public void AddNewOwner(DataSubscriber newOwner) {
        if (!this.owners.contains(newOwner)) {
            this.owners.add(newOwner);
        }
        newOwner.AddOwnerStore(this);
    }

    public void RemoveOwner(DataSubscriber owner){
        owners.remove(owner);
    }

    public void RemoveManager(DataSubscriber manager){
        managers.remove(manager);
    }

    public void AddNewManager(DataSubscriber newManager) {
        if (!this.managers.contains(newManager)) {
            this.managers.add(newManager);
        }
        newManager.AddManagerStore(this);
    }

    @Override
    public String toString() {
        return "DataStore{" +
                "storeID=" + storeID +
                ", storeName='" + storeName + '\'' +
                ", storeRate=" + storeRate +
                ", founder=" + founder.getUserID() +
                ", products=" + products +
                '}';
    }
}
