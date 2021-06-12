package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserStoreKey;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataManagerPermissionType;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataManagerPermissions;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataOwnerPermissions;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataPermission;
import TradingSystem.Server.DataLayer.Repositories.*;
import TradingSystem.Server.DomainLayer.UserComponent.ManagerPermission;
import TradingSystem.Server.DomainLayer.UserComponent.PermissionEnum;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Response getOwnerPermissions(int userID, int storeID){
        Optional<DataSubscriber> subscriber = subscriberRepository.findById(userID);
        if(!subscriber.isPresent()){
            return new Response(true,"Could not find user");
        }
        List<DataOwnerPermissions> ownerPermissions = ownerPermissionsRepository.findAllBySubscriber(subscriber.get());
        Response response=new Response(false,"Found permissions");
        response.AddPair("Owner_permissions", ownerPermissions);
        return response;
    }

    public Response EditManagerPermissions(int storeID, int managerID, List<PermissionEnum.Permission> permissions) {
        Optional<DataSubscriber> manager_opt = subscriberRepository.findById(managerID);
        Optional<DataStore> store_opt = storeRepository.findById(storeID);
        Optional<DataManagerPermissions> managerPermission_opt = managerPermissionsRepository.findById(new UserStoreKey(managerID, storeID));
        if(!manager_opt.isPresent() || !store_opt.isPresent() || !managerPermission_opt.isPresent()){
            return new Response(true,"Could not edit manager permissions");
        }
        DataStore store=store_opt.get();
        DataSubscriber manager= manager_opt.get();
        DataManagerPermissions managerPermission=managerPermission_opt.get();
        int appointmentID = managerPermission.getAppointment().getUserID();
        manager.RemoveManager(store, managerPermission);
        subscriberRepository.saveAndFlush(manager);

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
        return new Response(false," ");
    }

    public Response RemoveOwner(int storeID, int ownerID){
        Optional<DataSubscriber> owner = subscriberRepository.findById(ownerID);
        Optional<DataStore> store = storeRepository.findById(storeID);
        if(!store.isPresent() || !owner.isPresent()){
            return new Response(true,"Could not find store or owner");
        }
        DataOwnerPermissions ownerPermission = ownerPermissionsRepository.getOne(new UserStoreKey(ownerID, storeID));
        owner.get().RemoveOwner(store.get(), ownerPermission);
        subscriberRepository.saveAndFlush(owner.get());


        List<DataOwnerPermissions> ownerPermissions = ownerPermissionsRepository.findAllByAppointment(owner.get());
        for (DataOwnerPermissions ownerPermissionByAppointment: ownerPermissions){
            DataSubscriber ownerByAppointment = ownerPermissionByAppointment.getSubscriber();
            ownerByAppointment.RemoveOwner(store.get(), ownerPermissionByAppointment);
            subscriberRepository.saveAndFlush(ownerByAppointment);
        }

        List<DataManagerPermissions> managerPermissions = managerPermissionsRepository.findAllByAppointment(owner.get());
        for (DataManagerPermissions managerPermissionByAppointment: managerPermissions){
            DataSubscriber managerByAppointment = managerPermissionByAppointment.getSubscriber();
            managerByAppointment.RemoveManager(store.get(), managerPermissionByAppointment);
            subscriberRepository.saveAndFlush(managerByAppointment);
        }
        return new Response(false," ");
    }

    public void RemoveManager(int storeID, int managerID){
        DataSubscriber manager = subscriberRepository.getOne(managerID);
        DataStore store = storeRepository.getOne(storeID);
        DataManagerPermissions managerPermission = managerPermissionsRepository.getOne(new UserStoreKey(managerID, storeID));
        manager.RemoveManager(store, managerPermission);
        subscriberRepository.saveAndFlush(manager);
    }
}
