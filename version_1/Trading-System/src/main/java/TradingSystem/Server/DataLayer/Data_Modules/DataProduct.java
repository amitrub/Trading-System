package TradingSystem.Server.DataLayer.Data_Modules;

import javax.persistence.*;

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

//    @ManyToOne
//    @JoinColumn(
//            name = "shopping_bag",
////            nullable = false,
//            referencedColumnName = "shoppingbagid",
//            foreignKey = @ForeignKey(
//                    name = "shopping_bag_id_FK"
//            )
//    )
//    private DataShoppingBagCart shopping_bag;

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

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getProductID() {
        return productID;
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

    @Override
    public String toString() {
        return "DataProduct{" +
                "productID=" + productID +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
                ", store=" + store.getStoreID() +
                '}';
    }
}
