package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class StoreService {

    @Autowired
    StoreRepository storeRepository;

    public DataStore Addstore(DataStore store){
        return storeRepository.saveAndFlush(store);
    }

    public List<DataStore> getAllStores(){
        return storeRepository.findAll();
    }
}
