package TradingSystem.Server.ServiceLayer.DummyObject.DummyExpressions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DummyNumOfProductsForGetSale extends DummySimpleExpression {

    private Integer numOfProductsForSale;

    public DummyNumOfProductsForGetSale(Integer nodeId,Integer  numOfProductsForSale) {
       super(nodeId);
        this.numOfProductsForSale = numOfProductsForSale;
    }

//    @Override
//    public String toString() {
//        JSONObject JOFirst = new JSONObject();
//        JSONObject JO =new JSONObject();
//        try {
//            JO.put("NodeID", NodeID);
//            JO.put("numOfProductsForSale", numOfProductsForSale);
//        } catch (Exception e) {
//            System.out.println("DummyShoppingHistory toString error");
//        }
//        try {
//            JOFirst.accumulate("NumOfProductsForGetSale", JO);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return  JOFirst.toString();
//    }

    @Override
    public Map<String,Object> createMap() {
        Map<String,Object> mapElements=new HashMap<>();
        mapElements.put("NodeId",NodeID);
        mapElements.put("numOfProductsForSale",numOfProductsForSale);
        //Map<String,Object> mapType=new HashMap<>();
       // mapType.put("NumOfProductsForGetSale",mapElements);
        return mapElements;
    }

    @Override
    public String getName() {
        return "NumOfProductsForGetSale";
    }

    public Integer getNumOfProductsForSale() {
        return numOfProductsForSale;
    }

}
