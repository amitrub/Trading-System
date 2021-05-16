package TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AgeLimitForCategory extends SimpleExpression {

    Integer minAge;
    String  category;
    TradingSystemImpl tradingSystem;

    public AgeLimitForCategory(Integer minAge,String category) {
        this.category=category;
        this.minAge = minAge;
        tradingSystem= TradingSystemImpl.getInstance();
    }

    @Override
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        for (Integer key : products.keySet()
        ) {
            if(tradingSystem.getProduct(storeID, key).getCategory().equals(category)){
                return userID>=minAge;
            }
        }
        return true;
    }

    @Override
    public Response checkValidity(int storeID) {
        if(0>minAge){
            return new Response(true, "minAge cant be negative");
        }
        return new Response("correct");
    }
}
