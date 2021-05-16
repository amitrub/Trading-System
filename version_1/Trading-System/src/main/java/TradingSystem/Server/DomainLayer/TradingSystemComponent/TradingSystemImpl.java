package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingBag;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingCart;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.DiscountPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.*;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.ConditionRoles.ConditionRole;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.ConditionRoles.ExistProduct;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.*;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.NumOfProductsForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.PriceForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.QuantityForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.*;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.XorDecision.Cheaper;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.XorDecision.Decision;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.UserComponent.*;
import TradingSystem.Server.ServiceLayer.DummyObject.*;
import TradingSystem.Server.ServiceLayer.ServiceApi.Publisher;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

import static TradingSystem.Server.ServiceLayer.Configuration.*;


public class TradingSystemImpl implements TradingSystem {


    public Validation validation;

    private ConcurrentHashMap<Integer, Integer> systemAdmins;
    private ConcurrentHashMap<String, Integer> connectedSubscribers;
    //private ConcurrentHashMap<Integer, String> passwords;

    public ConcurrentHashMap<Integer, User> subscribers;
    public ConcurrentHashMap<String, User> guests;
    public ConcurrentHashMap<Integer, Store> stores;
    //storeID_systemManagerPermission
    private ConcurrentHashMap<Integer, SystemManagerPermission> systemManagerPermissions;


    //    Singleton
    private static TradingSystemImpl tradingSystem = null;

    private TradingSystemImpl() {
        this.connectedSubscribers = new ConcurrentHashMap<>();
        this.subscribers = new ConcurrentHashMap<>();
        this.guests = new ConcurrentHashMap<>();
        this.stores = new ConcurrentHashMap<>();
        this.systemAdmins = new ConcurrentHashMap<>();
        this.systemManagerPermissions=new ConcurrentHashMap<>();
    }

    public static TradingSystemImpl getInstance() {
        if (tradingSystem == null) {
            tradingSystem = new TradingSystemImpl();
            tradingSystem.validation = new Validation();
            tradingSystem.ClearSystem();
            tradingSystem.Initialization();
        }
        return tradingSystem;
    }

