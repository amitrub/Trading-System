package TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;

import java.util.concurrent.ConcurrentHashMap;

public class NumOfProductsForGetSale extends SimpleExpression {

    private Integer numOfProductsForSale;

    public NumOfProductsForGetSale(Integer expressionID, Integer numOfProductForSale) {
        super(expressionID);
        this.numOfProductsForSale=numOfProductForSale;
    }

    @Override
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID) {
        return products.size()>=numOfProductsForSale;
    }
}
