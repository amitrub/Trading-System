package TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import java.util.concurrent.ConcurrentHashMap;

public class AgeLimit implements Expression {

    Integer minAge;

    public AgeLimit(Integer minAge) {
        this.minAge = minAge;
    }

    @Override
    public Expression add(Expression expr) {
        return this;
    }

    @Override
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        if(products.isEmpty()){
            return true;
        }
        return userID>=minAge;
    }
}
