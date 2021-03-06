package TradingSystem.Server.ServiceLayer.DummyObject.DummyExpressions;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.OrComposite;

import java.util.HashMap;
import java.util.Map;

public class DummyOrExpression extends DummyCompositeExpression{

    public DummyOrExpression(Integer nodeID) {
        super(nodeID);
    }

//    @Override
//    public String toString() {
//        JSONObject JOFirst = new JSONObject();
//        JSONObject JO = new JSONObject();
//        try {
//            JO.put("NodeID", NodeID);
//            JSONArray prods = new JSONArray();
//            JO.put("children", prods);
//        } catch (Exception e) {
//            System.out.println("DummyOrExpression toString error");
//        }
//        try {
//            JOFirst.accumulate("OrComposite", JO);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return  JOFirst.toString();
//    }

    @Override
    public Map<String,Object> createMap() {
        Map<String,Object> mapElements=new HashMap<>();
        mapElements.put("NodeId",NodeID);
        for (DummyExpression DE:children
             ) {
            mapElements.put(DE.getName(),DE.createMap());
        }
        Map<String,Object> mapType=new HashMap<>();
        mapType.put("OrComposite",mapElements);
        return mapType;
    }

    @Override
    public String getName() {
        return "OrComposite";
    }

    @Override
    public Expression closeExp() {
        OrComposite Or=new OrComposite();
        for (DummyExpression DE:children
             ) {
            Expression Exp=DE.closeExp();
            Or.add(Exp);
        }
        return Or;
    }


}
