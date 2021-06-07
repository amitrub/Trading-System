package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingBag;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.DiscountPolicy;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.OrComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimitForProduct;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimitForStore;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.Task.*;
import TradingSystem.Server.DomainLayer.UserComponent.PermissionEnum;

import java.util.*;
import java.util.concurrent.*;

import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

//import static org.junit.jupiter.api.Assertions.*;

//@RunWith(SpringRunner.class)
//@SpringBootTest
class TradingSystemImplTest {

    private static TradingSystemImpl tradingSystemImpl = TradingSystemImpl.getInstance();

//    @Autowired
//    private TradingSystemImplRubin tradingSystemImpl;

    static int userID;
    static int userID1;
    static int storeid;
    static int productId;
    static int productId2;
    static String connID;
    static String connID1;

    static String EconnID;
    static String NconnID;
    static int ElinorID;
    static int NofetID;
    static int NofetStore;
    static int ElinorStore;
    static Store Nstore;
    static Store Estore;

    @BeforeAll
    public static void setup() {
        connID= tradingSystemImpl.ConnectSystem().returnConnID();
        Response response= tradingSystemImpl.Register(connID,"reutlevy","8119");

        userID= response.returnUserID();
        connID= tradingSystemImpl.Login(connID,"reutlevy","8119").returnConnID();


        tradingSystemImpl.AddStore(userID,connID,"store8");
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
        Response response1= tradingSystemImpl.Register(connID1,"reutlevy8","8119");
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
        tradingSystemImpl.AddNewOwner(userID,connID,storeid,ElinorID);

        Estore = tradingSystemImpl.stores.get(ElinorStore);
        Nstore = tradingSystemImpl.stores.get(NofetStore);
        Product p1=new Product(NofetStore,"NofetStore",1,"1","1",2.0);
        Product p2=new Product(NofetStore,"NofetStore",2,"2","2",4.0);
        Product p3=new Product(NofetStore,"NofetStore",3,"3","3",13.0);
        Product p4=new Product(NofetStore,"NofetStore",4,"4","4",21.0);

        LinkedList<Product> PSH1=new LinkedList<>();
        PSH1.add(p1);
        PSH1.add(p2);
        PSH1.add(p3);

        LinkedList<Product> PSH2=new LinkedList<>();
        PSH1.add(p2);
        PSH1.add(p3);
        PSH1.add(p4);

        ShoppingBag SB1=new ShoppingBag(1,NofetStore);
        ShoppingBag SB2=new ShoppingBag(2,NofetStore);
        SB1.setFinalPrice(17.0);
        SB2.setFinalPrice(33.0);

        ShoppingHistory SH1=new ShoppingHistory(SB1,PSH1);
        ShoppingHistory SH2=new ShoppingHistory(SB2,PSH2);

        Nstore.addHistory(SH1);
        Nstore.addHistory(SH2);

    }

    public void tearDown(){
        tradingSystemImpl.ClearSystem();
    }

    //region requirement 2

    // requirement 2.1
    @Test
    public void connectSystem() {
        Response response= tradingSystemImpl.ConnectSystem();
        Assertions.assertTrue(response.returnConnID()!="" && response.getIsErr()==false);
    }

    // requirement 2.2
    @Test
    public void exitGood() {
        String connId= tradingSystemImpl.ConnectSystem().returnConnID();
        Response response= tradingSystemImpl.Exit(connId);
        Assertions.assertFalse(response.getIsErr());
    }

    // requirement 2.2
    @Test
    public void exitBad() {
        String connId= tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Exit(connId);
        Response response= tradingSystemImpl.Exit(connId);
        Assertions.assertTrue(response.getIsErr());
    }

    // requirement 2.3
    @Test
    public void registerGood() {
        String connID= tradingSystemImpl.ConnectSystem().returnConnID();
        Response response= tradingSystemImpl.Register(connID,"reutlevy11","8111996");
        Assertions.assertFalse(response.getIsErr());
    }

    // requirement 2.3
    @Test
    void registerExistUser() {
        String connID= tradingSystemImpl.ConnectSystem().returnConnID();
        Response response= tradingSystemImpl.Register(connID,"reutlevy30","8111996");
        response= tradingSystemImpl.Register(connID,"reutlevy30","reut");
        Assertions.assertTrue(response.getIsErr());
    }

    // requirement 2.3
    //TODO
    @Test
    void registerParallelSadSameName(){
        ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(2);

        List<RegisterTaskUnitTests> taskList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            RegisterTaskUnitTests task = new RegisterTaskUnitTests("SameName");
            taskList.add(task);
        }

        //Execute all tasks and get reference to Future objects
        List<Future<ResultUnitTests>> resultList = null;

