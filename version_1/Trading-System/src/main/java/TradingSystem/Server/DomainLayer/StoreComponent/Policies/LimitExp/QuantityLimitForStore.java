package TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;

import java.util.concurrent.ConcurrentHashMap;

public class QuantityLimitForStore extends SimpleExpression {

    Integer maxQuantity;
    Integer storeID;

    public QuantityLimitForStore(Integer quantity, Integer storeID) {
        this.storeID = storeID;
        maxQuantity = quantity;
    }

    @Override
    public Expression add(Expression expr) {
        return this;
    }

    //ToDo check!
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        if (!products.isEmpty()) {
            int quantity = 0;
            for (Integer key : products.keySet()) {
                quantity = quantity + products.get(key);
            }
            return maxQuantity >= quantity;
        }
        return true;
    }
}
