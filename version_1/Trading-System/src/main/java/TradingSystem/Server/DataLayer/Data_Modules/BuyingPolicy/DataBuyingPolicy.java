package TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy;

import TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy.DataLimitExpression.BuyingPolicyKey;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import javax.persistence.*;

@Entity
public class DataBuyingPolicy {

//    @EmbeddedId
//    @Column(name = "buying_id")
//    private BuyingPolicyKey key;

    @Id
    @JoinColumn(
            name = "store_id",
            nullable = false,
            referencedColumnName = "storeID",
            foreignKey = @ForeignKey(
                    name = "store_id_fk"
            )
    )
    int store_id;
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

    public DataBuyingPolicy(){

    }

    public DataBuyingPolicy(int store_id, DataExpression expression){
        this.store_id = store_id;
        this.expression=expression;
    }

    public DataBuyingPolicy(BuyingPolicy buyingPolicy){
        this.store_id=buyingPolicy.getStoreID();
        this.expression=new DataExpression(buyingPolicy.getExp());
    }

    public DataExpression getExpression(){
        return expression;
    }

}
