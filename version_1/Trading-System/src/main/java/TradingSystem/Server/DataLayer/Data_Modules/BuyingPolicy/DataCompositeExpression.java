package TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy;

import TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy.DataSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.CompositeExpression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class DataCompositeExpression extends DataExpression {
    @OneToMany(
            mappedBy = "composite_data_expression",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    List<DataExpression> expressionDataList;

    public DataCompositeExpression(){

    }

    public DataCompositeExpression(List<DataExpression> expressionDataList){
        super();
        this.expressionDataList=expressionDataList;
    }

    public DataCompositeExpression(CompositeExpression expression){
        super();
        this.expressionDataList=new ArrayList<>();
        for(Expression expression1:expression.children){
            this.expressionDataList.add(new DataExpression(expression1));
        }
    }

    public List<DataExpression> getExpressionDataList(){
        return this.expressionDataList;
    }
}
