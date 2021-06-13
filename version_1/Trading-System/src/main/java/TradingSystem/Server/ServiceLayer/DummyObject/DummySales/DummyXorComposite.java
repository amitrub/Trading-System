package TradingSystem.Server.ServiceLayer.DummyObject.DummySales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.XorComposite;

import java.util.HashMap;
import java.util.Map;

public class DummyXorComposite extends DummyCompositeSale {

    public DummyXorComposite(int nodeID) {
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
        mapType.put("XorComposite",mapElements);
        return mapType;
    }

    @Override
    public String getName() {
        return "XorComposite";
    }

    @Override
    public Sale closeSale() {
        XorComposite xor=new XorComposite();
        for (DummySale DS:children
             ) {
            Sale sale=DS.closeSale();
            xor.add(sale);
        }
        return xor;
    }


}
