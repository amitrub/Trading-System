package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Limits;

import java.util.concurrent.ConcurrentHashMap;

public interface Limit {
    public Boolean checkEntitlement(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID);
}
