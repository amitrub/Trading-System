package TradingSystem.Server.DataLayer.Data_Modules;



import TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataBuyingPolicy;
import TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy.DataDiscountPolicy;
import TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy.DataSalePolicyKey;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagCart;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingHistory.DataShoppingHistory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
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

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
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
            fetch = FetchType.LAZY
    )
    private List<DataProduct> products = new ArrayList<>();

    @OneToMany(
            mappedBy = "store",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<DataShoppingBagCart> shoppingBagsCart= new ArrayList<>();

    @OneToMany(
            mappedBy = "store",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<DataShoppingHistory> shoppingBagsHistory= new ArrayList<>();

//    @ManyToOne
//    @JoinColumn(
//            name = "buyingPolicy_id",
//            nullable = false,
//            referencedColumnName = "data_buying_policy_pkey",
//            foreignKey = @ForeignKey(
//                    name = "buying_id_fk"
//            )
//    )
//    private DataBuyingPolicy buyingPolicy;
//
//    @ManyToOne
//    @JoinColumn(
//            name = "dataDiscountPolicy_id",
//            nullable = false,
//            referencedColumnName = "data_discount_policy_pkey",
//            foreignKey = @ForeignKey(
//                    name = "discount_id_fk"
//            )
//    )
//    private DataDiscountPolicy dataDiscountPolicy;


//    @ElementCollection
//    @CollectionTable(name="dummy_user", joinColumns=@JoinColumn(name="userid"))
//    private List<Integer> ownersIDs;
//    @ElementCollection
//    @CollectionTable(name="dummy_user", joinColumns=@JoinColumn(name="userid"))
//    private List<Integer> managersIDs;



    public DataStore() {
        // DO NOT DELETE
    }

    public DataStore(String name){
        this.storeName = name;
        this.storeRate = 5.0;
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
                ", founder=" + founder.getName() +
                ", products=" + products +
                ", shoppingBagsCart=" + shoppingBagsCart +
                '}';
    }
}
