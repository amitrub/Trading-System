package TradingSystem.Server.ServiceLayer.DummyObject.DummyExpressions;

import org.json.JSONObject;

import java.util.Map;

public interface DummyExpression {
    public void setExpression(int id, DummyExpression exp);
    public void setExpression(DummyExpression exp);
    public Map<String,Object> createMap();
    public String getName();
}
