package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserStoreKey;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataManagerPermissions;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataOwnerPermissions;
import TradingSystem.Server.DataLayer.Repositories.*;
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

    public void RemoveOwner(int storeID, int ownerID){
        DataSubscriber owner = subscriberRepository.getOne(ownerID);
        DataStore store = storeRepository.getOne(storeID);
        DataOwnerPermissions ownerPermission = ownerPermissionsRepository.getOne(new UserStoreKey(ownerID, storeID));
        owner.RemoveOwner(store, ownerPermission);
        subscriberRepository.saveAndFlush(owner);

        List<DataOwnerPermissions> ownerPermissions = ownerPermissionsRepository.findAllByAppointment(owner);
        for (DataOwnerPermissions ownerPermissionByAppointment: ownerPermissions){
            DataSubscriber ownerByAppointment = ownerPermissionByAppointment.getSubscriber();
            ownerByAppointment.RemoveOwner(store, ownerPermissionByAppointment);
            subscriberRepository.saveAndFlush(ownerByAppointment);
        }

        List<DataManagerPermissions> managerPermissions = managerPermissionsRepository.findAllByAppointment(owner);
        for (DataManagerPermissions managerPermissionByAppointment: managerPermissions){
            DataSubscriber managerByAppointment = managerPermissionByAppointment.getSubscriber();
            managerByAppointment.RemoveManager(store, managerPermissionByAppointment);
            subscriberRepository.saveAndFlush(managerByAppointment);
        }

    }

    public void RemoveManager(int storeID, int managerID){
        DataSubscriber manager = subscriberRepository.getOne(managerID);
        DataStore store = storeRepository.getOne(storeID);
        DataManagerPermissions managerPermission = managerPermissionsRepository.getOne(new UserStoreKey(managerID, storeID));
        manager.RemoveManager(store, managerPermission);
        subscriberRepository.saveAndFlush(manager);
    }
}
