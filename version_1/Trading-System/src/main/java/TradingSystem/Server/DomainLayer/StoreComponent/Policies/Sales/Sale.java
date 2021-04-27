package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import java.util.concurrent.ConcurrentHashMap;

public interface Sale {
      public Double calculateSale(ConcurrentHashMap<Integer, Integer> products, Double finalSale, Integer userID, Integer storeID);
}
