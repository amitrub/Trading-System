package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;

import java.util.concurrent.ConcurrentHashMap;

public class ProductSale implements Sale{
    Integer productID;
    Double  discountPercentage;
    Expression expression;
    TradingSystem tradingSystem=TradingSystem.getInstance();

    public ProductSale(Integer productID, Double discountPercentage, Expression exception) {
        this.productID = productID;
        this.discountPercentage = discountPercentage;
        this.expression = exception;
    }

    //Add productID,Price,quantity
    @Override
    public Double calculateSale(ConcurrentHashMap<Integer, Integer> products, Double finalSale, Integer userID, Integer storeID) {
       if(products.get(productID)!=null) {
           if (expression.evaluate(products, finalSale, userID, storeID)) {
               Product p=tradingSystem.getProduct(storeID,productID);
               return (discountPercentage / 100) * p.getPrice();
           }
       }
        return 0.0;
    }
}
