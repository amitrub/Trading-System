package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.ServiceLayer.DummyObject.DummySearch;

import java.util.LinkedList;
import java.util.List;

public class SearchSystem {

    private TradingSystem tradingSystem= TradingSystem.getInstance();

    public List<DummySearch> SearchProductByName(String name, int minprice, int maxprice, int prank , int srank){
        List<DummySearch> dummySearches = new LinkedList<>();
            for(Store store:tradingSystem.getStores().values()){
                if(((prank==-1 || store.getRate()>=srank) && !store.SearchByName(name, minprice, maxprice,prank).isEmpty())){
                    dummySearches.addAll(store.SearchByName(name, minprice, maxprice,prank));
                }
            }
        return dummySearches;
    }

    public List<DummySearch> SearchProductByCategory(String category, int minprice, int maxprice, int prank , int srank){
        List<DummySearch> dummySearches = new LinkedList<>();
        for(Store store:tradingSystem.getStores().values()){
            if(!store.SearchByCategory(category, minprice, maxprice,prank).isEmpty()){
                dummySearches.addAll(store.SearchByCategory(category, minprice, maxprice,prank));
            }
        }
        return dummySearches;
    }
}
