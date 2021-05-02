package TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;

import java.util.concurrent.ConcurrentHashMap;

public class AgeLimitForProduct extends SimpleExpression {

    Integer minAge;
    Integer productID;

    public AgeLimitForProduct(Integer minAge,Integer productID) {
      //  super(expId);
        this.productID=productID;
        this.minAge = minAge;
    }

    @Override
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        if(products.get(productID)!=null){
            return userID>=minAge;
        }
        return true;
    }
}
