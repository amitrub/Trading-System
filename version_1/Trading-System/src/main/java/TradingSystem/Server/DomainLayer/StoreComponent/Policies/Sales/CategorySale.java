package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CategorySale extends SimpleSale {

    //Integer saleID;
    String category;
    Double  discountPercentage;
    //Expression expression;
    TradingSystemImpl tradingSystem= TradingSystemImpl.getInstance();

    public CategorySale(Expression exp,String category, Double discountPercentage) {
        super(exp);
        this.category = category;
        this.discountPercentage = discountPercentage;
    }

    public CategorySale(String category, Double discountPercentage) {
        this.category = category;
        this.discountPercentage = discountPercentage;
    }

    @Override
    public Double calculateSale(ConcurrentHashMap<Integer, Integer> products, Double finalSale, Integer userID, Integer storeID) {
        double priceForCategory = 0.0;
        if (this.getExpression() != null) {
            if (this.getExpression().evaluate(products, finalSale, userID, storeID)) {
                Set<Integer> keySet = products.keySet();
                for (Integer key : keySet
                ) {
                    Product p = tradingSystem.getProduct(storeID, key);
                    if (p.getCategory().equals(category)) {
                        priceForCategory = priceForCategory + p.getPrice() * products.get(p.getProductID());
                    }
                }
            }
            return (discountPercentage / 100) * priceForCategory;
        }
        return 0.0;
    }
}

/*
 @Override
    public Expression setExpression(Expression exp) {
        this.expression=exp;
        return this.expression;
    }

    @Override
    public Expression getExpression() {
        return this.expression;
    }

    @Override
    public Sale setSale(Sale sale) {
        return this;
    }

    @Override
    public Sale getSale() {
        return this;
    }

    @Override
    public Expression setExpression(Integer expID, Expression exp) {
        return this.expression.setExpression(expID,exp);
    }

    @Override
    public Sale setSale(Integer saleID, Sale sale) {
        return this;
    }

    @Override
    public Expression getExpression(Integer ID) {
        if(this.expression!=null){
            return this.expression.getExpression(ID);
        }
        return this.expression;
    }

    @Override
    public Sale getSale(Integer saleID) {
        if(this.saleID==saleID){
            return this;
        }
        return null;
    }

    @Override
    public Integer getID() {
        return this.saleID;
    }
 */
