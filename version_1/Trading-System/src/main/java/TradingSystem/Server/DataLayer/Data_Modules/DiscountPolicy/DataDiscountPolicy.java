package TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy;

import TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataLimitExpression.BuyingPolicyKey;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;

import javax.persistence.*;

@Entity
public class DataDiscountPolicy {

    @EmbeddedId
    private DataSalePolicyKey key;

    @ManyToOne
    @JoinColumn(
            name = "store",
            nullable = false,
            referencedColumnName = "storeID",
            foreignKey = @ForeignKey(
                    name = "store_id_fk"
            )
    )
    DataStore store;
    @ManyToOne
    @JoinColumn(
            name = "sale",
            nullable = false,
            referencedColumnName = "SaleID",
            foreignKey = @ForeignKey(
                    name = "sale_id_fk"
            )
    )
    DataSale sale;
}
