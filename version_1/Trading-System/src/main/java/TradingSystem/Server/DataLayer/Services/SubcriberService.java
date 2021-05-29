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
public class SubcriberService {

    @Autowired
    SubscriberRepository subscriberRepository;


    @Autowired
    StoreRepository storeRepository;


    public int AddSubscriber(String userName, String password){
        DataSubscriber subscriber = new DataSubscriber(userName,password);
        DataSubscriber ret = subscriberRepository.saveAndFlush(subscriber);
        return ret.getUserID();
    }

    public DataSubscriber GetSubscriber(String userName, String password) {
//        return null;
        DataSubscriber subscriber = subscriberRepository.findByName(userName);
        return subscriber;
    }

    public List<DataSubscriber> getAllSubscribers(){
        return subscriberRepository.findAll();
    }

    public Optional<DataSubscriber> findSubscriberById(int subscriberid){
        return subscriberRepository.findById(subscriberid);
    }
    public void deleteAll(){
        subscriberRepository.deleteAll();
    }

    public List<DataSubscriber> findAllByStoresManagerContains(int storeid){
        DataStore store=storeRepository.getOne(storeid);
        return subscriberRepository.findAllByStoresManagerContains(store);
    }
    public List<DataSubscriber> findAllByStoresOwnedContains(int storeid){
        DataStore store=storeRepository.getOne(storeid);
        return subscriberRepository.findAllByStoresOwnerContains(store);
    }
}
