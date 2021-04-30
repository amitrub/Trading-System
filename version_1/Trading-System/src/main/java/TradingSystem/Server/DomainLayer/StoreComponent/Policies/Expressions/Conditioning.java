package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions;

import java.util.concurrent.ConcurrentHashMap;

public class Conditioning extends SimpleExpression{

    Expression cond;
    Expression condIf;


    public Conditioning(Integer expID,Expression cond, Expression condIf) {
        super(expID);
        this.cond = cond;
        this.condIf = condIf;
    }

    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        if(!cond.evaluate(products,finalPrice, userID,storeID )){
            return condIf.evaluate(products,finalPrice, userID,storeID );
        }
        return true;
    }
}
