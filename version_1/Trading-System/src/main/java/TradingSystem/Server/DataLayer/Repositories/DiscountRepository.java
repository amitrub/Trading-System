package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy.DataDiscountPolicy;
import TradingSystem.Server.DataLayer.Data_Modules.DiscountPolicy.DataSale;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserStoreKey;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataManagerPermissions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<DataDiscountPolicy, Integer> {
}
