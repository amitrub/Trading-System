package TradingSystem.Server.DomainLayer.StoreComponent.Policies;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DiscountPolicy {

    private Integer storeID;
   // private static int nextSaleID=0;
    private Sale sale;

    public TradingSystem tradingSystem;

    public DiscountPolicy(Integer storeID,Sale s){
     this.storeID=storeID;
     this.sale=s;
     this.tradingSystem=TradingSystem.getInstance();
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

    public void AddSale(Sale sale){
        this.sale=sale;
    }
   //TODO check
    public Double calculatePrice(ConcurrentHashMap<Integer,Integer> products, Integer userID, Double priceBeforeSale){
        Double sale =this.sale.calculateSale(products,priceBeforeSale,userID,storeID);
        return priceBeforeSale-sale;
    }




}
