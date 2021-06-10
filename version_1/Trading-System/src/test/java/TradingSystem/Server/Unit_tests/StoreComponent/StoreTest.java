package TradingSystem.Server.Unit_tests.StoreComponent;

import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingBag;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Bid;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.OrComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimitForProduct;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.Task.PurchaseTaskUnitTests;
import TradingSystem.Server.DomainLayer.Task.RemoveProductTaskUnitTests;
import TradingSystem.Server.DomainLayer.Task.ResultUnitTests;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import TradingSystem.Server.DomainLayer.UserComponent.ManagerPermission;
import TradingSystem.Server.DomainLayer.UserComponent.Permission;
import TradingSystem.Server.DomainLayer.UserComponent.PermissionEnum;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class StoreTest {

    @Autowired
    TradingSystemImplRubin tradingSystem;

    Store store;
    String EconnID;
    Integer EuserId;
    Integer storeID;
    String NconnID;
    int NofetID;

    @BeforeEach
    void setUp() {
        tradingSystem.ClearSystem();
        String guest1= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(guest1, "Elinor", "123");
        Response res= tradingSystem.Login(guest1, "Elinor", "123");
        EconnID= res.returnConnID();
        EuserId=res.returnUserID();

        tradingSystem.AddStore(EuserId, EconnID, "store1");
        storeID = tradingSystem.getStoreIDByName("store1");

        store = tradingSystem.stores.get(storeID);
        store.AddProductToStore( "computer", 3000.0, "Technology",5);
        store.AddProductToStore("Bag" ,100.0, "Beauty",5);
        store.AddProductToStore("IPed",  2500.0, "Technology", 5);
        store.AddProductToStore( "MakeUp",  40.0, "Beauty",5);
        store.AddProductToStore( "Ball",  60.0, "Fun",5);

        NconnID = tradingSystem.ConnectSystem().returnConnID();
        Response r1= tradingSystem.Register(NconnID, "nofet", "123");
        NofetID= r1.returnUserID();
        NconnID= tradingSystem.Login(NconnID, "nofet", "123").returnConnID();


    }

    @AfterEach
    void tearDown() {
        store = null;
    }



    //region requirement 2.6: search tests

    //requirement 2.6
    @Test
    void searchProduct() {
        List<DummyProduct> L1= store.SearchProduct("computer",null,-1,-1);
        assertTrue(L1.get(0).getProductName().equals("computer"));

        List<DummyProduct> L2= store.SearchProduct(null,"Technology",-1,-1);
        assertTrue(L2.get(0).getCategory().equals("Technology")&&L2.get(1).getCategory().equals("Technology"));

        List<DummyProduct> L3= store.SearchProduct(null,null,0,50);
        assertTrue(L3.get(0).getProductName().equals("MakeUp"));

        List<DummyProduct> L4= store.SearchProduct(null,"Fun",0,50);
        assertTrue(L4.isEmpty());
    }

    //endregion

    //region history tests
    @Test
    void showStoreHistorySuccess() {
        Integer productID1 = store.getProductID("computer");
        tradingSystem.AddProductToCart(EconnID,storeID, productID1,5);
        tradingSystem.subscriberPurchase(EuserId, EconnID, "123456","4","2022","123","123456","Rager","Beer Sheva","Israel","123");
        List<DummyShoppingHistory> list = store.ShowStoreHistory();
        assertEquals(list.size(), 1);
        assertTrue(list.get(0).getProducts().get(0).getProductName().equals("computer"));
    }

    @Test
    void showStoreEmpty() {
        List<DummyShoppingHistory> list =store.ShowStoreHistory();
        assertTrue(list.isEmpty());
    }
    //endregion

    //region requirement 3.3: Write Comment tests

    // requirement 3.3
    @Test
    public void WriteCommentSuccess() {
        Integer productID1 = store.getProductID("computer");
        tradingSystem.AddProductToCart(NconnID, storeID, productID1, 1);
        tradingSystem.subscriberPurchase(NofetID, NconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        Response response = tradingSystem.WriteComment(NofetID,NconnID, storeID, productID1, "Amazing");
        Assertions.assertFalse(response.getIsErr());
        Integer size = store.getProduct(productID1).getComments().size();
        Assertions.assertEquals(size, 1);
    }

    // requirement 3.3
    @Test
    public void WriteCommentWrongStoreID() {
        Integer productID1 = store.getProductID("computer");
        tradingSystem.AddProductToCart(NconnID, storeID, productID1, 1);
        tradingSystem.subscriberPurchase(NofetID, NconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        Response response = tradingSystem.WriteComment(NofetID,NconnID, 100, productID1, "Amazing");
        assertTrue(response.getIsErr());
        Integer size = store.getProduct(productID1).getComments().size();
        Assertions.assertEquals(size, 0);
    }

    // requirement 3.3
    @Test
    public void WriteCommentNotInHistory() {
        Integer productID1 = store.getProductID("computer");

        Response response = tradingSystem.WriteComment(NofetID,NconnID, storeID, productID1, "Amazing");
        assertTrue(response.getIsErr());
        Integer size = store.getProduct(productID1).getComments().size();
        Assertions.assertEquals(size, 0);

    }

    // requirement 3.3
    @Test
    public void WriteCommentExistComment() {
        Integer productID1 = store.getProductID("computer");
        tradingSystem.AddProductToCart(NconnID, storeID, productID1, 1);
        tradingSystem.subscriberPurchase(NofetID, NconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        tradingSystem.WriteComment(NofetID,NconnID, storeID, productID1, "Amazing");
        Response response = tradingSystem.WriteComment(NofetID,NconnID, storeID, productID1, "WTF");
        assertTrue(response.getIsErr());
        Integer size = store.getProduct(productID1).getComments().size();
        Assertions.assertEquals(size, 1);
    }

    //endregion

    //region requirement 4.12: Daily Income tests

    //requirement 4.12
    @Test
    public void HappyDailyIncomeForStore(){
        Integer productID1 = store.getProductID("computer");
        tradingSystem.AddProductToCart(NconnID, storeID, productID1, 3);
        tradingSystem.subscriberPurchase(NofetID, NconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        Double dailyIncome = store.getDailyIncome();
        assertEquals(dailyIncome, 9000.0);
    }

    //requirement 4.12
    @Test
    public void SadDailyIncomeForStore_NotExistPurchase(){
        Double dailyIncome= store.getDailyIncome();
        assertEquals(dailyIncome, 0.0);
    }

    //endregion

    //region requirement 8.3: Bidding
    @Test
    void checkBidAddedAfterSubscribeBidding(){
        tradingSystem.subscriberBidding(NofetID,NconnID,storeID,1,2950,1);
        boolean added=false;
        for (Bid b:store.getBids()
        ) {
            if(b.getProductID()==1&&b.getUserID()==NofetID&&b.getPrice()==2950&&b.getQuantity()==1){
                added=true;
            }
        }
        assertTrue(added);
    }

    //todo how to implement
    @Test
    void checkManagerWithPermissionGetAlert(){
        LinkedList<PermissionEnum.Permission> list=new LinkedList<>();
        list.add(PermissionEnum.Permission.RequestBidding);
        tradingSystem.AddNewManager(EuserId,EconnID,storeID,NofetID);
        tradingSystem.EditManagerPermissions(EuserId,EconnID,storeID,NofetID,list);
        Response Alert=new Response("Alert");
        store.sendAlertOfBiddingToManager(Alert);
        //??
    }

    @Test
    void checkTheListOfTheOwnerAndManagerInTheBidUpdateRunTime() {

        String guestMan1= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(guestMan1, "Man1", "123");
        Response resMan1= tradingSystem.Login(guestMan1, "Man1", "123");
        tradingSystem.AddNewManager(EuserId,EconnID,storeID,resMan1.returnUserID());
        LinkedList<PermissionEnum.Permission> list=new LinkedList<>();
        list.add(PermissionEnum.Permission.RequestBidding);
        tradingSystem.EditManagerPermissions(EuserId,EconnID,storeID,resMan1.returnUserID(),list);
        Response r=tradingSystem.GetPossiblePermissionsToManager(resMan1.returnUserID(), resMan1.returnConnID(), storeID);
        String guestMan2= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(guestMan2, "Man2", "123");
        Response resMan2= tradingSystem.Login(guestMan2, "Man2", "123");
        tradingSystem.AddNewManager(EuserId,EconnID,storeID,resMan2.returnUserID());

        tradingSystem.subscriberBidding(NofetID,NconnID,storeID,3,20,2);

        Response r1=store.approveSubmissionBid(resMan1.returnUserID(),NofetID,3);
        tradingSystem.EditManagerPermissions(EuserId,EconnID,storeID,resMan2.returnUserID(),list);
        Response r2=store.approveSubmissionBid(EuserId,NofetID,3);
        assertFalse(store.getBid(NofetID,3).isFinalState());
    }

    @Test
    void checkTheListOfTheOwnerAndManagerInTheBidUpdateAfterNewBidFromOwner() {

        String guestMan1 = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(guestMan1, "Man1", "123");
        Response resMan1 = tradingSystem.Login(guestMan1, "Man1", "123");
        tradingSystem.AddNewManager(EuserId, EconnID, storeID, resMan1.returnUserID());

        LinkedList<PermissionEnum.Permission> list = new LinkedList<>();
        list.add(PermissionEnum.Permission.RequestBidding);
        tradingSystem.EditManagerPermissions(EuserId, EconnID, storeID, resMan1.returnUserID(), list);

        tradingSystem.subscriberBidding(NofetID, NconnID, storeID, 3, 20, 2);

        Response r1 = store.approveSubmissionBid(resMan1.returnUserID(), NofetID, 3);
        Response r2 = store.changeSubmissionBid(EuserId, NofetID, 3, 30, 1);
        assertFalse(store.getBid(NofetID,3).isFinalState());
    }

    @Test
    void checkTheBidRemovedAfterOwnerRefusedTheBid() {
        tradingSystem.subscriberBidding(NofetID,NconnID,storeID,2,90,1);
        Bid b1=store.getBid(NofetID,2);
        store.refuseSubmissionBid(EuserId,NofetID,2);
        Bid b2=store.getBid(NofetID,2);
        assertTrue(b1!=null&&b2==null);
    }

    @Test
    void checkTheBidRemovedAfterOwnerApproveTheBid() {
        tradingSystem.subscriberBidding(NofetID,NconnID,storeID,2,90,1);
        Bid b1=store.getBid(NofetID,2);
        store.approveSubmissionBid(EuserId,NofetID,2);
        Bid b2=store.getBid(NofetID,2);
        assertTrue(b1!=null&&b2==null);
    }

    @Test
    void checkTheBidNotRemovedAfterOwnerChangeTheBid() {
        String guestMan1 = tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(guestMan1, "Man", "123");
        Response resMan1 = tradingSystem.Login(guestMan1, "Man", "123");
        tradingSystem.AddNewManager(EuserId, EconnID, storeID, resMan1.returnUserID());
        LinkedList<PermissionEnum.Permission> list = new LinkedList<>();
        list.add(PermissionEnum.Permission.RequestBidding);
        tradingSystem.EditManagerPermissions(EuserId, EconnID, storeID, resMan1.returnUserID(), list);

        tradingSystem.subscriberBidding(NofetID,NconnID,storeID,2,80,1);
        Bid b1=store.getBid(NofetID,2);
        store.changeSubmissionBid(EuserId,NofetID,2,90,1);
        Bid b2=store.getBid(NofetID,2);
        assertTrue(b1!=null&&b2!=null);
    }

    @Test
    void checkTheBidChangedAfterOwnerChangeTheBid() {
        String guest= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(guest, "Manager1", "123");
        Response res= tradingSystem.Login(guest, "Manager1", "123");
        tradingSystem.AddNewManager(EuserId,EconnID,storeID,res.returnUserID());
        LinkedList<PermissionEnum.Permission> list = new LinkedList<>();
        list.add(PermissionEnum.Permission.RequestBidding);
        tradingSystem.EditManagerPermissions(EuserId, EconnID, storeID, res.returnUserID(), list);

        tradingSystem.subscriberBidding(NofetID,NconnID,storeID,3,2000,2);
        Bid b1=store.getBid(NofetID,3);
         int Id=b1.getUserID();
         int PId=b1.getProductID();
         int quantity=b1.getQuantity();
         int price=b1.getPrice();
        store.changeSubmissionBid(EuserId,NofetID,3,2459,1);
        Bid b2=store.getBid(NofetID,3);
        assertTrue(Id==NofetID&&PId==3 && quantity==2 && price==2000&&
                           b2.getUserID()==NofetID&&b2.getProductID()==3 && b2.getQuantity()==1 && b2.getPrice()==2459);
    }

    //todo check how get alert?
    @Test
    void checkTheManagerGetAlertAfterOwnerChangeTheBid() {
        String guest= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(guest, "Manager2", "123");
        Response res= tradingSystem.Login(guest, "Manager2", "123");
        tradingSystem.AddNewManager(EuserId,EconnID,storeID,res.returnUserID());
        tradingSystem.subscriberBidding(NofetID,NconnID,storeID,4,20,2);
        store.changeSubmissionBid(EuserId,NofetID,4,30,1);
    }

    //todo update
    @Test
    void checkApproveTheCorrectBid() {
        String guest= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(guest, "M1", "123");
        Response res= tradingSystem.Login(guest, "M1", "123");
        tradingSystem.AddNewManager(EuserId,EconnID,storeID,res.returnUserID());
        LinkedList<PermissionEnum.Permission> list = new LinkedList<>();
        list.add(PermissionEnum.Permission.RequestBidding);
        tradingSystem.EditManagerPermissions(EuserId, EconnID, storeID, res.returnUserID(), list);

        tradingSystem.subscriberBidding(NofetID,NconnID,storeID,3,2000,2);
        store.changeSubmissionBid(EuserId,NofetID,3,2459,1);
        Response r=store.approveSubmissionBid(res.returnUserID(),NofetID,3);
        assertTrue(res.getIsErr());
    }

    @Test
    void RefuseTheCorrectBid() {
        String guest= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(guest, "M2", "123");
        Response res= tradingSystem.Login(guest, "M2", "123");
        tradingSystem.AddNewManager(EuserId,EconnID,storeID,res.returnUserID());
        LinkedList<PermissionEnum.Permission> list = new LinkedList<>();
        list.add(PermissionEnum.Permission.RequestBidding);
        tradingSystem.EditManagerPermissions(EuserId, EconnID, storeID, res.returnUserID(), list);

        tradingSystem.subscriberBidding(NofetID,NconnID,storeID,3,2000,2);
        store.changeSubmissionBid(EuserId,NofetID,3,2459,1);
        Response r=store.refuseSubmissionBid(res.returnUserID(),NofetID,3);
        assertTrue(res.getIsErr());
    }

    @Test
    void basicSenario(){
        String guest= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(guest, "M2", "123");
        Response res= tradingSystem.Login(guest, "M2", "123");
        tradingSystem.AddNewManager(EuserId,EconnID,storeID,res.returnUserID());
        LinkedList<PermissionEnum.Permission> list = new LinkedList<>();
        list.add(PermissionEnum.Permission.RequestBidding);
        tradingSystem.EditManagerPermissions(EuserId, EconnID, storeID, res.returnUserID(), list);

        List<DummyProduct> list0=tradingSystem.subscribers.get(res.returnUserID()).ShowSpecialProductInShoppingCart();
        Integer productID1 = store.getProductID("computer");

        Response r1=tradingSystem.subscriberBidding(NofetID,NconnID,storeID,productID1,2700,1);
        Response r2=tradingSystem.ResponseForSubmissionBidding(EuserId,EconnID,storeID,productID1,2700,NofetID,1,1);
        Response r3=tradingSystem.ResponseForSubmissionBidding(res.returnUserID(),res.returnConnID(),storeID,productID1,2700,NofetID,1,1);
        List<DummyProduct> list1=tradingSystem.subscribers.get(NofetID).ShowSpecialProductInShoppingCart();

        store.AddProductToStore( "bla", 3000.0, "Technology",5);
        Integer productID2 = store.getProductID("bla");
        Response prod=tradingSystem.AddProductToCart(NconnID,storeID,productID2,1);

        tradingSystem.subscriberPurchase(NofetID,NconnID,",1","1","","111","11","aaa","eeee","errrr","dddd");
        List<DummyShoppingHistory> hist= tradingSystem.ShowStoreHistory(storeID);
        assertTrue(hist.size()==1);
    }


    //endregion

    /*
    //region requirement 4.1: Add/Edit/Remove product
    // requirement 4.1
    @Test
    public void AddProductSuccess(){
        Response response= store.AddProductToStore("prod8",11.0,"food",9);
        assertFalse(response.getIsErr());
    }

    @Test
    public void AddProductSad_NameTaken(){
        store.AddProductToStore("prod8",11.0,"food",9);
        Response response= store.AddProductToStore("prod8",11.0,"food",9);
        assertTrue(response.getIsErr());
    }

//    // requirement 4.1
//    @Test
//    public void AddProductInvalidAmount(){
//        Response response= store.AddProductToStore("prod3",11.0,"food",-1);
//        assertTrue(response.getIsErr());
//    }

//    // requirement 4.1
//    @Test
//    public void AddProductInvalidPermission(){
//        Response response= tradingSystem.AddProductToStore(NofetID,NconnID,storeID,"prod3","food",11.0,2);
//        assertTrue(response.getIsErr());
//    }

//    // requirement 4.1
//    @Test
//    public void AddProductInValidDetails(){
//        Response response= tradingSystem.AddProductToStore(userID,connID,storeid,"prod3","food",-1,11);
//        assertTrue(response.getIsErr());
//    }

    // requirement 4.1
    @Test
    public void ChangeProductQuantitySuccess(){
        Response response= store.editProductDetails(EuserId,1,"computer",3000.0,"Technology",50);
        assertFalse(response.getIsErr());
        assertEquals(store.getQuantity(1), 50);
    }

//    // requirement 4.1
//    @Test
//    public void EditProductWrongQuantity(){
//        Response response= store.editProductDetails(EuserId,1,"computer",3000.0,"Technology",-5);
//        Assertions.assertFalse(response.getIsErr());
//        assertEquals(store.getQuantity(1), 5);
//    }

//    // requirement 4.1
//    @Test
//    public void ChangeProductQuantityInvalidPermission(){
//        Response response= tradingSystem.ChangeQuantityProduct(ElinorID,EconnID,storeid,productId,10);
//        assertTrue(response.getIsErr());
//    }

//    // requirement 4.1
//    @org.junit.Test
//    public void ChangeProductWrongQuantity(){
//        Response response= tradingSystem.ChangeQuantityProduct(userID,connID,storeid,productId,-10);
//        assertTrue(response.getIsErr());
//    }

    // requirement 4.1
    @Test
    public void EditProductSuccess(){
        Response response= store.editProductDetails(EuserId,1,"computer",2500.0,"Technology",5);
        assertFalse(response.getIsErr());
        assertEquals(store.getProduct(1).getPrice(), 2500.0);
    }

    @Test
    public void EditProductSad_NotExistProduct(){
        Response response= store.editProductDetails(EuserId,100,"computer",2500.0,"Technology",5);
        assertTrue(response.getIsErr());
        assertEquals(store.getProduct(1).getPrice(), 3000.0);
    }

//    // requirement 4.1
//    @org.junit.Test
//    public void EditProductInvalidPermission(){
//        String connID1= tradingSystem.ConnectSystem().returnConnID();
//        Response response= tradingSystem.Register(connID1,"reutlevy30","8119");
//        int userID1= response.returnUserID();
//        connID1= tradingSystem.Login(connID1,"reutlevy30","8119").returnConnID();
//        response= tradingSystem.EditProduct(userID1,connID1,storeid,productId,"prod4","food",12.0,9);
//        assertTrue(response.getIsErr());
//    }

    // requirement 4.1
//    @Test
//    public void EditProductWrongPrice(){
//        Response response= tradingSystem.EditProduct(userID,connID,storeid,productId,"prod4","food",-12.0,9);
//        assertTrue(response.getIsErr());
//    }

    // requirement 4.1
    @Test
    public void RemoveProductSuccess(){
        Response response= store.deleteProduct(2);
        assertFalse(response.getIsErr());
        assertEquals(store.getProduct(2), null);
    }

//    // requirement 4.1
//    @Test
//    public void RemoveProductInvalidPermission(){
//        String connID1= tradingSystem.ConnectSystem().returnConnID();
//        Response response= tradingSystem.Register(connID1,"reutlevy30","8119");
//        int userID1= response.returnUserID();
//        connID1= tradingSystem.Login(connID1,"reutlevy30","8119").returnConnID();
//        response= tradingSystem.RemoveProduct(userID1,storeid,productId,connID1);
//        assertTrue(response.getIsErr());
//    }

    // requirement 4.1
    @Test
    public void RemoveProductNotExist(){
        Response response= store.deleteProduct(100);
        assertTrue(response.getIsErr());
    }

    // requirement 4.1
    @Test
    public void removeProductFromStoreWhileOtherClientBuyingItTest_Parallel() {
        List<boolean[]> isErrsTotal = new ArrayList<>();
        for (int test_i = 0; test_i < 10; test_i++) {
            //Prepare
            Integer newProduct = store.getProductID("computer");
            //Create two clients with task to buy this product
            ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(2);

            //Prepare tasks for clients
            List<Callable<ResultUnitTests>> taskList = new ArrayList<>();
            Callable<ResultUnitTests> purchaseTask = new PurchaseTaskUnitTests("guestBuyer", storeID, newProduct, 25, "123456789", "4", "2022" ,"123" ,"123456789" ,"Rager 101","Beer Sheva","Israel","8458527");
            taskList.add(purchaseTask);
            Callable<ResultUnitTests> removeTask = new RemoveProductTaskUnitTests("Client-StoreOwner",storeID, newProduct);
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

    //endregion

     */

/*
    //region Conditional discount tests
    @Test
    void HappyStoreSaleTest() {
        PriceForGetSale exp = new PriceForGetSale(1, 100.0);
        store.addSaleToPolicy(null, -1, 10.0, exp);
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 1);
        products.put(productID2, 1);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(2790.0, newPrice, 0);
    }

    @Test
    void SadStoreSaleTest() {
        PriceForGetSale exp = new PriceForGetSale(1, 5000.0);
        store.addSaleToPolicy(null, -1, 10.0, exp);
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 1);
        products.put(productID2, 1);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(3100.0, newPrice, 0);
    }

    @Test
    void HappyProductSaleTest() {
        tradingSystem.AddStoreToList(store);
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        NumOfProductsForGetSale exp = new NumOfProductsForGetSale(1, 1);
        store.addSaleToPolicy(null, productID1, 20.0, exp);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(4900.0, newPrice,0);
    }

    @Test
    void SadProductSaleTest() {
        tradingSystem.AddStoreToList(store);
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        NumOfProductsForGetSale exp = new NumOfProductsForGetSale(1, 3);
        store.addSaleToPolicy(null, productID1, 20.0, exp);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(6100.0, newPrice,0);
    }

    @Test
    void HappyCategorySaleTest() {
        tradingSystem.AddStoreToList(store);
        Integer productID1 = store.getProductID("Ball");
        Integer productID2 = store.getProductID("Bag");
        QuantityForGetSale exp = new QuantityForGetSale(1, productID1, 2);
        store.addSaleToPolicy("Fun", -1, 50.0, exp);
        ConcurrentHashMap<Integer, Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(160.0, newPrice,0);
    }

    @Test
    void SadCategorySaleTest() {
        tradingSystem.AddStoreToList(store);
        Integer productID1 = store.getProductID("Ball");
        Integer productID2 = store.getProductID("Bag");
        QuantityForGetSale exp = new QuantityForGetSale(1, productID1, 4);
        store.addSaleToPolicy("Fun", -1, 50.0, exp);
        ConcurrentHashMap<Integer, Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(220.0, newPrice,0);
    }
    //endregion

    //region Discount rules tests
    @Test
    void HappyAndRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        PriceForGetSale exp1 = new PriceForGetSale(1, 1000.0);
        QuantityForGetSale exp2 = new QuantityForGetSale(2,productID1,2);
        AndComposite andExpression = new AndComposite(1);
        andExpression.add(exp1);
        andExpression.add(exp2);
        store.addSaleToPolicy(null, -1, 50.0, andExpression);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(3050.0, newPrice,0);
    }

    @Test
    void SadAndRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        PriceForGetSale exp1 = new PriceForGetSale(1, 1000.0);
        QuantityForGetSale exp2 = new QuantityForGetSale(2,productID1,5);
        AndComposite andExpression = new AndComposite(1);
        andExpression.add(exp1);
        andExpression.add(exp2);
        store.addSaleToPolicy(null, -1, 50.0, andExpression);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(6100.0, newPrice,0);
    }

    @Test
    void HappyOrRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        PriceForGetSale exp1 = new PriceForGetSale(1, 10000.0);
        QuantityForGetSale exp2 = new QuantityForGetSale(2,productID1,2);
        OrComposite orExpression = new OrComposite(1);
        orExpression.add(exp1);
        orExpression.add(exp2);
        store.addSaleToPolicy(null, -1, 50.0, orExpression);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(3050.0, newPrice,0);
    }

    @Test
    void SadOrRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        PriceForGetSale exp1 = new PriceForGetSale(1, 10000.0);
        QuantityForGetSale exp2 = new QuantityForGetSale(2,productID1,5);
        OrComposite orExpression = new OrComposite(1);
        orExpression.add(exp1);
        orExpression.add(exp2);
        store.addSaleToPolicy(null, -1, 50.0, orExpression);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(6100.0, newPrice,0);
    }

    @Test
    void HappyXorRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        //StoreSale sale = new StoreSale(1, store.getId(), 50.0);
        PriceForGetSale exp1 = new PriceForGetSale(1, 8000.0);
        QuantityForGetSale exp2 = new QuantityForGetSale(2,productID1,2);
        XorComposite xorComposite = new XorComposite(1);
        xorComposite.add(exp1);
        xorComposite.add(exp2);
        //sale.setExpression(xorComposite);
        //DC.AddSale(sale);
        store.addSaleToPolicy(null, -1, 50.0, xorComposite);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        //Double newPrice = DC.calculatePrice(products,2,6100.0);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(3050.0, newPrice,0);
    }

    @Test
    void SadXorRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        //StoreSale sale = new StoreSale(1, store.getId(), 50.0);
        PriceForGetSale exp1 = new PriceForGetSale(1, 1000.0);
        QuantityForGetSale exp2 = new QuantityForGetSale(2,productID1,2);
        XorComposite xorComposite = new XorComposite(1);
        xorComposite.add(exp1);
        xorComposite.add(exp2);
        //sale.setExpression(xorComposite);
        //DC.AddSale(sale);
        store.addSaleToPolicy(null, -1, 50.0, xorComposite);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        //Double newPrice = DC.calculatePrice(products,2,6100.0);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(6100.0, newPrice,0);
    }

    //endregion

    //region Simple buying rules tests
    @Test
    void HappyProductRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimit exp = new QuantityLimit(1, 1);
        store.addLimitToPolicy(null,productID1,exp);
        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 1);
        products.put(productID2, 3);
        Boolean isLegal = store.checkEntitlement(2, products);
        assertTrue(isLegal);
    }

    @Test
    void SadProductRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimit exp = new QuantityLimit(1, 1);
        store.addLimitToPolicy(null,productID1,exp);
        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 3);
        Boolean isLegal = store.checkEntitlement(2, products);
        assertFalse(isLegal);
    }

    @Test
    void HappyCategoryRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimit exp = new QuantityLimit(1, 1);
        store.addLimitToPolicy("Technology",-1,exp);
        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 1);
        products.put(productID2, 3);
        Boolean isLegal = store.checkEntitlement(2, products);
        assertTrue(isLegal);
    }

    @Test
    void SadCategoryRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimit exp = new QuantityLimit(1, 1);
        store.addLimitToPolicy("Technology",-1,exp);
        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 3);
        Boolean isLegal = store.checkEntitlement(2, products);
        assertFalse(isLegal);
    }

    @Test
    void HappyBagRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimit exp = new QuantityLimit(1, 4);
        store.addLimitToPolicy(null,-1,exp);
        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 1);
        products.put(productID2, 3);
        Boolean isLegal = store.checkEntitlement(2, products);
        assertTrue(isLegal);
    }

    @Test
    void SadBagRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimit exp = new QuantityLimit(1, 4);
        store.addLimitToPolicy(null,-1,exp);
        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 3);
        Boolean isLegal = store.checkEntitlement(2, products);
        assertFalse(isLegal);
    }

    @Test
    void HappyUserRule() {
    }

    @Test
    void SadUserRule() {
    }
    //endregion

    //region Assembly of buying rules tests
    @Test
    void HappyAndBuying() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimit exp1 = new QuantityLimit(1, 4);
        AgeLimit exp2 = new AgeLimit(2, 20);
        AndComposite andExpression = new AndComposite(1);
        andExpression.add(exp1);
        andExpression.add(exp2);
        store.addLimitToPolicy(null,-1,andExpression);
        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 1);
        products.put(productID2, 3);
        //TODO check age
        Boolean isLegal = store.checkEntitlement(2, products);
        assertTrue(isLegal);
    }

    @Test
    void SadAndBuying() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimit exp1 = new QuantityLimit(1, 4);
        AgeLimit exp2 = new AgeLimit(2, 20);
        AndComposite andExpression = new AndComposite(1);
        andExpression.add(exp1);
        andExpression.add(exp2);
        store.addLimitToPolicy(null,-1,andExpression);
        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 3);
        //TODO check age
        Boolean isLegal = store.checkEntitlement(2, products);
        assertTrue(isLegal);
    }

    @Test
    void HappyOrBuying() {
    }

    @Test
    void SadOrBuying() {
    }

    @Test
    void HappyXorBuying() {
    }

    @Test
    void SadXorBuying() {
    }

    @Test
    void HappyConditioningBuying() {
    }

    @Test
    void SadConditioningBuying() {
    }
    //endregion


 */
}