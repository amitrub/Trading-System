package TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;

import java.util.concurrent.ConcurrentHashMap;

public class AgeLimit extends SimpleExpression {

    Integer minAge;

    public AgeLimit(Integer expId,Integer minAge) {
        super(expId);
        this.minAge = minAge;
    }

    @Override
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        if(products.isEmpty()){
            return true;
        }
        return userID>=minAge;
    }
}
