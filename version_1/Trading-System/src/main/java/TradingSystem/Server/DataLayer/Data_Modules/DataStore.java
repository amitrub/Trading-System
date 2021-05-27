package TradingSystem.Server.DataLayer.Data_Modules;


import javax.persistence.*;
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
            name = "founder",
            nullable = false,
            referencedColumnName = "userID",
            foreignKey = @ForeignKey(
                    name = "subscriber_store_fk"
            )
    )
    private DataSubscriber founder;


//    @ElementCollection
//    @CollectionTable(name="dummy_user", joinColumns=@JoinColumn(name="userid"))
//    private List<Integer> ownersIDs;
//    @ElementCollection
//    @CollectionTable(name="dummy_user", joinColumns=@JoinColumn(name="userid"))
//    private List<Integer> managersIDs;

    public DataStore(String name, DataSubscriber founder){
        this.storeName = name;
        this.storeRate = 5.0;
        this.founder =founder;
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
}
