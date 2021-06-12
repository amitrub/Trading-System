package TradingSystem.Server.ServiceLayer.DummyObject.DummyExpressions;

import TradingSystem.Server.ServiceLayer.DummyObject.DummySales.DummySimpleSale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DummyPriceForGetSale extends DummySimpleExpression {

    private Integer priceForSale;

    public DummyPriceForGetSale(Integer nodeID, Integer priceForSale) {
        super(nodeID);
        this.priceForSale = priceForSale;
    }

//    @Override
//    public String toString() {
//        JSONObject JOFirst = new JSONObject();
//        JSONObject JO = new JSONObject();
//        try {
//            JO.put("NodeID", this);
//            JO.put("priceForSale", priceForSale);
//
//        } catch (Exception e) {
//            System.out.println("DummyPriceForGetSale toString error");
//        }
//        try {
//            JOFirst.accumulate("PriceForGetSale", JO);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return  JOFirst.toString();
//    }


    @Override
    public Map<String,Object> createMap() {
        Map<String,Object> mapElements=new HashMap<>();
        mapElements.put("NodeId",NodeID);
        mapElements.put("priceForSale",priceForSale);
        //Map<String,Object> mapType=new HashMap<>();
       // mapType.put("PriceForGetSale",mapElements);
        return mapElements;
    }

    @Override
    public String getName() {
        return "PriceForGetSale";
    }

    public Integer getPriceForSale() {
        return priceForSale;
    }

}
