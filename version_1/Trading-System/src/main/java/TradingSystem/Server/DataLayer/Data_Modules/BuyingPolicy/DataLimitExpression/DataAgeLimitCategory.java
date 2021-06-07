package TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataLimitExpression;

import TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataSimpleExpression;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class DataAgeLimitCategory extends DataSimpleExpression {
    @Column(
            name = "minAge"
    )
    Integer minAge;
    @Column(
            name = "category"
    )
    String  category;
}
