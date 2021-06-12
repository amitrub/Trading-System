package TradingSystem.Server.ServiceLayer.DummyObject.DummyExpressions;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class DummySimpleExpression implements DummyExpression {

    public Integer NodeID;

    public DummySimpleExpression(Integer nodeID) {
        NodeID = nodeID;
    }

    @Override
    public void setExpression(int id, DummyExpression exp) {
    }

    @Override
    public void setExpression(DummyExpression exp) {

    }

//    @Override
//    public String toString() {
//        JSONObject JOFirst = new JSONObject();
//        JSONObject JO = new JSONObject();
//        try {
//            JO.put("NodeID", this);
//
//        } catch (Exception e) {
//            System.out.println("DummySimpleExpression toString error");
//        }
//        try {
//            JOFirst.accumulate("SimpleExpression ", JO);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return  JOFirst.toString();
//    }

    @Override
    public Map<String,Object> createMap() {
        Map<String,Object> mapElements=new HashMap<>();
        mapElements.put("NodeId",NodeID);
        Map<String,Object> mapType=new HashMap<>();
        mapType.put("SimpleExpression",mapElements);
        return mapType;
    }

    @Override
    public String getName() {
        return "SimpleExpression";
    }

    @Override
    public Expression closeExp() {
        return null;
    }

    public Integer getNodeID() {
        return NodeID;
    }

}
