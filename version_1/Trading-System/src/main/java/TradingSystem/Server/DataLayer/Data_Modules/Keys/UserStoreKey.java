package TradingSystem.Server.DataLayer.Data_Modules.Keys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserStoreKey implements Serializable {

    @Column(name = "subscriber_id")
    private int subscriberID;

    @Column(name = "store_id")
    private int storeID;

    public UserStoreKey() {
        // DO NOT DELETE
    }

    public UserStoreKey(int subscriberID, int storeID) {
        this.subscriberID = subscriberID;
        this.storeID = storeID;
    }

    public int getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(int subscriberID) {
        this.subscriberID = subscriberID;
    }

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserStoreKey that = (UserStoreKey) o;
        return subscriberID == that.subscriberID && storeID == that.storeID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriberID, storeID);
    }

    @Override
    public String toString() {
        return "UserStoreKey{" +
                "subscriberID=" + subscriberID +
                ", storeID=" + storeID +
                '}';
    }
}
