package TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.concurrent.ConcurrentHashMap;

public class AgeLimitForStore extends SimpleExpression {

    private Integer minAge;
    private Integer storeID;

    public AgeLimitForStore(Integer minAge,Integer storeID) {
        this.storeID=storeID;
        this.minAge = minAge;
    }

    @Override
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID, int mode) {
        if(!products.isEmpty()){
            return userID>=minAge;
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

    public Integer getMinAge(){
        return minAge;
    }
    public Integer getStoreID(){
        return storeID;
    }
}
