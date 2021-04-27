package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import java.util.concurrent.ConcurrentHashMap;

public class StoreSale implements Sale {

    Integer storeID;
    Double  discountPercentage;
    Expression expression;

    public StoreSale(Integer storeID, Double discountPercentage, Expression exp) {
        this.storeID = storeID;
        this.discountPercentage = discountPercentage;
        this.expression = exp;
    }

    @Override
    public Double calculateSale(ConcurrentHashMap<Integer, Integer> products, Double finalSale, Integer userID, Integer storeID) {
        if(expression.evaluate(products,finalSale, userID, storeID)){
            return  (discountPercentage/100)*finalSale;
        }
        return 0.0;
    }
}
