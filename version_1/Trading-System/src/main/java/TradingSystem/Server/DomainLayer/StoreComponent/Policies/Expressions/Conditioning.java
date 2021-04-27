package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions;

import java.util.concurrent.ConcurrentHashMap;

public class Conditioning implements Expression {

    Expression cond;
    Expression condIf;

    public Conditioning(Expression cond, Expression condIf) {
        this.cond = cond;
        this.condIf = condIf;
    }

    @Override
    public Expression add(Expression expr) {
        return this;
    }

    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        if(!cond.evaluate(products,finalPrice, userID,storeID )){
            return condIf.evaluate(products,finalPrice, userID,storeID );
        }
        return true;
    }
}
