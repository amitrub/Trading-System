package TradingSystem.Server.DomainLayer.TradingSystemComponent;



import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.Service_Layer.DummyUser;
import TradingSystem.Server.Service_Layer.Response;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TradingSystem {

    private int nextUserID;
    private ConcurrentHashMap<String,Integer> connectedUser;

    private ConcurrentHashMap<Integer,User> systemAdmins;
    public ConcurrentHashMap<Integer,User> users;
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
        User defaultAdmin = new User(getNextUserID(),"amit","qweasd");
        this.systemAdmins.put(defaultAdmin.getId(),defaultAdmin);
        this.users.put(defaultAdmin.getId(),defaultAdmin);
    }

    //Check if there is a user if the same name then return -1
    //If there is no new user creator adds it to users in the hashmap and returns an ID number
    public Response Register(DummyUser dummyUser){
        if(IsUserNameExist(dummyUser.getUserName()))
            return new Response(-1, "Error user name is taken");
        int id = getNextUserID();
        User newUser = new User(id, dummyUser.getUserName(), dummyUser.getPassword());
        users.put(id, newUser);
        String connID = getNextConnectedUserID();
        connectedUser.put(connID,id);
        printUsers();
        return new Response(id, connID, "Registration was successful");
    }

    public void printUsers()
    {
        Iterator it = this.users.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            User user = (User) pair.getValue();
            System.out.println(user);
        }
    }

    public boolean IsUserNameExist(String userName) {
        Iterator it = this.users.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int id = (int)pair.getKey();
            User user = (User) pair.getValue();
            if(userName.equals(user.getUserName()))
                return true;
        }
        return false;
    }


    //Finds if the user exists and if the password is correct, if not returns 1 and error message
    //If the user exists and a correct password returns an ID number returns an ID number
    public Response Login(DummyUser dummyUser){
        int id = ValidPassword(dummyUser.getUserName(),dummyUser.getPassword());
        if(id == -1)
            return new Response(-1, "Error in login");
        String connID = getNextConnectedUserID();
        connectedUser.put(connID,id);
        printUsers();
        return new Response(id, connID, "Login was successful");
    }

    public int ValidPassword(String userName, String password) {
        Iterator it = this.users.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int id = (int)pair.getKey();
            User user = (User) pair.getValue();
            if(userName.equals(user.getUserName()))
            {
                if(password.equals(user.getPassword()))
                    return id;
                else
                    return -1;
            }
        }
        return -1;
    }


    public Response Logout(String connID){
        if(connectedUser.containsKey(connID))
        {
            connectedUser.remove(connID);
            return new Response(-1,  "Logout was successful");
        }
        else
        {
            return new Response(-1, "User not login");
        }
    }
}
