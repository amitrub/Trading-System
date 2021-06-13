package TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.concurrent.ConcurrentHashMap;

public class QuantityLimitForProduct extends SimpleExpression {

    Integer maxQuantity;
    Integer productID;

    public QuantityLimitForProduct(Integer quantity,Integer productID) {
      this.productID=productID;
       maxQuantity = quantity;
    }

    @Override
    public Expression add(Expression expr) {
        return this;
    }

    //ToDo check!
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID, int mode){
        if(!products.isEmpty()) {
            if (products.get(productID) != null) {
                int num=products.get(productID);
                return  maxQuantity >= num;
            }
        }
        return true;
    }

    @Override
    public Response checkValidity(int storeID) {
        if(0>maxQuantity){
            return new Response(true, "maxQuantity cant be negative");
        }
        if(this.tradingSystem.stores.get(storeID).getProduct(productID)==null){
            return new Response(true, "product is not exist in the store");
        }
        return new Response("correct");
    }

    public Integer getMaxQuantity(){
        return maxQuantity;
    }

    public Integer getProductID(){
        return productID;
    }
}
