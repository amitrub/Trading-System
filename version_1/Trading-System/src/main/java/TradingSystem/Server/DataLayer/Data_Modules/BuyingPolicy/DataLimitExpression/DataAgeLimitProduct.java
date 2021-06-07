package TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataLimitExpression;

import TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataSimpleExpression;
import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.AgeLimitForCategory;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.AgeLimitForProduct;

import javax.persistence.*;

@Entity
public class DataAgeLimitProduct extends DataSimpleExpression {
    @Column(
            name = "minAge"
    )
    Integer minAge;
    @JoinColumn(
            name = "product_id",
            nullable = false,
            referencedColumnName = "productID",
            foreignKey = @ForeignKey(
                    name = "prodcut_id_fk"
            )
    )
    int productid;

    public DataAgeLimitProduct(){

    }

    public DataAgeLimitProduct(int minAge,int product){
        this.minAge=minAge;
        this.productid=product;
    }

    public DataAgeLimitProduct(AgeLimitForProduct ageLimitForProduct){
        this.minAge=ageLimitForProduct.getMinAge();
        this.minAge=ageLimitForProduct.getProductID();
    }
}
