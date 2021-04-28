package TradingSystem.Server.DomainLayer.TradingSystemComponent;


import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.UserComponent.OwnerPermission;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;

import java.util.LinkedList;
import java.util.List;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertTrue;


import static org.junit.jupiter.api.Assertions.*;

class TradingSystemTest {

    private static TradingSystem tradingSystem=TradingSystem.getInstance();

    private static int userID;
    private static int userID1;
    private static int storeid;
    private static int productId;
    private static int productId2;
    private static String connID;
    private static String connID1;

    private static String EconnID;
    private static String NconnID;
    private static int ElinorID;
    private static int NofetID;
    private static int NofetStore;
    private static int ElinorStore;

    @BeforeAll
    public static void setup() {
        connID= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.Register(connID,"reutlevy","8119");

        userID= response.returnUserID();
        connID= tradingSystem.Login(connID,"reutlevy","8119").returnConnID();


        Response response1= tradingSystem.AddStore(userID,connID,"store8");
        for(Store store1: tradingSystem.stores.values()){
            if(store1.getName().equals("store8")){
                storeid=store1.getId();
            }
        }
        tradingSystem.AddProductToStore(userID,connID,storeid,"test","test1",19.0,9);
        productId=tradingSystem.stores.get(storeid).getProductID("test");
        tradingSystem.AddProductToStore(userID,connID,storeid,"test2","test",19.0,9);
        productId2=tradingSystem.stores.get(storeid).getProductID("test2");

        connID1= tradingSystem.ConnectSystem().returnConnID();
        response1= tradingSystem.Register(connID1,"reutlevy8","8119");
        userID1= response1.returnUserID();
        connID1= tradingSystem.Login(connID1,"reutlevy8","8119").returnConnID();


        NconnID = tradingSystem.ConnectSystem().returnConnID();
        Response r1=tradingSystem.Register(NconnID, "nofet", "123");
        NofetID= r1.returnUserID();
        NconnID=tradingSystem.Login(NconnID, "nofet", "123").returnConnID();

        EconnID = tradingSystem.ConnectSystem().returnConnID();
        Response r2=tradingSystem.Register(EconnID, "elinor", "123");
        ElinorID= r2.returnUserID();
        EconnID=tradingSystem.Login(EconnID, "elinor", "123").returnConnID();


        tradingSystem.AddStore(NofetID,NconnID,"NofetStore");
        tradingSystem.AddStore(ElinorID,EconnID,"ElinorStore");
        for(Store store1: tradingSystem.stores.values()){
            if(store1.getName().equals("NofetStore")){
                NofetStore=store1.getId();
            }
            if(store1.getName().equals("ElinorStore")){
                ElinorStore=store1.getId();
            }
        }
        Response response2= tradingSystem.AddNewOwner(userID,connID,storeid,ElinorID);
    }

    @Test
    void AddProductSuccess(){
        Response response=tradingSystem.AddProductToStore(userID,connID,storeid,"prod8","food",11.0,9);
        assertFalse(response.getIsErr());
    }

