package TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

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
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID, int mode) {
        if (!products.isEmpty()) {
            int quantity = 0;
            for (Integer key : products.keySet()) {
                quantity = quantity + products.get(key);
            }
            return maxQuantity >= quantity;
        }
        return true;
    }

    @Override
    public Response checkValidity(int storeID) {
        if(0>maxQuantity){
            return new Response(true, "maxQuantity cant be negative");
        }
        return new Response("correct");
    }

    public Integer getMaxQuantity(){
        return maxQuantity;
    }

    public Integer getStoreID(){
        return storeID;
    }
}
