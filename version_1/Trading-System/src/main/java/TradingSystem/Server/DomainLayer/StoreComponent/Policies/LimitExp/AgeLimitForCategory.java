package TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AgeLimitForCategory extends SimpleExpression {

    Integer minAge;
    String  category;
    TradingSystem tradingSystem;

    public AgeLimitForCategory(Integer minAge,String category) {
        this.category=category;
        this.minAge = minAge;
        tradingSystem=TradingSystem.getInstance();
    }

    @Override
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        for (Integer key : products.keySet()
        ) {
            if(tradingSystem.getProduct(storeID, key).getCategory().equals(category)){
                return userID>=minAge;
            }
        }
        return true;
    }
}
