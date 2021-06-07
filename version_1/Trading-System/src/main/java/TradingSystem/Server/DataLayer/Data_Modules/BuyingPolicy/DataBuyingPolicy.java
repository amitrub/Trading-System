package TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy;

import TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataLimitExpression.BuyingPolicyKey;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;

import javax.persistence.*;

@Entity
public class DataBuyingPolicy {

    @EmbeddedId
    @Column(name = "buying_id")
    private BuyingPolicyKey key;

    @ManyToOne
    @JoinColumn(
            name = "store",
            nullable = false,
            referencedColumnName = "storeID",
            foreignKey = @ForeignKey(
                    name = "store_id_fk"
            )
    )
    DataStore store;
    @ManyToOne
    @JoinColumn(
            name = "expression",
            nullable = false,
            referencedColumnName = "ExpressionID",
            foreignKey = @ForeignKey(
                    name = "expression_id_fk"
            )
    )
    DataExpression expression;
//    @ManyToOne
//    @JoinColumn(
//            name = "expression_id",
//            nullable = false,
//            referencedColumnName = "ExpressionID",
//            foreignKey = @ForeignKey(
//                    name = "expression_id_fk"
//            )
//    )
//    DataSimpleExpression simple_expression;
}
