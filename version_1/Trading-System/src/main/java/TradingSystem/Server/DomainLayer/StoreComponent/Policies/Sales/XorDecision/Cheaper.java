package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.XorDecision;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Cheaper implements Decision {

    @Override
    public Double chooseSale(List<Sale> sales, ConcurrentHashMap<Integer, Integer> products, Double finalSale, Integer userID, Integer storeID) {
        if (!sales.isEmpty()) {
            Double sale = Double.MAX_VALUE;
            for (Sale s : sales
            ) {
                Double tmp = s.calculateSale(products, finalSale, userID, storeID);
                if (tmp < sale) {
                    sale = tmp;
                }
            }
            return sale;
        }
        return 0.0;
    }
}
