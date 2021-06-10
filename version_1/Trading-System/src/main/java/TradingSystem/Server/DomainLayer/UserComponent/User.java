package TradingSystem.Server.DomainLayer.UserComponent;



import TradingSystem.Server.DataLayer.Data_Modules.DataStore;
import TradingSystem.Server.DataLayer.Data_Modules.DataSubscriber;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataManagerPermissions;
import TradingSystem.Server.DataLayer.Data_Modules.Permissions.DataOwnerPermissions;
import TradingSystem.Server.DataLayer.Data_Modules.ShoppingHistory.DataShoppingHistory;
import TradingSystem.Server.DataLayer.Services.Data_Controller;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingBag;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingCart;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import TradingSystem.Server.ServiceLayer.ServiceApi.Publisher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class User implements Observer {

    @Autowired
    public static Data_Controller data_controller;
    public static void setData_controller(Data_Controller data_controller) {
        Store.data_controller = data_controller;
    }

    private static TradingSystemImplRubin tradingSystem;
    public static void setTradingSystem(TradingSystemImplRubin tradingSystem) {
        User.tradingSystem = tradingSystem;
    }
    private List<Object> messages = new ArrayList<>();


    private static int nextUserID = 0;

    private final Integer id;
    private String userName;
    private String password;
    private List<Integer> myFoundedStoresIDs = new ArrayList<>();
    private List<Integer> myOwnedStoresIDs = new ArrayList<>();
    private List<Integer> myManagedStoresIDs = new ArrayList<>();
    ;
    //storeID_OwnerPermission
    private ConcurrentHashMap<Integer, OwnerPermission> ownerPermission = new ConcurrentHashMap<>();
    //storeID_ManagerPermission
    private ConcurrentHashMap<Integer, ManagerPermission> managerPermission = new ConcurrentHashMap<>();

    private ShoppingCart shoppingCart;
    private List<ShoppingHistory> shoppingHistory = new ArrayList<>();
    private Publisher publisher;

    private final Lock Lock = new ReentrantLock();

    public User() {
        this.id = -1;
        this.userName = "guest";
        this.password = "";
        this.shoppingCart = new ShoppingCart(this.id);
    }

    public User(int id) {
        this.id = id;
        this.userName = "guest";
        this.password = "";
        this.shoppingCart = new ShoppingCart(this.id);
    }

    //FOR the db
    public User(int id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.shoppingCart = new ShoppingCart(this.id);
    }
    public User(DataSubscriber subscriber) {
        this.id = subscriber.getUserID();
        this.userName = subscriber.getName();
        this.password = subscriber.getPassword();
        this.myFoundedStoresIDs = subscriber.getStoresFounder().stream().map(DataStore::getStoreID).collect(Collectors.toList());
        this.myOwnedStoresIDs=subscriber.getStoresOwner().stream().map(DataStore::getStoreID).collect(Collectors.toList());
        this.myManagedStoresIDs=subscriber.getStoresManager().stream().map(DataStore::getStoreID).collect(Collectors.toList());
        for (DataOwnerPermissions dataOwnerPermission: subscriber.getOwnerPermissions()){
            int storeID = dataOwnerPermission.getStore().getStoreID();
            OwnerPermission permission = new OwnerPermission(dataOwnerPermission);
            this.ownerPermission.put(storeID, permission);
        }
        for (DataManagerPermissions dataManagerPermission: subscriber.getManagerPermissions()){
            int storeID = dataManagerPermission.getStore().getStoreID();
            ManagerPermission permission = new ManagerPermission(dataManagerPermission);
            this.managerPermission.put(storeID, permission);
        }
        this.shoppingCart = new ShoppingCart(this.id, subscriber.getShoppingBagsCart());

        for (DataShoppingHistory dataShoppingHistory: subscriber.getShoppingBagsHistory()){
            ShoppingHistory history = new ShoppingHistory(dataShoppingHistory);
            this.shoppingHistory.add(history);
        }
    }


    public User(String userName, String password) {
        this.id = getNextUserID();
        this.userName = userName;
        this.password = password;
        this.shoppingCart = new ShoppingCart(this.id);
        this.myManagedStoresIDs=new ArrayList<>();
        this.myManagedStoresIDs=new ArrayList<>();
        this.myFoundedStoresIDs=new ArrayList<>();
        this.ownerPermission=new ConcurrentHashMap<>();
        this.managerPermission=new ConcurrentHashMap<>();
    }

    public User(String userName, String password, ShoppingCart shoppingCart) {
        this.id = getNextUserID();
        this.userName = userName;
        this.password = password;
        this.shoppingCart = shoppingCart;
        this.myManagedStoresIDs=new ArrayList<>();
        this.myManagedStoresIDs=new ArrayList<>();
        this.myFoundedStoresIDs=new ArrayList<>();
        this.ownerPermission=new ConcurrentHashMap<>();
        this.managerPermission=new ConcurrentHashMap<>();
    }

    public boolean isFounder(){
        return myFoundedStoresIDs!=null && myFoundedStoresIDs.size()>0;
    }
    public boolean isOwner(){
        return myOwnedStoresIDs!=null && myOwnedStoresIDs.size()>0;
    }
    public boolean isManaged(){
        return myManagedStoresIDs!=null && myManagedStoresIDs.size()>0;
    }

    private static synchronized int getNextUserID() {
        nextUserID++;
        return nextUserID;
    }

    public ConcurrentHashMap<Integer, OwnerPermission> getOwnerPermission() {
        return ownerPermission;
    }

    public ConcurrentHashMap<Integer, ManagerPermission> getManagerPermission() {
        return managerPermission;
    }

    public List<ShoppingHistory> getShoppingHistory() {
        return shoppingHistory;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public void notify(String topic, Response res) {
        if(publisher!=null){
            System.out.println("-------------------notify-------------------");
            publisher.SendMessage(topic, res);
        }
    }

    public static void ClearSystem() {
        nextUserID = 0;
    }

    public void lockUser() {
        this.Lock.lock();
    }
    public void unlockUser(){
        this.Lock.unlock();
    }

    public boolean tryToLock() {
        return this.Lock.tryLock();
    }

    public void AddStore(int storeID) {
        this.myFoundedStoresIDs.add(storeID);
        this.myOwnedStoresIDs.add(storeID);
        this.ownerPermission.put(storeID, new OwnerPermission(this.id, storeID));
    }

    public void mergeToMyCart(ShoppingCart shoppingCartToMerge) {
        this.shoppingCart.mergeToMyCart(shoppingCartToMerge);
    }

    public void AddShoppingCartTohistory(ShoppingHistory shoppingHistory) {
        this.shoppingHistory.add(shoppingHistory);
    }

    public Integer getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }

    public void setShoppingCart(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public void setMyFoundedStoresIDs(List<Integer> foundedStoresIDs){
        this.myFoundedStoresIDs=foundedStoresIDs;
    }

    public void setMyManagedStoresIDs(List<Integer> myManagedStoresIDs){
        this.myManagedStoresIDs=myManagedStoresIDs;
    }
    public void setMyOwnedStoresIDs(List<Integer> myOwnedStoresIDs){
        this.myOwnedStoresIDs=myOwnedStoresIDs;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", myFoundedStoresIDs=" + myFoundedStoresIDs +
                ", myOwnedStoresIDs=" + myOwnedStoresIDs +
                ", myManagedStoresIDs=" + myManagedStoresIDs +
                ", ownerPermission=" + ownerPermission.keySet() +
                ", managerPermission=" + managerPermission.keySet() +
                ", shoppingCart=" + shoppingCart +
                ", shoppingHistory=" + shoppingHistory +
                '}';
    }

    public void AddFounderStoresIfNotExist(int storeID){
        if (!myFoundedStoresIDs.contains(storeID)){
            myFoundedStoresIDs.add(storeID);
        }
    }
    public void AddOwnerStoresIfNotExist(int storeID){
        if (!myOwnedStoresIDs.contains(storeID)){
            myOwnedStoresIDs.add(storeID);
        }
    }

    public void AddManagerStoresIfNotExist(int storeID){
        if (!myManagedStoresIDs.contains(storeID)){
            myManagedStoresIDs.add(storeID);
        }
    }

    public void AddShoppingBagIfNotExist(ShoppingBag shoppingBag){
        shoppingCart.AddShoppingBagIfNotExist(shoppingBag);
    }

    public Response AddProductToCart(int StoreId, int productId, int quantity) {
        return shoppingCart.addProductToBag(StoreId, productId, quantity, id<1);
    }

    public List<DummyProduct> ShowShoppingCart() {
        return shoppingCart.ShowShoppingCart();
    }

    public Response guestPurchase(String name, String credit_number, String month, String year, String cvv, String ID, String address, String city, String country, String zip){
        return shoppingCart.Purchase(true, name, credit_number, month, year, cvv, ID, address,city,country,zip);
    }

    public Response subscriberPurchase(String credit_number, String month, String year, String cvv, String ID, String address, String city, String country, String zip){
        return shoppingCart.Purchase(false, this.userName, credit_number, month, year, cvv, ID, address,city,country,zip);

    }

    public void addHistory(ShoppingHistory sh) {
        this.shoppingHistory.add(sh);
    }

    public List<DummyShoppingHistory> ShowUserHistory(){
        List<DummyShoppingHistory> shoppingHistories=new ArrayList<>();
        for(ShoppingHistory s : shoppingHistory){
            shoppingHistories.add(new DummyShoppingHistory(s));
        }
        return shoppingHistories;
    }

    public List<Integer> getMyFoundedStoresIDs() {
        return this.myFoundedStoresIDs;
    }

    public void AddStoreInOwner(int storeID, OwnerPermission op) {
        this.myOwnedStoresIDs.add(storeID);
        this.ownerPermission.put(storeID, op);
    }

    public List<Integer> getMyOwnerStore() {
        return this.myOwnedStoresIDs;
    }

    public List<Integer> getMyManagerStore() {
        return this.myManagedStoresIDs;
    }

    public void setShoppingHistory(List<ShoppingHistory> shoppingHistories){
        this.shoppingHistory=shoppingHistories;
    }
    public void AddStoreInManager(int storeID, ManagerPermission om) {
        this.myManagedStoresIDs.add(storeID);
        this.managerPermission.put(storeID, om);
    }

    public OwnerPermission getOwnerPermission(int storeID) {
        return this.ownerPermission.get(storeID);
    }

    public ManagerPermission getManagerPermission(int storeID) {
        return this.managerPermission.get(storeID);
    }

    public void removeManagedStore(int storeID) {
        int index=this.myManagedStoresIDs.indexOf(storeID);
        this.myManagedStoresIDs.remove(index);
        this.managerPermission.remove(storeID);
    }

    public void removeOwnedStore(int storeID){
        int index=this.myOwnedStoresIDs.indexOf(storeID);
        this.myOwnedStoresIDs.remove(index);
        this.ownerPermission.remove(storeID);
    }

    public Response editProductQuantityFromCart(int storeID, int productID, int quantity) {
        return this.shoppingCart.editProductQuantityFromCart(storeID,productID, quantity);
}

    public Response RemoveProductFromCart(int storeID, int productID) {
      return this.shoppingCart.RemoveProductFromCart(storeID, productID);
    }

    public boolean IsProductExist(int productid){
        for(ShoppingHistory shoppingHistory:this.shoppingHistory){
            if(shoppingHistory.isProductExist(productid)){
                return true;
            }
        }
        return false;
    }

    public Response AbleToAddOwner(int userID, int storeID) {
        if (this.checkOwner(storeID)) {
            return new Response(true, "AddOwner: User "+userID+" is owner the store, so he can not appoint to owner again");
        }
        if (this.checkManager(storeID)){
            return new Response(true, "AddOwner: User "+userID+" is manages the store, so he can not be owner");
        }
        return new Response(false,"AddOwner: It is possible to add the user as the owner");
}

    public boolean checkOwner(int storeID) {
    return this.myOwnedStoresIDs.contains(storeID);
    }

    public boolean checkManager(int storeID) {
    return this.myManagedStoresIDs.contains(storeID);
    }

    public Response AbleToRemoveManager(int userID, int storeID) {
        if (!this.myManagedStoresIDs.contains(storeID)){
            return new Response(true, "RemoveManager: The user "+this.id+" is not manages the store, so he can not be removed from Manages the store.");
        }
        if (this.managerPermission.get(storeID)!=null&&
            this.managerPermission.get(storeID).getAppointmentId()!=userID) {
            return new Response(true, "RemoveManager: The user " + userID + " is not the one who appointed the manager");
        }
        return new Response(false, "It is possible to add the user as the owner");
    }

    public Response AbleToAddManager(int userID, int storeID, int newManager) {
        if (this.checkOwner(storeID)) {
            return new Response(true, "AddNewManager: The user "+newManager+" is owner the store, so he can not appoint to Manager");

        }
        if (this.checkManager(storeID)){
            return new Response(true, "AddNewManager: The user "+newManager+" is manages the store, so he can not appoint to Manager again");
        }
        return new Response(false, "It is possible to add the user as the owner");
    }

    public Response AbleToEditPermissions(int userID, int storeID) {
        if (!this.myManagedStoresIDs.contains(storeID)){
            return new Response(true, "EditPermissions: The user "+this.id+" is not manages the store, so it impossible to edit his permissions.");
        }
        if (this.managerPermission.get(storeID)!=null&&
                this.managerPermission.get(storeID).getAppointmentId()!=userID) {
            return new Response(true, "EditPermissions: The user " + userID + " is not the one who appointed the manager");
        }
        return new Response(false, "EditPermissions: It is possible to edit the manager permissions");
    }

    public void editPermissions(int userID,int storeID, List<PermissionEnum.Permission> permissions) {
        ManagerPermission MP=managerPermission.get(storeID);
        if(MP==null){
            MP=new ManagerPermission(this.id,storeID);
            MP.setAppointmentId(userID);
            MP.setPermissions(permissions);
            this.managerPermission.put(storeID,MP);
        }
        else{
            MP.setPermissions(permissions);
            this.managerPermission.remove(storeID);
            this.managerPermission.put(storeID,MP);
        }
    }

    public boolean isConnected(){
        if(tradingSystem.getConnectedSubscribers().containsValue(id))
            return true;
        return false;
    }

    public boolean isSubscriber(){
        if(tradingSystem.subscribers.containsKey(id))
            return true;
        return false;
    }


    //Observable pattern
    public void update(Object arg) {
        if(isSubscriber()) {
            if (isConnected()) {
                Response res = (Response) arg;
                this.notify(tradingSystem.getUserConnID(this.id), res);
            } else {
                addMessage(arg);
            }
        }
        else {
            Response res = (Response) arg;
            this.notify(tradingSystem.getUserConnID(this.id), res);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        update(arg);
//        System.out.println("\n\n------>>>> update, username: " + userName + "\n\n");
    }

    public void addMessage(Object arg){
        try {
            synchronized (messages){
                this.messages.add(arg);
                messages.notifyAll();
            }
        } catch (Exception e) {
            System.out.println("catch notify all error!!!");
            System.out.println(e);
        }
    }

    public List<String> updateAfterLogin(){
        List<String> strMessages = new ArrayList<>();
        System.out.println(messages.size());
        try {
            synchronized (messages) {
                int size = messages.size();
                for (int i = 0; i < size; i++) {
                    Response res = (Response) messages.get(i);
                    System.out.println(i + ": " + res.getMessage());
                    strMessages.add(res.getMessage());
//                this.notify(tradingSystem.getUserConnID(this.id), res);
                }
                messages = new ArrayList<>();
//                while(messages.isEmpty()) {
//                    messages.remove(0);
//                }
                messages.notifyAll();
            }
        } catch (Exception e) {
            System.out.println("catch notify all error!!!");
            System.out.println(e);
        }

        return strMessages;
    }

    //TODO Implement
   // public void AddProductForPurchaseOffer(int productID, int storeID, int productPrice) { }

    public Response AddSpacialProductForCart(int productID, int storeID, int productPrice,Integer quantity) {
         if(this.shoppingCart==null){
             this.shoppingCart=new ShoppingCart(this.id);
         }
        return this.shoppingCart.AddSpacialProductForCart(productID,storeID, productPrice,quantity);
    }


    public List<DummyProduct> ShowSpecialProductInShoppingCart() {
        return shoppingCart.ShowSpecialProductInShoppingCart();
    }

    public Response removeSpecialProductFromCart(int storeID, int productID) {
        return this.shoppingCart.removeSpecialProductFromCart(storeID, productID);
    }

    }


//    public List<DummySearch> getShoppingCart(){
//        List<DummySearch> shoppingBags=new ArrayList<>();
//        for(ShoppingBag shoppingBag: shoppingCart.shoppingBags()){
//            List<Integer> products=shoppingBag.getProductsList();
//            for(Integer i:products){
//                List<DummySearch> dummySearches= this.tradingSystem.getStores().get(shoppingBag.getStoreID()).getAllProducts();
//                shoppingBags.add(new DummySearch(shoppingBag.getStoreID(), this.tradingSystem.getStores().get(shoppingBag.getStoreID()).getName(),i,this.tradingSystem.ge,))
//            }
//        }
//    }

