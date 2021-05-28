package TradingSystem.Server.DataLayer.Data_Modules;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
//            nullable = false,
            columnDefinition = "TEXT"
    )
    private String storeName;

    @Column(
            name = "storeRate"
//            nullable = false
//            columnDefinition = "TEXT"
    )
    private Double storeRate;

    @ManyToOne
    @JoinColumn(
            name = "founder",
//            nullable = false,
            referencedColumnName = "userID",
            foreignKey = @ForeignKey(
                    name = "subscriber_store_fk"
            )
    )
    private DataSubscriber founder;

    @OneToMany(
            mappedBy = "store",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<DataProduct> products = new ArrayList<>();


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

    public Double getStoreRate() {
        return storeRate;
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
}
