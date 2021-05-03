package TradingSystem.Server.DomainLayer.StoreComponent.Policies;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;

import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BuyingPolicy {

    private Integer storeID;
    private Expression exp;

    public TradingSystem tradingSystem;

    public BuyingPolicy(Integer storeID,Expression exp){
        this.storeID=storeID;
        this.exp=exp;
        this.tradingSystem=TradingSystem.getInstance();
    }

    public boolean checkEntitlement(ConcurrentHashMap<Integer,Integer> products,Integer userID,Double finalPrice){
        return exp.evaluate(products,finalPrice,userID,storeID);
    }

}
