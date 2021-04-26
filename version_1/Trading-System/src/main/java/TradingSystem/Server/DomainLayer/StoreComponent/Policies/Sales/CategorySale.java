package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;

import java.util.concurrent.ConcurrentHashMap;

public class CategorySale implements Sale {
    String Category;
    Double  discountPercentage;
    Expression expression;

    public CategorySale(String category, Double discountPercentage, Expression exception) {
        Category = category;
        this.discountPercentage = discountPercentage;
        this.expression = exception;
    }

    @Override
    public Boolean checkEntitlement(ConcurrentHashMap<Integer, Integer> products, Double finalPrice) {
        return expression.evaluate(products,finalPrice);
    }

    @Override
    public Double calculateSale(ConcurrentHashMap<Integer, Integer> products, Double finalSale) {
        return null;
    }
}
