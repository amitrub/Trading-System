package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.ConditionRoles;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface ConditionRole {

    public boolean checkRole( ConcurrentHashMap<Integer, Integer> products, Double finalSale, Integer userID, Integer storeID);
}
