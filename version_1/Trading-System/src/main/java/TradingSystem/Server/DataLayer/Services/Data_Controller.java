package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class Data_Controller {

    @Autowired
    private StoreService storeService;
    //    Singleton
//    private static Data_Controller data_controller = null;
//
//    public static Data_Controller getInstance() {
//        if (data_controller == null) {
//            System.out.println("hello im data controller");
//            data_controller = new Data_Controller();
//        }
//        return data_controller;
//    }

    public void AddStore(DummyStore store){
        storeService.Addstore(store);
    }
}
