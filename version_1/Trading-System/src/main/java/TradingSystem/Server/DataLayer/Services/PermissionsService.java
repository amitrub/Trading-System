package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserStoreKey;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataManagerPermissionType;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataManagerPermissions;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataOwnerPermissions;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataPermission;
import TradingSystem.Server.DataLayer.Repositories.*;
import TradingSystem.Server.DomainLayer.UserComponent.PermissionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionsService {

    @Autowired
    SubscriberRepository subscriberRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    OwnerPermissionsRepository ownerPermissionsRepository;
    @Autowired
    OwnerPermissionTypeRepository ownerPermissionTypeRepository;
    @Autowired
    ManagerPermissionsRepository managerPermissionsRepository;
    @Autowired
    ManagerPermissionTypeRepository managerPermissionTypeRepository;

    public List<DataOwnerPermissions> getOwnerPermissions(int userID, int storeID){
        DataSubscriber subscriber = subscriberRepository.getOne(userID);
        List<DataOwnerPermissions> ownerPermissions = ownerPermissionsRepository.findAllBySubscriber(subscriber);
        return ownerPermissions;
    }

    public void EditManagerPermissions(int storeID, int managerID, List<PermissionEnum.Permission> permissions) {
        DataSubscriber manager = subscriberRepository.getOne(managerID);
        DataStore store = storeRepository.getOne(storeID);
        store.RemoveManager(manager);
        storeRepository.saveAndFlush(store);
        DataManagerPermissions managerPermission = managerPermissionsRepository.getOne(new UserStoreKey(managerID, storeID));
        manager.RemoveManagerPermission(managerPermission);
        subscriberRepository.save(manager);

        int appointmentID = managerPermission.getAppointment().getUserID();


        store = storeRepository.getOne(storeID);
        DataSubscriber newManager = subscriberRepository.getOne(managerID);
        store.AddNewManager(newManager);
        storeRepository.saveAndFlush(store);
        DataSubscriber appointment = subscriberRepository.getOne(appointmentID);
        DataManagerPermissions dataManagerPermissions = new DataManagerPermissions(newManager, store, appointment);
        managerPermissionsRepository.saveAndFlush(dataManagerPermissions);
        for (PermissionEnum.Permission permission: permissions){
            DataPermission.Permission dataPermission = DataPermission.toDataPermission(permission);
            DataManagerPermissionType permissionType = new DataManagerPermissionType(dataManagerPermissions, dataPermission);
            managerPermissionTypeRepository.saveAndFlush(permissionType);
        }

    }

    public void RemoveOwner(int storeID, int ownerID){
        DataSubscriber owner = subscriberRepository.getOne(ownerID);
        DataStore store = storeRepository.getOne(storeID);
        store.RemoveOwner(owner);
        storeRepository.saveAndFlush(store);

        DataOwnerPermissions ownerPermission = ownerPermissionsRepository.getOne(new UserStoreKey(ownerID, storeID));
        owner.RemoveOwnerPermission(ownerPermission);
        subscriberRepository.saveAndFlush(owner);

        List<DataOwnerPermissions> ownerPermissions = ownerPermissionsRepository.findAllByAppointment(owner);
        for (DataOwnerPermissions ownerPermissionByAppointment: ownerPermissions){
            DataSubscriber ownerByAppointment = ownerPermissionByAppointment.getSubscriber();
            store.RemoveOwner(ownerByAppointment);
            storeRepository.saveAndFlush(store);
            ownerByAppointment.RemoveOwnerPermission(ownerPermissionByAppointment);
            subscriberRepository.saveAndFlush(ownerByAppointment);
        }

        List<DataManagerPermissions> managerPermissions = managerPermissionsRepository.findAllByAppointment(owner);
        for (DataManagerPermissions managerPermissionByAppointment: managerPermissions){
            DataSubscriber managerByAppointment = managerPermissionByAppointment.getSubscriber();
            store.RemoveManager(managerByAppointment);
            storeRepository.saveAndFlush(store);
            managerByAppointment.RemoveManagerPermission(managerPermissionByAppointment);
            subscriberRepository.saveAndFlush(managerByAppointment);
        }
    }

    public void RemoveManager(int storeID, int managerID){
        DataSubscriber manager = subscriberRepository.getOne(managerID);
        DataStore store = storeRepository.getOne(storeID);
        store.RemoveManager(manager);
        storeRepository.saveAndFlush(store);
        DataManagerPermissions managerPermission = managerPermissionsRepository.getOne(new UserStoreKey(managerID, storeID));
        manager.RemoveManagerPermission(managerPermission);
        subscriberRepository.save(manager);
    }
}
