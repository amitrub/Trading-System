package TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataLimitExpression;

import TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataSimpleExpression;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class DataQuantityLimitCategory extends DataSimpleExpression {
    @Column(
            name = "maxQuantity"
    )
    Integer maxQuantity;
    String  category;
}
