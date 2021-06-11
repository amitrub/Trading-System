package TradingSystem.Server.DataLayer.Data_Modules;

import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserProductKey;
import javax.persistence.*;


@Entity(name = "Comments")
@Table(
        name = "comments"
)
public class DataComment {

    @EmbeddedId
    private UserProductKey key;

    @ManyToOne
    @MapsId("subscriberID")
    @JoinColumn(
            name = "subscriber_id",
            nullable = false,
            referencedColumnName = "userID",
            foreignKey = @ForeignKey(
                    name = "subscriber_comment_fk"
            )
    )
    private DataSubscriber subscriber;

    @ManyToOne
    @MapsId("storeID")
    @JoinColumn(
            name = "product_id",
            nullable = false,
            referencedColumnName = "productID",
            foreignKey = @ForeignKey(
                    name = "product_comment_fk"
            )
    )
    private DataProduct product;

    @Column(
            name = "comment",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String comment;

    public DataComment(){
        // DO NOT DELETE
    }

    public DataComment(DataSubscriber subscriber, DataProduct product, String comment) {
        this.key = new UserProductKey(subscriber.getUserID(), product.getProductID());
        this.subscriber = subscriber;
        this.product = product;
        this.comment = comment;
    }

    public DataSubscriber getSubscriber() {
        return subscriber;
    }

    public DataProduct getProduct() {
        return product;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "DataComment{" +
                "key=" + key +
                ", comment='" + comment + '\'' +
                '}';
    }
}
