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

    public void BuyingPolicy(Integer storeID){
        this.storeID=storeID;
        this.Limits =new ConcurrentHashMap<>();
        this.tradingSystem=TradingSystem.getInstance();
    }

    private static synchronized int getNextLimitID() {
        nextLimitID++;
        return nextLimitID;
    }

    public void AddLimit(Limit limit){
        int saleID=nextLimitID;
        this.Limits.put(saleID,limit);
    }

    public void RemoveLimit(Integer limitID){
        this.Limits.remove(limitID);
    }

    //TODO check
    public boolean checkEntitlement(ConcurrentHashMap<Integer,Integer> products,Integer userID){
        Double price=0.0;

        Set<Integer> keySetProdects=products.keySet();
        for (Integer key:keySetProdects
        ) {
            Double tmpPrice=tradingSystem.getProduct(storeID,key).getPrice();
            price=price+tmpPrice;
        }

        Set<Integer> keySetSales= Limits.keySet();
        for (Integer key:keySetSales
        ) {
            if(!this.Limits.get(key).checkEntitlement(products,price,userID,storeID ))
                return false;
        }
        return true;
    }
}
