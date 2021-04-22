package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingCart;
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
            tradingSystem.ClearSystem();
//            tradingSystem.Initialization();
        }
        return tradingSystem;
    }

    public void ClearSystem() {
        System.out.println("/////////////////////////////////");
        User.ClearSystem();
        Store.ClearSystem();
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
        printUsers();
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
                System.out.println(ANSI_WHITE + p + " with quantity of: "+ quantity + ":\n" + ANSI_WHITE);
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
    public Response ConnectSystem() {

        User newGuest = new User();
        String connID = connectGuestToSystemConnID(newGuest);
        Response res = new Response("Connect system was successful");
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

    /**
     * @requirement 2.2
     * @param connID
     * @return Response{
     *      *  "isErr: boolean
     *      *  "message": String
     *      *  "connID": String
     *      * }
     */
    public Response Exit(String connID) {
        if(!connectedSubscribers.containsKey(connID) && !guests.containsKey(connID))
            return new Response(true, "User not connect to system");
        else if (connectedSubscribers.containsKey(connID)) {
            connectedSubscribers.remove(connID);
            return new Response("Exit System was successful");
        } else  {
            guests.remove(connID);
            return new Response("Exit System was successful");
        }
    }

    /**
     * @param connID
     * @param userName
     * @param password
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "userID": int
     * }
     */
    public Response Register(String connID, String userName, String password) {
        if (!guests.containsKey(connID) && !connectedSubscribers.containsKey(connID)) {
            return new Response(true, "Error in connID");
        }
        else{    
            if (validation.IsUserNameExist(userName)) {
                loggerController.WriteErrorMsg("User "+userName+" try to register to the system and failed");
                return new Response(true, errMsgGenerator("Server", "TradingSystem", "62", "Error user name is taken"));
            }
            if(!validation.VerifyPassword(password)){
                return new Response(true, errMsgGenerator("Server", "TradingSystem", "62", "Error password is invalid"));
            }
            User newUser = new User(userName, password);
            subscribers.put(newUser.getId(), newUser);
            loggerController.WriteErrorMsg("User "+userName+" register to the system successfully");

            Response res = new Response("Registration was successful");
            res.AddConnID(connID);
            res.AddUserID(newUser.getId());

            return res;
        }
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


    /**
     * @param guestConnID
     * @param userName
     * @param password
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "userID": int
     * }
     */    
    public Response Login(String guestConnID, String userName, String password) {
        Response response = validation.ValidPassword(userName, password);
        if (response.getIsErr())
            return response;
        User myGuest = guests.get(guestConnID);
        subscribers.get(response.returnUserID()).mergeToMyCart(myGuest.getShoppingCart());
        String connID = connectSubscriberToSystemConnID(response.returnUserID());
        guests.remove(guestConnID);
        Response res = new Response("Login was successful");
        res.AddUserID(response.returnUserID());
        res.AddConnID(connID);
        return res;
    }


    /**
     * @param connID
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "userID": int
     * }
     */
    public Response Logout(String connID) {
        if (connectedSubscribers.containsKey(connID)) {
            User myUser = subscribers.get(connectedSubscribers.get(connID));
            connectedSubscribers.remove(connID);
            User newGuest = new User();
            newGuest.setShoppingCart(new ShoppingCart( myUser.getShoppingCart()));
            String guestConnID = connectGuestToSystemConnID(newGuest);
            Response res = new Response("Logout was successful");
            res.AddConnID(guestConnID);
            return res;
        } else {
            return new Response(true, "User not login");
        }
    }

    /**
     * @requirement 3.2
     *
     * @param userID: int (Path)
     * @param connID: String (Header)
     * @param storeName: String (Body)
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    public Response AddStore(int userID, String connID, String storeName){
        if(!ValidConnectedUser(userID,connID)){
            return new Response(true, "Error in User details");
        }
        else{
            if (validation.IsStoreNameExist(storeName)){
                loggerController.WriteErrorMsg("User "+userID+" try to add store to the system and failed");
                return new Response(true, "Error Store name is taken");
            }
            else {
                Store newStore = new Store(storeName, userID);
                User user = subscribers.get(userID);
                user.AddStore(newStore.getId());
                stores.put(newStore.getId(),newStore);
                loggerController.WriteErrorMsg("User "+userID+" add store to the system "+ storeName+" successfully");
                return new Response( "Add Store was successful");
            }
        }
    }

    /**
     * @requirement 2.5
     *
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "stores": [{
     *      "storeID": int
     *      "storeName": String
     *  }]
     * }
     */    
    public Response ShowAllStores() {
        List<DummyStore> list = new ArrayList<>();
        for (Map.Entry<Integer, Store> currStore : stores.entrySet()) {
            list.add(new DummyStore(currStore.getValue()));
        }
        if(list.isEmpty()){
            new Response(true,"There are no stores in the system");
        }
        Response res = new Response("num of stores in the system is " + list.size());
        res.AddPair("stores", list);        
        return res;
    }


    /**
     * @param userID
     * @param connID
     * @param storeID
     * @param productName
     * @param category
     * @param price
     * @param quantity
     * @return Response{
     *      *  "isErr: boolean
     *      *  "message": String
     *      *  "connID": String
     *      * }
     */
    public Response AddProductToStore(int userID, String connID, int storeID, String productName, String category, double price, int quantity){
        if(ValidConnectedUser(userID, connID)){
            if(!this.hasPermission(userID,storeID,User.Permission.AddProduct)){
                return new Response(true, "The User is not allowed to add a product");
            }
            else {
                if(price<0) {
                    return new Response(true, "The price of the product can't be negative");
                }
                else{
                    if(quantity<0) {
                        return new Response(true, "The quantity of the product can't be negative");

                    }
                    else{
                        Response res = stores.get(storeID).AddProductToStore(productName, price, category, quantity);
                        printProducts();
                        loggerController.WriteLogMsg("User " + userID + " add product " + productName + " to store " + storeID + " successfully");
                        return res;
                    }
                }
            }
        }
        else{
            return new Response(true, "Error in User details");
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


    /**
     * @param userID
     * @param connID
     * @param storeID
     * @param productId
     * @param quantity
     *  @return Response{
     *        "isErr: boolean
     *        "message": String
     *        "connID": String
     *       }
     */
    public Response ChangeQuantityProduct(int userID, String connID, int storeID, int productId, int quantity){
        if(ValidConnectedUser(userID, connID)){
            if(!hasPermission(userID,storeID,User.Permission.AddProduct)){
                return new Response(true, "The User is not allowed to add products to the inventory");
            }
            else {
                if(quantity<0){
                    return new Response(true, "The quantity of the product can't be negative");
                }
                else {
                    Response res = stores.get(storeID).addProductToInventory(productId, quantity);
                    printProducts();
                    return res;
                }
            }
        }
        else{
            return new Response(true, "Error in User details");
        }
    }

    /**
     * @requirement 4.1
     *
     * @param userID : int (Path)
     * @param storeID: int (Path)
     * @param productID: int (Path)
     * @param connID: String (Header)
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "connID": String
     * }
     */
    public Response RemoveProduct(int userID, int storeID, int productID, String connID) {
        if(ValidConnectedUser(userID, connID)){
            if(!hasPermission(userID,storeID,User.Permission.DeleteProduct)){
                return new Response(true, "The User is not allowed to remove products from the inventory");
            }
            else {
                Response res = stores.get(storeID).deleteProduct(productID);
                printProducts();
                return res;
            }
        }
        else{
            return new Response(true, "Error in User details");
        }
    }
    public Response ShowStoreProducts(int storeID) {
        if(stores.containsKey(storeID)){
            List<DummyProduct> list = stores.get(storeID).ShowStoreProducts();
            Response res = new Response("num of products in the store is " + list.size());
            res.AddPair("products", list);
            return res;
        }
        else
            return new Response(true, "store not found");
    }

    //Shopping Cart functions
    public Response AddProductToCart(String connID, int StoreId, int productId, int quantity){
            if(guests.containsKey(connID)){
                User myGuest= guests.get(connID);
                return myGuest.AddProductToCart(StoreId,productId,quantity);
            }
            else if(connectedSubscribers.containsKey(connID)){
                int userID= connectedSubscribers.get(connID);
                return subscribers.get(userID).AddProductToCart(StoreId,productId,quantity);
            }
            else {
                return new Response(true, "User not connect to system");
            }
    }
    public Response ShowShoppingCart(String connID){
        if(guests.containsKey(connID)) {
            List<DummyProduct> list = guests.get(connID).ShowShoppingCart();
            Response res = new Response("num of products in my Shopping Cart is " + list.size());
            res.AddPair("products", list);
            return res;
        }
        else if(connectedSubscribers.containsKey(connID)) {
            int userID = connectedSubscribers.get(connID);
            List<DummyProduct> list = subscribers.get(userID).ShowShoppingCart();
            Response res = new Response("num of products in my Shopping Cart is " + list.size());
            res.AddPair("products", list);
            return res;
        }
        else {
            return new Response(true, "user not Exist");
        }
    }

    public Response guestPurchase(String connID, String name, String credit_number, String phone_number, String address){
        if(!guests.containsKey(connID)){
            return new Response(true, "User not connect to system");
        }
        else {
            User myGuest= guests.get(connID);
            return myGuest.guestPurchase(name, credit_number, phone_number, address);
        }
    }
    public Response subscriberPurchase(int userID, String connID, String credit_number, String phone_number, String address){
        if(!ValidConnectedUser(userID, connID)){
            return new Response(true, "User not connect to system");
        }
        else {
            User user = subscribers.get(userID);
            return user.subscriberPurchase(credit_number, phone_number, address);

        }
    }

    //History Cart functions
    public void addHistoryToStoreAndUser(ShoppingHistory sh, boolean isGuest) {
        this.stores.get(sh.getStoreID()).addHistory(sh);
        if (!isGuest)
            this.subscribers.get(sh.getUserID()).addHistory(sh);
    }

    /**
     * @requirement 2.6
     *
     * @param name
     * @param category
     * @param minprice
     * @param maxprice
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "products": List [{
     *      "storeID": int
     *      "storeName": String
     *      "productID": int
     *      "productName": String
     *      "price": double
     *      "category": String
     *      "quantity": int
     *  }]
     *
     */
    //TODO: to check
    public Response SearchProduct(String name, String category, int minprice, int maxprice){
        List<DummyProduct> dummyProducts = new ArrayList<>();
        for(Store store: stores.values()){
           // if(((prank==-1 || store.getRate()>=srank) && !store.SearchByName(name, minprice, maxprice,prank).isEmpty())){
                dummyProducts.addAll(store.SearchProduct(name,category, minprice, maxprice));
            }
        Response res = new Response("num of products from search is " + dummyProducts.size());
        res.AddPair("products", dummyProducts);
        return res;
    }

    //TODO: to move?
    public List<DummyProduct> SearchProductByName(String name, int minprice, int maxprice, int prank , int srank){
        List<DummyProduct> dummyProducts = new ArrayList<>();
        for(Store store: stores.values()){
            if(((prank==-1 || store.getRate()>=srank) && !store.SearchByName(name, minprice, maxprice,prank).isEmpty())){
                dummyProducts.addAll(store.SearchByName(name, minprice, maxprice,prank));
            }
        }
        return dummyProducts;
    }
    //TODO: to move?
    public List<DummyProduct> SearchProductByCategory(String category, int minprice, int maxprice, int prank , int srank){
        List<DummyProduct> dummyProducts = new ArrayList<>();
        for(Store store: stores.values()){
            if(!store.SearchByCategory(category, minprice, maxprice,prank).isEmpty()){
                dummyProducts.addAll(store.SearchByCategory(category, minprice, maxprice,prank));
            }
        }
        return dummyProducts;
    }

    /**
     *
     * @requirement 4.3
     *
     * @param userID
     * @param connID
     * @param storeID
     * @param newOwner
     *
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     * }
     */
    public Response AddNewOwner(int userID, String connID, int storeID, int newOwner)
    {
        if (!ValidConnectedUser(userID, connID)) {
            loggerController.WriteErrorMsg("User " + userID + " try to Add owner to store " + storeID + " and failed. The err message: Error in User details");
            return new Response(true, "Error in User details");
        }
        if (this.subscribers.get(newOwner) == null) {
            loggerController.WriteErrorMsg("User " + userID + " try to Add " + newOwner + " to be the owner of store " + storeID + " and failed. " + newOwner + " is not subscriber");
            return new Response(true, "The user " + newOwner + " is not subscriber, so he can not be owner for store");
        }
        while (!this.subscribers.get(newOwner).tryToLock())
        {
            try{
                this.wait(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.subscribers.get(newOwner).lockUser();
        
        Response res1 = this.systemRoleChecks(userID, storeID, newOwner, User.Permission.AppointmentOwner);
        if (res1.getIsErr()) {
            this.subscribers.get(newOwner).unlockUser();
            return res1;
        }
        User NU = this.subscribers.get(newOwner);
        Response res2 =NU.AbleToAddOwner(userID, storeID);
        if (res2.getIsErr()) {
            this.subscribers.get(newOwner).unlockUser();
            return res2;
        }

        OwnerPermission OP = new OwnerPermission(newOwner, storeID);
        OP.setAppointmentId(userID);
        NU.AddStoreInOwner(storeID, OP);
        stores.get(storeID).addNewOwner(userID, newOwner);
        stores.get(storeID).addOwnerPermission(newOwner,OP);
        this.subscribers.get(newOwner).unlockUser();
        loggerController.WriteLogMsg("User " + userID + " add owner " + newOwner + " to store " + storeID + " successfully");
        return new Response("The owner Added successfully");
    }

    private Response systemRoleChecks(int userID, int storeID, int newRole, User.Permission permission)
    {
        if (!this.subscribers.containsKey(userID)) {
            loggerController.WriteErrorMsg("User " + userID + " try to "+permission.toString()+" "+newRole+"  of store "+storeID + " and failed. "+ userID+" is not not subscriber");
            return new Response(true, "The user "+userID+" is not subscriber, so he can not appoint manager for store");
        }
        if (!this.subscribers.containsKey(newRole)) {
            loggerController.WriteErrorMsg("User " + userID + " try to "+permission.toString() +" the user "+newRole +" to store "+storeID + " and failed. "+ newRole+" is not subscriber");
            return new Response(true, "User "+newRole+" is not subscriber, so it impossible to "+permission.toString()+" him for store");
        }
        if (!this.subscribers.get(userID).getMyOwnerStore().contains(storeID)){
            loggerController.WriteErrorMsg("User " + userID + " try to "+permission.toString() +" the user "+newRole +" to store "+storeID + " and failed. "+ userID+" is not the owner of the store");
            return new Response(true, "User "+userID+" is not the owner of the store, so he can not "+permission.toString()+" to the store");
        }
        if(!this.hasPermission(userID,storeID,permission)) {
            loggerController.WriteErrorMsg("User " + userID + " try to "+permission.toString() +" the user "+newRole +" to store "+storeID + " and failed. " + userID + " is not allowed to do that");
            return new Response(true, "User " + userID + " is not allowed to "+permission.toString());
        }
        return new Response(false,"Sys OK");
    }


    /**
     /**
     * @requirement 4.5
     *
     * @param userID
     * @param connID
     * @param storeID
     * @param newManager
     *
     * @return Response{
     *  "isErr: boolean
     *  "message": String
     * }
     *
     */
    public Response AddNewManager(int userID, String connID, int storeID, int newManager)
    {
        if (!ValidConnectedUser(userID, connID)) {
            loggerController.WriteErrorMsg("User " + userID + " try to add manager to store " + storeID + " and failed. The err message: Error in User details");
            return new Response(true, "Error in User details");
        }
        if (this.subscribers.get(newManager) == null) {
            loggerController.WriteErrorMsg("User " + userID + " try to Add "+newManager+" to be the manager of store "+storeID + " and failed. "+ newManager+" is not subscriber");
            return new Response(true, "The user "+newManager+" is not subscriber, so he can not be manager for store");
        }

        /*while (!this.subscribers.get(newManager).tryToLock()) {
            try{
                this.wait(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/

        /*
        System.out.println("TEST1!--------------------");
        boolean succeededToLock = false;
        while (!succeededToLock) {
            synchronized (this) {
            System.out.println("TEST2!--------------------");
                succeededToLock = NM.tryToLock();
            }
        }

         */
        User NM = this.subscribers.get(newManager);
        Response res1 = this.systemRoleChecks(userID, storeID, newManager, User.Permission.AppointmentManager);
        if (res1.getIsErr()) {
            //NM.unlockUser();
            return res1;
        }

        Response res2 = NM.AbleToAddManager(userID, storeID, newManager);
        if (res2.getIsErr()) {
            //NM.unlockUser();
            return res2;
        }

        ManagerPermission MP= new ManagerPermission(newManager, storeID);
        MP.setAppointmentId(userID);
        NM.AddStoreInManager(storeID, MP);
        stores.get(storeID).addNewManager(userID, newManager);
        stores.get(storeID).addManagerPermission(MP);
        //NM.unlockUser();
        loggerController.WriteLogMsg("User " + userID + " add manager " + newManager + " to store " + storeID + " successfully");
        return new Response( "The manager Added successfully");
    }


    /**
     * @requirement 4.7
     *
     * @param userID
     * @param connID
     * @param storeID
     * @param ManagerToRemove
     * @return Response{
      *  "isErr: boolean
      *  "message": String
      * }
     */
    public Response RemoveManager(int userID, String connID, int storeID, int ManagerToRemove)  {
        if (!ValidConnectedUser(userID, connID)) {
            loggerController.WriteErrorMsg("User " + userID + " try to remove manager from store " + storeID + " and failed. The err message: Error in User details");
            return new Response(true, "Error in User details");
        }
        if (this.subscribers.get(ManagerToRemove) == null) {
            loggerController.WriteErrorMsg("User " + userID + " try to Remove " + ManagerToRemove + " from management the store " + storeID + " and failed. " + ManagerToRemove + " is not subscriber");
            return new Response(true, "The user " + ManagerToRemove + " is not subscriber, so it impossible to remove him from management the store");
        }
        /*
        while (!this.subscribers.get(ManagerToRemove).tryToLock()) {
            try{
                this.wait(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
         */
        //this.subscribers.get(ManagerToRemove).lockUser();
        User MTR = this.subscribers.get(ManagerToRemove);
        Response res1 = this.systemRoleChecks(userID, storeID, ManagerToRemove, User.Permission.RemoveManager);
        if (res1.getIsErr()) {
            //MTR.unlockUser();
            return res1;
        }
        Response res2 =MTR.AbleToRemoveManager(userID, storeID);
        if (res2.getIsErr()) {
            //MTR.unlockUser();
            return res2;
        }
        MTR.removeStore(storeID);
        stores.get(storeID).removeManager(userID, ManagerToRemove);
        //MTR.unlockUser();
        loggerController.WriteLogMsg("User " + userID + " remove manager " + ManagerToRemove + " from store " + storeID + " successfully");
        return new Response("The manager removed successfully");
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

    public Response reduceProducts(ConcurrentHashMap<Integer, Integer> products, int storeID) {
       return this.stores.get(storeID).reduceProducts(products);
    }

    /**
     * @requirement 3.7
     *
     * @param userID: int (Path)
     * @param connID: String (Header)
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "history": List [{
     *      "userID": int
     *      "storeID": int
     *      "products": List [{
     *          "storeID": int
     *          "storeName": String
     *          "productID": int
     *          "productName": String
     *          "price": double
     *          "category": String
     *          "quantity": int
     *      }]
     *  }]
     * }
     */
    public Response ShowSubscriberHistory(int userID, String connID){
        if (ValidConnectedUser(userID,connID)){
            List<DummyShoppingHistory> list = subscribers.get(userID).ShowUserHistory();
            if(list.isEmpty()){
                return new Response(true,"There are no older shopping in the history");
            }
            Response res = new Response("num of history buying of the user is " + list.size());
            res.AddPair("history", list);
            return res;
        }
        else{
            return new Response(true, "Error in User details");
        }
    }

    //show the history for some store
    public List<DummyShoppingHistory> ShowStoreHistory(int storeId){
        return stores.get(storeId).ShowStoreHistory();
    }



    /**
     * @param userId
     * @param connID
     * @param storeId
     * @param productId
     * @param comment
     * @return Response{
     *      *  "isErr: boolean
     *      *  "message": String
     *      *  "connID": String
     *      * }
     */
    public Response WriteComment(int userId, String connID, int storeId, int productId, String comment) {
        if(!stores.containsKey(storeId)){
            return new Response(true, "Store doesn't exist in the system");
        }
        else if(stores.containsKey(storeId)){
            Store store=stores.get(storeId);
            if(!store.isProductExist(storeId)){
                return new Response(true, "The product doesn't exist in the store anymore");
            }
        }
        else if(!ValidConnectedUser(userId, connID)) {
            return new Response(true, "Error in User details");
        }
        User user=subscribers.get(userId);
        if(!user.IsProductExist(productId)){
            return new Response(true, "User didn't buy this product");
        }
        if(stores.get(storeId).getProduct(productId).isUserComment(userId)){
            return new Response(true, "The user already wrote comment for this product");
        }
        return new Response(false, "the comment added successfully");
    }



    /**
     * @param userID
     * @param connID
     * @param storeID
     * @param productID
     * @param productName
     * @param category
     * @param price
     * @param quantity
     * @return Response{
     *        "isErr: boolean
     *        "message": String
     *        "connID": String
     *       }
     */
    public Response EditProduct(int userID, String connID, int storeID, int productID, String productName, String category, double price, int quantity) {
        System.out.println("TEST------------->");
        if(ValidConnectedUser(userID, connID)){
            if(!hasPermission(userID,storeID, User.Permission.AddProduct)){
                return new Response(true, "The Edit is not allowed to Edit products");
            }
            else {
                if(price<0){
                    return new Response(true, "The product price can't be negative");
                }
                else {
                    if(quantity<0){
                        return new Response(true, "The product quantity can't be negative");
                    }
                    else{
                        //todo change quantity of product
                        Response res = stores.get(storeID).editProductDetails(userID, productID, productName, price, category, quantity);
                        printProducts();
                        loggerController.WriteLogMsg("User " + userID + " edit product " + productID + " successfully");
                        return res;
                    }
                }
            }
        }
        else{
            loggerController.WriteErrorMsg("User "+userID+" try to edit product "+ productID+" and failed");
            return new Response(true, "Error in User details");
        }
    }

    public Response StoreHistory(int userID, int storeID, String connID){
        if (ValidConnectedUser(userID, connID)) {
            if (hasPermission(userID, storeID, User.Permission.GetHistoryPurchasing)) {
                List<DummyShoppingHistory> list = stores.get(storeID).ShowStoreHistory();
                Response res = new Response("num of history buying in the store is " + list.size());
                res.AddPair("history", list);
                return res;
            }
            return new Response("user has no permission to watch the history" );
        }
        return new Response("Not connected user" );//todo make response inside?
    }

    public Response StoreHistoryOwner(int userID, int storeID, String connID){
        if (ValidConnectedUser(userID, connID)) {
            if (hasPermission(userID, storeID, User.Permission.GetStoreHistory)) {
                List<DummyShoppingHistory> list = stores.get(storeID).ShowStoreHistory();
                Response res = new Response("num of history buying in the store is " + list.size());
                res.AddPair("history", list);
                return res;
            }
            return new Response("user has no permission to watch the history" );
        }
        return new Response("Not connected user" );
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

    public Response editProductQuantityFromCart(String connID, int storeID, int productID, int quantity) {
        if(guests.containsKey(connID)){
            User myGuest= guests.get(connID);
            return myGuest.editProductQuantityFromCart(storeID, productID, quantity);
        }
        else if(connectedSubscribers.containsKey(connID)){
            int userID= connectedSubscribers.get(connID);
            return subscribers.get(userID).editProductQuantityFromCart(storeID, productID, quantity);
        }
        else {
            return new Response(true, "User not connect to system");
        }
    }
    public Response RemoveProductFromCart(String connID, int storeID, int productID) {
        if(guests.containsKey(connID)) {
            Response res =guests.get(connID).RemoveProductFromCart(storeID,productID);
            return res;
        }
        else if(connectedSubscribers.containsKey(connID)) {
            int userID = connectedSubscribers.get(connID);
            Response res = subscribers.get(userID).RemoveProductFromCart(storeID,productID);
            return res;
        }
        else {
            return new Response(true, "user not Exist");
        }
    }

    public Double calculateBugPrice(int userID, int storeID, ConcurrentHashMap<Integer, Integer> productsInTheBug) {
       if(this.subscribers.contains(userID))
       return this.stores.get(storeID).calculateBugPrice(true,productsInTheBug);
       else
       return this.stores.get(storeID).calculateBugPrice(false,productsInTheBug);
    }

    public Response ShowAllUsers(int adminID, String connID) {
        if(!ValidConnectedUser(adminID, connID)){
            loggerController.WriteErrorMsg("User "+adminID+" try see details of all users and failed");
            return new Response(true, "Error in User details");
        }
        else if (!systemAdmins.containsKey(adminID)){
            loggerController.WriteErrorMsg("User "+adminID+" try see details of all users and failed not admin");
            return new Response(true, "Error User not admin");
        }
        else {
            List<DummyUser> list = new ArrayList<>();
            for (Map.Entry<Integer, User> currUser : subscribers.entrySet()) {
                list.add(new DummyUser(currUser.getValue()));
            }
            Response res = new Response("num of users in the system is " + list.size());
            res.AddPair("users", list);
            return res;
        }
    }

    public Response ShowUserHistory(int adminID, int userID, String connID) {
        if(!ValidConnectedUser(adminID, connID)){
            loggerController.WriteErrorMsg("User "+adminID+" try see details of all users and failed");
            return new Response(true, "Error in User details");
        }
        else if (!systemAdmins.containsKey(adminID)){
            loggerController.WriteErrorMsg("User "+adminID+" try see details of all users and failed not admin");
            return new Response(true, "Error User not admin");
        }
        else if (!subscribers.containsKey(userID)){
            loggerController.WriteErrorMsg("User "+adminID+" try see history details of user"+userID+" and failed");
            return new Response(true, "Error in User details");
        }
        else {
            List<DummyShoppingHistory> list = subscribers.get(userID).ShowUserHistory();
            if(list.isEmpty()){
                return new Response(true,"There are no older shopping in the history");
            }
            Response res = new Response("num of history buying of the user is " + list.size());
            res.AddPair("history", list);
            return res;
        }
    }
      
    public Response ShowOwnerStores(int userID, String connID)
    {
        if(!ValidConnectedUser(userID, connID))
        {
            loggerController.WriteErrorMsg("User "+userID+" try see details of all users and failed");
            return new Response(true, "User is nor logged in");
        }
        else if (!subscribers.containsKey(userID)){
            loggerController.WriteErrorMsg("User "+userID+" try see details of all users and failed not admin");
            return new Response(true, "User is not subscriber");
        }
        else{
            User user = subscribers.get(userID);
            List<Integer> store = user.getMyOwnerStore();
            ConcurrentHashMap<Integer, Store> storeObjects = new ConcurrentHashMap<>();
            List<DummyStore> list = new ArrayList<>();
            for(int i=0; i<store.size(); i++) {
                for (Map.Entry<Integer, Store> currStore : stores.entrySet()) {
                    if (currStore.getValue().getId() == store.get(i))
                        list.add(new DummyStore(currStore.getValue()));
                }
            }
            Response res = new Response("num of owned stores of the user is " + list.size());
            res.AddPair("stores", list);
            return res;
        }
    }

    public Response ShowManagerStores(int userID, String connID)
    {
        if(!ValidConnectedUser(userID, connID))
        {
            loggerController.WriteErrorMsg("User "+userID+" try see details of all users and failed");
            return new Response(true, "User is nor logged in");
        }
        else if (!subscribers.containsKey(userID)){
            loggerController.WriteErrorMsg("User "+userID+" try see details of all users and failed not admin");
            return new Response(true, "User is not subscriber");
        }
        else{
            User user = subscribers.get(userID);
            List<Integer> store = user.getMyManagerStore();
            ConcurrentHashMap<Integer, Store> storeObjects = new ConcurrentHashMap<>();
            List<DummyStore> list = new ArrayList<>();
            for(int i=0; i<store.size(); i++) {
                for (Map.Entry<Integer, Store> currStore : stores.entrySet()) {
                    if (currStore.getValue().getId() == store.get(i))
                        list.add(new DummyStore(currStore.getValue()));
                }
            }
            Response res = new Response("num of managed stores of the user is " + list.size());
            res.AddPair("stores", list);
            return res;
        }
    }
}
