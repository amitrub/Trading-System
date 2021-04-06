package TradingSystem.Server.DomainLayer.TradingSystemComponent;



import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.Service_Layer.DummyUser;
import TradingSystem.Server.Service_Layer.Response;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TradingSystem {

    private int nextUserID;
    private ConcurrentHashMap<String,Integer> connectedUser;

    private ConcurrentHashMap<Integer,User> systemAdmins;
    private ConcurrentHashMap<Integer,User> users;
    private ConcurrentHashMap<Integer, Store> stores;

//    Singleton
    private static TradingSystem tradingSystem = null;

    private TradingSystem()
    {

        this.connectedUser = new ConcurrentHashMap<>();
        this.users = new ConcurrentHashMap<>();
        this.stores = new ConcurrentHashMap<>();
        this.systemAdmins = new ConcurrentHashMap<>();
        this.nextUserID = 0;
    }

    public static TradingSystem getInstance()
    {
        if (tradingSystem == null)
        {
            tradingSystem = new TradingSystem();
            tradingSystem.Initialization();
        }


        return tradingSystem;
    }

    private synchronized String getNextConnectedUserID() {
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }

    private synchronized int getNextUserID() {
        this.nextUserID++;
        return this.nextUserID;
    }

    private void Initialization(){
        User defaultAdmin = new User(1,"amit","qweasd");
        this.systemAdmins.put(defaultAdmin.getId(),defaultAdmin);
        this.users.put(defaultAdmin.getId(),defaultAdmin);
    }

    //Check if there is a user if the same name then return -1
    //If there is no new user creator adds it to users in the hashmap and returns an ID number
    public Response Register(DummyUser dummyUser){
        String msg = "";
        return new Response(1, msg);
    }

    //Finds if the user exists and if the password is correct, if not returns 1 and error message
    //If the user exists and a correct password returns an ID number returns an ID number
    public Response Login(DummyUser dummyUser){
        System.out.println(dummyUser.getUserName());
        System.out.println(dummyUser.getPassword());
        String msg = "";
        return new Response(2, msg);
    }


    public Response Logout(int connID){
        String msg = "";
        return new Response(3, msg);

    }



}
