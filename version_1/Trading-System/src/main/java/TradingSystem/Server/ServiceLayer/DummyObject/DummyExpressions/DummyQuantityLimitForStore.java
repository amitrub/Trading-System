package TradingSystem.Server.ServiceLayer.DummyObject.DummyExpressions;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimitForStore;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DummyQuantityLimitForStore extends DummySimpleExpression {

    private Integer maxQuantity;
    private Integer storeID;

    public DummyQuantityLimitForStore(Integer nodeID, Integer maxQuantity, Integer storeID) {
        super(nodeID);
        this.maxQuantity = maxQuantity;
        this.storeID = storeID;
    }

//    @Override
//    public String toString() {
//        JSONObject JOFirst = new JSONObject();
//        JSONObject JO = new JSONObject();
//        try {
//            JO.put("NodeID", this.NodeID);
//            JO.put("maxQuantity", maxQuantity);
//            JO.put("storeID", storeID);
//
//        } catch (Exception e) {
//            System.out.println("DummyQuantityLimitForStore toString error");
//        }
//        try {
//            JOFirst.accumulate("QuantityLimitForStore", JO);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return  JOFirst.toString();
//    }


    @Override
    public Map<String,Object> createMap() {
        Map<String,Object> mapElements=new HashMap<>();
        mapElements.put("NodeId",NodeID);
        mapElements.put("maxQuantity",maxQuantity);
        mapElements.put("storeID",storeID);
        //Map<String,Object> mapType=new HashMap<>();
        // mapType.put("PriceForGetSale",mapElements);
        return mapElements;
    }

    @Override
    public String getName() {
        return "QuantityLimitForStore";
    }

    @Override
    public Expression closeExp() {
        return new QuantityLimitForStore(maxQuantity,storeID);
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public Integer getStoreID() {
        return storeID;
    }
}