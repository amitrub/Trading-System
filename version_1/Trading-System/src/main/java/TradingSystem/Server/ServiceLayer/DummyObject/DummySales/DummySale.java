package TradingSystem.Server.ServiceLayer.DummyObject.DummySales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyExpressions.DummyExpression;

import java.util.Map;

public interface DummySale {
    public void setSale(int id,DummySale sale);
    public void setSale(DummySale sale);

    public void setExpression(int id, DummyExpression exp);
    public void setExpression(DummyExpression exp);

    public Map<String,Object> createMap();
    public String getName();

    public Sale closeSale();

}
