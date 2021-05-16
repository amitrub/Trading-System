package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.ConditionRoles.ConditionRole;

import java.util.concurrent.ConcurrentHashMap;

public class Conditioning extends SimpleExpression{

    Expression condIf;
    ConditionRole cond;

    public Conditioning(Expression condIf, ConditionRole cond) {
        this.condIf = condIf;
        this.cond = cond;
    }

    public Conditioning(){
    }

    public void setCond(ConditionRole cond) {
        this.cond = cond;
    }

    public void setCondIf(Expression condIf) {
        this.condIf = condIf;
    }

    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        if(!condIf.evaluate(products,finalPrice, userID,storeID )){
            return cond.checkRole(products,finalPrice, userID,storeID );
        }
        return true;
    }
}
