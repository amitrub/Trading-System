package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions;

import java.util.concurrent.ConcurrentHashMap;

public class Conditioning extends SimpleExpression{

    Expression condIf;
    Expression cond;

    public Conditioning(Expression condIf, Expression cond) {
        this.condIf = condIf;
        this.cond = cond;
    }

    public Conditioning(){
    }

    public void setCond(Expression cond) {
        this.cond = cond;
    }

    public void setCondIf(Expression condIf) {
        this.condIf = condIf;
    }

    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        if(!condIf.evaluate(products,finalPrice, userID,storeID )){
            return cond.evaluate(products,finalPrice, userID,storeID );
        }
        return true;
    }
}
