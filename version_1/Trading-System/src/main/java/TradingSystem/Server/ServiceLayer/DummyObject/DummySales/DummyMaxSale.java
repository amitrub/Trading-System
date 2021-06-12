package TradingSystem.Server.ServiceLayer.DummyObject.DummySales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.MaxComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;

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

    @Override
    public Sale closeSale() {
        MaxComposite Max=new MaxComposite();
        for (DummySale DS:children
             ) {
            Sale sale=DS.closeSale();
            Max.add(sale);
        }
        return Max;
    }

}
