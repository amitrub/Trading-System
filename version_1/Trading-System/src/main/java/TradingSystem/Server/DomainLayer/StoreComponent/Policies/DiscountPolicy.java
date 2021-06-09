package TradingSystem.Server.DomainLayer.StoreComponent.Policies;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DiscountPolicy {

    private Integer storeID;
   // private static int nextSaleID=0;
    private Sale sale;

    @Autowired
    TradingSystemImplRubin tradingSystem;

    public DiscountPolicy(Integer storeID,Sale s){
     this.storeID=storeID;
     this.sale=s;
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
    public int getStoreId(){
        return storeID;
    }

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
