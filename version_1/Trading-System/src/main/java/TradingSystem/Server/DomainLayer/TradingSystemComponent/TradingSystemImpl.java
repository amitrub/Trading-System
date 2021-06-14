package TradingSystem.Server.DomainLayer.TradingSystemComponent;


import TradingSystem.Server.DataLayer.Data_Modules.Bid.DataBid;
import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DBExpression;
import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DataBuyingPolicy;
import TradingSystem.Client.ClientProxy;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Sales.DBSale;
import TradingSystem.Server.DataLayer.Data_Modules.Sales.DataDiscountPolicy;
import TradingSystem.Server.DataLayer.Services.Data_Controller;
import TradingSystem.Server.DomainLayer.ExternalServices.*;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingBag;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingCart;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Bid;
import TradingSystem.Server.DomainLayer.StoreComponent.Inventory;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.DiscountPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.*;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.*;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.NumOfProductsForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.PriceForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.QuantityForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.*;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.XorDecision.Cheaper;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.XorDecision.Decision;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.Task.AddManagerTaskUnitTests;
import TradingSystem.Server.DomainLayer.Task.PurchaseTaskUnitTests;
import TradingSystem.Server.DomainLayer.Task.RegisterTaskUnitTests;
import TradingSystem.Server.DomainLayer.Task.RemoveProductTaskUnitTests;
import TradingSystem.Server.DomainLayer.UserComponent.*;

import TradingSystem.Server.JsonInitReader;
import TradingSystem.Server.JsonStateReader;
import TradingSystem.Server.JsonUser;
import TradingSystem.Server.ServiceLayer.Bridge.Trading_Driver;
import TradingSystem.Server.ServiceLayer.DummyObject.*;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyExpressions.*;
import TradingSystem.Server.ServiceLayer.DummyObject.DummySales.*;
import TradingSystem.Server.ServiceLayer.ServiceApi.Publisher;
import TradingSystem.Server.TradingSystemApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;


import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

import static TradingSystem.Server.ServiceLayer.Configuration.*;

@Service
@Scope("singleton")
public class TradingSystemImpl implements TradingSystem {

    @Autowired
    public Data_Controller data_controller;

    public Validation validation;
    public AddFromDb addFromDb;
    boolean isUploadAllData = false;

    private ConcurrentHashMap<Integer, Integer> systemAdmins = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Integer> connectedSubscribers = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Integer, User> subscribers = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, User> guests = new ConcurrentHashMap<>();
    public ConcurrentHashMap<Integer, Store> stores= new ConcurrentHashMap<>();
    //storeID_systemManagerPermission
    private ConcurrentHashMap<Integer, SystemManagerPermission> systemManagerPermissions = new ConcurrentHashMap<>();

    //storeID_DiscountPolicy
    private ConcurrentHashMap<Integer, DummySale> tmpDiscountPolicyForStore = new ConcurrentHashMap<>();
    //storeID_BuyingPolicy
    private ConcurrentHashMap<Integer, DummyExpression> tmpBuyingPolicyForStore = new ConcurrentHashMap<>();

    private static ExternalServices paymentSystem;
    private static ExternalServices supplySystem;


    public TradingSystemImpl(Data_Controller data_controller, ApplicationArguments args) {
        this.data_controller = data_controller;
        this.setData_controller(this.data_controller);
        this.setTradingSystem(this);
        this.validation = new Validation(this);
        this.addFromDb= new AddFromDb(this,this.data_controller);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<String> arg = args.getNonOptionArgs();
            String initializationPath = arg.get(0);
            String ExternalStatePath = arg.get(1);
            //System.out.println(ANSI_YELLOW + "-----------TEST---------\n"+ ExternalStatePath + "\n------------END TEST----------------" + ANSI_RESET);
            //String initializationPath = "src/main/resources/initialization_System.json";
            File file = new File(initializationPath);
            String absolutePath = file.getAbsolutePath();
            JsonInitReader readJson = objectMapper.readValue(new File(absolutePath), JsonInitReader.class);
            Boolean externalState = readJson.getExternalState();
            if (externalState){
                this.Initialization(ExternalStatePath);
            }
            Boolean externalServices = readJson.getExternalServices();
            if(externalServices){
                setPaymentSystem(PaymentSystem.getInstance());
                setSupplySystem(SupplySystem.getInstance());
            }
            else{
                setPaymentSystem(PaymentSystemMock.getInstance());
                setSupplySystem(SupplySystemMock.getInstance());
            }
            String userName = readJson.getAdmin().getUserName();
            String password = readJson.getAdmin().getPassword();
            Response response=this.data_controller.GetSubscriber(userName, password);
            DataSubscriber subscriber = this.data_controller.GetSubscriber(userName, password).returnDataSubscriber();
            int userID;
            if (subscriber==null){
            //    Response response= data_controller.AddSubscriber(userName, password);
                userID = data_controller.AddSubscriber(userName, password).returnUserID();
            } else {
                userID = subscriber.getUserID();
            }
            if(readJson.getLoadTest())
            {
                LoadTestInfo();
            }

            User defaultAdmin = new User(userID,userName, password);
            this.systemAdmins.put(userID, userID);
            this.subscribers.put(userID, defaultAdmin);
            this.systemManagerPermissions.put(userID,new SystemManagerPermission());

        }
        catch (Exception e){
            TradingSystemApplication.WriteToLogger("There is a problem with the Initialization file ");
            System.exit(1);
        }
    }

    public void setSubscribers(ConcurrentHashMap<Integer, User> subscribers){
        this.subscribers=subscribers;
    }

    private void setData_controller(Data_Controller data_controller){
        User.setData_controller(data_controller);
        Store.setData_controller(data_controller);
        Product.setData_controller(data_controller);
        Inventory.setData_controller(data_controller);
        ShoppingCart.setData_controller(data_controller);
        ShoppingBag.setData_controller(data_controller);
        Bid.setData_controller(data_controller);
    }

    private void setTradingSystem(TradingSystemImpl tradingSystem){
        User.setTradingSystem(tradingSystem);
        Store.setTradingSystem(tradingSystem);
        Product.setTradingSystem(tradingSystem);
        ShoppingCart.setTradingSystem(tradingSystem);
        ShoppingBag.setTradingSystem(tradingSystem);
        SimpleExpression.setTradingSystem(tradingSystem);
        AgeLimitForCategory.setTradingSystem(tradingSystem);
        QuantityLimitForCategory.setTradingSystem(tradingSystem);
        CategorySale.setTradingSystem(tradingSystem);
        ProductSale.setTradingSystem(tradingSystem);
        StoreSale.setTradingSystem(tradingSystem);
        AddManagerTaskUnitTests.setTradingSystem(tradingSystem);
        PurchaseTaskUnitTests.setTradingSystem(tradingSystem);
        RegisterTaskUnitTests.setTradingSystem(tradingSystem);
        RemoveProductTaskUnitTests.setTradingSystem(tradingSystem);
        Trading_Driver.setTradingSystem(tradingSystem);
        ClientProxy.setTradingSystem(tradingSystem);
        Bid.setTradingSystem(tradingSystem);
    }

    public void setStores(ConcurrentHashMap<Integer, Store> stores){
        this.stores=stores;
    }


    public void ClearSystem() {
        this.connectedSubscribers = new ConcurrentHashMap<>();
        this.subscribers = new ConcurrentHashMap<>();
        this.guests = new ConcurrentHashMap<>();
        this.stores = new ConcurrentHashMap<>();
        this.systemAdmins = new ConcurrentHashMap<>();
        this.systemManagerPermissions=new ConcurrentHashMap<>();
        this.tmpDiscountPolicyForStore=new ConcurrentHashMap<>();
        this.tmpBuyingPolicyForStore=new ConcurrentHashMap<>();

        User defaultAdmin = new User("amit", "qweasd");
        int userID = defaultAdmin.getId();
        this.systemAdmins.put(userID, userID);
        this.subscribers.put(userID, defaultAdmin);
        this.systemManagerPermissions.put(userID,new SystemManagerPermission());
        printUsers();

        this.data_controller.deleteAll();
    }



    public void Initialization(String path){
        data_controller.deleteAll();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //String path = "src/main/resources/External_State.json";
            File file = new File(path);
            String absolutePath = file.getAbsolutePath();
            JsonStateReader readJson = objectMapper.readValue(new File(absolutePath), JsonStateReader.class);
            Map<String, Integer> userName_id = new HashMap<>();

            String connID = ConnectSystem().returnConnID();

            for(Map<String, String> registerMap: readJson.register)
            {
                String userName = registerMap.get("userName");
                String password = registerMap.get("password");
                String conn = this.ConnectSystem().returnConnID();
                Integer userID = Register(conn,userName,password).returnUserID();
                userName_id.put(userName, userID);
            }
            for (Map<String, String> loginMap : readJson.login) {
                String userName = loginMap.get("userName");
                Integer userID = userName_id.get(userName);
                String password = this.subscribers.get(userID).getPassword();

                connID = Login(connID, userName, password).returnConnID();

                Map<String, Integer> storeName_id = new HashMap<>();
                for (Map<String, String> openStoreMap : readJson.open_store) {
                    String userNameOpenStore = openStoreMap.get("userName");
                    if (userName.equals(userNameOpenStore)) {
                        String storeName = openStoreMap.get("storeName");
                        Integer storeID = AddStore(userID, connID, storeName).returnStoreID();
                        storeName_id.put(storeName, storeID);
                    }
                }
                if(readJson.add_item != null) {
                    for (Map<String, Object> addItemMap : readJson.add_item) {
                        String userNameOpenStore = (String) addItemMap.get("userName");
                        if (userName.equals(userNameOpenStore)) {
                            String storeName = (String) addItemMap.get("storeName");
                            Integer storeID = storeName_id.get(storeName);
                            String productName = (String) addItemMap.get("productName");
                            String category = (String) addItemMap.get("category");
                            Double price = (Double) addItemMap.get("price");
                            Integer quantity = (Integer) addItemMap.get("quantity");
                            AddProductToStore(userID, connID, storeID, productName, category, price, quantity);
                        }
                    }
                }
                if(readJson.add_owner != null) {
                    for (Map<String, Object> addOwnerMap : readJson.add_owner) {
                        String owner = (String) addOwnerMap.get("owner");
                        if (userName.equals(owner)) {
                            String newOwner = (String) addOwnerMap.get("newOwner");
                            Integer newOwnerID = userName_id.get(newOwner);
                            String storeName = (String) addOwnerMap.get("storeName");
                            Integer storeID = this.getStoreIDByName(storeName);
                            AddNewOwner(userID, connID, storeID, newOwnerID);
                        }
                    }
                }
                connID = Logout(connID).returnConnID();

            }
            Exit(connID);
        }
        catch (Exception e){
            TradingSystemApplication.WriteToLogger("There is a problem with the External file");
            System.exit(1);
        }


