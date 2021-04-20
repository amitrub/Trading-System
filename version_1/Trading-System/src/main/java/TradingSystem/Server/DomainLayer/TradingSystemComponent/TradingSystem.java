package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.UserComponent.ManagerPermission;
import TradingSystem.Server.DomainLayer.UserComponent.OwnerPermission;
import TradingSystem.Server.DomainLayer.UserComponent.SystemManagerPermission;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.ServiceLayer.DummyObject.*;
import TradingSystem.Server.ServiceLayer.LoggerController;

import static TradingSystem.Server.ServiceLayer.Configuration.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;


public class TradingSystem {

    public Validation validation;

    private ConcurrentHashMap<Integer, Integer> systemAdmins;
    private ConcurrentHashMap<String, Integer> connectedSubscribers;

    public ConcurrentHashMap<Integer, User> subscribers;
    public ConcurrentHashMap<String, User> guests;
    public ConcurrentHashMap<Integer, Store> stores;
    //storeID_systemManagerPermission
    private ConcurrentHashMap<Integer, SystemManagerPermission> systemManagerPermissions;

    //    Singleton
    private static TradingSystem tradingSystem = null;
    private static final LoggerController loggerController=LoggerController.getInstance();

    private TradingSystem() {
        this.connectedSubscribers = new ConcurrentHashMap<>();
        this.subscribers = new ConcurrentHashMap<>();
        this.guests = new ConcurrentHashMap<>();
        this.stores = new ConcurrentHashMap<>();
        this.systemAdmins = new ConcurrentHashMap<>();
        this.systemManagerPermissions=new ConcurrentHashMap<>();
    }

    public static TradingSystem getInstance() {
        if (tradingSystem == null) {
            tradingSystem = new TradingSystem();
            tradingSystem.validation = new Validation();
            tradingSystem.Initialization();
        }
        return tradingSystem;
    }

    public void Initialization() {
        this.connectedSubscribers = new ConcurrentHashMap<>();
        this.subscribers = new ConcurrentHashMap<>();
        this.guests = new ConcurrentHashMap<>();
        this.stores = new ConcurrentHashMap<>();
        this.systemAdmins = new ConcurrentHashMap<>();
        this.systemManagerPermissions=new ConcurrentHashMap<>();

        User defaultAdmin = new User("amit", "qweasd");
        int userID = defaultAdmin.getId();
        this.systemAdmins.put(userID, userID);
        this.subscribers.put(userID, defaultAdmin);
        this.systemManagerPermissions.put(userID,new SystemManagerPermission());
        //TODO: to delete after
        String connID = "479f239c-797c-4bdb-8175-980acaabf070";
        this.connectedSubscribers.put(connID, userID);
        AddStore(userID, connID, "store1");
        AddProductToStore(userID,connID,1,"prod1","sport", 7.0, 7 );

        User user1 = new User("hadass", "1234");
        userID = user1.getId();
        this.subscribers.put(userID, user1);
        connID = "38095a9d-09dd-41ec-bd04-3a6d0da1c386";
        this.connectedSubscribers.put(connID, userID);
        printUsers();
    }

    public String errMsgGenerator(String side, String className, String line, String msg) {
        return side + " : <" + className + " in line >" + line + " ; \"" + msg + "\"";
    }

    //prints for debug
    public void printUsers() {
        Set<Integer> userSet = this.subscribers.keySet();

        System.out.println(ANSI_GREEN + "Connected Users" + ANSI_RESET);
        for (Integer id : userSet){
            User user = this.subscribers.get(id);
            if (connectedSubscribers.values().contains(user.getId()))
                System.out.println(ANSI_GREEN + user + "(connected)" + ANSI_RESET);
        }

        System.out.println("-----------------------------------------------");
        System.out.println(ANSI_PURPLE + "Registered Users");
        for (Integer id : userSet){
            User user = this.subscribers.get(id);
            if (!connectedSubscribers.values().contains(user.getId()))
                System.out.println(ANSI_PURPLE + user + "(not connected)");
        }

        System.out.println("-----------------------------------------------" + ANSI_RESET);
        Set<String> guestSet = this.guests.keySet();
        System.out.println(ANSI_YELLOW + "Guests");
        for (String id : guestSet){
            User user = this.guests.get(id);
            System.out.println(user + "(connected)");
        }
        System.out.println("-----------------------------------------------" + ANSI_RESET);
        System.out.println("---------------------------------------------\n");
    }
    public void printStores() {
        Set<Integer> storeSet = this.stores.keySet();
        System.out.println("-----------------------------------------------");
        for (Integer id : storeSet){
            Store store = this.stores.get(id);
            System.out.println(ANSI_RED + "Stores" + "\n" + store  + ANSI_RED);
        }
        System.out.println("-----------------------------------------------");
    }
    public void printProducts() {
        Set<Integer> storeSet = this.stores.keySet();
        System.out.println("-----------------------------------------------");
        for (Integer id : storeSet){
            System.out.println(ANSI_WHITE + "Products in Store "  + id +":\n" + ANSI_WHITE);
            List<Product> Products = this.stores.get(id).getProducts();
            for (Product p : Products) {
                int quantity= this.stores.get(id).getQuantity(p.getProductID());
                System.out.println(ANSI_WHITE + p + " with quantity of- "+ quantity + ":\n" + ANSI_WHITE);
            }
            }
        System.out.println("-----------------------------------------------");
    }


