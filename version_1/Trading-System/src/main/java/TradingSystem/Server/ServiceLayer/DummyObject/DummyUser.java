package TradingSystem.Server.ServiceLayer.DummyObject;

import TradingSystem.Server.DomainLayer.UserComponent.User;
import org.hibernate.annotations.GenericGenerator;
import org.json.JSONObject;

import javax.persistence.*;

@Entity(name = "Guest")
public class DummyUser {

    @Id
    private final int userid;
    String userName;
    String password;

    public DummyUser(){
        this.userid=-1;
    }


    public DummyUser(String userName, String password) {
        this.userid=-1;
        this.userName = userName;
        this.password = password;
    }

    public DummyUser(User user) {
        this.userid=-1;
        this.userName = user.getUserName();
        this.password = "XXXXXXX";
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        JSONObject JO = new JSONObject();
        try {
            JO.put("userName", this.userName);
            JO.put("password", this.password);
        } catch (Exception e) {
            System.out.println("dummyUser toString error");
        }
        return JO.toString();
    }
}
