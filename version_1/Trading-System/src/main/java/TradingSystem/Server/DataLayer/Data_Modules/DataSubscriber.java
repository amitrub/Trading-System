package TradingSystem.Server.DataLayer.Data_Modules;

import javax.persistence.*;

@Entity(name = "Subscriber")
@SequenceGenerator(name="USER_SEQUENCE_GENERATOR", sequenceName="USER_SEQUENCE_GENERATOR", initialValue=1, allocationSize=1)
public class DataSubscriber {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="USER_SEQUENCE_GENERATOR")
    private Integer userID;
    String name;
    String password;

    public DataSubscriber(){

    }

    public DataSubscriber(String name, String password){
        this.name=name;
        this.password=password;
    }

    public Integer getUserID() {
        return userID;
    }
}
