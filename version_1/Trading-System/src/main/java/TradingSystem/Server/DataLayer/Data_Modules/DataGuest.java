package TradingSystem.Server.DataLayer.Data_Modules;

import javax.persistence.*;

@Entity(name = "Guest")
@SequenceGenerator(name="USER_SEQUENCE_GENERATOR", sequenceName="USER_SEQUENCE", initialValue=1, allocationSize=10)
public class DataGuest {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="USER_SEQUENCE_GENERATOR")
    private Integer userID;

    public DataGuest(){
        System.out.println("hello im guest");
    }

    public Integer getUserID() {
        return userID;
    }
}
