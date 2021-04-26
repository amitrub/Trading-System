package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions;

import java.util.concurrent.ConcurrentHashMap;

public class AndComposite extends CompositeExpression {

    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products,Double finalPrice){

        for(Expression expr : children) if(!expr.evaluate(products, finalPrice)) return false;
        return true;
    }
}
