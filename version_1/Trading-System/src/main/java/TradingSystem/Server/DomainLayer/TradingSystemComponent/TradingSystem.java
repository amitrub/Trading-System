package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingCart;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.UserComponent.*;
import TradingSystem.Server.ServiceLayer.DummyObject.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public interface TradingSystem {


    public void ClearSystem();

    public void Initialization();

    public String errMsgGenerator(String side, String className, String line, String msg);
    //prints for debug
    public void printUsers();
    public void printStores();
    public void printProducts();
    public Response ConnectSystem();
    String connectGuestToSystemConnID(User newGuest);
    public Response Exit(String connID);
    public Response Register(String connID, String userName, String password);
    public String connectSubscriberToSystemConnID(Integer userID);
    public Response Login(String guestConnID, String userName, String password);
    public Response Logout(String connID);
    public Response AddStore(int userID, String connID, String storeName);
    public Response ShowAllStores();
    public Response AddProductToStore(int userID, String connID, int storeID, String productName, String category, double price, int quantity);
    public boolean hasPermission(int userID, int storeID, User.Permission p);
    public boolean hasPermission(int userID, User.Permission p);
    public Response ChangeQuantityProduct(int userID, String connID, int storeID, int productId, int quantity);
    public Response RemoveProduct(int userID, int storeID, int productID, String connID);
    public Response ShowStoreProducts(int storeID);
    public Response AddProductToCart(String connID, int StoreId, int productId, int quantity);
    public Response ShowShoppingCart(String connID);
    public Response guestPurchase(String connID, String name, String credit_number, String phone_number, String address);
    public Response subscriberPurchase(int userID, String connID, String credit_number, String phone_number, String address);
    public void addHistoryToStoreAndUser(ShoppingHistory sh, boolean isGuest);
    public Response SearchProduct(String name, String category, int minprice, int maxprice);
    //TODO: to move?
    public List<DummyProduct> SearchProductByName(String name, int minprice, int maxprice, int prank , int srank);
    //TODO: to move?
    public List<DummyProduct> SearchProductByCategory(String category, int minprice, int maxprice, int prank , int srank);

    public Response AddNewOwner(int userID, String connID, int storeID, int newOwner);

    public Response systemRoleChecks(int userID, int storeID, int newRole, User.Permission permission);

    public Response AddNewManager(int userID, String connID, int storeID, int newManager);

    public Response RemoveManager(int userID, String connID, int storeID, int ManagerToRemove);

    public boolean productIsLock(int productID, int storeID);
    public void lockProduct(int storeID, int productID);
    public Lock getProductLock(int storeID, int productID);
    public String getStoreName(int storeID);
    public String getProductName(int storeID, int productID);
    public void unLockProducts(Collection<Integer> values, int storeID);

    public Product getProduct(int storeID, int productID);
    public boolean ValidConnectedUser(int userID, String connID);

    public Response reduceProducts(ConcurrentHashMap<Integer, Integer> products, int storeID);

    public Response ShowSubscriberHistory(int userID, String connID);
    public List<DummyShoppingHistory> ShowStoreHistory(int storeId);
    public Response WriteComment(int userId, String connID, int storeId, int productId, String comment);
    public Response EditProduct(int userID, String connID, int storeID, int productID, String productName, String category, double price, int quantity);
    public Response StoreHistoryOwner(int userID, int storeID, String connID);
    public void printCommentForProduct(int storeID, int productID);
    public void PayToTheSellers(Double finalPrice, Integer storeID);
    public Response editProductQuantityFromCart(String connID, int storeID, int productID, int quantity);
    public Response RemoveProductFromCart(String connID, int storeID, int productID);
    public Double calculateBugPrice(int userID, int storeID, ConcurrentHashMap<Integer, Integer> productsInTheBug);
    public Response ShowAllUsers(int adminID, String connID);
    public Response ShowOwnerStores(int userID, String connID);
    public Response ShowManagerStores(int userID, String connID);
    //Admin

    public Response StoreHistoryAdmin(int AdminID, int storeID, String connID);
    public Response UserHistoryAdmin(int AdminID, int userID, String connID);
    public Response AllStoresHistoryAdmin(int AdminID, String connID);
    public Response AllUsersHistoryAdmin(int AdminID, String connID);
    public Response EditManagerPermissions(int userID, String connID, int storeID, int managerID, List<User.Permission> permissions);
    public User.Permission changeToPermission(String per);
    public Response GetPossiblePermissionsToManager(int userID, String connID);
    public Response ShowStoreWorkers(int userID, String connID, int storeID);
    public Response RemoveOwnerByOwner(int ownerID, String connID, int removeOwnerID, int storeID);

}
