package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions;

import java.util.concurrent.ConcurrentHashMap;

public class XorComposite extends CompositeExpression {

    public XorComposite(Integer exp) {
        super(exp);
    }

    @Override
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        return null;
    }
}
