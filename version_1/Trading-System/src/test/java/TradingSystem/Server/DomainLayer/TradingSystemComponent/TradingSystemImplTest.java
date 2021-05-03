package TradingSystem.Server.DomainLayer.TradingSystemComponent;


import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Expression;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.Sale;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.UserComponent.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertTrue;


import static org.junit.jupiter.api.Assertions.*;

class TradingSystemImplTest {

    private static TradingSystemImpl tradingSystemImpl = TradingSystemImpl.getInstance();

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
        connID= tradingSystemImpl.ConnectSystem().returnConnID();
        Response response= tradingSystemImpl.Register(connID,"reutlevy","8119");

        userID= response.returnUserID();
        connID= tradingSystemImpl.Login(connID,"reutlevy","8119").returnConnID();


        Response response1= tradingSystemImpl.AddStore(userID,connID,"store8");
        for(Store store1: tradingSystemImpl.stores.values()){
            if(store1.getName().equals("store8")){
                storeid=store1.getId();
            }
        }
        tradingSystemImpl.AddProductToStore(userID,connID,storeid,"test","test1",19.0,9);
        productId= tradingSystemImpl.stores.get(storeid).getProductID("test");
        tradingSystemImpl.AddProductToStore(userID,connID,storeid,"test2","test",19.0,9);
        productId2= tradingSystemImpl.stores.get(storeid).getProductID("test2");

        connID1= tradingSystemImpl.ConnectSystem().returnConnID();
        response1= tradingSystemImpl.Register(connID1,"reutlevy8","8119");
        userID1= response1.returnUserID();
        connID1= tradingSystemImpl.Login(connID1,"reutlevy8","8119").returnConnID();


        NconnID = tradingSystemImpl.ConnectSystem().returnConnID();
        Response r1= tradingSystemImpl.Register(NconnID, "nofet", "123");
        NofetID= r1.returnUserID();
        NconnID= tradingSystemImpl.Login(NconnID, "nofet", "123").returnConnID();

        EconnID = tradingSystemImpl.ConnectSystem().returnConnID();
        Response r2= tradingSystemImpl.Register(EconnID, "elinor", "123");
        ElinorID= r2.returnUserID();
        EconnID= tradingSystemImpl.Login(EconnID, "elinor", "123").returnConnID();


