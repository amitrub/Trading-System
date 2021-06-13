package TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.concurrent.ConcurrentHashMap;

public class PriceForGetSale extends SimpleExpression {

    Integer priceForSale;

    public PriceForGetSale(Integer priceForSale) {
        this.priceForSale = priceForSale;
    }

    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID, int mode){
        return finalPrice>=priceForSale;
    }

    @Override
    public Response checkValidity(int storeID) {
        if(0>priceForSale){
            return new Response(true, "priceForSale cant be negative");
        }
        return new Response("correct");
    }

    public Integer getPriceForSale(){
        return priceForSale;
    }

    @Override
    public String toString(){
        return "price for sale "+priceForSale;
    }
}
