package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Repositories.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class SubcriberService {

    @Autowired
    SubscriberRepository subscriberRepository;

    public int AddSubscriber(DataSubscriber subscriber){
        DataSubscriber ret= subscriberRepository.saveAndFlush(subscriber);
        return ret.getUserID();
    }

    public void removeSubscriberByid(int id){
        subscriberRepository.deleteByUserID(id);
    }
}
