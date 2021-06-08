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
import TradingSystem.Server.DomainLayer.UserComponent.PermissionEnum;
 import TradingSystem.Server.DomainLayer.Task.*;
// import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class myTest {

    @Autowired
    TradingSystemImplRubin tradingSystem;

    int userID;
    int userID1;
    int storeid;
    int productId;
    int productId2;
    String connID;
    String connID1;

    String EconnID;
    String NconnID;
    int ElinorID;
    int NofetID;
    int NofetStore;
    int ElinorStore;
    Store Nstore;
    Store Estore;

    @Before
    public void setUp(){
        connID= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.Register(connID,"reutlevy","8119");

        userID= response.returnUserID();
        connID= tradingSystem.Login(connID,"reutlevy","8119").returnConnID();


        Response responseR = tradingSystem.AddStore(userID,connID,"store8");
        storeid = (Integer) responseR.getReturnObject().get("storeID");
        for(Store store1: tradingSystem.stores.values()){
            if(store1.getName().equals("store8")){
                storeid=store1.getId();
            }
        }
        tradingSystem.AddProductToStore(userID,connID,storeid,"test","test1",19.0,9);
        productId= tradingSystem.stores.get(storeid).getProductID("test");
        tradingSystem.AddProductToStore(userID,connID,storeid,"test2","test",19.0,9);
        productId2= tradingSystem.stores.get(storeid).getProductID("test2");

        connID1= tradingSystem.ConnectSystem().returnConnID();
        Response response1= tradingSystem.Register(connID1,"reutlevy8","8119");
        userID1= response1.returnUserID();
        connID1= tradingSystem.Login(connID1,"reutlevy8","8119").returnConnID();


        NconnID = tradingSystem.ConnectSystem().returnConnID();
        Response r1= tradingSystem.Register(NconnID, "nofet", "123");
        NofetID= r1.returnUserID();
        NconnID= tradingSystem.Login(NconnID, "nofet", "123").returnConnID();

        EconnID = tradingSystem.ConnectSystem().returnConnID();
        Response r2= tradingSystem.Register(EconnID, "elinor", "123");
        ElinorID= r2.returnUserID();
        EconnID= tradingSystem.Login(EconnID, "elinor", "123").returnConnID();



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
        tradingSystem.AddNewOwner(userID,connID,storeid,ElinorID);

        Estore = tradingSystem.stores.get(ElinorStore);
        Nstore = tradingSystem.stores.get(NofetStore);
/*
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

*/


    }

    public void tearDown(){
        tradingSystem.ClearSystem();
    }


    //region requirement 2

    // requirement 2.1
    @Test
    public void connectSystem() {
        Response response= tradingSystem.ConnectSystem();
        assertTrue(response.returnConnID()!="" && response.getIsErr()==false);
    }

    // requirement 2.2
    @Test
    public void exitGood() {
        String connId= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.Exit(connId);
        Assertions.assertFalse(response.getIsErr());
    }

    // requirement 2.2
    @Test
    public void exitBad() {
        String connId= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Exit(connId);
        Response response= tradingSystem.Exit(connId);
        assertTrue(response.getIsErr());
    }

    // requirement 2.3
    @Test
    public void registerGood() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.Register(connID,"reutlevy11","8111996");
        Assertions.assertFalse(response.getIsErr());
    }

    // requirement 2.3
    @Test
    public void registerExistUser() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(connID,"reutlevy30","8111996");
        Response response= tradingSystem.Register(connID,"reutlevy30","reut");
        assertTrue(response.getIsErr());
    }

    // requirement 2.3
    //TODO
    @Test
    public void registerParallelSadSameName(){
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

//        assert resultList != null;
//        for (int i = 0; i < resultList.size(); i++) {
//            Future<ResultUnitTests> future = resultList.get(i);
//            try {
//                ResultUnitTests result = future.get();
//                System.out.println(result.getName() + ": " + result.getTimestamp());
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//        }


        boolean[] isErrs = new boolean[2];
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
        //Check that one of the client failed and the other succeed.
        assertTrue(!isErrs[0] && !isErrs[1] && (isErrs[0] || isErrs[1]));
    }



    // requirement 2.4
    @Test
    public void loginSuccess() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(connID,"reutlevy300","811199");
        Response response= tradingSystem.Login(connID,"reutlevy300","811199");
        Assertions.assertFalse(response.getIsErr() && response.returnUserID()<0);
    }

    // requirement 2.4
    @Test
    public void loginWrongUserName() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.Login(connID,"reutlevy3","811199");
        assertTrue(response.getIsErr());
    }

    // requirement 2.4
    @Test
    public void loginWrongPassword() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(connID,"reutlevy30","811199");
        Response response= tradingSystem.Login(connID,"reutlevy30","8111996");
        assertTrue(response.getIsErr());
    }

    // requirement 2.5
    @Test
    public void showAllStoresGood() {
        Response res = tradingSystem.ShowAllStores();
        Assertions.assertEquals(res.getIsErr(),false);
    }

    //endregion

    //region requirement 3

    // requirement 3.1
    @Test
    public void LogoutHappy() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(connID,"Rubin","123");
        Response res = tradingSystem.Login(connID,"Rubin","123");
        connID = res.returnConnID();
        Response response = tradingSystem.Logout(connID);
        Assertions.assertFalse(response.getIsErr());
    }

    // requirement 3.1
    @Test
    public void LogoutSad() {
        String connID= tradingSystem.ConnectSystem().returnConnID();
        Response res = tradingSystem.Register(connID,"Rubin","123");
        connID = res.returnConnID();
        Response response = tradingSystem.Logout(connID);
        assertTrue(response.getIsErr());
    }

    // requirement 3.2
    @Test
    public void AddStoreNotSubscriber() { ;
        Response response= tradingSystem.AddStore(11,connID,"Store3");
        assertTrue(response.getIsErr());
    }

    // requirement 3.2
    @Test
    public void AddStoreSameName() {
        tradingSystem.AddStore(userID,connID,"Store3");
        Response response= tradingSystem.AddStore(userID,connID,"Store3");
        assertTrue(response.getIsErr());
    }

    // requirement 3.2
    @Test
    public void AddStoreSuccess() {
        Response response= tradingSystem.AddStore(userID,connID,"Stor143");
        Assertions.assertFalse(response.getIsErr());
    }

    // requirement 3.3
    @Test
    public void WriteCommentSuccess() {
        setUpBeforePurchase();
        Integer productID1 = Nstore.getProductID("computer");
        tradingSystem.AddProductToCart(EconnID, NofetStore, productID1, 1);
        tradingSystem.subscriberPurchase(ElinorID, EconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        Response response = tradingSystem.WriteComment(ElinorID,EconnID, NofetStore, productID1, "Amazing");
        Assertions.assertFalse(response.getIsErr());
        Integer size = tradingSystem.stores.get(NofetStore).getProduct(productID1).getComments().size();
        Assertions.assertEquals(size, 1);
    }

    // requirement 3.3
    @Test
    public void WriteCommentWrongStoreID() {
        setUpBeforePurchase();
        Integer productID1 = Nstore.getProductID("computer");
        tradingSystem.AddProductToCart(EconnID, NofetStore, productID1, 1);
        tradingSystem.subscriberPurchase(ElinorID, EconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        Response response = tradingSystem.WriteComment(ElinorID,EconnID, 100, productID1, "Amazing");
        assertTrue(response.getIsErr());
        Integer size = tradingSystem.stores.get(NofetStore).getProduct(productID1).getComments().size();
        Assertions.assertEquals(size, 0);
    }

    // requirement 3.3
    @Test
    public void WriteCommentNotInHistory() {
        setUpBeforePurchase();
        Integer productID1 = Nstore.getProductID("computer");

        Response response = tradingSystem.WriteComment(ElinorID,EconnID, NofetStore, productID1, "Amazing");
        assertTrue(response.getIsErr());
        Integer size = tradingSystem.stores.get(NofetStore).getProduct(productID1).getComments().size();
        Assertions.assertEquals(size, 0);

    }

    // requirement 3.3
    @Test
    public void WriteCommentExistComment() {
        setUpBeforePurchase();
        Integer productID1 = Nstore.getProductID("computer");
        tradingSystem.AddProductToCart(EconnID, NofetStore, productID1, 1);
        tradingSystem.subscriberPurchase(ElinorID, EconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        tradingSystem.WriteComment(ElinorID,EconnID, NofetStore, productID1, "Amazing");
        Response response = tradingSystem.WriteComment(ElinorID,EconnID, NofetStore, productID1, "WTF");
        assertTrue(response.getIsErr());
        Integer size = tradingSystem.stores.get(NofetStore).getProduct(productID1).getComments().size();
        Assertions.assertEquals(size, 1);
    }

    // requirement 3.7
    @Test
    public void UserHistorySuccess() {
        setUpBeforePurchase();
        Integer productID1 = Nstore.getProductID("computer");
        Integer productID2 = Nstore.getProductID("Bag");
        tradingSystem.AddProductToCart(EconnID, NofetStore, productID1, 1);
        tradingSystem.subscriberPurchase(ElinorID, EconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        tradingSystem.AddProductToCart(EconnID, NofetStore, productID2, 1);
        tradingSystem.subscriberPurchase(ElinorID, EconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        Response res = tradingSystem.ShowSubscriberHistory(ElinorID, EconnID);
        Assertions.assertFalse(res.getIsErr());
        List<DummyShoppingHistory> list = (List<DummyShoppingHistory>) res.getReturnObject().get("history");
        Assertions.assertEquals(list.size(), 2);
    }

    // requirement 3.7
    @Test
    public void UserHistoryFailed() {
        Response res = tradingSystem.ShowSubscriberHistory(ElinorID, EconnID);
        assertTrue(res.getIsErr());
    }

    //endregion

    //region requirement 4

    // requirement 4.1
    @Test
    public void AddProductSuccess(){
        Response response= tradingSystem.AddProductToStore(userID,connID,storeid,"prod8","food",11.0,9);
        Assertions.assertFalse(response.getIsErr());
    }

    // requirement 4.1
    @Test
    public void AddProductInvalidAmount(){
        tradingSystem.AddStore(userID,connID,"store11");
        int storeid=0;
        for(Store store1: tradingSystem.stores.values()){
            if(store1.getName().equals("store11")){
                storeid=store1.getId();
            }
        }
        Response response= tradingSystem.AddProductToStore(userID,connID,storeid,"prod3","food",11.0,-1);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    public void AddProductInvalidPermission(){
        Response response= tradingSystem.AddProductToStore(userID,connID,storeid,"prod3","food",11.0,-1);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    public void AddProductInValidDetails(){
        Response response= tradingSystem.AddProductToStore(userID,connID,storeid,"prod3","food",-1,11);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    public void ChangeProductQuantitySuccess(){
        Response response= tradingSystem.ChangeQuantityProduct(userID,connID,storeid,productId,10);
        Assertions.assertFalse(response.getIsErr());
    }

    // requirement 4.1
    @Test
    public void EditProductWrongQuantity(){
        Response response= tradingSystem.EditProduct(userID,connID,storeid,productId,"prod4","food",12.0,-9);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    public void ChangeProductQuantityInvalidPermission(){
        Response response= tradingSystem.ChangeQuantityProduct(userID1,connID1,storeid,productId,10);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    public void ChangeProductWrongQuantity(){
        Response response= tradingSystem.ChangeQuantityProduct(userID,connID,storeid,productId,-10);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    public void EditProductSuccess(){
        Response response= tradingSystem.EditProduct(userID,connID,storeid,productId,"prod3","food",12.0,9);
        Assertions.assertFalse(response.getIsErr());
    }

    // requirement 4.1
    @Test
    public void EditProductInvalidPermission(){
        String connID1= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.Register(connID1,"reutlevy30","8119");
        int userID1= response.returnUserID();
        connID1= tradingSystem.Login(connID1,"reutlevy30","8119").returnConnID();
        response= tradingSystem.EditProduct(userID1,connID1,storeid,productId,"prod4","food",12.0,9);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    public void EditProductWrongPrice(){
        Response response= tradingSystem.EditProduct(userID,connID,storeid,productId,"prod4","food",-12.0,9);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    public void RemoveProductSuccess(){
        Response response= tradingSystem.RemoveProduct(userID,storeid,productId2,connID);
        Assertions.assertFalse(response.getIsErr());
    }

    // requirement 4.1
    @Test
    public void RemoveProductInvalidPermission(){
        String connID1= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.Register(connID1,"reutlevy30","8119");
        int userID1= response.returnUserID();
        connID1= tradingSystem.Login(connID1,"reutlevy30","8119").returnConnID();
        response= tradingSystem.RemoveProduct(userID1,storeid,productId,connID1);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    public void RemoveProductNotExist(){
        Store store= new Store("store11", userID);
        tradingSystem.AddStore(userID,connID,"store11");
        Product product=new Product(store.getId(), store.getName(),4,"prod4","food",7.0,11);
        Response response= tradingSystem.RemoveProduct(userID,storeid,product.getProductID(),connID);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    public void removeProductFromStoreWhileOtherClientBuyingItTest_Parallel() {
        List<boolean[]> isErrsTotal = new ArrayList<>();
        for (int test_i = 0; test_i < 10; test_i++) {
            //Prepare
            tradingSystem.AddProductToStore(ElinorID, EconnID, ElinorStore, "Sneakers", "Shoes", 150.0, 25);
            Integer newProduct = tradingSystem.stores.get(ElinorStore).getProductID("Sneakers");
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
                    Response response = result.getResponse();
                    System.out.println("Assert correctnes for " + result.getName() + ": response -> " + response + " ::" + result.getTimestamp());
                    isErrs[i] = response.getIsErr();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            //Check that one of the client failed and the other succeed.
            //assertTrue((isErrs[0] && !isErrs[1]) || (isErrs[1] && !isErrs[0]));
            isErrsTotal.add(isErrs);
            tearDown();
        }
        boolean ans = false;
        for(boolean[] errArr : isErrsTotal) {
            if ((errArr[0] && !errArr[1]) || (errArr[1] && !errArr[0])) {
                ans = true;
                break;
            }
        }
        assertTrue(ans);
        System.out.println("========Printing the results - TOTAL PARALLEL ======");
        for(int i=0; i<isErrsTotal.size(); i++) {
            System.out.printf("%d: purchase: %s remove: %s\n", i, isErrsTotal.get(i)[0], isErrsTotal.get(i)[1]);
        }
    }

    // requirement 4.2
    @Test
    public void HappyInfoPolicies() {
        QuantityLimitForProduct exp = new QuantityLimitForProduct(1, productId);
        tradingSystem.addBuyingPolicy(userID, connID, storeid, exp);
        Response res = tradingSystem.GetPoliciesInfo(userID, storeid, connID);
        Assertions.assertFalse(res.getIsErr());
    }

    // requirement 4.2
    @Test
    public void SadInfoPolicies() {
        Response res = tradingSystem.GetPoliciesInfo(userID, storeid, connID);
        Assertions.assertFalse(res.getIsErr());
        BuyingPolicy BP = (BuyingPolicy) res.getReturnObject().get("BuyingPolicy");
        DiscountPolicy DP = (DiscountPolicy) res.getReturnObject().get("DiscountPolicy");
        Assertions.assertEquals(BP.getExp(), null);
        Assertions.assertEquals(DP.getSale(), null);
    }

    // requirement 4.3
    @Test
    public void NewOwnerSuccess() {
        String gust2 = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust2, "roee", "123");
        Response res2= tradingSystem.Login(gust2, "roee", "123");
        Response r1 = tradingSystem.AddNewOwner(NofetID, NconnID, NofetStore, res2.returnUserID());
        System.out.println(r1.getMessage());
        Assertions.assertFalse(r1.getIsErr());
    }

    // requirement 4.3
    @Test
    public void NewOwnerNotConnected() {
        Response r = tradingSystem.AddNewOwner(NofetID, "--", NofetStore, ElinorID);
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.3
    @Test
    public void NewOwnerNotSubscriber() {
        Response r1 = tradingSystem.AddNewOwner(-1, NconnID, NofetStore, ElinorID);
        System.out.println(r1.getMessage());
        assertTrue(r1.getIsErr());
        Response r2 = tradingSystem.AddNewOwner(NofetID, NconnID, NofetStore, 20);
        System.out.println(r2.getMessage());
        assertTrue(r2.getIsErr());
    }

    // requirement 4.3
    @Test
    public void AddNewOwnerIsNotTheOwner() {
        String gust2 = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust2, "roee", "123");
        Response res2= tradingSystem.Login(gust2, "roee", "123");
        Response res3 = tradingSystem.AddNewOwner(res2.returnUserID(), res2.returnConnID(), NofetStore, ElinorID);
        System.out.println(res3.getMessage());
        assertTrue(res3.getIsErr());
    }

    // requirement 4.3
    @Test
    public void NewOwnerDouble() {
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
    public void NewOwnerAlreadyManager() {
        String gust = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust, "Hadas", "123");
        Response res= tradingSystem.Login(gust, "Hadas", "123");
        tradingSystem.AddNewManager(NofetID, NconnID, NofetStore, res.returnUserID());
        Response r = tradingSystem.AddNewOwner(NofetID, NconnID, NofetStore, res.returnUserID());
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.4
    @Test
    public void removeOwnerSuccess() {
        tradingSystem.AddNewOwner(NofetID, NconnID, NofetStore, ElinorID);
        tradingSystem.AddNewOwner(ElinorID, EconnID, NofetStore, userID);
        Response response = tradingSystem.RemoveOwnerByOwner(NofetID, NconnID, ElinorID, NofetStore);
        Assertions.assertFalse(response.getIsErr());

        Integer size = tradingSystem.stores.get(NofetStore).OwnersID().size();
        Assertions.assertEquals(size, 1);
    }

    // requirement 4.4
    @Test
    public void removeOwnerNotAppointment() {
        tradingSystem.AddNewOwner(NofetID, NconnID, NofetStore, ElinorID);
        tradingSystem.AddNewOwner(NofetID, NconnID, NofetStore, userID);
        Response response = tradingSystem.RemoveOwnerByOwner(userID, connID, ElinorID, NofetStore);
        assertTrue(response.getIsErr());

        Integer size = tradingSystem.stores.get(NofetStore).OwnersID().size();
        Assertions.assertEquals(size, 4);
    }

    // requirement 4.4
    @Test
    public void removeOwnerNotOwner1() {
        setUp();
        tradingSystem.AddNewOwner(NofetID, NconnID, NofetStore, ElinorID);
        Response response = tradingSystem.RemoveOwnerByOwner(userID, connID, ElinorID, NofetStore);
        assertTrue(response.getIsErr());

        Integer size = tradingSystem.stores.get(NofetStore).OwnersID().size();
        Assertions.assertEquals(size, 2);
    }

    // requirement 4.4
    @Test
    public void removeOwnerNotOwner2() {
        Response response = tradingSystem.RemoveOwnerByOwner(NofetID, NconnID, userID, NofetStore);
        assertTrue(response.getIsErr());

        Integer size = tradingSystem.stores.get(NofetStore).OwnersID().size();
        Assertions.assertEquals(size, 2);
    }

    // requirement 4.5
    @Test
    public void NewManagerSuccess() {
        String gust = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust, "r", "123");
        Response res = tradingSystem.Login(gust, "r", "123");
        Response r1 = tradingSystem.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        System.out.println(r1.getMessage());
        Assertions.assertFalse(r1.getIsErr());
    }

    // requirement 4.5
    @Test
    public void NewManagerNotConnected() {
        Response r = tradingSystem.AddNewManager(NofetID, "--", NofetStore, ElinorID);
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.5
    @Test
    public void NewManagerNotSubscriber() {
        Response r1 = tradingSystem.AddNewManager(-1, NconnID, NofetStore, ElinorID);
        System.out.println(r1.getMessage());
        assertTrue(r1.getIsErr());
        Response r2 = tradingSystem.AddNewManager(NofetID, NconnID, NofetStore, 20);
        System.out.println(r2.getMessage());
        assertTrue(r2.getIsErr());
    }

    // requirement 4.5
    @Test
    public void AddNewManagerIsNotTheOwner() {
        String gust2 = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust2, "roee", "123");
        Response res2 = tradingSystem.Login(gust2, "roee", "123");
        Response res3 = tradingSystem.AddNewManager(res2.returnUserID(), res2.returnConnID(), NofetStore, ElinorID);
        System.out.println(res3.getMessage());
        assertTrue(res3.getIsErr());
    }

    // requirement 4.5
    @Test
    public void NewManagerDouble() {
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
    public void NewManagerAlreadyOwner() {
        String gust = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust, "Amit", "123");
        Response res= tradingSystem.Login(gust, "Amit", "123");
        tradingSystem.AddNewOwner(ElinorID, EconnID, ElinorStore, res.returnUserID());
        Response r = tradingSystem.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.5
    @Test
    public void AddManager_Parallel_TwoOwnerAppointManagerTogether() {
        List<boolean[]> isErrsTotal = new ArrayList<>();
        //for(int test_i = 0; test_i < 100; test_i++) {
            //Prepare

            //appoint Nofet to owner
            tradingSystem.AddNewOwner(ElinorID, EconnID, ElinorStore, NofetID);
            Integer size = tradingSystem.stores.get(ElinorStore).OwnersID().size();
            assertTrue(size == 2);

            //Create two clients with task to add manager
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
        //}
//
//        boolean ans = false;
//        for(boolean[] errArr : isErrsTotal) {
//            if ((errArr[0] && errArr[1]) || (!errArr[1] && !errArr[0])) {
//                ans = true;
//                break;
//            }
//        }
//        System.out.println("========Printing the results - TOTAL PARALLEL ======");
//        for(int i=0; i<isErrsTotal.size(); i++) {
//            System.out.printf("%d: purchase: %s remove: %s\n", i, isErrsTotal.get(i)[0], isErrsTotal.get(i)[1]);
//        }
//        assertTrue(ans);
//
//        Response res = tradingSystem.AddNewManager(ElinorID, EconnID, ElinorStore, userID);
//        boolean isManager = tradingSystem.stores.get(ElinorStore).checkManager(userID);
//        Assertions.assertFalse(res.getIsErr());
//        assertTrue(isManager);
    }

    // requirement 4.6
    @Test
    public void EditManagerPermissionsSuccess() {
        String gust = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust, "m", "123");
        Response res = tradingSystem.Login(gust, "m", "123");
        tradingSystem.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        LinkedList<PermissionEnum.Permission> p=new LinkedList<>();
        p.add(PermissionEnum.Permission.AddProduct);
        tradingSystem.EditManagerPermissions(ElinorID,EconnID,ElinorStore,res.returnUserID(),p);
        boolean r1 = tradingSystem.hasPermission(res.returnUserID(),ElinorStore, PermissionEnum.Permission.AddProduct);
        boolean r2 = tradingSystem.hasPermission(res.returnUserID(),ElinorStore, PermissionEnum.Permission.GetInfoOfficials);
        Assertions.assertTrue(r1);
        Assertions.assertFalse(r2);
    }

    // requirement 4.6
    @Test
    public void EditManagerPermissionsNotConnected() {
        Response r = tradingSystem.EditManagerPermissions(NofetID, "--", NofetStore, ElinorID,null);
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.6
    @Test
    public void EditManagerPermissionsIsNotSubscriber() {
        Response r1 = tradingSystem.EditManagerPermissions(-1, NconnID, NofetStore, ElinorID,null);
        System.out.println(r1.getMessage());
        assertTrue(r1.getIsErr());
        Response r2 = tradingSystem.EditManagerPermissions(NofetID, NconnID, NofetStore, 20,null);
        System.out.println(r2.getMessage());
        assertTrue(r2.getIsErr());
    }

    // requirement 4.6
    @Test
    public void EditManagerPermissionsIsNotTheOwner() {
        String gust2 = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust2, "l", "123");
        Response res2 = tradingSystem.Login(gust2, "l", "123");
        LinkedList<PermissionEnum.Permission> p=new LinkedList<>();
        p.add(PermissionEnum.Permission.AddProduct);
        Response res3 = tradingSystem.EditManagerPermissions(res2.returnUserID(), res2.returnConnID(), NofetStore, ElinorID,p);
        System.out.println(res3.getMessage());
        assertTrue(res3.getIsErr());
    }

    // requirement 4.6
    @Test
    public void EditManagerPermissionsIsNotTheAppointment() {
        String gust = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust, "D", "123");
        Response res = tradingSystem.Login(gust, "D", "123");
        LinkedList<PermissionEnum.Permission> p=new LinkedList<>();
        p.add(PermissionEnum.Permission.AddProduct);
        tradingSystem.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        Response res3 = tradingSystem.EditManagerPermissions(NofetID, NconnID, ElinorStore, res.returnUserID(),p);
        System.out.println(res3.getMessage());
        assertTrue(res3.getIsErr());
    }

    // requirement 4.7
    @Test
    public void RemoveManagerSuccess() {
        String gust = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust, "Deme", "123");
        Response res= tradingSystem.Login(gust, "Deme", "123");
        tradingSystem.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        Response r1 = tradingSystem.RemoveManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        System.out.println(r1.getMessage());
        Assertions.assertFalse(r1.getIsErr());
    }

    // requirement 4.7
    @Test
    public void RemoveManagerNotConnected() {
        Response r = tradingSystem.RemoveManager(NofetID, "--", NofetStore, ElinorID);
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.7
    @Test
    public void RemoveManagerNotSubscriber() {
        Response r1 = tradingSystem.RemoveManager(-1, NconnID, NofetStore, ElinorID);
        System.out.println(r1.getMessage());
        assertTrue(r1.getIsErr());
        Response r2 = tradingSystem.RemoveManager(NofetID, NconnID, NofetStore, 20);
        System.out.println(r2.getMessage());
        assertTrue(r2.getIsErr());
    }

    // requirement 4.7
    @Test
    public void RemoveManagerIsNotTheOwner() {
        String gust = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust, "Deme2", "123");
        Response res= tradingSystem.Login(gust, "Deme2", "123");
        Response res3 = tradingSystem.RemoveManager(res.returnUserID(), res.returnConnID(), ElinorStore, ElinorID);
        System.out.println(res3.getMessage());
        assertTrue(res3.getIsErr());
    }

    // requirement 4.7
    @Test
    public void RemoveManagerIsNotManager() {
        String gust = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust, "A", "123");
        Response res= tradingSystem.Login(gust, "A", "123");
        Response r = tradingSystem.RemoveManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    // requirement 4.7
    @Test
    public void RemoveManagerIsNotTheAppointment() {
        String gust = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(gust, "Deme3", "123");
        Response res = tradingSystem.Login(gust, "Deme3", "123");
        tradingSystem.AddNewManager(ElinorID, EconnID, ElinorStore, res.returnUserID());
        tradingSystem.AddNewOwner(ElinorID, EconnID, ElinorStore, NofetID);
        Response res3 = tradingSystem.RemoveManager(NofetID, NconnID, ElinorStore, res.returnUserID());
        System.out.println(res3.getMessage());
        assertTrue(res3.getIsErr());
    }

    // requirement 4.7
    @Test
    public void removeManagerByOwnerSuccess() {
        Response response = tradingSystem.RemoveOwnerByOwner(userID,connID,ElinorID,storeid);
        boolean exist = tradingSystem.stores.get(storeid).OwnersID().contains(ElinorID);
        assertTrue(!exist && !response.getIsErr());
    }

    // requirement 4.7
    @Test
    public void removeManagerNotByOwner() {
        tradingSystem.AddNewOwner(userID, connID, storeid, ElinorID);
        Response response = tradingSystem.RemoveOwnerByOwner(userID1, connID1, ElinorID, storeid);
        boolean exist = tradingSystem.stores.get(storeid).OwnersID().contains(ElinorID);
        assertTrue(exist && response.getIsErr());
    }

    // requirement 4.9
    @Test
    public void ShowStoreWorkersSuccess(){
        tradingSystem.AddNewManager(ElinorID, EconnID, ElinorStore, userID);
        tradingSystem.AddNewOwner(ElinorID, EconnID, ElinorStore, NofetID);
        System.out.println(tradingSystem.ShowStoreWorkers(ElinorID,EconnID,ElinorStore));
        Assertions.assertFalse(tradingSystem.ShowStoreWorkers(ElinorID,EconnID,ElinorStore).getIsErr());
    }

    // requirement 4.9
    @Test
    public void ShowStoreWorkersNotPermission(){
        assertTrue(tradingSystem.ShowStoreWorkers(userID,EconnID,ElinorStore).getIsErr());
    }

    // requirement 4.9
    @Test
    public void ShowStoreWorkersStoreNotExist(){
        assertTrue(tradingSystem.ShowStoreWorkers(userID,EconnID,-1).getIsErr());
    }

    // requirement 4.9
    @Test
    public void ShowStoreWorkersEmpty(){
        Object res= tradingSystem.ShowStoreWorkers(ElinorID,EconnID,ElinorStore).getReturnObject().get("workers");
        Assertions.assertNull(res);
    }

    //endregion

    //region requirement 5

    @org.junit.jupiter.api.Test
    void ManagerOperationsSuccess() {
        tradingSystem.AddNewManager(ElinorID, EconnID, ElinorStore, NofetID);
        LinkedList<PermissionEnum.Permission> p=new LinkedList<>();
        p.add(PermissionEnum.Permission.AddProduct);
        tradingSystem.EditManagerPermissions(ElinorID,EconnID,ElinorStore,NofetID,p);
        Response response = tradingSystem.AddProductToStore(NofetID, NconnID, ElinorStore, "Watermelon", "Fruits", 20.0, 10);
        Assertions.assertFalse(response.getIsErr());

        Integer productID = tradingSystem.stores.get(ElinorStore).getProductID("Watermelon");
        assertTrue(productID != -1);
    }

    @org.junit.jupiter.api.Test
    void ManagerOperationsFailed() {
        tradingSystem.AddNewManager(ElinorID, EconnID, ElinorStore, NofetID);
        LinkedList<PermissionEnum.Permission> p=new LinkedList<>();
        p.add(PermissionEnum.Permission.AddProduct);
        tradingSystem.EditManagerPermissions(ElinorID,EconnID,ElinorStore,NofetID,p);
        Response response = tradingSystem.AddNewOwner(NofetID, NconnID, ElinorStore, userID);
        assertTrue(response.getIsErr());

        Integer size = tradingSystem.stores.get(ElinorStore).OwnersID().size();
        Assertions.assertEquals(size, 1);
    }

    //endregion

    //region purchase tests - requirement 2.9
    void setUpBeforePurchase(){
        tradingSystem.AddProductToStore(NofetID, NconnID, NofetStore, "computer", "Technology", 3000.0,20);
        tradingSystem.AddProductToStore(NofetID, NconnID, NofetStore, "Bag", "Beauty", 100.0,50);
        tradingSystem.AddProductToStore(NofetID, NconnID, NofetStore, "Bed", "Fun", 4500.0,30);
    }


    @Test
    public void HappyPurchase() {
        setUpBeforePurchase();
        QuantityLimitForStore exp = new QuantityLimitForStore(3, NofetStore);
        tradingSystem.addBuyingPolicy(NofetID, NconnID, NofetStore, exp);
        Integer productID1 = Nstore.getProductID("computer");
        Integer productID2 = Nstore.getProductID("Bag");
        Integer preQuantity = Nstore.getQuantity(productID1);
        tradingSystem.Logout(NconnID);
        tradingSystem.AddProductToCart(EconnID, NofetStore, productID1, 1);
        tradingSystem.AddProductToCart(EconnID, NofetStore, productID2, 1);
        Response response = tradingSystem.subscriberPurchase(ElinorID, EconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        Assertions.assertFalse(response.getIsErr());

        //check Inventory after happy purchase
        Integer newQuantity = Nstore.getQuantity(productID1);
        Assertions.assertEquals(preQuantity, newQuantity+1);
    }

    @Test
    public void SadPurchase_BuyingPolicy() {
        QuantityLimitForStore exp = new QuantityLimitForStore(2, ElinorStore);
        tradingSystem.addBuyingPolicy(ElinorID, EconnID, ElinorStore, exp);
        tradingSystem.AddProductToStore(ElinorID, EconnID, ElinorStore, "computer", "Technology", 3000.0,20);
        Integer productID1 = Estore.getProductID("computer");
        Integer preQuantity = Estore.getQuantity(productID1);
        Response response = tradingSystem.AddProductToCart(NconnID, ElinorStore, productID1, 5);
        //Response response = tradingSystem.subscriberPurchase(NofetID, NconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        assertTrue(response.getIsErr());

        //check Inventory after sad purchase
        Integer newQuantity = Estore.getQuantity(productID1);
        Assertions.assertEquals(preQuantity, newQuantity);
    }

    @Test
    public void SadPurchase_WrongPaymentDetails() {
        setUpBeforePurchase();
        Integer productID1 = Nstore.getProductID("computer");
        Integer preQuantity = Nstore.getQuantity(productID1);
        tradingSystem.Logout(NconnID);
        tradingSystem.AddProductToCart(EconnID, NofetStore, productID1, 1);
        Response response = tradingSystem.subscriberPurchase(ElinorID, EconnID, "123456789", "15","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        assertTrue(response.getIsErr());

        //check Inventory after sad purchase
        Integer newQuantity = Nstore.getQuantity(productID1);
        Assertions.assertEquals(preQuantity, newQuantity);
    }

    @Test
    public void SadPurchase_WrongSupplyDetails() {
        setUpBeforePurchase();
        Integer productID1 = Nstore.getProductID("computer");
        Integer preQuantity = Nstore.getQuantity(productID1);
        tradingSystem.Logout(NconnID);
        tradingSystem.AddProductToCart(EconnID, NofetStore, productID1, 1);
        Response response = tradingSystem.subscriberPurchase(ElinorID, EconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Eilat","Israel","8458527");
        assertTrue(response.getIsErr());

        //check Inventory after sad purchase
        Integer newQuantity = Nstore.getQuantity(productID1);
        Assertions.assertEquals(preQuantity, newQuantity);
    }

    @Test
    public void SadPurchase_EmptyCart() {
        Response response = tradingSystem.subscriberPurchase(userID, connID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        assertTrue(response.getIsErr());
    }

    @Test
    public void Sad_Purchase_Supply() {
        QuantityLimitForStore exp = new QuantityLimitForStore(100, NofetStore);
        tradingSystem.addBuyingPolicy(NofetID, NconnID, NofetStore, exp);
        Integer productID1 = Nstore.getProductID("computer");
        tradingSystem.AddProductToCart(EconnID, NofetStore, productID1, 10);

        //another user is buying this product
        tradingSystem.AddProductToCart(connID, NofetStore, productID1, 15);
        tradingSystem.subscriberPurchase(userID, connID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        //the previous user make the purchase
        Integer preQuantity = Nstore.getQuantity(productID1);
        Response response = tradingSystem.subscriberPurchase(ElinorID, EconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        assertTrue(response.getIsErr());

        //check Inventory after sad purchase
        Integer newQuantity = Nstore.getQuantity(productID1);
        Assertions.assertEquals(preQuantity, newQuantity);
    }

    //Parallel tests
    @Test
    public void PurchaseParallel_Happy_TwoBuyersProduct() {
        //Prepare
        tradingSystem.AddProductToStore(ElinorID, EconnID, ElinorStore, "computer", "Technology", 3000.0,2);
        Integer newProduct = tradingSystem.stores.get(ElinorStore).getProductID("computer");
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
        assertTrue(!isErrs[0] && !isErrs[1]);
    }

    @Test
    public void PurchaseParallel_HappyFailed_TwoBuyersLastProduct_10times() {
        for(int test_try = 1; test_try <= 10; test_try++) {
            //Prepare
            tradingSystem.AddProductToStore(ElinorID, EconnID, ElinorStore, "computer", "Technology", 3000.0,1);
            Integer newProduct = tradingSystem.stores.get(ElinorStore).getProductID("computer");
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
            assertTrue((isErrs[0] && !isErrs[1]) || (isErrs[1] && !isErrs[0]));
            tearDown();
            setUp();
        }

    }
    //endregion

    //region Load and Timer tests
/*
    // requirement 2.3
    @Test
    //TODO
    public void register_100_Clients(){
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
    public void purchase_100_Clients() {
        //Prepare
        tradingSystem.AddProductToStore(ElinorID, EconnID, ElinorStore, "computer", "Technology", 3000.0,200);
        Integer newProduct = tradingSystem.stores.get(ElinorStore).getProductID("computer");
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
        Integer newQuantity = tradingSystem.stores.get(ElinorStore).getQuantity(newProduct);
        Assertions.assertTrue(newQuantity == 100);
    }

    @Test
    public void Subscribers_AND_Stores() {
        for(int i=0; i<100; i++){
            String newConnID = tradingSystem.ConnectSystem().returnConnID();
            Response res1 = tradingSystem.Register(newConnID, "Client-"+i,"123");
            Assertions.assertFalse(res1.getIsErr());
            newConnID = res1.returnConnID();
            Response res2 = tradingSystem.Login(newConnID, "Client-"+i, "123");
            Assertions.assertFalse(res2.getIsErr());
        }

        for(int i=0; i<100; i++){
            Integer newUserID = tradingSystem.getUserID("Client-"+i);
            String newConnID = tradingSystem.getUserConnID(newUserID);
            Response res = tradingSystem.AddStore(newUserID, newConnID, "It's A Test Num-"+i);
            Assertions.assertFalse(res.getIsErr());
        }

    }

    @Test
    public void TimerTest() {
    }
*/
    //endregion



    //region requirement 4.12, 6.6
    @Test
    public void HappyDailyIncomeForStore(){
        Response res= tradingSystem.getDailyIncomeForStore(NofetID,NofetStore,NconnID);
        Double DailyIncome=(Double) res.getReturnObject().get("DailyIncome");
        Assertions.assertEquals(DailyIncome, 50.0);
    }

    @Test
    public void HappyDailyIncomeForStore_NotExistPurchase(){
        Store New=new Store("New",NofetID);
        tradingSystem.stores.put(New.getId(),New);
        tradingSystem.subscribers.get(NofetID).AddStore(New.getId());
        Response res= tradingSystem.getDailyIncomeForStore(NofetID,New.getId(),NconnID);
        Double DailyIncome=(Double) res.getReturnObject().get("DailyIncome");
        Assertions.assertEquals(DailyIncome, 0);
    }

    @Test
    public void SadDailyIncomeForStore_UserNotOwnerOfTheStore(){
        Store New=new Store("New",NofetID);
        tradingSystem.stores.put(New.getId(),New);
        tradingSystem.subscribers.get(NofetID).AddStore(New.getId());
        Response res= tradingSystem.getDailyIncomeForStore(ElinorID,New.getId(),EconnID);
        Assertions.assertEquals(res.getMessage(),"getDailyIncomeForStore: The user " + ElinorID + " is not the owner of the store");
    }

    @Test
    public void SadDailyIncomeForStore_StoreNotExistInTheSystem(){
        Response res= tradingSystem.getDailyIncomeForStore(ElinorID,100,EconnID);
        Assertions.assertEquals(res.getMessage(),"getDailyIncomeForStore: The store " + 100 + " doesn't exist in the system");
    }

    @Test
    public void HappyDailyIncomeForSystem(){
        String tmpConn= tradingSystem.ConnectSystem().returnConnID();
        Response r1= tradingSystem.Login(tmpConn,"amit","qweasd");

        Store tmp= tradingSystem.stores.get(storeid);
        Product p1=new Product(NofetStore,"NofetStore",1,"1","1",2.0);
        Product p2=new Product(NofetStore,"NofetStore",2,"2","2",4.0);
        Product p3=new Product(NofetStore,"NofetStore",3,"3","3",13.0);
        Product p4=new Product(NofetStore,"NofetStore",4,"4","4",21.0);

        LinkedList<Product> PSH1=new LinkedList<>();
        PSH1.add(p1);
        PSH1.add(p2);
        PSH1.add(p3);

        ShoppingBag SB1=new ShoppingBag(1,NofetStore);
        SB1.setFinalPrice(20.0);

        ShoppingHistory SH1=new ShoppingHistory(SB1,PSH1);
        tmp.addHistory(SH1);

        Response r2= tradingSystem.getDailyIncomeForSystem(r1.returnUserID(),r1.returnConnID());
        Double DailyIncome=(Double) r2.getReturnObject().get("DailyIncome");
        Assertions.assertEquals(DailyIncome, 70.0);
    }

    @Test
    public void SadDailyIncomeForSystem_UserNotTheSystemManager(){
        Response r= tradingSystem.getDailyIncomeForSystem(NofetID,NconnID);
        Assertions.assertEquals(r.getMessage(),"getDailyIncomeForSystem: The user "+NofetID+"  try to see the Daily Income for the system but he is not the admin of the system");
    }
    //endregion

    //region requirement 3.8
    // Subscriber Bidding
    @Test
    public void HappysubscriberBidding() {
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        tradingSystem.AddProductToCart(NconnID,NofetStore,1,3);

        Response r8=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        Assertions.assertFalse(r8.getIsErr());
        System.out.println(r8.getMessage());
    }

    @Test
    public void SadsubscriberBidding_unsubscribe() {
        Response r1=tradingSystem.subscriberBidding(-1,"",1,1,1,1);
        assertTrue(r1.getIsErr());
        System.out.println(r1.getMessage());
    }

    @Test
    public void SadsubscriberBidding_storeNotExist() {
        Response r2=tradingSystem.subscriberBidding(NofetID,NconnID,-1,1,1,1);
        Assertions.assertTrue(r2.getIsErr());
        System.out.println(r2.getMessage());
    }

    @Test
    public void SadsubscriberBidding_productNotExist() {
        Response r3=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,-1,1,1);
        Assertions.assertTrue(r3.getIsErr());
        System.out.println(r3.getMessage());
    }

    @Test
    public void SadsubscriberBidding_productInTheCartAlready() {
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        tradingSystem.AddProductToCart(NconnID,NofetStore,1,3);
        Response r4=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,1,1,1);
        Assertions.assertTrue(r4.getIsErr());
        System.out.println(r4.getMessage());
    }

    @Test
    public void SadsubscriberBidding_priceNotInRange() {
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);

        Response r5=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,-1,1);
        assertTrue(r5.getIsErr());
        System.out.println(r5.getMessage());

        Response r6=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,70,1);
        assertTrue(r6.getIsErr());
        System.out.println(r6.getMessage());

    }

    @Test
    public void SadsubscriberBidding_NegativeQuantity() {
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        tradingSystem.AddProductToCart(NconnID,NofetStore,1,3);

        Response r7=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,3,-1);
        assertTrue(r7.getIsErr());
        System.out.println(r7.getMessage());
    }

    @Test
    public void SadsubscriberBidding_BidAlreadyExist() {
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        tradingSystem.AddProductToCart(NconnID,NofetStore,1,3);

        Response r8=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        Assertions.assertFalse(r8.getIsErr());
        Response r9=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        assertTrue(r9.getIsErr());
        System.out.println(r8.getMessage());

    }

    //endregion..1

    //region requirement 3.8
    //Response to subscriber bidding
    @Test
    public void HappyResponseToSubscriberBidding() {
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        tradingSystem.AddProductToCart(NconnID,NofetStore,1,3);

        Response r=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,2,2);
        Response r8=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,2,NofetID,2, 2);
        Assertions.assertFalse(r8.getIsErr());
    }

    @Test
    public void SadUnsubscribe() {
        Response r0=tradingSystem.ResponseForSubmissionBidding(-1,"",1,1,1,1,1, 2);
        Assertions.assertTrue(r0.getIsErr());
        System.out.println(r0.getMessage());

        Response r1=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,1,1,1,1,1, 2);
        Assertions.assertTrue(r1.getIsErr());
        System.out.println(r1.getMessage());
    }

    @Test
    public void SadStoreNotExist() {
        Response r2=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,-1,-1,1,ElinorID,1, 2);
        Assertions.assertTrue(r2.getIsErr());
        System.out.println(r2.getMessage());
    }

    @Test
    public void SadProductNotExist() {
        Response r3=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,-1,1,ElinorID,1, 2);
        Assertions.assertTrue(r3.getIsErr());
        System.out.println(r3.getMessage());
    }

    @Test
    public void SadBideHadResponseAlready() {
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        tradingSystem.AddProductToCart(NconnID,NofetStore,1,3);

        Response r=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,2,2);
        Response r8=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,2,NofetID,2, 2);
        Assertions.assertFalse(r8.getIsErr());
        Response r9=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,3,NofetID,1, 2);
        assertTrue(r9.getIsErr());
        System.out.println(r8.getMessage());

    }

    @Test
    public void SadPriceNotInRange() {
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        tradingSystem.AddProductToCart(NconnID,NofetStore,1,3);

        Response r5=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,-1,ElinorID,1, 2);
        assertTrue(r5.getIsErr());
        System.out.println(r5.getMessage());

        Response r6=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,70,ElinorID,1, 2);
        assertTrue(r6.getIsErr());
        System.out.println(r6.getMessage());
    }

    @Test
    public void SadNegativeQuantity() {
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        tradingSystem.AddProductToCart(NconnID,NofetStore,1,3);

        Response r7=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,3,ElinorID,-1, 2);
        assertTrue(r7.getIsErr());
        System.out.println(r7.getMessage());

    }

    @Test
    public void showBids() {
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        Response r0=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,1,3,2);
        Response r1= tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        Response r=tradingSystem.ShowBids(NofetID,NconnID,NofetStore);
        Assertions.assertFalse(r.getIsErr());
    }

    //endregion.2

    //region requirement 3.8
    // Add special product

    @Test
    public void HappyAddSpecialProduct() {
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);

        Response r8=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        Assertions.assertFalse(r8.getIsErr());
        System.out.println(r8.getMessage());
    }

    @Test
    public void SadProductExistInCart() {
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        Response r1=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,1,1);
        tradingSystem.AddProductToCart(NconnID,NofetStore,2,3);
        Response r2=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,3,NofetID,3, 2);
        System.out.println(r1.getMessage());
        System.out.println(r2.getMessage());
        Assertions.assertFalse(r1.getIsErr());
        assertTrue(r2.getIsErr());
    }

    @Test
    public void SadProductsNotInStock() {
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        Response r1=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        Response r2=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,3,NofetID,30, 2);
        System.out.println(r1.getMessage());
        System.out.println(r2.getMessage());
        Assertions.assertFalse(r1.getIsErr());
        assertTrue(r2.getIsErr());
    }

    @Test
    public void SadAddSpecialProduct_productNotExist() {
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        tradingSystem.RemoveProduct(NofetID,NofetStore,2,NconnID);
        Response r8=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        System.out.println(r8.getMessage());
        assertTrue(r8.getIsErr());
    }

    @Test
    public void SadProductNotInStore() {
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"2","1",7,20);
        tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        tradingSystem.RemoveProduct(NofetID,NofetStore,2,NconnID);
        Response r=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,3,NofetID,30, 2);
        System.out.println(r.getMessage());
        assertTrue(r.getIsErr());
    }

    @Test
    public void SadProductAgainstThePolicy() {
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"1","1",10,20);
        Store s= tradingSystem.stores.get(NofetStore);
        Integer productID1 =s.getProductID("1");
        QuantityLimitForProduct exp1 = new QuantityLimitForProduct(5, productID1);
        OrComposite or = new OrComposite();
        or.add(exp1);
        BuyingPolicy b=new BuyingPolicy(s.getId(),or);
        tradingSystem.stores.get(NofetStore).setBuyingPolicy(b);
        Response r1=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,productID1,3,2);
        Response r2=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,productID1,3,NofetID,6, 2);
        System.out.println(r1.getMessage());
        System.out.println(r2.getMessage());
        Assertions.assertFalse(r1.getIsErr());
        assertTrue(r2.getIsErr());
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
