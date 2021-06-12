package TradingSystem.Server.ServiceLayer.DummyObject.DummyExpressions;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimitForProduct;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DummyQuantityLimitForProduct extends DummySimpleExpression {

    private Integer maxQuantity;
    private Integer productID;

    public DummyQuantityLimitForProduct(Integer nodeID, Integer maxQuantity, Integer productID) {
        super(nodeID);
        this.maxQuantity = maxQuantity;
        this.productID = productID;
    }

    public Integer getProductID() {
        return productID;
    }

    public Integer getMaxQuantity () {
        return maxQuantity;
    }


//    @Override
//    public String toString () {
//        JSONObject JOFirst = new JSONObject();
//        JSONObject JO = new JSONObject();
//        try {
//            JO.put("NodeID", this.NodeID);
//            JO.put("maxQuantity", maxQuantity);
//            JO.put("productID", productID);
//
//        } catch (Exception e) {
//            System.out.println("DummyQuantityLimitForProduct toString error");
//        }
//        try {
//            JOFirst.accumulate("QuantityLimitForProduct", JO);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return JOFirst.toString();
//    }


    @Override
    public Map<String, Object> createMap () {
        Map<String, Object> mapElements = new HashMap<>();
        mapElements.put("NodeId", NodeID);
        mapElements.put("maxQuantity", maxQuantity);
        mapElements.put("productID", productID);
        //Map<String,Object> mapType=new HashMap<>();
        // mapType.put("PriceForGetSale",mapElements);
        return mapElements;
    }

    @Override
    public String getName () {
        return "QuantityLimitForProduct";

    }

    @Override
    public Expression closeExp() {
        return new QuantityLimitForProduct(maxQuantity,productID);
    }


}