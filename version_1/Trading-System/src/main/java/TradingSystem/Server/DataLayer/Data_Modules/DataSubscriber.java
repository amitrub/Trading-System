package TradingSystem.Server.DataLayer.Data_Modules;

import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagCart;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingHistory.DataShoppingHistory;
import TradingSystem.Server.DomainLayer.UserComponent.User;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

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
            fetch = FetchType.LAZY
    )
    private List<DataStore> storesFounder = new ArrayList<>();

    @OneToMany(
            mappedBy = "subscriber",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<DataShoppingBagCart> shoppingBagsCart= new ArrayList<>();

    @OneToMany(
            mappedBy = "subscriber",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
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

    public DataShoppingBagCart FindBag(Integer storeID) {
        for (DataShoppingBagCart bag : this.shoppingBagsCart){
            if(bag.getStore().getStoreID()== storeID){
                return bag;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "DataSubscriber{" +
                "userID=" + userID +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", storesFounder=" + storesFounder +
                ", shoppingBagsCart=" + shoppingBagsCart +
                '}';
    }
}
