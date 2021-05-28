package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Repositories.StoreRepository;
import TradingSystem.Server.DataLayer.Repositories.SubscriberRepository;
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

    public List<DataStore> getAllStores(){
        return storeRepository.findAll();
    }

    public DataStore findStorebyId(int storeid){
       Optional<DataStore> res= storeRepository.findById(storeid);
        return res.orElse(null);
    }
}
