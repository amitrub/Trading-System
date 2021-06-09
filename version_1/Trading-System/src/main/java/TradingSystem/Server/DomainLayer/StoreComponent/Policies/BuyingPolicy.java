package TradingSystem.Server.DomainLayer.StoreComponent.Policies;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BuyingPolicy {

    private Integer storeID;
    private Expression exp;


    @Autowired
    TradingSystemImplRubin tradingSystem;


    public BuyingPolicy(Integer storeID,Expression exp){
        this.storeID=storeID;
        this.exp=exp;
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
}
