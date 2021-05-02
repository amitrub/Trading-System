package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingBag;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingCart;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.UserComponent.*;
import TradingSystem.Server.ServiceLayer.DummyObject.*;
import TradingSystem.Server.ServiceLayer.LoggerController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

import static TradingSystem.Server.ServiceLayer.Configuration.*;


public class TradingSystemImpl extends Observable implements TradingSystem {

    public Validation validation;

    private ConcurrentHashMap<Integer, Integer> systemAdmins;
    private ConcurrentHashMap<String, Integer> connectedSubscribers;

    public ConcurrentHashMap<Integer, User> subscribers;
    public ConcurrentHashMap<String, User> guests;
    public ConcurrentHashMap<Integer, Store> stores;
    //storeID_systemManagerPermission
    private ConcurrentHashMap<Integer, SystemManagerPermission> systemManagerPermissions;

    //    Singleton
    private static TradingSystemImpl tradingSystemImpl = null;
    private static final LoggerController loggerController=LoggerController.getInstance();
    private List<Observer> observers = new ArrayList<>();

    private TradingSystemImpl() {
        this.connectedSubscribers = new ConcurrentHashMap<>();
        this.subscribers = new ConcurrentHashMap<>();
        this.guests = new ConcurrentHashMap<>();
        this.stores = new ConcurrentHashMap<>();
        this.systemAdmins = new ConcurrentHashMap<>();
        this.systemManagerPermissions=new ConcurrentHashMap<>();
    }

    public static TradingSystemImpl getInstance() {
        if (tradingSystemImpl == null) {
            tradingSystemImpl = new TradingSystemImpl();
            tradingSystemImpl.validation = new Validation();
            tradingSystemImpl.ClearSystem();
//            tradingSystemImpl.Initialization();
        }
        return tradingSystemImpl;
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

        ConnectSystem();
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
    //    guests.put(connID,newGuest);
        Response res = new Response("Connect system was successful");
        res.AddConnID(connID);
        return res;
    }

