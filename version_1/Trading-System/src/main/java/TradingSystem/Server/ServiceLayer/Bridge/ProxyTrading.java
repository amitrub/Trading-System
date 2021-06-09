package TradingSystem.Server.ServiceLayer.Bridge;

import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.UserComponent.PermissionEnum;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import TradingSystem.Server.ServiceLayer.ServiceApi.Publisher;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public class ProxyTrading implements TradingSystem {

    private TradingSystem real;

    public ProxyTrading(){
        real=null;
    }

    public void setRealBridge(TradingSystem implementation) {
        if (real == null)
            real = implementation;
    }

    @Override
    public ConcurrentHashMap<Integer, User> getSubscribers() {
        if(real!=null)
            return real.getSubscribers();
        return null;
    }

    @Override
    public ConcurrentHashMap<Integer, Store> getStores() {
        if(real!=null)
            return real.getStores();
        return null;
    }

    @Override
    public void ClearSystem() {
        if (real != null)
            real.ClearSystem();
    }

    @Override
    public void Initialization() {
        if (real != null)
            real.ClearSystem();
    }

    @Override
    public String errMsgGenerator(String side, String className, String line, String msg) {
        if(real!=null)
            return real.errMsgGenerator(side,className,line,msg);
        return null;
    }

    @Override
    public void printUsers() {
        if(real!=null)
            real.printUsers();
    }

    @Override
    public void printStores() {
        if(real!=null)
            real.printStores();
    }

    @Override
    public void printProducts() {
        if(real!=null)
            real.printStores();
    }

    @Override
    public Response ConnectSystem() {
        if(real!=null)
            return real.ConnectSystem();
        return null;
    }

    @Override
    public String connectGuestToSystemConnID(User newGuest) {
        if(real!=null)
            return real.connectGuestToSystemConnID(newGuest);
        return null;
    }

    @Override
    public Response Exit(String connID) {
       if(real!=null)
           return real.Exit(connID);
       return null;
    }

    @Override
    public Response Register(String connID, String userName, String password) {
        if(real!=null)
            return real.Register(connID,userName,password);
        return null;
    }

    @Override
    public String connectSubscriberToSystemConnID(Integer userID) {
        if(real!=null)
            return real.connectSubscriberToSystemConnID(userID);
        return null;
    }

    @Override
    public Response Login(String guestConnID, String userName, String password) {
        if(real!=null)
            return real.Login(guestConnID,userName,password);
        return null;
    }

    @Override
    public Response LoginPublisher(String guestConnID, String userName, String password, Publisher publisher) {
        if(real!=null)
            return real.LoginPublisher(guestConnID,userName,password, publisher);
        return null;
    }

    @Override
    public Response Logout(String connID) {
        if(real!=null)
            return real.Logout(connID);
        return null;
    }

    @Override
    public Response AddStore(int userID, String connID, String storeName) {
        if(real!=null)
            return real.AddStore(userID,connID,storeName);
        return null;
    }

    @Override
    public Response ShowAllStores() {
        if(real!=null)
            return real.ShowAllStores();
        return null;
    }

    @Override
    public Response AddProductToStore(int userID, String connID, int storeID, String productName, String category, double price, int quantity) {
        if(real!=null)
            return real.AddProductToStore(userID,connID,storeID,productName,category,price,quantity);
        return null;
    }

    @Override
    public boolean hasPermission(int userID, int storeID, PermissionEnum.Permission p) {
        if(real!=null)
            return real.hasPermission(userID,storeID,p);
        return false;
    }

    @Override
    public boolean hasPermission(int userID, PermissionEnum.Permission p) {
        if(real!=null)
            return real.hasPermission(userID,p);
        return false;
    }

    @Override
    public Response ChangeQuantityProduct(int userID, String connID, int storeID, int productId, int quantity) {
        if(real!=null)
            return real.ChangeQuantityProduct(userID,connID,storeID,productId,quantity);
        return null;
    }

    @Override
    public Response RemoveProduct(int userID, int storeID, int productID, String connID) {
        if(real!=null)
            return real.RemoveProduct(userID,storeID,productID,connID);
        return null;
    }

    @Override
    public Response ShowStoreProducts(int storeID) {
        if(real!=null)
            return real.ShowStoreProducts(storeID);
        return null;
    }

    @Override
    public Response AddProductToCart(String connID, int StoreId, int productId, int quantity) {
        if(real!=null)
            return real.AddProductToCart(connID,StoreId,productId,quantity);
        return null;
    }

    @Override
    public Response ShowShoppingCart(String connID) {
        if(real!=null)
            return real.ShowShoppingCart(connID);
        return null;
    }

    @Override
    public Response guestPurchase(String connID, String name, String credit_number, String month, String year, String cvv, String ID, String address, String city, String country, String zip) {
        if(real!=null)
            return real.guestPurchase(connID,name,credit_number,month,year,cvv,ID,address,city,country,zip);
        return null;
    }

    @Override
    public Response subscriberPurchase(int userID, String connID, String credit_number, String month, String year, String cvv, String ID, String address, String city, String country, String zip) {
        if(real!=null)
            return real.subscriberPurchase(userID,connID,credit_number,month,year,cvv,ID,address,city,country,zip);
        return null;
    }

    @Override
    public Integer getUserID(String name) {
        if(real!=null)
            return real.getUserID(name);
        return null;
    }

    @Override
    public String getUserConnID(Integer userID) {
        if(real!=null)
            return real.getUserConnID(userID);
        return null;
    }

    @Override
    public void addHistoryToStoreAndUser(ShoppingHistory sh, boolean isGuest) {
        if(real!=null)
            real.addHistoryToStoreAndUser(sh,isGuest);
    }

    @Override
    public Response SearchProduct(String name, String category, int minprice, int maxprice) {
        if(real!=null)
            return real.SearchProduct(name,category,minprice,maxprice);
        return null;
    }
/*
    @Override
    public List<DummyProduct> SearchProductByName(String name, int minprice, int maxprice, int prank, int srank) {
        if(real!=null)
            return real.SearchProduct(name,null,minprice,maxprice);
        return null;
    }

    @Override
    public List<DummyProduct> SearchProductByCategory(String category, int minprice, int maxprice, int prank, int srank) {
        if(real!=null)
            return real.SearchProductByCategory(category,minprice,maxprice,prank,srank);
        return null;
    }

 */
    @Override
    public Response AddNewOwner(int userID, String connID, int storeID, int newOwner) {
        if(real!=null)
            return real.AddNewOwner(userID,connID,storeID,newOwner);
        return null;
    }

    @Override
    public Response systemRoleChecks(int userID, int storeID, int newRole, PermissionEnum.Permission permission) {
        if(real!=null)
            return real.systemRoleChecks(userID,storeID,newRole,permission);
        return null;
    }

    @Override
    public Response AddNewManager(int userID, String connID, int storeID, int newManager) {
        if(real!=null)
            return real.AddNewManager(userID,connID,storeID,newManager);
        return null;
    }

    @Override
    public Response RemoveManager(int userID, String connID, int storeID, int ManagerToRemove) {
        if(real!=null)
            return real.RemoveManager(userID,connID,storeID,ManagerToRemove);
        return null;
    }

    @Override
    public boolean productIsLock(int productID, int storeID) {
        if(real!=null)
            return real.productIsLock(productID,storeID);
        return false;
    }

    @Override
    public void lockProduct(int storeID, int productID) {
        if(real!=null)
            real.lockProduct(storeID,productID);
    }

    @Override
    public Lock getProductLock(int storeID, int productID) {
        if(real!=null)
            return real.getProductLock(storeID,productID);
        return null;
    }

    @Override
    public String getStoreName(int storeID) {
        if(real!=null)
            return real.getStoreName(storeID);
        return null;
    }

    @Override
    public String getProductName(int storeID, int productID) {
        if(real!=null)
            return real.getProductName(storeID,productID);
        return null;
    }

    @Override
    public void unLockProducts(Collection<Integer> values, int storeID) {
        if(real!=null)
            real.unLockProducts(values,storeID);
    }

    @Override
    public Product getProduct(int storeID, int productID) {
        if(real!=null)
            return real.getProduct(storeID,productID);
        return null;
    }

    @Override
    public boolean ValidConnectedUser(int userID, String connID) {
        if(real!=null)
           return real.ValidConnectedUser(userID,connID);
       return false;
    }

    @Override
    public Response reduceProducts(ConcurrentHashMap<Integer, Integer> products, int storeID) {
        if(real!=null)
           return real.reduceProducts(products,storeID);
       return null;
    }

    @Override
    public Response ShowSubscriberHistory(int userID, String connID) {
        if(real!=null)
            return real.ShowSubscriberHistory(userID,connID);
        return null;
    }

    @Override
    public List<DummyShoppingHistory> ShowStoreHistory(int storeId) {
        if(real!=null)
            return real.ShowStoreHistory(storeId);
        return null;
    }

    @Override
    public Response WriteComment(int userId, String connID, int storeId, int productId, String comment) {
        if(real!=null)
            return real.WriteComment(userId,connID,storeId,productId,comment);
        return null;
    }

    @Override
    public Response EditProduct(int userID, String connID, int storeID, int productID, String productName, String category, double price, int quantity) {
        if(real!=null)
            return real.EditProduct(userID,connID,storeID,productID,productName,category,price,quantity);
        return null;
    }

    @Override
    public Response StoreHistoryOwner(int userID, int storeID, String connID) {
        if(real!=null)
            return real.StoreHistoryOwner(userID,storeID,connID);
        return null;
    }

    @Override
    public void printCommentForProduct(int storeID, int productID) {
        if(real!=null)
            real.printCommentForProduct(storeID,productID);
    }

    @Override
    public void PayToTheSellers(Double finalPrice, Integer storeID) {
        if(real!=null)
            real.PayToTheSellers(finalPrice,storeID);
    }

    @Override
    public Response editProductQuantityFromCart(String connID, int storeID, int productID, int quantity) {
        if(real!=null)
            return real.editProductQuantityFromCart(connID,storeID,productID,quantity);
        return null;
    }

    @Override
    public Response RemoveProductFromCart(String connID, int storeID, int productID) {
        if(real!=null)
            return real.RemoveProductFromCart(connID,storeID,productID);
        return null;
    }

    @Override
    public Double calculateBugPrice(int userID, int storeID, ConcurrentHashMap<Integer, Integer> productsInTheBug) {
        if(real!=null)
            return real.calculateBugPrice(userID,storeID,productsInTheBug);
        return -1.0;
    }

    @Override
    public Response ShowAllUsers(int adminID, String connID) {
        if(real!=null)
            return real.ShowAllUsers(adminID,connID);
        return null;
    }

    @Override
    public Response ShowOwnerStores(int userID, String connID) {
        if(real!=null)
            return real.ShowOwnerStores(userID,connID);
        return null;
    }

    @Override
    public Response ShowManagerStores(int userID, String connID) {
        if(real!=null)
            return real.ShowManagerStores(userID,connID);
        return null;
    }

    @Override
    public Response StoreHistoryAdmin(int AdminID, int storeID, String connID) {
        if(real!=null)
            return real.StoreHistoryAdmin(AdminID,storeID,connID);
        return null;
    }

    @Override
    public Response UserHistoryAdmin(int AdminID, int userID, String connID) {
        if(real!=null)
            return real.UserHistoryAdmin(AdminID,userID,connID);
        return null;
    }

    @Override
    public Response AllStoresHistoryAdmin(int AdminID, String connID) {
        if(real!=null)
            return real.AllStoresHistoryAdmin(AdminID,connID);
        return null;
    }

    @Override
    public Response AllUsersHistoryAdmin(int AdminID, String connID) {
        if(real!=null)
            return real.AllUsersHistoryAdmin(AdminID,connID);
        return null;
    }

    @Override
    public Response EditManagerPermissions(int userID, String connID, int storeID, int managerID, List<PermissionEnum.Permission> permissions) {
        if(real!=null)
            return real.EditManagerPermissions(userID,connID,storeID,managerID,permissions);
        return null;
    }

    @Override
    public PermissionEnum.Permission changeToPermission(String per) {
        if(real!=null)
            return real.changeToPermission(per);
        return null;
    }

    @Override
    public Response GetPossiblePermissionsToManager(int userID, String connID, int storeID) {
        if(real!=null)
            return real.GetPossiblePermissionsToManager(userID,connID,storeID);
        return null;
    }

    @Override
    public Response ShowStoreWorkers(int userID, String connID, int storeID) {
        if(real!=null)
            return real.ShowStoreWorkers(userID,connID,storeID);
        return null;
    }

    @Override
    public Response RemoveOwnerByOwner(int ownerID, String connID, int removeOwnerID, int storeID) {
        if(real!=null)
            return real.RemoveOwnerByOwner(ownerID,connID,removeOwnerID,storeID);
        return null;
    }

    @Override
    public Response ShowAllMyStores(String connID, int userID, boolean founder, boolean owner, boolean manager) {
        if(real!=null)
            return real.ShowAllMyStores(connID,userID,founder,owner,manager);
        return null;
    }

    @Override
    public Response addDiscountPolicy(int userID, String connID, int storeID, Sale sale) {
        if(real!=null)
            return real.addDiscountPolicy(userID, connID, storeID,  sale);
        return null;
    }

    @Override
    public Response addBuyingPolicy(int userID, String connID, int storeID, Expression exp ) {
        if(real!=null)
            return real.addBuyingPolicy( userID, connID, storeID,  exp);
        return null;
    }

    @Override
    public void AddStoreToList(Store store) {
        if(real!=null)
            real.AddStoreToList(store);
    }

    @Override
    public Response RemoveBuyingPolicy(int userID, int storeID, String connID) {
        if(real!=null)
            return real.RemoveBuyingPolicy(userID, storeID, connID);
        return null;
    }

    @Override
    public Response RemoveDiscountPolicy(int userID, int storeID, String connID) {
        if(real!=null)
            return real.RemoveDiscountPolicy( userID, storeID, connID);
        return null;
    }

    @Override
    public Response GetPoliciesInfo(int userID, int storeID, String connID) {
        if(real!=null)
            return real.GetPoliciesInfo( userID, storeID, connID);
        return null;
    }

    @Override
    public Expression CreateExpForBuy(Integer storeID, Map<String, Object> map) {
        if(real!=null)
            return real.CreateExpForBuy(storeID, map);
        return null;
    }

    @Override
    public Sale createSaleForDiscount(int storeID, Map<String, Object> obj) {
        if(real!=null)
            return real.createSaleForDiscount( storeID, obj);
        return null;
    }

    @Override
    public Response GetAllSubscribers(String connID, int userID) {
        if(real!=null)
            return real.GetAllSubscribers(connID, userID);
        return null;
    }

    @Override
    public Response getDailyIncomeForStore(int userID, int storeID, String connID) {
        if(real!=null)
            return real.getDailyIncomeForStore(userID,storeID,connID);
        return null;
    }

    @Override
    public Response getDailyIncomeForSystem(int userID, String connID) {
        if(real!=null)
            return real.getDailyIncomeForSystem(userID,connID);
        return null;
    }

    @Override
    public Response subscriberBidding(int userID, String connID, int storeID, int productID, int productPrice, int quantity) {
        if (real != null)
            return real.subscriberBidding(userID, connID, storeID, productID, productPrice, quantity);
        return null;
    }

    @Override
    public Response ResponseForSubmissionBidding(int userID, String connID, int storeID, int productID, int productPrice, int userBiddingPrice, int quantity, int mode) {
        if (real != null)
            return real.ResponseForSubmissionBidding(userID, connID, storeID, productID, productPrice, userBiddingPrice, quantity, mode);
        return null;
    }

    @Override
    public Response ShowBids(int userID, String connID, int storeID) {
        if(real!=null)
            return real.ShowBids(userID, connID, storeID);
        return null;
    }

    @Override
    public void setSubscribers(ConcurrentHashMap<Integer, User> subscribers) {

    }

    @Override
    public void setStores(ConcurrentHashMap<Integer, Store> stores) {

    }

    @Override
    public Response ShowSpecialProductInShoppingCart(String connID) {
        if(real!=null)
            return real.ShowSpecialProductInShoppingCart(connID);
        return null;
    }

    @Override
    public Response removeSpecialProductFromCart(String connID, int storeID, int productID) {
        if(real!=null)
            return real.removeSpecialProductFromCart( connID, storeID,productID);
        return null;
    }

    @Override
    public Response subscriberSpecialProductPurchase(int userID, String connID, String credit_number, String month, String year, String cvv, String id, String address, String city, String country, String zip) {
        if(real!=null)
            return real.subscriberSpecialProductPurchase(userID, connID, credit_number, month,  year,cvv,  id,  address, city,  country,  zip);
        return null;
    }
}
