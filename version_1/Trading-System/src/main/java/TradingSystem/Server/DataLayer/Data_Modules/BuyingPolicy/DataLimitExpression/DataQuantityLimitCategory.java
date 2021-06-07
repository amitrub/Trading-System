package TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataLimitExpression;

import TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataSimpleExpression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimitForCategory;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class DataQuantityLimitCategory extends DataSimpleExpression {
    @Column(
            name = "maxQuantity"
    )
    Integer maxQuantity;
    String  category;

    public DataQuantityLimitCategory(){

    }

    public DataQuantityLimitCategory(Integer maxQuantity, String category){
        this.maxQuantity=maxQuantity;
        this.category=category;
    }

    public DataQuantityLimitCategory(QuantityLimitForCategory quantityLimitForCategory){
        this.maxQuantity=quantityLimitForCategory.getMaxQuantity();
        this.category=quantityLimitForCategory.getCategory();
    }
}
