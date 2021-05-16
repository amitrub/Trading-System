package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.ConditionRoles;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ExistProduct implements ConditionRole {

    private Integer productID;

    public ExistProduct(Integer productID) {
        this.productID = productID;
    }

    @Override
    public boolean checkRole( ConcurrentHashMap<Integer, Integer> products, Double finalSale, Integer userID, Integer storeID) {
       for (Integer key : products.keySet()){
           if(key==productID){
               return true;
           }
       }
       return false;
    }
}
