package TradingSystem.Server.ServiceLayer.DummyObject;

public class DummySubscriber {

    public int userID;
    public String userName;

    public DummySubscriber(int userID, String userName) {
        this.userID = userID;
        this.userName = userName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "DummySubscriber{" +
                "userID=" + userID +
                ", userName='" + userName + '\'' +
                '}';
    }
}
