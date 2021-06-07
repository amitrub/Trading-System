package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.DataShoppingBagProductKey;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.PermissionKey;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserStoreKey;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataManagerPermissions;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataOwnerPermissions;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ManagerPermissionsRepository extends JpaRepository<DataManagerPermissions, UserStoreKey> {
    List<DataManagerPermissions> findAllBySubscriber(DataSubscriber subscriber);

    List<DataManagerPermissions> findAllByAppointment(DataSubscriber appointment);
}
