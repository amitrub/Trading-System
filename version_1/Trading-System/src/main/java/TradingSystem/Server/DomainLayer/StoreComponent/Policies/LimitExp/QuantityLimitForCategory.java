package TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;

import java.util.concurrent.ConcurrentHashMap;

public class QuantityLimitForCategory extends SimpleExpression {

    Integer maxQuantity;
    String  category;
    TradingSystem tradingSystem;

    public QuantityLimitForCategory(Integer maxQuantity,String category) {
        this.category=category;
        this.maxQuantity = maxQuantity;
        tradingSystem=TradingSystem.getInstance();
    }

    @Override
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        Integer quantity = 0;
        for (Integer key : products.keySet()
        ) {
            if (tradingSystem.getProduct(storeID, key).getCategory().equals(category)) {
                quantity = quantity + products.get(key);
            }
        }
        return maxQuantity>=quantity;
    }
    }
