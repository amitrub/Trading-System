package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CategorySale implements Sale {
    String category;
    Double  discountPercentage;
    Expression expression;
    TradingSystem tradingSystem=TradingSystem.getInstance();

    public CategorySale(String category, Double discountPercentage, Expression exception) {
        this.category = category;
        this.discountPercentage = discountPercentage;
        this.expression = exception;
    }

    @Override
    public Double calculateSale(ConcurrentHashMap<Integer, Integer> products, Double finalSale, Integer userID, Integer storeID) {
        double priceForCategory=0.0;
        if(expression.evaluate(products,finalSale,userID,storeID )){
            Set<Integer> keySet=products.keySet();
            for (Integer key:keySet
                 ) {
                Product p=tradingSystem.getProduct(storeID,key);
                if(p.getCategory().equals(category)){
                    priceForCategory=priceForCategory+p.getPrice();
                }
            }
        }
        return (discountPercentage/100)*priceForCategory;
    }
}
