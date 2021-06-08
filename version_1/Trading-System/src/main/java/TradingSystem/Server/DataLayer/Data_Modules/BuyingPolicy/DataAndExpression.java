package TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.AndComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class DataAndExpression extends DataCompositeExpression {

    public DataAndExpression(){

    }

    public DataAndExpression(AndComposite andExpression){
        this.expressionDataList=new ArrayList<>();
        for(Expression expression: andExpression.getChildren()){
            this.expressionDataList.add(new DataExpression(expression));
        }
    }
}
