package TradingSystem.Server.ServiceLayer.DummyObject.DummyExpressions;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.PriceForGetSale;
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

    @Override
    public Expression closeExp() {
        return new PriceForGetSale(priceForSale);
    }

    public Integer getPriceForSale() {
        return priceForSale;
    }

}
