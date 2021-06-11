package TradingSystem.Server.ServiceLayer.DummyObject.DummySales;

import java.util.HashMap;
import java.util.Map;

public class DummyStoreSale extends DummySimpleSale {

    private Integer storeID;
    private Integer discountPercentage;

    public DummyStoreSale(int nodeId,Integer storeID, Integer discountPercentage) {
        super(nodeId);
        this.storeID = storeID;
        this.discountPercentage = discountPercentage;
    }

    public Integer getStoreID() {
        return storeID;
    }

    public void setStoreID(Integer storeID) {
        this.storeID = storeID;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    @Override
    public Map<String,Object> createMap() {
        Map<String,Object> mapElements=new HashMap<>();
        mapElements.put("NodeId",NodeID);
        mapElements.put("storeID",storeID);
        mapElements.put("discountPercentage",discountPercentage);
        mapElements.put("expression",expression.createMap());
        //Map<String,Object> mapType=new HashMap<>();
        //mapType.put("StoreSale",mapElements);
        return mapElements;
    }

    @Override
    public String getName() {
        return "StoreSale";
    }


}
