package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions;

import java.util.concurrent.ConcurrentHashMap;

public class QuantityExp implements Expression{
    Integer  productID;
    Integer quantityForSale;

    public QuantityExp(Integer productID, Integer quantityForSale) {
        this.productID = productID;
        this.quantityForSale = quantityForSale;
    }

    public Expression add(Expression expr){ return this; }

    public Expression set(String name, Boolean value){
        return this;
    }

    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products,Double finalPrice){
        if(!products.contains(productID)){
            return false;
        }
        return products.get(productID)>=quantityForSale;
    }

}
