package TradingSystem.Server.ServiceLayer.DummyObject.DummySales;

import TradingSystem.Server.ServiceLayer.DummyObject.DummyExpressions.DummyExpression;

import java.util.HashMap;
import java.util.Map;

public class DummyAddSale extends DummyCompositeSale {

    public DummyAddSale(int nodeID) {
        super(nodeID);
    }


    @Override
    public Map<String,Object> createMap() {
        Map<String,Object> mapElements=new HashMap<>();
        mapElements.put("NodeId",NodeID);
        for (DummySale DE:children
        ) {
            mapElements.put(DE.getName(),DE.createMap());
        }
        Map<String,Object> mapType=new HashMap<>();
        mapType.put("AddComposite",mapElements);
        return mapType;
    }

    @Override
    public String getName() {
        return "AddComposite";
    }

}
