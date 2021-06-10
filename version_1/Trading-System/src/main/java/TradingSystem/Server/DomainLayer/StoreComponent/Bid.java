package TradingSystem.Server.DomainLayer.StoreComponent;

import TradingSystem.Server.DomainLayer.StoreComponent.States.InitState;
import TradingSystem.Server.DomainLayer.StoreComponent.States.State;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

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
    private ConcurrentHashMap<Integer,Boolean> ownerAndManagerApprovals;
    private final Lock lock = new ReentrantLock();

    private static TradingSystemImplRubin tradingSystem;
    public static void setTradingSystem(TradingSystemImplRubin tradingSystem) {
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
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
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
        for (Integer key:this.ownerAndManagerApprovals.keySet()
             ) {
            if(!this.ownerAndManagerApprovals.get(key)){
                return false;
            }
        }
        return true;
    }

    public void approveBid(int userID) {
        if(this.ownerAndManagerApprovals.get(userID)==null){
            this.ownerAndManagerApprovals.put(userID,true);
        }
        else{
            this.ownerAndManagerApprovals.remove(userID);
            this.ownerAndManagerApprovals.put(userID,true);
        }
    }

    public void UpdateOwnerList(ConcurrentHashMap<Integer, Boolean> ownerList) {
        for (Integer key :ownerList.keySet()
             ) {
              if(!this.ownerAndManagerApprovals.keySet().contains(key)){
                  this.ownerAndManagerApprovals.put(key,false);
              }
        }
        for (Integer key :ownerAndManagerApprovals.keySet()
        ) {
            if(!ownerList.keySet().contains(key)){
                this.ownerAndManagerApprovals.remove(key);
            }
        }
    }

    public void initialAprrovment() {
        for (Integer key:this.ownerAndManagerApprovals.keySet()
             ) {
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
