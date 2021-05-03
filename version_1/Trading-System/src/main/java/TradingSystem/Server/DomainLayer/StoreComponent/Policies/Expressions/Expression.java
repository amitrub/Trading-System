package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions;

import java.util.concurrent.ConcurrentHashMap;

public interface Expression {

    public Expression add(Expression expr);

    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID);
    
}

