package TradingSystem.Bridge;

import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.checkerframework.checker.units.qual.A;

import java.util.Collection;
import java.util.List;
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
        if(side!=null && className!=null && line!=null && msg!=null)
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
        if(newGuest!=null)
            return real.connectGuestToSystemConnID(newGuest);
        return null;
    }

    @Override
    public Response Exit(String connID) {
       if(connID!=null)
           return real.Exit(connID);
       return null;
    }

    @Override
    public Response Register(String connID, String userName, String password) {
        if(connID!=null && userName!=null && password!=null)
            return real.Register(connID,userName,password);
        return null;
    }

    @Override
    public String connectSubscriberToSystemConnID(Integer userID) {
        if(userID!=null)
            return real.connectSubscriberToSystemConnID(userID);
        return null;
    }

    @Override
    public Response Login(String guestConnID, String userName, String password) {
        if(guestConnID!=null && userName!=null && password!=null)
            return real.Login(guestConnID,userName,password);
        return null;
    }

    @Override
    public Response Logout(String connID) {
        if(connID!=null)
            return real.Logout(connID);
        return null;
    }

    @Override
    public Response AddStore(int userID, String connID, String storeName) {
        if(connID!=null && storeName!=null)
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
        if(userID!=-1 && connID!=null && storeID!=-1 && productName!=null && category!=null && price!=0 && quantity!=0)
            return real.AddProductToStore(userID,connID,storeID,productName,category,price,quantity);
        return null;
    }

    @Override
    public boolean hasPermission(int userID, int storeID, User.Permission p) {
        if(userID!=0 && storeID!=0 && p!=null)
            return real.hasPermission(userID,storeID,p);
        return false;
    }

    @Override
    public boolean hasPermission(int userID, User.Permission p) {
        if(userID!=0 && p!=null)
            return real.hasPermission(userID,p);
        return false;
    }

    @Override
    public Response ChangeQuantityProduct(int userID, String connID, int storeID, int productId, int quantity) {
        if(userID!=0 && connID!=null && storeID!=0 && productId!=0 && quantity!=0)
            return real.ChangeQuantityProduct(userID,connID,storeID,productId,quantity);
        return null;
    }

    @Override
    public Response RemoveProduct(int userID, int storeID, int productID, String connID) {
        if(userID!=0 && storeID!=0 && productID!=0 && connID!=null)
            return real.RemoveProduct(userID,storeID,productID,connID);
        return null;
    }

    @Override
    public Response ShowStoreProducts(int storeID) {
        if(storeID!=0)
            return real.ShowStoreProducts(storeID);
        return null;
    }

    @Override
    public Response AddProductToCart(String connID, int StoreId, int productId, int quantity) {
        if(connID!=null && StoreId!=0 && productId!=0 && quantity!=0)
            return real.AddProductToCart(connID,StoreId,productId,quantity);
        return null;
    }

    @Override
    public Response ShowShoppingCart(String connID) {
        if(connID!=null)
            return real.ShowShoppingCart(connID);
        return null;
    }

    @Override
    public Response guestPurchase(String connID, String name, String credit_number, String phone_number, String address) {
        if(connID!=null && name!=null && credit_number!=null && phone_number!=null && address!=null)
            return real.guestPurchase(connID,name,credit_number,phone_number,address);
        return null;
    }

    @Override
    public Response subscriberPurchase(int userID, String connID, String credit_number, String phone_number, String address) {
        if(userID!=0 && connID!=null && credit_number!=null && phone_number!=null && address!=null)
            return real.subscriberPurchase(userID,connID,credit_number,phone_number,address);
        return null;
    }

    @Override
    public void addHistoryToStoreAndUser(ShoppingHistory sh, boolean isGuest) {
        if(sh!=null)
            real.addHistoryToStoreAndUser(sh,isGuest);
    }

    @Override
    public Response SearchProduct(String name, String category, int minprice, int maxprice) {
        if(name!=null && category!=null)
            return real.SearchProduct(name,category,minprice,maxprice);
        return null;
    }

    @Override
    public List<DummyProduct> SearchProductByName(String name, int minprice, int maxprice, int prank, int srank) {
        if(name!=null)
            return real.SearchProductByName(name,minprice,maxprice,prank,srank);
        return null;
    }

    @Override
    public List<DummyProduct> SearchProductByCategory(String category, int minprice, int maxprice, int prank, int srank) {
        if(category!=null)
            return real.SearchProductByCategory(category,minprice,maxprice,prank,srank);
        return null;
    }

    @Override
    public Response AddNewOwner(int userID, String connID, int storeID, int newOwner) {
        if(connID!=null)
            return real.AddNewOwner(userID,connID,storeID,newOwner);
        return null;
    }

    @Override
    public Response systemRoleChecks(int userID, int storeID, int newRole, User.Permission permission) {
        if(permission!=null)
            return real.systemRoleChecks(userID,storeID,newRole,permission);
        return null;
    }

    @Override
    public Response AddNewManager(int userID, String connID, int storeID, int newManager) {
        if(connID!=null)
            return real.AddNewManager(userID,connID,storeID,newManager);
        return null;
    }

    @Override
    public Response RemoveManager(int userID, String connID, int storeID, int ManagerToRemove) {
        if(connID!=null)
            return real.RemoveManager(userID,connID,storeID,ManagerToRemove);
        return null;
    }

    @Override
    public boolean productIsLock(int productID, int storeID) {
        if(productID!=-1 && storeID!=-1)
            return real.productIsLock(productID,storeID);
        return false;
    }

    @Override
    public void lockProduct(int storeID, int productID) {
        if(storeID!=-1 && productID!=-1)
            real.lockProduct(storeID,productID);
    }

    @Override
    public Lock getProductLock(int storeID, int productID) {
        if(storeID!=-1 && productID!=-1)
            return real.getProductLock(storeID,productID);
        return null;
    }

    @Override
    public String getStoreName(int storeID) {
        if(storeID!=-1)
            return real.getStoreName(storeID);
        return null;
    }

    @Override
    public String getProductName(int storeID, int productID) {
        if(storeID!=-1 && productID!=-1)
            return real.getProductName(storeID,productID);
        return null;
    }

    @Override
    public void unLockProducts(Collection<Integer> values, int storeID) {
        if(values!=null && storeID!=-1)
            real.unLockProducts(values,storeID);
    }

    @Override
    public Product getProduct(int storeID, int productID) {
        if(storeID!=-1 && productID!=-1)
            return real.getProduct(storeID,productID);
        return null;
    }

    @Override
    public boolean ValidConnectedUser(int userID, String connID) {
       if(userID!=-1 && connID!=null)
           return real.ValidConnectedUser(userID,connID);
       return false;
    }

    @Override
    public Response reduceProducts(ConcurrentHashMap<Integer, Integer> products, int storeID) {
       if(products!=null && storeID!=-1)
           return real.reduceProducts(products,storeID);
       return null;
    }

    @Override
    public Response ShowSubscriberHistory(int userID, String connID) {
        if(userID!=-1 && connID!=null)
            return real.ShowSubscriberHistory(userID,connID);
        return null;
    }

    @Override
    public List<DummyShoppingHistory> ShowStoreHistory(int storeId) {
        if(storeId!=-1)
            return real.ShowStoreHistory(storeId);
        return null;
    }

    @Override
    public Response WriteComment(int userId, String connID, int storeId, int productId, String comment) {
        if(userId!=-1 && connID!=null && storeId!=-1 && productId!=-1 && comment!=null)
            return real.WriteComment(userId,connID,storeId,productId,comment);
        return null;
    }

    @Override
    public Response EditProduct(int userID, String connID, int storeID, int productID, String productName, String category, double price, int quantity) {
        if(userID!=-1 && connID!=null && storeID!=-1 && productID!=-1 && productName!=null && category!=null && price!=-1 && quantity!=-1)
            return real.EditProduct(userID,connID,storeID,productID,productName,category,price,quantity);
        return null;
    }

    @Override
    public Response StoreHistoryOwner(int userID, int storeID, String connID) {
        if(userID!=-1 && storeID!=-1 && connID!=null)
            return real.StoreHistoryOwner(userID,storeID,connID);
        return null;
    }

    @Override
    public void printCommentForProduct(int storeID, int productID) {
        if(storeID!=-1 && productID!=-1)
            real.printCommentForProduct(storeID,productID);
    }

    @Override
    public void PayToTheSellers(Double finalPrice, Integer storeID) {
        if(finalPrice!=-1 && storeID!=-1)
            real.PayToTheSellers(finalPrice,storeID);
    }

    @Override
    public Response editProductQuantityFromCart(String connID, int storeID, int productID, int quantity) {
        if(connID!=null && storeID!=-1 && productID!=-1 && quantity!=-1)
            return real.editProductQuantityFromCart(connID,storeID,productID,quantity);
        return null;
    }

    @Override
    public Response RemoveProductFromCart(String connID, int storeID, int productID) {
        if(connID!=null && storeID!=-1 && productID!=-1)
            return real.RemoveProductFromCart(connID,storeID,productID);
        return null;
    }

    @Override
    public Double calculateBugPrice(int userID, int storeID, ConcurrentHashMap<Integer, Integer> productsInTheBug) {
        if(userID!=-1 && storeID!=-1 && productsInTheBug!=null)
            return real.calculateBugPrice(userID,storeID,productsInTheBug);
        return -1.0;
    }

    @Override
    public Response ShowAllUsers(int adminID, String connID) {
        if(adminID!=-1 && connID!=null)
            return real.ShowAllUsers(adminID,connID);
        return null;
    }

    @Override
    public Response ShowOwnerStores(int userID, String connID) {
        if(userID!=-1 && connID!=null)
            return real.ShowOwnerStores(userID,connID);
        return null;
    }

    @Override
    public Response ShowManagerStores(int userID, String connID) {
        if(userID!=-1 && connID!=null)
            return ShowManagerStores(userID,connID);
        return null;
    }

    @Override
    public Response StoreHistoryAdmin(int AdminID, int storeID, String connID) {
        if(AdminID!=-1 && storeID!=-1 && connID!=null)
            return real.StoreHistoryAdmin(AdminID,storeID,connID);
        return null;
    }

    @Override
    public Response UserHistoryAdmin(int AdminID, int userID, String connID) {
        if(AdminID!=-1 && userID!=-1 && connID!=null)
            return real.UserHistoryAdmin(AdminID,userID,connID);
        return null;
    }

    @Override
    public Response AllStoresHistoryAdmin(int AdminID, String connID) {
        if(AdminID!=-1 && connID!=null)
            return real.AllStoresHistoryAdmin(AdminID,connID);
        return null;
    }

    @Override
    public Response AllUsersHistoryAdmin(int AdminID, String connID) {
        if(AdminID!=-1 && connID!=null)
            return real.AllUsersHistoryAdmin(AdminID,connID);
        return null;
    }

    @Override
    public Response EditManagerPermissions(int userID, String connID, int storeID, int managerID, List<User.Permission> permissions) {
        if(userID!=-1 && connID!=null && storeID!=-1 && managerID!=-1 && permissions!=null)
            return real.EditManagerPermissions(userID,connID,storeID,managerID,permissions);
        return null;
    }

    @Override
    public User.Permission changeToPermission(String per) {
        if(per!=null)
            return real.changeToPermission(per);
        return null;
    }

    @Override
    public Response GetPossiblePermissionsToManager(int userID, String connID) {
        if(userID!=-1 && connID!=null)
            return real.GetPossiblePermissionsToManager(userID,connID);
        return null;
    }

    @Override
    public Response ShowStoreWorkers(int userID, String connID, int storeID) {
        if(userID!=-1 && connID!=null && storeID!=-1)
            return real.ShowStoreWorkers(userID,connID,storeID);
        return null;
    }

    @Override
    public Response RemoveOwnerByOwner(int ownerID, String connID, int removeOwnerID, int storeID) {
        if(ownerID!=-1 && connID!=null && removeOwnerID!=-1 && storeID!=-1)
            return real.RemoveOwnerByOwner(ownerID,connID,removeOwnerID,storeID);
        return null;
    }
}
