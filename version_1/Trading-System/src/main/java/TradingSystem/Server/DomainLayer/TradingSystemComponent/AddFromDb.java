package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Services.Data_Controller;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.UserComponent.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class AddFromDb {

    public final TradingSystem tradingSystemImpl;
    public final Data_Controller data_controller;

    public AddFromDb(TradingSystem tradingSystemImpl, Data_Controller data_controller) {
        this.tradingSystemImpl = tradingSystemImpl;
        this.data_controller= data_controller;
    }

    public void UploadAllUsers(){
        List<DataSubscriber> subscribers= data_controller.getAllSubscribers();
        ConcurrentHashMap<Integer, User> res= tradingSystemImpl.getSubscribers();
        for(DataSubscriber subscriber:subscribers){
            res.putIfAbsent(subscriber.getUserID(),new User(subscriber));
        }
        tradingSystemImpl.setSubscribers(res);
    }

    public void UploadAllStores(){
        List<DataStore> stores= data_controller.getAllStores();
        ConcurrentHashMap<Integer, Store> res= tradingSystemImpl.getStores();
        for(DataStore store:stores){
            res.putIfAbsent(store.getStoreID(),new Store(store));
        }
        tradingSystemImpl.setStores(res);
    }

    public void UploadStore(int storeid){
        if(!tradingSystemImpl.getStores().containsKey(storeid)){
            Optional<DataStore> optionalDataStore=data_controller.findStorebyId(storeid);
            if(optionalDataStore.isPresent()){
                ConcurrentHashMap<Integer,Store> stores=tradingSystemImpl.getStores();
                stores.put(optionalDataStore.get().getStoreID(),new Store(optionalDataStore.get()));
                tradingSystemImpl.setStores(stores);
            }
        }
    }

    public void UploadSubscriber(int subscriberid){
        if(!tradingSystemImpl.getSubscribers().containsKey(subscriberid)){
            Optional<DataSubscriber> optionalDataSubscriber=data_controller.findSubscriberById(subscriberid);
            if(optionalDataSubscriber.isPresent()){
                ConcurrentHashMap<Integer,User> subscribers=tradingSystemImpl.getSubscribers();
                subscribers.put(optionalDataSubscriber.get().getUserID(),new User(optionalDataSubscriber.get()));
                tradingSystemImpl.setSubscribers(subscribers);
            }
        }
    }
}
