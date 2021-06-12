package TradingSystem.Server.ServiceLayer.DummyObject.DummySales;


import TradingSystem.Server.ServiceLayer.DummyObject.DummyExpressions.DummyExpression;

import java.util.*;

public abstract class DummyCompositeSale implements DummySale{

    public Integer NodeID;
    public List<DummySale> children;

    public DummyCompositeSale(int nodeID) {
     this.NodeID=nodeID;
     this.children=new  ArrayList<>();
    }

    @Override
    public void setSale(int id, DummySale sale) {
       if(NodeID==id){
           this.children.add(sale);
       }
       else {
           for (DummySale s : children
           ) {
               s.setSale(id, sale);
           }
       }
    }

    @Override
    public void setSale(DummySale sale) {
       children.add(sale);
    }

    @Override
    public void setExpression(int id, DummyExpression exp) {
        for (DummySale s:children
        ) {
            s.setExpression(id, exp);
        }
    }

    @Override
    public void setExpression(DummyExpression exp) {

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
        mapType.put("CompositeSale",mapElements);
        return mapType;
    }

    @Override
    public String getName() {
        return "CompositeSale";
    }

    public Integer getNodeID() {
        return NodeID;
    }

    public List<DummySale> getChildren() {
        return children;
    }

}
