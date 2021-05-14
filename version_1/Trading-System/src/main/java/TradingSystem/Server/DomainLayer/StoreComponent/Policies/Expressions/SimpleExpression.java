package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.concurrent.ConcurrentHashMap;

public abstract class SimpleExpression  implements Expression{

    public TradingSystemImpl tradingSystem = TradingSystemImpl.getInstance();
    public abstract Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID);

    @Override
    public Response checkValidity(int storeID) {
        return new Response("");
    }

    @Override
    public Expression add(Expression expr) {
        return this;
    }

}
