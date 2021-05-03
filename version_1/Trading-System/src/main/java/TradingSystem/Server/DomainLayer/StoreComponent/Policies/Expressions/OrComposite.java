package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class OrComposite extends CompositeExpression {


    public OrComposite(List<Expression> c) {
        super(c);
    }

    public OrComposite(){
        super(new LinkedList<Expression>());
    }

    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID){
        for(Expression expr : children)
            if(expr.evaluate(products, finalPrice, userID,storeID))
                return true;
        return false;
    }
}