    /**
     * @requirement 2.1
     *
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    //User functions
    public NewResponse ConnectSystem() {
        User newGuest = new User();
        String connID = connectGuestToSystemConnID(newGuest);
        NewResponse res = new NewResponse("Connect system was successful");
        res.AddConnID(connID);
        return res;
    }
    private synchronized String connectGuestToSystemConnID(User newGuest) {
        String uniqueID = "";
        boolean canExit = false;
        while (!canExit) {
            uniqueID = UUID.randomUUID().toString();
            if ((!guests.containsKey(uniqueID)) && (!connectedSubscribers.containsKey(uniqueID))) {
                guests.put(uniqueID, newGuest);
                canExit = true;
            }
        }
        return uniqueID;
    }
    public NewResponse Exit(String connID) {
        if (connectedSubscribers.containsKey(connID)) {
            connectedSubscribers.remove(connID);
            return new NewResponse("Exit System was successful");
        } else if (guests.containsKey(connID)) {
            guests.remove(connID);
            return new NewResponse("Exit System was successful");
        } else {
            return new NewResponse(true, "User not connect to system");
        }
    }
    //Check if there is a user if the same name then return -1
    //If there is no new user creator adds it to users in the hashmap and returns an ID number
    public NewResponse Register(String connID, String userName, String password) {
            if (guests.containsKey(connID) || connectedSubscribers.containsKey(connID)){
            if (validation.IsUserNameExist(userName)) {
                loggerController.WriteErrorMsg("User "+userName+" try to register to the system and failed");
                return new NewResponse(true, errMsgGenerator("Server", "TradingSystem", "62", "Error user name is taken"));
            }
            User newUser = new User(userName, password);
            subscribers.put(newUser.getId(), newUser);
//        guests.remove(connID);
            loggerController.WriteErrorMsg("User "+userName+" register to the system successfully");

            NewResponse res = new NewResponse("Registration was successful");
            res.AddConnID(connID);
            res.AddUserID(newUser.getId());

            return res;
        }
        else
            return new NewResponse(true, "Error in connID");
    }

    //return connID and add user to connection Hash Map
    private synchronized String connectSubscriberToSystemConnID(Integer userID) {
        String uniqueID = "";
        boolean canExit = false;
        while (!canExit) {
            uniqueID = UUID.randomUUID().toString();
            if ((!guests.containsKey(uniqueID)) && (!connectedSubscribers.containsKey(uniqueID))) {
                connectedSubscribers.put(uniqueID, userID);
                canExit = true;
            }
        }
        return uniqueID;
    }
    //Finds if the user exists and if the password is correct, if not returns 1 and error message
    //If the user exists and a correct password returns an ID number returns an ID number
    public NewResponse Login(String guestConnID, String userName, String password) {
        NewResponse response = validation.ValidPassword(userName, password);
        if (response.getIsErr())
            return response;
        User myGuest = guests.get(guestConnID);
        subscribers.get(response.getUserID()).mergeToMyCart(myGuest.getShoppingCart());
        String connID = connectSubscriberToSystemConnID(response.getUserID());
        guests.remove(guestConnID);
        NewResponse res = new NewResponse("Login was successful");
        res.AddUserID(response.getUserID());
        res.AddConnID(connID);
        return res;
    }
    public NewResponse Logout(String connID) {
        if (connectedSubscribers.containsKey(connID)) {
            User myUser = subscribers.get(connectedSubscribers.get(connID));
            connectedSubscribers.remove(connID);
            User newGuest = new User();
            //TODO: mybe do deep copy
            newGuest.setShoppingCart(myUser.getShoppingCart());
            String guestConnID = connectGuestToSystemConnID(newGuest);
            NewResponse res = new NewResponse("Logout was successful");
            res.AddConnID(guestConnID);
            return res;
        } else {
            return new NewResponse(true, "User not login");
        }
    }

    //Store functions
    public NewResponse AddStore(int userID, String connID, String storeName){
        if(ValidConnectedUser(userID, connID)){
            if (validation.IsStoreNameExist(storeName)){
                loggerController.WriteErrorMsg("User "+userID+" try to add store to the system and failed");
                return new NewResponse(true, "Error Store name is taken");
            }
            else {
                Store newStore = new Store(storeName, userID);
                User user = subscribers.get(userID);
                user.AddStore(newStore.getId());
                stores.put(newStore.getId(),newStore);
                loggerController.WriteErrorMsg("User "+userID+" add store to the system "+ storeName+" successfully");
                return new NewResponse( "Add Store was successful");
            }
        }
        else{
            return new NewResponse(true, "Error in User details");
        }
    }
    public NewResponse ShowAllStores() {
        List<DummyStore> list = new ArrayList<>();
        for (Map.Entry<Integer, Store> currStore : stores.entrySet()) {
            list.add(new DummyStore(currStore.getValue()));
        }
        NewResponse res = new NewResponse("num of stores in the system is " + list.size());
        res.AddPair("stores", list);
        System.out.println("777777777777777777777777777777777777777777777");
        System.out.println(res);
        return res;
    }

    //Product functions
    public NewResponse AddProductToStore(int userID, String connID, int storeID, String productName, String category, double price, int quantity){
        if(ValidConnectedUser(userID, connID)){
            if(this.hasPermission(userID,storeID,User.Permission.AddProduct)) {
                if(price>=0) {
                    if(quantity>0) {
                        NewResponse res = stores.get(storeID).AddProductToStore(productName, price, category, quantity);
                        printProducts();
                        loggerController.WriteLogMsg("User " + userID + " add product " + productName + " to store " + storeID + " successfully");
                        return res;
                    }
                    loggerController.WriteErrorMsg("User "+userID+" try to add product "+ productName+" to store "+storeID+" and failed");
                    return new NewResponse(true, "The quantity of the product can't be negative");

                }
                loggerController.WriteErrorMsg("User "+userID+" try to add product "+ productName+" to store "+storeID+" and failed");
                return new NewResponse(true, "The price of the product can't be negative");
            }
            loggerController.WriteErrorMsg("User "+userID+" try to add product "+ productName+" to store "+storeID+" and failed");
            return new NewResponse(true, "The User is not allowed to add a product");
        }
        else{
            loggerController.WriteErrorMsg("User "+userID+" try to add product "+ productName+" to store "+storeID+" and failed");
            return new NewResponse(true, "Error in User details");
        }
    }

    private boolean hasPermission(int userID, int storeID, User.Permission p) {
        if(this.subscribers.containsKey(userID)){
            User u=this.subscribers.get(userID);
            if(u.getOwnerPermission(storeID)!=null){
                return u.getOwnerPermission(storeID).hasPermission(p);
            }
            if (u.getManagerPermission(storeID)!=null){
                return u.getManagerPermission(storeID).hasPermission(p);
            }
            if(this.systemManagerPermissions.get(userID)!=null){
                return this.systemManagerPermissions.get(userID).hasPermission(p);
            }
        }
        return false;
    }

    public NewResponse ChangeQuantityProduct(int userID, String connID, int storeID, int productId, int quantity){
        if(ValidConnectedUser(userID, connID)){
            if(hasPermission(userID,storeID,User.Permission.AddProduct)) {
                if(quantity>0) {
                    NewResponse res = stores.get(storeID).addProductToInventory(productId, quantity);
                    printProducts();
                    loggerController.WriteLogMsg("User " + userID + " add " + quantity + " products of " + productId + " to store " + storeID + " successfully");
                    return res;
                }
                loggerController.WriteErrorMsg("User "+userID+" try to add "+ quantity+" products of "+productId+" to store "+storeID+" and failed");
                return new NewResponse(true, "The quantity of the product can't be negative");

            }
            loggerController.WriteErrorMsg("User "+userID+" try to add "+ quantity+" products of "+productId+" to store "+storeID+" and failed");
            return new NewResponse(true, "The User is not allowed to add products to the inventory");
        }
        else{
            loggerController.WriteErrorMsg("User "+userID+" try to add "+ quantity+" products of "+productId+" to store "+storeID+" and failed");
            return new NewResponse(true, "Error in User details");
        }
    }
    public NewResponse RemoveProduct(int userID, int storeID, int productID, String connID) {
        if(ValidConnectedUser(userID, connID)){
            if(hasPermission(userID,storeID,User.Permission.DeleteProduct)) {
                NewResponse res = stores.get(storeID).deleteProduct(productID);
                printProducts();
                loggerController.WriteLogMsg("User "+userID+" remove product"+ productID+" from store "+storeID+" successfully");
                return res;
            }
            loggerController.WriteErrorMsg("User "+userID+" try to remove product"+ productID+" from store "+storeID+" and failed");
            return new NewResponse(true, "The User is not allowed to remove products from the inventory");
        }
        else{
            loggerController.WriteErrorMsg("User "+userID+" try to remove product"+ productID+" from store "+storeID+" and failed");
            return new NewResponse(true, "Error in User details");
        }
    }
    public NewResponse ShowStoreProducts(int storeID) {
        if(stores.containsKey(storeID)){
            List<DummyProduct> list = stores.get(storeID).ShowStoreProducts();
            NewResponse res = new NewResponse("num of products in the store is " + list.size());
            res.AddPair("products", list);
            return res;
        }
        else
            return new NewResponse(true, "store not found");
    }

    //Shopping Cart functions
    public NewResponse AddProductToCart(String connID, int StoreId, int productId, int quantity){
            if(guests.containsKey(connID)){
                User myGuest= guests.get(connID);
                return myGuest.AddProductToCart(StoreId,productId,quantity);
            }
            else if(connectedSubscribers.containsKey(connID)){
                int userID= connectedSubscribers.get(connID);
                return subscribers.get(userID).AddProductToCart(StoreId,productId,quantity);
            }
            else {
                return new NewResponse(true, "User not connect to system");
            }
    }
    public NewResponse ShowShoppingCart(String connID){
        if(guests.containsKey(connID)) {
            List<DummyProduct> list = guests.get(connID).ShowShoppingCart();
            NewResponse res = new NewResponse("num of products in my Shopping Cart is " + list.size());
            res.AddPair("products", list);
            return res;
        }
        else if(connectedSubscribers.containsKey(connID)) {
            int userID = connectedSubscribers.get(connID);
            List<DummyProduct> list = subscribers.get(userID).ShowShoppingCart();
            NewResponse res = new NewResponse("num of products in my Shopping Cart is " + list.size());
            res.AddPair("products", list);
            return res;
        }
        else {
            return new NewResponse(true, "user not Exist");
        }
    }
    public NewResponse guestPurchase(String connID, String name, String credit_number, String phone_number, String address){
        if(guests.containsKey(connID)){
            User myGuest= guests.get(connID);
            return myGuest.guestPurchase(name, credit_number, phone_number, address);
        }
        else {
            return new NewResponse(true, "User not connect to system");
        }
    }
    public NewResponse subscriberPurchase(int userID, String connID, String credit_number, String phone_number, String address){
        if(ValidConnectedUser(userID, connID)){
            User user = subscribers.get(userID);
            return user.subscriberPurchase(credit_number, phone_number, address);
        }
        else {
            return new NewResponse(true, "User not connect to system");
        }
    }

    //History Cart functions
    public void addHistoryToStoreAndUser(ShoppingHistory sh, boolean isGuest) {
        this.stores.get(sh.getStoreID()).addHistory(sh);
        if (!isGuest)
            this.subscribers.get(sh.getUserID()).addHistory(sh);
    }

    //TODO: to check
    public NewResponse SearchProduct(String name, String category, int minprice, int maxprice){
        List<DummyProduct> dummyProducts = new ArrayList<>();
        for(Store store: stores.values()){
           // if(((prank==-1 || store.getRate()>=srank) && !store.SearchByName(name, minprice, maxprice,prank).isEmpty())){
                dummyProducts.addAll(store.SearchProduct(name,category, minprice, maxprice));
            }
        NewResponse res = new NewResponse("num of products from search is " + dummyProducts.size());
        res.AddPair("products", dummyProducts);
        return res;
    }

    //TODO: to check
    public List<DummyProduct> SearchProductByName(String name, int minprice, int maxprice, int prank , int srank){
        List<DummyProduct> dummyProducts = new ArrayList<>();
        for(Store store: stores.values()){
            if(((prank==-1 || store.getRate()>=srank) && !store.SearchByName(name, minprice, maxprice,prank).isEmpty())){
                dummyProducts.addAll(store.SearchByName(name, minprice, maxprice,prank));
            }
        }
        return dummyProducts;
    }
    //TODO: to check
    public List<DummyProduct> SearchProductByCategory(String category, int minprice, int maxprice, int prank , int srank){
        List<DummyProduct> dummyProducts = new ArrayList<>();
        for(Store store: stores.values()){
            if(!store.SearchByCategory(category, minprice, maxprice,prank).isEmpty()){
                dummyProducts.addAll(store.SearchByCategory(category, minprice, maxprice,prank));
            }
        }
        return dummyProducts;
    }

    public NewResponse AddNewOwner(int userID, String connID, int storeID, int newOwner)  {
        if (ValidConnectedUser(userID, connID)) {
            if (this.subscribers.get(newOwner) != null) {
                while (!this.subscribers.get(newOwner).userIsLock()) {
                    try{
                        this.wait(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.subscribers.get(newOwner).lockUser();
                NewResponse res = this.AbleToAddOwner(userID, storeID, newOwner);
                if (!res.getIsErr()) {
                        User NU = this.subscribers.get(newOwner);
                        OwnerPermission OP = new OwnerPermission(newOwner, storeID);
                        OP.setAppointmentId(userID);
                        NU.AddStoreInOwner(storeID, OP);
                        stores.get(storeID).addNewOwner(userID, newOwner);
                        // stores.get(storeID).addOnerPermission(OP);
                        this.subscribers.get(newOwner).unlockUser();
                        loggerController.WriteLogMsg("User " + userID + " add owner " + newOwner + " to store " + storeID + " successfully");
                        return new NewResponse("The owner Added successfully");

                    }
                this.subscribers.get(newOwner).unlockUser();
                return res;

                }
            loggerController.WriteErrorMsg("User " + userID + " try to Add " + newOwner + " to be the owner of store " + storeID + " and failed. " + newOwner + " is not subscriber");
            return new NewResponse(true, "The user " + newOwner + " is not subscriber, so he can not be owner for store");
        }
        loggerController.WriteErrorMsg("User " + userID + " try to Add owner to store " + storeID + " and failed. The err message: Error in User details");
        return new NewResponse(true, "Error in User details");
    }

    private NewResponse AbleToAddOwner(int userID, int storeID, int newOwner) {
        if (this.subscribers.containsKey(userID)) {
            if (this.subscribers.containsKey(newOwner)) {
                if (this.subscribers.get(userID).getMyOwnerStore().contains(storeID)){
                    if (!stores.get(storeID).checkOwner(newOwner)) {
                        if (!stores.get(storeID).checkManager(newOwner)){
                            if(this.hasPermission(userID,storeID,User.Permission.AppointmentOwner))
                                {
                                return new NewResponse("It is possible to add the user as the owner");
                                }
                            else{
                                loggerController.WriteErrorMsg("User " + userID + " try to Add " +newOwner+" to be the owner of store " + storeID + " and failed. "+ userID+" is not allowed to add owner to the store");
                                return new NewResponse(true, "User "+userID+" is not allowed to add owner to the store");
                                }
                        }
                        loggerController.WriteErrorMsg("User " + userID + " try to Add " +newOwner+" to be the owner of store " + storeID + " and failed. "+ newOwner+" is already manages the store");
                        return new NewResponse(true, "User "+newOwner+" is manages the store, so he can not be owner");
                    }
                    loggerController.WriteErrorMsg("User " + userID + " try to Add "+newOwner+" to be the owner of store "+storeID + " and failed. "+ newOwner+" is already owner the store");
                    return new NewResponse(true, "User "+newOwner+" is owner the store, so he can not appoint to owner again");
                }
                loggerController.WriteErrorMsg("User " + userID + " try to Add "+newOwner+" to be the owner of store "+storeID + " and failed. "+ userID+" is not the owner of the store");
                return new NewResponse(true, "User "+userID+" is not the owner of the store, so he can not appoint new owner to the store");
            }
            loggerController.WriteErrorMsg("User " + userID + " try to Add "+newOwner+" to be the owner of store "+storeID + " and failed. "+ newOwner+" is not subscriber");
            return new NewResponse(true, "User "+newOwner+" is not subscriber, so he can not be owner for store");
        }
        loggerController.WriteErrorMsg("User " + userID + " try to Add "+newOwner+" to be the owner of store "+storeID + " and failed. "+ userID+" is not not subscriber");
        return new NewResponse(true, "User "+userID+" is not subscriber, so he can not appoint owner for store");
    }

    public NewResponse AddNewManager(int userID, String connID, int storeID, int newManager) {
        if (ValidConnectedUser(userID, connID))
        {
            if (this.subscribers.get(newManager) != null) {
                while (!this.subscribers.get(newManager).userIsLock()) {
                    try{
                        this.wait(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                this.subscribers.get(newManager).lockUser();
                NewResponse res = this.AbleToAddManager(userID, storeID, newManager);
                if (!res.getIsErr())
                {
                        User NM = this.subscribers.get(newManager);
                        ManagerPermission MP= new ManagerPermission(newManager, storeID);
                        MP.setAppointmentId(userID);
                        NM.AddStoreInManager(storeID, MP);
                        stores.get(storeID).addNewManager(userID, newManager);
                        //stores.get(storeID).addManagerPermission(OM);
                        this.subscribers.get(newManager).unlockUser();
                        loggerController.WriteLogMsg("User " + userID + " add manager " + newManager + " to store " + storeID + " successfully");
                        return new NewResponse( "The manager Added successfully");
                }
                this.subscribers.get(newManager).unlockUser();
                return res;
                }

            loggerController.WriteErrorMsg("User " + userID + " try to Add "+newManager+" to be the manager of store "+storeID + " and failed. "+ newManager+" is not subscriber");
            return new NewResponse(true, "The user "+newManager+" is not subscriber, so he can not be manager for store");
        }
        loggerController.WriteErrorMsg("User " + userID + " try to add manager to store " + storeID + " and failed. The err message: Error in User details");
        return new NewResponse(true, "Error in User details");
    }
    private NewResponse AbleToAddManager(int userID, int storeID, int newManager){
        if (this.subscribers.containsKey(userID)) {
            if (this.subscribers.containsKey(newManager)) {
                if (this.subscribers.get(userID).getMyOwnerStore().contains(storeID)){
                    if (!stores.get(storeID).checkOwner(newManager)) {
                        if (!stores.get(storeID).checkManager(newManager)){
                            if(this.hasPermission(userID,storeID,User.Permission.AppointmentManager))
                            {
                                return new NewResponse("It is possible to add the user as the owner");
                            }
                            else{
                                loggerController.WriteErrorMsg("User " + userID + " try to Add " +newManager+" to be the owner of store " + storeID + " and failed. "+ userID+" is not allowed to add manager to the store");
                                return new NewResponse(true, "The user "+userID+" is not allowed to add manager to the store");
                            }
                        }
                        loggerController.WriteErrorMsg("User " + userID + " try to Add " +newManager+" to be the Manager of store " + storeID + " and failed. "+ newManager+" is already manages the store");
                        return new NewResponse(true, "The user "+newManager+" is manages the store, so he can not appoint to Manager again");
                    }
                    loggerController.WriteErrorMsg("User " + userID + " try to Add "+newManager+" to be the owner of store "+storeID + " and failed. "+ newManager+" is already owner the store");
                    return new NewResponse(true, "The user "+newManager+" is owner the store, so he can not appoint to Manager");
                }
                loggerController.WriteErrorMsg("User " + userID + " try to Add "+newManager+" to be the manager of store "+storeID + " and failed. "+ userID+" is not the owner of the store");
                return new NewResponse(true, "The user "+userID+" is not the owner of the store, so he can not appoint new manager to the store");
            }
            loggerController.WriteErrorMsg("User " + userID + " try to Add "+newManager+" to be the manager of store "+storeID + " and failed. "+ newManager+" is not subscriber");
            return new NewResponse(true, "The user "+newManager+" is not subscriber, so he can not be manager for store");
        }
        loggerController.WriteErrorMsg("User " + userID + " try to Add "+newManager+" to be the manager of store "+storeID + " and failed. "+ userID+" is not not subscriber");
        return new NewResponse(true, "The user "+userID+" is not subscriber, so he can not appoint manager for store");
    }

    public NewResponse RemoveManager(int userID, String connID, int storeID, int ManagerToRemove)  {
        if (ValidConnectedUser(userID, connID)) {
            if (this.subscribers.get(ManagerToRemove) != null) {
                while (!this.subscribers.get(ManagerToRemove).userIsLock()) {
                    try{
                        this.wait(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                    this.subscribers.get(ManagerToRemove).lockUser();
                    NewResponse res = this.AbleToRemoveManager(userID, storeID, ManagerToRemove);
                    if (!res.getIsErr()) {
                        User MTR = this.subscribers.get(ManagerToRemove);
                        MTR.removeStore(storeID);
                        stores.get(storeID).removeManager(userID, ManagerToRemove);
                        this.subscribers.get(ManagerToRemove).unlockUser();
                        loggerController.WriteLogMsg("User " + userID + " remove manager " + ManagerToRemove + " from store " + storeID + " successfully");
                        return new NewResponse("The manager removed successfully");
                    }
                    this.subscribers.get(ManagerToRemove).unlockUser();
                    return res;
            }
            loggerController.WriteErrorMsg("User " + userID + " try to Remove " + ManagerToRemove + " from management the store " + storeID + " and failed. " + ManagerToRemove + " is not subscriber");
            return new NewResponse(true, "The user " + ManagerToRemove + " is not subscriber, so it impossible to remove him from management the store");
         }
        loggerController.WriteErrorMsg("User " + userID + " try to remove manager from store " + storeID + " and failed. The err message: Error in User details");
        return new NewResponse(true, "Error in User details");
    }
    private NewResponse AbleToRemoveManager(int userID, int storeID, int managerToRemove){
        if (this.subscribers.containsKey(userID)) {
            if (this.subscribers.containsKey(managerToRemove)) {
                if (this.subscribers.get(userID).getMyOwnerStore().contains(storeID)){
                        if (stores.get(storeID).checkManager(managerToRemove)){
                            User manager=subscribers.get(managerToRemove);
                            if(manager.getManagerPermission(storeID).getAppointmentId()==userID) {
                                if (this.hasPermission(userID,storeID,User.Permission.RemoveManager)) {
                                    return new NewResponse("It is possible to add the user as the owner");
                                } else {
                                    loggerController.WriteErrorMsg("User " + userID + " try to remove " + managerToRemove + " from be the manager of store " + storeID + " and failed. " + userID + " is not allowed to remove manager from the store");
                                    return new NewResponse(true, "The user " + userID + " is not allowed to remove manager from store");
                                }
                            }
                            loggerController.WriteErrorMsg("User " + userID + " try to remove " + managerToRemove + " from be the manager of store " + storeID + " and failed. " + userID + " is not the one who appointed the manager.");
                            return new NewResponse(true, "The user " + userID + " is not the one who appointed the manager");
                        }
                        loggerController.WriteErrorMsg("User " + userID + " try to remove " +managerToRemove+" from be the manager of store " + storeID + " and failed. "+ managerToRemove+" is not manages the store");
                        return new NewResponse(true, "The user "+managerToRemove+" is not manages the store, so he can not be removed from Manages the store.");
                    }
                loggerController.WriteErrorMsg("User " + userID + " try to remove "+managerToRemove+" from be the manager of store "+storeID + " and failed. "+ userID+" is not the owner of the store");
                return new NewResponse(true, "The user "+userID+" is not the owner of the store, so he can not removed manager from store");
            }
            loggerController.WriteErrorMsg("User " + userID + " try to remove "+managerToRemove+" from be the manager of store "+storeID + " and failed. "+ managerToRemove+" is not subscriber");
            return new NewResponse(true, "The user "+managerToRemove+" is not subscriber, so he can not be removed from manages store");
        }
        loggerController.WriteErrorMsg("User " + userID + " try to remove "+managerToRemove+" from be the manager of store "+storeID + " and failed. "+ userID+" is not not subscriber");
        return new NewResponse(true, "The user "+userID+" is not subscriber, so he can not removed manager from store");
    }

    public Double calculateBugPrice(Integer productID, Integer storeID, Integer quantity, ConcurrentHashMap<Integer, Integer> productsInTheBug) {
        return this.stores.get(storeID).calculateBugPrice(productID,quantity,productsInTheBug);
    }

    public boolean productIsLock(int productID, int storeID) {
        return this.stores.get(storeID).productIsLock(productID);
    }
    public void lockProduct(int storeID, int productID) {
        this.stores.get(storeID).lockProduct(productID);
    }
    public Lock getProductLock(int storeID, int productID) {
        return this.stores.get(storeID).getProductLock(productID);
    }

    public String getStoreName(int storeID) {
        return this.stores.get(storeID).getName();
    }

    public String getProductName(int storeID, int productID) {
        return this.stores.get(storeID).getProductName(productID);
    }

    public void unLockProducts(Collection<Integer> values, int storeID) {
        this.stores.get(storeID).unLockProducts(values);
    }

    public Product getProduct(int storeID, int productID) {
        return this.stores.get(storeID).getProduct(productID);
    }



    public boolean ValidConnectedUser(int userID, String connID){
        return connectedSubscribers.containsKey(connID) && connectedSubscribers.get(connID).equals(userID);
    }

    public NewResponse reduseProducts(ConcurrentHashMap<Integer, Integer> products, int storeID) {
       return this.stores.get(storeID).reduceProducts(products);
    }

    //show the history for some user
    public NewResponse ShowSubscriberHistory(int userID, String connID){
        if (ValidConnectedUser(userID,connID)){
            List<DummyShoppingHistory> list = subscribers.get(userID).ShowUserHistory();
            NewResponse res = new NewResponse("num of history buying of the user is " + list.size());
            res.AddPair("history", list);
            return res;
        }
        else{
            return new NewResponse(true, "Error in User details");
        }
    }

    //show the history for some store
    public List<DummyShoppingHistory> ShowStoreHistory(int storeId){
        return stores.get(storeId).ShowStoreHistory();
    }


    public NewResponse WriteComment(int userId, String connID, int storeId, int productId, String comment) {
        if (ValidConnectedUser(userId, connID)) {
            loggerController.WriteLogMsg("User "+userId+" add new comment to store "+ storeId+" successfully");
            return this.stores.get(storeId).WriteComment(userId, productId, comment);
        }
        else
            return new NewResponse(true, "Error in User details");
    }





    public NewResponse EditProduct(int userID, String connID, int storeID, int productID, String productName, String category, double price) {
        if(ValidConnectedUser(userID, connID)){
            if(hasPermission(userID,storeID, User.Permission.AddProduct)) {
                if(price>=0) {
                    stores.get(storeID).editProductDetails(userID, productID, productName, price, category);
                    printProducts();
                    loggerController.WriteLogMsg("User " + userID + " edit product " + productID + " successfully");
                    return new NewResponse( "Edit Product was successful");
                }
                loggerController.WriteErrorMsg("User "+userID+" try to edit product "+ productID+" and failed");
                return new NewResponse(true, "The product price can't be negative");

            }
            loggerController.WriteErrorMsg("User "+userID+" try to edit product "+ productID+" and failed");
            return new NewResponse(true, "The Edit is not allowed to Edit products");
        }
        else{
            loggerController.WriteErrorMsg("User "+userID+" try to edit product "+ productID+" and failed");
            return new NewResponse(true, "Error in User details");
        }
    }

    public NewResponse StoreHistory(int userID, int storeID, String connID){
        if (ValidConnectedUser(userID, connID)) {
            if (hasPermission(userID, storeID, User.Permission.GetHistoryPurchasing)) {
                List<DummyShoppingHistory> list = stores.get(storeID).ShowStoreHistory();
                NewResponse res = new NewResponse("num of history buying in the store is " + list.size());
                res.AddPair("history", list);
                return res;
            }
        }
        return new NewResponse("Error in details" );//todo make response inside?
    }


    public void printCommentForProduct(int storeID, int productID) {
       System.out.println("-----------------------------------------------");
       List<String> comments= this.stores.get(storeID).getCommentsForProduct(productID);
       if(comments!=null){
           System.out.println(ANSI_YELLOW+"The comments for products "+productID+" in store "+storeID+" is:"+ANSI_YELLOW);
           for (String s:comments) {
                 System.out.println(ANSI_YELLOW+s+ANSI_YELLOW);
           }
       }
       else {
           System.out.println(ANSI_YELLOW+"There is no comments for this product " + productID+ANSI_YELLOW);
       }
       System.out.println("-----------------------------------------------");
    }

    public void PayToTheSellers(Double finalPrice, Integer storeID) {
        this.stores.get(storeID).pay(finalPrice);
    }
}
