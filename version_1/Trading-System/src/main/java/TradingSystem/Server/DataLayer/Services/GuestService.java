package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Repositories.GuestRepository;
import TradingSystem.Server.ServiceLayer.DummyObject.DummySubscriber;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class GuestService {

    @Autowired
    GuestRepository guestRepository;

    public GuestService() {

    }

    public List<DummyUser> getAllGuests(){
        return guestRepository.findAll();
    }

    public void Addguest (DummyUser user){
        guestRepository.saveAndFlush(user);
    }

    public void removeguest(DummyUser user){
        guestRepository.delete(user);
    }
}
