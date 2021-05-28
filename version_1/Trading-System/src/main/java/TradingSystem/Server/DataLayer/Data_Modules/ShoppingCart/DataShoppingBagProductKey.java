package TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DataShoppingBagProductKey implements Serializable {

    private DataShoppingBagKey shoppingBagKey;

    @Column(name = "product_id")
    private int productID;

    public DataShoppingBagProductKey() {
    }

    public DataShoppingBagProductKey(DataShoppingBagKey key, int productID) {
        this.shoppingBagKey = key;
        this.productID = productID;
    }

    public DataShoppingBagKey getShoppingBagKey() {
        return shoppingBagKey;
    }

    public void setShoppingBagKey(DataShoppingBagKey shoppingBagKey) {
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
