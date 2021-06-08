package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.DataLayer.Data_Modules.Keys.PermissionKey;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataManagerPermissionType;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataOwnerPermissionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerPermissionTypeRepository extends JpaRepository<DataManagerPermissionType, PermissionKey> {
}
