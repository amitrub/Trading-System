package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions;

import java.util.concurrent.ConcurrentHashMap;

public abstract class SimpleExpression  implements Expression{

    private Integer expressionID;

    public SimpleExpression(Integer expressionID) {
        this.expressionID = expressionID;
    }

    public abstract Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID);

    @Override
    public Expression add(Expression expr) {
        return this;
    }



    /**
     *implement with ID
     **/

    @Override
    public Expression setExpression(Integer expID, Expression exp) {
       if(this.expressionID==expID){
           return this;
       }
        return null;
    }

    @Override
    public Expression getExpression(Integer id) {
        if(this.expressionID==id){
            return this;
        }
        return null;
    }

    @Override
    public Integer getExpressionId() {
        return this.expressionID;
    }
}
