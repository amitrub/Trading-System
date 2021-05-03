package TradingSystem.Client;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.ServiceLayer.Bridge.Trading_Driver;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientProxy implements Client_Interface {

    TradingSystem tradingSystem= Trading_Driver.getTradingSystem();
    String ConnID;
    int userID;
    private String userName;
    private String pass;

    private Client real;

    public ClientProxy(){
        real=null;
    }

    public void setRealBridge(Client implementation) {
        if (real == null)
            real = implementation;
    }


//    @Override
//    public String getUserName() {
//        return null;
//    }
//
//    @Override
//    public boolean isSubscriber() {
//        return false;
//    }
//
//    @Override
//    public boolean isOwner() {
//        return false;
//    }
//
//    @Override
//    public String getConnID() {
//        return null;
//    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public boolean isSubscriber() {
        return this.userID != -1 && !this.ConnID.equals("");
    }

    @Override
    public boolean isOwner() {
        return true;
    }

    @Override
    public String getConnID() {
        return ConnID;
    }

    @Override
    public int getUserID(){
        return userID;
    }

    @Override
    public void clearSystem() {
        tradingSystem.ClearSystem();
    }

    @Override
    public void connectSystem() {
        String id= tradingSystem.ConnectSystem().returnConnID();
        ConnID=id;
    }

    @Override
    public void connectSystemNew(int port) {

    }

    @Override
    public void exitSystem() {
        tradingSystem.Exit(ConnID);
    }

    @Override
    public int Register(String userName, String pass) {
        Response response=tradingSystem.Register(ConnID,userName,pass);
        this.ConnID= response.returnConnID();
        this.userID= response.returnUserID();
        return userID;
    }

    @Override
    public int Login(String userName, String pass) {
        Response response = tradingSystem.Login(ConnID,userName,pass);
        this.ConnID= response.returnConnID();
        this.userID= response.returnUserID();
        return userID;
    }

    @Override
    public List<DummyStore> showAllStores() {
        return tradingSystem.ShowAllStores().getStores();
    }

    @Override
    public List<DummyProduct> showStoreProducts(int storeID) {
        return tradingSystem.ShowStoreProducts(storeID).returnProductList();
    }

    @Override
    public List<DummyProduct> Search(String mode, String name, String minPrice, String maxPrice, String p_rank, String s_rank) {
        try {
            Integer min = Integer.parseInt(minPrice);
            Integer max = Integer.parseInt(maxPrice);
            if (mode.equals("Product Name")) {
                return tradingSystem.SearchProduct(name, null, min, max).returnProductList();
            } else {
                return tradingSystem.SearchProduct(null, name, min, max).returnProductList();
            }
        } catch (Exception ex){
            return new ArrayList<>();
        }
    }

    @Override
    public Response addProductToCart(int storeID, int productID, int quantity) {
        return tradingSystem.AddProductToCart(this.ConnID,storeID,productID,quantity);
    }

    @Override
    public List<DummyProduct> showShoppingCart() {
        return tradingSystem.ShowShoppingCart(this.ConnID).returnProductList();
    }

    @Override
    public Response removeFromShoppingCart(int storeID, int productID) {
        return tradingSystem.RemoveProductFromCart(ConnID,storeID,productID);
    }

    @Override
    public Response editShoppingCart(int storeID, int productID, int quantity) {
        return tradingSystem.editProductQuantityFromCart(ConnID,storeID,productID,quantity);
    }

    @Override
    public Response guestPurchase(String name, String credit_number, String phone_number, String address) {
        return tradingSystem.guestPurchase(ConnID,name,credit_number,phone_number,address);
    }

    @Override
    public Response Logout() {
        Response response=tradingSystem.Logout(this.ConnID);
        this.ConnID=response.returnConnID();
        this.userID=-1;
        return response;
    }

    @Override
    public Response openStore(String storeName) {
        return tradingSystem.AddStore(userID,ConnID,storeName);
    }

    @Override
    public Response showUserHistory() {
        return tradingSystem.ShowSubscriberHistory(userID,ConnID);
    }

    @Override
    public Response writeComment(int storeID, int productID, double rate, String review) {
        return tradingSystem.WriteComment(userID,ConnID,storeID,productID,review);
    }

    @Override
    public boolean subscriberPurchase(String credit_number, String phone_number, String address) {
        return tradingSystem.subscriberPurchase(userID,ConnID,credit_number,phone_number,address).getIsErr();
    }

    @Override
    public boolean addProduct(int storeID, String productName, String category, double price, int quantity) {
        return tradingSystem.AddProductToStore(userID,ConnID,storeID,productName,category,price,quantity).getIsErr();
    }

    @Override
    public Response removeProduct(int storeID, int productID) {
        return tradingSystem.RemoveProduct(userID,storeID,productID,ConnID);
    }

    @Override
    public boolean editProduct(int storeID, int productID, String productName, String category, double price, int quantity) {
        return tradingSystem.EditProduct(userID,ConnID,storeID,productID,productName,category,price,quantity).getIsErr();
    }

    @Override
    public Response addBuyingPolicy(int storeID, Expression exp){
        return tradingSystem.addBuyingPolicy(userID, ConnID, storeID, exp);
    }

    @Override
    public Response addDiscountPolicy(int storeID, Sale sale){
        return tradingSystem.addDiscountPolicy(userID, ConnID, storeID, sale);
    }

    @Override
    public Response editBuyingPolicy(int storeID){
        return new Response("not implemented");
    }

    @Override
    public Response editDiscountPolicy(int storeID){
        return new Response("not implemented");
    }

    @Override
    public Response removeBuyingPolicy(int storeID){
        return tradingSystem.RemoveBuyingPolicy(userID,storeID,ConnID);
    }

    @Override
    public Response removeDiscountPolicy(int storeID){ return tradingSystem.RemoveDiscountPolicy(userID,storeID,ConnID); }

    @Override
    public boolean addOwner(int storeID, int newOwnerID) {
        return tradingSystem.AddNewOwner(userID,ConnID,storeID,newOwnerID).getIsErr();
    }

    @Override
    public Response removeOwner(int storeID, int ownerToRemoveID) {
        return tradingSystem.RemoveOwnerByOwner(userID,ConnID,ownerToRemoveID,storeID);
    }

    @Override
    public Response addManager(int storeID, int newManagerID) {
        return tradingSystem.AddNewManager(userID,ConnID,storeID,newManagerID);
    }

    @Override
    public Response editManagerPermissions(int storeID, int managerID, List<User.Permission> permissions) {
       return tradingSystem.EditManagerPermissions(userID,ConnID,storeID,managerID,permissions);
        //return null;
    }

    @Override
    public Response GetPossiblePermissionsToManager() {
        return tradingSystem.GetPossiblePermissionsToManager(userID,ConnID);
    }

    @Override
    public boolean removeManager(int storeID, int managerToRemoveID) {
        return tradingSystem.RemoveManager(userID,ConnID,storeID,managerToRemoveID).getIsErr();
    }

    @Override
    public List<DummyStore> showOwnerStores() {
        return tradingSystem.ShowOwnerStores(userID,ConnID).getStores();
    }

    @Override
    public List<DummyStore> showManagerStores() {
        return tradingSystem.ShowManagerStores(userID,ConnID).getStores();
    }

    @Override
    public Response showStoreWorkers(int storeID) {
        return tradingSystem.ShowStoreWorkers(userID,ConnID,storeID);
    }

    @Override
    public List<DummyShoppingHistory> ownerStoreHistory(int storeID) {
        return tradingSystem.StoreHistoryOwner(userID,storeID,ConnID).returnHistoryList();
    }

    @Override
    public List<DummyShoppingHistory> adminStoreHistory(int storeID) {
        return tradingSystem.StoreHistoryAdmin(userID,storeID,ConnID).returnHistoryList();
    }

    @Override
    public List<DummyShoppingHistory> adminUserHistory(int userToShow) {
        return tradingSystem.UserHistoryAdmin(userID,userToShow,ConnID).returnHistoryList();
    }

    @Override
    public List<DummyShoppingHistory> AdminAllStores() {
        return tradingSystem.AllStoresHistoryAdmin(userID,ConnID).returnHistoryList();
    }

    @Override
    public List<DummyShoppingHistory> AdminAllUsers() {
        return tradingSystem.AllUsersHistoryAdmin(userID,ConnID).returnHistoryList();
    }
}
