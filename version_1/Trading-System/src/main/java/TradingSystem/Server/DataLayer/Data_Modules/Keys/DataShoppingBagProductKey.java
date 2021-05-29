package TradingSystem.Server.DataLayer.Data_Modules.Keys;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DataShoppingBagProductKey implements Serializable {

    private UserStoreProductKey shoppingBagKey;

    @Column(name = "product_id")
    private int productID;

    public DataShoppingBagProductKey() {
    }

    public DataShoppingBagProductKey(UserStoreProductKey key, int productID) {
        this.shoppingBagKey = key;
        this.productID = productID;
    }

    public UserStoreProductKey getShoppingBagKey() {
        return shoppingBagKey;
    }

    public void setShoppingBagKey(UserStoreProductKey shoppingBagKey) {
        this.shoppingBagKey = shoppingBagKey;
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
        return productID == that.productID && Objects.equals(shoppingBagKey, that.shoppingBagKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shoppingBagKey, productID);
    }

    @Override
    public String toString() {
        return "DataShoppingBagProductKey{" +
                "shoppingBagKey=" + shoppingBagKey +
                ", productID=" + productID +
                '}';
    }
}
