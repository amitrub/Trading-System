package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Limits;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import java.util.concurrent.ConcurrentHashMap;

public class ProductLimit implements Limit{

    Integer productID;
    Expression expression;

    @Override
    public Boolean checkEntitlement(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        ConcurrentHashMap<Integer,Integer> productToCheck=new ConcurrentHashMap<Integer, Integer>();
        if(products.get(productID)!=null){
            productToCheck.put(productID,products.get(productID));
        }
        return expression.evaluate(productToCheck,finalPrice,userID, storeID);
    }
}
