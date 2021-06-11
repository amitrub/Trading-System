package TradingSystem.Server.ServiceLayer.DummyObject.DummySales;

import java.util.HashMap;
import java.util.Map;

public class DummyMaxSale  extends DummyCompositeSale{
    public DummyMaxSale(int nodeID) {
        super(nodeID);
    }

    @Override
    public Map<String,Object> createMap() {
        Map<String,Object> mapElements=new HashMap<>();
        mapElements.put("NodeId",NodeID);
        for (DummySale DE:children
        ) {
            mapElements.put(DE.getName(), DE.createMap());
        }
        Map<String,Object> mapType=new HashMap<>();
        mapType.put("MaxComposite",mapElements);
        return mapType;
    }

    @Override
    public String getName() {
        return "MaxComposite";
    }

}
