package TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataLimitExpression;

import TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataSimpleExpression;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;

import javax.persistence.*;

@Entity
public class DataQuantityLimitStore extends DataSimpleExpression {
    @Column(
            name = "maxQuantity"
    )
    Integer maxquantity;
    @ManyToOne
    @JoinColumn(
            name = "store_id",
            nullable = false,
            referencedColumnName = "storeID",
            foreignKey = @ForeignKey(
                    name = "store_id_fk"
            )
    )
    DataStore storeId;
}
