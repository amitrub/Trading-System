package TradingSystem.Server.DataLayer.Data_Modules.BuyingPolicy;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.AndComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.OrComposite;

import javax.persistence.Entity;
import java.util.ArrayList;

@Entity
public class DataOrExpression extends DataCompositeExpression {

    public DataOrExpression(){

    }

    public DataOrExpression(OrComposite orExpression){
        this.expressionDataList=new ArrayList<>();
        for(Expression expression: orExpression.getChildren()){
            this.expressionDataList.add(new DataExpression(expression));
        }
    }
}
