package TradingSystem.Server.DomainLayer.StoreComponent.Policies;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Limits.Limit;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class BuyingPolicy {

    private Integer storeID;
    private static int nextLimitID=0;
    private ConcurrentHashMap<Integer, Limit> Limits;

    public TradingSystem tradingSystem;

    public BuyingPolicy(Integer storeID){
        this.storeID=storeID;
        this.Limits =new ConcurrentHashMap<>();
        this.tradingSystem=TradingSystem.getInstance();
    }

       public static synchronized int getNextLimitID() {
        nextLimitID++;
        return nextLimitID;
    }

    public void AddLimit(Limit limit){
        this.Limits.put(limit.getID(),limit);
    }

    public void RemoveLimit(Integer limitID){
        this.Limits.remove(limitID);
    }

    //TODO check
    public boolean checkEntitlement(ConcurrentHashMap<Integer,Integer> products,Integer userID,Double finalPrice){
        Set<Integer> keySetSales= Limits.keySet();
        for (Integer key:keySetSales
        ) {
            if(!this.Limits.get(key).checkEntitlement(products,finalPrice,userID,storeID))
                return false;
        }
        return true;
    }
}
