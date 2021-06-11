package TradingSystem.Server.ServiceLayer.DummyObject.DummyExpressions;

import TradingSystem.Server.ServiceLayer.DummyObject.DummySales.DummyCompositeSale;
import TradingSystem.Server.ServiceLayer.DummyObject.DummySales.DummySale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public abstract class DummyCompositeExpression implements DummyExpression {

    public Integer NodeID;
    public List<DummyExpression> children;

    public DummyCompositeExpression(Integer nodeID) {
        NodeID = nodeID;
        this.children=new ArrayList<> ();
    }

    @Override
    public void setExpression(int id, DummyExpression exp) {
        if(NodeID==id){
            this.children.add(exp);
        }
        else {
            for (DummyExpression s : children
            ) {
                s.setExpression(id, exp);
            }
        }
    }

    @Override
    public void setExpression(DummyExpression exp) {
       this.children.add(exp);
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
//            System.out.println("DummyCompositeExpression toString error");
//        }
//        try {
//            JOFirst.accumulate("CompositeExpression", JO);
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
        }
        Map<String,Object> mapType=new HashMap<>();
         mapType.put("CompositeExpression",mapElements);
        return mapType;
    }

    @Override
    public String getName() {
        return "CompositeExpression";
    }

    public Integer getNodeID() {
        return NodeID;
    }

    public List<DummyExpression> getChildren() {
        return children;
    }

}


