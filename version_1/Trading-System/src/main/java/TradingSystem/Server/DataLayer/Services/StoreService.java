package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Repositories.StoreRepository;
import TradingSystem.Server.DataLayer.Repositories.SubscriberRepository;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StoreService {

    @Autowired
    StoreRepository storeRepository;
    @Autowired
    SubscriberRepository subscriberRepository;

    public int AddStore(String storeName, int founderID){
        DataStore store = new DataStore(storeName);
        DataSubscriber founder = subscriberRepository.getOne(founderID);
        store.setFounder(founder);
        DataStore dataStore = storeRepository.saveAndFlush(store);
        return dataStore.getStoreID();
    }


    public void AddNewOwner(int storeID, int newOwnerID) {
        DataStore store = storeRepository.getOne(storeID);
        DataSubscriber newOwner = subscriberRepository.getOne(newOwnerID);
        store.AddNewOwner(newOwner);
        storeRepository.saveAndFlush(store);
    }

    public void AddNewManager(int storeID, int newManagerID) {
        DataStore store = storeRepository.getOne(storeID);
        DataSubscriber newManager = subscriberRepository.getOne(newManagerID);
        store.AddNewManager(newManager);
        storeRepository.saveAndFlush(store);
    }

    public List<DataStore> getAllStores(){
        return storeRepository.findAll();
    }

    public Optional<DataStore> findStorebyId(int storeid){
       Optional<DataStore> res= storeRepository.findById(storeid);
        return res;
    }
    public void deleteAll(){
        storeRepository.deleteAll();
    }

    public List<DataStore> getAllStoresOfOwner(int userId){
        DataSubscriber subscriber= subscriberRepository.getOne(userId);
        return storeRepository.findAllByOwnersContains(subscriber);
    }

    public List<DataStore> getAllStoresofFounder(int userId){
        DataSubscriber subscriber= subscriberRepository.getOne(userId);
        return storeRepository.findAllByFounder(subscriber);
    }

    public List<DataStore> getAllStoresofManager(int userId){
        DataSubscriber subscriber= subscriberRepository.getOne(userId);
        return storeRepository.findAllByManagersContains(subscriber);
    }
//    public void AddNewOwner(int storeId, Integer userId, Integer newOwnerId){
//        DataSubscriber owner= subscriberRepository.getOne(userId);
//        DataSubscriber newowner= subscriberRepository.getOne(newOwnerId);
//        DataStore store=storeRepository.getOne(storeId);
//
//    }
}
