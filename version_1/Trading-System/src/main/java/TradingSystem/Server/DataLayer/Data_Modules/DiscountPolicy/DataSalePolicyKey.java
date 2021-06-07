package TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy;

import TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataExpression;
import TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataLimitExpression.BuyingPolicyKey;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class DataSalePolicyKey implements Serializable {

    @Column(name = "sale_id")
    private int sale;

    @Column(name = "store_id")
    private int storeID;
}
