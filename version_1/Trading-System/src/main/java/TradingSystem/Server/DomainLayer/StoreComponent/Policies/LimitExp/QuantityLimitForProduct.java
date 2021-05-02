package TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class QuantityLimitForProduct extends SimpleExpression {

    Integer maxQuantity;
    Integer productID;

    public QuantityLimitForProduct(Integer quantity,Integer productID) {
      this.productID=productID;
       maxQuantity = quantity;
    }

    @Override
    public Expression add(Expression expr) {
        return this;
    }

    //ToDo check!
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID){
        if(!products.isEmpty()) {
            if (products.get(productID) != null) {
                return  maxQuantity>=products.get(productID) ;
            }
        }
        return true;
    }
}
