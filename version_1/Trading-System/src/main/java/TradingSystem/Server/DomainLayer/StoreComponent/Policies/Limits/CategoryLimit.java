package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Limits;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CategoryLimit implements Limit{

    private Integer limitId;
    private String category;
    private Expression expression;
    private TradingSystem tradingSystem=TradingSystem.getInstance();

    public CategoryLimit(Integer limitID, String category, Expression expression) {
        this.limitId=limitID;
        this.category = category;
        this.expression = expression;
    }

    @Override
    public Boolean checkEntitlement(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        ConcurrentHashMap<Integer, Integer> productToCheck = new ConcurrentHashMap<Integer, Integer>();
        Set<Integer> keySet = products.keySet();
        for (Integer key : keySet
        ) {
            Product p = tradingSystem.getProduct(storeID, key);
            if (p.getCategory().equals(category)) {
                productToCheck.put(key, products.get(key));

            }
        }
            return expression.evaluate(productToCheck, finalPrice, userID, storeID);
    }



    @Override
    public Integer getID() {
        return this.limitId;
    }

    @Override
    public Expression setExpression(Expression exp) {
       this.expression=exp;
       return this.expression;
    }

    @Override
    public Expression addExpression(Expression exp) {
        if (this.expression != null) {
           return this.expression.add(exp);
        } else {
           this.expression = exp;
           return this.expression;
        }

    }

    @Override
    public Expression setExpression(Integer expId, Expression exp) {
        return null;
    }
}
