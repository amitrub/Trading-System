package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Limits;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CategoryLimit implements Limit{

    String category;
    Expression expression;
    TradingSystemImpl tradingSystemImpl = TradingSystemImpl.getInstance();

    public CategoryLimit(String category, Expression expression) {
        this.category = category;
        this.expression = expression;
    }

    @Override
    public Boolean checkEntitlement(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        ConcurrentHashMap<Integer, Integer> productToCheck = new ConcurrentHashMap<Integer, Integer>();
        Set<Integer> keySet = products.keySet();
        for (Integer key : keySet
        ) {
            Product p = tradingSystemImpl.getProduct(storeID, key);
            if (p.getCategory().equals(category)) {
                productToCheck.put(key, products.get(key));

            }
        }
            return expression.evaluate(productToCheck, finalPrice, userID, storeID);
    }
}
