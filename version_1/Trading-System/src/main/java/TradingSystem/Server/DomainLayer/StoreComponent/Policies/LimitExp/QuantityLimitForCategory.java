package TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;

public class QuantityLimitForCategory extends SimpleExpression {

    public Integer maxQuantity;
    public String  category;

    @Autowired
    public static TradingSystemImplRubin tradingSystem;

    public static void setTradingSystem(TradingSystemImplRubin tradingSystem) {
        QuantityLimitForCategory.tradingSystem = tradingSystem;
    }

    public QuantityLimitForCategory(Integer maxQuantity, String category) {
        this.category=category;
        this.maxQuantity = maxQuantity;
    }

    @Override
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        Integer quantity = 0;
        for (Integer key : products.keySet()
        ) {
            if (tradingSystem.getProduct(storeID, key).getCategory().equals(category)) {
                quantity = quantity + products.get(key);
            }
        }
        return maxQuantity>=quantity;
    }

    @Override
    public Response checkValidity(int storeID) {
        if(0>maxQuantity){
            return new Response(true, "maxQuantity cant be negative");
        }
        return new Response("correct");
    }

    public Integer getMaxQuantity(){
        return maxQuantity;
    }

    public String getCategory(){
        return category;
    }
}
