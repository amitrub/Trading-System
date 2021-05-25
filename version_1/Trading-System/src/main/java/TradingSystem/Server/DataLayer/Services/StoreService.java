package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Repositories.StoreRepository;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class StoreService {

    @Autowired
    StoreRepository storeRepository;

    public DummyStore Addstore(DummyStore store){
        return storeRepository.saveAndFlush(store);
    }

    public List<DummyStore> getAllStores(){
        return storeRepository.findAll();
    }
}
