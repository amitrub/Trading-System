package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions;

import java.util.concurrent.ConcurrentHashMap;

public interface Expression {
    Expression add(Expression expr);
    Expression set(String name, Boolean value);
    Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice);
}
