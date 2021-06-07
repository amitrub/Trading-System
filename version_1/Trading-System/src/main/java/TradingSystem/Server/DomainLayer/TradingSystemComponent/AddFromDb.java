package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagCart;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingHistory.DataShoppingHistory;
import TradingSystem.Server.DataLayer.Services.Data_Controller;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingBag;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.UserComponent.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AddFromDb {
    public final TradingSystemImplRubin tradingSystem;

    public final Data_Controller data_controller;

    public AddFromDb(TradingSystemImplRubin tradingSystemImpl, Data_Controller data_controller) {
        this.tradingSystem = tradingSystemImpl;
        this.data_controller= data_controller;
    }
    public void UploadAllData(){
        UploadAllStores();
        UploadAllUsers();
        tradingSystem.AddStoreOwnerPermission();
        tradingSystem.AddStoreManagerPermission();
        tradingSystem.AddStoreHistory();
    }

    private void UploadAllStores(){
        List<DataStore> stores= data_controller.getAllStores();
        for(DataStore store:stores){
            Store toAdd= new Store(store);
            tradingSystem.AddStoreIfNotExist(toAdd);
        }
    }
    private void UploadAllUsers(){
        List<DataSubscriber> subscribers= data_controller.getAllSubscribers();
        for(DataSubscriber subscriber:subscribers){
            User toAdd = new User(subscriber);
            tradingSystem.AddSubscriberIfNotExist(toAdd);
        }
    }

//    private void UploadUserShoppingHistory(User subscriber){
//        List<DataShoppingHistory> shoppingHistories = data_controller.getAllHistoryOfSubscriber(subscriber.getId());
//        List<ShoppingHistory> shoppingHistoriesToAdd= new ArrayList<>();
//        for(DataShoppingHistory shoppingHistory : shoppingHistories){
//            shoppingHistoriesToAdd.add(new ShoppingHistory(shoppingHistory));
//        }
//        subscriber.setShoppingHistory(shoppingHistoriesToAdd);
//    }
//
//    private void UploadUserShoppingCart(Integer userID){
//        UploadSubscriber(userID);
//        List<DataShoppingBagCart> shoppingBagCart = data_controller.getSubscriberShoppingCart(userID);
//        for(DataShoppingBagCart dataShoppingBag : shoppingBagCart){
//            UploadStore(dataShoppingBag.getStore().getStoreID());
//            tradingSystem.AddShoppingBagIfNotExist(new ShoppingBag(dataShoppingBag));
//        }
//    }
//
//    private void UploadStoreShoppingHistory(Store store){
//        List<DataShoppingHistory> shoppingHistories = data_controller.findAllByStore(store.getId());
//        List<ShoppingHistory> shoppingHistoriesToAdd= new ArrayList<>();
//        for(DataShoppingHistory shoppingHistory:shoppingHistories){
//            shoppingHistoriesToAdd.add(new ShoppingHistory(shoppingHistory));
//        }
//        store.setShoppingHistory(shoppingHistoriesToAdd);
//    }
//
//    private void UploadStore(int storeID){
//        if(!tradingSystem.getStores().containsKey(storeID)){
//            Optional<DataStore> optionalDataStore = data_controller.findStorebyId(storeID);
//
//            if(optionalDataStore.isPresent()){
//                tradingSystem.AddStoreIfNotExist(new Store(optionalDataStore.get()));
//            }
//        }
//        uploadProductsForStore(storeID);
//    }
//
//    private void uploadProductsForStore(Integer storeID){
//        List<DataProduct> productsDB = data_controller.findDummyProductByStore(storeID);
//        for(DataProduct product : productsDB){
//            tradingSystem.AddStoreProductIfNotExist(storeID, new Product(product));
//        }
//    }
//
//    private void UploadSubscriber(int subscriberid){
//        if(!tradingSystem.getSubscribers().containsKey(subscriberid)){
//            Optional<DataSubscriber> optionalDataSubscriber=data_controller.findSubscriberById(subscriberid);
//            if(optionalDataSubscriber.isPresent()){
//                tradingSystem.AddSubscriberIfNotExist(new User(optionalDataSubscriber.get()));
//            }
//        }
//    }
//
//    private void UploadUserFounderStores(User subscriber ){
//        List<Integer> foundedStores= data_controller.getAllFoundedStores(subscriber.getId()).stream().map(DataStore::getStoreID).collect(Collectors.toList());
//        for (Integer storeID : foundedStores){
//            UploadStore(storeID);
//            subscriber.AddFounderStoresIfNotExist(storeID);
//        }
//    }
//
//    private void UploadUserOwnerStores(User subscriber){
//        List<Integer> ownedStores = data_controller.getAllOwnedStores(subscriber.getId()).stream().map(DataStore::getStoreID).collect(Collectors.toList());
//        for (Integer storeID : ownedStores){
//            UploadStore(storeID);
//            tradingSystem.AddOwnerIfNotExist(subscriber.getId(), storeID);
//        }
//    }
//
//    private void UploadUserManagerStores(User subscriber){
//        List<Integer> managedStores = data_controller.getAllManagerStores(subscriber.getId()).stream().map(DataStore::getStoreID).collect(Collectors.toList());
//        for (Integer storeID : managedStores){
//            UploadStore(storeID);
//            tradingSystem.AddManagerIfNotExist(subscriber.getId(), storeID);
//        }
//    }
//
//    private void UploadStoreOwners(Store store){
//        List<Integer> owners= data_controller.findAllByStoresOwnedContains(store.getId()).stream().map(DataSubscriber::getUserID).collect(Collectors.toList());
//        for (Integer userID : owners){
//            UploadSubscriber(userID);
//            tradingSystem.AddOwnerIfNotExist(userID, store.getId());
//        }
//    }
//
//    private void UploadStoreManager(Store store){
//        List<Integer> managers = data_controller.findAllStoresManagerContains(store.getId()).stream().map(DataSubscriber::getUserID).collect(Collectors.toList());
//        for (Integer userID : managers){
//            UploadSubscriber(userID);
//            tradingSystem.AddManagerIfNotExist(userID, store.getId());
//        }
//    }
//
//    private void UploadUserShoppingBag(Integer userID){
//
//    }


}
