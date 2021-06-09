package TradingSystem.Server.DomainLayer.StoreComponent.States;

import TradingSystem.Server.DomainLayer.StoreComponent.Bid;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.concurrent.ConcurrentHashMap;

public interface State {
    public Response handle(int ownerId, ConcurrentHashMap<Integer, Boolean> ownersList, int userId);
    public void setBid(Bid bid);
    public boolean isFinalState();
}
