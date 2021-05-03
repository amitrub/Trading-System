package TradingSystem.Server.DomainLayer.StoreComponent.Policies;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DiscountPolicy {

    private Integer storeID;
   // private static int nextSaleID=0;
    private Sale sale;

    public TradingSystemImpl tradingSystem;

    public DiscountPolicy(Integer storeID,Sale s){
     this.storeID=storeID;
     this.sale=s;
     this.tradingSystem=TradingSystemImpl.getInstance();
    }
/*
    public static synchronized int getNextSaleID() {
        nextSaleID++;
        return nextSaleID;
    }

    public void RemoveSale(Integer saleID){
        this.Sales.remove(saleID);
    }
*/

    public Sale getSale() {
        return sale;
    }

    public void AddSale(Sale sale){
        this.sale=sale;
    }
   //TODO check
    public Double calculatePrice(ConcurrentHashMap<Integer,Integer> products, Integer userID, Double priceBeforeSale){
       if(sale!=null) {
           Double sale = this.sale.calculateSale(products, priceBeforeSale, userID, storeID);
           return priceBeforeSale - sale;
       }
       return 0.0;
    }




}
