package TradingSystem.Server.DataLayer.Data_Modules.Bid;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.BidManagerKey;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserProductKey;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataManagerPermissions;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;


@Entity(name = "BidManagerApproves")
@Table(
        name = "bid_manager_approves"
)
public class DataBidManagerApproves {

    @EmbeddedId
    private BidManagerKey key;

    @ManyToOne
    @MapsId("key")
    @JoinColumns({@JoinColumn(
            name = "subscriber_id",
            nullable = false,
            referencedColumnName = "subscriber_id",
            foreignKey = @ForeignKey(
                    name = "bid_subscriber_fk"
            )),
            @JoinColumn(
                    name = "product_id",
                    nullable = false,
                    referencedColumnName = "product_id",
                    foreignKey = @ForeignKey(
                            name = "bid_product_fk"
                    )
            )
    })
    private DataBid bid;

    @ManyToOne
    @MapsId("managerID")
    @JoinColumn(
            name = "manager_id",
            nullable = false,
            referencedColumnName = "userID",
            foreignKey = @ForeignKey(
                    name = "bid_manager_fk"
            )
    )
    private DataSubscriber manager;

    @Column(
            name = "approves"
    )
    private boolean approves;

    public DataBidManagerApproves(){
        // DO NOT DELETE
    }

    public DataBidManagerApproves(DataBid bid, DataSubscriber manager, boolean approves) {
        this.key = new BidManagerKey(bid.getKey(), manager.getUserID());
        this.bid = bid;
        this.manager = manager;
        this.approves = approves;
    }

    public BidManagerKey getKey() {
        return key;
    }

    public DataBid getBid() {
        return bid;
    }

    public DataSubscriber getManager() {
        return manager;
    }

    public boolean isApproves() {
        return approves;
    }

    public void setApproves(boolean approves) {
        this.approves = approves;
    }
}
