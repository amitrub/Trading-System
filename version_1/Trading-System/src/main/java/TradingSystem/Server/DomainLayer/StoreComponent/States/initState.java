package TradingSystem.Server.DomainLayer.StoreComponent.States;

import TradingSystem.Server.DomainLayer.StoreComponent.Bid;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.concurrent.ConcurrentHashMap;

public class initState implements State {
    private Bid bid;

    public initState(Bid bid) {
        this.bid = bid;
    }

    public initState(){
    }
    @Override
    public Response handle(int ownerId, ConcurrentHashMap<Integer, Boolean> ownersList, int userId) {
        bid.initialAprrovment();
        bid.approveBid(ownerId);
        if(bid.checkApproveBid()){
           bid.changeState(new approveState());
           return bid.handle(ownerId);
        }
        Response resAlert = new Response(false, "The owner/manager " + ownerId + " brought a counter offer for the bid of user "
                +bid.getUserID() + " for product " + bid.getProductID() + ", in your store: " + bid.getStoreId()
                + "the ofer is: "+bid.getPrice());
        resAlert.AddTag("updateBid");
        bid.sendAlertToOwner(resAlert);
        bid.unlockBid();
        return new Response("The counter offer update successfully");
    }

    @Override
    public void setBid(Bid bid) {
        this.bid=bid;
    }

    @Override
    public boolean isFinalState() {
        return false;
    }
}