package TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import java.util.concurrent.ConcurrentHashMap;

public class QuantityExp implements Expression {
    Integer productID;
    Integer quantityForSale;

    public QuantityExp(Integer productID, Integer quantityForSale) {
        this.productID = productID;
        this.quantityForSale = quantityForSale;
    }

    public Expression add(Expression expr){ return this; }

    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID){
        if(!products.contains(productID)){
            return false;
        }
        return products.get(productID)>=quantityForSale;
    }

}
