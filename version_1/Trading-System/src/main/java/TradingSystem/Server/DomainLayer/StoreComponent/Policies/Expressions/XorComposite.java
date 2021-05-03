package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class XorComposite extends CompositeExpression {

    public XorComposite(List<Expression> c) {
        super(c);
    }

    public XorComposite(){
        super(new LinkedList<Expression>());
    }

    @Override
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        return null;
    }
}
