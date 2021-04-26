package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
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

    @Override
    public Boolean checkEntitlement(ConcurrentHashMap<Integer, Integer> productsn,Double finalPrice) {
        return expression.evaluate(productsn,finalPrice);
    }


    //Add productID,Price,quantity
    @Override
    public Double calculateSale(ConcurrentHashMap<Integer, Integer> products, Double finalSale) {
        if(checkEntitlement(products,finalSale)){
            return  (discountPercentage/100)*finalSale;
        }
        return 0.0;
    }
}
