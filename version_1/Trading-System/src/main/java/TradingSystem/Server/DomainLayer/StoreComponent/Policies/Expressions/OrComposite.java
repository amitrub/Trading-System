package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions;

import java.util.concurrent.ConcurrentHashMap;

public class OrComposite extends CompositeExpression {

    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID){
        for(Expression expr : children) if(expr.evaluate(products, finalPrice, userID,storeID )) return true;
        return false;
    }
}
