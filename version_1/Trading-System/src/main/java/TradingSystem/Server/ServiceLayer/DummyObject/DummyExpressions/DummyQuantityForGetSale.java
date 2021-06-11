package TradingSystem.Server.ServiceLayer.DummyObject.DummyExpressions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DummyQuantityForGetSale extends DummySimpleExpression {

    private Integer productId;
    private Integer quantityForSale;

    public DummyQuantityForGetSale(Integer nodeId,Integer productId, Integer quantityForSale) {
        super(nodeId);
        this.productId = productId;
        this.quantityForSale = quantityForSale;
    }

//    @Override
//    public String toString() {
//        JSONObject JOFirst = new JSONObject();
//        JSONObject JO = new JSONObject();
//        try {
//            JO.put("NodeID", NodeID);
//            JO.put("productId", productId);
//            JO.put("quantityForSale", quantityForSale);
//
//        } catch (Exception e) {
//            System.out.println("DummyQuantityForGetSale toString error");
//        }
//        try {
//            JOFirst.accumulate("QuantityForGetSale", JO);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return  JOFirst.toString();
//    }

    @Override
    public Map<String,Object> createMap() {
        Map<String,Object> mapElements=new HashMap<>();
        mapElements.put("NodeId",NodeID);
        mapElements.put("productId",productId);
        mapElements.put("quantityForSale",quantityForSale);
        //Map<String,Object> mapType=new HashMap<>();
       // mapType.put("QuantityForGetSale",mapElements);
        return mapElements;
    }

    @Override
    public String getName() {
        return "QuantityForGetSale";
    }

    public Integer getProductId() {
        return productId;
    }

    public Integer getQuantityForSale() {
        return quantityForSale;
    }

}
