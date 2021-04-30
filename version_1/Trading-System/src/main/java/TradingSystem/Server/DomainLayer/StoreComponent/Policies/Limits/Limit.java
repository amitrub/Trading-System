package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Limits;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import java.util.concurrent.ConcurrentHashMap;

public interface Limit {
    public Boolean checkEntitlement(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID);
    public Integer getID();
    public Expression setExpression(Expression exp);
    public Expression addExpression(Expression exp);
    public Expression setExpression(Integer expId, Expression exp);
}
