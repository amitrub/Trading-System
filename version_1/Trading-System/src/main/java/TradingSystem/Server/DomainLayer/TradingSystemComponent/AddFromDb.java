package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.DataLayer.Data_Modules.DataProduct;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DataBuyingPolicy;
import TradingSystem.Server.DataLayer.Data_Modules.Sales.DataDiscountPolicy;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingCart.DataShoppingBagCart;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingHistory.DataShoppingHistory;
import TradingSystem.Server.DataLayer.Services.Data_Controller;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingBag;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.DiscountPolicy;
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
            Optional<DataBuyingPolicy> buyingPolicy=data_controller.getBuyingByStoreId(toAdd.getId());
            buyingPolicy.ifPresent(dataBuyingPolicy -> toAdd.setBuyingPolicy(new BuyingPolicy(dataBuyingPolicy)));
            Optional<DataDiscountPolicy> dataDiscountPolicy=data_controller.getdiscountByStoreId(toAdd.getId());
            dataDiscountPolicy.ifPresent(discountPolicy -> toAdd.setDiscountPolicy(new DiscountPolicy(discountPolicy)));
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


}
