package TradingSystem.Server.DomainLayer.TradingSystemComponent;



import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.UserComponent.User;

import java.util.concurrent.ConcurrentHashMap;

public class TradingSystem {

    private ConcurrentHashMap<Integer,User> connectedUser;

    private ConcurrentHashMap<Integer,User> users;
    private ConcurrentHashMap<Integer, Store> stores;

//    Singleton
    private static TradingSystem tradingSystem = null;

    private TradingSystem()
    {
        connectedUser = new ConcurrentHashMap<>();
        users = new ConcurrentHashMap<>();
        stores = new ConcurrentHashMap<>();
    }

    public static TradingSystem getInstance()
    {
        if (tradingSystem == null)
            tradingSystem = new TradingSystem();

        return tradingSystem;
    }

    public Integer Register(String userName, String password){
        return 7;
    }



}
