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
    public Data_Controller(){

    }

    public int AddStore(DataStore store){

        return storeService.Addstore(store);
    }

    public int AddSubscriber(DataSubscriber dataSubscriber){
        return subscriberService.AddSubscriber(dataSubscriber);
    }

}
