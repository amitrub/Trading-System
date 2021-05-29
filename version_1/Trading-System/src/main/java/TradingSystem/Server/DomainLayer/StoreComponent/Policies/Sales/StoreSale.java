package TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class StoreSale extends SimpleSale {

    @Autowired
    TradingSystemImplRubin tradingSystem;

    private Integer storeID;
    private Double  discountPercentage;;

    public StoreSale(Expression exp,Integer storeID, Double discountPercentage) {
        super(exp);
        this.storeID = storeID;
        this.discountPercentage = discountPercentage;
    }

    public StoreSale(Integer storeID, Double discountPercentage) {
        this.storeID = storeID;
        this.discountPercentage = discountPercentage;
    }

    @Override
    public Double calculateSale(ConcurrentHashMap<Integer, Integer> products, Double finalSale, Integer userID, Integer storeID) {
      if(this.getExpression()!=null) {
          if (this.getExpression().evaluate(products, finalSale, userID, storeID)) {
              return (discountPercentage / 100) * finalSale;
          }
      }
        return 0.0;
    }

    @Override
    public Response checkValidity(int storeID) {
        if(0>discountPercentage||discountPercentage>100){
            return new Response(true, "discount percentage cant be negative");
        }
        if(tradingSystem.stores.get(storeID)==null){
            return new Response(true,"store dont exist in the system");
        }
        if(this.getExpression()==null){
            return new Response(true,"there is not expression from some reason");
        }
        return this.getExpression().checkValidity(storeID);
    }

}