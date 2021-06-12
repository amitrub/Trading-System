package TradingSystem.Server.ServiceLayer.DummyObject.DummyExpressions;

import TradingSystem.Server.ServiceLayer.DummyObject.DummySales.DummyStoreSale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DummyAndExpression extends DummyCompositeExpression {

    public DummyAndExpression(Integer nodeID) {
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
//            System.out.println("DummyAndExpression toString error");
//        }
//        try {
//            JOFirst.accumulate("AndExpression", JO);
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
            mapElements.put(DE.getName(), DE.createMap());
        }Map<String,Object> mapType=new HashMap<>();
        mapType.put("AndComposite",mapElements);
       return mapType;
    }

    @Override
    public String getName() {
        return "AndComposite";
    }


}
