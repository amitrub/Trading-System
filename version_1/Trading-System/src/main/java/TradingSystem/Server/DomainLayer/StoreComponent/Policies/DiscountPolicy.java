package TradingSystem.Server.DomainLayer.StoreComponent.Policies;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DiscountPolicy {

    private Integer storeID;
    private static int nextSaleID=0;
    private  ConcurrentHashMap<Integer,Sale> Sales;

    public TradingSystemImpl tradingSystemImpl;

    public void DiscountPolicy(Integer storeID){
     this.storeID=storeID;
     this.Sales=new ConcurrentHashMap<>();
     this.tradingSystemImpl = TradingSystemImpl.getInstance();
    }

    private static synchronized int getNextSaleID() {
        nextSaleID++;
        return nextSaleID;
    }

    public void AddSale(Sale sale){
        int saleID=nextSaleID;
        this.Sales.put(saleID,sale);
    }

    public void RemoveSale(Integer saleID){
        this.Sales.remove(saleID);
    }

   //TODO check
    public Double calculateSale(ConcurrentHashMap<Integer,Integer> products,Integer userID){
        Double price=0.0;

        Set<Integer> keySetProdects=products.keySet();
        for (Integer key:keySetProdects
             ) {
            Double tmpPrice= tradingSystemImpl.getProduct(storeID,key).getPrice();
            price=price+tmpPrice;
        }


        Set<Integer> keySetSales=Sales.keySet();
        for (Integer key:keySetSales
        ) {
            Double sale=Sales.get(key).calculateSale(products,price, userID,storeID );
            price=price-sale;
        }

        return price;
    }

}
