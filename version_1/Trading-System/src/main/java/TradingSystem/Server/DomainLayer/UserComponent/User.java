package TradingSystem.Server.DomainLayer.UserComponent;



import TradingSystem.Server.ServiceLayer.ServiceApi.Publisher;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingCart;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public  class User implements Observer {


    private List<Object> messages = new ArrayList<>();

    public enum Permission {
        AddProduct,
        ReduceProduct,
        DeleteProduct,
        EditProduct,
        AppointmentOwner,
        AppointmentManager,
        EditManagerPermission,
        RemoveManager,
        GetInfoOfficials,
        GetInfoRequests,
        ResponseRequests,
        GetHistoryPurchasing,
        GetStoreHistory
    }

    private final TradingSystemImpl tradingSystem = TradingSystemImpl.getInstance();
    private static int nextUserID = 0;

    private final Integer id;
    private String userName;
    private String password;
    private List<Integer> myFoundedStoresIDs = new ArrayList<>();
    ;
    private List<Integer> myOwnedStoresIDs = new ArrayList<>();
    ;
    private List<Integer> myManagedStoresIDs = new ArrayList<>();
    ;
    //storeID_OwnerPermission
    private ConcurrentHashMap<Integer, OwnerPermission> ownerPermission;
    //storeID_ManagerPermission
    private ConcurrentHashMap<Integer, ManagerPermission> managerPermission;

    private ShoppingCart shoppingCart;
    private List<ShoppingHistory> shoppingHistory = new ArrayList<>();
    private Publisher publisher;

    private final Lock Lock = new ReentrantLock();

    public User() {
        this.id = -1;
        this.userName = "guest";
        this.password = "";
        this.shoppingCart = new ShoppingCart(this.id);
        this.ownerPermission = null;
        this.managerPermission = null;
        this.myManagedStoresIDs=new ArrayList<>();
        this.myManagedStoresIDs=new ArrayList<>();
        this.myFoundedStoresIDs=new ArrayList<>();
        this.ownerPermission=new ConcurrentHashMap<>();
        this.managerPermission=new ConcurrentHashMap<>();
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", myFoundedStoresIDs=" + myFoundedStoresIDs +
                ", shoppingCart=" + shoppingCart +
                '}';
    }

    public Response AddProductToCart(int StoreId, int productId, int quantity) {
        return shoppingCart.addProductToBag(StoreId, productId, quantity);
    }

    public List<DummyProduct> ShowShoppingCart() {
        return shoppingCart.ShowShoppingCart();
    }

    public Response guestPurchase(String name, String credit_number, String phone_number, String address){
        return shoppingCart.Purchase(true, name, credit_number, phone_number, address);
    }

    public Response subscriberPurchase(String credit_number, String phone_number, String address){
        return shoppingCart.Purchase(false, this.userName, credit_number, phone_number, address);

    }

    public void addHistory(ShoppingHistory sh) {
        this.shoppingHistory.add(sh);
    }

    public List<DummyShoppingHistory> ShowUserHistory(){
        List<DummyShoppingHistory> shoppingHistories=new ArrayList<>();
        for(ShoppingHistory shoppingHistory : shoppingHistory){
            shoppingHistories.add(new DummyShoppingHistory(shoppingHistory));
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

    public void editPermissions(int userID,int storeID, List<User.Permission> permissions) {
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
    }

    public void addMessage(Object arg){
        synchronized (messages){
            this.messages.add(arg);
        }
        notifyAll();
    }

    public void updateAfterLogin(){
        synchronized (messages) {
            for (Object arg : messages) {
                Response res = (Response) arg;
                this.notify(tradingSystem.getUserConnID(this.id), res);
                messages.remove(arg);
            }
        }
        notifyAll();
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

