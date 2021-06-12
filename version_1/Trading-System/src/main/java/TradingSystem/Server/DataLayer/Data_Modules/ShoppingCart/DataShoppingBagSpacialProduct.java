package TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.DataShoppingBagProductKey;

import javax.persistence.*;

@Entity(name = "ShoppingBagSpacialProduct")
@Table(
        name = "shopping_bag_spacial_product"
)
public class DataShoppingBagSpacialProduct {

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

    @Column(
            name = "price",
            nullable = false
//            columnDefinition = "TEXT"
    )
    private int price;

    public DataShoppingBagSpacialProduct() {
        // DO NOT DELETE
    }

    public DataShoppingBagSpacialProduct(DataShoppingBagCart shoppingBag, DataProduct product, int quantity, int price) {
        this.key = new DataShoppingBagProductKey(shoppingBag.getKey(), product.getProductID());
        this.shoppingBag = shoppingBag;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public String toString() {
        return "DataShoppingBagSpacialProduct{" +
                "key=" + key +
                ", shoppingBag=" + shoppingBag +
                ", product=" + product +
                ", quantity=" + quantity +
                ", price=" + price +
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

    public int getPrice() {
        return price;
    }
}