    public synchronized String connectGuestToSystemConnID(User newGuest) {
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
    public synchronized String connectSubscriberToSystemConnID(Integer userID) {
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

    public boolean hasPermission(int userID, int storeID, User.Permission p) {
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

    public boolean hasPermission(int userID, User.Permission p) {
        if (this.subscribers.containsKey(userID)) {
            User u = this.subscribers.get(userID);
            if (this.systemManagerPermissions.get(userID) != null) {
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
            observers = new ArrayList<>();
            for (ShoppingBag s:myGuest.getShoppingCart().getShoppingBags().values())
            {
                Integer storeID = s.getStoreID();
                //stores.get(storeID).getOwnersIDs().keys();
                for (User u:subscribers.values()) {
                    for (Integer ownedStore:u.getMyOwnerStore()) {
                        if(ownedStore == storeID)
                            this.addObserver(u);
                    }
                }
            }
            Response res = myGuest.guestPurchase(name, credit_number, phone_number, address);
            if(!res.getIsErr())
            {
                this.notifyObservers("A product has been purchased from your store");
            }
            return res;
        }
    }
    public Response subscriberPurchase(int userID, String connID, String credit_number, String phone_number, String address){
        if(!ValidConnectedUser(userID, connID)){
            return new Response(true, "User not connect to system");
        }
        else {
            User user = subscribers.get(userID);
            observers = new ArrayList<>();
            for (ShoppingBag s:user.getShoppingCart().getShoppingBags().values())
            {
                Integer storeID = s.getStoreID();
                for (User u:subscribers.values()) {
                    for (Integer ownedStore:u.getMyOwnerStore()) {
                        if(ownedStore == storeID)
                            this.addObserver(u);
                    }
                }
            }
            Response res = user.subscriberPurchase(credit_number, phone_number, address);
            if(!res.getIsErr())
            {
                this.notifyObservers("A product has been purchased from your store!");
            }
            return res;

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

        User NO = this.subscribers.get(newOwner);
//        System.out.println(ANSI_BLUE + "???? userID BEFORE lock: " + userID + ANSI_RESET);
        boolean succeededToLock = false;
        while (!succeededToLock) {
            synchronized (this) {
//                System.out.println(ANSI_BLUE + "???? userID try to lock: " + userID + ANSI_RESET);
                succeededToLock = NO.tryToLock();
            }
        }
        
        Response res1 = this.systemRoleChecks(userID, storeID, newOwner, User.Permission.AppointmentOwner);
        if (res1.getIsErr()) {
            NO.unlockUser();
            return res1;
        }
        Response res2 =NO.AbleToAddOwner(userID, storeID);
        if (res2.getIsErr()) {
            NO.unlockUser();
            return res2;
        }

        OwnerPermission OP = new OwnerPermission(newOwner, storeID);
        OP.setAppointmentId(userID);
        NO.AddStoreInOwner(storeID, OP);
        stores.get(storeID).addNewOwner(userID, newOwner);
        stores.get(storeID).addOwnerPermission(newOwner,OP);
        //this.subscribers.get(newOwner).unlockUser();
        loggerController.WriteLogMsg("User " + userID + " add owner " + newOwner + " to store " + storeID + " successfully");
        NO.unlockUser();
        return new Response("The owner Added successfully");
    }

    public Response systemRoleChecks(int userID, int storeID, int newRole, User.Permission permission)
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

        User NM = this.subscribers.get(newManager);
        System.out.println(ANSI_BLUE + "???? userID BEFORE lock: " + userID + ANSI_RESET);
        boolean succeededToLock = false;
        while (!succeededToLock) {
            synchronized (this) {
                System.out.println(ANSI_BLUE + "???? userID try to lock: " + userID + ANSI_RESET);
                succeededToLock = NM.tryToLock();
            }
        }

        Response res1 = this.systemRoleChecks(userID, storeID, newManager, User.Permission.AppointmentManager);
        if (res1.getIsErr()) {
            NM.unlockUser();
            return res1;
        }

        Response res2 = NM.AbleToAddManager(userID, storeID, newManager);
        if (res2.getIsErr()) {
            NM.unlockUser();
            return res2;
        }

        ManagerPermission MP = new ManagerPermission(newManager, storeID);
        MP.setAppointmentId(userID);
        NM.AddStoreInManager(storeID, MP);
        stores.get(storeID).addNewManager(userID, newManager);
        stores.get(storeID).addManagerPermission(MP);
        NM.unlockUser();
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

        User MTR = this.subscribers.get(ManagerToRemove);
//        System.out.println(ANSI_BLUE + "???? userID BEFORE lock: " + userID + ANSI_RESET);
        boolean succeededToLock = false;
        while (!succeededToLock) {
            synchronized (this) {
//                System.out.println(ANSI_BLUE + "???? userID try to lock: " + userID + ANSI_RESET);
                succeededToLock = MTR.tryToLock();
            }
        }

        Response res1 = this.systemRoleChecks(userID, storeID, ManagerToRemove, User.Permission.RemoveManager);
        if (res1.getIsErr()) {
            MTR.unlockUser();
            return res1;
        }
        Response res2 =MTR.AbleToRemoveManager(userID, storeID);
        if (res2.getIsErr()) {
            MTR.unlockUser();
            return res2;
        }
        MTR.removeManagedStore(storeID);
        stores.get(storeID).removeManager(ManagerToRemove);
        MTR.unlockUser();
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
                Response res = new Response(true,"There are no older shopping in the history");
                res.AddPair("history", list);
                return res;
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

    public Response StoreHistoryOwner(int userID, int storeID, String connID) {
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "Error in User details");
        }

        if (!hasPermission(userID, storeID, User.Permission.GetStoreHistory)) {
            List<DummyShoppingHistory> list = new ArrayList<>();
            Response res = new Response(true, "user has no permission to watch the history");
            res.AddPair("history", list);
            return res;
        }
        if (!stores.containsKey(storeID)) {
            List<DummyShoppingHistory> list = new ArrayList<>();
            Response res = new Response(true, "Owner - wrong store ID");
            res.AddPair("history", list);
            return res;
        }

        List<DummyShoppingHistory> list = stores.get(storeID).ShowStoreHistory();
        Response res = new Response(false,"num of history buying in the store is " + list.size());
        res.AddPair("history", list);
        return res;
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


    //Admin
    public Response StoreHistoryAdmin(int AdminID, int storeID, String connID){
        if (!ValidConnectedUser(AdminID, connID)) {
            return new Response(true, "Error in AdminID details");
        }
        if (!hasPermission(AdminID, storeID, User.Permission.GetHistoryPurchasing)) {
            List<DummyShoppingHistory> list = new ArrayList<>();
            Response res = new Response(true, "user has no permission to watch the history");
            res.AddPair("history", list);
            return res;
        }
        if (!stores.containsKey(storeID)) {
            List<DummyShoppingHistory> list = new ArrayList<>();
            Response res = new Response(true, "Admin - wrong store ID");
            res.AddPair("history", list);
            return res;
        }

        List<DummyShoppingHistory> list = stores.get(storeID).ShowStoreHistory();
        Response res = new Response(false,"num of history buying in the store is " + list.size());
        res.AddPair("history", list);
        return res;
    }

    public Response UserHistoryAdmin(int AdminID, int userID, String connID){
        if (!ValidConnectedUser(AdminID, connID)) {
            return new Response(true, "Error in Admin details");
        }
        if (!hasPermission(AdminID, userID, User.Permission.GetHistoryPurchasing)) {
            List<DummyShoppingHistory> list = new ArrayList<>();
            Response res = new Response(true, "user has no permission to watch the history");
            res.AddPair("history", list);
            return res;
        }
        if (!subscribers.containsKey(userID)) {
            List<DummyShoppingHistory> list = new ArrayList<>();
            Response res = new Response(true, "Admin - wrong user ID");
            res.AddPair("history", list);
            return res;
        }

        User user = subscribers.get(userID);
        List<DummyShoppingHistory> list = user.ShowUserHistory();
        Response res = new Response(false,"num of history buying in the store is " + list.size());
        res.AddPair("history", list);
        return res;
    }

    public Response AllStoresHistoryAdmin(int AdminID, String connID){
        if (!ValidConnectedUser(AdminID, connID)) {
            return new Response(true, "Error in Admin details");
        }
        if (!hasPermission(AdminID, User.Permission.GetHistoryPurchasing)) {
            List<DummyShoppingHistory> list = new ArrayList<>();
            Response res = new Response(true, "user has no permission to watch the history");
            res.AddPair("history", list);
            return res;
        }
        List<DummyShoppingHistory> list = new ArrayList<>();
        for(Store s: stores.values())
        {
            for(DummyShoppingHistory e : s.ShowStoreHistory()) {
                list.add(e);
            }
        }
        Response res = new Response(false,"num of history buying in the store is " + list.size());
        res.AddPair("history", list);
        return res;
    }

    public Response AllUsersHistoryAdmin(int AdminID, String connID) {
        if (!ValidConnectedUser(AdminID, connID)) {
            return new Response(true, "Error in Admin details");
        }
        if (!hasPermission(AdminID, User.Permission.GetHistoryPurchasing)) {
            List<DummyShoppingHistory> list = new ArrayList<>();
            Response res = new Response(true, "user has no permission to watch the history");
            res.AddPair("history", list);
            return res;
        }
        List<DummyShoppingHistory> list = new ArrayList<>();
        for (User u : subscribers.values()) {
            for (DummyShoppingHistory e : u.ShowUserHistory()) {
                list.add(e);
            }
        }
        Response res = new Response(false, "num of history buying in the store is " + list.size());
        res.AddPair("history", list);
        return res;
    }

    public Response EditManagerPermissions(int userID, String connID, int storeID, int managerID, List<User.Permission> permissions) {
        if (!ValidConnectedUser(userID, connID)) {
            loggerController.WriteErrorMsg("User " + userID + " try to edit "+managerID+ " permissions for store " + storeID + " and failed. The err message: Error in User details");
            return new Response(true, "Error in User details");
        }
        if (this.subscribers.get(managerID) == null) {
            loggerController.WriteErrorMsg("User " + userID + " try to edit " + managerID + " details, for store " + storeID + " and failed. " + managerID + " is not subscriber");
            return new Response(true, "The user " + managerID + " is not subscriber, so it impossible to edit his Permissions");
        }

       //TODO add synchronize
        User MTE = this.subscribers.get(managerID);
        Response res1 = this.systemRoleChecks(userID, storeID, managerID, User.Permission.EditManagerPermission);
        if (res1.getIsErr()) {
            //MTR.unlockUser();
            return res1;
        }
        Response res2 =MTE.AbleToEditPermissions(userID, storeID);
        if (res2.getIsErr()) {
            //MTR.unlockUser();
            return res2;
        }

        MTE.editPermissions(userID,storeID,permissions);
        stores.get(storeID).editManagerPermissions(userID, managerID,permissions);
        //NM.unlockUser();
        loggerController.WriteLogMsg("User " + userID + " edit permissions to " + managerID + " for store " + storeID + " successfully");
        return new Response( "The manager permissions edit successfully");
    }

    public User.Permission changeToPermission(String per){
        switch (per){
            case "AddProduct":
                return User.Permission.AddProduct;
            case "ReduceProduct":
                return User.Permission.ReduceProduct;
            case "DeleteProduct":
                return User.Permission.DeleteProduct;
            case "EditProduct":
                return User.Permission.EditProduct;
            case "AppointmentOwner":
                return User.Permission.AppointmentOwner;
            case "AppointmentManager":
                return User.Permission.AppointmentManager;
            case "EditManagerPermission":
                return User.Permission.EditManagerPermission;
            case "RemoveManager":
                return User.Permission.RemoveManager;
            case "GetInfoOfficials":
                return User.Permission.GetInfoOfficials;
            case "GetInfoRequests":
                return User.Permission.GetInfoRequests;
            case "ResponseRequests":
                return User.Permission.ResponseRequests;
            case "GetHistoryPurchasing":
                return User.Permission.GetHistoryPurchasing;
            case "GetStoreHistory":
                return User.Permission.GetStoreHistory;
        }
        return null;
    }

    public Response GetPossiblePermissionsToManager(int userID, String connID) {
        if (!ValidConnectedUser(userID, connID)) {
            loggerController.WriteErrorMsg("User " + userID + "try to get permissions for store and failed. The err message: Error in User details");
            return new Response(true, "Error in User details");
        }
        List<String> permissions = new ArrayList<>();
        OwnerPermission OP = new OwnerPermission(userID, -1);
        for (User.Permission P : OP.getPermissions()
        ) {
            permissions.add(P.toString());
        }
        Response res = new Response(false, "Viewing permissions was successful");
        res.AddPair("permissions", permissions);
        return res;
    }

    public Response ShowStoreWorkers(int userID, String connID, int storeID){
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "Error in User details");
        }
        if(!stores.containsKey(storeID)){
            return new Response(true, "the store doesn't exist");
        }
        if(!stores.get(storeID).checkOwner(userID)){
            return new Response(true, "the user has no permissions to see this information");
        }
        else{
            List<String> list=new LinkedList<>();
            ConcurrentHashMap<Integer,OwnerPermission> ownerPermissionHashMap=stores.get(storeID).getOwnersIDs();
            ConcurrentHashMap<Integer,ManagerPermission> managerPermissionHashMap= stores.get(storeID).getManagerIDs();
            if(ownerPermissionHashMap.size()==0 && managerPermissionHashMap.size()==0){
                return new Response(true,"There are no users with permmissions in the store");
            }
            HashMap<Integer,List<Permission>> union= new HashMap<>();
            for(Integer key:ownerPermissionHashMap.keySet()){
                Permission permission=ownerPermissionHashMap.get(key);
                List<Permission> lst = new LinkedList<>();
                lst.add(ownerPermissionHashMap.get(key));
                union.put(key,lst);
            }
            for(Integer key:managerPermissionHashMap.keySet()){
                List<Permission> lst = union.getOrDefault(key, new LinkedList<>());
                lst.add(managerPermissionHashMap.get(key));
                union.put(key,lst);
            }
            List<String> workers=new LinkedList<>();
            for(Integer key: union.keySet()){
                List<Permission> value= union.get(key);
                String permmision="";
                for(int i=0;i<value.size();i++){
                    permmision+= value.get(i).toString();
                }
                String res= "Id = "+key+", name ="+subscribers.get(key).getUserName()+", permissions= "+ permmision+"\n";
                workers.add(res);
            }
            Response response = new Response(false, "");
            response.AddPair("ConnId",connID);
            response.AddPair("workers", workers);
            return response;
        }
    }

    public Response RemoveOwnerByOwner(int ownerID, String connID, int removeOwnerID, int storeID) {
        if (!ValidConnectedUser(ownerID, connID)) {
            return new Response(true, "Error in User details!");
        }
        if (!stores.containsKey(storeID)) {
            return new Response(true, "the store doesn't exist");
        }
        if(!stores.get(storeID).checkOwner(ownerID)){
            return new Response(true, "the user that is not the owner of the store");
        }
        if (!stores.get(storeID).checkOwner(removeOwnerID)) {
            return new Response(true, "the user that we want to remove is not the owner of the store");
        }
        if (stores.get(storeID).getPermission(removeOwnerID).getAppointmentId()!=ownerID) {
            return new Response(true, "the user has no permissions to see this information");
        }
        else{
            stores.get(storeID).removeOwner(removeOwnerID);
            subscribers.get(removeOwnerID).removeOwnedStore(storeID);
            ConcurrentHashMap<Integer,OwnerPermission> ownerPermissionHashMap=stores.get(storeID).getOwnersIDs();
            ConcurrentHashMap<Integer,ManagerPermission> managerPermissionHashMap= stores.get(storeID).getManagerIDs();
            for(OwnerPermission permission: ownerPermissionHashMap.values()){
                if(permission.getAppointmentId()==removeOwnerID) {
                    stores.get(storeID).removeOwner(permission.getUserId());
                    subscribers.get(permission.getUserId()).removeOwnedStore(storeID);
                }
            }
            for(ManagerPermission permission: managerPermissionHashMap.values()){
                if(permission.getAppointmentId()==removeOwnerID)
                    stores.get(storeID).removeManager(permission.getUserId());
            }
        }
        return new Response(false, "Successfully removed the owner");
    }

    public ConcurrentHashMap<String, Integer> getConnectedSubscribers() {
        return connectedSubscribers;
    }

    //Observable pattern
    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }


    public void notifyObservers(Object object) {
        for (Observer o : this.observers) {
            o.update(this, object);
        }
    }
}