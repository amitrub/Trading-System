package TradingSystem.Server.DataLayer.Data_Modules.Expressions;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DBExpression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.BuyingPolicy;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "Buying_Policy")
public class DataBuyingPolicy {
    @Id
    @SequenceGenerator(
            name = "Buying_SEQUENCE",
            sequenceName = "Buying_SEQUENCE",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "Buying_SEQUENCE"
    )
    @Column(
            name = "BuyingID"
    )
    private Integer BuyingID;

//    @EmbeddedId
//    @Column(name = "buying_id")
//    private BuyingPolicyKey key;

  //  @OneToOne
    @JoinColumn(
            name = "store_id",
            nullable = false,
            referencedColumnName = "storeID",
            foreignKey = @ForeignKey(
                    name = "store_id_fk"
            )
    )
    Integer store_id;
    @ManyToOne
    @JoinColumn(
            name = "expression",
            nullable = false,
            referencedColumnName = "ExpressionID",
            foreignKey = @ForeignKey(
                    name = "expression_id_fk"
            )
    )
    DBExpression expression;

    public DataBuyingPolicy(){

    }

    public DataBuyingPolicy(Integer store_id, DBExpression expression){
        this.store_id = store_id;
        this.expression=expression;
    }

//    public DataBuyingPolicy(BuyingPolicy buyingPolicy){
//        this.store_id=buyingPolicy.getStoreID();
//        this.expression=new DBExpression(buyingPolicy.getExp(),new DBExpression());
//    }

    public DBExpression getExpression(){
        return expression;
    }
    public void setExp(DBExpression expression){
        this.expression=expression;
    }

    public Integer getStore() {
        return store_id;
    }
}
