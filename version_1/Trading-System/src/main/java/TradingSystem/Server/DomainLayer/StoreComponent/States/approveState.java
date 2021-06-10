package TradingSystem.Server.DomainLayer.StoreComponent.States;

import TradingSystem.Server.DomainLayer.StoreComponent.Bid;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.concurrent.ConcurrentHashMap;

public class approveState implements State {

    private Bid bid;

    public approveState(Bid bid) {
        this.bid = bid;
    }

    public approveState(){
    }
    @Override
    public Response handle(int ownerId, ConcurrentHashMap<Integer, Boolean> ownersList, int userId) {
        Response res = bid.AddSpacialProductForCart();
        if (res.getIsErr()) {
            Response resAlert = new Response(false, "You have received a Response for your bidding for product "+bid.getProductID()+" in store "+bid.getStoreId()+".\n but the product could not be added to the cart.\n" +
                    "The reason: " + res.getMessage());
            bid.sendAlert(bid.getUserID(), resAlert);
            bid.removeBid();
            bid.unlockBid();
            return res;
        }
         Product p=bid.getProduct();
         Response resAlert = new Response(false, "You have received a Response for your bidding.\n" +
                "You may purchase " + p.getProductName() + " in store " + bid.getStoreName() + " at a price- " +  bid.getPrice() + ". (The original price is- " + p.getPrice() + ")." +
                 "go to your ShoppingCart to see the product");
        resAlert.AddTag("bidShoppingCart");
        bid.sendAlert(bid.getUserID(), resAlert);
        bid.unlockBid();
        bid.removeBid();
        return new Response(false, "The bid was Response successfully");
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
