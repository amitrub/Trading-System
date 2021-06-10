package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions;

import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DBExpression;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;

public abstract class SimpleExpression  implements Expression{

    @Autowired
    public static TradingSystemImplRubin tradingSystem;

    public static void setTradingSystem(TradingSystemImplRubin tradingSystem) {
        SimpleExpression.tradingSystem = tradingSystem;
    }

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
