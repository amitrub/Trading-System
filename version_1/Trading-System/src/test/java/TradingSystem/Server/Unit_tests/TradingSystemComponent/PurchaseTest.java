package TradingSystem.Server.Unit_tests.TradingSystemComponent;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimitForStore;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.Task.PurchaseTaskUnitTests;
import TradingSystem.Server.DomainLayer.Task.ResultUnitTests;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PurchaseTest {

    @Autowired
    TradingSystemImplRubin tradingSystem;

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

    public void tearDown(){
        tradingSystem.ClearSystem();
    }

    //requirement 2.9

    void setUpBeforePurchase(){
        tradingSystem.AddProductToStore(NofetID, NconnID, NofetStore, "computer", "Technology", 3000.0,20);
        tradingSystem.AddProductToStore(NofetID, NconnID, NofetStore, "Bag", "Beauty", 100.0,50);
        tradingSystem.AddProductToStore(NofetID, NconnID, NofetStore, "Bed", "Fun", 4500.0,30);
    }

    @Test
    public void SadPurchase_EmptyCart() {
        Response response = tradingSystem.subscriberPurchase(ElinorID, EconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        assertTrue(response.getIsErr());
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
    public void Sad_Purchase_Supply() {
        QuantityLimitForStore exp = new QuantityLimitForStore(100, NofetStore);
        tradingSystem.addBuyingPolicy(NofetID, NconnID, NofetStore, exp);
        Integer productID1 = Nstore.getProductID("computer");
        tradingSystem.AddProductToCart(EconnID, NofetStore, productID1, 10);

        //another user is buying this product
        tradingSystem.AddProductToCart(NconnID, NofetStore, productID1, 15);
        tradingSystem.subscriberPurchase(NofetID, NconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

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
}
