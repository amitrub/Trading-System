package TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.concurrent.ConcurrentHashMap;

public class AgeLimitForProduct extends SimpleExpression {

    private Integer minAge;
    private Integer productID;

    public AgeLimitForProduct(Integer minAge,Integer productID) {
      //  super(expId);
        this.productID=productID;
        this.minAge = minAge;
    }

    @Override
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        if(products.get(productID)!=null){
            return userID>=minAge;
        }
        return true;
    }

    @Override
    public Response checkValidity(int storeID) {
        if(0>minAge){
            return new Response(true, "minAge cant be negative");
        }
        if(this.tradingSystem.stores.get(storeID).getProduct(productID)==null){
            return new Response(true, "product is not exist in the store");
        }
        return new Response("correct");
    }

    public Integer getMinAge() {
        return minAge;
    }

    public Integer getProductID(){
        return productID;
    }
}
