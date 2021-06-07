package TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataLimitExpression;

import TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataSimpleExpression;
import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;

import javax.persistence.*;

@Entity
public class DataAgeLimitProduct extends DataSimpleExpression {
    @Column(
            name = "minAge"
    )
    Integer minAge;
    @ManyToOne
    @JoinColumn(
            name = "product_id",
            nullable = false,
            referencedColumnName = "productID",
            foreignKey = @ForeignKey(
                    name = "prodcut_id_fk"
            )
    )
    DataProduct product;
}
