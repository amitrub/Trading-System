package TradingSystem.Server.DataLayer.Data_Modules.ShoppingHistory;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "HistoryProducts")
@Table(
        name = "history_product"
//        uniqueConstraints = {
//                @UniqueConstraint(name = "store_name_unique", columnNames = "name")
//        }
)
public class DataHistoryProduct {

    @Id
    @SequenceGenerator(
            name = "HISTORY_PRODUCT_SEQUENCE",
            sequenceName = "HISTORY_PRODUCT_SEQUENCE",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "HISTORY_PRODUCT_SEQUENCE"
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
            name = "shopping_history_id",
            nullable = false,
            referencedColumnName = "shoppingHistoryID",
            foreignKey = @ForeignKey(
                    name = "shopping_history_product_fk"
            )
    )
    private DataShoppingHistory shoppingHistory;

    public DataHistoryProduct() {
        // DO NOT DELETE
    }

    public DataHistoryProduct(String productName, String category, double price, int quantity) {
        this.productName = productName;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
    }

    public DataHistoryProduct(Product product) {
        this.productName = product.getProductName();
        this.price = product.getPrice();
        this.category = product.getCategory();
        this.quantity = product.getQuantity();
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

    public DataShoppingHistory getShoppingHistory() {
        return shoppingHistory;
    }

    public void setShoppingHistory(DataShoppingHistory shoppingHistory) {
        this.shoppingHistory = shoppingHistory;
    }

    @Override
    public String toString() {
        return "DataProduct{" +
                "productID=" + productID +
                ", productName='" + productName + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                ", quantity=" + quantity +
//                ", store=" + shoppingHistory.() +
                '}';
    }
}
