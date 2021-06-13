package TradingSystem.Server.DataLayer.Data_Modules.Keys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DataShoppingBagProductKey implements Serializable {

    private UserStoreKey userStoreKey;

    @Column(name = "product_id")
    private int productID;

    public DataShoppingBagProductKey() {
    }

    public DataShoppingBagProductKey(UserStoreKey key, int productID) {
        this.userStoreKey = key;
        this.productID = productID;
    }

    public UserStoreKey getUserStoreKey() {
        return userStoreKey;
    }

    public void setUserStoreKey(UserStoreKey userStoreKey) {
        this.userStoreKey = userStoreKey;
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
        DataShoppingBagProductKey that = (DataShoppingBagProductKey) o;
        return productID == that.productID && Objects.equals(userStoreKey, that.userStoreKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userStoreKey, productID);
    }

    @Override
    public String toString() {
        return "DataShoppingBagProductKey{" +
                "shoppingBagKey=" + userStoreKey +
                ", productID=" + productID +
                '}';
    }
}
