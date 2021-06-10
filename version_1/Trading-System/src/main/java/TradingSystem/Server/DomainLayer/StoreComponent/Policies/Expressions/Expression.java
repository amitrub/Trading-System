package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions;

import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DBExpression;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.concurrent.ConcurrentHashMap;

public interface Expression {

    public Expression add(Expression expr);

    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID);
    Response checkValidity(int storeID);
}

