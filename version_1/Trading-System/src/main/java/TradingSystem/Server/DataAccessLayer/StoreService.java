package TradingSystem.Server.DataAccessLayer;

import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class StoreService implements Service<DummyStore> {

    @Autowired
    StoreRepository storeRepository;

    public StoreService(){
    }

    public DummyStore findDummyStoreById(int Id){
        return storeRepository.findDummyStoreById(Id);
    }

    public DummyStore addStore(DummyStore store){
        return storeRepository.saveAndFlush(store);
    }
}
