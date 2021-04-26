package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Product;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface Sale {
    public Boolean checkEntitlement(ConcurrentHashMap<Integer,Integer> products,Double finalPrice);
    public Double calculateSale(ConcurrentHashMap<Integer,Integer> products,Double finalSale);
}
