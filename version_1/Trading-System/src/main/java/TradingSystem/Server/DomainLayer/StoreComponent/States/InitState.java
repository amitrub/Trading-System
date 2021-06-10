package TradingSystem.Server.DomainLayer.StoreComponent.States;

import TradingSystem.Server.DomainLayer.StoreComponent.Bid;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.concurrent.ConcurrentHashMap;

public class InitState implements State {
    private Bid bid;

    public InitState(Bid bid) {
        this.bid = bid;
    }

    public InitState(){
    }
    @Override
    public Response handle(int ownerId, ConcurrentHashMap<Integer, Boolean> ownersList, int userId) {
        bid.initialAprrovment();
        bid.approveBid(ownerId);
        if(bid.checkApproveBid()){
           bid.changeState(new ApproveState());
           return bid.handle(ownerId);
        }
        Response resAlert = new Response(false, "The owner/manager " + ownerId + " brought a counter offer for the bid of user "
                +bid.getUserID() + " and product " + bid.getProductID() + ", in your store: " + bid.getStoreId()
                + ".\n The ofer is- price: "+ bid.getPrice() +" quantity: "+bid.getQuantity()+".");
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