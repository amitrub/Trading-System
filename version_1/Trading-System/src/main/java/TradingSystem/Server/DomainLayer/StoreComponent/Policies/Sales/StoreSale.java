package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;

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
    public Boolean checkEntitlement(ConcurrentHashMap<Integer, Integer> products,Double finalPrice) {

        return expression.evaluate(products,finalPrice);
    }

    @Override
    public Double calculateSale(ConcurrentHashMap<Integer,Integer> products,Double finalSale) {
        if(checkEntitlement(products,finalSale)){
            return  (discountPercentage/100)*finalSale;
        }
        return 0.0;
    }
}
