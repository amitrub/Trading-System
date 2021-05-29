package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
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

    public int AddSubscriber(String userName, String password){
        DataSubscriber subscriber = new DataSubscriber(userName,password);
        DataSubscriber ret = subscriberRepository.saveAndFlush(subscriber);
        return ret.getUserID();
    }

    public void removeSubscriberByid(int id){
        subscriberRepository.deleteByUserID(id);
    }

    public List<DataSubscriber> getAllSubscribers(){
        return subscriberRepository.findAll();
    }

    public Optional<DataSubscriber> findSubscriberById(int subscriberid){
        return subscriberRepository.findById(subscriberid);
    }
}
