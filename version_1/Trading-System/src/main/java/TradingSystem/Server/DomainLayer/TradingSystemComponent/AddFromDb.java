package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagCart;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagProduct;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingHistory.DataShoppingHistory;
import TradingSystem.Server.DataLayer.Services.Data_Controller;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingBag;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Inventory;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class AddFromDb {
    public final TradingSystemImplRubin tradingSystemImpl;

    public final Data_Controller data_controller;

    public AddFromDb(TradingSystemImplRubin tradingSystemImpl, Data_Controller data_controller) {
        this.tradingSystemImpl = tradingSystemImpl;
        this.data_controller= data_controller;
    }

    public void UploadAllUsers(){
        List<DataSubscriber> subscribers= data_controller.getAllSubscribers();
        for(DataSubscriber subscriber:subscribers){
            User toAdd = new User(subscriber);
            tradingSystemImpl.AddSubscriberIfNotExist(toAdd);
        }

    }

    public void UploadUserShoppingHistory(User subscriber){
        List<DataShoppingHistory> shoppingHistories = data_controller.getAllHistoryOfSubscriber(subscriber.getId());
        List<ShoppingHistory> shoppingHistoriesToAdd= new ArrayList<>();
        for(DataShoppingHistory shoppingHistory : shoppingHistories){
            shoppingHistoriesToAdd.add(new ShoppingHistory(shoppingHistory));
        }
        subscriber.setShoppingHistory(shoppingHistoriesToAdd);
    }

    public void UploadUserShoppingCart(Integer userID){
        UploadSubscriber(userID);
        List<DataShoppingBagCart> shoppingBagCart = data_controller.getAllBySubscriber(userID);
        for(DataShoppingBagCart dataShoppingBag : shoppingBagCart){
            UploadStore(dataShoppingBag.getStore().getStoreID());
            tradingSystemImpl.AddShoppingBagIfNotExist(new ShoppingBag(dataShoppingBag));
        }
    }

    public void UploadAllStores(){
        List<DataStore> stores= data_controller.getAllStores();
        for(DataStore store:stores){
            Store toAdd= new Store(store);
            tradingSystemImpl.AddStoreIfNotExist(toAdd);

//            List<DataShoppingBagCart> shoppingBagCarts=data_controller.findAllCartsOfStore(store.getStoreID());
//            Inventory inventory=new Inventory(store.getStoreID(),store.getStoreName());
//            ConcurrentHashMap<Integer, Product> products = new ConcurrentHashMap<>();
//            //TODO fix
//            for(DataShoppingBagCart shoppingBagCart:shoppingBagCarts){
//                List<DataShoppingBagProduct> shoppingBagProducts=shoppingBagCart.getProducts();
//                for(DataShoppingBagProduct product: shoppingBagProducts){
//                    products.put(product.getQuantity(),new Product(product.getProduct()));
//                }
//            }
//            inventory.setProducts(products);
        }
    }

    public void UploadStoreShoppingHistory(Store store){
        List<DataShoppingHistory> shoppingHistories = data_controller.findAllByStore(store.getId());
        List<ShoppingHistory> shoppingHistoriesToAdd= new ArrayList<>();
        for(DataShoppingHistory shoppingHistory:shoppingHistories){
            shoppingHistoriesToAdd.add(new ShoppingHistory(shoppingHistory));
        }
        store.setShoppingHistory(shoppingHistoriesToAdd);
    }

    public void UploadStore(int storeID){
        if(!tradingSystemImpl.getStores().containsKey(storeID)){
            Optional<DataStore> optionalDataStore = data_controller.findStorebyId(storeID);

            if(optionalDataStore.isPresent()){
                tradingSystemImpl.AddStoreIfNotExist(new Store(optionalDataStore.get()));
            }
        }
        uploadProductsForStore(storeID);
    }

    public void uploadProductsForStore(Integer storeID){
        List<DataProduct> productsDB = data_controller.findDummyProductByStore(storeID);
        for(DataProduct product : productsDB){
            tradingSystemImpl.AddStoreProductIfNotExist(storeID, new Product(product));
        }
    }

    public void UploadSubscriber(int subscriberid){
        if(!tradingSystemImpl.getSubscribers().containsKey(subscriberid)){
            Optional<DataSubscriber> optionalDataSubscriber=data_controller.findSubscriberById(subscriberid);
            if(optionalDataSubscriber.isPresent()){
                tradingSystemImpl.AddSubscriberIfNotExist(new User(optionalDataSubscriber.get()));
            }
        }
    }

    public void UploadUserFounderStores(User subscriber ){
        List<Integer> foundedStores= data_controller.getAllFoundedStores(subscriber.getId()).stream().map(DataStore::getStoreID).collect(Collectors.toList());
        for (Integer storeID : foundedStores){
            UploadStore(storeID);
            subscriber.AddFounderStoresIfNotExist(storeID);
        }
    }

    public void UploadUserOwnerStores(User subscriber){
        List<Integer> ownedStores = data_controller.getAllOwnedStores(subscriber.getId()).stream().map(DataStore::getStoreID).collect(Collectors.toList());
        for (Integer storeID : ownedStores){
            UploadStore(storeID);
            tradingSystemImpl.AddOwnerIfNotExist(subscriber.getId(), storeID);
        }
    }

    public void UploadUserManagerStores(User subscriber){
        List<Integer> managedStores = data_controller.getAllManagerStores(subscriber.getId()).stream().map(DataStore::getStoreID).collect(Collectors.toList());
        for (Integer storeID : managedStores){
            UploadStore(storeID);
            tradingSystemImpl.AddManagerIfNotExist(subscriber.getId(), storeID);
        }
    }

    public void UploadStoreOwners(Store store){
        List<Integer> owners= data_controller.findAllByStoresOwnedContains(store.getId()).stream().map(DataSubscriber::getUserID).collect(Collectors.toList());
        for (Integer userID : owners){
            UploadSubscriber(userID);
            tradingSystemImpl.AddOwnerIfNotExist(userID, store.getId());
        }
    }

    public void UploadStoreManager(Store store){
        List<Integer> managers = data_controller.findAllStoresManagerContains(store.getId()).stream().map(DataSubscriber::getUserID).collect(Collectors.toList());
        for (Integer userID : managers){
            UploadSubscriber(userID);
            tradingSystemImpl.AddManagerIfNotExist(userID, store.getId());
        }
    }

    public void UploadUserShoppingBag(Integer userID){

    }


}
