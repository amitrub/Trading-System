package TradingSystem.Server.DataLayer.Services;

import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Keys.UserStoreKey;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.*;
import TradingSystem.Server.DataLayer.Repositories.*;
import TradingSystem.Server.DomainLayer.UserComponent.ManagerPermission;
import TradingSystem.Server.DomainLayer.UserComponent.OwnerPermission;
import TradingSystem.Server.DomainLayer.UserComponent.PermissionEnum;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;


//import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.*;

@Service
//@Transactional
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

    @Transactional(rollbackFor = { Exception.class })
    public void test(){

        DataStore store = new DataStore("a2");
        Optional<DataSubscriber> founder = subscriberRepository.findById(1);
        store.setFounder(founder.get());
        storeRepository.saveAndFlush(store);
        System.out.println("sleep");
//        Thread.sleep(7000);
        System.out.println("finish sleep");
        DataSubscriber founder1 = subscriberRepository.getOne(7);
        System.out.println(founder1.getName());
//        throw new Exception("Throwing exception for demoing Rollback!!!");

    }

    @Transactional(rollbackFor = { Exception.class }, timeout = 10)
    public Response AddStore(String storeName, int founderID){
        try {
            DataStore store = new DataStore(storeName);
            Optional<DataSubscriber> founder = subscriberRepository.findById(founderID);
            if(!founder.isPresent()){
                return new Response(true,"Could not find founder");
            }
            store.setFounder(founder.get());
            DataStore dataStore = storeRepository.saveAndFlush(store);
            Response response=AddNewOwner(dataStore.getStoreID(), founderID, new OwnerPermission(founderID, dataStore.getStoreID()));
            if (response.getIsErr()){
                return response;
            }
            response=new Response(false,"Store was added successfully");
            response.AddStoreID(dataStore.getStoreID());
            return response;
        }
        catch (UnexpectedRollbackException e){
            return new Response(true," Could not add the store on the limit time");
        }
    }

    @Transactional(rollbackFor = { Exception.class }, timeout = 10)
    public Response AddNewOwner(int storeID, int newOwnerID, OwnerPermission OP) {
        try {
            Optional<DataStore> store = storeRepository.findById(storeID);
            Optional<DataSubscriber> newOwner = subscriberRepository.findById(newOwnerID);
            if(!store.isPresent() || !newOwner.isPresent()){
                return new Response(true,"Cannot find store or user");
            }
            store.get().AddNewOwner(newOwner.get());
            storeRepository.saveAndFlush(store.get());
            DataSubscriber appointment = null;
            if(OP.getAppointmentId()!=-1&&OP.getAppointmentId()!=null){
                appointment = subscriberRepository.getOne(OP.getAppointmentId());
            }
            DataOwnerPermissions dataOwnerPermissions = new DataOwnerPermissions(newOwner.get(), store.get(), appointment);
            ownerPermissionsRepository.saveAndFlush(dataOwnerPermissions);
            for (PermissionEnum.Permission permission: OP.getPermissions()){
                DataPermission.Permission dataPermission = DataPermission.toDataPermission(permission);
                DataOwnerPermissionType permissionType = new DataOwnerPermissionType(dataOwnerPermissions, dataPermission);
                ownerPermissionTypeRepository.saveAndFlush(permissionType);
            }
            return new Response(false," ");
        }
        catch (UnexpectedRollbackException e){
            return new Response(true," Could not add the store on the limit time");
        }
    }
    @Transactional(rollbackFor = { Exception.class }, timeout = 10)
    public Response AddNewManager(int storeID, int newManagerID, ManagerPermission MP) {
        try {
            Optional<DataStore> store = storeRepository.findById(storeID);
            Optional<DataSubscriber> newManager = subscriberRepository.findById(newManagerID);
            if(!store.isPresent() || !newManager.isPresent()){
                return new Response(true,"Could not find store or manager");
            }
            store.get().AddNewManager(newManager.get());
            storeRepository.saveAndFlush(store.get());
            DataSubscriber appointment = subscriberRepository.getOne(MP.getAppointmentId());
            DataManagerPermissions dataManagerPermissions = new DataManagerPermissions(newManager.get(), store.get(), appointment);
            managerPermissionsRepository.saveAndFlush(dataManagerPermissions);
            for (PermissionEnum.Permission permission: MP.getPermissions()){
                DataPermission.Permission dataPermission = DataPermission.toDataPermission(permission);
                DataManagerPermissionType permissionType = new DataManagerPermissionType(dataManagerPermissions, dataPermission);
                managerPermissionTypeRepository.saveAndFlush(permissionType);
            }
            return new Response(false," ");
        }
        catch (UnexpectedRollbackException e){
            return new Response(true," Could not add the store on the limit time");
        }
    }


    @Transactional(rollbackFor = { Exception.class }, timeout = 10)
    public Response getAllStores(){
        try {
            Response response= new Response();
            response.AddDBStoresList(storeRepository.findAll());
            return response;
        }
        catch (UnexpectedRollbackException e){
            return new Response(true," Could not add the store on the limit time");
        }
    }
    @Transactional(rollbackFor = { Exception.class }, timeout = 10)
    public Response findStorebyId(int storeid){
        try {
            Optional<DataStore> res= storeRepository.findById(storeid);
            if(!res.isPresent()){
                return new Response(true,"Store id doesn't exist");
            }
            Response response= new Response();
            response.AddDataStore(res.get());
            return response;
        }
        catch (UnexpectedRollbackException e){
            return new Response(true," Could not add the store on the limit time");
        }

    }

    @Transactional(rollbackFor = { Exception.class }, timeout = 10)
    public void deleteAll(){
        storeRepository.deleteAll();
    }

    @Transactional(rollbackFor = { Exception.class }, timeout = 10)
    public Response getAllStoresOfOwner(int userId){
        try {
            Optional<DataSubscriber> subscriber= subscriberRepository.findById(userId);
            if(!subscriber.isPresent()){
                return new Response(true,"Could not find user");
            }
            List<DataStore> stores= storeRepository.findAllByOwnersContains(subscriber.get());
            Response response=new Response(false," ");
            response.AddDBStoresList(stores);
            return response;
        }
        catch (UnexpectedRollbackException e){
            return new Response(true," Could not add the store on the limit time");
        }
    }

    @Transactional(rollbackFor = { Exception.class }, timeout = 10)
    public Response getAllStoresofFounder(int userId){
        try {
            Optional<DataSubscriber> subscriber= subscriberRepository.findById(userId);
            if(!subscriber.isPresent()){
                return new Response(true,"Could not found user id");
            }
            List<DataStore> stores= storeRepository.findAllByFounder(subscriber.get());
            Response response=new Response(false," ");
            response.AddDBStoresList(stores);
            return response;
        }
        catch (UnexpectedRollbackException e){
            return new Response(true," Could not add the store on the limit time");
        }
    }

    @Transactional(rollbackFor = { Exception.class }, timeout = 10)
    public Response getAllStoresofManager(int userId) {
        try {
            Optional<DataSubscriber> subscriber = subscriberRepository.findById(userId);
            if (!subscriber.isPresent()) {
                return new Response(true, "could not find subscriber");
            }
            List<DataStore> stores = storeRepository.findAllByManagersContains(subscriber.get());
            Response response = new Response(false, " ");
            response.AddDBStoresList(stores);
            return response;
        }
        catch (UnexpectedRollbackException e){
            return new Response(true," Could not add the store on the limit time");
        }
    }
//    public void AddNewOwner(int storeId, Integer userId, Integer newOwnerId){
//        DataSubscriber owner= subscriberRepository.getOne(userId);
//        DataSubscriber newowner= subscriberRepository.getOne(newOwnerId);
//        DataStore store=storeRepository.getOne(storeId);
//
//    }
    @Transactional(rollbackFor = { Exception.class }, timeout = 10)
    public HashMap<Date,Integer> getAllStoresWeek(){
        HashMap<Date,Integer> hashMap=new HashMap<>();
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int i = c.get(Calendar.DAY_OF_WEEK) - c.getFirstDayOfWeek();
        c.add(Calendar.DATE, -i);
        //   Date start = c.getTime();
        for(int j=0;j<=6;j++){
            Date start = c.getTime();
            c.add(Calendar.DATE, 1);
            Date end = c.getTime();
            List<DataStore> stores=storeRepository.findAllByDateBetween(start,end);
            hashMap.put(start,stores.size());
        }
        return hashMap;
    }
}
