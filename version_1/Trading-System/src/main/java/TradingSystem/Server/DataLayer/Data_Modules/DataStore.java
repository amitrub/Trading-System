package TradingSystem.Server.DataLayer.Data_Modules;


import javax.persistence.*;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Store")
@Table(
        name = "stores",
        uniqueConstraints = {
                @UniqueConstraint(name = "store_name_unique", columnNames = "name")
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
            name = "name",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;

    @Column(
            name = "storeRate",
            nullable = false
//            columnDefinition = "TEXT"
    )
    private Double storeRate;

//    @ManyToOne
    @JoinColumn(
            table = "subscribers",
            name = "founderID",
            nullable = false
//            referencedColumnName = "userID",
//            foreignKey = @ForeignKey(
//                    name = "store_founder_fk"
//            )
    )
    private Integer founderID;


//    @ElementCollection
//    @CollectionTable(name="dummy_user", joinColumns=@JoinColumn(name="userid"))
//    private List<Integer> ownersIDs;
//    @ElementCollection
//    @CollectionTable(name="dummy_user", joinColumns=@JoinColumn(name="userid"))
//    private List<Integer> managersIDs;

    public DataStore(String name, Integer founderID){
        this.name = name;
        this.storeRate = 5.0;
        this.founderID=founderID;
    }

    public Integer getStoreID() {
        return storeID;
    }

    public void setStoreID(Integer storeID) {
        this.storeID = storeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getStoreRate() {
        return storeRate;
    }

    public void setStoreRate(Double storeRate) {
        this.storeRate = storeRate;
    }

    public Integer getFounderID() {
        return founderID;
    }

    public void setFounderID(Integer founderID) {
        this.founderID = founderID;
    }
}
