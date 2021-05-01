package TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;

import java.util.concurrent.ConcurrentHashMap;

public class PriceForGetSale extends SimpleExpression {

    Double priceForSale;

    public PriceForGetSale(Integer expId, Double priceForSale) {
        super(expId);
        this.priceForSale = priceForSale;
    }

    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID){
        return finalPrice>=priceForSale;
    }
}