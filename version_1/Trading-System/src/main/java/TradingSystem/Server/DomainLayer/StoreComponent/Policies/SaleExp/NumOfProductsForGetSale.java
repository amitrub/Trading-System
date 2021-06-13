package TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.SimpleExpression;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.concurrent.ConcurrentHashMap;

public class NumOfProductsForGetSale extends SimpleExpression {

   private Integer numOfProductsForSale;

    public NumOfProductsForGetSale(Integer numOfProductForSale) {
      this.numOfProductsForSale=numOfProductForSale;
    }

    @Override
    public Boolean evaluate(ConcurrentHashMap<Integer, Integer> products, Double finalPrice, Integer userID, Integer storeID, int mode) {
        return products.size()>=numOfProductsForSale;
    }

    @Override
    public Response checkValidity(int storeID) {
        if(0>numOfProductsForSale){
            return new Response(true, "numOfProductsForSale cant be negative");
        }
        return new Response("correct");
    }

    public Integer getNumOfProductsForSale(){
        return numOfProductsForSale;
    }

    @Override
    public String toString(){
        return "num of products "+numOfProductsForSale;
    }
}
