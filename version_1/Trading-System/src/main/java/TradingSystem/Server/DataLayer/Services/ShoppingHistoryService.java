//package TradingSystem.Server.DataLayer.Services;
//
//import TradingSystem.Server.DataLayer.Data_Modules.DataShoppingHistory;
//import TradingSystem.Server.DataLayer.Repositories.ShoppingHistoryRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import javax.transaction.Transactional;
//import java.util.List;
//
//@Service
//@Transactional
//public class ShoppingHistoryService {
//
//    @Autowired
//    ShoppingHistoryRepository shoppingHistoryRepository;
//
//    public ShoppingHistoryService(){
//
//    }
//
//    public DataShoppingHistory findByuserId(int userId){
//        return shoppingHistoryRepository.findByUserID(userId);
//    }
//
//    public DataShoppingHistory ShoppingHistory(DataShoppingHistory shoppingHistory){
//        return shoppingHistoryRepository.saveAndFlush(shoppingHistory);
//    }
//
//    public List<DataShoppingHistory> getShoppingHistoryByStoreId(int storeId){
//        return shoppingHistoryRepository.findAllByStoreID(storeId);
//    }
//
//    public List<DataShoppingHistory> getAllShoppingHistory(){
//        return shoppingHistoryRepository.findAll();
//    }
//}