        tradingSystemImpl.AddStore(NofetID,NconnID,"NofetStore");
        tradingSystemImpl.AddStore(ElinorID,EconnID,"ElinorStore");
        for(Store store1: tradingSystemImpl.stores.values()){
            if(store1.getName().equals("NofetStore")){
                NofetStore=store1.getId();
            }
            if(store1.getName().equals("ElinorStore")){
                ElinorStore=store1.getId();
            }
        }
        Response response2= tradingSystemImpl.AddNewOwner(userID,connID,storeid,ElinorID);
    }

    @Test
    void AddProductSuccess(){
        Response response= tradingSystemImpl.AddProductToStore(userID,connID,storeid,"prod8","food",11.0,9);
        assertFalse(response.getIsErr());
    }

    @Test
    void ChangeProductQuantitySuccess(){
        Response response= tradingSystemImpl.ChangeQuantityProduct(userID,connID,storeid,productId,10);
        assertFalse(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void EditProductSuccess(){
        Response response= tradingSystemImpl.EditProduct(userID,connID,storeid,productId,"prod3","food",12.0,9);
        assertFalse(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void EditProductInvalidPremmision(){
        String connID1= tradingSystemImpl.ConnectSystem().returnConnID();
        Response response= tradingSystemImpl.Register(connID1,"reutlevy30","8119");
        int userID1= response.returnUserID();
        connID1= tradingSystemImpl.Login(connID1,"reutlevy30","8119").returnConnID();
        response= tradingSystemImpl.EditProduct(userID1,connID1,storeid,productId,"prod4","food",12.0,9);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void AddProductInvalidAmount(){
        Store store= new Store("store11", userID);
        tradingSystemImpl.AddStore(userID,connID,"store11");
        int storeid=0;
        for(Store store1: tradingSystemImpl.stores.values()){
            if(store1.getName().equals("store11")){
                storeid=store1.getId();
            }
        }
        Response response= tradingSystemImpl.AddProductToStore(userID,connID,storeid,"prod3","food",11.0,-1);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void EditProductWrongPrice(){
        Response response= tradingSystemImpl.EditProduct(userID,connID,storeid,productId,"prod4","food",-12.0,9);
        assertTrue(response.getIsErr());
    }

    // requirement 2.1
    @Test
    void connectSystem() {
        Response response= tradingSystemImpl.ConnectSystem();
        assertTrue(response.returnConnID()!="" && response.getIsErr()==false);
    }

    // requirement 2.2
    @Test
    void exitGood() {
        String connId= tradingSystemImpl.ConnectSystem().returnConnID();
        Response response= tradingSystemImpl.Exit(connId);
        assertFalse(response.getIsErr());
    }

    // requirement 2.2
    @Test
    void exiBad() {
        String connId= tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Exit(connId);
        Response response= tradingSystemImpl.Exit(connId);
        assertTrue(response.getIsErr());
    }

    // requirement 2.3
    @Test
    void registerGood() {
        String connID= tradingSystemImpl.ConnectSystem().returnConnID();
        Response response= tradingSystemImpl.Register(connID,"reutlevy11","8111996");
        assertFalse(response.getIsErr());
    }

    // requirement 2.3
    @Test
    void registerExistUser() {
        String connID= tradingSystemImpl.ConnectSystem().returnConnID();
        Response response= tradingSystemImpl.Register(connID,"reutlevy30","8111996");
        response= tradingSystemImpl.Register(connID,"reutlevy30","reut");
        assertTrue(response.getIsErr());
    }
    //TODO- implement this
    void registerInvalidPassword() {

    }

    // requirement 2.4
    @Test
    void loginSucess() {
        String connID= tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(connID,"reutlevy300","811199");
        Response response= tradingSystemImpl.Login(connID,"reutlevy300","811199");
        assertFalse(response.getIsErr() && response.returnUserID()<0);
    }

    // requirement 2.4
    @Test
    void loginWrongUserName() {
        String connID= tradingSystemImpl.ConnectSystem().returnConnID();
        Response response= tradingSystemImpl.Login(connID,"reutlevy3","811199");
        assertTrue(response.getIsErr());
    }

    // requirement 2.4
    @Test
    void loginWrongPassword() {
        String connID= tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(connID,"reutlevy30","811199");
        Response response= tradingSystemImpl.Login(connID,"reutlevy30","8111996");
        assertTrue(response.getIsErr());
    }

    // requirement 2.5
    @Test
    void showAllStoresGood() {
        Response res = tradingSystemImpl.ShowAllStores();
        assertEquals(res.getIsErr(),false);
    }

    // requirement 3.2
    @Test
    void AddStoreNotSubscriber() { ;
        Response response= tradingSystemImpl.AddStore(11,connID,"Store3");
        assertTrue(response.getIsErr());
    }
    // requirement 3.2
    @Test
    void AddStoreSameName() {
        tradingSystemImpl.AddStore(userID,connID,"Store3");
        Response response= tradingSystemImpl.AddStore(userID,connID,"Store3");
        assertTrue(response.getIsErr());
    }

    // requirement 3.2
    @Test
    void AddStoreSuccess() {
        Response response= tradingSystemImpl.AddStore(userID,connID,"Stor143");
        assertFalse(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void RemoveProductSuccess(){
        Response response= tradingSystemImpl.RemoveProduct(userID,storeid,productId2,connID);
        assertFalse(response.getIsErr());
    }
    // requirement 4.1
    @Test
    void RemoveProductInvalidPermmison(){
        String connID1= tradingSystemImpl.ConnectSystem().returnConnID();
        Response response= tradingSystemImpl.Register(connID1,"reutlevy30","8119");
        int userID1= response.returnUserID();
        connID1= tradingSystemImpl.Login(connID1,"reutlevy30","8119").returnConnID();
        response= tradingSystemImpl.RemoveProduct(userID1,storeid,productId,connID1);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void RemoveProductNotExist(){
        Store store= new Store("store11", userID);
        tradingSystemImpl.AddStore(userID,connID,"store11");
        Product product=new Product(4,"prod4","food",7.0,11);
        Response response= tradingSystemImpl.RemoveProduct(userID,storeid,product.getProductID(),connID);
        assertTrue(response.getIsErr());
    }
    // requirement 4.1
    @Test
    void AddProductInvalidPremmision(){
        Response response= tradingSystemImpl.AddProductToStore(userID,connID,storeid,"prod3","food",11.0,-1);
        assertTrue(response.getIsErr());
    }
    // requirement 4.1
    @Test
    void EditProductWrongQuantity(){
        Response response= tradingSystemImpl.EditProduct(userID,connID,storeid,productId,"prod4","food",12.0,-9);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void ChangeProductQunatityInvalidPremmision(){
        Response response= tradingSystemImpl.ChangeQuantityProduct(userID1,connID1,storeid,productId,10);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void ChangeProductWrongQuantity(){
        Response response= tradingSystemImpl.ChangeQuantityProduct(userID,connID,storeid,productId,-10);
        assertTrue(response.getIsErr());
    }
    // requirement 4.1
    @Test
    void AddProductInvaliddetails(){
        Response response= tradingSystemImpl.AddProductToStore(userID,connID,storeid,"prod3","food",-1,11);
        assertTrue(response.getIsErr());
    }

    // requirement 4.3
    @Test
    void NewOwnerSuccess() {
        Response r1 = tradingSystemImpl.AddNewOwner(NofetID, NconnID, NofetStore, ElinorID);
        System.out.println(r1.getMessage());
        assertFalse(r1.getIsErr());
    }

    // requirement 4.3
    @Test
    void NewOwnerNotConnected() {
        Response r = tradingSystemImpl.AddNewOwner(NofetID, "--", NofetStore, ElinorID);
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.3
    @Test
    void NewOwnerNotSubscriber() {
        Response r1 = tradingSystemImpl.AddNewOwner(-1, NconnID, NofetStore, ElinorID);
        System.out.println(r1.getMessage());
        assertTrue(r1.getIsErr());
        Response r2 = tradingSystemImpl.AddNewOwner(NofetID, NconnID, NofetStore, 20);
        System.out.println(r2.getMessage());
        assertTrue(r2.getIsErr());
    }

    // requirement 4.3
    @Test
    void AddNewOwnerIsNotTheOwner() {
        String gust2 = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust2, "roee", "123");
        Response res2= tradingSystemImpl.Login(gust2, "roee", "123");
        Response res3 = tradingSystemImpl.AddNewOwner(res2.returnUserID(), res2.returnConnID(), NofetStore, ElinorID);
        System.out.println(res3.getMessage());
        assertTrue(res3.getIsErr());
    }

    // requirement 4.3
    @Test
    void NewOwnerDouble() {
        String gust = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust, "roee", "123");
        Response res= tradingSystemImpl.Login(gust, "roee", "123");
        tradingSystemImpl.AddNewOwner(NofetID, NconnID, NofetStore, res.returnUserID());
        Response r = tradingSystemImpl.AddNewOwner(NofetID, NconnID, NofetStore, res.returnUserID());
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.3
    @Test
    void NewOwnerAlreadyManager() {
        String gust = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust, "Hadas", "123");
        Response res= tradingSystemImpl.Login(gust, "Hadas", "123");
        tradingSystemImpl.AddNewManager(NofetID, NconnID, NofetStore, res.returnUserID());
        Response r = tradingSystemImpl.AddNewOwner(NofetID, NconnID, NofetStore, res.returnUserID());
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.5
    @Test
    void NewManagerSuccess() {
        String gust = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust, "r", "123");
        Response res = tradingSystemImpl.Login(gust, "r", "123");
        Response r1 = tradingSystemImpl.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        System.out.println(r1.getMessage());
        assertFalse(r1.getIsErr());
    }

    // requirement 4.5
    @Test
    void NewManagerNotConnected() {
        Response r = tradingSystemImpl.AddNewManager(NofetID, "--", NofetStore, ElinorID);
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.5
    @Test
    void NewManagerNotSubscriber() {
        Response r1 = tradingSystemImpl.AddNewManager(-1, NconnID, NofetStore, ElinorID);
        System.out.println(r1.getMessage());
        assertTrue(r1.getIsErr());
        Response r2 = tradingSystemImpl.AddNewManager(NofetID, NconnID, NofetStore, 20);
        System.out.println(r2.getMessage());
        assertTrue(r2.getIsErr());
    }

    // requirement 4.5
    @Test
    void AddNewManagerIsNotTheOwner() {
        String gust2 = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust2, "roee", "123");
        Response res2 = tradingSystemImpl.Login(gust2, "roee", "123");
        Response res3 = tradingSystemImpl.AddNewManager(res2.returnUserID(), res2.returnConnID(), NofetStore, ElinorID);
        System.out.println(res3.getMessage());
        assertTrue(res3.getIsErr());
    }

    // requirement 4.5
    @Test
    void NewManagerDouble() {
        String gust = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust, "Amir", "123");
        Response res= tradingSystemImpl.Login(gust, "Amir", "123");
        tradingSystemImpl.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        Response r = tradingSystemImpl.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.5
    @Test
    void NewManagerAlreadyOwner() {
        String gust = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust, "Amit", "123");
        Response res= tradingSystemImpl.Login(gust, "Amit", "123");
        tradingSystemImpl.AddNewOwner(ElinorID, EconnID, ElinorStore, res.returnUserID());
        Response r = tradingSystemImpl.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.7
    @Test
    void RemoveManagerSuccess() {
        String gust = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust, "Deme", "123");
        Response res= tradingSystemImpl.Login(gust, "Deme", "123");
        tradingSystemImpl.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        Response r1 = tradingSystemImpl.RemoveManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        System.out.println(r1.getMessage());
        assertFalse(r1.getIsErr());
    }

    // requirement 4.7
    @Test
    void RemoveManagerNotConnected() {
        Response r = tradingSystemImpl.RemoveManager(NofetID, "--", NofetStore, ElinorID);
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.7
    @Test
    void RemoveManagerNotSubscriber() {
        Response r1 = tradingSystemImpl.RemoveManager(-1, NconnID, NofetStore, ElinorID);
        System.out.println(r1.getMessage());
        assertTrue(r1.getIsErr());
        Response r2 = tradingSystemImpl.RemoveManager(NofetID, NconnID, NofetStore, 20);
        System.out.println(r2.getMessage());
        assertTrue(r2.getIsErr());
    }

    // requirement 4.7
    @Test
    void RemoveManagerIsNotTheOwner() {
        String gust = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust, "Deme2", "123");
        Response res= tradingSystemImpl.Login(gust, "Deme2", "123");
        Response res3 = tradingSystemImpl.RemoveManager(res.returnUserID(), res.returnConnID(), ElinorStore, ElinorID);
        System.out.println(res3.getMessage());
        assertTrue(res3.getIsErr());
    }

    // requirement 4.7
    @Test
    void RemoveManagerIsNotManager() {
        String gust = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust, "A", "123");
        Response res= tradingSystemImpl.Login(gust, "A", "123");
        Response r = tradingSystemImpl.RemoveManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.7
    @Test
    void RemoveManagerIsNotTheAppointment() {
        String gust = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust, "Deme3", "123");
        Response res = tradingSystemImpl.Login(gust, "Deme3", "123");
        tradingSystemImpl.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        tradingSystemImpl.AddNewOwner(ElinorID, EconnID, ElinorStore, NofetID);
        Response res3 = tradingSystemImpl.RemoveManager(NofetID, NconnID, ElinorStore, res.returnUserID());
        System.out.println(res3.getMessage());
        assertTrue(res3.getIsErr());
    }


    // requirement 4.6
    @Test
    void EditManagerPermissionsSuccess() {
        String gust = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust, "m", "123");
        Response res = tradingSystemImpl.Login(gust, "m", "123");
        tradingSystemImpl.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        LinkedList<User.Permission> p=new LinkedList<>();
        p.add(User.Permission.AddProduct);
        tradingSystemImpl.EditManagerPermissions(ElinorID,EconnID,ElinorStore,res.returnUserID(),p);
        boolean r1 = tradingSystemImpl.hasPermission(res.returnUserID(),ElinorStore, User.Permission.AddProduct);
        boolean r2 = tradingSystemImpl.hasPermission(res.returnUserID(),ElinorStore, User.Permission.GetInfoOfficials);
        assertTrue(r1);
        assertFalse(r2);
    }

    // requirement 4.6
    @Test
    void EditManagerPermissionsNotConnected() {
        Response r = tradingSystemImpl.EditManagerPermissions(NofetID, "--", NofetStore, ElinorID,null);
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.6
    @Test
    void EditManagerPermissionsIsNotSubscriber() {
        Response r1 = tradingSystemImpl.EditManagerPermissions(-1, NconnID, NofetStore, ElinorID,null);
        System.out.println(r1.getMessage());
        assertTrue(r1.getIsErr());
        Response r2 = tradingSystemImpl.EditManagerPermissions(NofetID, NconnID, NofetStore, 20,null);
        System.out.println(r2.getMessage());
        assertTrue(r2.getIsErr());
    }

    // requirement 4.6
    @Test
    void EditManagerPermissionsIsNotTheOwner() {
        String gust2 = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust2, "l", "123");
        Response res2 = tradingSystemImpl.Login(gust2, "l", "123");
        LinkedList<User.Permission> p=new LinkedList<>();
        p.add(User.Permission.AddProduct);
        Response res3 = tradingSystemImpl.EditManagerPermissions(res2.returnUserID(), res2.returnConnID(), NofetStore, ElinorID,p);
        System.out.println(res3.getMessage());
        assertTrue(res3.getIsErr());
    }

    // requirement 4.6
    @Test
    void EditManagerPermissionsIsNotTheAppointment() {
        String gust = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust, "D", "123");
        Response res = tradingSystemImpl.Login(gust, "D", "123");
        LinkedList<User.Permission> p=new LinkedList<>();
        p.add(User.Permission.AddProduct);
        tradingSystemImpl.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        Response res3 = tradingSystemImpl.EditManagerPermissions(NofetID, NconnID, ElinorStore, res.returnUserID(),p);
        System.out.println(res3.getMessage());
        assertTrue(res3.getIsErr());
    }


    @Test
    void editManagerPermissions() {
    }

    //4.9
    @Test
    void ShowStoreWorkersSuccess(){
        tradingSystemImpl.AddNewManager(ElinorID, EconnID, ElinorStore, userID);
        tradingSystemImpl.AddNewOwner(ElinorID, EconnID, ElinorStore, NofetID);
        System.out.println(tradingSystemImpl.ShowStoreWorkers(ElinorID,EconnID,ElinorStore));
        assertFalse(tradingSystemImpl.ShowStoreWorkers(ElinorID,EconnID,ElinorStore).getIsErr());
    }

    @Test
    void ShowStoreWorkersNotPerminssion(){
        assertTrue(tradingSystemImpl.ShowStoreWorkers(userID,EconnID,ElinorStore).getIsErr());
    }

    @Test
    void ShowStoreWorkersStoreNotexist(){
        assertTrue(tradingSystemImpl.ShowStoreWorkers(userID,EconnID,-1).getIsErr());
    }

    @Test
    void ShowStoreWorkersEmpty(){
        Object res= tradingSystemImpl.ShowStoreWorkers(ElinorID,EconnID,ElinorStore).getReturnObject().get("workers");
        assertNull(res);
    }

    @Test
    void removeManagerByOwnerSuccess() {
        Response response = tradingSystemImpl.RemoveOwnerByOwner(userID,connID,ElinorID,storeid);
        boolean exist = tradingSystemImpl.stores.get(storeid).getOwnersIDs().containsKey(ElinorID);
        assertTrue(!exist && !response.getIsErr());
    }

    @Test
    void removeManagerNotByOwner() {
        tradingSystemImpl.AddNewOwner(userID, connID, storeid, ElinorID);
        Response response = tradingSystemImpl.RemoveOwnerByOwner(userID1, connID1, ElinorID, storeid);
        boolean exist = tradingSystemImpl.stores.get(storeid).getOwnersIDs().containsKey(ElinorID);
        assertTrue(exist && response.getIsErr());
    }

    @Test
    void removeOwnerFaliled() {
        Response response = tradingSystemImpl.RemoveOwnerByOwner(userID, connID, userID1, storeid);
        assertTrue(response.getIsErr());
    }

    @Test
    void tmpTest1() {


        Map<String, Object> storeSale = new HashMap<>();
        Map<String, Object> storeSaleElements = new HashMap<>();
        storeSaleElements.put("storeID", 1);
        storeSaleElements.put("discount", 15.0);

        Map<String, Object> priceForSaleElements = new HashMap<>();
        priceForSaleElements.put("priceForSale", 100.0);

        Map<String, Object> QuantityForGetSaleElements = new HashMap<>();
        QuantityForGetSaleElements.put("productID", 1);
        QuantityForGetSaleElements.put("quantityForSale", 3);

        Map<String, Object> storeSaleExpressionElements = new HashMap<>();
        storeSaleExpressionElements.put("PriceForGetSale", priceForSaleElements);
        storeSaleExpressionElements.put("QuantityForGetSale", QuantityForGetSaleElements);


        Map<String, Object> storeSaleExpression = new HashMap<>();
        storeSaleExpression.put("AndComposite", storeSaleExpressionElements);

        storeSaleElements.put("expression", storeSaleExpression);

        storeSale.put("StoreSale", storeSaleElements);


        Map<String, Object> productSaleElements = new HashMap<>();
        productSaleElements.put("productID", 2);
        productSaleElements.put("discount", 10.0);

        Map<String, Object> priceForSaleElements2 = new HashMap<>();
        priceForSaleElements2.put("priceForSale", 50.0);

        Map<String, Object> productSaleExpression = new HashMap<>();
        productSaleExpression.put("PriceForGetSale", priceForSaleElements2);

        productSaleElements.put("expression", productSaleExpression);


        Map<String, Object> categorySaleElements = new HashMap<>();
        categorySaleElements.put("category", "blabla");
        categorySaleElements.put("discount", 20.0);

        Map<String, Object> NumOfProductsForGetSaleElements = new HashMap<>();
        NumOfProductsForGetSaleElements.put("numOfProductsForSale", 4);

        Map<String, Object> categorySaleExpression = new HashMap<>();
        categorySaleExpression.put("NumOfProductsForGetSale", NumOfProductsForGetSaleElements);

        categorySaleElements.put("expression", categorySaleExpression);

        Map<String, Object> AddElement = new HashMap<>();
        AddElement.put("CategorySale", categorySaleElements);
        AddElement.put("ProductSale", productSaleElements);
        Map<String, Object> Add = new HashMap<>();
        Add.put("AddComposite", AddElement);

        Map<String, Object> MaxElement = new HashMap<>();
        MaxElement.put("AddComposite", AddElement);
        MaxElement.put("StoreSale", storeSaleElements);

        Map<String, Object> Max = new HashMap<>();
        Max.put("MaxComposite", MaxElement);

        Sale s = tradingSystemImpl.createSaleForDiscount(1,  Max);
        assertTrue(true);
    }
    @Test
    void tmpTest2() {

        Map<String, Object> QuantityLimitForProductElement =new HashMap<> ();
        QuantityLimitForProductElement.put("maxQuantity",30);
        QuantityLimitForProductElement.put("productID",2);
        Map<String, Object> QuantityLimitForProduct =new HashMap<> ();
        QuantityLimitForProduct.put("QuantityLimitForProduct",QuantityLimitForProductElement);


        Map<String, Object> QuantityLimitForCategoryElement =new HashMap<> ();
        QuantityLimitForCategoryElement.put("maxQuantity",50);
        QuantityLimitForCategoryElement.put("category","blabla");
        Map<String, Object> QuantityLimitForCategory =new HashMap<> ();
        QuantityLimitForCategory.put("QuantityLimitForCategory",QuantityLimitForCategoryElement);

        Map<String, Object> ConditioningElement =new HashMap<> ();
        ConditioningElement.put("cond",QuantityLimitForCategory);
        ConditioningElement.put("condIf",QuantityLimitForProduct);

        Map<String, Object> QuantityLimitForStoreElement =new HashMap<> ();
        QuantityLimitForStoreElement.put("maxQuantity",70);
        QuantityLimitForStoreElement.put("storeID",2);

        Map<String, Object> AgeLimitForProductElement =new HashMap<> ();
        AgeLimitForProductElement.put("minAge",20);
        AgeLimitForProductElement.put("productID",1);

        Map<String, Object> AndCompositeElement = new HashMap<>();
        AndCompositeElement.put("AgeLimitForProduct",AgeLimitForProductElement);
        AndCompositeElement.put("QuantityLimitForStore",QuantityLimitForStoreElement);
        AndCompositeElement.put("Conditioning",ConditioningElement);

        Map<String, Object> AgeLimitForCategoryElement =new HashMap<> ();
        AgeLimitForCategoryElement.put("minAge",19);
        AgeLimitForCategoryElement.put("category","foo");

        Map<String, Object> AgeLimitForStoreElement =new HashMap<> ();
        AgeLimitForStoreElement.put("minAge",10);
        AgeLimitForStoreElement.put("storeID",1);
        //Map<String, Object> AgeLimitForStore =new HashMap<> ();

        Map<String, Object> OrCompositeElement = new HashMap<>();
        OrCompositeElement.put("AgeLimitForCategory",AgeLimitForCategoryElement);
        OrCompositeElement.put("AndComposite",AndCompositeElement);
        OrCompositeElement.put("AgeLimitForStore",AgeLimitForStoreElement);

        Map<String, Object> OrComposite = new HashMap<>();
        OrComposite.put("OrComposite",OrCompositeElement);
;

        Expression exp = tradingSystemImpl.CreateExpForBuy(1,  OrComposite);
        assertTrue(true);
    }


}