package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CategorySale extends SimpleSale {

    @Autowired
    public static TradingSystemImpl tradingSystem;

    public static void setTradingSystem(TradingSystemImpl tradingSystem) {
        CategorySale.tradingSystem = tradingSystem;
    }

    //Integer saleID;
    String category;
    Integer  discountPercentage;
    //Expression expression;

    public CategorySale(Expression exp,String category, Integer discountPercentage) {
        super(exp);
        this.category = category;
        this.discountPercentage = discountPercentage;
    }

    public CategorySale(String category, Integer discountPercentage) {
        this.category = category;
        this.discountPercentage = discountPercentage;
    }

    @Override
    public Double calculateSale(ConcurrentHashMap<Integer, Integer> products, Double finalSale, Integer userID, Integer storeID) {
        double priceForCategory = 0.0;
        if (this.getExpression() != null) {
            if (this.getExpression().evaluate(products, finalSale, userID, storeID, 2)) {
                Set<Integer> keySet = products.keySet();
                for (Integer key : keySet
                ) {
                    Product p = tradingSystem.getProduct(storeID, key);
                    if (p.getCategory().equals(category)) {
                        priceForCategory = priceForCategory + p.getPrice() * products.get(p.getProductID());
                    }
                }
            }
            Double calculate=((double) discountPercentage / 100);
            Double ret = calculate * priceForCategory;
            return ret;
        }
        return 0.0;
    }

    @Override
    public Response checkValidity(int storeID) {
        if(0>discountPercentage||discountPercentage>100){
            return new Response(true, "discount percentage cant be negative");
        }
        if(this.getExpression()==null){
            return new Response(true,"there is not expression from some reason");
        }
        return this.getExpression().checkValidity(storeID);
    }

    public String getCategory(){
        return category;
    }

    public Integer getDiscountPercentage(){
        return discountPercentage;
    }

    @Override
    public String toString(){
        return category+" "+discountPercentage+" "+getExpression().toString();
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
