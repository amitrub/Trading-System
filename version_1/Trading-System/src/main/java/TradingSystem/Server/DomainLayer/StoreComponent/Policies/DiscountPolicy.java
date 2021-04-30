package TradingSystem.Server.DomainLayer.StoreComponent.Policies;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DiscountPolicy {

    private Integer storeID;
    private static int nextSaleID=0;
    private  ConcurrentHashMap<Integer,Sale> Sales;

    public TradingSystem tradingSystem;

    public DiscountPolicy(Integer storeID){
     this.storeID=storeID;
     this.Sales=new ConcurrentHashMap<>();
     this.tradingSystem=TradingSystem.getInstance();
    }

    public static synchronized int getNextSaleID() {
        nextSaleID++;
        return nextSaleID;
    }

    public void AddSale(Sale sale){

        this.Sales.put(sale.getID(),sale);
    }

    public void RemoveSale(Integer saleID){
        this.Sales.remove(saleID);
    }

   //TODO check
    public Double calculatePrice(ConcurrentHashMap<Integer,Integer> products, Integer userID, Double priceBeforeSale){
        Double sale =0.0;

        Set<Integer> keySetSales=Sales.keySet();
        for (Integer key:keySetSales
        ) {
            Double s=Sales.get(key).calculateSale(products,priceBeforeSale, userID,storeID );
            sale=s+sale;
        }

        return priceBeforeSale-sale;
    }

}
