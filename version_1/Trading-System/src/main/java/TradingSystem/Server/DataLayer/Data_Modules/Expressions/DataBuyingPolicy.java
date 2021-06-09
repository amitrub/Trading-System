package TradingSystem.Server.DataLayer.Data_Modules.Expressions;

import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DBExpression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.BuyingPolicy;

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
            referencedColumnName = "parent",
            foreignKey = @ForeignKey(
                    name = "expression_id_fk"
            )
    )
    DBExpression expression;

    public DataBuyingPolicy(){

    }

    public DataBuyingPolicy(int store_id, DBExpression expression){
        this.store_id = store_id;
        this.expression=expression;
    }

    public DataBuyingPolicy(BuyingPolicy buyingPolicy){
        this.store_id=buyingPolicy.getStoreID();
        this.expression=new DBExpression(buyingPolicy.getExp(),new DBExpression());
    }

    public DBExpression getExpression(){
        return expression;
    }
    public void setExp(DBExpression expression){
        this.expression=expression;
    }

}
