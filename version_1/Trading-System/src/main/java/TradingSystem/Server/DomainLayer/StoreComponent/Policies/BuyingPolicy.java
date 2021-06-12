package TradingSystem.Server.DomainLayer.StoreComponent.Policies;

import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DBExpression;
import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DataBuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.AndComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.CompositeExpression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.OrComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.*;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ConcurrentHashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BuyingPolicy {

    private Integer storeID;
    private Expression exp;


    @Autowired
    TradingSystemImpl tradingSystem;


    public BuyingPolicy(Integer storeID,Expression exp){
        this.storeID=storeID;
        this.exp=exp;
    }

    public BuyingPolicy(DataBuyingPolicy buyingPolicy) {
        this.storeID = buyingPolicy.getStore();
        this.exp= buildExp(buyingPolicy.getExpression());
    }

    public void setExp(Expression exp) {
        this.exp = exp;
    }

    public Expression getExp() {
        return exp;
    }

    public boolean checkEntitlement(ConcurrentHashMap<Integer,Integer> products, Integer userID, Double finalPrice){
       if(exp!=null) {
           return exp.evaluate(products, finalPrice, userID, storeID);
       }
       return true;
    }

    public Integer getStoreID(){
        return this.storeID;
    }

    public Expression buildExp(DBExpression expression){
        switch (expression.getType()) {
            case "OR":
                CompositeExpression orComposite = new OrComposite();
                for (DBExpression exp : expression.getSubdomains()) {
                    orComposite.add(buildExp(exp));
                }
                return orComposite;
            case "AND":
                CompositeExpression andComposite = new AndComposite();
                for (DBExpression exp : expression.getSubdomains()) {
                    andComposite.add(buildExp(exp));
                }
                return andComposite;
            case "Limit_Category":
                return new AgeLimitForCategory(expression.getMinAge(), expression.getCategory());
            case "Limit_Product":
                return new AgeLimitForProduct(expression.getMinAge(), expression.getProductid());
            case "Limit_Store":
                return new AgeLimitForStore(expression.getMinAge(), expression.getStoreId());
            case "Quantity_Store":
                return new QuantityLimitForStore(expression.getMaxQuantity(), expression.getStoreId());
            case "Quantity_Product":
                return new QuantityLimitForProduct(expression.getMaxQuantity(), expression.getProductid());
            case "Quantity_Category":
                return new QuantityLimitForCategory(expression.getMaxQuantity(), expression.getCategory());
        }
        return null;
    }
}
