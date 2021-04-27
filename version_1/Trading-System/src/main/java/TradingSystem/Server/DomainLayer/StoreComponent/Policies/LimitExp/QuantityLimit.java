package TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class QuantityLimit implements Expression {

    Integer maxQuantity;

    public QuantityLimit(Integer productID, Integer quantity) {
        maxQuantity = quantity;
    }

    @Override
    public Expression add(Expression expr) {
        return this;
    }

    //ToDo check!
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID){
        if(products.isEmpty()){
            return true;
        }
        Integer quantity=0;
        Set<Integer> keySet=products.keySet();
        for (Integer key: keySet
             ) {
            quantity=quantity+products.get(key);
        }
        return quantity<= maxQuantity;
    }
}
