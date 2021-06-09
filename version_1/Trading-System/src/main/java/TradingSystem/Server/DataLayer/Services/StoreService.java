package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserStoreKey;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.*;
import TradingSystem.Server.DataLayer.Repositories.*;
import TradingSystem.Server.DomainLayer.UserComponent.ManagerPermission;
import TradingSystem.Server.DomainLayer.UserComponent.OwnerPermission;
import TradingSystem.Server.DomainLayer.UserComponent.PermissionEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StoreService {

    @Autowired
    StoreRepository storeRepository;
    @Autowired
    SubscriberRepository subscriberRepository;
    @Autowired
    OwnerPermissionsRepository ownerPermissionsRepository;
    @Autowired
    OwnerPermissionTypeRepository ownerPermissionTypeRepository;
    @Autowired
    ManagerPermissionsRepository managerPermissionsRepository;
    @Autowired
    ManagerPermissionTypeRepository managerPermissionTypeRepository;

    public int AddStore(String storeName, int founderID){
        DataStore store = new DataStore(storeName);
        DataSubscriber founder = subscriberRepository.getOne(founderID);
        store.setFounder(founder);
        DataStore dataStore = storeRepository.saveAndFlush(store);
        return dataStore.getStoreID();
    }


    public void AddNewOwner(int storeID, int newOwnerID, OwnerPermission OP) {
        DataStore store = storeRepository.getOne(storeID);
        DataSubscriber newOwner = subscriberRepository.getOne(newOwnerID);
        store.AddNewOwner(newOwner);
        storeRepository.saveAndFlush(store);
        DataSubscriber appointment = null;
        if(OP.getAppointmentId()!=-1&&OP.getAppointmentId()!=null){
            appointment = subscriberRepository.getOne(OP.getAppointmentId());
        }
        DataOwnerPermissions dataOwnerPermissions = new DataOwnerPermissions(newOwner, store, appointment);
        ownerPermissionsRepository.saveAndFlush(dataOwnerPermissions);
        for (PermissionEnum.Permission permission: OP.getPermissions()){
            DataPermission.Permission dataPermission = DataPermission.toDataPermission(permission);
            DataOwnerPermissionType permissionType = new DataOwnerPermissionType(dataOwnerPermissions, dataPermission);
            ownerPermissionTypeRepository.saveAndFlush(permissionType);
        }
    }

    public void AddNewManager(int storeID, int newManagerID, ManagerPermission MP) {
        DataStore store = storeRepository.getOne(storeID);
        DataSubscriber newManager = subscriberRepository.getOne(newManagerID);
        store.AddNewManager(newManager);
        storeRepository.saveAndFlush(store);
        DataSubscriber appointment = subscriberRepository.getOne(MP.getAppointmentId());
        DataManagerPermissions dataManagerPermissions = new DataManagerPermissions(newManager, store, appointment);
        managerPermissionsRepository.saveAndFlush(dataManagerPermissions);
        for (PermissionEnum.Permission permission: MP.getPermissions()){
            DataPermission.Permission dataPermission = DataPermission.toDataPermission(permission);
            DataManagerPermissionType permissionType = new DataManagerPermissionType(dataManagerPermissions, dataPermission);
            managerPermissionTypeRepository.saveAndFlush(permissionType);
        }
    }



    public List<DataStore> getAllStores(){
        return storeRepository.findAll();
    }

    public Optional<DataStore> findStorebyId(int storeid){
       Optional<DataStore> res= storeRepository.findById(storeid);
        return res;
    }
    public void deleteAll(){
        storeRepository.deleteAll();
    }

    public List<DataStore> getAllStoresOfOwner(int userId){
        DataSubscriber subscriber= subscriberRepository.getOne(userId);
        return storeRepository.findAllByOwnersContains(subscriber);
    }

    public List<DataStore> getAllStoresofFounder(int userId){
        DataSubscriber subscriber= subscriberRepository.getOne(userId);
        return storeRepository.findAllByFounder(subscriber);
    }

    public List<DataStore> getAllStoresofManager(int userId){
        DataSubscriber subscriber= subscriberRepository.getOne(userId);
        return storeRepository.findAllByManagersContains(subscriber);
    }
//    public void AddNewOwner(int storeId, Integer userId, Integer newOwnerId){
//        DataSubscriber owner= subscriberRepository.getOne(userId);
//        DataSubscriber newowner= subscriberRepository.getOne(newOwnerId);
//        DataStore store=storeRepository.getOne(storeId);
//
//    }
}
