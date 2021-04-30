package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Limits;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import java.util.concurrent.ConcurrentHashMap;

public class StoreLimit implements Limit{

    private Integer limitId;
    private Integer storeID;
    private Expression expression;

    public StoreLimit(Integer limitId,Integer storeID, Expression expression) {
        this.limitId=limitId;
        this.storeID = storeID;
        this.expression = expression;
    }

    @Override
    public Boolean checkEntitlement(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        return expression.evaluate(products,finalPrice,userID,storeID );
    }

    @Override
    public Integer getID() {
        return this.limitId;
    }

    @Override
    public Expression setExpression(Expression exp) {
        return null;
    }

    @Override
    public Expression addExpression(Expression exp) {
        return null;
    }

    @Override
    public Expression setExpression(Integer expId, Expression exp) {

        return null;
    }
}
