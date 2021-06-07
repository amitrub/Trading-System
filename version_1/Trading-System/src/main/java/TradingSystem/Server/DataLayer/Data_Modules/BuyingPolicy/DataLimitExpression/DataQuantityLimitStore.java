package TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataLimitExpression;

import TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataSimpleExpression;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimitForStore;

import javax.persistence.*;

@Entity
public class DataQuantityLimitStore extends DataSimpleExpression {
    @Column(
            name = "maxQuantity"
    )
    Integer maxquantity;
    @JoinColumn(
            name = "store_id",
            nullable = false,
            referencedColumnName = "storeID",
            foreignKey = @ForeignKey(
                    name = "store_id_fk"
            )
    )
    Integer storeId;

    public DataQuantityLimitStore(){

    }

    public DataQuantityLimitStore(QuantityLimitForStore quantityLimitForStore){
        this.maxquantity=quantityLimitForStore.getMaxQuantity();
        this.storeId=quantityLimitForStore.getStoreID();
    }
}
