package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.DataLayer.Data_Modules.Keys.PermissionKey;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserStoreKey;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataOwnerPermissionType;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataOwnerPermissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OwnerPermissionTypeRepository extends JpaRepository<DataOwnerPermissionType, PermissionKey> {
}
