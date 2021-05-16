package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.XorDecision;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface Decision {

    public Double chooseSale(List<Sale> sales, ConcurrentHashMap<Integer, Integer> products, Double finalSale, Integer userID, Integer storeID);
}