//        int userID = 1;
//        String connID = "479f239c-797c-4bdb-8175-980acaabf070";
//        this.connectedSubscribers.put(connID, userID);
//        AddStore(userID, connID, "store1");
//        AddStore(userID, connID, "Mar y juana");
//        AddStore(userID, connID, "Roee Hadas");
//        AddProductToStore(userID,connID,1,"prod1","sport", 7.0, 7 );
//        AddProductToStore(userID, connID,1, "Sneakers2", "Shoes",50.0, 25);
//        AddProductToStore(userID, connID, 1,"Sneaker3", "bla" ,80.0, 25);
//        AddProductToStore(userID, connID, 2,"Sneakers24",  "Shoes", 80.0,25);
//        AddProductToStore(userID, connID, 2, "Sneak23", "bloo", 840.0, 25);
//        AddProductToStore(userID, connID, 2,"Sneakers",  "Shoes",80.0, 25);
//        AddProductToStore(userID, connID, 3,"Sneakers2", "Shoes", 50.0, 25);
//        AddProductToStore(userID, connID, 3,"Sneaker3", "bla" , 80.0,25);
//        AddProductToStore(userID, connID, 3,"Sneakers24",  "Shoes", 80.0,25);
//        AddProductToStore(userID, connID, 1, "Sneak23",  "bloo",840.0, 25);
//        AddProductToStore(userID, connID, 2,"Sneakers",  "Shoes", 80.0,25);
//        AddProductToStore(userID,connID,1,"Sneak","Shos", 52.0, 2 );
//        AddProductToStore(userID,connID,2,"Sneak","Shos", 52.0, 2 );
//
//        User user1 = new User("hadass", "1234");
//        userID = user1.getId();
//        this.subscribers.put(userID, user1);
//        connID = "38095a9d-09dd-41ec-bd04-3a6d0da1c386";
//        this.connectedSubscribers.put(connID, userID);
//
//        this.connectedSubscribers = new ConcurrentHashMap<>();
        printUsers();
    }

    public void LoadTestInfo(){
        for(int i=1; i<=1000; i++)
        {
            String connID = this.ConnectSystem().returnConnID();
            Integer userID = this.Register(connID,"user"+i,"123").returnUserID();
            connID = this.Login(connID,"user"+i,"123").returnConnID();
            this.AddStore(userID,connID,"store"+i);
            int storeID = this.getStoreIDByName("store"+i);
            for(int j=1; j<=1000; j++)
            {
                this.AddProductToStore(userID,connID,storeID,"product"+i,"products",10.0,10);
            }
        }

    }

    public void AddStoreOwnerPermission(){
        for (User user: this.subscribers.values()){
            for (OwnerPermission permission: user.getOwnerPermission().values()){
                int storeID = permission.getStoreId();
                if (this.stores.containsKey(storeID)){
                    Store store = this.stores.get(storeID);
                    store.addOwnerPermission(permission.getUserId(), permission);
                }
            }
        }
    }

    public void AddStoreManagerPermission(){
        for (User user: this.subscribers.values()){
            for (ManagerPermission permission: user.getManagerPermission().values()){
                int storeID = permission.getStoreId();
                if (this.stores.containsKey(storeID)){
                    Store store = this.stores.get(storeID);
                    store.addManagerPermission(permission);
                }
            }
        }
    }
    public void AddStoreHistory(){
        for (User user: this.subscribers.values()){
            for (ShoppingHistory history: user.getShoppingHistory()){
                int storeID = history.getStoreID();
                if (this.stores.containsKey(storeID)){
                    Store store = this.stores.get(storeID);
                    store.addHistory(history);
                }
            }
        }
    }


    public void AddSubscriberIfNotExist(User user){
        subscribers.putIfAbsent(user.getId(),user);
    }

    public void AddStoreIfNotExist(Store store){
        stores.putIfAbsent(store.getId(),store);
    }

    public void AddOwnerIfNotExist(int ownerID, int storeID){
        Store store = stores.get(storeID);
        User owner = subscribers.get(ownerID);
        store.AddOwnerIfNotExist(ownerID);
        owner.AddOwnerStoresIfNotExist(storeID);
    }

    public void AddManagerIfNotExist(int managerID, int storeID){
        Store store = stores.get(storeID);
        User manager = subscribers.get(managerID);
        store.AddManagerIfNotExist(managerID);
        manager.AddManagerStoresIfNotExist(storeID);
    }

    public void UploadBidToStore(int storeID, DataBid dataBid) {
        if(stores.containsKey(storeID)){
            Store store = stores.get(storeID);
            store.UploadBidToStore(dataBid);
        }
    }

    public void AddStoreProductIfNotExist(Integer storeID, Product product){
        Store store = stores.get(storeID);
        store.AddStoreProductIfNotExist(product);
    }

    public void AddShoppingBagIfNotExist(ShoppingBag shoppingBag){
        User user = subscribers.get(shoppingBag.getUserID());
        user.AddShoppingBagIfNotExist(shoppingBag);
    }

    public ConcurrentHashMap<Integer, User> getSubscribers() {
        return subscribers;
    }

    public ConcurrentHashMap<Integer, Store> getStores() {
        return stores;
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
        if(!isUploadAllData){
            addFromDb.UploadAllData();
            isUploadAllData = true;
            printUsers();
            printStores();
            printProducts();
        }
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
            return new Response(true, "Register Error: error in connID");
        }
        else{
            if (validation.IsUserNameExist(userName)) { 
                return new Response(true, "Register Error: user name is taken");
            }
            Response response;
            try{
                //Adds to the db
                response= data_controller.AddSubscriber(userName, password);
            }catch (Exception e){
                return new Response(true, "Error In DB!");
            }
            if(response.getIsErr()){
                return response;
            }
            int userID=response.returnUserID();
            User newUser = new User(userID, userName, password);
            subscribers.put(newUser.getId(), newUser);
        //    Response res = new Response(false,"Register: Registration of " + userName + " was successful");
            response.AddConnID(connID);
        //    res.AddUserID(newUser.getId());
            response.AddUserGuest();
            return response;
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
        System.out.println("--------------Login--------------");
        Response response = validation.ValidPassword(userName, password);
        if (response.getIsErr())
            return response;
        User myGuest = guests.get(guestConnID);
        User myUser = subscribers.get(response.returnUserID());
        //TODO: not working with DB
//        myUser.mergeToMyCart(myGuest.getShoppingCart());
        String connID = connectSubscriberToSystemConnID(response.returnUserID());
        guests.remove(guestConnID);
//        myUser.updateAfterLogin();
        Response res = new Response(false, "Login: Login of user " + userName + " was successful");
        res.AddUserID(response.returnUserID());
        res.AddConnID(connID);
        res.AddUserSubscriber(myUser.isManaged(), myUser.isOwner(), myUser.isFounder(),systemAdmins.containsKey(myUser.getId()));
        sendAlert(myUser, res);
        return res;
    }

    //Observer
    private void sendAlert(User user, Response res){
//        Publisher publisher = new Publisher();
//        user.setPublisher(publisher);
        user.update(res);
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
        System.out.println(!res.getIsErr());
        if(!res.getIsErr()){
            User myUser = subscribers.get(res.returnUserID());
            myUser.setPublisher(publisher);
            List<String> notConnectedMessages = myUser.updateAfterLogin();
//            List<String> notConnectedMessages = new ArrayList<>();
            res.AddPair("messages", notConnectedMessages);
            System.out.println(res.getReturnObject().get("messages"));
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
         //TODO check if valid
//        if(name==null){
//            dummyProducts=productService.findDummyProductByCategory(category, minprice,maxprice);
//        }
//        else if(category==null){
//            dummyProducts=productService.findDummyProductByName(name, minprice,maxprice);
//        }
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
        System.out.println("---------------------AddProductToCart----------------------");
        System.out.println("---------------------AddProductToCart----------------------");
        if(guests.containsKey(connID)){
            User myGuest= guests.get(connID);
            Response response = myGuest.AddProductToCart(StoreId, productId, quantity);
            Response res = response;
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
     * @param address
     * @param city
     * @param country
     * @param zip
     * @return Response{
     *      "isErr: boolean
     *      "message": String
     *      "connID": String
     *      }
     */
    public Response guestPurchase(String connID, String name, String credit_number, String month, String year, String cvv, String ID, String address, String city, String country, String zip){
        if(!guests.containsKey(connID)){
            return new Response(true, "guestPurchase: The user is not connected to the system");
        }
        else {
            User myGuest= guests.get(connID);
            Collection<ShoppingBag> shoppingBags = myGuest.getShoppingCart().getShoppingBags().values();
            Response res = myGuest.guestPurchase(name, credit_number, month, year, cvv, ID, address,city,country,zip);
            if(!res.getIsErr())
            {
                for (ShoppingBag bag:shoppingBags){
                    Store store = this.stores.get(bag.getStoreID());
                    List<Integer> productsID = bag.getProductsList();
                    String productsList = makeProductsList(store.getId(), productsID);
                    Response resAlert = new Response(false, "The guest " + name +
                            " has been purchased the products: " + productsList + " from your store: " + store.getName());
                    store.sendAlertToOwners(resAlert);
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
     * @param address
     * @param city
     * @param country
     * @param zip
     * @return Response{
     *      "isErr: boolean
     *      "message": String
     *      "connID": String
     *      }
     */
    public Response subscriberPurchase(int userID, String connID, String credit_number, String month, String year, String cvv, String ID, String address, String city, String country, String zip){
        if(!ValidConnectedUser(userID, connID)){
            return new Response(true, "subscriberPurchase: The user is not connected to the system");
        }
        else {
            User user = subscribers.get(userID);
            Collection<ShoppingBag> shoppingBags = user.getShoppingCart().getShoppingBags().values();
            Response res = user.subscriberPurchase(credit_number, month, year, cvv, ID, address,city,country,zip);
            if(!res.getIsErr())
            {
                for (ShoppingBag bag:shoppingBags){
                    Store store = this.stores.get(bag.getStoreID());
                    List<Integer> productsID = bag.getProductsList();
                    String productsList = makeProductsList(store.getId(), productsID);
                    List<Integer> specialProductsID = bag.getSpecialProductProductsList();
                    String specialProductsList = makeProductsList(store.getId(), specialProductsID);
                    Response resAlert = new Response(false, "The client " + user.getUserName() +
                            " has been purchased the products: " + productsList +" "+specialProductsList+ " from your store: " + store.getName());
                    store.sendAlertToOwners(resAlert);
                }
            }
            res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
            return res;

        }
    }

    public String getUserConnID(Integer userID){
        for (String connID: this.connectedSubscribers.keySet()){
            int id = this.connectedSubscribers.get(connID);
            if(id == userID){
                return connID;
            }
        }
        return "";
    }

    public Integer getUserID(String name){
        for(User user : subscribers.values()){
            if(user.getUserName().equals(name))
                return user.getId();
        }
        return -1;
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
//            TODO: maybe to add
//            newGuest.setShoppingCart(new ShoppingCart( myUser.getShoppingCart()));
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
                Response response;
                try{
                    //Adds to the db
                    response = data_controller.AddStore(storeName, userID);
                }catch (Exception e){
                    return new Response(true, "Error In DB!");
                }
                if(response.getIsErr()){
                    return response;
                }
                Integer storeID=response.returnStoreID();
                try{
                    response= data_controller.AddNewOwner(storeID, userID, new OwnerPermission(userID, storeID));
                }catch (Exception e){
                    return new Response(true, "Error In DB!");
                }
                if(response.getIsErr()){
                    return response;
                }

                Store newStore = new Store(storeID, storeName, userID);
                User user = subscribers.get(userID);
                user.AddStore(newStore.getId());
                newStore.addOwnerPermission(userID,user.getOwnerPermission(storeID));
                stores.put(newStore.getId(),newStore);
                Response res = new Response( "AddStore: Add store " + storeName + " was successful");
                res.AddPair("storeID", newStore.getId());
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
        stores.get(storeId).WriteComment(userId,productId,comment);
        Product product = stores.get(storeId).getProduct(productId);
        //product.addComment(userId, comment);
        String storeName = stores.get(storeId).getName();

        Response resAlert = new Response(false, "There is a new comment on your product: " + product.getProductName() +
                " from the store: " + storeName);
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
            User user = subscribers.get(userID);
            List<DummyShoppingHistory> list = user.ShowUserHistory();
            if(list.isEmpty()){
                Response res = new Response(true,"ShowSubscriberHistory: There are no older shopping in the history of user with id " + userID);
                res.AddPair("history", list);
                return res;
            }
            Response res = new Response(false, "ShowSubscriberHistory: Num of history buying of the user is " + list.size());
            res.AddPair("history", list);
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
            if(!this.hasPermission(userID,storeID, PermissionEnum.Permission.AddProduct)){
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
                        Integer productID = stores.get(storeID).getProductID(productName);
                        User user = subscribers.get(userID);
                        res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
                        res.AddPair("productID", productID);
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
            if(!hasPermission(userID,storeID, PermissionEnum.Permission.DeleteProduct)){
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
            if(!hasPermission(userID,storeID, PermissionEnum.Permission.AddProduct)){
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
            if(!hasPermission(userID,storeID, PermissionEnum.Permission.AddProduct)){
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

        Response res1 = this.systemRoleChecks(userID, storeID, newOwner, PermissionEnum.Permission.AppointmentOwner);
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
        Response response;
        try{
            //Adds to the db
            response= data_controller.AddNewOwner(storeID, newOwner, OP);
        }catch (Exception e){
            return new Response(true, "Error In DB!");
        }
        if(response.getIsErr()){
            return response;
        }

        NO.AddStoreInOwner(storeID, OP);
        Store store = stores.get(storeID);
        store.addNewOwner(userID, newOwner);
        store.addOwnerPermission(newOwner,OP);
        //Alert
        String ownerName = this.subscribers.get(userID).getUserName();
        Response resAlert = new Response(false, ownerName + " appointed you to own the store: " + store.getName());
        store.sendAlert(newOwner, resAlert);
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
            Response response;
            try{
                response= data_controller.RemoveOwner(storeID, removeOwnerID);
            }catch (Exception e){
                return new Response(true, "Error In DB!");
            }
            if(response.getIsErr()){
                return response;
            }

            User owner=subscribers.get(ownerID);
            Store store = stores.get(storeID);
            store.removeOwner(removeOwnerID);
            subscribers.get(removeOwnerID).removeOwnedStore(storeID);

            //alert the removed owner
            String storeName = store.getName();
            Response resAlert = new Response(false, owner.getUserName() + " removed you from owning the store: " + storeName);
            store.sendAlert(removeOwnerID, resAlert);

            ConcurrentHashMap<Integer,OwnerPermission> ownerPermissionHashMap=stores.get(storeID).getOwnersIDs();
            ConcurrentHashMap<Integer,ManagerPermission> managerPermissionHashMap= stores.get(storeID).getManagerIDs();
            for(OwnerPermission permission: ownerPermissionHashMap.values()){
                if(permission.getAppointmentId()==removeOwnerID) {
                    stores.get(storeID).removeOwner(permission.getUserId());
                    subscribers.get(permission.getUserId()).removeOwnedStore(storeID);
                    //alert the removed owner
                    store.sendAlert(permission.getAppointmentId(), resAlert);
                }
            }
            for(ManagerPermission permission: managerPermissionHashMap.values()){
                if(permission.getAppointmentId()==removeOwnerID)
                    stores.get(storeID).removeManager(permission.getUserId());
            }
        }
        User user=subscribers.get(ownerID);
        Response res = new Response(false, "RemoveOwnerByOwner: Successfully removed the owner " + removeOwnerID);
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

        Response res1 = this.systemRoleChecks(userID, storeID, newManager, PermissionEnum.Permission.AppointmentManager);
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
        Response response;
        try{
            //Adds to the db
            response= data_controller.AddNewManager(storeID, newManager, MP);
        }catch (Exception e){
            return new Response(true, "Error In DB!");
        }
        if(response.getIsErr()){
            return response;
        }

        NM.AddStoreInManager(storeID, MP);
        stores.get(storeID).addNewManager(userID, newManager);
        stores.get(storeID).addManagerPermission(MP);
        NM.unlockUser();
        Response res = new Response(false, "AddNewManager: The manager Added successfully");
        User user = subscribers.get(userID);
        res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));

        //Alert
        String ownerName = this.subscribers.get(userID).getUserName();
        Response resAlert = new Response(false, ownerName + " appointed you to be manager of the store: " + stores.get(storeID).getName());
        stores.get(storeID).sendAlert(newManager, resAlert);

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
    public Response EditManagerPermissions(int userID, String connID, int storeID, int managerID, List<PermissionEnum.Permission> permissions) {
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "EditManagerPermissions: The user" + userID + "is not connected");
        }
        if (this.subscribers.get(managerID) == null) {
            return new Response(true, "EditManagerPermissions: The user " + managerID + " is not subscriber, so it impossible to edit his Permissions");
        }

        //TODO add synchronize
        User MTE = this.subscribers.get(managerID);
        Response res1 = this.systemRoleChecks(userID, storeID, managerID, PermissionEnum.Permission.EditManagerPermission);
        if (res1.getIsErr()) {
            //MTR.unlockUser();
            return res1;
        }
        Response res2 =MTE.AbleToEditPermissions(userID, storeID);
        if (res2.getIsErr()) {
            //MTR.unlockUser();
            return res2;
        }

        Response response;
        try{
            //Adds to the db
            response= data_controller.EditManagerPermissions(storeID, managerID, permissions);
        }catch (Exception e){
            return new Response(true, "Error In DB!");
        }
        if(response.getIsErr())
            return response;

        MTE.editPermissions(userID,storeID,permissions);
        stores.get(storeID).editManagerPermissions(userID, managerID,permissions);
        //NM.unlockUser();

        //Alert update Permissions
        String Permissions="";
        for (PermissionEnum.Permission p:permissions
             ) {
            Permissions=Permissions+ p.toString()+", ";
        }
        Response resAlert=new Response("your permission for store "+ storeID+" changed. \n"+
                    " You are now allowed to- \n"+
                      Permissions.substring(0,Permissions.length()-1));
        stores.get(storeID).sendAlert(managerID,resAlert);
        User user=subscribers.get(userID);
        Response res = new Response(false, "EditManagerPermissions:: The permissions of manager " + managerID + " edit successfully");
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

        Response res1 = this.systemRoleChecks(userID, storeID, ManagerToRemove, PermissionEnum.Permission.RemoveManager);
        if (res1.getIsErr()) {
            MTR.unlockUser();
            return res1;
        }
        Response res2 =MTR.AbleToRemoveManager(userID, storeID);
        if (res2.getIsErr()) {
            MTR.unlockUser();
            return res2;
        }
        Response response;
        try{
            data_controller.RemoveManager(storeID, ManagerToRemove);
        }catch (Exception e){
            return new Response(true, "Error In DB!");
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
    public Response GetPossiblePermissionsToManager(int userID, String connID, int storeID) {
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "GetPossiblePermissionsToManager: The user " + userID + " is not connected");
        }
        User user=subscribers.get(userID);
        if(user==null) {
            return new Response(true, "GetPossiblePermissionsToManager: The user " + userID + " is not in the list from some reason");
        }
        ManagerPermission MP= user.getManagerPermission(storeID);
        Response res = new Response(false, "GetPossiblePermissionsToManager: Viewing permissions was successful");
        if(MP!=null){
            res.AddPair("permissions", MP.getPermissions());
        }
       else {
            res.AddPair("permissions", new LinkedList<PermissionEnum.Permission>());
        }
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
        if (!hasPermission(userID, storeID, PermissionEnum.Permission.GetStoreHistory)) {
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
        if (!hasPermission(AdminID, storeID, PermissionEnum.Permission.GetHistoryPurchasing)) {
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
        if (!hasPermission(AdminID, PermissionEnum.Permission.GetHistoryPurchasing)) {
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
        if (!hasPermission(AdminID, PermissionEnum.Permission.GetHistoryPurchasing)) {
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
       // List<DummyShoppingHistory> list1= shoppingHistoryService.getAllShoppingHistory();
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
        if (!hasPermission(AdminID, PermissionEnum.Permission.GetHistoryPurchasing)) {
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
    public boolean hasPermission(int userID, int storeID, PermissionEnum.Permission p) {
        boolean hasPer=false;
        if(this.subscribers.containsKey(userID)){
            User u=this.subscribers.get(userID);
            if(!hasPer && u.getOwnerPermission(storeID)!=null){
                hasPer = u.getOwnerPermission(storeID).hasPermission(p);
            }
            if (!hasPer && u.getManagerPermission(storeID)!=null){
                hasPer = u.getManagerPermission(storeID).hasPermission(p);
            }
            if(!hasPer && this.systemManagerPermissions.get(userID)!=null){
                hasPer = this.systemManagerPermissions.get(userID).hasPermission(p);
            }
            if(u.getMyFoundedStoresIDs().contains(storeID)){
                hasPer = true;
            }
        }
        return hasPer ;
    }

    public boolean hasPermission(int userID, PermissionEnum.Permission p) {
        //if (this.subscribers.containsKey(userID)) {
          //  User u = this.subscribers.get(userID);
            if (this.systemManagerPermissions.get(userID) != null) {
                return this.systemManagerPermissions.get(userID).hasPermission(p);
            }
        //}
        return false;
    }

    public void addHistoryToStoreAndUser(ShoppingHistory sh, boolean isGuest) {
        this.stores.get(sh.getStoreID()).addHistory(sh);
        if (!isGuest)
            this.subscribers.get(sh.getUserID()).addHistory(sh);
    }

    public Response systemRoleChecks(int userID, int storeID, int newRole, PermissionEnum.Permission permission) {
        if (!this.subscribers.containsKey(userID)) {
            return new Response(true, "The user "+userID+" is not subscriber, so he can not appoint manager for store");
        }
        if (!this.subscribers.containsKey(newRole)) {
            return new Response(true, "User "+newRole+" is not subscriber, so it impossible to "+permission.toString()+" him for store");
        }
        if (this.subscribers.get(userID).getMyFoundedStoresIDs().contains(storeID)){
            return new Response(false,"Sys OK");
        }
        //TODO: check this
//        if (!this.subscribers.get(userID).getMyFoundedStoresIDs().contains(storeID) && !this.subscribers.get(userID).getMyOwnerStore().contains(storeID)){
//            return new Response(true, "User "+userID+" is not the owner of the store, so he can not "+permission.toString()+" to the store");
//        }
//        if (!this.subscribers.get(userID).getMyOwnerStore().contains(storeID)){
//            return new Response(true, "User "+userID+" is not the owner of the store, so he can not "+permission.toString()+" to the store");
//        }
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

    public void cancelReduceProducts(Integer storeID, ConcurrentHashMap<Integer, Integer> products) {
      this.stores.get(storeID).cancelReduceProducts(products);
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
        if(this.subscribers.containsKey(userID))
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

    public PermissionEnum.Permission changeToPermission(String per){
        switch (per){
            case "AddProduct":
                return PermissionEnum.Permission.AddProduct;
            case "ReduceProduct":
                return PermissionEnum.Permission.ReduceProduct;
            case "DeleteProduct":
                return PermissionEnum.Permission.DeleteProduct;
            case "EditProduct":
                return PermissionEnum.Permission.EditProduct;
            case "AppointmentOwner":
                return PermissionEnum.Permission.AppointmentOwner;
            case "AppointmentManager":
                return PermissionEnum.Permission.AppointmentManager;
            case "EditManagerPermission":
                return PermissionEnum.Permission.EditManagerPermission;
            case "RemoveManager":
                return PermissionEnum.Permission.RemoveManager;
            case "GetInfoOfficials":
                return PermissionEnum.Permission.GetInfoOfficials;
            case "GetInfoRequests":
                return PermissionEnum.Permission.GetInfoRequests;
            case "ResponseRequests":
                return PermissionEnum.Permission.ResponseRequests;
            case "GetHistoryPurchasing":
                return PermissionEnum.Permission.GetHistoryPurchasing;
            case "GetStoreHistory":
                return PermissionEnum.Permission.GetStoreHistory;
            case "GetDailyIncomeForStore":
                return PermissionEnum.Permission.GetDailyIncomeForStore;
            case "GetDailyIncomeForSystem":
                return PermissionEnum.Permission.GetDailyIncomeForSystem;
            case "RequestBidding":
                return PermissionEnum.Permission.RequestBidding;
            case "EditDiscountPolicy":
                return PermissionEnum.Permission.EditDiscountPolicy;
            case "EditBuyingPolicy":
                return PermissionEnum.Permission.EditBuyingPolicy;
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
        Response response = checkPermissionToPolicy(userID, connID, storeID, PermissionEnum.Permission.EditDiscountPolicy);
        Response res = response;
        if(res.getIsErr()){
            return res;
        }
        Store s=this.stores.get(storeID);
        Response r=sale.checkValidity(storeID);
        if(r.getIsErr()){
            return r;
        }
        DiscountPolicy d=new DiscountPolicy(storeID,sale);
        if(this.tmpDiscountPolicyForStore.containsKey(storeID)){
            this.tmpDiscountPolicyForStore.remove(storeID);
        }
        s.setDiscountPolicy(d);
//        DBSale parent=new DBSale(sale,null);
//        DataStore store=data_controller.findStorebyId(storeID).returnDataStore();
        try{
            res= data_controller.AddDiscountPolicy(storeID,sale);
        }catch (Exception e){
            return new Response(true, "Error In DB!");
        }
        if(res.getIsErr())
            return res;
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
        if(saleName.equals("XorComposite")){
            LinkedList<Sale> list=new LinkedList<Sale>();
            XorComposite sale=new XorComposite(list);
            for (String mapKey:o.keySet()
            ) {
                Map<String, Object> tosend = new HashMap<>();
                tosend.put(mapKey,(Map<String, Object>) o.get(mapKey));
                CreateDiscountPolicy(storeID,sale,tosend);
            }
            sale.setDes(new Cheaper());
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
                    Decision dec=new Cheaper();
                    for (String mapKey:map0.keySet()
                    ) {
                        Map<String, Object> tosend = new HashMap<>();
                        tosend.put(mapKey,(Map<String, Object>) map0.get(mapKey));
                        CreateDiscountPolicy(storeID,xorComposite,tosend);
                    }
                    xorComposite.setDes(dec);
                    sale.setSale(xorComposite);
                    return xorComposite;
                case "StoreSale":
                    Map<String, Object> map3=(Map<String, Object>)o.get("StoreSale");
                    Integer discount1=(Integer) map3.get("discount");
                    Map<String, Object> exp1= (Map<String, Object>)map3.get("expression");
                    StoreSale storeSale=new StoreSale(storeID,discount1);
                    Expression e1=createSaleExp(storeID,exp1);
                    storeSale.setExpression(e1);
                    sale.setSale(storeSale);
                    return sale;
                case "ProductSale":
                    Map<String, Object> map4=(Map<String, Object>)o.get("ProductSale");
                    Integer productID=(Integer)map4.get("productID");
                    Integer discoun2=(Integer)map4.get("discount");
                    Map<String, Object> exp2= (Map<String, Object>)map4.get("expression");
                    ProductSale productSale=new ProductSale(productID,discoun2);
                    Expression e2=createSaleExp(storeID,exp2);
                    productSale.setExpression(e2);
                    sale.setSale(productSale);
                    return sale;
                case "CategorySale":
                    Map<String, Object> map5=(Map<String, Object>)o.get("CategorySale");
                    String category=(String) map5.get("category");
                    Integer discoun3=(Integer)map5.get("discount");
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
                    Integer price = (Integer) map6.get("priceForSale");
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

    private Response checkPermissionToPolicy(int userID, String connID, int storeID, PermissionEnum.Permission p){
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "Error in Admin details");
        }
        if (!subscribers.containsKey(userID)) {
            return new Response(true, "the user is not subscriber to the system");
        }
        if(stores.get(storeID)==null){
            return new Response(true, "the store not exist in the system");
        }
//        if(!stores.get(storeID).checkOwner(userID)){
//            return new Response(true, "the user is not the owner of the store");
//        }
        if(!hasPermission(userID,storeID,p)){
            return new Response(true, "the user does not have permission to do that");
        }
        return new Response(false, "");
    }

    @Override
    public Response addBuyingPolicy(int userID, String connID, int storeID, Expression exp){
        Response response = checkPermissionToPolicy(userID, connID, storeID, PermissionEnum.Permission.EditBuyingPolicy);
        Response res = response;
        if(res.getIsErr()){
            return res;
        }
        Response r=exp.checkValidity(storeID);
        if(r.getIsErr()){
            return r;
        }
        Store s=this.stores.get(storeID);
        BuyingPolicy b=new BuyingPolicy(storeID,exp);
        if(this.tmpBuyingPolicyForStore.containsKey(storeID)){
            this.tmpBuyingPolicyForStore.remove(storeID);
        }
        s.setBuyingPolicy(b);
        //ADD to db
//        DBExpression parent=new DBExpression(exp,null);
//        DataStore store=data_controller.findStorebyId(storeID).returnDataStore();
        try{
            res = data_controller.AddBuyingPolicy(storeID,exp);
        }catch (Exception e){
            return new Response(true, "Error In DB!");
        }
        if(res.getIsErr()){
            return res;
        }
        return new Response("Buying Policy added successes");
    }

    public Response GetPoliciesInfo(int userID, int storeID, String connID){
        Response res = checkPermissionToPolicy(userID, connID, storeID, PermissionEnum.Permission.GetInfoOfficials);
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
                    Expression e1 = createLimitExp(storeID, cond);
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
/*
    private ConditionRole createConditingRole(int storeID, Map<String, Object> cond) {
        String role=(String) cond.get("Role");
        if(role.equals("ExistProduct")){
           Integer productId=(Integer) cond.get("productID");
            return new ExistProduct(productId);
        }
        return null;
    }
*/
    //for the tests
    public void AddStoreToList(Store store) {
        this.stores.put(store.getId(), store);
    }


    public Response RemoveBuyingPolicy(int userID, int storeID, String connID) {
        Response response = checkPermissionToPolicy(userID, connID, storeID, PermissionEnum.Permission.EditDiscountPolicy);
        Response res = response;
        if(res.getIsErr()){
            return res;
        }

        this.tmpBuyingPolicyForStore.remove(storeID);

        return  stores.get(storeID).RemoveBuyingPolicy();
    }

    public Response RemoveDiscountPolicy(int userID, int storeID, String connID) {
        Response response = checkPermissionToPolicy(userID, connID, storeID, PermissionEnum.Permission.EditDiscountPolicy);
        Response res = response;
        if(res.getIsErr()){
            return res;
        }
        this.tmpDiscountPolicyForStore.remove(storeID);
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

    private String makeProductsList(Integer storeID, List<Integer> productsID){
        String output = "";
        for(Integer ID : productsID){
            Product product = this.stores.get(storeID).getProduct(ID);
            String productName = product.getProductName();
            output = output + productName + ", ";
        }
        if(output.length()>2){
            output = output.substring(0, output.length()-2);
        }
        return output;
    }

    public Response GetAllSubscribers(String connID, int userID) {
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "Error in Subscriber details");
        }

        List<DummySubscriber> dummySubscribers = new ArrayList<>();
        for(Integer id : this.subscribers.keySet()) {
            User u = this.subscribers.get(id);
            DummySubscriber dummySubscriber = new DummySubscriber(u.getId(), u.getUserName());
            dummySubscribers.add(dummySubscriber);
        }
        Response res = new Response("Get All Subscribers succeed");
        res.AddPair("subscribers", dummySubscribers);
        return res;
    }

    /**
     * @requirement 4.12
     *
     * @param userID: int
     * @param storeID: int
     * @param connID: String
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "DailyIncome": {[Double]}
     *  }
     * }
     */
    @Override
    public Response getDailyIncomeForStore(int userID, int storeID, String connID) {
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "getDailyIncomeForStore: The user " + userID + " is not connected");
        }
        if(this.subscribers.get(userID)==null){
            return new Response(true, "getDailyIncomeForStore: The user "+userID+" is not in the list");
        }
        if(!stores.containsKey(storeID)){
            return new Response(true, "getDailyIncomeForStore: The store " + storeID + " doesn't exist in the system");
        }
        Store store=this.stores.get(storeID);
        if(store==null){
            return new Response(true, "getDailyIncomeForStore: The user "+userID+" try to get the daily income for store that not in the system ");
        }
        if(!this.hasPermission(userID,storeID, PermissionEnum.Permission.GetDailyIncomeForStore)){
            return new Response(true, "getDailyIncomeForStore: The user " + userID + " has no permissions to see this information");
        }
        Double DailyIncome=store.getDailyIncome();
        Response res =new Response(false, "the income can be displayed");
        res.AddPair("DailyIncome", DailyIncome);
        return res;
    }

    /**
     * @requirement 6.6
     *
     * @param userID: int
     * @param connID: String
     * @return Response {
     *  "isErr: boolean
     *  "message": String
     *  "connID: String
     *  "DailyIncome": {[Double]}
     */
    @Override
    public Response getDailyIncomeForSystem(int userID, String connID) {
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "getDailyIncomeForSystem: The user " + userID + " is not connected");
        }
        if(this.subscribers.get(userID)==null){
            return new Response(true, "getDailyIncomeForSystem: The user "+userID+" is not in the list");
        }
        if(!this.systemAdmins.keySet().contains(userID)){
            return new Response(true, "getDailyIncomeForSystem: The user "+userID+"  try to see the Daily Income for the system but he is not the admin of the system");
        }
        if(!this.hasPermission(userID, PermissionEnum.Permission.GetDailyIncomeForSystem)){
            return new Response(true, "getDailyIncomeForSystem: The user " + userID + " has no permissions to see this information");
        }
        Double DailyIncome=0.0;
        for (Integer key:this.stores.keySet()
        ) {
            DailyIncome = DailyIncome+this.stores.get(key).getDailyIncome();
        }
        Response res =new Response(false, "the income can be displayed");
        res.AddPair("DailyIncome", DailyIncome);
        return res;
    }


    @Override
    public Response subscriberBidding(int userID, String connID, int storeID, int productID, int productPrice, int quantity) {
       Response res=ableToSubscriberBid(userID, connID, storeID, productID, productPrice,quantity);
       if(!res.getIsErr()) {
           Store store=this.stores.get(storeID);
           if(store==null){
               return new Response(true, "subscriberBidding: The user "+userID+" try to submit a bid for store that not in the system");
           }
           store.AddBidForProduct(productID, userID, productPrice, quantity);
           Response resAlert = new Response(false, "The subscriber " + userID +
                   " has been submit a bid of " + productPrice + " for product: " + productID + " in your store: " + store.getName());
           store.sendAlertToOwners(resAlert);
           store.sendAlertOfBiddingToManager(resAlert);
           return new Response(false, "The bid was submitted successfully");
       }
       return res;
    }

    private Response ableToSubscriberBid(int userID, String connID, int storeID, int productID, int productPrice, int quantity){
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "subscriberBidding: The user " + userID + " is not connected");
        }
        User user=this.subscribers.get(userID);
        if(user==null){
            return new Response(true, "subscriberBidding: The user "+userID+" is not in the list of the subscriber");
        }
        Store store=this.stores.get(storeID);
        if(store==null){
            return new Response(true, "subscriberBidding: The user "+userID+" try to submit a bid for store that not in the system");
        }
        Product product=store.getProduct(productID);
        if(product==null){
            return new Response(true, "subscriberBidding: The user "+userID+" try to submit a bid for product " +productID +". but the product not in the store");
        }
        if(user.getShoppingCart().getShoppingBags().get(storeID)!=null&&
                user.getShoppingCart().getShoppingBags().get(storeID).getProducts()!=null&&
                user.getShoppingCart().getShoppingBags().get(storeID).getProducts().containsKey(productID)){
            return new Response(true, "subscriberBidding: The user "+userID+" try to submit a bid for product " +productID +" but the product exist in the bag already");
        }
        if(productPrice<=0||productPrice>product.getPrice()){
            return new Response(true, "subscriberBidding: The user "+userID+" try to submit a bid with price " +productPrice +" but it is not in the range: 0-"+product.getPrice());
        }
        if(quantity<=0){
            return new Response(true, "subscriberBidding: The user "+userID+" try to submit a bid with quantity " +quantity +" but it is not bigger then 0");
        }
        if(store.CheckBidForProductExist(userID,productID)){
            return new Response(true, "subscriberBidding: The user "+userID+" try to to submit a bid for product " +productID +" but this product already has a bid");
        }
        return new Response(false,"able");
    }

    @Override
    public Response ResponseForSubmissionBidding(int userID, String connID, int storeId, int productID, int productPrice, int userWhoOffer, int quantity, int mode) {
        Store store = this.stores.get(storeId);
        Response res = ableToResponseForSubmissionBid(userID, connID, storeId, store, productID, productPrice, userWhoOffer, quantity);
        if (!res.getIsErr()) {
            switch (mode) {
                case 0:
                    return store.refuseSubmissionBid(userID, userWhoOffer,productID);
                case 1:
                    return store.approveSubmissionBid(userID, userWhoOffer,productID);
                case 2: {
                    Product product = store.getProduct(productID);
                    Response r = checkValidTOChangeBid(userID, productID, quantity, productPrice, product);
                    if (!r.getIsErr()) {
                        return store.changeSubmissionBid(userID, userWhoOffer, productID, productPrice, quantity);
                    }
                    return r;
                }
            }
            return new Response(true, "ResponseForSubmissionBidding: The user " + userID + " try to response for submission bid for store (" + storeId + ") with unValid mode");
        }
        return res;
    }


    private Response checkValidTOChangeBid(int userID,int productID,int quantity,int productPrice, Product product){
        if(product==null){
            return new Response(true, "ResponseForSubmissionBidding: The user "+userID+" try to response for submission bid for product ("+productID+ ") that not in the store");
        }
            if (quantity <= 0) {
            return new Response(true, "ResponseForSubmissionBidding: The user " + userID + " try to response for submission bid with quantity " + quantity + " but it is not bigger then 0");
        }
        if (productPrice <= 0 || productPrice > product.getPrice()) {
            return new Response(true, "ResponseForSubmissionBidding: The user " + userID + " try to to response for submission bid with price " + productPrice + " but it is not in the range: 0-" + product.getPrice());
        }
        return new Response(false,"valid");
    }

    private Response ableToResponseForSubmissionBid(int userID, String connID, int storeId,Store store, int productID, int productPrice, int userWhoOffer, int quantity){
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "ResponseForSubmissionBidding: The user " + userID + " is not connected");
        }
        if(this.subscribers.get(userID)==null){
            return new Response(true, "ResponseForSubmissionBidding: The user "+userID+" is not in the subscriber");
        }
        if(this.subscribers.get(userWhoOffer)==null){
            return new Response(true, "ResponseForSubmissionBidding: The user "+userWhoOffer+" is not in the subscriber");
        }
        if(store==null){
            return new Response(true, "ResponseForSubmissionBidding: The user "+userID+" try to response for submission bid for store ("+storeId+ ") that not in the system");
        }
        if(store.getProduct(productID)==null){
            return new Response(true, "ResponseForSubmissionBidding: The user "+userID+" try to response for submission bid for product ("+productID+ ") that not in the store");
        }
        if(store.getBid(userWhoOffer,productID)==null){
            return new Response(true, "ResponseForSubmissionBidding: The user "+userID+" try to to response the submission bid for product " +productID +" and user "+userWhoOffer+" but the bidding not exist");
        }
        if(store.getBid(userWhoOffer,productID).isFinalState()){
            return new Response(true, "ResponseForSubmissionBidding: The user "+userID+" try to to response the submission bid for product " +productID +" and user "+userWhoOffer+" but the bidding has already been answered");
        }
        if(!this.hasPermission(userID,storeId,PermissionEnum.Permission.RequestBidding)){
            return new Response(true, "ResponseForSubmissionBidding: The user "+userID+" try to to response the submission bid for product " +productID +" and user "+userWhoOffer+" but he does not have permission to do so");
        }
        return new Response(false,"able");
    }

    @Override
    public Response ShowBids(int userID, String connID, int storeID) {
        if(!ValidConnectedUser(userID, connID)) {
            return new Response(true, "ShowBids: The User is not connected");
        }
        else if (!subscribers.containsKey(userID)){
            return new Response(true, "ShowBids: User is not subscriber");
        }
        else if (stores.get(storeID)==null){
            return new Response(true, "ShowBids: Store is not exist");
        }
        else{
            Store store = stores.get(storeID);
            List<DummyBid> list = new ArrayList<>();
            for (Bid bid: store.getBids()
                 ) {
                 list.add(new DummyBid(bid));
            }
            Response res = new Response(false, "ShowBids: Num of Bids in the store is " + list.size());
            res.AddPair("Bids", list);
            return res;
        }
    }

    @Override
    public Response ShowSpecialProductInShoppingCart(String connID) {
          if(connectedSubscribers.containsKey(connID)) {
            int userID = connectedSubscribers.get(connID);

            User user = subscribers.get(userID);
            List<DummyProduct> list = user.ShowSpecialProductInShoppingCart();
            Response res = new Response(false, "ShowSpecialProductInShoppingCart: Num of special products in my Shopping Cart is " + list.size());
            res.AddPair("products", list);
            res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
            return res;
        }
        else {
            return new Response(true, "ShowSpecialProductInShoppingCart: The user doesn't Exist");
        }
    }

    @Override
    public Response removeSpecialProductFromCart(String connID, int storeID, int productID) {
        if(connectedSubscribers.containsKey(connID)) {
            int userID = connectedSubscribers.get(connID);
            User user=subscribers.get(userID);
            Response res = user.removeSpecialProductFromCart(storeID,productID);
            res.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
            return res;
        }
        else {
            return new Response(true, "RemoveFromCart: The user is not Exist");
        }
    }

    @Override
    public Response GetAllManager(String connID, int stoerId) {
        List<DummySubscriber> dummySubscribers = new ArrayList<>();
        if(this.stores.get(stoerId)!=null) {
            for (Integer id : this.stores.get(stoerId).getManagerIDs().keySet()) {
                User u = this.subscribers.get(id);
                DummySubscriber dummySubscriber = new DummySubscriber(u.getId(), u.getUserName());
                dummySubscribers.add(dummySubscriber);
            }
        }
        Response res = new Response("Get All Subscribers succeed");
        res.AddPair("subscribers", dummySubscribers);
        return  res;
    }

    @Override
    public Integer getStoreIDByName(String storeName){
        for(Store s : stores.values())
        {
            if(s.getName().equals(storeName))
                return s.getId();
        }
        return -1;
    }

    @Override
    public Response ShowProductComments(String connID, int userID, int storeID) {
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "ShowProductComments: The user " + userID + " is not connected");
        }
        if(!stores.containsKey(storeID)){
            return new Response(true, "ShowProductComments: The store " + storeID + " doesn't exist in the system");
        }
        if(!stores.get(storeID).checkOwner(userID)){
            return new Response(true, "ShowProductComments: The user " + userID + " has no permissions to see this information");
        }
        else {
            String storeName = stores.get(storeID).getName();
            List<DummyComment> comments = new ArrayList<>();
            for(Product p : stores.get(storeID).getProducts())
            {
                List<String> list = p.getCommentsForProduct(p.getProductID());
                for(String s:list) {
                    DummyComment comment = new DummyComment(storeID, storeName, p.getProductID(), p.getProductName(), s, userID);
                    comments.add(comment);
                }
            }
            Response response = new Response(false, "ShowProductComments: Num of comments of the product: " + comments.size());
            response.AddPair("ConnId",connID);
            response.AddPair("comments", comments);
            User user=subscribers.get(userID);
            response.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
            return response;
        }
    }

    @Override
    public Integer getProductIDByName(String productName, int storeID) {
        for(Store s : stores.values())
        {
            if(s.getId() == storeID)
            {
                for(Product p : s.getProducts())
                {
                    if(p.getProductName().equals(productName))
                        return p.getProductID();
                }
            }
        }
        return -1;
    }

    //todo ____________________NewFunctions___________________

    @Override
    public Response ShowBuyingPolicyBuildingTree(String connID, int userID, int storeID){
        Response response = new Response(false, "ShowBuyingPolicyBuildingTree: success");
        response.AddPair("tree",this.tmpBuyingPolicyForStore.get(storeID));
        System.out.println("\n\n------ in TS ------\n");
        System.out.println(response);

        return response;
    }

    @Override
    public Response ShowDiscountPolicyBuildingTree(String connID, int userID, int storeID){
        Response response = new Response(false, "ShowBuyingPolicyBuildingTree: success");
        response.AddPair("tree",this.tmpDiscountPolicyForStore.get(storeID));
        System.out.println("\n\n------ in TS ------\n");
        System.out.println(response);

        return response;
    }

    @Override
    public Response AddNodeToBuildingTree(int userID, String connID, int storeID, int nodeID, int quantity, int productID, int maxQuantity, String category, int numOfProductsForSale, int priceForSale, int quantityForSale, int discount, int mode, String type) {
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "AddNodeToBuildingTree: The user " + userID + " is not connected");
        }
        if(this.subscribers.get(userID)==null){
            return new Response(true, "AddNodeToBuildingTree: The user "+userID+" is not in the list");
        }
        if(stores.get(storeID)==null){
            return new Response(true, "AddNodeToBuildingTree: The store "+storeID+" is not exist");
        }
        switch (mode){
           case  1:
            return createSaleNode(storeID, nodeID, quantity,productID, maxQuantity, category, numOfProductsForSale, priceForSale, quantityForSale, discount, type);
           case  2:
            return createExpNode(storeID, nodeID, productID, maxQuantity, category, type);
        }
        return new Response(true, "AddNodeToBuildingTree: The mode "+mode+" is not valid");
    }

    @Override
    public Response CloseDiscountPolicyTree(String connID, int userID, int storeID) {
        Response response = checkPermissionToPolicy(userID, connID, storeID, PermissionEnum.Permission.EditDiscountPolicy);
        Response res = response;
        if(res.getIsErr()){
            return res;
        }
        Sale sale= this.tmpDiscountPolicyForStore.get(storeID).closeSale();
        Store s=this.stores.get(storeID);
        Response r=sale.checkValidity(storeID);
        if(r.getIsErr()){
            return r;
        }
        DiscountPolicy discountPolicy=new DiscountPolicy(storeID,sale);
        s.setDiscountPolicy(discountPolicy);
        //this.tmpDiscountPolicyForStore.remove(storeID);
//        DBSale parent=new DBSale(sale,null);
//        DataStore store=data_controller.findStorebyId(storeID).returnDataStore();
        //TODO
//        res= data_controller.AddDiscountPolicy(storeID,sale);
//        if(res.getIsErr())
//            return res;
        return new Response("Discount Policy for store "+ storeID+" added successfully" );
    }

    @Override
    public Response CloseBuingPolicyTree(String connID, int userID, int storeID) {
        Response response = checkPermissionToPolicy(userID, connID, storeID, PermissionEnum.Permission.EditDiscountPolicy);
        Response res = response;
        if(res.getIsErr()){
            return res;
        }
        Expression exp= this.tmpBuyingPolicyForStore.get(storeID).closeExp();
        Store s=this.stores.get(storeID);
        Response r=exp.checkValidity(storeID);
        if(r.getIsErr()){
            return r;
        }
        BuyingPolicy buyingPolicy=new BuyingPolicy(storeID,exp);
        s.setBuyingPolicy(buyingPolicy);
      //  this.tmpBuyingPolicyForStore.remove(storeID);
//        DBSale parent=new DBSale(sale,null);
//        DataStore store=data_controller.findStorebyId(storeID).returnDataStore();
        try{
            res= data_controller.AddBuyingPolicy(storeID,exp);
        }catch (Exception e){
            return new Response(true, "Error In DB!");
        }
        if(res.getIsErr())
            return res;
        return new Response("Buying Policy for store "+ storeID+" added successfully" );
    }

    private Response createExpNode(int storeID, int nodeID,int productID, int maxQuantity, String category,String type) {
        switch (type) {
            case "OrComposite": {
                DummyOrExpression OrExp = new DummyOrExpression(stores.get(storeID).getNextExpressionID());
                if (this.tmpBuyingPolicyForStore.get(storeID) == null) {
                    this.tmpBuyingPolicyForStore.put(storeID, OrExp);
                } else {
                    this.tmpBuyingPolicyForStore.get(storeID).setExpression(nodeID, OrExp);
                }
                return new Response("createExpNode: The node OrComposite added successfully");
            }
            case "AndComposite": {
                DummyAndExpression AndExp = new DummyAndExpression(stores.get(storeID).getNextExpressionID());
                if (this.tmpBuyingPolicyForStore.get(storeID) == null) {
                    this.tmpBuyingPolicyForStore.put(storeID, AndExp);
                } else {
                    this.tmpBuyingPolicyForStore.get(storeID).setExpression(nodeID, AndExp);
                }
                return new Response("createExpNode: The node AndComposite added successfully");
            }
            case "QuantityLimitForProduct": {
                if (maxQuantity < 0 || productID < 0) {
                    return new Response(true, "createExpNode: in create QuantityLimitForProduct there is invalid parameters");
                }
                DummyQuantityLimitForProduct QLFPExp = new DummyQuantityLimitForProduct(stores.get(storeID).getNextExpressionID(), maxQuantity, productID);
                if (this.tmpBuyingPolicyForStore.get(storeID) == null) {
                    this.tmpBuyingPolicyForStore.put(storeID, QLFPExp);
                } else {
                    this.tmpBuyingPolicyForStore.get(storeID).setExpression(nodeID, QLFPExp);
                }
                return new Response("createExpNode: The node QuantityLimitForStore added successfully");
            }
            case "QuantityLimitForCategory": {
                if (maxQuantity < 0 || category == null) {
                    return new Response(true, "createExpNode: in create QuantityLimitForCategory there is invalid parameters");
                }
                DummyQuantityLimitForCategory QLFQExp = new DummyQuantityLimitForCategory(stores.get(storeID).getNextExpressionID(), maxQuantity, category);
                if (this.tmpBuyingPolicyForStore.get(storeID) == null) {
                    this.tmpBuyingPolicyForStore.put(storeID, QLFQExp);
                } else {
                    this.tmpBuyingPolicyForStore.get(storeID).setExpression(nodeID, QLFQExp);
                }
                return new Response("createExpNode: The node QuantityLimitForCategory added successfully");
            }
            case "QuantityLimitForStore" : {
                if (category == null) {
                    return new Response(true, "createExpNode: in create QuantityLimitForStore there is invalid parameters");
                }
                DummyQuantityLimitForStore QLFSExp = new DummyQuantityLimitForStore(stores.get(storeID).getNextExpressionID(), maxQuantity, storeID);
                if (this.tmpBuyingPolicyForStore.get(storeID) == null) {
                    this.tmpBuyingPolicyForStore.put(storeID, QLFSExp);
                } else {
                    this.tmpBuyingPolicyForStore.get(storeID).setExpression(nodeID, QLFSExp);
                }
                return new Response("createExpNode: The node QuantityLimitForStore added successfully");
            }
        }
        return  new Response(true, "createExpNode: the type is not valid");
    };

    private Response createSaleNode(int storeID, int nodeID, int quantity, int productID, int maxQuantity, String category, int numOfProductsForSale, int priceForSale, int quantityForSale, int discount, String type) {
        switch (type) {
            case "AddComposite": {
                DummyAddSale AddSale = new DummyAddSale(stores.get(storeID).getNextSaleID());
                if (this.tmpDiscountPolicyForStore.get(storeID) == null) {
                    this.tmpDiscountPolicyForStore.put(storeID, AddSale);
                } else {
                    this.tmpDiscountPolicyForStore.get(storeID).setSale(nodeID, AddSale);
                }
                return new Response("createSaleNode: The node AddComposite added successfully");
            }
            case "MaxComposite": {
                DummyMaxSale MaxSale = new DummyMaxSale(stores.get(storeID).getNextSaleID());
                if (this.tmpDiscountPolicyForStore.get(storeID) == null) {
                    this.tmpDiscountPolicyForStore.put(storeID, MaxSale);
                } else {
                    this.tmpDiscountPolicyForStore.get(storeID).setSale(nodeID, MaxSale);
                }
                return new Response("createSaleNode: The node MaxComposite added successfully");
            }
            case "XorComposite": {
                DummyXorComposite XorSale = new DummyXorComposite(stores.get(storeID).getNextSaleID());
                if (this.tmpDiscountPolicyForStore.get(storeID) == null) {
                    this.tmpDiscountPolicyForStore.put(storeID, XorSale);
                } else {
                    this.tmpDiscountPolicyForStore.get(storeID).setSale(nodeID, XorSale);
                }
                return new Response("createSaleNode: The node XorComposite added successfully");
            }
            case "StoreSale": {
                if (discount<0) {
                    return new Response(true, "createSaleNode: in create StoreSale there is invalid parameter discount");
                }
                DummyStoreSale StoreSale = new DummyStoreSale(stores.get(storeID).getNextSaleID(), storeID, discount);
                if (this.tmpDiscountPolicyForStore.get(storeID) == null) {
                    this.tmpDiscountPolicyForStore.put(storeID, StoreSale);
                } else {
                    this.tmpDiscountPolicyForStore.get(storeID).setSale(nodeID, StoreSale);
                }
                return new Response("createSaleNode: The node StoreSale added successfully");
            }
            case "ProductSale": {
                if (discount<0 || productID<0 ){
                    return new Response(true, "createSaleNode: in create ProductSale there is invalid parameters");
                }
                DummyProductSale ProductSale = new DummyProductSale(stores.get(storeID).getNextSaleID(), productID, discount);
                if (this.tmpDiscountPolicyForStore.get(storeID) == null) {
                    this.tmpDiscountPolicyForStore.put(storeID, ProductSale);
                } else {
                    this.tmpDiscountPolicyForStore.get(storeID).setSale(nodeID, ProductSale);
                }
                return new Response("createSaleNode: The node ProductSale added successfully");
            }
            case "CategorySale": {
                if (discount<0||category==null) {
                    return new Response(true, "createSaleNode: in create CategorySale there is invalid parameters");
                }
                DummyCategorySale ProductSale = new DummyCategorySale(stores.get(storeID).getNextSaleID(), category, discount);
                if (this.tmpDiscountPolicyForStore.get(storeID) == null) {
                    this.tmpDiscountPolicyForStore.put(storeID, ProductSale);
                } else {
                    this.tmpDiscountPolicyForStore.get(storeID).setSale(nodeID, ProductSale);
                }
                return new Response("createSaleNode: The node DummyCategorySale added successfully");
            }
            //return crateExpForSale(storeID, nodeID, productID, numOfProductsForSale, priceForSale, quantityForSale,type);
        }
        return crateExpForSale(storeID, nodeID, productID, numOfProductsForSale, priceForSale, quantityForSale,type);
    }

    private Response crateExpForSale(int storeID, int nodeID, int productID, int numOfProductsForSale, int priceForSale, int quantityForSale, String type) {
        if(this.tmpDiscountPolicyForStore.get(storeID) == null) {
            return new Response(true, "crateExpForSale: you try to put Exp "+type+" for store "+storeID+" in node "+ nodeID+" but there is no sale in the store. add sale before.");
        }
        switch (type) {
            case "OrComposite": {
                DummyOrExpression OrExp = new DummyOrExpression(stores.get(storeID).getNextSaleID());
                this.tmpDiscountPolicyForStore.get(storeID).setExpression(nodeID, OrExp);
                return new Response("crateExpForSale: The node OrComposite added successfully");
            }
            case "AndComposite": {
                DummyAndExpression AndExp = new DummyAndExpression(stores.get(storeID).getNextSaleID());
                this.tmpDiscountPolicyForStore.get(storeID).setExpression(nodeID, AndExp);
                return new Response("crateExpForSale: The node AndComposite added successfully");
            }
            case "NumOfProductsForGetSale":{
                if (numOfProductsForSale < 0 ) {
                    return new Response(true, "crateExpForSale: in create NumOfProductsForGetSale there is invalid parameter 'numOfProductsForSale'");
                }
                DummyNumOfProductsForGetSale NOPFGSExp = new DummyNumOfProductsForGetSale(stores.get(storeID).getNextSaleID(),numOfProductsForSale);
                this.tmpDiscountPolicyForStore.get(storeID).setExpression(nodeID, NOPFGSExp);
                return new Response("crateExpForSale: The node NumOfProductsForGetSale added successfully");
            }
            case "PriceForGetSale" : {
            if (priceForSale < 0 ) {
                return new Response(true, "crateExpForSale: in create PriceForGetSale there is invalid parameter 'priceForSale'");
            }
            DummyPriceForGetSale PFGSExp = new DummyPriceForGetSale(stores.get(storeID).getNextSaleID(),priceForSale);
            this.tmpDiscountPolicyForStore.get(storeID).setExpression(nodeID, PFGSExp);
            return new Response("crateExpForSale: The node PriceForGetSale added successfully");
            }
            case "QuantityForGetSale" : {
                if (productID<0||quantityForSale < 0 ) {
                    return new Response(true, "crateExpForSale: in create QuantityForGetSale there is invalid parameters");
                }
                DummyQuantityForGetSale DQFGSExp = new DummyQuantityForGetSale(stores.get(storeID).getNextSaleID(),productID,quantityForSale);
                this.tmpDiscountPolicyForStore.get(storeID).setExpression(nodeID, DQFGSExp);
                return new Response("crateExpForSale: The node QuantityForGetSale added successfully");}
            }
        return  new Response(true, "crateNodeSale: the type is not valid");
        }

    public static ExternalServices getPaymentSystem() {
        return paymentSystem;
    }

    public static void setPaymentSystem(ExternalServices paymentSystem) {
        TradingSystemImpl.paymentSystem = paymentSystem;
    }

    public static ExternalServices getSupplySystem() {
        return supplySystem;
    }

    public static void setSupplySystem(ExternalServices supplySystem) {
        TradingSystemImpl.supplySystem = supplySystem;
    }

    /**
     * @requirement 6.5
     *
     * @param connID
     * @param userID
     * @return @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "DailyReview": List [{
     *      "date": String
     *      "numOfViewers": int
     *  }]
     * }
     */
    //Viewing daily system's conduct
    public Response getAllSubscribersWeek(String connID, int userID){
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "getAllSubscribersWeek: The user " + userID + " is not connected");
        }
        if(this.subscribers.get(userID)==null){
            return new Response(true, "getAllSubscribersWeek: The user "+userID+" is not in the list");
        }
        if(!this.systemAdmins.containsKey(userID)){
            return new Response(true, "getAllSubscribersWeek: The user "+userID+" is not the admin of the system");
        }

        if(!this.hasPermission(userID, PermissionEnum.Permission.GetDailyIncomeForSystem)){
            return new Response(true, "getAllSubscribersWeek: The user " + userID + " has no permissions to see this information");
        }
        HashMap<Date, Integer> hashMap;
        try{
            hashMap = this.data_controller.getAllSubscribersWeek();
        }catch (Exception e){
            return new Response(true, "Error In DB!");
        }
        List<DummyDaily> list = new ArrayList<>();
        for(Map.Entry<Date, Integer> s : hashMap.entrySet())
        {
            String date = s.getKey().toString();
            DummyDaily daily = new DummyDaily(date, s.getValue());
            list.add(daily);
        }
        Response response = new Response(false, "getAllSubscribersWeek successfully");
        response.AddPair("DailyReview", list);
        User user=subscribers.get(userID);
        response.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
        return response;
    }

    /**
     * @requirement 6.5
     *
     * @param connID
     * @param userID
     * @return @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "DailyReview": List [{
     *      "date": String
     *      "numOfViewers": int
     *  }]
     * }
     */
    public Response getAllStoresWeek(String connID, int userID){
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "getAllSubscribersWeek: The user " + userID + " is not connected");
        }
        if(this.subscribers.get(userID)==null){
            return new Response(true, "getAllSubscribersWeek: The user "+userID+" is not in the list");
        }
        if(!this.systemAdmins.containsKey(userID)){
            return new Response(true, "getAllSubscribersWeek: The user "+userID+" is not the admin of the system");
        }

        if(!this.hasPermission(userID, PermissionEnum.Permission.GetDailyIncomeForSystem)){
            return new Response(true, "getAllSubscribersWeek: The user " + userID + " has no permissions to see this information");
        }
        HashMap<Date, Integer> hashMap;
        try{
            hashMap = this.data_controller.getAllStoresWeek();
        }catch (Exception e){
            return new Response(true, "Error In DB!");
        }
        List<DummyDaily> list = new ArrayList<>();
        for(Map.Entry<Date, Integer> s : hashMap.entrySet())
        {
            String date = s.getKey().toString();
            DummyDaily daily = new DummyDaily(date, s.getValue());
            list.add(daily);
        }
        Response response = new Response(false, "getAllSubscribersWeek successfully");
        response.AddPair("DailyReview", list);
        User user=subscribers.get(userID);
        response.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
        return response;
    }

    /**
     * @requirement 6.5
     *
     * @param connID
     * @param userID
     * @return @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "DailyReview": List [{
     *      "date": String
     *      "numOfViewers": int
     *  }]
     * }
     */
    public Response getAllShoppingHistoriesWeek(String connID, int userID) {
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "getAllSubscribersWeek: The user " + userID + " is not connected");
        }
        if(this.subscribers.get(userID)==null){
            return new Response(true, "getAllSubscribersWeek: The user "+userID+" is not in the list");
        }
        if(!this.systemAdmins.containsKey(userID)){
            return new Response(true, "getAllSubscribersWeek: The user "+userID+" is not the admin of the system");
        }

        if(!this.hasPermission(userID, PermissionEnum.Permission.GetDailyIncomeForSystem)){
            return new Response(true, "getAllSubscribersWeek: The user " + userID + " has no permissions to see this information");
        }
        HashMap<Date, Integer> hashMap;
        try{
            hashMap = this.data_controller.getAllShoppingHistoriesWeek();
        }catch (Exception e){
            return new Response(true, "Error In DB!");
        }
        List<DummyDaily> list = new ArrayList<>();
        for(Map.Entry<Date, Integer> s : hashMap.entrySet())
        {
            String date = s.getKey().toString();
            DummyDaily daily = new DummyDaily(date, s.getValue());
            list.add(daily);
        }
        Response response = new Response(false, "getAllSubscribersWeek successfully");
        response.AddPair("DailyReview", list);
        User user=subscribers.get(userID);
        response.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
        return response;
    }

    /**
     * @requirement 6.5
     *
     * @param connID
     * @param userID
     * @return @return Response{
     *  "isErr: boolean
     *  "message": String
     *  "DailyReview": List [{
     *      "date": String
     *      "numOfViewers": int
     *  }]
     * }
     */
    public Response getAllMoneyWeek(String connID, int userID) {
        if (!ValidConnectedUser(userID, connID)) {
            return new Response(true, "getAllSubscribersWeek: The user " + userID + " is not connected");
        }
        if(this.subscribers.get(userID)==null){
            return new Response(true, "getAllSubscribersWeek: The user "+userID+" is not in the list");
        }
        if(!this.systemAdmins.containsKey(userID)){
            return new Response(true, "getAllSubscribersWeek: The user "+userID+" is not the admin of the system");
        }

        if(!this.hasPermission(userID, PermissionEnum.Permission.GetDailyIncomeForSystem)){
            return new Response(true, "getAllSubscribersWeek: The user " + userID + " has no permissions to see this information");
        }
        HashMap<Date, Integer> hashMap;
        try{
            hashMap = this.data_controller.getAllMoneyWeek();
        }catch (Exception e){
            return new Response(true, "Error In DB!");
        }
        List<DummyDaily> list = new ArrayList<>();
        for(Map.Entry<Date, Integer> s : hashMap.entrySet())
        {
            String date = s.getKey().toString();
            DummyDaily daily = new DummyDaily(date, s.getValue());
            list.add(daily);
        }
        Response response = new Response(false, "getAllSubscribersWeek successfully");
        response.AddPair("DailyReview", list);
        User user=subscribers.get(userID);
        response.AddUserSubscriber(user.isManaged(), user.isOwner(), user.isFounder(),systemAdmins.containsKey(userID));
        return response;
    }

}
