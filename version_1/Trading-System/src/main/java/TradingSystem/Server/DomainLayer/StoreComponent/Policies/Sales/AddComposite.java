package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class AddComposite extends CompositeSale{

    public AddComposite(List<Sale> S) {
        super(S);
    }

    public AddComposite() {
       // super(new LinkedList<Sale>());
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
