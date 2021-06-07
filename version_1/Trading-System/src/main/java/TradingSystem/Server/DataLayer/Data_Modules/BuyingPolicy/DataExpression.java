package TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.CompositeExpression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class DataExpression {
    @Id
    @SequenceGenerator(
            name = "Expression_SEQUENCE",
            sequenceName = "Expression_SEQUENCE",
            allocationSize = 1
    )
    @Column(
            name = "ExpressionID"
    )
    int ExpressionId;
    @ManyToOne
    @JoinColumn(
            name = "Data_Expression_ID",
            nullable = false,
            referencedColumnName = "ExpressionID",
            foreignKey = @ForeignKey(
                    name = "data_expression_id_fk"
            )
    )
    DataCompositeExpression composite_data_expression;
    public DataExpression(){

    }
    public DataExpression(Expression expression){

    }
}
