package TradingSystem.Server.DomainLayer.UserComponent;



import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingCart;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.NewResponse;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public  class User {


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
        GetHistoryPurchasing
    }

    private final TradingSystem tradingSystem = TradingSystem.getInstance();
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

    private final Lock Lock = new ReentrantLock();;

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
        this.ownerPermission = null;
        this.managerPermission = null;
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

    private static synchronized int getNextUserID() {
        nextUserID++;
        return nextUserID;
    }

    public static void ClearSystem() {
        nextUserID = 0;
    }

    public void lockUser() {
        this.Lock.lock();
    }
    public void unlockUser(){
        this.Lock.unlock();
      //  this.Lock.notify();
    }

    public boolean userIsLock() {
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

    public NewResponse AddProductToCart(int StoreId, int productId, int quantity) {
        return shoppingCart.addProductToBag(StoreId, productId, quantity);
    }

    public List<DummyProduct> ShowShoppingCart() {
        return shoppingCart.ShowShoppingCart();
    }

    public NewResponse guestPurchase(String name, String credit_number, String phone_number, String address){
        return shoppingCart.Purchase(true, name, credit_number, phone_number, address);
    }

    public NewResponse subscriberPurchase(String credit_number, String phone_number, String address){
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

    public void removeStore(int storeID) {
        int index=this.myManagedStoresIDs.indexOf(storeID);
        this.myManagedStoresIDs.remove(index);
        this.managerPermission.remove(storeID);
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

