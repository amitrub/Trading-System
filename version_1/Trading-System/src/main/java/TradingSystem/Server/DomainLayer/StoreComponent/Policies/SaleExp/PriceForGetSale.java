package TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.concurrent.ConcurrentHashMap;

public class PriceForGetSale extends SimpleExpression {

    Double priceForSale;

    public PriceForGetSale(Double priceForSale) {
        this.priceForSale = priceForSale;
    }

    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID){
        return finalPrice>=priceForSale;
    }

    @Override
    public Response checkValidity(int storeID) {
        if(0>priceForSale){
            return new Response(true, "priceForSale cant be negative");
        }
        return new Response("correct");
    }
}
