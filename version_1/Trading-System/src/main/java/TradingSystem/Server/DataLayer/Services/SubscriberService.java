package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Repositories.GuestRepository;
import TradingSystem.Server.DataLayer.Repositories.SubsriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class SubscriberService {

    @Autowired
    SubsriberRepository subsriberRepository;

    public SubscriberService() {

    }
}
