package TradingSystem.Server.DataLayer.Data_Modules;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "Subscriber")
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
