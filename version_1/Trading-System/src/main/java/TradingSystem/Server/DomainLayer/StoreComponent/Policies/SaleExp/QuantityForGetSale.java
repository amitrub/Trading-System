package TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class QuantityForGetSale extends SimpleExpression {

    private Integer productId;
    private Integer quantityForSale;

    public QuantityForGetSale(Integer expId,Integer productId,  Integer quantityForSale) {
        super(expId);
        this.productId=productId;
        this.quantityForSale = quantityForSale;
    }

    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID){
      if(products.get(productId)!=null){
          return products.get(productId)>=quantityForSale;
      }
      return false;
    }

/*
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID){
        Integer quantityInTheBag=0;
        Set<Integer> keySet=products.keySet();
        for(Integer key:keySet){
            quantityInTheBag=quantityInTheBag+products.get(key);
        }
        return quantityInTheBag>=quantityForSale;
    }

 */
}
