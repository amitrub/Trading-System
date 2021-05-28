package TradingSystem.Server.DataLayer.Data_Modules;

import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import static TradingSystem.Server.ServiceLayer.Configuration.errMsgGenerator;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Products")
@Table(
        name = "products"
//        uniqueConstraints = {
//                @UniqueConstraint(name = "store_name_unique", columnNames = "name")
//        }
)
public class DataProduct{

    @Id
    @SequenceGenerator(
            name = "PRODUCT_SEQUENCE",
            sequenceName = "PRODUCT_SEQUENCE",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "PRODUCT_SEQUENCE"
    )
    @Column(
            name = "productID"
    )
    private Integer productID;

    @Column(
            name = "productName",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String productName;

    @Column(
            name = "price",
            nullable = false
//            columnDefinition = "TEXT"
    )
    private double price;

    @Column(
            name = "category",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String category;

    @Column(
            name = "quantity"
    )
    private int quantity;

    @ManyToOne
    @JoinColumn(
            name = "store",
//            nullable = false,
            referencedColumnName = "storeID",
            foreignKey = @ForeignKey(
                    name = "store_product_fk"
            )
    )
    private DataStore store;

    public DataProduct() {
        // DO NOT DELETE
    }

    public DataProduct(String productName, String category, double price, int quantity) {
        this.productName = productName;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
    }

    public Integer getProductID() {
        return productID;
    }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public DataStore getStore() {
        return store;
    }

    public void setStore(DataStore store) {
        this.store = store;
    }
}