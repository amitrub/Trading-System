package TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class QuantityForGetSale extends SimpleExpression {


    private Integer productId;
    private Integer quantityForSale;

    public QuantityForGetSale(Integer productId,  Integer quantityForSale) {
        this.productId=productId;
        this.quantityForSale = quantityForSale;
    }

    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID){
      if(products.get(productId)!=null){
          return products.get(productId)>=quantityForSale;
      }
      return false;
    }

    @Override
    public Response checkValidity(int storeID) {
        if(0>quantityForSale){
            return new Response(true, "quantityForSale cant be negative");
        }
        if(this.tradingSystem.stores.get(storeID).getProduct(productId)==null){
            return new Response(true, "product is not exist in the store");
        }
        return new Response("correct");
    }

    public Integer getQuantityForSale(){
        return quantityForSale;
    }
}
