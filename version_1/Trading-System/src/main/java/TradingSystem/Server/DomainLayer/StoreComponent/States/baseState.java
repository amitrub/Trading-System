package TradingSystem.Server.DomainLayer.StoreComponent.States;

import TradingSystem.Server.DomainLayer.StoreComponent.Bid;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.concurrent.ConcurrentHashMap;

public class baseState  implements State {
    private Bid bid;

    public baseState(Bid bid) {
        this.bid = bid;
    }

    public baseState(){
    }

    @Override
    public Response handle(int ownerId, ConcurrentHashMap<Integer, Boolean> ownersList, int userId) {
        bid.approveBid(ownerId);
        boolean finalApprove=bid.checkApproveBid();
        if(finalApprove) {
            bid.changeState(new approveState());
            return bid.handle(ownerId);
        }
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
