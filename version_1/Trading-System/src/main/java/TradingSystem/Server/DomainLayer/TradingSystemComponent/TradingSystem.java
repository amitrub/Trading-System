package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import TradingSystem.Server.ServiceLayer.LoggerController;
import org.springframework.expression.spel.ast.Assign;

import static TradingSystem.Server.ServiceLayer.Configuration.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class TradingSystem {

    public Validation validation;

    private ConcurrentHashMap<Integer, Integer> systemAdmins;
    private ConcurrentHashMap<String, Integer> connectedSubscribers;

    public ConcurrentHashMap<Integer, User> subscribers;
    public ConcurrentHashMap<String, User> guests;
    public ConcurrentHashMap<Integer, Store> stores;

    //    Singleton
    private static TradingSystem tradingSystem = null;
    private static LoggerController loggerController=LoggerController.getInstance();

    private TradingSystem() {
        this.connectedSubscribers = new ConcurrentHashMap<>();
        this.subscribers = new ConcurrentHashMap<>();
        this.guests = new ConcurrentHashMap<>();
        this.stores = new ConcurrentHashMap<>();
        this.systemAdmins = new ConcurrentHashMap<>();
    }

    public static TradingSystem getInstance() {
        if (tradingSystem == null) {
            tradingSystem = new TradingSystem();
            tradingSystem.validation = new Validation();
            tradingSystem.Initialization();
        }
        return tradingSystem;
    }

    private void Initialization() {
        User defaultAdmin = new User("amit", "qweasd");
        int userID = defaultAdmin.getId();
        this.systemAdmins.put(userID, userID);
        this.subscribers.put(userID, defaultAdmin);
        //TODO: to delete after
        String connID = "479f239c-797c-4bdb-8175-980acaabf070";
        this.connectedSubscribers.put("479f239c-797c-4bdb-8175-980acaabf070", userID);
        AddStore(userID, connID, "store1");
        AddProductToStore(userID,connID,1,"prod1","sport", 7.0 );
        printUsers();
    }

    public String errMsgGenerator(String side, String className, String line, String msg) {
        return side + " : <" + className + " in line >" + line + " ; \"" + msg + "\"";
    }

    //prints for debug
    public void printUsers() {
        Set<Integer> userSet = this.subscribers.keySet();
        System.out.println(ANSI_GREEN + "Users" + ANSI_RESET);
        for (Integer id : userSet){
            User user = this.subscribers.get(id);
            if (connectedSubscribers.values().contains(user.getId()))
                System.out.println(ANSI_GREEN + user + "(connected)" + ANSI_RESET);
        }
        System.out.println(ANSI_PURPLE + "Users" + ANSI_RESET);
        for (Integer id : userSet){
            User user = this.subscribers.get(id);
            if (!connectedSubscribers.values().contains(user.getId()))
                System.out.println(ANSI_PURPLE + user + "(not connected)" + ANSI_RESET);
        }
        System.out.println("-----------------------------------------------");
        Set<String> guestSet = this.guests.keySet();
        System.out.println(ANSI_GREEN + "Guests"  + ANSI_RESET);
        for (String id : guestSet){
            User user = this.guests.get(id);
            System.out.println(ANSI_GREEN +  user + "(connected)" + ANSI_RESET);
        }
        System.out.println("-----------------------------------------------");
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
            LinkedList<Product> Products = this.stores.get(id).getProducts();
            for (Product p : Products) {
                int quantity= this.stores.get(id).getQuantity(p.getProductID());
                System.out.println(ANSI_WHITE + p + " with quantity of- "+ quantity + ":\n" + ANSI_WHITE);
            }
            }
        System.out.println("-----------------------------------------------");
    }

    //User functions
    public Response connectSystem() {
        User newGuest = new User();
        String connID = connectGuestToSystemConnID(newGuest);
        return new Response(-1,connID,false, "Connect system was successful");
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
    public Response Exit(String connID) {
        if (connectedSubscribers.containsKey(connID)) {
            connectedSubscribers.remove(connID);
            return new Response(false, "Exit System was successful");
        } else if (guests.containsKey(connID)) {
            guests.remove(connID);
            return new Response(false, "Exit System was successful");
        } else {
            return new Response(true, "User not connect to system");
        }
    }
    //Check if there is a user if the same name then return -1
    //If there is no new user creator adds it to users in the hashmap and returns an ID number
    public Response Register(String connID, String userName, String password) {
        if (guests.containsKey(connID) || connectedSubscribers.containsKey(connID)){
            if (validation.IsUserNameExist(userName)) {
                loggerController.WriteErrorMsg("User "+userName+" try to register to the system and failed");
                return new Response(true, errMsgGenerator("Server", "TradingSystem", "62", "Error user name is taken"));
            }
            User newUser = new User(userName, password);
            subscribers.put(newUser.getId(), newUser);
//        guests.remove(connID);
            loggerController.WriteErrorMsg("User "+userName+" register to the system successfully");
            Response res = new Response(newUser.getId(), connID, false, "Registration was successful");
            return res;
        }
        else
            return new Response(true, "Error in connID");
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
    public Response Login(String guestConnID, String userName, String password) {
        Response response = validation.ValidPassword(userName, password);
        if (response.isErr())
            return response;
        User myGuest = guests.get(guestConnID);
        subscribers.get(response.getUserID()).mergeToMyCart(myGuest.getShoppingCart());
        String connID = connectSubscriberToSystemConnID(response.getUserID());
        guests.remove(guestConnID);
        return new Response(response.getUserID(), connID, "Login was successful");
    }
    public Response Logout(String connID) {
        if (connectedSubscribers.containsKey(connID)) {
            User myUser = subscribers.get(connectedSubscribers.get(connID));
            connectedSubscribers.remove(connID);
            User newGuest = new User();
            //TODO: mybe do deep copy
            newGuest.setShoppingCart(myUser.getShoppingCart());
            String guestConnID = connectGuestToSystemConnID(newGuest);
            return new Response(-1, guestConnID,false, "Logout was successful");
        } else {
            return new Response(true, "User not login");
        }
    }

    //Store functions
    public Response AddStore(int userID, String connID, String storeName){
        if(ValidConnectedUser(userID, connID)){
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
                return new Response(false,  "Add Store was successful");
            }
        }
        else{
            return new Response(true, "Error in User details");
        }
    }
    public List<DummyStore> ShowAllStores() {
        List<DummyStore> list = new LinkedList<>();
        for (Map.Entry<Integer, Store> currStore : stores.entrySet()) {
            list.add(new DummyStore(currStore.getValue()));
        }
        return list;
    }

    //Product functions
    public Response AddProductToStore(int userID, String connID, int storeID, String productName, String category, double price){
        if(ValidConnectedUser(userID, connID)){
            if(allowedToAddProduct(userID,storeID)) {
                Response res = stores.get(storeID).AddProductToStore(productName, price, category);
                printProducts();
                loggerController.WriteLogMsg("User "+userID+" add product "+ productName+" to store "+storeID+" successfully");
                return res;
            }
            loggerController.WriteErrorMsg("User "+userID+" try to add product "+ productName+" to store "+storeID+" and failed");
            return new Response(true, "The User is not allowed to add a product");
        }
        else{
            loggerController.WriteErrorMsg("User "+userID+" try to add product "+ productName+" to store "+storeID+" and failed");
            return new Response(true, "Error in User details");
        }
    }
    public Response AddQuantityProduct(int userID, String connID, int storeID,int productId, int quantity){
        if(ValidConnectedUser(userID, connID)){
            if(allowedToAddQuantityProduct(userID,storeID)) {
                Response res = stores.get(storeID).addProductToInventory(productId, quantity);
                printProducts();
                loggerController.WriteLogMsg("User "+userID+" add "+ quantity+" products of "+productId+" to store "+storeID+" successfully");
                return res;
            }
            loggerController.WriteErrorMsg("User "+userID+" try to add "+ quantity+" products of "+productId+" to store "+storeID+" and failed");
            return new Response(true, "The User is not allowed to add products to the inventory");
        }
        else{
            loggerController.WriteErrorMsg("User "+userID+" try to add "+ quantity+" products of "+productId+" to store "+storeID+" and failed");
            return new Response(true, "Error in User details");
        }
    }
    public Response RemoveProduct(int userID, int storeID, int productID, String connID){
        if(ValidConnectedUser(userID, connID)){
            if(allowedToRemoveProduct(userID,storeID)) {
                Response res = stores.get(storeID).deleteProduct(productID);
                printProducts();
                loggerController.WriteLogMsg("User "+userID+" remove product"+ productID+" from store "+storeID+" successfully");
                return res;
            }
            loggerController.WriteErrorMsg("User "+userID+" try to remove product"+ productID+" from store "+storeID+" and failed");
            return new Response(true, "The User is not allowed to remove products from the inventory");
        }
        else{
            loggerController.WriteErrorMsg("User "+userID+" try to remove product"+ productID+" from store "+storeID+" and failed");
            return new Response(true, "Error in User details");
        }
    }
    public List<DummyProduct> ShowStoreProducts(int storeID) {
        if(stores.containsKey(storeID))
            return stores.get(storeID).ShowStoreProducts();
        else
            return new LinkedList<>();
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
    public List<DummyProduct> ShowShoppingCart(String connID){
        if(guests.containsKey(connID)) {
            return guests.get(connID).ShowShoppingCart();
        }
        else if(connectedSubscribers.containsKey(connID)) {
            int userID = connectedSubscribers.get(connID);
            return subscribers.get(userID).ShowShoppingCart();
        }
        else {
            return new LinkedList<>();
        }
    }


    //TODO: to check
    public List<DummyProduct> SearchProductByName(String name, int minprice, int maxprice, int prank , int srank){
        List<DummyProduct> dummyProducts = new LinkedList<>();
        for(Store store: stores.values()){
            if(((prank==-1 || store.getRate()>=srank) && !store.SearchByName(name, minprice, maxprice,prank).isEmpty())){
                dummyProducts.addAll(store.SearchByName(name, minprice, maxprice,prank));
            }
        }
        return dummyProducts;
    }
    //TODO: to check
    public List<DummyProduct> SearchProductByCategory(String category, int minprice, int maxprice, int prank , int srank){
        List<DummyProduct> dummyProducts = new LinkedList<>();
        for(Store store: stores.values()){
            if(!store.SearchByCategory(category, minprice, maxprice,prank).isEmpty()){
                dummyProducts.addAll(store.SearchByCategory(category, minprice, maxprice,prank));
            }
        }
        return dummyProducts;
    }










    public Double calculateBugPrice(Integer productID, Integer storeID, Integer quantity, ConcurrentHashMap<Integer, Integer> productsInTheBug) {
        return this.stores.get(storeID).calculateBugPrice(productID,quantity,productsInTheBug);
    }

    public boolean productIsLock(int productID, int storeID) {
        return this.stores.get(storeID).productIsLock(productID);
    }

    public void lockProduct(int productID, int storeID) {
        this.stores.get(storeID).lockProduct(productID);
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

    public Response reduseProducts(ConcurrentHashMap<Integer, Integer> products, int storeID) {
       return this.stores.get(storeID).reduceProducts(products);
    }

    //show the history for some user
    public List<DummyShoppingHistory> ShowStoreHistory(int userId){
        return subscribers.get(userId).ShowStoreHistory();
    }

    //show the history for some store
    public List<DummyShoppingHistory> ShowHistory(int storeId){
        return stores.get(storeId).getHistory();
    }


    public Response WriteComment(int userId, String connID, int storeId, int productId, String comment) {
        if (ValidConnectedUser(userId, connID)) {
            loggerController.WriteLogMsg("User "+userId+" add new comment to store "+ storeId+" successfully");
            return this.stores.get(storeId).WriteComment(userId, productId, comment);
        }
        else
            return new Response(true, "Error in User details");
    }

    public void addHistoryToStoreAndUser(ShoppingHistory sh)
    {
        this.stores.get(sh.getStoreID()).addHistory(sh);
        this.subscribers.get(sh.getUserID()).addHistory(sh);
    }



    public Response EditProduct(int userID, String connID, int storeID, int productID, String productName, String category, double price)
    {
        if(ValidConnectedUser(userID, connID)){
            if(allowedToEditProduct(userID,storeID)) {
                stores.get(storeID).editProductDetails(userID,productID,productName,price,category);
                printProducts();
                loggerController.WriteLogMsg("User "+userID+" edit product "+ productID+" successfully");
                return new Response(false, "Edit Product was successful");
            }
            loggerController.WriteErrorMsg("User "+userID+" try to edit product "+ productID+" and failed");
            return new Response(true, "The Edit is not allowed to Edit products");
        }
        else{
            loggerController.WriteErrorMsg("User "+userID+" try to edit product "+ productID+" and failed");
            return new Response(true, "Error in User details");
        }
    }

    public List<DummyShoppingHistory> StoreHistory(int userID, int storeID, String connID)
    {
        if (ValidConnectedUser(userID, connID)) {
            if (allowedToShowStoreHistory(userID, storeID)) {
                return stores.get(storeID).getHistory();
                // printStoreHistory?()
            }
        }
        return null;//todo make response inside?
    }

    private boolean allowedToAddProduct(int userID, int storeID) {
        Store store = stores.get(storeID);
        return store.checkFounder(userID) || store.checkFounder(userID);
    }

    private boolean allowedToAddQuantityProduct(int userID, int storeID) {
        Store store = stores.get(storeID);
        return store.checkFounder(userID) || store.checkFounder(userID);
    }

    private boolean allowedToRemoveProduct(int userID, int storeID) { return true; }

    private boolean allowedToEditProduct(int userID, int storeID) { return true; }

    private boolean allowedToShowStoreHistory(int userID, int storeID) { return true; }

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
