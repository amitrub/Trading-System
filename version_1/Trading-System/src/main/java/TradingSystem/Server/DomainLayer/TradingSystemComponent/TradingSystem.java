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

    private ConcurrentHashMap<Integer,Integer> systemAdmins;
    private ConcurrentHashMap<String,Integer> connectedUser;

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
    }

    public static TradingSystem getInstance()
    {
        if (tradingSystem == null){
            tradingSystem = new TradingSystem();
            tradingSystem.Initialization();
        }
        return tradingSystem;
    }

    private void Initialization(){
        User defaultAdmin = new User("amit","qweasd");
        this.systemAdmins.put(defaultAdmin.getId(),defaultAdmin.getId());
        this.users.put(defaultAdmin.getId(),defaultAdmin);
    }

    public void printUsers(){
        Iterator it = this.users.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            User user = (User) pair.getValue();
            System.out.println(user);
        }
    }

    //Check if there is a user if the same name then return -1
    //If there is no new user creator adds it to users in the hashmap and returns an ID number
    public Response Register(DummyUser dummyUser){
        if(IsUserNameExist(dummyUser.getUserName()))
            return new Response(true, errMsgGenerator("Server", "TradingSystem","62","Error user name is taken"));
        User newUser = new User(dummyUser.getUserName(), dummyUser.getPassword());
        users.put(newUser.getId(), newUser);
        printUsers();
        return new Response(newUser.getId(), "",false, "Registration was successful");
    }

    //return connID and add user to connection Hash Map
    private synchronized String ConnectedUserID(Integer userID) {
        String uniqueID = "";
        boolean canExit = false;
        while (!canExit)
        {
            uniqueID = UUID.randomUUID().toString();
            if(!connectedUser.containsKey(uniqueID))
            {
                connectedUser.put(uniqueID,userID);
                canExit= true;
            }
        }
        return uniqueID;
    }

    //return true if user name is exist in the system
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
        Response response = ValidPassword(dummyUser.getUserName(),dummyUser.getPassword());
        if(response.isErr())
            return response;
        String connID = ConnectedUserID(response.getUserID());
        printUsers();
        return new Response(response.getUserID(), connID, "Login was successful");
    }

    //if valid return Response(userId, "", false, "")
    //if not valid return Response(isErr: true, "Error Message")
    public Response ValidPassword(String userName, String password) {
        Iterator it = this.users.entrySet().iterator();
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
        if(connectedUser.containsKey(connID)){
            connectedUser.remove(connID);
            return new Response(false,  "Logout was successful");
        }
        else{
            return new Response(true, "User not login");
        }
    }

    public String errMsgGenerator(String side, String className, String line, String msg) {
        return side + " : <" + className + " in line >" + line + " ; \"" + msg + "\"";
    }
}
