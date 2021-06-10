package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;

public class ProductSale extends SimpleSale {

    @Autowired
    public static TradingSystemImpl tradingSystem;

    public static void setTradingSystem(TradingSystemImpl tradingSystem) {
        ProductSale.tradingSystem = tradingSystem;
    }

    Integer productID;
    Integer discountPercentage;

    public ProductSale(Expression exp, Integer productID, Integer discountPercentage) {
        super(exp);
        this.productID = productID;
        this.discountPercentage = discountPercentage;
    }

    public ProductSale(Integer productID, Integer discountPercentage) {
        this.productID = productID;
        this.discountPercentage = discountPercentage;
    }

    @Override
    public Double calculateSale(ConcurrentHashMap<Integer, Integer> products, Double finalSale, Integer userID, Integer storeID) {
        if(this.getExpression()!=null) {
            if (products.get(productID) != null) {
                if (this.getExpression().evaluate(products, finalSale, userID, storeID)) {
                    Product p = tradingSystem.getProduct(storeID, productID);
                    Double ret = ((discountPercentage / 100) * p.getPrice()) * products.get(productID);
                    return ret;
                }
            }
        }
        return 0.0;
    }

    @Override
    public Response checkValidity(int storeID) {
       if(0>discountPercentage||discountPercentage>100){
         return new Response(true, "discount percentage cant be negative");
       }
       if(tradingSystem.getProduct(storeID,productID)==null){
           return new Response(true,"product dont exist in the store");
       }
       if(this.getExpression()==null){
           return new Response(true,"there is not expression from some reason");
       }
        return this.getExpression().checkValidity(storeID);
    }

    public Integer getProductID(){
        return productID;
    }
    public Integer getDiscountPercentage(){
        return discountPercentage;
    }
    public String toString(){
        return "product id "+productID+" discount "+discountPercentage+" "+getExpression().toString();
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
