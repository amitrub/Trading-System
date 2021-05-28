package TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DataShoppingBagKey implements Serializable {

    @Column(name = "subscriber_id")
    private int subscriberID;

    @Column(name = "store_id")
    private int storeID;

    public DataShoppingBagKey() {
        // DO NOT DELETE
    }

    public DataShoppingBagKey(int subscriberID, int storeID) {
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
        DataShoppingBagKey that = (DataShoppingBagKey) o;
        return subscriberID == that.subscriberID && storeID == that.storeID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriberID, storeID);
    }

    @Override
    public String toString() {
        return "DataShoppingBagKey{" +
                "subscriberID=" + subscriberID +
                ", storeID=" + storeID +
                '}';
    }
}
