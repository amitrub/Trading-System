package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions;

import java.util.concurrent.ConcurrentHashMap;

public class PriceExp implements Expression{

    Double priceForSale;

    public PriceExp(Double priceForSale) {
        this.priceForSale = priceForSale;
    }

    public Expression add(Expression expr){ return this; }

    public Expression set(String name, Boolean value){
        return this;
    }

    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice){
        return finalPrice>=priceForSale;
    }
}