        try {
            resultList = executor.invokeAll(taskList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();

        System.out.println("\n========Printing the results======");

        assert resultList != null;
        for (int i = 0; i < resultList.size(); i++) {
            Future<ResultUnitTests> future = resultList.get(i);
            try {
                ResultUnitTests result = future.get();
                System.out.println(result.getName() + ": " + result.getTimestamp());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    // requirement 2.4
    @Test
    void loginSuccess() {
        String connID= tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(connID,"reutlevy300","811199");
        Response response= tradingSystemImpl.Login(connID,"reutlevy300","811199");
        Assertions.assertFalse(response.getIsErr() && response.returnUserID()<0);
    }

    // requirement 2.4
    @Test
    void loginWrongUserName() {
        String connID= tradingSystemImpl.ConnectSystem().returnConnID();
        Response response= tradingSystemImpl.Login(connID,"reutlevy3","811199");
        Assertions.assertTrue(response.getIsErr());
    }

    // requirement 2.4
    @Test
    void loginWrongPassword() {
        String connID= tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(connID,"reutlevy30","811199");
        Response response= tradingSystemImpl.Login(connID,"reutlevy30","8111996");
        Assertions.assertTrue(response.getIsErr());
    }

    // requirement 2.5
    @Test
    void showAllStoresGood() {
        Response res = tradingSystemImpl.ShowAllStores();
        Assertions.assertEquals(res.getIsErr(),false);
    }

    //endregion

    //region requirement 3

    // requirement 3.1
    @Test
    void LogoutHappy() {
        String connID= tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(connID,"Rubin","123");
        Response res = tradingSystemImpl.Login(connID,"Rubin","123");
        connID = res.returnConnID();
        Response response = tradingSystemImpl.Logout(connID);
        Assertions.assertFalse(response.getIsErr());
    }

    // requirement 3.1
    @Test
    void LogoutSad() {
        String connID= tradingSystemImpl.ConnectSystem().returnConnID();
        Response res = tradingSystemImpl.Register(connID,"Rubin","123");
        connID = res.returnConnID();
        Response response = tradingSystemImpl.Logout(connID);
        Assertions.assertTrue(response.getIsErr());
    }

    // requirement 3.2
    @Test
    void AddStoreNotSubscriber() { ;
        Response response= tradingSystemImpl.AddStore(11,connID,"Store3");
        Assertions.assertTrue(response.getIsErr());
    }

    // requirement 3.2
    @Test
    void AddStoreSameName() {
        tradingSystemImpl.AddStore(userID,connID,"Store3");
        Response response= tradingSystemImpl.AddStore(userID,connID,"Store3");
        Assertions.assertTrue(response.getIsErr());
    }

    // requirement 3.2
    @Test
    void AddStoreSuccess() {
        Response response= tradingSystemImpl.AddStore(userID,connID,"Stor143");
        Assertions.assertFalse(response.getIsErr());
    }

    // requirement 3.3
    @Test
    void WriteCommentSuccess() {
        setUpBeforePurchase();
        Integer productID1 = Nstore.getProductID("computer");
        tradingSystemImpl.AddProductToCart(EconnID, NofetStore, productID1, 1);
        tradingSystemImpl.subscriberPurchase(ElinorID, EconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        Response response = tradingSystemImpl.WriteComment(ElinorID,EconnID, NofetStore, productID1, "Amazing");
        Assertions.assertFalse(response.getIsErr());
        Integer size = tradingSystemImpl.stores.get(NofetStore).getProduct(productID1).getComments().size();
        Assertions.assertEquals(size, 1);
    }

    // requirement 3.3
    @Test
    void WriteCommentWrongStoreID() {
        setUpBeforePurchase();
        Integer productID1 = Nstore.getProductID("computer");
        tradingSystemImpl.AddProductToCart(EconnID, NofetStore, productID1, 1);
        tradingSystemImpl.subscriberPurchase(ElinorID, EconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        Response response = tradingSystemImpl.WriteComment(ElinorID,EconnID, 100, productID1, "Amazing");
        Assertions.assertTrue(response.getIsErr());
        Integer size = tradingSystemImpl.stores.get(NofetStore).getProduct(productID1).getComments().size();
        Assertions.assertEquals(size, 0);
    }
    
    // requirement 3.3
    @Test
    void WriteCommentNotInHistory() {
        setUpBeforePurchase();
        Integer productID1 = Nstore.getProductID("computer");

        Response response = tradingSystemImpl.WriteComment(ElinorID,EconnID, NofetStore, productID1, "Amazing");
        Assertions.assertTrue(response.getIsErr());
        Integer size = tradingSystemImpl.stores.get(NofetStore).getProduct(productID1).getComments().size();
        Assertions.assertEquals(size, 0);

    }

    // requirement 3.3
    @Test
    void WriteCommentExistComment() {
        setUpBeforePurchase();
        Integer productID1 = Nstore.getProductID("computer");
        tradingSystemImpl.AddProductToCart(EconnID, NofetStore, productID1, 1);
        tradingSystemImpl.subscriberPurchase(ElinorID, EconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        tradingSystemImpl.WriteComment(ElinorID,EconnID, NofetStore, productID1, "Amazing");
        Response response = tradingSystemImpl.WriteComment(ElinorID,EconnID, NofetStore, productID1, "WTF");
        Assertions.assertTrue(response.getIsErr());
        Integer size = tradingSystemImpl.stores.get(NofetStore).getProduct(productID1).getComments().size();
        Assertions.assertEquals(size, 1);
    }

    // requirement 3.7
    @Test
    void UserHistorySuccess() {
        setUpBeforePurchase();
        Integer productID1 = Nstore.getProductID("computer");
        Integer productID2 = Nstore.getProductID("Bag");
        tradingSystemImpl.AddProductToCart(EconnID, NofetStore, productID1, 1);
        tradingSystemImpl.subscriberPurchase(ElinorID, EconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        tradingSystemImpl.AddProductToCart(EconnID, NofetStore, productID2, 1);
        tradingSystemImpl.subscriberPurchase(ElinorID, EconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        //DummyUser test = new DummyUser("ala", "123");
        Response res = tradingSystemImpl.ShowSubscriberHistory(ElinorID, EconnID);
        Assertions.assertFalse(res.getIsErr());
        List<DummyShoppingHistory> list = (List<DummyShoppingHistory>) res.getReturnObject().get("history");
        Assertions.assertEquals(list.size(), 2);
    }

    // requirement 3.7
    @Test
    void UserHistoryFailed() {
        Response res = tradingSystemImpl.ShowSubscriberHistory(ElinorID, EconnID);
        Assertions.assertTrue(res.getIsErr());
    }

    //endregion

    //region requirement 4

    // requirement 4.1
    @Test
    void AddProductSuccess(){
        Response response= tradingSystemImpl.AddProductToStore(userID,connID,storeid,"prod8","food",11.0,9);
        Assertions.assertFalse(response.getIsErr());
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
        Assertions.assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void AddProductInvalidPermission(){
        Response response= tradingSystemImpl.AddProductToStore(userID,connID,storeid,"prod3","food",11.0,-1);
        Assertions.assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void AddProductInValidDetails(){
        Response response= tradingSystemImpl.AddProductToStore(userID,connID,storeid,"prod3","food",-1,11);
        Assertions.assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void ChangeProductQuantitySuccess(){
        Response response= tradingSystemImpl.ChangeQuantityProduct(userID,connID,storeid,productId,10);
        Assertions.assertFalse(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void EditProductWrongQuantity(){
        Response response= tradingSystemImpl.EditProduct(userID,connID,storeid,productId,"prod4","food",12.0,-9);
        Assertions.assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void ChangeProductQuantityInvalidPermission(){
        Response response= tradingSystemImpl.ChangeQuantityProduct(userID1,connID1,storeid,productId,10);
        Assertions.assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void ChangeProductWrongQuantity(){
        Response response= tradingSystemImpl.ChangeQuantityProduct(userID,connID,storeid,productId,-10);
        Assertions.assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void EditProductSuccess(){
        Response response= tradingSystemImpl.EditProduct(userID,connID,storeid,productId,"prod3","food",12.0,9);
        Assertions.assertFalse(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void EditProductInvalidPermission(){
        String connID1= tradingSystemImpl.ConnectSystem().returnConnID();
        Response response= tradingSystemImpl.Register(connID1,"reutlevy30","8119");
        int userID1= response.returnUserID();
        connID1= tradingSystemImpl.Login(connID1,"reutlevy30","8119").returnConnID();
        response= tradingSystemImpl.EditProduct(userID1,connID1,storeid,productId,"prod4","food",12.0,9);
        Assertions.assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void EditProductWrongPrice(){
        Response response= tradingSystemImpl.EditProduct(userID,connID,storeid,productId,"prod4","food",-12.0,9);
        Assertions.assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void RemoveProductSuccess(){
        Response response= tradingSystemImpl.RemoveProduct(userID,storeid,productId2,connID);
        Assertions.assertFalse(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void RemoveProductInvalidPermission(){
        String connID1= tradingSystemImpl.ConnectSystem().returnConnID();
        Response response= tradingSystemImpl.Register(connID1,"reutlevy30","8119");
        int userID1= response.returnUserID();
        connID1= tradingSystemImpl.Login(connID1,"reutlevy30","8119").returnConnID();
        response= tradingSystemImpl.RemoveProduct(userID1,storeid,productId,connID1);
        Assertions.assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    void RemoveProductNotExist(){
        Store store= new Store("store11", userID);
        tradingSystemImpl.AddStore(userID,connID,"store11");
        Product product=new Product(store.getId(), store.getName(),4,"prod4","food",7.0,11);
        Response response= tradingSystemImpl.RemoveProduct(userID,storeid,product.getProductID(),connID);
        Assertions.assertTrue(response.getIsErr());
    }

    @Test
    void removeProductFromStoreWhileOtherClientBuyingItTest() {
        List<boolean[]> isErrsTotal = new ArrayList<>();
        for (int test_i = 0; test_i < 10; test_i++) {
            //Prepare
            tradingSystemImpl.AddProductToStore(ElinorID, EconnID, ElinorStore, "Sneakers", "Shoes", 150.0, 25);
            Integer newProduct = tradingSystemImpl.stores.get(ElinorStore).getProductID("Sneakers");
            //Create two clients with task to buy this product
            ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(2);

            //Prepare tasks for clients
            List<Callable<ResultUnitTests>> taskList = new ArrayList<>();
            Callable<ResultUnitTests> purchaseTask = new PurchaseTaskUnitTests("guestBuyer", ElinorStore, newProduct, 25, "123456789", "4", "2022" ,"123" ,"123456789" ,"Rager 101","Beer Sheva","Israel","8458527");
            taskList.add(purchaseTask);
            Callable<ResultUnitTests> removeTask = new RemoveProductTaskUnitTests("Client-StoreOwner",ElinorStore, newProduct);
            taskList.add(removeTask);

            //Execute all tasks and get reference to Future objects
            List<Future<ResultUnitTests>> resultList = null;

            try {
                resultList = executor.invokeAll(taskList);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            executor.shutdown();

            System.out.println("\n========Printing the results======");
            boolean[] isErrs = new boolean[2];
            for (int i = 0; i < resultList.size(); i++) {
                Future<ResultUnitTests> future = resultList.get(i);
                try {
                    ResultUnitTests result = future.get();
//                System.out.println(result.getName() + ": " + result.getTimestamp());
                    Response response = result.getResponse();
                    System.out.println("Assert correctnes for " + result.getName() + ": response -> " + response + " ::" + result.getTimestamp());
                    isErrs[i] = response.getIsErr();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            //Check that one of the client failed and the other succeed.
//            assertTrue((isErrs[0] && !isErrs[1]) || (isErrs[1] && !isErrs[0]));
            isErrsTotal.add(isErrs);
            tearDown();
            setup();
        }
        boolean ans = false;
        for(boolean[] errArr : isErrsTotal) {
            if ((errArr[0] && !errArr[1]) || (errArr[1] && !errArr[0])) {
                ans = true;
                break;
            }
        }
        Assertions.assertTrue(ans);
        System.out.println("========Printing the results - TOTAL PARALLEL ======");
        for(int i=0; i<isErrsTotal.size(); i++) {
            System.out.printf("%d: purchase: %s remove: %s\n", i, isErrsTotal.get(i)[0], isErrsTotal.get(i)[1]);
        }
    }

    // requirement 4.2
    @Test
    void HappyInfoPolicies() {
        QuantityLimitForProduct exp = new QuantityLimitForProduct(1, productId);
        tradingSystemImpl.addBuyingPolicy(userID, connID, storeid, exp);
        Response res = tradingSystemImpl.GetPoliciesInfo(userID, storeid, connID);
        Assertions.assertFalse(res.getIsErr());
    }

    // requirement 4.2
    @Test
    void SadInfoPolicies() {
        Response res = tradingSystemImpl.GetPoliciesInfo(userID, storeid, connID);
        Assertions.assertFalse(res.getIsErr());
        BuyingPolicy BP = (BuyingPolicy) res.getReturnObject().get("BuyingPolicy");
        DiscountPolicy DP = (DiscountPolicy) res.getReturnObject().get("DiscountPolicy");
        Assertions.assertEquals(BP.getExp(), null);
        Assertions.assertEquals(DP.getSale(), null);
    }

    // requirement 4.3
    @Test
    void NewOwnerSuccess() {
        String gust2 = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust2, "roee", "123");
        Response res2= tradingSystemImpl.Login(gust2, "roee", "123");
        Response r1 = tradingSystemImpl.AddNewOwner(NofetID, NconnID, NofetStore, res2.returnUserID());
        System.out.println(r1.getMessage());
        Assertions.assertFalse(r1.getIsErr());
    }

    // requirement 4.3
    @Test
    void NewOwnerNotConnected() {
        Response r = tradingSystemImpl.AddNewOwner(NofetID, "--", NofetStore, ElinorID);
        System.out.println(r.getMessage());
        Assertions.assertTrue(r.getIsErr());
    }

    // requirement 4.3
    @Test
    void NewOwnerNotSubscriber() {
        Response r1 = tradingSystemImpl.AddNewOwner(-1, NconnID, NofetStore, ElinorID);
        System.out.println(r1.getMessage());
        Assertions.assertTrue(r1.getIsErr());
        Response r2 = tradingSystemImpl.AddNewOwner(NofetID, NconnID, NofetStore, 20);
        System.out.println(r2.getMessage());
        Assertions.assertTrue(r2.getIsErr());
    }

    // requirement 4.3
    @Test
    void AddNewOwnerIsNotTheOwner() {
        String gust2 = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust2, "roee", "123");
        Response res2= tradingSystemImpl.Login(gust2, "roee", "123");
        Response res3 = tradingSystemImpl.AddNewOwner(res2.returnUserID(), res2.returnConnID(), NofetStore, ElinorID);
        System.out.println(res3.getMessage());
        Assertions.assertTrue(res3.getIsErr());
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
        Assertions.assertTrue(r.getIsErr());
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
        Assertions.assertTrue(r.getIsErr());
    }

    // requirement 4.4
    @Test
    void removeOwnerSuccess() {
        tradingSystemImpl.AddNewOwner(NofetID, NconnID, NofetStore, ElinorID);
        tradingSystemImpl.AddNewOwner(ElinorID, EconnID, NofetStore, userID);
        Response response = tradingSystemImpl.RemoveOwnerByOwner(NofetID, NconnID, ElinorID, NofetStore);
        Assertions.assertFalse(response.getIsErr());

        Integer size = tradingSystemImpl.stores.get(NofetStore).OwnersID().size();
        Assertions.assertEquals(size, 1);
    }

    // requirement 4.4
    @Test
    void removeOwnerNotAppointment() {
        tradingSystemImpl.AddNewOwner(NofetID, NconnID, NofetStore, ElinorID);
        tradingSystemImpl.AddNewOwner(NofetID, NconnID, NofetStore, userID);
        Response response = tradingSystemImpl.RemoveOwnerByOwner(userID, connID, ElinorID, NofetStore);
        Assertions.assertTrue(response.getIsErr());

        Integer size = tradingSystemImpl.stores.get(NofetStore).OwnersID().size();
        Assertions.assertEquals(size, 4);
    }

    // requirement 4.4
    @Test
    void removeOwnerNotOwner1() {
        tradingSystemImpl.AddNewOwner(NofetID, NconnID, NofetStore, ElinorID);
        Response response = tradingSystemImpl.RemoveOwnerByOwner(userID, connID, ElinorID, NofetStore);
        Assertions.assertTrue(response.getIsErr());

        Integer size = tradingSystemImpl.stores.get(NofetStore).OwnersID().size();
        Assertions.assertEquals(size, 2);
    }

    // requirement 4.4
    @Test
    void removeOwnerNotOwner2() {
        Response response = tradingSystemImpl.RemoveOwnerByOwner(NofetID, NconnID, userID, NofetStore);
        Assertions.assertTrue(response.getIsErr());

        Integer size = tradingSystemImpl.stores.get(NofetStore).OwnersID().size();
        Assertions.assertEquals(size, 2);
    }

    // requirement 4.5
    @Test
    void NewManagerSuccess() {
        String gust = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust, "r", "123");
        Response res = tradingSystemImpl.Login(gust, "r", "123");
        Response r1 = tradingSystemImpl.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        System.out.println(r1.getMessage());
        Assertions.assertFalse(r1.getIsErr());
    }

    // requirement 4.5
    @Test
    void NewManagerNotConnected() {
        Response r = tradingSystemImpl.AddNewManager(NofetID, "--", NofetStore, ElinorID);
        System.out.println(r.getMessage());
        Assertions.assertTrue(r.getIsErr());
    }

    // requirement 4.5
    @Test
    void NewManagerNotSubscriber() {
        Response r1 = tradingSystemImpl.AddNewManager(-1, NconnID, NofetStore, ElinorID);
        System.out.println(r1.getMessage());
        Assertions.assertTrue(r1.getIsErr());
        Response r2 = tradingSystemImpl.AddNewManager(NofetID, NconnID, NofetStore, 20);
        System.out.println(r2.getMessage());
        Assertions.assertTrue(r2.getIsErr());
    }

    // requirement 4.5
    @Test
    void AddNewManagerIsNotTheOwner() {
        String gust2 = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust2, "roee", "123");
        Response res2 = tradingSystemImpl.Login(gust2, "roee", "123");
        Response res3 = tradingSystemImpl.AddNewManager(res2.returnUserID(), res2.returnConnID(), NofetStore, ElinorID);
        System.out.println(res3.getMessage());
        Assertions.assertTrue(res3.getIsErr());
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
        Assertions.assertTrue(r.getIsErr());
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
        Assertions.assertTrue(r.getIsErr());
    }

    // requirement 4.5
    @Test
    void AddManager_Parallel_TwoOwnerAppointManagerTogether() {
        List<boolean[]> isErrsTotal = new ArrayList<>();
        for(int test_i = 0; test_i < 100; test_i++) {
            //Prepare

            //appoint Nofet to owner
            tradingSystemImpl.AddNewOwner(ElinorID, EconnID, ElinorStore, NofetID);
            Integer size = tradingSystemImpl.stores.get(ElinorStore).OwnersID().size();
            Assertions.assertTrue(size == 2);

            //Create two clients with task to buy this product
            ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(2);

            //Prepare tasks for clients
            List<Callable<ResultUnitTests>> taskList = new ArrayList<>();
            Callable<ResultUnitTests> addManagerTask_1 = new AddManagerTaskUnitTests("Elinor", "123", ElinorStore, userID, ElinorID, EconnID);
            taskList.add(addManagerTask_1);
            Callable<ResultUnitTests> addManagerTask_2 = new AddManagerTaskUnitTests("Nofet", "123", ElinorStore, userID, NofetID, NconnID);
            taskList.add(addManagerTask_2);

            //Execute all tasks and get reference to Future objects
            List<Future<ResultUnitTests>> resultList = null;

            try {
                resultList = executor.invokeAll(taskList);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            executor.shutdown();

            System.out.println("\n========Printing the results======");
            boolean[] isErrs = new boolean[2];
            for (int i = 0; i < resultList.size(); i++) {
                Future<ResultUnitTests> future = resultList.get(i);
                try {
                    ResultUnitTests result = future.get();
//                System.out.println(result.getName() + ": " + result.getTimestamp());
                    Response response = result.getResponse();
                    System.out.println("Assert correctnes for " + result.getName() + ": response -> " + response + " ::" + result.getTimestamp());
                    isErrs[i] = response.getIsErr();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            //Check that one of the client failed and the other succeed.
//            assertTrue((isErrs[0] && !isErrs[1]) || (isErrs[1] && !isErrs[0]));
            isErrsTotal.add(isErrs);
            tearDown();
            setup();
        }

        boolean ans = true;
        for(boolean[] errArr : isErrsTotal) {
            if ((errArr[0] && errArr[1]) || (!errArr[1] && !errArr[0])) {
                ans = false;
                break;
            }
        }
        System.out.println("========Printing the results - TOTAL PARALLEL ======");
        for(int i=0; i<isErrsTotal.size(); i++) {
            System.out.printf("%d: purchase: %s remove: %s\n", i, isErrsTotal.get(i)[0], isErrsTotal.get(i)[1]);
        }
        Assertions.assertTrue(ans);

        Response res = tradingSystemImpl.AddNewManager(ElinorID, EconnID, ElinorStore, userID);
        boolean isManager = tradingSystemImpl.stores.get(ElinorStore).checkManager(userID);
        Assertions.assertFalse(res.getIsErr());
        Assertions.assertTrue(isManager);
    }

    // requirement 4.6
    @Test
    void EditManagerPermissionsSuccess() {
        String gust = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust, "m", "123");
        Response res = tradingSystemImpl.Login(gust, "m", "123");
        tradingSystemImpl.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        LinkedList<PermissionEnum.Permission> p=new LinkedList<>();
        p.add(PermissionEnum.Permission.AddProduct);
        tradingSystemImpl.EditManagerPermissions(ElinorID,EconnID,ElinorStore,res.returnUserID(),p);
        boolean r1 = tradingSystemImpl.hasPermission(res.returnUserID(),ElinorStore, PermissionEnum.Permission.AddProduct);
        boolean r2 = tradingSystemImpl.hasPermission(res.returnUserID(),ElinorStore, PermissionEnum.Permission.GetInfoOfficials);
        Assertions.assertTrue(r1);
        Assertions.assertFalse(r2);
    }

    // requirement 4.6
    @Test
    void EditManagerPermissionsNotConnected() {
        Response r = tradingSystemImpl.EditManagerPermissions(NofetID, "--", NofetStore, ElinorID,null);
        System.out.println(r.getMessage());
        Assertions.assertTrue(r.getIsErr());
    }

    // requirement 4.6
    @Test
    void EditManagerPermissionsIsNotSubscriber() {
        Response r1 = tradingSystemImpl.EditManagerPermissions(-1, NconnID, NofetStore, ElinorID,null);
        System.out.println(r1.getMessage());
        Assertions.assertTrue(r1.getIsErr());
        Response r2 = tradingSystemImpl.EditManagerPermissions(NofetID, NconnID, NofetStore, 20,null);
        System.out.println(r2.getMessage());
        Assertions.assertTrue(r2.getIsErr());
    }

    // requirement 4.6
    @Test
    void EditManagerPermissionsIsNotTheOwner() {
        String gust2 = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust2, "l", "123");
        Response res2 = tradingSystemImpl.Login(gust2, "l", "123");
        LinkedList<PermissionEnum.Permission> p=new LinkedList<>();
        p.add(PermissionEnum.Permission.AddProduct);
        Response res3 = tradingSystemImpl.EditManagerPermissions(res2.returnUserID(), res2.returnConnID(), NofetStore, ElinorID,p);
        System.out.println(res3.getMessage());
        Assertions.assertTrue(res3.getIsErr());
    }

    // requirement 4.6
    @Test
    void EditManagerPermissionsIsNotTheAppointment() {
        String gust = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust, "D", "123");
        Response res = tradingSystemImpl.Login(gust, "D", "123");
        LinkedList<PermissionEnum.Permission> p=new LinkedList<>();
        p.add(PermissionEnum.Permission.AddProduct);
        tradingSystemImpl.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        Response res3 = tradingSystemImpl.EditManagerPermissions(NofetID, NconnID, ElinorStore, res.returnUserID(),p);
        System.out.println(res3.getMessage());
        Assertions.assertTrue(res3.getIsErr());
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
        Assertions.assertFalse(r1.getIsErr());
    }

    // requirement 4.7
    @Test
    void RemoveManagerNotConnected() {
        Response r = tradingSystemImpl.RemoveManager(NofetID, "--", NofetStore, ElinorID);
        System.out.println(r.getMessage());
        Assertions.assertTrue(r.getIsErr());
    }

    // requirement 4.7
    @Test
    void RemoveManagerNotSubscriber() {
        Response r1 = tradingSystemImpl.RemoveManager(-1, NconnID, NofetStore, ElinorID);
        System.out.println(r1.getMessage());
        Assertions.assertTrue(r1.getIsErr());
        Response r2 = tradingSystemImpl.RemoveManager(NofetID, NconnID, NofetStore, 20);
        System.out.println(r2.getMessage());
        Assertions.assertTrue(r2.getIsErr());
    }

    // requirement 4.7
    @Test
    void RemoveManagerIsNotTheOwner() {
        String gust = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust, "Deme2", "123");
        Response res= tradingSystemImpl.Login(gust, "Deme2", "123");
        Response res3 = tradingSystemImpl.RemoveManager(res.returnUserID(), res.returnConnID(), ElinorStore, ElinorID);
        System.out.println(res3.getMessage());
        Assertions.assertTrue(res3.getIsErr());
    }

    // requirement 4.7
    @Test
    void RemoveManagerIsNotManager() {
        String gust = tradingSystemImpl.ConnectSystem().returnConnID();
        tradingSystemImpl.Register(gust, "A", "123");
        Response res= tradingSystemImpl.Login(gust, "A", "123");
        Response r = tradingSystemImpl.RemoveManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        System.out.println(r.getMessage());
        Assertions.assertTrue(r.getIsErr());
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
        Assertions.assertTrue(res3.getIsErr());
    }

    // requirement 4.7
    @Test
    void removeManagerByOwnerSuccess() {
        Response response = tradingSystemImpl.RemoveOwnerByOwner(userID,connID,ElinorID,storeid);
        boolean exist = tradingSystemImpl.stores.get(storeid).OwnersID().contains(ElinorID);
        Assertions.assertTrue(!exist && !response.getIsErr());
    }

    // requirement 4.7
    @Test
    void removeManagerNotByOwner() {
        tradingSystemImpl.AddNewOwner(userID, connID, storeid, ElinorID);
        Response response = tradingSystemImpl.RemoveOwnerByOwner(userID1, connID1, ElinorID, storeid);
        boolean exist = tradingSystemImpl.stores.get(storeid).OwnersID().contains(ElinorID);
        Assertions.assertTrue(exist && response.getIsErr());
    }

    // requirement 4.9
    @Test
    void ShowStoreWorkersSuccess(){
        tradingSystemImpl.AddNewManager(ElinorID, EconnID, ElinorStore, userID);
        tradingSystemImpl.AddNewOwner(ElinorID, EconnID, ElinorStore, NofetID);
        System.out.println(tradingSystemImpl.ShowStoreWorkers(ElinorID,EconnID,ElinorStore));
        Assertions.assertFalse(tradingSystemImpl.ShowStoreWorkers(ElinorID,EconnID,ElinorStore).getIsErr());
    }

    // requirement 4.9
    @Test
    void ShowStoreWorkersNotPermission(){
        Assertions.assertTrue(tradingSystemImpl.ShowStoreWorkers(userID,EconnID,ElinorStore).getIsErr());
    }

    // requirement 4.9
    @Test
    void ShowStoreWorkersStoreNotExist(){
        Assertions.assertTrue(tradingSystemImpl.ShowStoreWorkers(userID,EconnID,-1).getIsErr());
    }

    // requirement 4.9
    @Test
    void ShowStoreWorkersEmpty(){
        Object res= tradingSystemImpl.ShowStoreWorkers(ElinorID,EconnID,ElinorStore).getReturnObject().get("workers");
        Assertions.assertNull(res);
    }

    //endregion

    //region requirement 5

    @Test
    void ManagerOperationsSuccess() {
        tradingSystemImpl.AddNewManager(ElinorID, EconnID, ElinorStore, NofetID);
        LinkedList<PermissionEnum.Permission> p=new LinkedList<>();
        p.add(PermissionEnum.Permission.AddProduct);
        tradingSystemImpl.EditManagerPermissions(ElinorID,EconnID,ElinorStore,NofetID,p);
        Response response = tradingSystemImpl.AddProductToStore(NofetID, NconnID, ElinorStore, "Watermelon", "Fruits", 20.0, 10);
        Assertions.assertFalse(response.getIsErr());

        Integer productID = tradingSystemImpl.stores.get(ElinorStore).getProductID("Watermelon");
        Assertions.assertTrue(productID != -1);
    }

    @Test
    void ManagerOperationsFailed() {
        tradingSystemImpl.AddNewManager(ElinorID, EconnID, ElinorStore, NofetID);
        LinkedList<PermissionEnum.Permission> p=new LinkedList<>();
        p.add(PermissionEnum.Permission.AddProduct);
        tradingSystemImpl.EditManagerPermissions(ElinorID,EconnID,ElinorStore,NofetID,p);
        Response response = tradingSystemImpl.AddNewOwner(NofetID, NconnID, ElinorStore, userID);
        Assertions.assertTrue(response.getIsErr());

        Integer size = tradingSystemImpl.stores.get(ElinorStore).OwnersID().size();
        Assertions.assertEquals(size, 1);
    }

    //endregion

    //region purchase tests - requirement 2.9
    void setUpBeforePurchase(){
        tradingSystemImpl.AddProductToStore(NofetID, NconnID, NofetStore, "computer", "Technology", 3000.0,20);
        tradingSystemImpl.AddProductToStore(NofetID, NconnID, NofetStore, "Bag", "Beauty", 100.0,50);
        tradingSystemImpl.AddProductToStore(NofetID, NconnID, NofetStore, "Bed", "Fun", 4500.0,30);
    }


    @Test
    void HappyPurchase() {
        setUpBeforePurchase();
        QuantityLimitForStore exp = new QuantityLimitForStore(3, NofetStore);
        tradingSystemImpl.addBuyingPolicy(NofetID, NconnID, NofetStore, exp);
        Integer productID1 = Nstore.getProductID("computer");
        Integer productID2 = Nstore.getProductID("Bag");
        Integer preQuantity = Nstore.getQuantity(productID1);
        tradingSystemImpl.Logout(NconnID);
        tradingSystemImpl.AddProductToCart(EconnID, NofetStore, productID1, 1);
        tradingSystemImpl.AddProductToCart(EconnID, NofetStore, productID2, 1);
        Response response = tradingSystemImpl.subscriberPurchase(ElinorID, EconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        Assertions.assertFalse(response.getIsErr());

        //check Inventory after happy purchase
        Integer newQuantity = Nstore.getQuantity(productID1);
        Assertions.assertEquals(preQuantity, newQuantity+1);
    }

    @Test
    void SadPurchase_BuyingPolicy() {
        QuantityLimitForStore exp = new QuantityLimitForStore(2, ElinorStore);
        tradingSystemImpl.addBuyingPolicy(ElinorID, EconnID, ElinorStore, exp);
        tradingSystemImpl.AddProductToStore(ElinorID, EconnID, ElinorStore, "computer", "Technology", 3000.0,20);
        Integer productID1 = Estore.getProductID("computer");
        Integer preQuantity = Estore.getQuantity(productID1);
        Response response = tradingSystemImpl.AddProductToCart(NconnID, ElinorStore, productID1, 5);
        //Response response = tradingSystem.subscriberPurchase(NofetID, NconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        Assertions.assertTrue(response.getIsErr());

        //check Inventory after sad purchase
        Integer newQuantity = Estore.getQuantity(productID1);
        Assertions.assertEquals(preQuantity, newQuantity);
    }

    @Test
    void SadPurchase_WrongPaymentDetails() {
        setUpBeforePurchase();
        Integer productID1 = Nstore.getProductID("computer");
        Integer preQuantity = Nstore.getQuantity(productID1);
        tradingSystemImpl.Logout(NconnID);
        tradingSystemImpl.AddProductToCart(EconnID, NofetStore, productID1, 1);
        Response response = tradingSystemImpl.subscriberPurchase(ElinorID, EconnID, "123456789", "15","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        Assertions.assertTrue(response.getIsErr());

        //check Inventory after sad purchase
        Integer newQuantity = Nstore.getQuantity(productID1);
        Assertions.assertEquals(preQuantity, newQuantity);
    }

    @Test
    void SadPurchase_WrongSupplyDetails() {
        setUpBeforePurchase();
        Integer productID1 = Nstore.getProductID("computer");
        Integer preQuantity = Nstore.getQuantity(productID1);
        tradingSystemImpl.Logout(NconnID);
        tradingSystemImpl.AddProductToCart(EconnID, NofetStore, productID1, 1);
        Response response = tradingSystemImpl.subscriberPurchase(ElinorID, EconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Eilat","Israel","8458527");
        Assertions.assertTrue(response.getIsErr());

        //check Inventory after sad purchase
        Integer newQuantity = Nstore.getQuantity(productID1);
        Assertions.assertEquals(preQuantity, newQuantity);
    }

    @Test
    void SadPurchase_EmptyCart() {
        Response response = tradingSystemImpl.subscriberPurchase(userID, connID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        Assertions.assertTrue(response.getIsErr());
    }

    @Test
    void Sad_Purchase_Supply() {
        QuantityLimitForStore exp = new QuantityLimitForStore(100, NofetStore);
        tradingSystemImpl.addBuyingPolicy(NofetID, NconnID, NofetStore, exp);
        Integer productID1 = Nstore.getProductID("computer");
        tradingSystemImpl.AddProductToCart(EconnID, NofetStore, productID1, 10);

        //another user is buying this product
        tradingSystemImpl.AddProductToCart(connID, NofetStore, productID1, 15);
        tradingSystemImpl.subscriberPurchase(userID, connID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        //the previous user make the purchase
        Integer preQuantity = Nstore.getQuantity(productID1);
        Response response = tradingSystemImpl.subscriberPurchase(ElinorID, EconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        Assertions.assertTrue(response.getIsErr());

        //check Inventory after sad purchase
        Integer newQuantity = Nstore.getQuantity(productID1);
        Assertions.assertEquals(preQuantity, newQuantity);
    }

    //Parallel tests
    @Test
    void PurchaseParallel_Happy_TwoBuyersProduct() {
        //Prepare
        tradingSystemImpl.AddProductToStore(ElinorID, EconnID, ElinorStore, "computer", "Technology", 3000.0,2);
        Integer newProduct = tradingSystemImpl.stores.get(ElinorStore).getProductID("computer");
        //Create two clients with task to buy this product
        ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(2);

        List<PurchaseTaskUnitTests> taskList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            PurchaseTaskUnitTests task = new PurchaseTaskUnitTests("Client-" + i, ElinorStore, newProduct,
                    1,"123456789", "4", "2022" ,"123" ,"123456789" ,"Rager 101","Beer Sheva","Israel","8458527");
            taskList.add(task);
        }

        //Execute all tasks and get reference to Future objects
        List<Future<ResultUnitTests>> resultList = null;

        try {
            resultList = executor.invokeAll(taskList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();

        System.out.println("\n========Printing the results======");
        boolean[] isErrs = new boolean[2];
        for (int i = 0; i < resultList.size(); i++) {
            Future<ResultUnitTests> future = resultList.get(i);
            try {
                ResultUnitTests result = future.get();
//                System.out.println(result.getName() + ": " + result.getTimestamp());
                Response response = result.getResponse();
                System.out.println("Assert correctnes for " + result.getName() + ": response -> " + response + " ::" + result.getTimestamp());
                isErrs[i] = response.getIsErr();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        //Check that one of the client failed and the other succeed.
        Assertions.assertTrue(!isErrs[0] && !isErrs[1]);
    }

    @Test
    void PurchaseParallel_HappyFailed_TwoBuyersLastProduct_10times() {
        for(int test_try = 1; test_try <= 10; test_try++) {
            //Prepare
            tradingSystemImpl.AddProductToStore(ElinorID, EconnID, ElinorStore, "computer", "Technology", 3000.0,1);
            Integer newProduct = tradingSystemImpl.stores.get(ElinorStore).getProductID("computer");
            //Create two clients with task to buy this product
            ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(2);

            List<PurchaseTaskUnitTests> taskList = new ArrayList<>();
            for (int i = 0; i < 2; i++) {
                PurchaseTaskUnitTests task = new PurchaseTaskUnitTests("Client-" + i, ElinorStore, newProduct,
                        1,"123456789", "4", "2022" ,"123" ,"123456789" ,"Rager 101","Beer Sheva","Israel","8458527");
                taskList.add(task);
            }

            //Execute all tasks and get reference to Future objects
            List<Future<ResultUnitTests>> resultList = null;

            try {
                resultList = executor.invokeAll(taskList);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            executor.shutdown();

            System.out.println("\n========Printing the results======");
            boolean[] isErrs = new boolean[2];
            for (int i = 0; i < resultList.size(); i++) {
                Future<ResultUnitTests> future = resultList.get(i);
                try {
                    ResultUnitTests result = future.get();
//                System.out.println(result.getName() + ": " + result.getTimestamp());
                    Response response = result.getResponse();
                    System.out.println("Assert correctnes for " + result.getName() + ": response -> " + response + " ::" + result.getTimestamp());
                    isErrs[i] = response.getIsErr();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            //Check that one of the client failed and the other succeed.
            Assertions.assertTrue((isErrs[0] && !isErrs[1]) || (isErrs[1] && !isErrs[0]));
            tearDown();
            setup();
        }

    }
    //endregion

    //region Load and Timer tests

    @Test
    //TODO
    void register_100_Clients(){
        ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(2);

        List<RegisterTaskUnitTests> taskList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            RegisterTaskUnitTests task = new RegisterTaskUnitTests("Client-" + i);
            taskList.add(task);
        }

        //Execute all tasks and get reference to Future objects
        List<Future<ResultUnitTests>> resultList = null;

        try {
            resultList = executor.invokeAll(taskList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();

        System.out.println("\n========Printing the results======");

        assert resultList != null;
        for (int i = 0; i < resultList.size(); i++) {
            Future<ResultUnitTests> future = resultList.get(i);
            try {
                ResultUnitTests result = future.get();
                System.out.println(result.getName() + ": " + result.getTimestamp());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void purchase_100_Clients() {
        //Prepare
        tradingSystemImpl.AddProductToStore(ElinorID, EconnID, ElinorStore, "computer", "Technology", 3000.0,200);
        Integer newProduct = tradingSystemImpl.stores.get(ElinorStore).getProductID("computer");
        //Create 20 clients with task to buy this product
        ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(2);

        List<PurchaseTaskUnitTests> taskList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            PurchaseTaskUnitTests task = new PurchaseTaskUnitTests("Client-" + i, ElinorStore, newProduct,
                    1,"123456789", "4", "2022" ,"123" ,"123456789" ,"Rager 101","Beer Sheva","Israel","8458527");
            taskList.add(task);
        }

        //Execute all tasks and get reference to Future objects
        List<Future<ResultUnitTests>> resultList = null;
        try {
            resultList = executor.invokeAll(taskList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();

        System.out.println("\n========Printing the results======");
        boolean[] isErrs = new boolean[100];
        for (int i = 0; i < resultList.size(); i++) {
            Future<ResultUnitTests> future = resultList.get(i);
            try {
                ResultUnitTests result = future.get();
                Response response = result.getResponse();
                System.out.println("Assert correctness for " + result.getName() + ": response -> " + response + " ::" + result.getTimestamp());
                isErrs[i] = response.getIsErr();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        //Check that all of the clients succeed.
        for(int i = 0; i < 100; i++){
            Assertions.assertFalse(isErrs[i]);
        }
        Integer newQuantity = tradingSystemImpl.stores.get(ElinorStore).getQuantity(newProduct);
        Assertions.assertTrue(newQuantity == 100);
    }

    @Test
    void Subscribers_AND_Stores() {
        for(int i=0; i<100; i++){
            String newConnID = tradingSystemImpl.ConnectSystem().returnConnID();
            Response res1 = tradingSystemImpl.Register(newConnID, "Client-"+i,"123");
            Assertions.assertFalse(res1.getIsErr());
            newConnID = res1.returnConnID();
            Response res2 = tradingSystemImpl.Login(newConnID, "Client-"+i, "123");
            Assertions.assertFalse(res2.getIsErr());
        }

        for(int i=0; i<100; i++){
            Integer newUserID = tradingSystemImpl.getUserID("Client-"+i);
            String newConnID = tradingSystemImpl.getUserConnID(newUserID);
            Response res = tradingSystemImpl.AddStore(newUserID, newConnID, "It's A Test Num-"+i);
            Assertions.assertFalse(res.getIsErr());
        }

    }
    

    //endregion
    


   //region requirement 4.12, 6.6
   @Test
    void HappyDailyIncomeForStore(){
       Response res= tradingSystemImpl.getDailyIncomeForStore(NofetID,NofetStore,NconnID);
       Double DailyIncome=(Double) res.getReturnObject().get("DailyIncome");
       Assertions.assertEquals(DailyIncome, 50.0);
    }

   @Test
   void HappyDailyIncomeForStore_NotExistPurchase(){
       Store New=new Store("New",NofetID);
       tradingSystemImpl.stores.put(New.getId(),New);
       tradingSystemImpl.subscribers.get(NofetID).AddStore(New.getId());
       Response res= tradingSystemImpl.getDailyIncomeForStore(NofetID,New.getId(),NconnID);
       Double DailyIncome=(Double) res.getReturnObject().get("DailyIncome");
       Assertions.assertEquals(DailyIncome, 0);
   }

   @Test
   void SadDailyIncomeForStore_UserNotOwnerOfTheStore(){
       Store New=new Store("New",NofetID);
       tradingSystemImpl.stores.put(New.getId(),New);
       tradingSystemImpl.subscribers.get(NofetID).AddStore(New.getId());
       Response res= tradingSystemImpl.getDailyIncomeForStore(ElinorID,New.getId(),EconnID);
       Assertions.assertEquals(res.getMessage(),"getDailyIncomeForStore: The user " + ElinorID + " is not the owner of the store");
   }

   @Test
   void SadDailyIncomeForStore_StoreNotExistInTheSystem(){
       Response res= tradingSystemImpl.getDailyIncomeForStore(ElinorID,100,EconnID);
       Assertions.assertEquals(res.getMessage(),"getDailyIncomeForStore: The store " + 100 + " doesn't exist in the system");
   }

   @Test
   void HappyDailyIncomeForSystem(){
       String tmpConn= tradingSystemImpl.ConnectSystem().returnConnID();
       Response r1=tradingSystemImpl.Login(tmpConn,"amit","qweasd");
       Response r2=tradingSystemImpl.getDailyIncomeForSystem(r1.returnUserID(),r1.returnConnID());
       Double DailyIncome=(Double) r2.getReturnObject().get("DailyIncome");
       Assertions.assertEquals(DailyIncome, 70.0);
   }

   @Test
   void SadDailyIncomeForSystem_UserNotTheSystemManager(){
        Response r=tradingSystemImpl.getDailyIncomeForSystem(NofetID,NconnID);
        Assertions.assertEquals(r.getMessage(),"getDailyIncomeForSystem: The user "+NofetID+"  try to see the Daily Income for the system but he is not the admin of the system");
   }

   //endregion

    //region requirement 3.8
    // Subscriber Bidding
    @Test
    void HappysubscriberBidding() {
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        tradingSystemImpl.AddProductToCart(NconnID,NofetStore,1,3);

        Response r8=tradingSystemImpl.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        Assertions.assertFalse(r8.getIsErr());
        System.out.println(r8.getMessage());
    }

    @Test
    void SadsubscriberBidding_unsubscribe() {
        Response r1=tradingSystemImpl.subscriberBidding(-1,"",1,1,1.1,1);
        Assertions.assertTrue(r1.getIsErr());
        System.out.println(r1.getMessage());
    }

    @Test
    void SadsubscriberBidding_storeNotExist() {
        Response r2=tradingSystemImpl.subscriberBidding(NofetID,NconnID,-1,1,1.1,1);
        Assertions.assertTrue(r2.getIsErr());
        System.out.println(r2.getMessage());
    }

    @Test
    void SadsubscriberBidding_productNotExist() {
        Response r3=tradingSystemImpl.subscriberBidding(NofetID,NconnID,NofetStore,-1,1.1,1);
        Assertions.assertTrue(r3.getIsErr());
        System.out.println(r3.getMessage());
    }

    @Test
    void SadsubscriberBidding_productInTheCartAlready() {
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        tradingSystemImpl.AddProductToCart(NconnID,NofetStore,1,3);
        Response r4=tradingSystemImpl.subscriberBidding(NofetID,NconnID,NofetStore,1,1.1,1);
        Assertions.assertTrue(r4.getIsErr());
        System.out.println(r4.getMessage());
    }

    @Test
    void SadsubscriberBidding_priceNotInRange() {
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);

        Response r5=tradingSystemImpl.subscriberBidding(NofetID,NconnID,NofetStore,2,-1,1);
        Assertions.assertTrue(r5.getIsErr());
        System.out.println(r5.getMessage());

        Response r6=tradingSystemImpl.subscriberBidding(NofetID,NconnID,NofetStore,2,70,1);
        Assertions.assertTrue(r6.getIsErr());
        System.out.println(r6.getMessage());

    }

    @Test
    void SadsubscriberBidding_NegativeQuantity() {
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        tradingSystemImpl.AddProductToCart(NconnID,NofetStore,1,3);

        Response r7=tradingSystemImpl.subscriberBidding(NofetID,NconnID,NofetStore,2,3,-1);
        Assertions.assertTrue(r7.getIsErr());
        System.out.println(r7.getMessage());
    }

    @Test
    void SadsubscriberBidding_BidAlreadyExist() {
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        tradingSystemImpl.AddProductToCart(NconnID,NofetStore,1,3);

        Response r8=tradingSystemImpl.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        Assertions.assertFalse(r8.getIsErr());
        Response r9=tradingSystemImpl.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        Assertions.assertTrue(r9.getIsErr());
        System.out.println(r8.getMessage());

    }

    //endregion..1

    //region requirement 3.8
    //Response to subscriber bidding
    @Test
    void HappyResponseToSubscriberBidding() {
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        tradingSystemImpl.AddProductToCart(NconnID,NofetStore,1,3);

        Response r=tradingSystemImpl.subscriberBidding(NofetID,NconnID,NofetStore,2,2,2);
        Response r8=tradingSystemImpl.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,2,NofetID,2);
        Assertions.assertFalse(r8.getIsErr());
    }

    @Test
    void SadUnsubscribe() {
        Response r0=tradingSystemImpl.ResponseForSubmissionBidding(-1,"",1,1,1.1,1,1);
        Assertions.assertTrue(r0.getIsErr());
        System.out.println(r0.getMessage());

        Response r1=tradingSystemImpl.ResponseForSubmissionBidding(NofetID,NconnID,1,1,1.1,1,1);
        Assertions.assertTrue(r1.getIsErr());
        System.out.println(r1.getMessage());
    }

    @Test
    void SadStoreNotExist() {
        Response r2=tradingSystemImpl.ResponseForSubmissionBidding(NofetID,NconnID,-1,-1,1.1,ElinorID,1);
        Assertions.assertTrue(r2.getIsErr());
        System.out.println(r2.getMessage());
    }

    @Test
    void SadProductNotExist() {
        Response r3=tradingSystemImpl.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,-1,1.1,ElinorID,1);
        Assertions.assertTrue(r3.getIsErr());
        System.out.println(r3.getMessage());
    }

    @Test
    void SadBideHadResponseAlready() {
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        tradingSystemImpl.AddProductToCart(NconnID,NofetStore,1,3);

        Response r=tradingSystemImpl.subscriberBidding(NofetID,NconnID,NofetStore,2,2,2);
        Response r8=tradingSystemImpl.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,2,NofetID,2);
        Assertions.assertFalse(r8.getIsErr());
        Response r9=tradingSystemImpl.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,3,NofetID,1);
        Assertions.assertTrue(r9.getIsErr());
        System.out.println(r8.getMessage());

    }

    @Test
    void SadPriceNotInRange() {
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        tradingSystemImpl.AddProductToCart(NconnID,NofetStore,1,3);

        Response r5=tradingSystemImpl.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,-1,ElinorID,1);
        Assertions.assertTrue(r5.getIsErr());
        System.out.println(r5.getMessage());

        Response r6=tradingSystemImpl.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,70,ElinorID,1);
        Assertions.assertTrue(r6.getIsErr());
        System.out.println(r6.getMessage());
    }

    @Test
    void SadNegativeQuantity() {
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        tradingSystemImpl.AddProductToCart(NconnID,NofetStore,1,3);

        Response r7=tradingSystemImpl.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,3,ElinorID,-1);
        Assertions.assertTrue(r7.getIsErr());
        System.out.println(r7.getMessage());

    }

    @Test
    void showBids() {
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        Response r0=tradingSystemImpl.subscriberBidding(NofetID,NconnID,NofetStore,1,3,2);
        Response r1= tradingSystemImpl.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        Response r=tradingSystemImpl.ShowBids(NofetID,NconnID,NofetStore);
        Assertions.assertFalse(r.getIsErr());
    }

    //endregion.2

    //region requirement 3.8
    // Add special product

    @Test
    void HappyAddSpecialProduct() {
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);

        Response r8=tradingSystemImpl.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        Assertions.assertFalse(r8.getIsErr());
        System.out.println(r8.getMessage());
    }

    @Test
    void SadProductExistInCart() {
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        Response r1=tradingSystemImpl.subscriberBidding(NofetID,NconnID,NofetStore,2,1.1,1);
        tradingSystemImpl.AddProductToCart(NconnID,NofetStore,2,3);
        Response r2=tradingSystemImpl.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,3.0,NofetID,3);
        System.out.println(r1.getMessage());
        System.out.println(r2.getMessage());
        Assertions.assertFalse(r1.getIsErr());
        Assertions.assertTrue(r2.getIsErr());
    }

    @Test
    void SadProductsNotInStock() {
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        Response r1=tradingSystemImpl.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        Response r2=tradingSystemImpl.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,3.0,NofetID,30);
        System.out.println(r1.getMessage());
        System.out.println(r2.getMessage());
        Assertions.assertFalse(r1.getIsErr());
        Assertions.assertTrue(r2.getIsErr());
    }

    @Test
    void SadAddSpecialProduct_productNotExist() {
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        tradingSystemImpl.RemoveProduct(NofetID,NofetStore,2,NconnID);
        Response r8=tradingSystemImpl.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        System.out.println(r8.getMessage());
        Assertions.assertTrue(r8.getIsErr());
    }

    @Test
    void SadProductNotInStore() {
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        tradingSystemImpl.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        tradingSystemImpl.RemoveProduct(NofetID,NofetStore,2,NconnID);
        Response r=tradingSystemImpl.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,3.0,NofetID,30);
        System.out.println(r.getMessage());
        Assertions.assertTrue(r.getIsErr());
    }

    @Test
    void SadProductAgainstThePolicy() {
        tradingSystemImpl.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        Store s= tradingSystemImpl.stores.get(NofetStore);
        Integer productID1 =s.getProductID("1");
        QuantityLimitForProduct exp1 = new QuantityLimitForProduct(5, productID1);
        OrComposite or = new OrComposite();
        or.add(exp1);
        BuyingPolicy b=new BuyingPolicy(s.getId(),or);
        tradingSystemImpl.stores.get(NofetStore).setBuyingPolicy(b);
        Response r1=tradingSystemImpl.subscriberBidding(NofetID,NconnID,NofetStore,productID1,3,2);
        Response r2=tradingSystemImpl.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,productID1,3.0,NofetID,6);
        System.out.println(r1.getMessage());
        System.out.println(r2.getMessage());
        Assertions.assertFalse(r1.getIsErr());
        Assertions.assertTrue(r2.getIsErr());
    }
    //endregion

    /*
    //region template tests
    @Test
    void CreateSaleTest() {
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
        Sale s = tradingSystem.createSaleForDiscount(1,  Max);
        assertTrue(true);
    }
    @Test
    void CreateExpTest2() {

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

        Expression exp = tradingSystem.CreateExpForBuy(1,  OrComposite);
        assertTrue(true);
    }

    //endregion

*/
}