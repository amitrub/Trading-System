package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions;

import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;

public class XorComposite extends CompositeExpression {



    @Override
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products,Double finalPrice) {
        return null;
    }
}
