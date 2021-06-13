package TradingSystem.Server.DomainLayer.StoreComponent;

import TradingSystem.Server.DataLayer.Data_Modules.Bid.DataBid;
import TradingSystem.Server.DataLayer.Data_Modules.Bid.DataBidManagerApproves;
import TradingSystem.Server.DataLayer.Services.Data_Controller;
import TradingSystem.Server.DomainLayer.StoreComponent.States.BaseState;
import TradingSystem.Server.DomainLayer.StoreComponent.States.InitState;
import TradingSystem.Server.DomainLayer.StoreComponent.States.State;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Bid {

    private Integer userID;
    private Integer productID;
    private Integer storeId;
    private Integer quantity;
    private Integer price;
    private State state;
    private ConcurrentHashMap<Integer,Boolean> ownerAndManagerApprovals = new ConcurrentHashMap<>();
    private final Lock lock = new ReentrantLock();

    @Autowired
    public static Data_Controller data_controller;
    public static void setData_controller(Data_Controller data_controller) {
        Bid.data_controller = data_controller;
    }

    private static TradingSystemImpl tradingSystem;
    public static void setTradingSystem(TradingSystemImpl tradingSystem) {
        Bid.tradingSystem = tradingSystem;
    }


    public Bid(Integer userID, Integer productID,Integer storeId, Integer price,Integer quantity,ConcurrentHashMap<Integer,Boolean> list) {
        this.productID = productID;
        this.storeId=storeId;
        this.price = price;
        this.userID=userID;
        this.quantity=quantity;
        this.ownerAndManagerApprovals=list;
        this.state=new InitState();
        state.setBid(this);
    }

    public Bid(DataBid dataBid){
        this.productID = dataBid.getProduct().getProductID();
        this.storeId = dataBid.getProduct().getStore().getStoreID();
        this.price = dataBid.getPrice();
        this.userID = dataBid.getSubscriber().getUserID();
        this.quantity = dataBid.getQuantity();
        this.state=new InitState();
        for (DataBidManagerApproves managerApproves: dataBid.getOwnerAndManagerApprovals()){
            this.ownerAndManagerApprovals.putIfAbsent(managerApproves.getManager().getUserID(), managerApproves.isApproves());
            if(managerApproves.isApproves()){
                this.state=new BaseState();
            }
        }
        state.setBid(this);
    }

    public Lock getLock() {
        return lock;
    }

    public void lockBid() { this.lock.lock(); }

    public void unlockBid(){
        this.lock.unlock();
    }

    public boolean bidIsLock() {
        return this.lock.tryLock();
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getProductID() { return productID; }

    public void setProductID(Integer productID) {
        this.productID = productID;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        data_controller.setBidPrice(productID, userID, price);
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        data_controller.setBidQuantity(productID, userID, quantity);
        this.quantity = quantity;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public void changeState(State s){ this.state=s;s.setBid(this); }

    public Response handle(int ownerId){
        return this.state.handle(ownerId,this.ownerAndManagerApprovals,userID);
    }

    public boolean isFinalState(){ return state.isFinalState(); }

    public boolean checkApproveBid() {
        for (Integer key:this.ownerAndManagerApprovals.keySet()) {
            if(!this.ownerAndManagerApprovals.get(key)){
                return false;
            }
        }
        return true;
    }

    public void approveBid(int managerID) {
        data_controller.approveBid(productID, userID, managerID);
        if(this.ownerAndManagerApprovals.get(managerID)==null){
            this.ownerAndManagerApprovals.put(managerID,true);
        }
        else{
            this.ownerAndManagerApprovals.remove(managerID);
            this.ownerAndManagerApprovals.put(managerID,true);
        }
    }

    public void UpdateOwnerList(ConcurrentHashMap<Integer, Boolean> ownerList) {
        data_controller.UpdateOwnerList(productID, userID, ownerList);
        for (Integer key :ownerList.keySet()) {
              if(!this.ownerAndManagerApprovals.keySet().contains(key)){
                  this.ownerAndManagerApprovals.put(key,false);
              }
        }
        for (Integer key :ownerAndManagerApprovals.keySet()) {
            if(!ownerList.keySet().contains(key)){
                this.ownerAndManagerApprovals.remove(key);
            }
        }
    }

    public void initialAprrovment() {
        data_controller.initialAprrovment(productID, userID);
        for (Integer key:this.ownerAndManagerApprovals.keySet()) {
            this.ownerAndManagerApprovals.remove(key);
            this.ownerAndManagerApprovals.put(key,false);
        }
    }

    public void sendAlertToOwner(Response res){
        tradingSystem.getStores().get(storeId).sendAlertOfBiddingToManager(res);
        tradingSystem.getStores().get(storeId).sendAlertToOwners(res);
    }

    public void sendAlert(Integer userID, Response resAlert) {
        tradingSystem.getStores().get(storeId).sendAlert(userID,resAlert);
    }

    public Response AddSpacialProductForCart() {
        if(tradingSystem.subscribers.get(userID)!=null) {
           return tradingSystem.subscribers.get(userID).AddSpacialProductForCart(productID,storeId,price,quantity);
        }
        return new Response(true ,"The user is not subscriber");
    }

    public Product getProduct() {
        return tradingSystem.getProduct(storeId,productID);
    }

    public String getStoreName() {
        return tradingSystem.getStoreName(storeId);
    }

    //todo implement!
    public void removeBid() {
        tradingSystem.getStores().get(storeId).removeBid(this);
    }
}
