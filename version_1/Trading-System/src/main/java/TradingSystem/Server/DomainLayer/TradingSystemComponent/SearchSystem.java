package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.Service_Layer.DummyStore;

import java.util.LinkedList;
import java.util.List;

public class SearchSystem {

    private TradingSystem tradingSystem= TradingSystem.getInstance();

    public List<DummyStore> SearchProductByName(String name){
        List<DummyStore> dummyStores = new LinkedList<>();
//            for(Store store:tradingSystem.getStores().values()){
//                if(store.SearchByName(name)){
//                    dummyStores.add(new DummyStore(store.getId(),store.getName(),store.getFounderID(),store.getOwnersIDs(),store.getManagersIDs(),store.getDiscountPolicy(),store.getBuyingPolicy(),store.getRate(),store.getNumberOfRatings(),store.getShoppingHistory()));
//                }
//            }
        return dummyStores;
    }
//
//    public List<DummyStore> SearchProductByCategory(String category){
//        List<DummyStore> dummyStores = new LinkedList<>();
//        for(Store store:tradingSystem.getStores().values()){
//            if(store.SearchByCategory(category)){
//                dummyStores.add(new DummyStore(store.getId(),store.getName(),store.getFounderID(),store.getOwnersIDs(),store.getManagersIDs(),store.getDiscountPolicy(),store.getBuyingPolicy(),store.getRate(),store.getNumberOfRatings(),store.getShoppingHistory()));
//            }
//        }
//        return dummyStores;
//    }
}
