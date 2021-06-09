package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DataBuyingPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyingRepository extends JpaRepository<DataBuyingPolicy,Integer> {

}
