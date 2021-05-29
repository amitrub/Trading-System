package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagCart;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagProduct;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingHistory.DataShoppingHistory;
import TradingSystem.Server.DataLayer.Services.Data_Controller;
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
        ConcurrentHashMap<Integer, User> res= tradingSystemImpl.getSubscribers();

        for(DataSubscriber subscriber:subscribers){
            List<Integer> ownedstores=data_controller.getAllOwnedStores(subscriber.getUserID()).stream().map(DataStore::getStoreID).collect(Collectors.toList());
            List<Integer> foundedstores= data_controller.getAllFoundedStores(subscriber.getUserID()).stream().map(DataStore::getStoreID).collect(Collectors.toList());
            List<Integer> ManagedStores = data_controller.getAllManagerStores(subscriber.getUserID()).stream().map(DataStore::getStoreID).collect(Collectors.toList());
            User toAdd=new User(subscriber);
            toAdd.setMyFoundedStoresIDs(foundedstores);
            toAdd.setMyManagedStoresIDs(ManagedStores);
            toAdd.setMyOwnedStoresIDs(ownedstores);
            List<DataShoppingHistory> shoppingHistories= data_controller.getAllHistoryOfSubscriber(subscriber.getUserID());
            List<ShoppingHistory> shoppingHistoriestoadd= new ArrayList<>();
            for(DataShoppingHistory shoppingHistory:shoppingHistories){
                shoppingHistoriestoadd.add(new ShoppingHistory(shoppingHistory));
            }
            toAdd.setShoppingHistory(shoppingHistoriestoadd);
            res.putIfAbsent(subscriber.getUserID(),toAdd);
        }
        tradingSystemImpl.subscribers=res;
    }

    public void UploadUserFounderStores(Integer userID ){

    }

    public void UploadUserShoppingBag(Integer userID){

    }

    public void UploadAllStores(){
        List<DataStore> stores= data_controller.getAllStores();
        ConcurrentHashMap<Integer, Store> res= tradingSystemImpl.getStores();
        for(DataStore store:stores){
            List<Integer> owners= data_controller.findAllByStoresOwnedContains(store.getStoreID()).stream().map(DataSubscriber::getUserID).collect(Collectors.toList());
            List<Integer> managers= data_controller.findAllStoresManagerContains(store.getStoreID()).stream().map(DataSubscriber::getUserID).collect(Collectors.toList());
            List<DataShoppingHistory> shoppingHistories=data_controller.findAllByStore(store.getStoreID());
            List<ShoppingHistory> shoppingHistoriestoadd= new ArrayList<>();
            for(DataShoppingHistory shoppingHistory:shoppingHistories){
                shoppingHistoriestoadd.add(new ShoppingHistory(shoppingHistory));
            }
            List<DataShoppingBagCart> shoppingBagCarts=data_controller.findAllCartsOfStore(store.getStoreID());
            Inventory inventory=new Inventory(store.getStoreID(),store.getStoreName());
            ConcurrentHashMap<Integer, Product> products = new ConcurrentHashMap<>();
            //TODO fix
            for(DataShoppingBagCart shoppingBagCart:shoppingBagCarts){
                List<DataShoppingBagProduct> shoppingBagProducts=shoppingBagCart.getProducts();
                for(DataShoppingBagProduct product: shoppingBagProducts){
                    products.put(product.getQuantity(),new Product(product.getProduct()));
                }
            }
            inventory.setProducts(products);
            Store toAdd= new Store(store);
            toAdd.setManagersIDs(managers);
            toAdd.setOwnersIDs(owners);
            toAdd.setShoppingHistory(shoppingHistoriestoadd);
            res.putIfAbsent(store.getStoreID(),toAdd);
        }
        tradingSystemImpl.stores=res;
    }

    public void UploadStore(int storeid){
        if(!tradingSystemImpl.getStores().containsKey(storeid)){
            Optional<DataStore> optionalDataStore=data_controller.findStorebyId(storeid);
            if(optionalDataStore.isPresent()){
                ConcurrentHashMap<Integer,Store> stores=tradingSystemImpl.getStores();
                stores.put(optionalDataStore.get().getStoreID(),new Store(optionalDataStore.get()));
                tradingSystemImpl.setStores(stores);
            }
        }
    }

    public void UploadSubscriber(int subscriberid){
        if(!tradingSystemImpl.getSubscribers().containsKey(subscriberid)){
            Optional<DataSubscriber> optionalDataSubscriber=data_controller.findSubscriberById(subscriberid);
            if(optionalDataSubscriber.isPresent()){
                ConcurrentHashMap<Integer,User> subscribers=tradingSystemImpl.getSubscribers();
                subscribers.put(optionalDataSubscriber.get().getUserID(),new User(optionalDataSubscriber.get()));
                tradingSystemImpl.setSubscribers(subscribers);
            }
        }
    }

    public List<DummyProduct> uploadProductsForStore(Store store, List<DummyProduct> products){
        List<DataProduct> productsdb=data_controller.findDummyProductByStore(new DataStore(store.getName()));
        List<DummyProduct> convert= new ArrayList<>();
        for(DataProduct product:productsdb){
            convert.add(new DummyProduct(product));
        }
        convert.addAll(products);
        return convert;
    }


}