    public void ClearSystem() {
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
        int userID = 1;
        User defaultAdmin = this.subscribers.get(userID);
        //TODO: to delete after
        String connID = "479f239c-797c-4bdb-8175-980acaabf070";
        this.connectedSubscribers.put(connID, userID);
        AddStore(userID, connID, "store1");
        AddStore(userID, connID, "Mar y juana");
        AddStore(userID, connID, "Roee Hadas");
        AddProductToStore(userID,connID,1,"prod1","sport", 7.0, 7 );
        AddProductToStore(userID, connID,1, "Sneakers2", "Shoes",50.0, 25);
        AddProductToStore(userID, connID, 1,"Sneaker3", "bla" ,80.0, 25);
        AddProductToStore(userID, connID, 2,"Sneakers24",  "Shoes", 80.0,25);
        AddProductToStore(userID, connID, 2, "Sneak23", "bloo", 840.0, 25);
        AddProductToStore(userID, connID, 2,"Sneakers",  "Shoes",80.0, 25);
        AddProductToStore(userID, connID, 3,"Sneakers2", "Shoes", 50.0, 25);
        AddProductToStore(userID, connID, 3,"Sneaker3", "bla" , 80.0,25);
        AddProductToStore(userID, connID, 3,"Sneakers24",  "Shoes", 80.0,25);
        AddProductToStore(userID, connID, 1, "Sneak23",  "bloo",840.0, 25);
        AddProductToStore(userID, connID, 2,"Sneakers",  "Shoes", 80.0,25);
        AddProductToStore(userID,connID,1,"Sneak","Shos", 52.0, 2 );
        AddProductToStore(userID,connID,2,"Sneak","Shos", 52.0, 2 );



        User user1 = new User("hadass", "1234");
        userID = user1.getId();
        this.subscribers.put(userID, user1);
        connID = "38095a9d-09dd-41ec-bd04-3a6d0da1c386";
        this.connectedSubscribers.put(connID, userID);

        this.connectedSubscribers = new ConcurrentHashMap<>();
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
    public Response ConnectSystem() {

        User newGuest = new User();
        String connID = connectGuestToSystemConnID(newGuest);
        Response res = new Response(false, "Connect system was successful");
        res.AddConnID(connID);
        res.AddUserGuest();
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
        Store store1 = new Store("Mar y juana", 1);
        store1.AddProductToStore("Sneakers2", 50.0, "Shoes", 25);
        store1.AddProductToStore( "Sneaker3", 80.0,"bla" , 25);
        store1.AddProductToStore("Sneakers24", 80.0, "Shoes", 25);
        store1.AddProductToStore( "Sneak23", 840.0, "bloo", 25);
        store1.AddProductToStore("Sneakers", 80.0, "Shoes", 25);
        Store store2 = new Store("Roee Hadas", 1);
        store2.AddProductToStore("Sneakers2", 50.0, "Shoes", 25);
        store2.AddProductToStore( "Sneaker3", 80.0,"bla" , 25);
        store2.AddProductToStore("Sneakers24", 80.0, "Shoes", 25);
        store2.AddProductToStore( "Sneak23", 840.0, "bloo", 25);
        store2.AddProductToStore("Sneakers", 80.0, "Shoes", 25);
        this.stores.put(store1.getId(), store1);
        this.stores.put(store2.getId(), store2);

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
            return new Response(true, "Exit: The user is not connected to the system");
        else if (connectedSubscribers.containsKey(connID)) {
            connectedSubscribers.remove(connID);
            return new Response(false, "Exit: Exit system was successful");
        } else  {
            guests.remove(connID);
            return new Response(false, "Exit: Exit system was successful");
        }
    }

    /**
     * @requirement 2.3
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
            return new Response(true, "Register: Error in connID");
        }
        else{
            if (validation.IsUserNameExist(userName)) { 
                return new Response(true, errMsgGenerator("Server", "TradingSystem", "62", "Error user name is taken"));
            }
            if(!validation.VerifyPassword(userName, password)){
                return new Response(true, errMsgGenerator("Server", "TradingSystem", "62", "Error password is invalid"));
            }
            User newUser = new User(userName, password);
            subscribers.put(newUser.getId(), newUser);
            Response res = new Response(false,"Register: Registration of " + userName + " was successful");
            res.AddConnID(connID);
            res.AddUserID(newUser.getId());
            res.AddUserGuest();
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
     * @requirement 2.4
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
        User myUser = subscribers.get(response.returnUserID());
        myUser.mergeToMyCart(myGuest.getShoppingCart());
        String connID = connectSubscriberToSystemConnID(response.returnUserID());
        guests.remove(guestConnID);
        Response res = new Response(false, "Login: Login of user " + userName + " was successful");
        res.AddUserID(response.returnUserID());
        res.AddConnID(connID);
        res.AddUserSubscriber(myUser.isManaged(), myUser.isOwner(), myUser.isFounder(),systemAdmins.containsKey(myUser.getId()));
        sendAlert(myUser, res);
        return res;
    }

    //Observer
    private void sendAlert(User user, Response res){
        Publisher publisher = new Publisher();
        user.setPublisher(publisher);
        user.update(res);

        //user.updateAfterLogin();
    }

    /**
     * @requirement 2.4
     * @param guestConnID
     * @param userName
     * @param password
     * @param publisher
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "userID": int
     * }
     */
    @Override
    public Response LoginPublisher(String guestConnID, String userName, String password, Publisher publisher) {
        Response res = Login(guestConnID,userName, password);
        if(!res.getIsErr()){
            User myUser = subscribers.get(res.returnUserID());
            myUser.setPublisher(publisher);
        }
        return res;
    }

    /**
     * @requirement 2.5.1
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
            new Response(true,"ShowAllStores: There are no stores in the system");
        }
        Response res = new Response(false, "ShowAllStores: Num of stores in the system is " + list.size());
        res.AddPair("stores", list);
        return res;
    }

    /**
     * @requirement 2.5.2
     * @param storeID
     * @return Response{
     *      "isErr: boolean
     *      "message": String
     *      "connID": String
     *      }
     */
    public Response ShowStoreProducts(int storeID) {
        if(stores.containsKey(storeID)){
            List<DummyProduct> list = stores.get(storeID).ShowStoreProducts();
            Response res = new Response(false, "ShowStoreProducts: Num of products in the store is " + list.size());
            res.AddPair("products", list);
            return res;
        }
        else
            return new Response(true, "ShowStoreProducts: The store with id " + storeID + " doesn't exist in the system");
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
     *  
     */
    public Response SearchProduct(String name, String category, int minprice, int maxprice){
        List<DummyProduct> dummyProducts = new ArrayList<>();
        for(Store store: stores.values()){
            // if(((prank==-1 || store.getRate()>=srank) && !store.SearchByName(name, minprice, maxprice,prank).isEmpty())){
            dummyProducts.addAll(store.SearchProduct(name,category, minprice, maxprice));
        }
        Response res = new Response(false, "Search: Num of products from search is " + dummyProducts.size());
        res.AddPair("products", dummyProducts);
        return res;
    }

    /**
     * @requirement 2.7
     * @param connID
     * @param StoreId
     * @param productId
     * @param quantity
     * @return
     */
    public Response AddProductToCart(String connID, int StoreId, int productId, int quantity){
        if(guests.containsKey(connID)){
            User myGuest= guests.get(connID);
            Response res = myGuest.AddProductToCart(StoreId,productId,quantity);
            res.AddUserGuest();
            return res;

        }
        else if(connectedSubscribers.containsKey(connID)){
            int userID= connectedSubscribers.get(connID);
            User user = subscribers.get(userID);
            Response res = user.AddProductToCart(StoreId,productId,quantity);
            res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
            return res;
        }
        else {
            return new Response(true, "AddProductToCart: The user is not connected to the system");
        }
    }

    /**
     * @requirement 2.8.1
     * @param connID
     * @return Response{
     *      "isErr: boolean
     *      "message": String
     *      "connID": String
     *     }
     */
    public Response ShowShoppingCart(String connID){
        if(guests.containsKey(connID)) {
            List<DummyProduct> list = guests.get(connID).ShowShoppingCart();
            Response res = new Response(false, "ShowShoppingCart: Num of products in my Shopping Cart is " + list.size());
            res.AddPair("products", list);
            res.AddUserGuest();
            return res;
        }
        else if(connectedSubscribers.containsKey(connID)) {
            int userID = connectedSubscribers.get(connID);
            User user = subscribers.get(userID);
            List<DummyProduct> list = user.ShowShoppingCart();
            Response res = new Response(false, "ShowShoppingCart: Num of products in my Shopping Cart is " + list.size());
            res.AddPair("products", list);
            res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
            return res;
        }
        else {
            return new Response(true, "ShowShoppingCart: The user doesn't Exist");
        }
    }

    /**
     * @requirement 2.8.2
     * @param connID
     * @param storeID
     * @param productID
     * @param quantity
     * @return Response{
     *      "isErr: boolean
     *      "message": String
     *      "connID": String
     *      }
     */
    public Response editProductQuantityFromCart(String connID, int storeID, int productID, int quantity) {
        if(guests.containsKey(connID)){
            User myGuest= guests.get(connID);
            Response res = myGuest.editProductQuantityFromCart(storeID, productID, quantity);
            res.AddUserGuest();
            return res;
        }
        else if(connectedSubscribers.containsKey(connID)){
            int userID= connectedSubscribers.get(connID);
            Response res = subscribers.get(userID).editProductQuantityFromCart(storeID, productID, quantity);
            User user=subscribers.get(userID);
            res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
            return res;
        }
        else {
            return new Response(true, "EditCart: : The user is not connected");
        }
    }

    /**
     * @requirement 2.8.3
     * @param connID
     * @param storeID
     * @param productID
     * @return Response{
     *      "isErr: boolean
     *      "message": String
     *      "connID": String
     *      }
     */
    public Response RemoveProductFromCart(String connID, int storeID, int productID) {
        if(guests.containsKey(connID)) {
            Response res =guests.get(connID).RemoveProductFromCart(storeID,productID);
            res.AddUserGuest();
            return res;
        }
        else if(connectedSubscribers.containsKey(connID)) {
            int userID = connectedSubscribers.get(connID);
            User user=subscribers.get(userID);
            Response res = user.RemoveProductFromCart(storeID,productID);
            res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
            return res;
        }
        else {
            return new Response(true, "RemoveFromCart: The user is not Exist");
        }
    }

    /**
     * @requirement 2.9.1
     * @param connID
     * @param name
     * @param credit_number
     * @param phone_number
     * @param address
     * @return Response{
     *      "isErr: boolean
     *      "message": String
     *      "connID": String
     *      }
     */
    public Response guestPurchase(String connID, String name, String credit_number, String phone_number, String address){
        if(!guests.containsKey(connID)){
            return new Response(true, "guestPurchase: The user is not connected to the system");
        }
        else {
            User myGuest= guests.get(connID);
            List<Store> storesToUpdate = new ArrayList<>();
            for (ShoppingBag s:myGuest.getShoppingCart().getShoppingBags().values())
            {
                Integer storeID = s.getStoreID();
                Store store = stores.get(storeID);
                storesToUpdate.add(store);
            }
            Response res = myGuest.guestPurchase(name, credit_number, phone_number, address);
            if(!res.getIsErr())
            {
                for(Store s:storesToUpdate){
                    Response resAlert = new Response(false, "A product has been purchased from your store");
                    s.sendAlertToOwners(resAlert);
                }
            }
            res.AddUserGuest();
            return res;
        }
    }

    /**
     * @requirement 2.9.2
     * @param userID
     * @param connID
     * @param credit_number
     * @param phone_number
     * @param address
     * @return Response{
     *      "isErr: boolean
     *      "message": String
     *      "connID": String
     *      }
     */
    public Response subscriberPurchase(int userID, String connID, String credit_number, String phone_number, String address){
        if(!ValidConnectedUser(userID, connID)){
            return new Response(true, "subscriberPurchase: The user is not connected to the system");
        }
        else {
            User user = subscribers.get(userID);
            List<Store> storesToUpdate = new ArrayList<>();
            for (ShoppingBag s:user.getShoppingCart().getShoppingBags().values())
            {
                Integer storeID = s.getStoreID();
                Store store = stores.get(storeID);
                storesToUpdate.add(store);
            }
            Response res = user.subscriberPurchase(credit_number, phone_number, address);
            if(!res.getIsErr())
            {
                for(Store s:storesToUpdate){
                    Response resAlert = new Response(false, "A product has been purchased from your store");
                    s.sendAlertToOwners(resAlert);
                }
            }
            res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
            return res;

        }
    }


    /**
     * @requirement 3.1
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
            myUser.setPublisher(null);
            connectedSubscribers.remove(connID);
            User newGuest = new User();
            newGuest.setShoppingCart(new ShoppingCart( myUser.getShoppingCart()));
            String guestConnID = connectGuestToSystemConnID(newGuest);
            Response res = new Response(false, "Logout: Logout of " + myUser.getUserName() + " was successful");
            res.AddConnID(guestConnID);
            res.AddUserGuest();
            return res;
        }
        else {
            return new Response(true, "Logout: User is not login");
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
            return new Response(true, "AddStore: The user is not connected");
        }
        else{
            if (validation.IsStoreNameExist(storeName)){
                return new Response(true, "AddStore: The store name is taken");
            }
            else {
                Store newStore = new Store(storeName, userID);
                User user = subscribers.get(userID);
                user.AddStore(newStore.getId());
                stores.put(newStore.getId(),newStore);
                Response res = new Response( "AddStore: Add store " + storeName + " was successful");
                res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
                return res; 
            }
        }
    }

    /**
     * @requirement 3.3
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
            return new Response(true, "WriteComment: Store " + storeId + " doesn't exist in the system");
        }
        else if(stores.containsKey(storeId)){
            Store store=stores.get(storeId);
            if(!store.isProductExist(productId)){
                return new Response(true, "WriteComment: The product " + productId + " doesn't exist in the store anymore");
            }
        }
        if(!ValidConnectedUser(userId, connID)) {
            return new Response(true, "WriteComment: The user " + userId + " is not connected");
        }
        User user=subscribers.get(userId);
        if(!user.IsProductExist(productId)){
            return new Response(true, "WriteComment: User didn't buy this product");
        }
        if(stores.get(storeId).getProduct(productId).isUserComment(userId)){
            return new Response(true, "WriteComment: The user already wrote comment for this product");
        }
        Product product = stores.get(storeId).getProduct(productId);
        product.addComment(userId, comment);

        Response resAlert = new Response(false, "There is a new comment on one of your store's products");
        stores.get(storeId).sendAlertToOwners(resAlert);

        Response res = new Response(false, "WriteComment: The comment added successfully to product " + productId);
        res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userId));
        return res; 
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
                Response res = new Response(true,"ShowSubscriberHistory: There are no older shopping in the history of user with id " + userID);
                res.AddPair("history", list);
                return res;
            }
            Response res = new Response(false, "ShowSubscriberHistory: Num of history buying of the user is " + list.size());
            res.AddPair("history", list);
            User user = subscribers.get(userID);
            res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
            return res;
        }
        else{
            return new Response(true, "ShowSubscriberHistory: The user " + userID + " is not connected");
        }
    }


    /**
     * @requirement 4.1.1
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
                return new Response(true, "AddProductToStore: The User " + userID + " is not allowed to add a product");
            }
            else {
                if(price<0) {
                    return new Response(true, "AddProductToStore: The price of the product can't be negative");
                }
                else{
                    if(quantity<0) {
                        return new Response(true, "AddProductToStore: The quantity of the product can't be negative");

                    }
                    else{
                        Response res = stores.get(storeID).AddProductToStore(productName, price, category, quantity);
                        printProducts();
                        User user = subscribers.get(userID);
                        res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
                        return res;
                    }
                }
            }
        }
        else{
            return new Response(true, "AddProductToStore: The user is not connected");
        }
    }

    /**
     * @requirement 4.1.2
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
                return new Response(true, "RemoveProduct: The User " + userID + " is not allowed to remove products from the inventory");
            }
            else {
                Response res = stores.get(storeID).deleteProduct(productID);
                printProducts();
                User user = subscribers.get(userID);
                res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
                return res;
            }
        }
        else{
            return new Response(true, "RemoveProduct: The user " + userID + "is not connected");
        }
    }

    /**
     * @requirement 4.1.3
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
                return new Response(true, "ChangeQuantityProduct: The user " + userID + " is not allowed to add products to the inventory");
            }
            else {
                if(quantity<0){
                    return new Response(true, "ChangeQuantityProduct: The quantity of the product can't be negative");
                }
                else {
                    Response res = stores.get(storeID).addProductToInventory(productId, quantity);
                    printProducts();
                    User user = subscribers.get(userID);
                    res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
                    return res;
                }
            }
        }
        else{
            return new Response(true, "ChangeQuantityProduct: The user " + userID + "is not connected");
        }
    }

    /**
     * @requirement 4.1.3
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
        if(ValidConnectedUser(userID, connID)){
            if(!hasPermission(userID,storeID, User.Permission.AddProduct)){
                return new Response(true, "EditProduct: The Edit is not allowed");
            }
            else {
                if(price<0){
                    return new Response(true, "EditProduct: The product price can't be negative");
                }
                else {
                    if(quantity<0){
                        return new Response(true, "EditProduct: The product quantity can't be negative");
                    }
                    else{
                        Response res = stores.get(storeID).editProductDetails(userID, productID, productName, price, category, quantity);
                        printProducts();
                        User user=subscribers.get(userID);
                        res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
                        return res;
                    }
                }
            }
        }
        else{
            return new Response(true, "EditProduct: The user " + userID + " is not connected");
        }
    }

    /**
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
    public Response AddNewOwner(int userID, String connID, int storeID, int newOwner) {
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "AddNewOwner: The user " + userID + "is not connected");
        }
        if (this.subscribers.get(newOwner) == null) {
            return new Response(true, "AddNewOwner: The user " + newOwner + " is not subscriber, so he can not be owner for store");
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
        NO.unlockUser();
        Response res = new Response(false, "AddNewOwner: The owner Added successfully");
        User user = subscribers.get(userID);
        res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
        return res; 
    }

    /**
     * @requirement 4.4
     * @param ownerID
     * @param connID
     * @param removeOwnerID
     * @param storeID
     * @return Response{
     *      "isErr: boolean
     *      "message": String
     *      "connID": String
     *      }
     */
    public Response RemoveOwnerByOwner(int ownerID, String connID, int removeOwnerID, int storeID) {
        if (!ValidConnectedUser(ownerID, connID)) {
            return new Response(true, "RemoveOwnerByOwner: The user " + ownerID + " is not connected");
        }
        if (!stores.containsKey(storeID)) {
            return new Response(true, "RemoveOwnerByOwner: The store doesn't exist in the system");
        }
        if(!stores.get(storeID).checkOwner(ownerID)){
            return new Response(true, "RemoveOwnerByOwner: The user " + ownerID + " is not the owner of the store");
        }
        if (!stores.get(storeID).checkOwner(removeOwnerID)) {
            return new Response(true, "RemoveOwnerByOwner: The user " + removeOwnerID + " that we want to remove is not the owner of the store");
        }
        if (stores.get(storeID).getPermission(removeOwnerID).getAppointmentId()!=ownerID) {
            return new Response(true, "RemoveOwnerByOwner: The user " + ownerID + " has no permissions to do this operation");
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

                    Store store = stores.get(storeID);
                    String storeName = store.getName();
                    Response resAlert = new Response(false, "You are removed from owning the store: " + storeName);
                    store.sendAlert(removeOwnerID, resAlert);
                }
            }
            for(ManagerPermission permission: managerPermissionHashMap.values()){
                if(permission.getAppointmentId()==removeOwnerID)
                    stores.get(storeID).removeManager(permission.getUserId());
            }
        }
        Response res = new Response(false, "RemoveOwnerByOwner: Successfully removed the owner " + removeOwnerID);
        User user=subscribers.get(ownerID);
        res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(ownerID));
        return res; 
    }

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
    public Response AddNewManager(int userID, String connID, int storeID, int newManager) {
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "AddNewManager: The user " + userID + "is not connected");
        }
        if (this.subscribers.get(newManager) == null) {
            return new Response(true, "AddNewManager: The user "+newManager+" is not subscriber, so he can not be manager for store");
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
        Response res = new Response(false, "AddNewManager: The manager Added successfully");
        User user = subscribers.get(userID);
        res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
        return res; 
    }

    /**
     * @requirement 4.6
     * @param userID
     * @param connID
     * @param storeID
     * @param managerID
     * @param permissions
     * @return Response{
     *      "isErr: boolean
     *      "message": String
     *      "connID": String
     *      }
     */
    public Response EditManagerPermissions(int userID, String connID, int storeID, int managerID, List<User.Permission> permissions) {
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "EditManagerPermissions: The user" + userID + "is not connected");
        }
        if (this.subscribers.get(managerID) == null) {
            return new Response(true, "EditManagerPermissions: The user " + managerID + " is not subscriber, so it impossible to edit his Permissions");
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
        Response res = new Response(false, "EditManagerPermissions:: The permissions of manager" + managerID + "edit successfully");
        User user=subscribers.get(userID);
        res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
        return res; 
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
            return new Response(true, "RemoveManager: The user " + userID + "is not connected");
        }
        if (this.subscribers.get(ManagerToRemove) == null) {
            return new Response(true, "RemoveManager: The user " + ManagerToRemove + " is not subscriber, so it impossible to remove him from management the store");
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
        Response res = new Response(false, "RemoveManager: The manager removed successfully");
        User user = subscribers.get(userID);
        res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
        return res;
    }

    /**
     * @requirement 4.9.1
     * @param userID
     * @param connID
     * @param storeID
     * @return Response{
     *      "isErr: boolean
     *      "message": String
     *      "connID": String
     *      }
     */
    public Response ShowStoreWorkers(int userID, String connID, int storeID){
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "ShowStoreWorkers: The user " + userID + " is not connected");
        }
        if(!stores.containsKey(storeID)){
            return new Response(true, "ShowStoreWorkers: The store " + storeID + " doesn't exist in the system");
        }
        if(!stores.get(storeID).checkOwner(userID)){
            return new Response(true, "ShowStoreWorkers: The user " + userID + " has no permissions to see this information");
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
            Response response = new Response(false, "ShowStoreWorkers: Num of workers in the store: " + workers.size());
            response.AddPair("ConnId",connID);
            response.AddPair("workers", workers);
            User user=subscribers.get(userID);
            response.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
            return response;
        }
    }

    /**
     * @requirement 4.9.2
     * @param userID
     * @param connID
     * @return Response{
     *      "isErr: boolean
     *      "message": String
     *      "connID": String
     *      }
     */
    public Response GetPossiblePermissionsToManager(int userID, String connID) {
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "GetPossiblePermissionsToManager: The user " + userID + " is not connected");
        }
        //List<String> permissions = new ArrayList<>();
        OwnerPermission OP = new OwnerPermission(userID, -1);
        //for (User.Permission P : OP.getPermissions()
        //) {
        //    permissions.add(P.toString());
        //}
        Response res = new Response(false, "GetPossiblePermissionsToManager: Viewing permissions was successful");
        //res.AddPair("permissions", permissions);
        res.AddPair("permissions", OP.getPermissions());
        User user=subscribers.get(userID);
        res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
        return res;
    }

    /**
     * @requirement 4.11
     * @param userID
     * @param storeID
     * @param connID
     * @return Response{
     *      "isErr: boolean
     *      "message": String
     *      "connID": String
     *      }
     */
    public Response StoreHistoryOwner(int userID, int storeID, String connID) {
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "StoreHistoryOwner: The user " + userID + "is not connected");
        }
        if (!hasPermission(userID, storeID, User.Permission.GetStoreHistory)) {
            List<DummyShoppingHistory> list = new ArrayList<>();
            Response res = new Response(true, "StoreHistoryOwner: The user has no permission to watch the history");
            res.AddPair("history", list);
            return res;
        }
        if (!stores.containsKey(storeID)) {
            List<DummyShoppingHistory> list = new ArrayList<>();
            Response res = new Response(true, "StoreHistoryOwner: wrong store ID");
            res.AddPair("history", list);
            return res;
        }

        List<DummyShoppingHistory> list = stores.get(storeID).ShowStoreHistory();
        Response res = new Response(false,"StoreHistoryOwner: Num of history buying in the store is " + list.size());
        res.AddPair("history", list);
        User user=subscribers.get(userID);
        res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
        return res;
    }

    /**
     * @requirement 6.4.1
     * @param AdminID
     * @param storeID
     * @param connID
     * @return Response{
     *      "isErr: boolean
     *      "message": String
     *      "connID": String
     *      }
     */
    public Response StoreHistoryAdmin(int AdminID, int storeID, String connID){
        if (!ValidConnectedUser(AdminID, connID)) {
            return new Response(true, "StoreHistory: User is not connected");
        }
        if (!hasPermission(AdminID, storeID, User.Permission.GetHistoryPurchasing)) {
            List<DummyShoppingHistory> list = new ArrayList<>();
            Response res = new Response(true, "StoreHistory: User has no permission to watch the history");
            res.AddPair("history", list);
            return res;
        }
        if (!stores.containsKey(storeID)) {
            List<DummyShoppingHistory> list = new ArrayList<>();
            Response res = new Response(true, "StoreHistory: The store doesn't exist in the system");
            res.AddPair("history", list);
            return res;
        }

        List<DummyShoppingHistory> list = stores.get(storeID).ShowStoreHistory();
        Response res = new Response(false,"StoreHistory: Num of history buying in the store is " + list.size());
        res.AddPair("history", list);
        User user=subscribers.get(AdminID);
        res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(AdminID));
        return res;
    }

    /**
     * @requirement 6.4.2
     * @param AdminID
     * @param userID
     * @param connID
     * @return Response{
     *      "isErr: boolean
     *      "message": String
     *      "connID": String
     *      }
     */
    public Response UserHistoryAdmin(int AdminID, int userID, String connID){
        if (!ValidConnectedUser(AdminID, connID)) {
            return new Response(true, "UserHistory: User is not connected");
        }
        if (!hasPermission(AdminID, userID, User.Permission.GetHistoryPurchasing)) {
            List<DummyShoppingHistory> list = new ArrayList<>();
            Response res = new Response(true, "UserHistory: User has no permission to watch the history");
            res.AddPair("history", list);
            return res;
        }
        if (!subscribers.containsKey(userID)) {
            List<DummyShoppingHistory> list = new ArrayList<>();
            Response res = new Response(true, "UserHistory: The user doesn't exist in the system");
            res.AddPair("history", list);
            return res;
        }

        User user = subscribers.get(userID);
        List<DummyShoppingHistory> list = user.ShowUserHistory();
        Response res = new Response(false,"UserHistory: Num of history buying in the store is " + list.size());
        res.AddPair("history", list);
        res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
        return res;
    }

    /**
     * @requirement 6.4.3
     * @param AdminID
     * @param connID
     * @return Response{
     *      "isErr: boolean
     *      "message": String
     *      "connID": String
     *      }
     */
    public Response AllStoresHistoryAdmin(int AdminID, String connID){
        if (!ValidConnectedUser(AdminID, connID)) {
            return new Response(true, "AllStoresHistory: User is not connected");
        }
        if (!hasPermission(AdminID, User.Permission.GetHistoryPurchasing)) {
            List<DummyShoppingHistory> list = new ArrayList<>();
            Response res = new Response(true, "AllStoresHistory: User has no permission to watch the history");
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
        Response res = new Response(false,"AllStoresHistory: Num of history buying in the store is " + list.size());
        res.AddPair("history", list);
        User user=subscribers.get(AdminID);
        res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(AdminID));
        return res;
    }

    /**
     * @requirement 6.4.4
     * @param AdminID
     * @param connID
     * @return Response{
     *      "isErr: boolean
     *      "message": String
     *      "connID": String
     *      }
     */
    public Response AllUsersHistoryAdmin(int AdminID, String connID) {
        if (!ValidConnectedUser(AdminID, connID)) {
            return new Response(true, "AllUsersHistory: User is not connected");
        }
        if (!hasPermission(AdminID, User.Permission.GetHistoryPurchasing)) {
            List<DummyShoppingHistory> list = new ArrayList<>();
            Response res = new Response(true, "AllUsersHistory: User has no permission to watch the history");
            res.AddPair("history", list);
            return res;
        }
        List<DummyShoppingHistory> list = new ArrayList<>();
        for (User u : subscribers.values()) {
            for (DummyShoppingHistory e : u.ShowUserHistory()) {
                list.add(e);
            }
        }
        Response res = new Response(false, "AllUsersHistory: Num of history buying in the store is " + list.size());
        res.AddPair("history", list);
        User user=subscribers.get(AdminID);
        res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(AdminID));
        return res;
    }


    //other functions
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

    public void addHistoryToStoreAndUser(ShoppingHistory sh, boolean isGuest) {
        this.stores.get(sh.getStoreID()).addHistory(sh);
        if (!isGuest)
            this.subscribers.get(sh.getUserID()).addHistory(sh);
    }
