package TradingSystem.Client;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.DiscountPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface Client_Interface {


    public int getUserID();
    public String getUserName();
    public boolean isSubscriber();
    public boolean isOwner();
    public String getConnID();

    public void clearSystem();
    /**
     * @requirement 2.1
     *
     *
     */
    public void connectSystem();
    public void connectSystemNew(int port);

    /**
     * @requirement 2.2
     * */
    public void exitSystem();

    /**
     * @requirement 2.3
     * @param userName userName
     * @param pass pass
     * @return int if ok
     */
    public int Register(String userName, String pass);

    /**
     * @requirement 2.4
     * @param userName userName
     * @param pass pass
     * @return int if ok
     */
    public int Login(String userName, String pass);
    /**
     * @requirement 2.5
     * @return int userID
     */
    public List<DummyStore> showAllStores();
    public List<DummyProduct> showStoreProducts(int storeID);
    /**
     * @requirement 2.6
     * @param mode mode
     * @param name name
     * @param minPrice minPrice
     * @param maxPrice maxPrice
     * @param p_rank p_rank
     * @param s_rank s_rank
     * @return List<DummyProduct>
     */
    public List<DummyProduct> Search(String mode, String name, String minPrice, String maxPrice, String p_rank, String s_rank);

    /**
     * @requirement 2.7
     * @param storeID  storeID
     * @param productID productID
     * @param quantity quantity
     * @return response
     */
    public Response addProductToCart(int storeID, int productID, int quantity);

    /**
     * @requirement 2.8
     * @return List<DummyProduct>
     */
    public List<DummyProduct> showShoppingCart();
    public Response removeFromShoppingCart(int storeID, int productID);
    public Response editShoppingCart(int storeID, int productID, int quantity);

    /**
     * @requirement 2.9
     * @param name
     * @param credit_number
     * @param phone_number
     * @param address
     * @return
     */
    public Response guestPurchase(String name, String credit_number, String phone_number, String address);

    //Subscriber

    /**
     * @requirement 3.1
     *
     * @return String userID
     */
    public Response Logout();

    /**
     * @requirement 3.2 open store
     * @param storeName
     * @return
     */
    public Response openStore(String storeName);

    /**
     * @requirement 3.7 show history
     * @return
     */
    public Response showUserHistory();

    /**
     * @requirement 3.3 write comment
     * @param storeID
     * @param productID
     * @param rate
     * @param review
     * @return
     */
    public Response writeComment(int storeID, int productID, double rate, String review);

    /**
     * @requirement 3.4 subscriber (user) purchase
     * @param credit_number
     * @param phone_number
     * @param address
     * @return
     */
    public boolean subscriberPurchase(String credit_number, String phone_number, String address);
    //Store Owner Service

    /**
     * @requirement 4.1.1
     * @param storeID
     * @param productName
     * @param category
     * @param price
     * @param quantity
     * @return boolean
     */
    public boolean addProduct(int storeID, String productName, String category, double price, int quantity);

    /**
     * @requirement 4.1.2
     * @param storeID
     * @param productID
     * @return Response
     */
    public Response removeProduct(int storeID, int productID);

    /**
     * @requirement 4.1.3
     * @param storeID
     * @param productID
     * @param productName
     * @param category
     * @param price
     * @param quantity
     * @return boolean
     */
    public boolean editProduct(int storeID, int productID, String productName, String category, double price, int quantity);

    /**
     * @requirement 4.2.1
     * @param storeID
     * @param storeID
     * @return Response
     */
    public Response addBuyingPolicy(int storeID, Expression exp);

    /**
     * @requirement 4.2.2
     * @param storeID
     * @param storeID
     * @return Response
     */
    public Response addDiscountPolicy(int storeID, Sale sale);

    /**
     * @requirement 4.2.3
     * @param storeID
     * @param storeID
     * @return Response
     */
    public Response removeBuyingPolicy(int storeID);

    /**
     * @requirement 4.2.4
     * @param storeID
     * @param storeID
     * @return Response
     */
    public Response removeDiscountPolicy(int storeID);

    /**
     * @requirement 4.2.5
     * @param storeID
     * @param storeID
     * @return Response
     */
    public Response getPoliciesInfo(int storeID);

    /**
     * @requirement 4.3
     * @param storeID
     * @param newOwnerID
     * @return boolean
     */
    public boolean addOwner(int storeID, int newOwnerID);

    /**
     * @requirement 4.4
     * @param storeID
     * @param ownerToRemoveID
     * @return Response
     */
    public Response removeOwner(int storeID, int ownerToRemoveID);

    /**
     * @requirement 4.5
     * @param storeID
     * @param newManagerID
     * @return Response
     */
    public Response addManager(int storeID, int newManagerID);

    /**
     * @requirement 4.6
     * @param storeID storeID
     * @param managerID managerID
     * @param permissions permissions
     * @return Response
     */
    public Response editManagerPermissions(int storeID, int managerID, List<User.Permission> permissions);
    public Response GetPossiblePermissionsToManager();

    /**
     * @requirement 4.7
     * @param storeID
     * @param managerToRemoveID
     * @return boolean
     */
    public boolean removeManager(int storeID, int managerToRemoveID);
    public List<DummyStore> showOwnerStores();
    public List<DummyStore> showManagerStores();

    /**
     * requirement 4.9
     * @param storeID
     * @return Response
     */
    public Response showStoreWorkers(int storeID);

    /**
     * requirement 4.11
     * @param storeID
     * @return List<DummyShoppingHistory>
     */
    public List<DummyShoppingHistory> ownerStoreHistory(int storeID);

    //Admin
    /**
     * requirement 6.4
     * @param storeID
     * @return List<DummyShoppingHistory>
     */
    public List<DummyShoppingHistory> adminStoreHistory(int storeID);
    /**
     * requirement 6.4
     * @param userToShow
     * @return List<DummyShoppingHistory>
     */
    public List<DummyShoppingHistory> adminUserHistory(int userToShow);
    /**
     * requirement 6.4
     * @return List<DummyShoppingHistory>
     */
    public List<DummyShoppingHistory> AdminAllStores();
    /**
     * requirement 6.4
     * @return List<DummyShoppingHistory>
     */
    public List<DummyShoppingHistory> AdminAllUsers();


}
