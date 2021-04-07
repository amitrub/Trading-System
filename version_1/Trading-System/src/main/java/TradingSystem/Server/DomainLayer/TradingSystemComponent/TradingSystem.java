package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.ServiceLayer.DummyObject.DummySearch;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyUser;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import static TradingSystem.Server.ServiceLayer.Configuration.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class TradingSystem {

    private ConcurrentHashMap<Integer,Integer> systemAdmins;
    private ConcurrentHashMap<String,Integer> connectedSubscribers;

    public ConcurrentHashMap<Integer,User> subscribers;
    public ConcurrentHashMap<String,User> guests;
    private ConcurrentHashMap<Integer, Store> stores;

//    Singleton
    private static TradingSystem tradingSystem = null;

    private TradingSystem(){
        this.connectedSubscribers = new ConcurrentHashMap<>();
        this.subscribers = new ConcurrentHashMap<>();
        this.stores = new ConcurrentHashMap<>();
        this.systemAdmins = new ConcurrentHashMap<>();
    }

    public static TradingSystem getInstance(){
        if (tradingSystem == null){
            tradingSystem = new TradingSystem();
            tradingSystem.Initialization();
        }
        return tradingSystem;
    }

    private void Initialization(){
        User defaultAdmin = new User("amit","qweasd");
        this.systemAdmins.put(defaultAdmin.getId(),defaultAdmin.getId());
        this.subscribers.put(defaultAdmin.getId(),defaultAdmin);
        printUsers();
    }

    public void printUsers(){
        Iterator it = this.subscribers.entrySet().iterator();
        System.out.println("-----------------------------------------------");
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            User user = (User) pair.getValue();
            if(connectedSubscribers.values().contains(user.getId()))
                System.out.println(ANSI_GREEN + user + "(connected)" + ANSI_RESET);
            else
                System.out.println(ANSI_PURPLE + user + "(guest)" + ANSI_RESET);
        }
        System.out.println("-----------------------------------------------");
    }

    public Response connectSystem(){
        User newGuest = new User();
        String connID = getConnIDGuest(newGuest);
        return new Response(false, "Connect system was successful");
    }

    private synchronized String getConnIDGuest(User newGuest) {
        String uniqueID = "";
        boolean canExit = false;
        while (!canExit)
        {
            uniqueID = UUID.randomUUID().toString();
            if((!guests.containsKey(uniqueID))&&(!connectedSubscribers.containsKey(uniqueID)))
            {
                guests.put(uniqueID,newGuest);
                canExit= true;
            }
        }
        return uniqueID;
    }

    public Response Exit(String connID){
        if(connectedSubscribers.containsKey(connID)){
            connectedSubscribers.remove(connID);
            return new Response(false,  "Exit System was successful");
        }
        else if(guests.containsKey(connID)){
            connectedSubscribers.remove(connID);
            return new Response(false,  "Exit System was successful");
        }
        else{
            printUsers();
            return new Response(true, "User not connect to system");
        }
    }

    //Check if there is a user if the same name then return -1
    //If there is no new user creator adds it to users in the hashmap and returns an ID number
    public Response Register(String userName, String password){
        if(IsUserNameExist(userName)) {
            return new Response(true, errMsgGenerator("Server", "TradingSystem", "62", "Error user name is taken"));
        }
        User newUser = new User(userName, password);
        subscribers.put(newUser.getId(), newUser);
        printUsers();
        Response res = new Response(newUser.getId(), "",false, "Registration was successful");
        return res;
    }

    //return connID and add user to connection Hash Map
    private synchronized String getConnIDSubscriber(Integer userID) {
        String uniqueID = "";
        boolean canExit = false;
        while (!canExit)
        {
            uniqueID = UUID.randomUUID().toString();
            if((!guests.containsKey(uniqueID))&&(!connectedSubscribers.containsKey(uniqueID)))
            {
                connectedSubscribers.put(uniqueID,userID);
                canExit= true;
            }
        }
        return uniqueID;
    }

    //return true if user name is exist in the system
    public boolean IsUserNameExist(String userName) {
        Iterator it = this.subscribers.entrySet().iterator();
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
    public Response Login(String userName, String password){
        Response response = ValidPassword(userName,password);
        if(response.isErr())
            return response;
        String connID = getConnIDSubscriber(response.getUserID());
        printUsers();
        return new Response(response.getUserID(), connID, "Login was successful");
    }

    //if valid return Response(userId, "", false, "")
    //if not valid return Response(isErr: true, "Error Message")
    public Response ValidPassword(String userName, String password) {
        Iterator it = this.subscribers.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int id = (int)pair.getKey();
            User user = (User) pair.getValue();
            if(userName.equals(user.getUserName()))
            {
                if(password.equals(user.getPassword()))
                    return new Response(id,"", false,"");
                else
                    return new Response(true,errMsgGenerator("Server", "TradingSystem","122","Incorrect password"));
            }
        }
        return new Response(true,errMsgGenerator("Server", "TradingSystem","125","User not found"));
    }

    public Response Logout(String connID){
        if(connectedSubscribers.containsKey(connID)){
            connectedSubscribers.remove(connID);
            printUsers();
            return new Response(false,  "Logout was successful");
        }
        else{
            printUsers();
            return new Response(true, "User not login");
        }
    }

    public String errMsgGenerator(String side, String className, String line, String msg) {
        return side + " : <" + className + " in line >" + line + " ; \"" + msg + "\"";
    }

    public ConcurrentHashMap<Integer, Store> getStores() {
        return stores;
    }

    public List<DummyStore> getAllstores(){
        List<DummyStore> list=new LinkedList<>();
        for(Map.Entry<Integer,Store> storecurr:stores.entrySet()){
            list.add(new DummyStore(storecurr.getKey(),storecurr.getValue().getName(),storecurr.getValue().getRate()));
        }
        return list;
    }


    public List<DummySearch> getAllPoductsByStore(DummyStore store){
        return stores.get(store.getId()).getAllProducts();
    }

}
