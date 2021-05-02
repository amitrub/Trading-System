package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Validation {
    private final TradingSystemImpl tradingSystemImpl = TradingSystemImpl.getInstance();

    public Validation() {
    }

    //User functions
    //return true if user name is exist in the system
    public boolean IsUserNameExist(String userName) {
        Set<Integer> userSet = tradingSystemImpl.subscribers.keySet();
        for (Integer id : userSet) {
            User user = tradingSystemImpl.subscribers.get(id);
            if (userName.equals(user.getUserName()))
                return true;
        }
        return false;
    }
    //if valid return Response(userId, "", false, "")
    //if not valid return Response(isErr: true, "Error Message")
    public Response ValidPassword(String userName, String password) {
        Set<Integer> userSet = tradingSystemImpl.subscribers.keySet();
        for (Integer id : userSet) {
            User user = tradingSystemImpl.subscribers.get(id);
            if (userName.equals(user.getUserName())) {
                if (password.equals(user.getPassword())){
                    Response res = new Response();
                    res.AddUserID(id);
                    return res;
                }
                else
                    return new Response(true, tradingSystemImpl.errMsgGenerator("Server", "TradingSystem", "122", "Incorrect password"));
            }
        }
        return new Response(true, tradingSystemImpl.errMsgGenerator("Server", "TradingSystem", "125", "User not found"));
    }

    //TODO- implement the function
    public boolean VerifyPassword(String password) {
            return true;
    }


    //Store functions
    public synchronized boolean IsStoreNameExist(String storeName) {
        Set<Integer> storeSet = tradingSystemImpl.stores.keySet();
        for (Integer id : storeSet) {
            Store store = tradingSystemImpl.stores.get(id);
            if (storeName.equals(store.getName()))
                return true;
        }
        return false;
    }

    //Shopping Cart functions
    public boolean checkBuyingPolicy(Integer productID, Integer storeID, Integer quantity, ConcurrentHashMap<Integer, Integer> productsInTheBug) {
        return tradingSystemImpl.stores.get(storeID).checkBuyingPolicy(productID,quantity,productsInTheBug);
    }
    public boolean checkProductsExistInTheStore(Integer storeID, Integer productID,  Integer quantity) {
        if (tradingSystemImpl.stores.containsKey(storeID))
            return tradingSystemImpl.stores.get(storeID).checkProductsExistInTheStore(productID,quantity);
        else
            return false;
    }


}
