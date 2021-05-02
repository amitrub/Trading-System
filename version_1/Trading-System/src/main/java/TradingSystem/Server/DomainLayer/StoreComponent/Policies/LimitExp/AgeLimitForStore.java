package TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;

import java.util.concurrent.ConcurrentHashMap;

public class AgeLimitForStore extends SimpleExpression {

    Integer minAge;
    Integer storeID;

    public AgeLimitForStore(Integer minAge,Integer storeID) {
        this.storeID=storeID;
        this.minAge = minAge;
    }

    @Override
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        if(!products.isEmpty()){
            return userID>=minAge;
        }
        return true;
    }
}
