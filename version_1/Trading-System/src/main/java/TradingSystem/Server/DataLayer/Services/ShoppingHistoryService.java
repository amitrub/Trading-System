package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Repositories.ShoppingHistoryRepository;
import TradingSystem.Server.DataLayer.Repositories.StoreRepository;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ShoppingHistoryService {

    @Autowired
    ShoppingHistoryRepository shoppingHistoryRepository;

    public ShoppingHistoryService(){

    }

    public DummyShoppingHistory ShoppingHistory(DummyShoppingHistory shoppingHistory){
        return shoppingHistoryRepository.saveAndFlush(shoppingHistory);
    }

    public List<DummyShoppingHistory> getAllShoppingHistory(){
        return shoppingHistoryRepository.findAll();
    }
}
