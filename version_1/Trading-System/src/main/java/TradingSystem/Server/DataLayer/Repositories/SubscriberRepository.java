package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubscriberRepository extends JpaRepository<DataSubscriber,Integer> {

    DataSubscriber findByName(String name);
    List<DataSubscriber> findAllByStoresManagerContains(DataStore store);
    List<DataSubscriber> findAllByStoresOwnerContains(DataStore store);




    //    @Query("SELECT * FROM subscribers WHERE subscribers.name = ?1")
//    DataSubscriber findByName(String name);

//    void deleteByUserID(int userid);
}
