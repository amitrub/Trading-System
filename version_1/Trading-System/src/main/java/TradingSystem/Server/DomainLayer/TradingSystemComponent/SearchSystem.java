package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;

import java.util.LinkedList;
import java.util.List;

public class SearchSystem {

    private final TradingSystem tradingSystem= TradingSystem.getInstance();

    public List<DummyProduct> SearchProductByName(String name, int minprice, int maxprice, int prank , int srank){
        List<DummyProduct> dummyProducts = new LinkedList<>();
            for(Store store:tradingSystem.getStores().values()){
                if(((prank==-1 || store.getRate()>=srank) && !store.SearchByName(name, minprice, maxprice,prank).isEmpty())){
                    dummyProducts.addAll(store.SearchByName(name, minprice, maxprice,prank));
                }
            }
        return dummyProducts;
    }

    public List<DummyProduct> SearchProductByCategory(String category, int minprice, int maxprice, int prank , int srank){
        List<DummyProduct> dummyProducts = new LinkedList<>();
        for(Store store:tradingSystem.getStores().values()){
            if(!store.SearchByCategory(category, minprice, maxprice,prank).isEmpty()){
                dummyProducts.addAll(store.SearchByCategory(category, minprice, maxprice,prank));
            }
        }
        return dummyProducts;
    }
}
