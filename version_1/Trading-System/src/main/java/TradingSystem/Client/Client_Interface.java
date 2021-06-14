package TradingSystem.Client;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.UserComponent.PermissionEnum;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.List;

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
    public Response Login(String userName, String pass);

    /**
     * @requirement 2.5
     * @return int userID
     */
    public Response showAllStores();
    public Response showStoreProducts(int storeID);

    public Response showAllStoresSubscriber();
    public Response showStoreProductsSubscriber(int storeID);


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
    public Response Search(String mode, String name, String minPrice, String maxPrice, String p_rank, String s_rank);

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
    public Response showShoppingCart();
    public Response removeFromShoppingCart(int storeID, int productID);
    public Response editShoppingCart(int storeID, int productID, int quantity);

    /**
     * @requirement 2.9
     * @param name
     * @param credit_number
     * @param month
     * @param year
     * @param cvv
     * @param ID
     * @param address
     * @param city
     * @param country
     * @param zip
     * @return
     */
    public Response guestPurchase(String name, String credit_number, String month, String year, String cvv, String ID, String address, String city, String country, String zip);

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
     * @param month
     * @param year
     * @param cvv
     * @param ID
     * @param address
     * @param city
     * @param country
     * @param zip
     * @return
     */
    public Response subscriberPurchase(String credit_number, String month, String year, String cvv, String ID, String address, String city, String country, String zip);
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
    public Response addProduct(int storeID, String productName, String category, double price, int quantity);

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
    public Response editProduct(int storeID, int productID, String productName, String category, double price, int quantity);

    /**
     * @requirement 4.2.1
     * @param storeID
     * @return Response
     */
    public Response addBuyingPolicy(int storeID);

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
    public Response addOwner(int storeID, int newOwnerID);

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
    public Response editManagerPermissions(int storeID, int managerID, List<PermissionEnum.Permission> permissions);
    public Response GetPossiblePermissionsToManager(int storeId);

    /**
     * @requirement 4.7
     * @param storeID
     * @param managerToRemoveID
     * @return boolean
     */
    public Response removeManager(int storeID, int managerToRemoveID);
    public Response showOwnerStores();
    public Response showManagerStores();

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
    public Response ownerStoreHistory(int storeID);

    //Admin
    /**
     * requirement 6.4
     * @param storeID
     * @return List<DummyShoppingHistory>
     */
    public Response adminStoreHistory(int storeID);
    /**
     * requirement 6.4
     * @param userToShow
     * @return List<DummyShoppingHistory>
     */
    public Response adminUserHistory(int userToShow);
    /**
     * requirement 6.4
     * @return List<DummyShoppingHistory>
     */
    public Response AdminAllStores();
    /**
     * requirement 6.4
     * @return List<DummyShoppingHistory>
     */
    public Response AdminAllUsers();

    /**
     * requirement 6.6
     * @return Double
     */
    public Response AdminDailyIncomeForSystem();

    /**
     * requirement 4.12
     * @return Double
     */
    public Response  OwnerDailyIncomeForStore(int storeID);

    /**
     * requirement 8.3.1
     * @return Response
     */
    public Response submissionBidding( int storeID,int productID,int quantity,int productPrice);


    /**
     * requirement 8.3.2
     * @return Response
     */
    public Response ResponseForSubmissionBidding( int storeID,int productID, int userWhoOffer,int quantity,int productPrice,int mode);

    /**
     * requirement 8.3.3
     * @return Response
     */
    public Response RemoveSpecialProductProductFromCart( String connID, int storeID, int productID);

    /**
     * requirement 8.3-None
     * @return Response
     */
    public Response ShowSpecialProductsInShoppingCart(String connID);

    /**
     * requirement 8.3-None
     * @return Response
     */
    public Response ShowBids( int storeID);

    public Response getStoreIDByName(String storeName);

    public Response getProductIDByName(String productName, int StoreID);

}
