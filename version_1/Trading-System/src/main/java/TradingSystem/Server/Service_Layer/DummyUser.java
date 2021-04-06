package TradingSystem.Server.Service_Layer;

import org.json.JSONObject;

public class DummyUser {
    String userName;
    String password;

    public DummyUser(String userName, String password) {
        this.userName = userName;
        this.password = password;
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
