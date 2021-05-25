package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataGuest;
import TradingSystem.Server.DataLayer.Repositories.GuestRepository;
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

    public List<DataGuest> getAllGuests(){
        return guestRepository.findAll();
    }

    public int Addguest (){
        DataGuest user =new DataGuest();
        DataGuest dataGuest= guestRepository.saveAndFlush(user);
        return dataGuest.getUserID();
    }

    public void removeguest(DataGuest user){
        guestRepository.delete(user);
    }
}
