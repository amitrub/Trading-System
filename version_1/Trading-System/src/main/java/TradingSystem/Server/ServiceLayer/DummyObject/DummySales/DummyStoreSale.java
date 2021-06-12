package TradingSystem.Server.ServiceLayer.DummyObject.DummySales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.StoreSale;

import java.util.HashMap;
import java.util.Map;

public class DummyStoreSale extends DummySimpleSale {

    private Integer storeID;
    private Integer discount;

    public DummyStoreSale(int nodeId,Integer storeID, Integer discountPercentage) {
        super(nodeId);
        this.storeID = storeID;
        this.discount = discountPercentage;
    }

    public Integer getStoreID() {
        return storeID;
    }

    public void setStoreID(Integer storeID) {
        this.storeID = storeID;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discount = discountPercentage;
    }

    public Integer getDiscountPercentage() {
        return discount;
    }

    @Override
    public Map<String,Object> createMap() {
        Map<String,Object> mapElements=new HashMap<>();
        mapElements.put("NodeId",NodeID);
        mapElements.put("storeID",storeID);
        mapElements.put("discount",discount);
        mapElements.put("expression",expression.createMap());
        //Map<String,Object> mapType=new HashMap<>();
        //mapType.put("StoreSale",mapElements);
        return mapElements;
    }

    @Override
    public String getName() {
        return "StoreSale";
    }

    @Override
    public Sale closeSale() {
        StoreSale SS=new StoreSale(storeID,discount);
        Expression exp=this.expression.closeExp();
        SS.setExpression(exp);
        return SS;
    }


}
