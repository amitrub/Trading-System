package TradingSystem.Server.ServiceLayer.DummyObject.DummyExpressions;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimitForCategory;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DummyQuantityLimitForCategory extends DummySimpleExpression {

    private Integer maxQuantity;
    private String  category;


    public DummyQuantityLimitForCategory(Integer nodeID, Integer maxQuantity, String category) {
        super(nodeID);
        this.maxQuantity = maxQuantity;
        this.category = category;
    }

    public Integer getMaxQuantity() {
        return maxQuantity;
    }

    public String getCategory() {
        return category;
    }

//    @Override
//    public String toString() {
//        JSONObject JOFirst = new JSONObject();
//        JSONObject JO = new JSONObject();
//        try {
//            JO.put("NodeID", this.NodeID);
//            JO.put("maxQuantity", maxQuantity);
//            JO.put("category", category);
//
//        } catch (Exception e) {
//            System.out.println("DummyQuantityLimitForStore toString error");
//        }
//        try {
//            JOFirst.accumulate("QuantityLimitForCategory", JO);
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
        mapElements.put("category",category);
        //Map<String,Object> mapType=new HashMap<>();
        // mapType.put("PriceForGetSale",mapElements);
        return mapElements;
    }

    @Override
    public String getName() {
        return "QuantityLimitForCategory";
    }

    @Override
    public Expression closeExp() {
        return new QuantityLimitForCategory(maxQuantity,category);
    }


}