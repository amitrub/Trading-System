package TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataLimitExpression;

import TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataSimpleExpression;
import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimitForCategory;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimitForProduct;

import javax.persistence.*;

@Entity
public class DataQuantityLimitProduct extends DataSimpleExpression {
    @Column(
            name = "maxQuantity"
    )
    Integer maxQuantity;
    @JoinColumn(
            name = "product_id",
            nullable = false,
            referencedColumnName = "productID",
            foreignKey = @ForeignKey(
                    name = "prodcut_id_fk"
            )
    )
    int product;

    public DataQuantityLimitProduct(){

    }

    public DataQuantityLimitProduct(QuantityLimitForProduct quantityLimitForProduct){
        this.maxQuantity=quantityLimitForProduct.getMaxQuantity();
        this.product=quantityLimitForProduct.getProductID();
    }
}
