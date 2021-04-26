package TradingSystem.Server.DomainLayer.StoreComponent.Policies;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DiscountPolicy {

    Integer storeID;
    private  List<Sale> Sales;
    TradingSystem tradingSystem;

    public void DiscountPolicy(Integer storeID){
     this.storeID=storeID;
     this.Sales=new LinkedList<>();
     this.tradingSystem=TradingSystem.getInstance();
    }

    public void AddSale(Sale sale){
        this.Sales.add(sale);
    }

   //TODO check
    public Double calculateSale(ConcurrentHashMap<Integer,Integer> products){
        Double price=0.0;
        Set<Integer> keySet=products.keySet();
        for (Integer key:keySet
             ) {
            Double tmpPrice=tradingSystem.getProduct(storeID,key).getPrice();
            price=price+tmpPrice;
        }

        for (Sale s:Sales
             ) {
            Double sale=s.calculateSale(products,price);
            price=price-sale;
        }
       return price;
    }

}
