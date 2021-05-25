package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriberRepository extends JpaRepository<DataSubscriber,Integer> {

    void deleteByUserID(int userid);
}
