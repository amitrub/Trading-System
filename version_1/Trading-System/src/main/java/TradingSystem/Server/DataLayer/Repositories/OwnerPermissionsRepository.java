package TradingSystem.Server.DataLayer.Repositories;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.PermissionKey;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserStoreKey;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataManagerPermissions;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataOwnerPermissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerPermissionsRepository extends JpaRepository<DataOwnerPermissions, UserStoreKey> {
    List<DataOwnerPermissions> findAllBySubscriber(DataSubscriber subscriber);

    List<DataOwnerPermissions> findAllByAppointment(DataSubscriber appointment);

    @Override
    void deleteById(UserStoreKey userStoreKey);

    void deleteAllByStoreAndAppointment(DataStore store, DataSubscriber appointment);
}
