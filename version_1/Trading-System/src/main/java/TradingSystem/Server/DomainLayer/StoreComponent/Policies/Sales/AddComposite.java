package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import java.util.concurrent.ConcurrentHashMap;

public class AddComposite extends CompositeSale{
    @Override
    public Boolean checkEntitlement(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID) {
        return true;
    }

    @Override
    public Double calculateSale(ConcurrentHashMap<Integer, Integer> products, Double finalSale, Integer userID, Integer storeID) {
        Double sale=0.0;
        for (Sale s:children
        ) {
            Double tempSale=s.calculateSale(products,finalSale, userID,storeID );
            sale=tempSale+sale;
        }
        return sale;
    }
}
