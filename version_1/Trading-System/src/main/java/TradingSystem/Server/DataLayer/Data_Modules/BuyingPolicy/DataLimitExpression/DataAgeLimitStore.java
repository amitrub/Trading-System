package TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataLimitExpression;

import TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataSimpleExpression;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.AgeLimitForStore;

import javax.persistence.*;

@Entity
public class DataAgeLimitStore extends DataSimpleExpression {
    @Column(
            name = "minAge"
    )
    Integer minAge;
    @JoinColumn(
            name = "store_id",
            nullable = false,
            referencedColumnName = "storeID",
            foreignKey = @ForeignKey(
                    name = "store_id_fk"
            )
    )
    int storeId;

    public DataAgeLimitStore(){

    }

    public DataAgeLimitStore(int minAge,int store){
        this.minAge=minAge;
        this.storeId=store;
    }

    public DataAgeLimitStore(AgeLimitForStore ageLimitForStore){
        this.minAge=ageLimitForStore.getMinAge();
        this.storeId=ageLimitForStore.getStoreID();
    }
}
