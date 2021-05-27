package TradingSystem.Server.DataLayer.Data_Modules;

import TradingSystem.Server.DomainLayer.UserComponent.User;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;


@Entity(name = "Subscriber")
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

    public DataSubscriber(String name, String password){
        this.name=name;
        this.password=password;
    }

    public Integer getUserID() {
        return userID;
    }
}
