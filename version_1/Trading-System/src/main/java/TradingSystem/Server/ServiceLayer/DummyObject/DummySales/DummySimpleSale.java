package TradingSystem.Server.ServiceLayer.DummyObject.DummySales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyExpressions.DummyExpression;

import java.util.HashMap;
import java.util.Map;

public abstract class DummySimpleSale implements DummySale{

    public Integer NodeID;
    public DummyExpression expression;

    public DummySimpleSale(Integer nodeID) {
        NodeID = nodeID;
    }

    @Override
    public void setSale(int id, DummySale sale) {
    }

    @Override
    public void setSale(DummySale sale) {
    }

    @Override
    public void setExpression(int id, DummyExpression exp) {
     if(this.NodeID==id){
         this.expression=exp;
     }
     else
         if(this.expression!=null) {
             this.expression.setExpression(id, exp);
         }
    }

    @Override
    public void setExpression(DummyExpression exp) {
        this.expression=exp;
    }

    @Override
    public Map<String,Object> createMap() {
        Map<String,Object> mapElements=new HashMap<>();
        mapElements.put("NodeId",NodeID);
        mapElements.put("expression",expression.createMap());
        //Map<String,Object> mapType=new HashMap<>();
        //mapType.put("SimpleSale",mapElements);
        return mapElements;
    }

    @Override
    public String getName() {
        return "SimpleSale";
    }

    @Override
    public Sale closeSale() {
        return null;
    }

    public Integer getNodeID() {
        return NodeID;
    }

    public DummyExpression getExpression() {
        return expression;
    }
}
