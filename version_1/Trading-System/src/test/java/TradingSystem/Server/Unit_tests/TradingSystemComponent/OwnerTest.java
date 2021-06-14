package TradingSystem.Server.Unit_tests.TradingSystemComponent;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.DiscountPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimitForProduct;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.Task.AddManagerTaskUnitTests;
import TradingSystem.Server.DomainLayer.Task.PurchaseTaskUnitTests;
import TradingSystem.Server.DomainLayer.Task.RemoveProductTaskUnitTests;
import TradingSystem.Server.DomainLayer.Task.ResultUnitTests;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.DomainLayer.UserComponent.PermissionEnum;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(args = {"src/main/resources/initialization_System.json","src/main/resources/External_State.json"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class OwnerTest {

    @Autowired
    TradingSystemImpl tradingSystem;

    String EconnID;
    String NconnID;
    int ElinorID;
    int NofetID;
    int NofetStore;
    int ElinorStore;
    Store Nstore;
    Store Estore;
    String connID;
    int userID;
    int storeid;

    int productId;
    int productId2;

    @Before
    public void setUp(){
        connID= tradingSystem.ConnectSystem().returnConnID();
        Response response= tradingSystem.Register(connID,"reutlevy","8119");
        userID= response.returnUserID();
        connID= tradingSystem.Login(connID,"reutlevy","8119").returnConnID();

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
        NofetStore = tradingSystem.getStoreIDByName("NofetStore");
        ElinorStore = tradingSystem.getStoreIDByName("ElinorStore");
        Estore = tradingSystem.stores.get(ElinorStore);
        Nstore = tradingSystem.stores.get(NofetStore);
    }

    @After
    public void tearDown(){
        tradingSystem.ClearSystem();
    }

    // requirement 4.1
    //TODO check
//    @Test
//    public void AddProductSuccess(){
//        Response response= tradingSystem.AddProductToStore(userID,connID,storeid,"prod8","food",11.0,9);
//        Assertions.assertFalse(response.getIsErr());
//    }

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
        Response response= tradingSystem.ChangeQuantityProduct(ElinorID,EconnID,storeid,productId,10);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    public void ChangeProductWrongQuantity(){
        Response response= tradingSystem.ChangeQuantityProduct(userID,connID,storeid,productId,-10);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    //TODO check
//    @Test
//    public void EditProductSuccess(){
//        Response response= tradingSystem.EditProduct(userID,connID,storeid,productId,"prod3","food",12.0,9);
//        Assertions.assertFalse(response.getIsErr());
//    }

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
    //TODO check
//    @Test
//    public void RemoveProductSuccess(){
//        Response response= tradingSystem.RemoveProduct(userID,storeid,productId2,connID);
//        Assertions.assertFalse(response.getIsErr());
//    }

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
    //TODO check
//    @Test
//    public void removeProductFromStoreWhileOtherClientBuyingItTest_Parallel() {
//        List<boolean[]> isErrsTotal = new ArrayList<>();
//        for (int test_i = 0; test_i < 10; test_i++) {
//            //Prepare
//            tradingSystem.AddProductToStore(ElinorID, EconnID, ElinorStore, "Sneakers", "Shoes", 150.0, 25);
//            Integer newProduct = tradingSystem.stores.get(ElinorStore).getProductID("Sneakers");
//            //Create two clients with task to buy this product
//            ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(2);
//
//            //Prepare tasks for clients
//            List<Callable<ResultUnitTests>> taskList = new ArrayList<>();
//            Callable<ResultUnitTests> purchaseTask = new PurchaseTaskUnitTests("guestBuyer", ElinorStore, newProduct, 25, "123456789", "4", "2022" ,"123" ,"123456789" ,"Rager 101","Beer Sheva","Israel","8458527");
//            taskList.add(purchaseTask);
//            Callable<ResultUnitTests> removeTask = new RemoveProductTaskUnitTests("Client-StoreOwner",ElinorStore, newProduct);
//            taskList.add(removeTask);
//
//            //Execute all tasks and get reference to Future objects
//            List<Future<ResultUnitTests>> resultList = null;
//
//            try {
//                resultList = executor.invokeAll(taskList);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            executor.shutdown();
//
//            System.out.println("\n========Printing the results======");
//            boolean[] isErrs = new boolean[2];
//            for (int i = 0; i < resultList.size(); i++) {
//                Future<ResultUnitTests> future = resultList.get(i);
//                try {
//                    ResultUnitTests result = future.get();
//                    Response response = result.getResponse();
//                    System.out.println("Assert correctnes for " + result.getName() + ": response -> " + response + " ::" + result.getTimestamp());
//                    isErrs[i] = response.getIsErr();
//                } catch (InterruptedException | ExecutionException e) {
//                    e.printStackTrace();
//                }
//            }
//            //Check that one of the client failed and the other succeed.
//            //assertTrue((isErrs[0] && !isErrs[1]) || (isErrs[1] && !isErrs[0]));
//            isErrsTotal.add(isErrs);
//            tearDown();
//        }
//        boolean ans = false;
//        for(boolean[] errArr : isErrsTotal) {
//            if ((errArr[0] && !errArr[1]) || (errArr[1] && !errArr[0])) {
//                ans = true;
//                break;
//            }
//        }
//        assertTrue(ans);
//        System.out.println("========Printing the results - TOTAL PARALLEL ======");
//        for(int i=0; i<isErrsTotal.size(); i++) {
//            System.out.printf("%d: purchase: %s remove: %s\n", i, isErrsTotal.get(i)[0], isErrsTotal.get(i)[1]);
//        }
//    }

    // requirement 4.2
    //TODO check
//    @Test
//    public void HappyInfoPolicies() {
//        QuantityLimitForProduct exp = new QuantityLimitForProduct(1, productId);
//        tradingSystem.addBuyingPolicy(userID, connID, storeid, exp);
//        Response res = tradingSystem.GetPoliciesInfo(userID, storeid, connID);
//        Assertions.assertFalse(res.getIsErr());
//    }

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

    //TODO check
    // requirement 4.4
//    @Test
//    public void removeOwnerNotAppointment() {
//        tradingSystem.AddNewOwner(NofetID, NconnID, NofetStore, ElinorID);
//        tradingSystem.AddNewOwner(NofetID, NconnID, NofetStore, userID);
//        Response response = tradingSystem.RemoveOwnerByOwner(userID, connID, ElinorID, NofetStore);
//        assertTrue(response.getIsErr());
//
//        Integer size = tradingSystem.stores.get(NofetStore).OwnersID().size();
//        Assertions.assertEquals(size, 4);
//    }

    // requirement 4.4
    //TODO check
//    @Test
//    public void removeOwnerNotOwner1() {
//        setUp();
//        tradingSystem.AddNewOwner(NofetID, NconnID, NofetStore, ElinorID);
//        Response response = tradingSystem.RemoveOwnerByOwner(userID, connID, ElinorID, NofetStore);
//        assertTrue(response.getIsErr());
//
//        Integer size = tradingSystem.stores.get(NofetStore).OwnersID().size();
//        Assertions.assertEquals(size, 2);
//    }
//
//    // requirement 4.4
//    @Test
//    public void removeOwnerNotOwner2() {
//        Response response = tradingSystem.RemoveOwnerByOwner(NofetID, NconnID, userID, NofetStore);
//        assertTrue(response.getIsErr());
//
//        Integer size = tradingSystem.stores.get(NofetStore).OwnersID().size();
//        Assertions.assertEquals(size, 2);
//    }

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

        //appoint Elinor to owner
        tradingSystem.AddNewOwner(ElinorID, EconnID, ElinorStore, NofetID);
        Integer size = tradingSystem.stores.get(ElinorStore).OwnersID().size();
        assertTrue(size == 2);

        //Create two clients with task to add manager
        ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(2);

        //Prepare tasks for clients
        List<Callable<ResultUnitTests>> taskList = new ArrayList<>();
        Callable<ResultUnitTests> addManagerTask_1 = new AddManagerTaskUnitTests("Elinor", "123", ElinorStore, userID, ElinorID, EconnID);
        taskList.add(addManagerTask_1);
        Callable<ResultUnitTests> addManagerTask_2 = new AddManagerTaskUnitTests("Elinor", "123", ElinorStore, userID, NofetID, NconnID);
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
    //TODO check
//    @Test
//    public void removeManagerByOwnerSuccess() {
//        Response response = tradingSystem.RemoveOwnerByOwner(userID,connID,ElinorID,storeid);
//        boolean exist = tradingSystem.stores.get(storeid).OwnersID().contains(ElinorID);
//        assertTrue(!exist && !response.getIsErr());
//    }

    // requirement 4.7
    //TODO check
//    @Test
//    public void removeManagerNotByOwner() {
//        tradingSystem.AddNewOwner(userID, connID, storeid, ElinorID);
//        Response response = tradingSystem.RemoveOwnerByOwner(NofetID, NconnID, ElinorID, storeid);
//        boolean exist = tradingSystem.stores.get(storeid).OwnersID().contains(ElinorID);
//        assertTrue(exist && response.getIsErr());
//    }

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
    //TODO check
//    @Test
//    public void ShowStoreWorkersEmpty(){
//        Object res= tradingSystem.ShowStoreWorkers(ElinorID,EconnID,ElinorStore).getReturnObject().get("workers");
//        Assertions.assertNull(res);
//    }
}
