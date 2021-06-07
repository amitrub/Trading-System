package TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.DataShoppingBagProductKey;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagCart;

import javax.persistence.*;

@Entity(name = "ShoppingBagProducts")
@Table(
        name = "shopping_bag_product"
)
public class DataShoppingBagProduct {

    @EmbeddedId
    private DataShoppingBagProductKey key;

    @ManyToOne
    @MapsId("shoppingBagKey")
    @JoinColumns({@JoinColumn(
            name = "subscriber_id",
            nullable = false,
            referencedColumnName = "subscriber_id",
            foreignKey = @ForeignKey(
                    name = "bag_product_subscriber_fk"
            )),
            @JoinColumn(
            name = "store_id",
            nullable = false,
            referencedColumnName = "store_id",
            foreignKey = @ForeignKey(
                    name = "bag_product_store_fk"
            )
    )
    })
    private DataShoppingBagCart shoppingBag;

    @ManyToOne
    @MapsId("productID")
    @JoinColumn(
            name = "product_id",
            nullable = false,
            referencedColumnName = "productID",
            foreignKey = @ForeignKey(
                    name = "bag_product_fk"
            )
    )
    private DataProduct product;

    @Column(
            name = "quantity"
    )
    private int quantity;

    public DataShoppingBagProduct() {
        // DO NOT DELETE
    }

    public DataShoppingBagProduct(DataShoppingBagCart shoppingBag, DataProduct product, int quantity) {
        this.key = new DataShoppingBagProductKey(shoppingBag.getKey(), product.getProductID());
        this.shoppingBag = shoppingBag;
        this.product = product;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "DataShoppingBagProduct{" +
                "key=" + key +
                ", shoppingBag=" + shoppingBag.getKey() +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }

    public int getQuantity(){
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public DataProduct getProduct(){
        return this.product;
    }
}
