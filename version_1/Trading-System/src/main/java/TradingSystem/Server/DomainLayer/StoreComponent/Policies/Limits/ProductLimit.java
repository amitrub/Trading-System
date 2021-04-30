package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Limits;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import java.util.concurrent.ConcurrentHashMap;

public class ProductLimit implements Limit{

    private Integer limitId;
    private Integer productID;
    private Expression expression;

    public ProductLimit(Integer limitID,Integer productID, Expression expression) {
        this.limitId=limitID;
        this.productID = productID;
        this.expression = expression;
    }

    @Override
    public Boolean checkEntitlement(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        ConcurrentHashMap<Integer,Integer> productToCheck=new ConcurrentHashMap<Integer, Integer>();
        if(products.get(productID)!=null){
            productToCheck.put(productID,products.get(productID));
        }
        return expression.evaluate(productToCheck,finalPrice,userID, storeID);
    }

    @Override
    public Integer getID() {
        return this.limitId;
    }

    @Override
    public Expression setExpression(Expression exp) {
        return null;
    }

    @Override
    public Expression addExpression(Expression exp) {
        return null;
    }

    @Override
    public Expression setExpression(Integer expId, Expression exp) {
        return null;
    }
}
