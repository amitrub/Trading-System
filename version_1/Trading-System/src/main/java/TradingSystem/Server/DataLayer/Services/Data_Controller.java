package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class Data_Controller {

    @Autowired
    private StoreService storeService;
    @Autowired
    private SubcriberService subscriberService;
    @Autowired
    private GuestService guestService;

    public Data_Controller(){

    }

    public void AddStore(DataStore store){
        storeService.Addstore(store);
    }

    public int AddGuest(){
        return guestService.Addguest();
    }

    public int AddSubscriber(DataSubscriber dataSubscriber){
        return subscriberService.AddSubscriber(dataSubscriber);
    }
}
