package TradingSystem.Server.DataLayer.Data_Modules.Keys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserProductKey implements Serializable {

    @Column(name = "subscriber_id")
    private int subscriberID;

    @Column(name = "product_id")
    private int productID;

    public UserProductKey() {
        // DO NOT DELETE
    }

    public UserProductKey(int subscriberID, int productID) {
        this.subscriberID = subscriberID;
        this.productID = productID;
    }

    public int getSubscriberID() {
        return subscriberID;
    }

    public void setSubscriberID(int subscriberID) {
        this.subscriberID = subscriberID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProductKey that = (UserProductKey) o;
        return subscriberID == that.subscriberID && productID == that.productID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(subscriberID, productID);
    }

    @Override
    public String toString() {
        return "UserStoreKey{" +
                "subscriberID=" + subscriberID +
                ", productID=" + productID +
                '}';
    }
}
