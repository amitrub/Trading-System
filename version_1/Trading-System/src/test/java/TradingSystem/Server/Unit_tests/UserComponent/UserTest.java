package TradingSystem.Server.Unit_tests.UserComponent;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimitForStore;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.Task.PurchaseTaskUnitTests;
import TradingSystem.Server.DomainLayer.Task.ResultUnitTests;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import TradingSystem.Server.TradingSystemApplication;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(args = {"src/main/resources/initialization_System.json","src/main/resources/External_State.json"})
public class UserTest {

    @Autowired
    TradingSystemImpl tradingSystem;

    String NconnID;
    int NofetID;
    int NofetStore;
    Store Nstore;

    User Elinor;
    String EconnID;
    int ElinorID;
    int ElinorStore;
    Store Estore;

    @Before
    public void setUp(){
        NconnID = tradingSystem.ConnectSystem().returnConnID();
        Response r1= tradingSystem.Register(NconnID, "nofet", "123");
        NofetID= r1.returnUserID();
        NconnID= tradingSystem.Login(NconnID, "nofet", "123").returnConnID();

        tradingSystem.AddStore(NofetID,NconnID,"NofetStore");
        NofetStore = tradingSystem.getStoreIDByName("NofetStore");
        Nstore = tradingSystem.stores.get(NofetStore);
        tradingSystem.AddProductToStore(NofetID, NconnID, NofetStore, "computer", "Technology", 3000.0,20);
        tradingSystem.AddProductToStore(NofetID, NconnID, NofetStore, "Bag", "Beauty", 100.0,50);
        tradingSystem.AddProductToStore(NofetID, NconnID, NofetStore, "Bed", "Fun", 4500.0,30);

        EconnID = tradingSystem.ConnectSystem().returnConnID();
        Response r2= tradingSystem.Register(EconnID, "elinor", "123");
        ElinorID= r2.returnUserID();
        EconnID= tradingSystem.Login(EconnID, "elinor", "123").returnConnID();
        Elinor = tradingSystem.subscribers.get(ElinorID);
        tradingSystem.AddStore(ElinorID,EconnID,"ElinorStore");
        ElinorStore = tradingSystem.getStoreIDByName("ElinorStore");
        Estore = tradingSystem.stores.get(ElinorStore);
    }

    @After
    public void tearDown(){
        tradingSystem.ClearSystem();
    }

    //region user history tests
    // requirement 3.7
    @Test
    public void UserHistorySuccess() {
        //prepare
        Integer productID1 = Nstore.getProductID("computer");
        Integer productID2 = Nstore.getProductID("Bag");
        tradingSystem.AddProductToCart(EconnID, NofetStore, productID1, 1);
        tradingSystem.subscriberPurchase(ElinorID, EconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        tradingSystem.AddProductToCart(EconnID, NofetStore, productID2, 1);
        tradingSystem.subscriberPurchase(ElinorID, EconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        //action
        List<DummyShoppingHistory> list = Elinor.ShowUserHistory();
        assertEquals(list.size(), 2);

    }

    // requirement 3.7
    @Test
    public void UserHistoryFailed() {
        List<DummyShoppingHistory> list = Elinor.ShowUserHistory();
        assertEquals(list.size(), 0);
    }

    //endregion

    //region add to cart tests
    @Test
    public void AddToCartHappy() {
        QuantityLimitForStore exp = new QuantityLimitForStore(10, ElinorStore);
        tradingSystem.addBuyingPolicy(ElinorID, EconnID, ElinorStore, exp);
        tradingSystem.AddProductToStore(ElinorID, EconnID, ElinorStore, "computer", "Technology", 3000.0,20);
        Integer productID1 = Estore.getProductID("computer");
        Integer preQuantity = Estore.getQuantity(productID1);
        Response response = Elinor.AddProductToCart(NofetStore,productID1, 5);
        assertTrue(response.getIsErr());

        //check Inventory after happy add
        Integer newQuantity = Estore.getQuantity(productID1);
        Assertions.assertEquals(preQuantity, newQuantity);
    }

    @Test
    public void AddToCartSad_BuyingPolicy() {
        QuantityLimitForStore exp = new QuantityLimitForStore(2, ElinorStore);
        tradingSystem.addBuyingPolicy(ElinorID, EconnID, ElinorStore, exp);
        tradingSystem.AddProductToStore(ElinorID, EconnID, ElinorStore, "computer", "Technology", 3000.0,20);
        Integer productID1 = Estore.getProductID("computer");
        Integer preQuantity = Estore.getQuantity(productID1);
        Response response = Elinor.AddProductToCart( NofetStore,productID1, 5);
        assertTrue(response.getIsErr());
    }

    //endregion

    //region purchase tests
    @Test
    public void SadPurchase_EmptyCart() {
        Response response = Elinor.subscriberPurchase( "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        assertTrue(response.getIsErr());
    }

    @Test
    public void HappyPurchase() {
        QuantityLimitForStore exp = new QuantityLimitForStore(3, NofetStore);
        tradingSystem.addBuyingPolicy(NofetID, NconnID, NofetStore, exp);
        Integer productID1 = Nstore.getProductID("computer");
        Integer productID2 = Nstore.getProductID("Bag");
        Integer productID3 = Nstore.getProductID("Bed");
        Integer preQuantity = Nstore.getQuantity(productID1);
        tradingSystem.AddProductToCart(EconnID, NofetStore, productID1, 1);
        tradingSystem.AddProductToCart(EconnID, NofetStore, productID2, 1);
        Response rr=tradingSystem.subscriberBidding(ElinorID,EconnID,NofetStore,productID3,2700,1);
        Response ww=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,productID3,2700,ElinorID,1,1);

        Response response = Elinor.subscriberPurchase( "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        Assertions.assertFalse(response.getIsErr());

        //check Inventory after happy purchase
        Integer newQuantity = Nstore.getQuantity(productID1);
        Assertions.assertEquals(preQuantity, newQuantity+1);
    }

    @Test
    public void SadPurchase_WrongPaymentDetails() {
        Integer productID1 = Nstore.getProductID("computer");
        Integer preQuantity = Nstore.getQuantity(productID1);
        tradingSystem.AddProductToCart(EconnID, NofetStore, productID1, 1);
        Response response = Elinor.subscriberPurchase("123456789", "15","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        assertTrue(response.getIsErr());

        //check Inventory after sad purchase
        Integer newQuantity = Nstore.getQuantity(productID1);
        Assertions.assertEquals(preQuantity, newQuantity);
    }

    @Test
    public void SadPurchase_WrongSupplyDetails() {
        Integer productID1 = Nstore.getProductID("computer");
        Integer preQuantity = Nstore.getQuantity(productID1);
        tradingSystem.AddProductToCart(EconnID, NofetStore, productID1, 1);
        Response response = Elinor.subscriberPurchase("123456789", "4","2022" , "123", "123456789", "Rager 101","Eilat","Israel","8458527");
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
        User Nofet = tradingSystem.subscribers.get(NofetID);
        Nofet.subscriberPurchase("123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        //the previous user make the purchase
        Integer preQuantity = Nstore.getQuantity(productID1);
        Response response = Elinor.subscriberPurchase("123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
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
    //TODO - Not Work!
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
}
