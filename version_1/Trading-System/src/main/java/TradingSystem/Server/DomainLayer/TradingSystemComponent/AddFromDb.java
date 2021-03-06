package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.DataLayer.Data_Modules.Bid.DataBid;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Services.Data_Controller;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.DiscountPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.List;

public class AddFromDb {
    public final TradingSystemImpl tradingSystem;

    public final Data_Controller data_controller;

    public AddFromDb(TradingSystemImpl tradingSystemImpl, Data_Controller data_controller) {
        this.tradingSystem = tradingSystemImpl;
        this.data_controller= data_controller;
    }
    public void UploadAllData(){
        UploadAllStores();
        UploadAllUsers();
        tradingSystem.AddStoreOwnerPermission();
        tradingSystem.AddStoreManagerPermission();
        tradingSystem.AddStoreHistory();
        UploadAllBids();
    }

    private void UploadAllStores(){
        List<DataStore> stores= data_controller.getAllStores().returnStoresDB();
        for(DataStore store:stores){
            Store toAdd= new Store(store);
            Response response=data_controller.getBuyingByStoreId(toAdd.getId());
            if(!response.getIsErr()){
                toAdd.setBuyingPolicy(new BuyingPolicy(response.returnDBBuying()));
            }
            response=data_controller.getdiscountByStoreId(toAdd.getId());
            if(!response.getIsErr()){
                toAdd.setDiscountPolicy(new DiscountPolicy(response.returnDBDiscount()));
            }
            tradingSystem.AddStoreIfNotExist(toAdd);
        }
    }
    private void UploadAllUsers(){
        Response response= data_controller.getAllSubscribers();
        List<DataSubscriber> subscribers= response.returnDBsubscribers();
        for(DataSubscriber subscriber:subscribers){
            User toAdd = new User(subscriber);
            tradingSystem.AddSubscriberIfNotExist(toAdd);
        }
    }

    private void UploadAllBids(){
        List<DataBid> bids = data_controller.getAllBids();
        for (DataBid bid: bids){
            tradingSystem.UploadBidToStore(bid.getProduct().getStore().getStoreID(), bid);
        }
    }


}
