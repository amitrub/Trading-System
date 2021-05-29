package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingHistory.DataShoppingHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingHistoryRepository extends JpaRepository<DataShoppingHistory,Integer> {

//    DataShoppingHistory findByUserID(int userId);
//
    List<DataShoppingHistory> findAllBySubscriber(DataSubscriber subscriber);
    List<DataShoppingHistory> findAllByStore(DataStore store);
}

