package TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import java.util.concurrent.ConcurrentHashMap;

public class PriceExp implements Expression {

    Double priceForSale;

    public PriceExp(Double priceForSale) {
        this.priceForSale = priceForSale;
    }

    public Expression add(Expression expr){ return this; }

    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID){
        return finalPrice>=priceForSale;
    }
}
