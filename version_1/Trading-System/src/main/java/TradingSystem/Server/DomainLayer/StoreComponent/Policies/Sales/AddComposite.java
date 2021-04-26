package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import java.util.concurrent.ConcurrentHashMap;

public class AddComposite extends CompositeSale{
    @Override
    public Boolean checkEntitlement(ConcurrentHashMap<Integer, Integer> products,Double finalPrice) {
        return true;
    }

    @Override
    public Double calculateSale(ConcurrentHashMap<Integer, Integer> products, Double finalSale) {
        Double sale=0.0;
        for (Sale s:children
        ) {
            Double tempSale=calculateSale(products,finalSale);
            if(tempSale>sale)
                sale=tempSale+sale;
        }
        return sale;
    }
}