/*
    public List<DummyProduct> SearchProductByName(String name, int minprice, int maxprice, int prank , int srank){
        List<DummyProduct> dummyProducts = new ArrayList<>();
        for(Store store: stores.values()){
            if(((prank==-1 || store.getRate()>=srank) && !store.SearchByName(name, minprice, maxprice,prank).isEmpty())){
                dummyProducts.addAll(store.SearchByName(name, minprice, maxprice,prank));
            }
        }
        return dummyProducts;
    }

    public List<DummyProduct> SearchProductByCategory(String category, int minprice, int maxprice, int prank , int srank){
        List<DummyProduct> dummyProducts = new ArrayList<>();
        for(Store store: stores.values()){
            if(!store.SearchByCategory(category, minprice, maxprice,prank).isEmpty()){
                dummyProducts.addAll(store.SearchByCategory(category, minprice, maxprice,prank));
            }
        }
        return dummyProducts;
    }

 */

    public Response systemRoleChecks(int userID, int storeID, int newRole, User.Permission permission) {
        if (!this.subscribers.containsKey(userID)) {
            return new Response(true, "The user "+userID+" is not subscriber, so he can not appoint manager for store");
        }
        if (!this.subscribers.containsKey(newRole)) {
            return new Response(true, "User "+newRole+" is not subscriber, so it impossible to "+permission.toString()+" him for store");
        }
        if (!this.subscribers.get(userID).getMyOwnerStore().contains(storeID)){
            return new Response(true, "User "+userID+" is not the owner of the store, so he can not "+permission.toString()+" to the store");
        }
        if(!this.hasPermission(userID,storeID,permission)) {
            return new Response(true, "User " + userID + " is not allowed to "+permission.toString());
        }
        Response res = new Response(false,"Sys OK");
        User user = subscribers.get(userID);
        res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
        return res;
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
       if(this.stores.get(storeID)!=null) {
           return this.stores.get(storeID).getProduct(productID);
       }
       return null;
    }

    public boolean ValidConnectedUser(int userID, String connID){
        return connectedSubscribers.containsKey(connID) && connectedSubscribers.get(connID).equals(userID);
    }

    public Response reduceProducts(ConcurrentHashMap<Integer, Integer> products, int storeID) {
       return this.stores.get(storeID).reduceProducts(products);
    }

    public void cancilReduceProducts(Integer storeID, ConcurrentHashMap<Integer, Integer> products) {
      this.stores.get(storeID).cancilReduceProducts(products);
    }

    public List<DummyShoppingHistory> ShowStoreHistory(int storeId){
        return stores.get(storeId).ShowStoreHistory();
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

    public Double calculateBugPrice(int userID, int storeID, ConcurrentHashMap<Integer, Integer> productsInTheBug) {
        if(this.subscribers.contains(userID))
            return this.stores.get(storeID).calculateBugPrice(userID,productsInTheBug);
        else
            return this.stores.get(storeID).calculateBugPrice(userID,productsInTheBug);
    }

    public Response ShowOwnerStores(int userID, String connID) {
        if(!ValidConnectedUser(userID, connID)) {
            return new Response(true, "ShowOwnerStores: The User is not connected");
        }
        else if (!subscribers.containsKey(userID)){
            return new Response(true, "ShowOwnerStores: User is not subscriber");
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
            Response res = new Response(false, "ShowOwnerStores: Num of owned stores of the user is " + list.size());
            res.AddPair("stores", list);
            res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
            return res;
        }
    }

    public Response ShowManagerStores(int userID, String connID) {
        if(!ValidConnectedUser(userID, connID)) {
            return new Response(true, "ShowManagerStores: The User is not connected");
        }
        else if (!subscribers.containsKey(userID)){
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
            Response res = new Response(false, "ShowManagerStores: num of managed stores of the user is " + list.size());
            res.AddPair("stores", list);
            res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
            return res;
        }
    }

    public Response ShowAllUsers(int adminID, String connID) {
        if(!ValidConnectedUser(adminID, connID)){
            return new Response(true, "ShowAllUsers: User is not connected");
        }
        else if (!systemAdmins.containsKey(adminID)){
            return new Response(true, "ShowAllUsers: User is not admin");
        }
        else {
            List<DummyUser> list = new ArrayList<>();
            for (Map.Entry<Integer, User> currUser : subscribers.entrySet()) {
                list.add(new DummyUser(currUser.getValue()));
            }
            Response res = new Response(false, "ShowAllUsers: Num of users in the system is " + list.size());
            res.AddPair("users", list);
            User user=subscribers.get(adminID);
            res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(adminID) );
            return res;
        }
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

    public ConcurrentHashMap<String, Integer> getConnectedSubscribers() {
        return connectedSubscribers;
    }

    public Response ShowAllMyStores(String connID, int userID, boolean founder,boolean owner,boolean manager) {
        if(!ValidConnectedUser(userID,connID)){
            return new Response(true, "Error in User details");
        }
        List<DummyStore> list = new ArrayList<>();
        for (Integer storeID: stores.keySet()){
            Store store = stores.get(storeID);
            if((founder && store.checkFounder(userID))||(owner && store.checkOwner(userID))||(manager && store.checkManager(userID)))
                list.add(new DummyStore(store));
        }
        if(list.isEmpty()){
            new Response(true,"There are no stores in the system");
        }
        Response res = new Response("num of stores in the system is " + list.size());
        if(founder)
            res.AddPair("founderStores", list);
        else if(owner)
            res.AddPair("ownerStores", list);
        if(manager)
            res.AddPair("managerStores", list);
        return res;
    }

    //Todo finish
    public Response addDiscountPolicy(int userID, String connID, int storeID,Sale sale){
        if (!ValidConnectedUser(userID, connID)) {
             return new Response(true, "Error in Admin details");
        }
        if (!subscribers.containsKey(userID)) {
              return new Response(true, "the user is not subscriber to the system");
        }
        if(stores.get(storeID)==null){
            return new Response(true, "the store not exist in the system");
        }
        if(!stores.get(storeID).checkOwner(userID)){
             return new Response(true, "the user is not the owner of the store");
        }
        Store s=this.stores.get(storeID);
        Response r=sale.checkValidity(storeID);
        if(r.getIsErr()){
            return r;
        }
        DiscountPolicy d=new DiscountPolicy(storeID,sale);
        s.setDiscountPolicy(d);
        return new Response("the discountPolicy added successfully");
    }


    private Sale createSale(Integer storeID,String saleName, Map<String, Object> o) {
        Sale s=null;

        if(saleName.equals("AddComposite")){
            LinkedList<Sale> list=new LinkedList<Sale>();
            AddComposite sale=new AddComposite(list);
            for (String mapKey:o.keySet()
            ) {
                Map<String, Object> tosend = new HashMap<>();
                tosend.put(mapKey,(Map<String, Object>) o.get(mapKey));
                CreateDiscountPolicy(storeID,sale,tosend);
            }
            s=sale;
        }
        if(saleName.equals("MaxComposite")){
            LinkedList<Sale> list=new LinkedList<Sale>();
            MaxComposite sale=new MaxComposite(list);
            for (String mapKey:o.keySet()
            ) {
                Map<String, Object> tosend = new HashMap<>();
                tosend.put(mapKey,(Map<String, Object>) o.get(mapKey));
                CreateDiscountPolicy(storeID,sale,tosend);
            }
            s=sale;
        }
        return s;
    }

    private Sale CreateDiscountPolicy(Integer storeID, Sale sale, Map<String, Object> o) {
        Response res=new Response("");
        for (String key:o.keySet()
        ) {
            switch (key){
                case "AddComposite":
                    AddComposite addComposite=new AddComposite();
                    Map<String, Object> map1=(Map<String, Object>)o.get("AddComposite");
                    for (String mapKey:map1.keySet()
                    ) {
                        Map<String, Object> tosend = new HashMap<>();
                        tosend.put(mapKey,(Map<String, Object>) map1.get(mapKey));
                        CreateDiscountPolicy(storeID,addComposite,tosend);
                    }
                    sale.setSale(addComposite);
                    return sale;
                case "MaxComposite":
                    MaxComposite maxComposite=new MaxComposite();
                    Map<String, Object> map2=(Map<String, Object>)o.get("MaxComposite");
                    for (String mapKey:map2.keySet()
                    ) {
                        Map<String, Object> tosend = new HashMap<>();
                        tosend.put(mapKey,(Map<String, Object>) map2.get(mapKey));
                        CreateDiscountPolicy(storeID,maxComposite,tosend);
                    }
                    sale.setSale(maxComposite);
                    return sale;
                case "XorComposite":
                    XorComposite xorComposite = new XorComposite();
                    Map<String, Object> map0 = (Map<String, Object>) o.get("XorComposite");
                    Map<String, Object> Decision=(  Map<String, Object>) map0.get("Decision");
                    Decision dec=createDecision(Decision);
                    for (String mapKey:map0.keySet()
                    ) {
                        Map<String, Object> tosend = new HashMap<>();
                        tosend.put(mapKey,(Map<String, Object>) map0.get(mapKey));
                        CreateDiscountPolicy(storeID,xorComposite,tosend);
                    }
                    sale.setSale(xorComposite);
                    return xorComposite;
                case "StoreSale":
                    Map<String, Object> map3=(Map<String, Object>)o.get("StoreSale");
                    Double discount1=(Double)map3.get("discount");
                    Map<String, Object> exp1= (Map<String, Object>)map3.get("expression");
                    StoreSale storeSale=new StoreSale(storeID,discount1);
                    Expression e1=createSaleExp(storeID,exp1);
                    storeSale.setExpression(e1);
                    sale.setSale(storeSale);
                    return sale;
                case "ProductSale":
                    Map<String, Object> map4=(Map<String, Object>)o.get("ProductSale");
                    Integer productID=(Integer)map4.get("productID");
                    Double discoun2=(Double)map4.get("discount");
                    Map<String, Object> exp2= (Map<String, Object>)map4.get("expression");
                    ProductSale productSale=new ProductSale(productID,discoun2);
                    Expression e2=createSaleExp(storeID,exp2);
                    productSale.setExpression(e2);
                    sale.setSale(productSale);
                    return sale;
                case "CategorySale":
                    Map<String, Object> map5=(Map<String, Object>)o.get("CategorySale");
                    String category=(String) map5.get("category");
                    Double discoun3=(Double)map5.get("discount");
                    Map<String, Object> exp3= (Map<String, Object>)map5.get("expression");
                    CategorySale categorySale=new CategorySale(category,discoun3);
                    Expression e3=createSaleExp(storeID,exp3);
                    categorySale.setExpression(e3);
                    sale.setSale(categorySale);
                    return sale;
            }
        }
        return sale;
    }

    private Decision createDecision(  Map<String, Object> decision) {
        String des=(String) decision.get("decision");
        if(decision.equals("Cheaper")){
          return new Cheaper();
        }
        return null;
    }

    private Expression createSaleExp(Integer storeID, Map<String, Object> exp) {
        for (String key : exp.keySet()
        ) {
            switch (key) {
                case "AndComposite":
                    AndComposite andComposite = new AndComposite();
                    Map<String, Object> map1 = (Map<String, Object>) exp.get("AndComposite");
                    for (String mapKey:map1.keySet()
                    ) {
                        Map<String, Object> tosend = new HashMap<>();
                        tosend.put(mapKey,(Map<String, Object>) map1.get(mapKey));
                        Expression tmp=createSaleExp(storeID, tosend);
                        andComposite.add(tmp);
                    }
                    return andComposite;
                case "OrComposite":
                    OrComposite orComposite = new OrComposite();
                    Map<String, Object> map2 = (Map<String, Object>) exp.get("OrComposite");
                    for (String mapKey:map2.keySet()
                    ) {
                        Map<String, Object> tosend = new HashMap<>();
                        tosend.put(mapKey,(Map<String, Object>) map2.get(mapKey));
                        Expression tmp=createSaleExp(storeID, tosend);
                        orComposite.add(tmp);
                    }
                    return orComposite;
                case "NumOfProductsForGetSale":
                    Map<String, Object> map5 = (Map<String, Object>) exp.get("NumOfProductsForGetSale");
                    Integer num = (Integer) map5.get("numOfProductsForSale");
                    NumOfProductsForGetSale numOfProductsForSale = new NumOfProductsForGetSale(num);
                    return numOfProductsForSale;
                case "PriceForGetSale":
                    Map<String, Object> map6 = (Map<String, Object>) exp.get("PriceForGetSale");
                    Double price = (Double) map6.get("priceForSale");
                    PriceForGetSale priceForGetSale = new PriceForGetSale(price);
                    return priceForGetSale;
                case "QuantityForGetSale":
                    Map<String, Object> map7 = (Map<String, Object>) exp.get("QuantityForGetSale");
                    Integer productId = (Integer) map7.get("productID");
                    Integer quantity = (Integer) map7.get("quantityForSale");
                    QuantityForGetSale quantityForGetSale = new QuantityForGetSale(productId, quantity);
                    return quantityForGetSale;
            }
        }
        return null;
    }

    private Response checkPermissionToPolicy(int userID, String connID, int storeID){
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "Error in Admin details");
        }
        if (!subscribers.containsKey(userID)) {
            return new Response(true, "the user is not subscriber to the system");
        }
        if(stores.get(storeID)==null){
            return new Response(true, "the store not exist in the system");
        }
        if(!stores.get(storeID).checkOwner(userID)){
            return new Response(true, "the user is not the owner of the store");
        }
        return new Response(false, "");
    }

    @Override
    public Response addBuyingPolicy(int userID, String connID, int storeID, Expression exp){
        Response res = checkPermissionToPolicy(userID, connID, storeID);
        if(res.getIsErr()){
            return res;
        }
        Response r=exp.checkValidity(storeID);
        if(r.getIsErr()){
            return r;
        }
        Store s=this.stores.get(storeID);
        BuyingPolicy b=new BuyingPolicy(storeID,exp);
        s.setBuyingPolicy(b);
        return new Response("");
    }

    public Response GetPoliciesInfo(int userID, int storeID, String connID){
        Response res = checkPermissionToPolicy(userID, connID, storeID);
        if(res.getIsErr()){
            return res;
        }
        Store s=this.stores.get(storeID);
        BuyingPolicy BP = s.getBuyingPolicy();
        DiscountPolicy DP = s.getDiscountPolicy();
        Response r = new Response(false, "The Buying Policy and Discount Policy returned successfully");
        r.AddConnID(connID);
        r.AddUserID(userID);
        r.AddPair("BuyingPolicy", BP);
        r.AddPair("DiscountPolicy", DP);
        return r;
    }

    private Expression createLimitExp(int storeID, Map<String, Object> exp) {
        for (String key : exp.keySet()
        ) {
            switch (key) {
                case "AndComposite":
                    AndComposite andComposite = new AndComposite();
                    Map<String, Object> map1 = (Map<String, Object>) exp.get("AndComposite");
                    for (String mapKey : map1.keySet()
                    ) {
                        Map<String, Object> tosend = new HashMap<>();
                        tosend.put(mapKey, (Map<String, Object>) map1.get(mapKey));
                        Expression tmp = createLimitExp(storeID, tosend);
                        andComposite.add(tmp);
                    }
                    return andComposite;
                case "OrComposite":
                    OrComposite orComposite = new OrComposite();
                    Map<String, Object> map2 = (Map<String, Object>) exp.get("OrComposite");
                    for (String mapKey : map2.keySet()
                    ) {
                        Map<String, Object> tosend = new HashMap<>();
                        tosend.put(mapKey, (Map<String, Object>) map2.get(mapKey));
                        Expression tmp = createLimitExp(storeID, tosend);
                        orComposite.add(tmp);
                    }
                    return orComposite;
                case "Conditioning":
                    Conditioning conditioning = new Conditioning();
                    Map<String, Object> map4 = (Map<String, Object>) exp.get("Conditioning");
                    Map<String, Object> cond = (Map<String, Object>) map4.get("cond");
                    ConditionRole e1 = createConditingRole(storeID, cond);
                    Map<String, Object> condIf = (Map<String, Object>) map4.get("condIf");
                    Expression e2 = createLimitExp(storeID, condIf);
                    conditioning.setCond(e1);
                    conditioning.setCondIf(e2);
                    return conditioning;
                case "AgeLimitForCategory":
                    Map<String, Object> map5 = (Map<String, Object>) exp.get("AgeLimitForCategory");
                    Integer minAgec= (Integer) map5.get("minAge");
                    String category= (String) map5.get("category");
                    AgeLimitForCategory ageLimitForCategory = new AgeLimitForCategory(minAgec,category);
                    return ageLimitForCategory;
                case "AgeLimitForProduct":
                    Map<String, Object> map6 = (Map<String, Object>) exp.get("AgeLimitForProduct");
                    Integer minAgep= (Integer) map6.get("minAge");
                    Integer productID= (Integer) map6.get("productID");
                    AgeLimitForProduct ageLimitForProduct = new AgeLimitForProduct(minAgep,productID);
                    return ageLimitForProduct;
                case "AgeLimitForStore":
                    Map<String, Object> map7 = (Map<String, Object>) exp.get("AgeLimitForStore");
                    Integer minAges= (Integer) map7.get("minAge");
                    AgeLimitForStore ageLimitForStore = new AgeLimitForStore(minAges,storeID);
                    return ageLimitForStore;
                case "QuantityLimitForProduct":
                    Map<String, Object> map8 = (Map<String, Object>) exp.get("QuantityLimitForProduct");
                    Integer maxQuantityp= (Integer) map8.get("maxQuantity");
                    Integer productIDq= (Integer) map8.get("productID");
                    QuantityLimitForProduct quantityLimitForProduct = new QuantityLimitForProduct(maxQuantityp,productIDq);
                    return quantityLimitForProduct;
                case "QuantityLimitForCategory":
                    Map<String, Object> map9 = (Map<String, Object>) exp.get("QuantityLimitForCategory");
                    Integer maxQuantityc= (Integer) map9.get("maxQuantity");
                    String categoryq= (String) map9.get("category");
                    QuantityLimitForCategory quantityLimitForCategory = new QuantityLimitForCategory(maxQuantityc,categoryq);
                    return quantityLimitForCategory;
                case "QuantityLimitForStore":
                    Map<String, Object> map10 = (Map<String, Object>) exp.get("QuantityLimitForStore");
                    Integer maxQuantitys= (Integer) map10.get("maxQuantity");
                    QuantityLimitForStore quantityLimitForStore = new QuantityLimitForStore(maxQuantitys,storeID);
                    return quantityLimitForStore;
            }
        }
        return null;
    }

    private ConditionRole createConditingRole(int storeID, Map<String, Object> cond) {
        String role=(String) cond.get("Role");
        if(role.equals("ExistProduct")){
           Integer productId=(Integer) cond.get("productID");
            return new ExistProduct(productId);
        }
        return null;


    }

    //for the tests
    public void AddStoreToList(Store store) {
        this.stores.put(store.getId(), store);
    }

    //todo add permission?
    public Response RemoveBuyingPolicy(int userID, int storeID, String connID) {
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "Error in Admin details");
        }
        if (!subscribers.containsKey(userID)) {
             return new Response(true, "the user is not subscriber to the system");
        }
        if(stores.get(storeID)==null){
            return new Response(true, "the store not exist in the system");
        }
        if(!stores.get(storeID).checkOwner(userID)){
              return new Response(true, "the user is not the owner of the store");
        }
       if(stores.get(storeID).getBuyingPolicy()==null){
           return new Response(true,"there is not policy");
       }
        return  stores.get(storeID).RemoveBuyingPolicy();
    }

    public Response RemoveDiscountPolicy(int userID, int storeID, String connID) {
        if (!ValidConnectedUser(userID, connID)) {
               return new Response(true, "Error in Admin details");
        }
        if (!subscribers.containsKey(userID)) {
              return new Response(true, "the user is not subscriber to the system");
        }
        if(stores.get(storeID)==null){
             return new Response(true, "the store not exist in the system");
        }
        if(!stores.get(storeID).checkOwner(userID)){
             return new Response(true, "the user is not the owner of the store");
        }
        return stores.get(storeID).RemoveDiscountPolicy();
    }

    @Override
    public Expression CreateExpForBuy(Integer storeID,Map<String, Object> map) {
        AndComposite exp=new AndComposite();
        Iterator it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            String limitName = (String) pair.getKey();
            Map<String, Object> value = (Map<String, Object>) pair.getValue();
            Map<String, Object> tosend = new HashMap<>();
            tosend.put(limitName, value);
            Expression res = this.createLimitExp(storeID, tosend);
            exp.add(res);
        }
        return exp;
    }

    @Override
    public Sale createSaleForDiscount(int storeID, Map<String, Object> obj) {
        if (obj.size() == 1) {
            Iterator it = obj.entrySet().iterator();
            Map.Entry pair = (Map.Entry) it.next();
            String saleName = (String) pair.getKey();
            Map<String, Object> value = (Map<String, Object>) pair.getValue();
            Sale res = this.createSale(storeID, saleName, value);
            return res;
        }
        return null;
    }


}
