package TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataLimitExpression;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class BuyingPolicyKey implements Serializable {

    @Column(name = "expression_id")
    private int expressionid;

    @Column(name = "store_id")
    private int storeID;
}
