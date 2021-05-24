package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Repositories.GuestRepository;
import TradingSystem.Server.DataLayer.Repositories.SubsriberRepository;
import TradingSystem.Server.ServiceLayer.DummyObject.DummySubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class SubscriberService {

    @Autowired
    SubsriberRepository subsriberRepository;

    public SubscriberService() {

    }

    public List<DummySubscriber> getAllSubscribers(){
        return subsriberRepository.findAll();
    }

    public void Addsubscriber(DummySubscriber dummySubscriber){
        subsriberRepository.saveAndFlush(dummySubscriber);
    }
}
