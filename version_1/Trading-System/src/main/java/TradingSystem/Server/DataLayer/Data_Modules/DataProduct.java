package TradingSystem.Server.DataLayer.Data_Modules;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

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
            name = "store_id",
            nullable = false,
            referencedColumnName = "storeID",
            foreignKey = @ForeignKey(
                    name = "store_product_fk"
            )
    )
    private DataStore store;

    @OneToMany(
            mappedBy = "product",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.EAGER
    )
    private List<DataComment> comments = new ArrayList<>();

    public DataProduct() {
        // DO NOT DELETE
    }

    public DataProduct(String productName, String category, double price, int quantity) {
        this.productName = productName;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
    }

    public void AddComment(DataComment comment) {
        if (!this.comments.contains(comment)) {
            this.comments.add(comment);
        }
    }

    public List<DataComment> getComments() {
        return comments;
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
    public void setReduceQuantity(int quantity) {
        this.quantity = this.quantity-quantity;
    }

    public DataStore getStore() {
        return store;
    }

    public void setStore(DataStore store) {
        this.store = store;
    }

    @Override
    public String toString() {
        return "DataProduct{" +
                "productID=" + productID +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                ", store=" + store.getStoreID() +
                ", comments=" + comments +
                '}';
    }
}
