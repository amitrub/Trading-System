package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Validation {
//    private final TradingSystemImpl tradingSystemImpl = TradingSystemImpl.getInstance();


    public final TradingSystem tradingSystemImpl;
    
    public Validation(TradingSystem tradingSystemImpl) {
        this.tradingSystemImpl = tradingSystemImpl;
    }

    //User functions
    //return true if user name is exist in the system
    public boolean IsUserNameExist(String userName) {
        Set<Integer> userSet = tradingSystemImpl.getSubscribers().keySet();
        for (Integer id : userSet) {
            User user = tradingSystemImpl.getSubscribers().get(id);
            if (userName.equals(user.getUserName()))
                return true;
        }
        return false;
    }
    //if valid return Response(userId, "", false, "")
    //if not valid return Response(isErr: true, "Error Message")
    public Response ValidPassword(String userName, String password) {
        Set<Integer> userSet = tradingSystemImpl.getSubscribers().keySet();
        for (Integer id : userSet) {
            User user = tradingSystemImpl.getSubscribers().get(id);
            if (userName.equals(user.getUserName())) {
                if (password.equals(user.getPassword())){
                    Response res = new Response();
                    res.AddUserID(id);
                    return res;
                }
                else
                    return new Response(true, "Incorrect password");
            }
        }
        return new Response(true, "User not found");
    }

    public boolean VerifyPassword(String userName, String password) {
        return true;
    }


    //Store functions
    public synchronized boolean IsStoreNameExist(String storeName) {
        Set<Integer> storeSet = tradingSystemImpl.getStores().keySet();
        for (Integer id : storeSet) {
            Store store = tradingSystemImpl.getStores().get(id);
            if (storeName.equals(store.getName()))
                return true;
        }
        return false;
    }

    //Shopping Cart functions
    public boolean checkBuyingPolicy(Integer userId, Integer storeID, ConcurrentHashMap<Integer, Integer> productsInTheBug) {
        return tradingSystemImpl.getStores().get(storeID).checkBuyingPolicy(userId, productsInTheBug);
    }

    public boolean checkProductsExistInTheStore(Integer storeID, Integer productID,  Integer quantity) {
        if (tradingSystemImpl.getStores().containsKey(storeID))
            return tradingSystemImpl.getStores().get(storeID).checkProductsExistInTheStore(productID,quantity);
        else
            return false;
    }
}
