package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class MaxComposite extends CompositeSale {

    public MaxComposite(List<Sale> S) {
        super(S);
    }

    public MaxComposite() {
      //  super(S);
    }

    @Override
    public Double calculateSale(ConcurrentHashMap<Integer, Integer> products, Double finalSale, Integer userID, Integer storeID) {
        Double sale=0.0;
        for (Sale s:this.children
             ) {
            Double tempSale=s.calculateSale(products,finalSale, userID,storeID );
            if(tempSale>sale)
                sale=tempSale;
        }
        return sale;
    }

}
