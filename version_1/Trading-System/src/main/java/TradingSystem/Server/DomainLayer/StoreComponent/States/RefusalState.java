package TradingSystem.Server.DomainLayer.StoreComponent.States;

import TradingSystem.Server.DomainLayer.StoreComponent.Bid;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.concurrent.ConcurrentHashMap;

public class RefusalState implements State {

    private Bid bid;

    public RefusalState(Bid bid) {
        this.bid = bid;
    }

    public RefusalState(){

    }
    @Override
    public Response handle(int ownerId, ConcurrentHashMap<Integer, Boolean> ownersList, int userId) {
        Response resAlert = new Response(false, "You are not approved to purchase the product "+ bid.getProduct().getProductName() + " in store " + bid.getStoreName()+" at a special price");
        bid.sendAlert(bid.getUserID(), resAlert);
        bid.unlockBid();
        bid.removeBid();
        return new Response("The bid was successfully rejected");
    }

    @Override
    public void setBid(Bid bid) {
        this.bid=bid;
    }

    @Override
    public boolean isFinalState() {
        return true;
    }
}