    @Test
    void ChangeProductQuantitySuccess(){
        Response response=tradingSystem.ChangeQuantityProduct(userID,connID,storeid,productId,10);
        assertFalse(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void EditProductSuccess(){
        Response response=tradingSystem.EditProduct(userID,connID,storeid,productId,"prod3","food",12.0,9);
        assertFalse(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void EditProductInvalidPremmision(){
        String connID1= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.Register(connID1,"reutlevy30","8119");
        int userID1= response.returnUserID();
        connID1= tradingSystem.Login(connID1,"reutlevy30","8119").returnConnID();
        response=tradingSystem.EditProduct(userID1,connID1,storeid,productId,"prod4","food",12.0,9);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void AddProductInvalidAmount(){
        Store store= new Store("store11", userID);
        tradingSystem.AddStore(userID,connID,"store11");
        int storeid=0;
        for(Store store1: tradingSystem.stores.values()){
            if(store1.getName().equals("store11")){
                storeid=store1.getId();
            }
        }
        Response response=tradingSystem.AddProductToStore(userID,connID,storeid,"prod3","food",11.0,-1);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void EditProductWrongPrice(){
        Response response=tradingSystem.EditProduct(userID,connID,storeid,productId,"prod4","food",-12.0,9);
        assertTrue(response.getIsErr());
    }

    // requirement 2.1
    @Test
    void connectSystem() {
        Response response= tradingSystem.ConnectSystem();
        assertTrue(response.returnConnID()!="" && response.getIsErr()==false);
    }

    // requirement 2.2
    @Test
    void exitGood() {
        String connId= tradingSystem.ConnectSystem().returnConnID();
        Response response=tradingSystem.Exit(connId);
        assertFalse(response.getIsErr());
    }

    // requirement 2.2
    @Test
    void exiBad() {
        String connId= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Exit(connId);
        Response response=tradingSystem.Exit(connId);
        assertTrue(response.getIsErr());
    }

    // requirement 2.3
    @Test
    void registerGood() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.Register(connID,"reutlevy11","8111996");
        assertFalse(response.getIsErr());
    }

    // requirement 2.3
    @Test
    void registerExistUser() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.Register(connID,"reutlevy30","8111996");
        response= tradingSystem.Register(connID,"reutlevy30","reut");
        assertTrue(response.getIsErr());
    }
    //TODO- implement this
    void registerInvalidPassword() {

    }

    // requirement 2.4
    @Test
    void loginSucess() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(connID,"reutlevy300","811199");
        Response response=tradingSystem.Login(connID,"reutlevy300","811199");
        assertFalse(response.getIsErr() && response.returnUserID()<0);
    }

    // requirement 2.4
    @Test
    void loginWrongUserName() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        Response response=tradingSystem.Login(connID,"reutlevy3","811199");
        assertTrue(response.getIsErr());
    }

    // requirement 2.4
    @Test
    void loginWrongPassword() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(connID,"reutlevy30","811199");
        Response response=tradingSystem.Login(connID,"reutlevy30","8111996");
        assertTrue(response.getIsErr());
    }

    // requirement 2.5
    @Test
    void showAllStoresGood() {
        Response res = tradingSystem.ShowAllStores();
        assertEquals(res.getIsErr(),false);
    }

    // requirement 3.2
    @Test
    void AddStoreNotSubscriber() { ;
        Response response= tradingSystem.AddStore(11,connID,"Store3");
        assertTrue(response.getIsErr());
    }
    // requirement 3.2
    @Test
    void AddStoreSameName() {
        tradingSystem.AddStore(userID,connID,"Store3");
        Response response= tradingSystem.AddStore(userID,connID,"Store3");
        assertTrue(response.getIsErr());
    }

    // requirement 3.2
    @Test
    void AddStoreSuccess() {
        Response response= tradingSystem.AddStore(userID,connID,"Stor143");
        assertFalse(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void RemoveProductSuccess(){
        Response response=tradingSystem.RemoveProduct(userID,storeid,productId2,connID);
        assertFalse(response.getIsErr());
    }
    // requirement 4.1
    @Test
    void RemoveProductInvalidPermmison(){
        String connID1= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.Register(connID1,"reutlevy30","8119");
        int userID1= response.returnUserID();
        connID1= tradingSystem.Login(connID1,"reutlevy30","8119").returnConnID();
        response=tradingSystem.RemoveProduct(userID1,storeid,productId,connID1);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void RemoveProductNotExist(){
        Store store= new Store("store11", userID);
        tradingSystem.AddStore(userID,connID,"store11");
        Product product=new Product(4,"prod4","food",7.0,11);
        Response response=tradingSystem.RemoveProduct(userID,storeid,product.getProductID(),connID);
        assertTrue(response.getIsErr());
    }
    // requirement 4.1
    @Test
    void AddProductInvalidPremmision(){
        Response response=tradingSystem.AddProductToStore(userID,connID,storeid,"prod3","food",11.0,-1);
        assertTrue(response.getIsErr());
    }
    // requirement 4.1
    @Test
    void EditProductWrongQuantity(){
        Response response=tradingSystem.EditProduct(userID,connID,storeid,productId,"prod4","food",12.0,-9);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void ChangeProductQunatityInvalidPremmision(){
        Response response=tradingSystem.ChangeQuantityProduct(userID1,connID1,storeid,productId,10);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void ChangeProductWrongQuantity(){
        Response response=tradingSystem.ChangeQuantityProduct(userID,connID,storeid,productId,-10);
        assertTrue(response.getIsErr());
    }
    // requirement 4.1
    @Test
    void AddProductInvaliddetails(){
        Response response=tradingSystem.AddProductToStore(userID,connID,storeid,"prod3","food",-1,11);
        assertTrue(response.getIsErr());
    }

    // requirement 4.3
    @Test
    void NewOwnerSuccess() {
        Response r1 = tradingSystem.AddNewOwner(NofetID, NconnID, NofetStore, ElinorID);
        System.out.println(r1.getMessage());
        assertFalse(r1.getIsErr());
    }

    // requirement 4.3
    @Test
    void NewOwnerNotConnected() {
        Response r = tradingSystem.AddNewOwner(NofetID, "--", NofetStore, ElinorID);
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.3
    @Test
    void NewOwnerNotSubscriber() {
        Response r1 = tradingSystem.AddNewOwner(-1, NconnID, NofetStore, ElinorID);
        System.out.println(r1.getMessage());
        assertTrue(r1.getIsErr());
        Response r2 = tradingSystem.AddNewOwner(NofetID, NconnID, NofetStore, 20);
        System.out.println(r2.getMessage());
        assertTrue(r2.getIsErr());
    }

    // requirement 4.3
    @Test
    void AddNewOwnerIsNotTheOwner() {
        String gust2 = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust2, "roee", "123");
        Response res2= tradingSystem.Login(gust2, "roee", "123");
        Response res3 =tradingSystem.AddNewOwner(res2.returnUserID(), res2.returnConnID(), NofetStore, ElinorID);
        System.out.println(res3.getMessage());
        assertTrue(res3.getIsErr());
    }

    // requirement 4.3
    @Test
    void NewOwnerDouble() {
        String gust = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust, "roee", "123");
        Response res= tradingSystem.Login(gust, "roee", "123");
        tradingSystem.AddNewOwner(NofetID, NconnID, NofetStore, res.returnUserID());
        Response r = tradingSystem.AddNewOwner(NofetID, NconnID, NofetStore, res.returnUserID());
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.3
    @Test
    void NewOwnerAlreadyManager() {
        String gust = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust, "Hadas", "123");
        Response res= tradingSystem.Login(gust, "Hadas", "123");
        tradingSystem.AddNewManager(NofetID, NconnID, NofetStore, res.returnUserID());
        Response r = tradingSystem.AddNewOwner(NofetID, NconnID, NofetStore, res.returnUserID());
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.5
    @Test
    void NewManagerSuccess() {
        String gust = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust, "r", "123");
        Response res = tradingSystem.Login(gust, "r", "123");
        Response r1 = tradingSystem.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        System.out.println(r1.getMessage());
        assertFalse(r1.getIsErr());
    }

    // requirement 4.5
    @Test
    void NewManagerNotConnected() {
        Response r = tradingSystem.AddNewManager(NofetID, "--", NofetStore, ElinorID);
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.5
    @Test
    void NewManagerNotSubscriber() {
        Response r1 = tradingSystem.AddNewManager(-1, NconnID, NofetStore, ElinorID);
        System.out.println(r1.getMessage());
        assertTrue(r1.getIsErr());
        Response r2 = tradingSystem.AddNewManager(NofetID, NconnID, NofetStore, 20);
        System.out.println(r2.getMessage());
        assertTrue(r2.getIsErr());
    }

    // requirement 4.5
    @Test
    void AddNewManagerIsNotTheOwner() {
        String gust2 = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust2, "roee", "123");
        Response res2 = tradingSystem.Login(gust2, "roee", "123");
        Response res3 = tradingSystem.AddNewManager(res2.returnUserID(), res2.returnConnID(), NofetStore, ElinorID);
        System.out.println(res3.getMessage());
        assertTrue(res3.getIsErr());
    }

    // requirement 4.5
    @Test
    void NewManagerDouble() {
        String gust = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust, "Amir", "123");
        Response res= tradingSystem.Login(gust, "Amir", "123");
        tradingSystem.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        Response r = tradingSystem.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.5
    @Test
    void NewManagerAlreadyOwner() {
        String gust = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust, "Amit", "123");
        Response res= tradingSystem.Login(gust, "Amit", "123");
        tradingSystem.AddNewOwner(ElinorID, EconnID, ElinorStore, res.returnUserID());
        Response r = tradingSystem.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.7
    @Test
    void RemoveManagerSuccess() {
        String gust = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust, "Deme", "123");
        Response res= tradingSystem.Login(gust, "Deme", "123");
        tradingSystem.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        Response r1 = tradingSystem.RemoveManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        System.out.println(r1.getMessage());
        assertFalse(r1.getIsErr());
    }

    // requirement 4.7
    @Test
    void RemoveManagerNotConnected() {
        Response r = tradingSystem.RemoveManager(NofetID, "--", NofetStore, ElinorID);
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.7
    @Test
    void RemoveManagerNotSubscriber() {
        Response r1 = tradingSystem.RemoveManager(-1, NconnID, NofetStore, ElinorID);
        System.out.println(r1.getMessage());
        assertTrue(r1.getIsErr());
        Response r2 = tradingSystem.RemoveManager(NofetID, NconnID, NofetStore, 20);
        System.out.println(r2.getMessage());
        assertTrue(r2.getIsErr());
    }

    // requirement 4.7
    @Test
    void RemoveManagerIsNotTheOwner() {
        String gust = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust, "Deme2", "123");
        Response res= tradingSystem.Login(gust, "Deme2", "123");
        Response res3 =tradingSystem.RemoveManager(res.returnUserID(), res.returnConnID(), ElinorStore, ElinorID);
        System.out.println(res3.getMessage());
        assertTrue(res3.getIsErr());
    }

    // requirement 4.7
    @Test
    void RemoveManagerIsNotManager() {
        String gust = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust, "A", "123");
        Response res= tradingSystem.Login(gust, "A", "123");
        Response r = tradingSystem.RemoveManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.7
    @Test
    void RemoveManagerIsNotTheAppointment() {
        String gust = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust, "Deme3", "123");
        Response res = tradingSystem.Login(gust, "Deme3", "123");
        tradingSystem.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        tradingSystem.AddNewOwner(ElinorID, EconnID, ElinorStore, NofetID);
        Response res3 = tradingSystem.RemoveManager(NofetID, NconnID, ElinorStore, res.returnUserID());
        System.out.println(res3.getMessage());
        assertTrue(res3.getIsErr());
    }


    // requirement 4.6
    @Test
    void EditManagerPermissionsSuccess() {
        String gust = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust, "m", "123");
        Response res = tradingSystem.Login(gust, "m", "123");
        tradingSystem.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        LinkedList<User.Permission> p=new LinkedList<>();
        p.add(User.Permission.AddProduct);
        tradingSystem.EditManagerPermissions(ElinorID,EconnID,ElinorStore,res.returnUserID(),p);
        boolean r1 = tradingSystem.hasPermission(res.returnUserID(),ElinorStore, User.Permission.AddProduct);
        boolean r2 = tradingSystem.hasPermission(res.returnUserID(),ElinorStore, User.Permission.GetInfoOfficials);
        assertTrue(r1);
        assertFalse(r2);
    }

    // requirement 4.6
    @Test
    void EditManagerPermissionsNotConnected() {
        Response r = tradingSystem.EditManagerPermissions(NofetID, "--", NofetStore, ElinorID,null);
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.6
    @Test
    void EditManagerPermissionsIsNotSubscriber() {
        Response r1 = tradingSystem.EditManagerPermissions(-1, NconnID, NofetStore, ElinorID,null);
        System.out.println(r1.getMessage());
        assertTrue(r1.getIsErr());
        Response r2 = tradingSystem.EditManagerPermissions(NofetID, NconnID, NofetStore, 20,null);
        System.out.println(r2.getMessage());
        assertTrue(r2.getIsErr());
    }

    // requirement 4.6
    @Test
    void EditManagerPermissionsIsNotTheOwner() {
        String gust2 = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust2, "l", "123");
        Response res2 = tradingSystem.Login(gust2, "l", "123");
        LinkedList<User.Permission> p=new LinkedList<>();
        p.add(User.Permission.AddProduct);
        Response res3 = tradingSystem.EditManagerPermissions(res2.returnUserID(), res2.returnConnID(), NofetStore, ElinorID,p);
        System.out.println(res3.getMessage());
        assertTrue(res3.getIsErr());
    }

    // requirement 4.6
    @Test
    void EditManagerPermissionsIsNotTheAppointment() {
        String gust = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust, "D", "123");
        Response res = tradingSystem.Login(gust, "D", "123");
        LinkedList<User.Permission> p=new LinkedList<>();
        p.add(User.Permission.AddProduct);
        tradingSystem.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        Response res3 = tradingSystem.EditManagerPermissions(NofetID, NconnID, ElinorStore, res.returnUserID(),p);
        System.out.println(res3.getMessage());
        assertTrue(res3.getIsErr());
    }


    @Test
    void editManagerPermissions() {
    }

    //4.9
    @Test
    void ShowStoreWorkersSuccess(){
        tradingSystem.AddNewManager(ElinorID, EconnID, ElinorStore, userID);
        tradingSystem.AddNewOwner(ElinorID, EconnID, ElinorStore, NofetID);
        System.out.println(tradingSystem.ShowStoreWorkers(ElinorID,EconnID,ElinorStore));
        assertFalse(tradingSystem.ShowStoreWorkers(ElinorID,EconnID,ElinorStore).getIsErr());
    }

    @Test
    void ShowStoreWorkersNotPerminssion(){
        assertTrue(tradingSystem.ShowStoreWorkers(userID,EconnID,ElinorStore).getIsErr());
    }

    @Test
    void ShowStoreWorkersStoreNotexist(){
        assertTrue(tradingSystem.ShowStoreWorkers(userID,EconnID,-1).getIsErr());
    }

    @Test
    void ShowStoreWorkersEmpty(){
        Object res=tradingSystem.ShowStoreWorkers(ElinorID,EconnID,ElinorStore).getReturnObject().get("workers");
        assertNull(res);
    }

    @Test
    void removeManagerByOwnerSuccess() {
        Response response = tradingSystem.RemoveOwnerByOwner(userID,connID,ElinorID,storeid);
        boolean exist = tradingSystem.stores.get(storeid).getOwnersIDs().containsKey(ElinorID);
        assertTrue(!exist && !response.getIsErr());
    }

    @Test
    void removeManagerNotByOwner() {
        tradingSystem.AddNewOwner(userID, connID, storeid, ElinorID);
        Response response = tradingSystem.RemoveOwnerByOwner(userID1, connID1, ElinorID, storeid);
        boolean exist = tradingSystem.stores.get(storeid).getOwnersIDs().containsKey(ElinorID);
        assertTrue(exist && response.getIsErr());
    }

    @Test
    void removeOwnerFaliled() {
        Response response = tradingSystem.RemoveOwnerByOwner(userID, connID, userID1, storeid);
        assertTrue(response.getIsErr());
    }
}