package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<DataStore,Integer> {
  List<DataStore> findAllByFounder(DataSubscriber subscriber);
  List<DataStore> findAllByOwnersContains(DataSubscriber subscriber);
  List<DataStore> findAllByManagersContains(DataSubscriber subscriber);
  List<DataStore> findAllByDateBetween(Date begin, Date end);
}
