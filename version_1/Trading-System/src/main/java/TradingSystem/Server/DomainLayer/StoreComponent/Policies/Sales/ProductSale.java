package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;

import java.util.concurrent.ConcurrentHashMap;

public class ProductSale extends SimpleSale {

    //Integer saleID;
    Integer productID;
    Double discountPercentage;
    //Expression expression;
    TradingSystem tradingSystem = TradingSystem.getInstance();

    public ProductSale(Expression exp, Integer productID, Double discountPercentage) {
        //this.saleID = saleID;
        super(exp);
        this.productID = productID;
        this.discountPercentage = discountPercentage;
      //  this.setExpression(null);
    }

    public ProductSale(Integer productID, Double discountPercentage) {
        //this.saleID = saleID;
       // super(exp);
        this.productID = productID;
        this.discountPercentage = discountPercentage;
    }

    //Add productID,Price,quantity
    @Override
    public Double calculateSale(ConcurrentHashMap<Integer, Integer> products, Double finalSale, Integer userID, Integer storeID) {
        if (products.get(productID) != null) {
            if (this.getExpression().evaluate(products, finalSale, userID, storeID)) {
                Product p = tradingSystem.getProduct(storeID, productID);
                Double ret = ((discountPercentage / 100) * p.getPrice())*products.get(productID);
                return ret;
            }
        }
        return 0.0;
    }
}

/*

    @Override
    public Expression setExpression(Expression exp) {
        if(this.expression==null)
            this.expression=exp;
        else
            this.expression.add(exp);
        return exp;
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
        if(this.expression!=null){
            this.expression.setExpression(expID,exp);
        }

        return null;
    }

    @Override
    public Sale setSale(Integer saleID, Sale sale) {
        return this;
    }

    @Override
    public Expression getExpression(Integer ID) {
        if(this.expression!=null)
         return this.expression.getExpression(ID);
        else
        return this.expression;
    }

    @Override
    public Integer getID() {
        return this.saleID;
    }

    @Override
    public Sale getSale(Integer saleID) {
        return this;
    }
 */