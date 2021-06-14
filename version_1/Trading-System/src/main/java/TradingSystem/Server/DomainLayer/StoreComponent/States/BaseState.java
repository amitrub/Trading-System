package TradingSystem.Server.DomainLayer.StoreComponent.States;

import TradingSystem.Server.DomainLayer.StoreComponent.Bid;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.concurrent.ConcurrentHashMap;

public class BaseState implements State {
    private Bid bid;

    public BaseState(Bid bid) {
        this.bid = bid;
    }

    public BaseState(){
    }

    @Override
    public Response handle(int ownerId, ConcurrentHashMap<Integer, Boolean> ownersList, int userId) {
        bid.approveBid(ownerId);
        boolean finalApprove=bid.checkApproveBid();
        if(finalApprove) {
            bid.changeState(new ApproveState());
            return bid.handle(ownerId);
        }
        bid.unlockBid();
        return new Response("The Bid approve successfully");
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
