package TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataLimitExpression;

import TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataSimpleExpression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.AgeLimitForCategory;

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

    public DataAgeLimitCategory(){

    }

    public DataAgeLimitCategory(int minAge, String category){
        super();
        this.minAge=minAge;
        this.category=category;
    }

    public DataAgeLimitCategory(AgeLimitForCategory ageLimitForCategory){
        this.minAge=ageLimitForCategory.minAge;
        this.category= ageLimitForCategory.category;
    }
}
