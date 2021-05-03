package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class StoreSale extends SimpleSale {

    private Integer storeID;
    private Double  discountPercentage;

    public StoreSale(Expression exp,Integer storeID, Double discountPercentage) {
        super(exp);
        this.storeID = storeID;
        this.discountPercentage = discountPercentage;
    }

    public StoreSale(Integer storeID, Double discountPercentage) {
        this.storeID = storeID;
        this.discountPercentage = discountPercentage;
    }

    @Override
    public Double calculateSale(ConcurrentHashMap<Integer, Integer> products, Double finalSale, Integer userID, Integer storeID) {
        if(this.getExpression().evaluate(products,finalSale, userID, storeID)){
            return  (discountPercentage/100)*finalSale;
        }
        return 0.0;
    }

}