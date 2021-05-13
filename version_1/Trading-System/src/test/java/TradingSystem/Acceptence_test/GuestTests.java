package TradingSystem.Acceptence_test;

import TradingSystem.Client.ClientProxy;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

public class GuestTests {

    ClientProxy client=new ClientProxy();
    TradingSystemImpl tradingSystemImpl = TradingSystemImpl.getInstance();

    @BeforeEach
    void setUp() {
       // this.client = new Client();
        client.clearSystem();
        client.connectSystem();
    }

    @AfterEach
    void tearDown() {
        client.exitSystem();
        client.clearSystem();
    }

    Integer getStoreID(List<DummyStore> stores, String storename)
    {
        for (int i=0; i<stores.size(); i++)
        {
            if(stores.get(i).getName().equals(storename))
                return stores.get(i).getId();
        }
        return -1;
    }

    //region system Tests requirement 2.1-2.2
    /**
     * @requirement 2.1
     */
    @Test
    void connectionTest() {
        // setUp
        // this.client = new Client();
        assertNotEquals(this.client.getConnID(), "");
    }

    /**
     * @requirement 2.2
     */
    @Test
    void exitTest() {
        // Will work just without the tearDown!!!
//        client.exitSystem();
//        assertEquals(this.client.getConnID(), "");
    }

    //endregion
    //region Register Tests requirement 2.3

    /**
     * @requirement 2.3 register to the system
     */
    @Test
    void registerHappy() {
        int respondID1 = client.Register("Roee", "1234");
        assertTrue(respondID1 != -1 && !this.client.getConnID().equals(""));

        int respondID2 = client.Register("Dani", "qwerty");
        assertTrue(respondID2 != -1 && !this.client.getConnID().equals(""));
    }
    @Test
    void registerDuplicateUserName() {   //duplicate userName
        int respondID1 = client.Register("Avi", "qwerty");
        assertTrue(respondID1 != -1 && !this.client.getConnID().equals(""));
        int respondID2 = client.Register("Avi", "qqq");
        assertTrue(respondID2 == -1 && this.client.getConnID().equals(""));
    }
    @Test
    void registerParallelHappy(){
        ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(2);

        List<RegisterTask> taskList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            RegisterTask task = new RegisterTask("Client-" + i);
            taskList.add(task);
        }

        //Execute all tasks and get reference to Future objects
        List<Future<Result>> resultList = null;

        try {
            resultList = executor.invokeAll(taskList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();

        System.out.println("\n========Printing the results======");

        assert resultList != null;
        for (int i = 0; i < resultList.size(); i++) {
            Future<Result> future = resultList.get(i);
            try {
                Result result = future.get();
                System.out.println(result.getName() + ": " + result.getTimestamp());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    @Test
    void registerParallelSadSameName(){
        ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(2);

        List<RegisterTask> taskList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            RegisterTask task = new RegisterTask("SameName");
            taskList.add(task);
        }

        //Execute all tasks and get reference to Future objects
        List<Future<Result>> resultList = null;

        try {
            resultList = executor.invokeAll(taskList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executor.shutdown();

        System.out.println("\n========Printing the results======");

        assert resultList != null;
        for (int i = 0; i < resultList.size(); i++) {
            Future<Result> future = resultList.get(i);
            try {
                Result result = future.get();
                System.out.println(result.getName() + ": " + result.getTimestamp());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
//    @Test
//    void registerShortPassword() {
//        int respondID = client.Register("Lior", "q");
//        assertTrue(respondID == -1);
//    }

    //endregion
    //region Login Tests requirement 2.4

    /**
     * @requirement 2.4 login tests
     */
    @Test
    void loginHappy(){
        int guestID = client.Register("Yossi", "qwerty");
        String guestConnID = this.client.getConnID();
        int subscriberID = client.Login("Yossi", "qwerty");
        assertTrue(guestID == subscriberID && !guestConnID.equals(this.client.getConnID()));
    }
    @Test
    void loginIncorrectPassword(){
        client.Register("Shai", "qwerty");
        int subscriberID = client.Login("Shai", "qwe");
        assertTrue(subscriberID == -1 && this.client.getConnID().equals(""));
    }
    @Test
    void loginIncorrectUserName(){
        client.Register("Elinor", "qwerty");
        int respondID = client.Login("Eli", "qwerty");
        assertTrue(respondID == -1 && this.client.getConnID().equals(""));
    }

    //endregion
    //region Stores Tests: requirement 2.5
    /**
     * @requirement 2.5 show all stores, products in store
     */
    @Test
    void showAllStores() {
        ClientProxy clientProxy= new ClientProxy();
        clientProxy.connectSystem();
        //case: no stores at all
        clientProxy.Register("Reut", "123");
        tradingSystemImpl.ClearSystem();
        clientProxy.Login("Reut", "123");
        List<DummyStore> stores1 = client.showAllStores();
        assertEquals(stores1.size(), 0);

        //case: have stores
//        clientProxy.openStore("Castro");
//        clientProxy.openStore("Urbanica");
//        clientProxy.openStore("Zara");
//        List<DummyStore> stores2 = client.showAllStores();
//        assertEquals(stores2.size(), 3);
    }
    @Test
    void showAllStoresSadNoStores() {
        //case: no stores at all
        client.Register("Reut", "123");
        tradingSystemImpl.ClearSystem();
        client.Login("Reut", "123");
        List<DummyStore> stores1 = client.showAllStores();
        assertEquals(stores1.size(), 0);
    }
    @Test
    void showProductsOnSpecificStore() {
        client.Register("Or", "123");
        client.Login("Or", "123");
        client.openStore("Renuar");
        List<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Simple Dress", "Dress", 120.0, 20);
        client.addProduct(storeID, "Evening Dress", "Dress", 250.0, 20);
        List<DummyProduct> products= client.showStoreProducts(storeID);
        assertEquals(products.size(), 5);
    }
    //endregion
    //region Search Tests requirement 2.6

    /**
     * @requirement 2.6 search products
     */
    @Test
    void search_ProductName(){
        client.Register("Shani", "123");
        client.Login("Shani", "123");
        client.openStore("H&M");
        List<DummyStore> store = client.showAllStores();
        Integer storeID = getStoreID(store, "H&M");
        client.addProduct(storeID, "Simple Dress", "Dress", 120.0, 50);
        client.addProduct(storeID, "Evening Dress", "Dress", 250.0, 50);
        client.addProduct(storeID, "Jeans Dress", "Dress", 90.0, 50);
        client.addProduct(storeID, "Basic T-shirt", "Tops", 120.0, 50);
        client.addProduct(storeID, "Stripe Shirt", "Tops", 120.0, 50);

        //2.6.1 Search by product name exist
        List<DummyProduct> searchProducts1 = client.Search("Product Name","Jeans Dress", "50","100","1","5");
        assertEquals(searchProducts1.size(),1);

        //2.6.2 Search by product name doesnt exist
        List<DummyProduct> searchNoProducts = client.Search("Product Name","blabla", "50","100","1","5");
        assertEquals(searchNoProducts.size(),0);
    }
    @Test
    void searchTest_ProductCategory() {
        client.Register("Shalom", "123");
        client.Login("Shalom", "123");
        client.openStore("H&O");
        List<DummyStore> store = client.showAllStores();
        Integer storeID = getStoreID(store, "H&O");
        client.addProduct(storeID, "Simple Dress", "Dress", 120.0, 50);
        client.addProduct(storeID, "Evening Dress", "Dress", 250.0, 50);
        client.addProduct(storeID, "Jeans Dress", "Dress", 90.0, 50);
        client.addProduct(storeID, "Basic T-shirt", "Tops", 120.0, 50);
        client.addProduct(storeID, "Stripe Shirt", "Tops", 120.0, 50);

        //2.6.3 search by product category exist
        List<DummyProduct> searchProducts2 = client.Search("Product Category", "Tops", "30","150","1", "5");
        assertEquals(searchProducts2.size(),2);

        //2.6.4 search by product category exist
        List<DummyProduct> searchNoProducts = client.Search("Product Category", "blabla", "30","150","1", "5");
        assertEquals(searchNoProducts.size(),0);
    }
    @Test
    void searchTest_ProductCategoryAndPrice() {
        client.Register("Shaya", "123");
        client.Login("Shaya", "123");
        client.openStore("H&L");
        List<DummyStore> store = client.showAllStores();
        Integer storeID = getStoreID(store, "H&L");
        client.addProduct(storeID, "Simple Dress", "Dress", 120.0, 50);
        client.addProduct(storeID, "Evening Dress", "Dress", 250.0, 50);
        client.addProduct(storeID, "Jeans Dress", "Dress", 90.0, 50);
        client.addProduct(storeID, "Basic T-shirt", "Tops", 120.0, 50);
        client.addProduct(storeID, "Stripe Shirt", "Tops", 120.0, 50);

        //2.6.5 search by product category and price
        List<DummyProduct> searchProducts3 = client.Search("Product Category", "Tops", "100","150","1", "5");
        assertEquals(searchProducts3.size(),2);
    }
    @Test
    void search_Sad_emptySearchList() {
        client.Register("Lital", "123");
        client.Login("Lital", "123");
        client.openStore("H&V");
        List<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Simple Dress", "Dress", 120.0, 50);
        client.addProduct(storeID, "Evening Dress", "Dress", 250.0, 50);
        client.addProduct(storeID, "Jeans Dress", "Dress", 90.0, 50);
        client.addProduct(storeID, "Basic T-shirt", "Tops", 120.0, 50);
        client.addProduct(storeID, "Stripe Shirt", "Tops", 120.0, 50);

        //2.6.6 sad search - there isn't products that match the search
        List<DummyProduct> searchProducts4 = client.Search("Product Category", "Tops", "150","200","1", "5");
        assertEquals(searchProducts4.size(),0);
    }

    //endregion
    //region Shopping Cart Tests requirement 2.7-2.8

    /**
     * @requirement  2.7
     */
    @Test
    void addProductToCart_Happy() {
        client.Register("Hadas", "123");
        client.Login("Hadas", "123");
        client.openStore("Mania Jeans");
        List<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Short Pants", "Pants", 120.0, 2);
        List<DummyProduct> products = client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();

        client.addProductToCart(storeID, productID, 1);
        assertEquals(client.showShoppingCart().size(), 1);
        String ans1 = client.showShoppingCart().get(0).getProductName();
        assertEquals(ans1, "Sneakers2");
    }
    @Test
    void addProductToCart_SadQuantity() {
        client.Register("Liat", "123");
        client.Login("Liat", "123");
        client.openStore("Mango");
        List<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Short Pants", "Pants", 120.0, 2);
        List<DummyProduct> products = client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();

        Response response = client.addProductToCart(storeID, productID, 3);
        assertEquals(client.showShoppingCart().size(), 1);
    }
    @Test
    void addProductToCart_SadStoreWithoutThisProduct() {
        client.Register("Liat", "123");
        client.Login("Liat", "123");
        client.openStore("Mango");
        List<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Short Pants", "Pants", 120.0, 2);
        List<DummyProduct> products = client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();

        Response response = client.addProductToCart(storeID, 8, 3);
        assertEquals(client.showShoppingCart().size(), 0);
    }


    /**
     * @requirement 2.8.1 show cart
     */
    @Test
    void showShoppingCart_Happy() {
        client.Register("Amit", "123");
        client.Login("Amit", "123");
        client.openStore("Bershka");
        List<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Jeans Pants", "Pants", 100.0, 10);

        List<DummyProduct> products= client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();
        client.addProductToCart(storeID, productID , 3);
        List<DummyProduct> dummyProducts = client.showShoppingCart();
        assertEquals(dummyProducts.size(), 1); //types
        int quantity = dummyProducts.get(0).getQuantity();
        assertEquals(quantity, 3); //quantity
    }
    @Test
    void showShoopingCart_Sad() {
        client.Register("Dana", "123");
        client.Login("Dana", "123");
        client.openStore("Cocktail");

        List<DummyProduct> ans1 = client.showShoppingCart();
        assertEquals(ans1.size(), 0);
    }


    /**
     * @requirement 2.8.2 remove from cart
     */
    @Test
    void removeFromShoppingCart_Happy()
    {
        // Prepare
        client.Register("Amit", "123");
        client.Login("Amit", "123");
        client.openStore("Bershka");
        List<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Jeans Pants", "Pants", 100.0, 10);
        List<DummyProduct> products= client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();
        client.addProductToCart(storeID, productID , 3);
        List<DummyProduct> dummyProducts = client.showShoppingCart();
        assertEquals(dummyProducts.size(), 1); //types
        int quantity = dummyProducts.get(0).getQuantity();
        assertEquals(quantity, 3); //quantity

        //Issue
        Response response = client.removeFromShoppingCart(storeID, productID);

        //Assert
        assertFalse(response.getIsErr());
        dummyProducts = client.showShoppingCart();
        assertEquals(dummyProducts.size(), 0);
    }
    @Test
    void removeFromShoppingCart_Sad()
    {
        // Prepare
        client.Register("Amit", "123");
        client.Login("Amit", "123");
        client.openStore("Bershka");
        List<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Jeans Pants", "Pants", 100.0, 10);
        List<DummyProduct> products= client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();
        client.addProductToCart(storeID, productID , 3);
        List<DummyProduct> dummyProducts = client.showShoppingCart();
        assertEquals(dummyProducts.size(), 1); //types
        int quantity = dummyProducts.get(0).getQuantity();
        assertEquals(quantity, 3); //quantity

        //Issue
        Response response = client.removeFromShoppingCart(storeID, 8);

        //Assert
        assertTrue(response.getIsErr());
        dummyProducts = client.showShoppingCart();
        assertEquals(dummyProducts.size(), 1);
    }

    /**
     * @requirement 2.8.3 Edit cart
     */
    @Test
    void editShoppingCart_Happy()
    {
        // Prepare
        client.Register("Amit", "123");
        client.Login("Amit", "123");
        client.openStore("Bershka");
        List<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Jeans Pants", "Pants", 100.0, 10);
        List<DummyProduct> products= client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();
        client.addProductToCart(storeID, productID , 3);
        List<DummyProduct> dummyProducts = client.showShoppingCart();
        assertEquals(dummyProducts.size(), 1); //types
        int quantity = dummyProducts.get(0).getQuantity();
        assertEquals(quantity, 3); //quantity

        //Issue
        Response response = client.editShoppingCart(storeID, productID, 6);

        //Assert
        assertFalse(response.getIsErr());
        dummyProducts = client.showShoppingCart();
        assertEquals(dummyProducts.size(), 1); //types
        quantity = dummyProducts.get(0).getQuantity();
        assertEquals(quantity, 6); //quantity
    }
    @Test
    void editShoppingCart_SadProductNoInStore()
    {
        // Prepare
        client.Register("Amit", "123");
        client.Login("Amit", "123");
        client.openStore("Bershka");
        List<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Jeans Pants", "Pants", 100.0, 10);
        List<DummyProduct> products= client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();
        client.addProductToCart(storeID, productID , 3);
        List<DummyProduct> dummyProducts = client.showShoppingCart();
        assertEquals(dummyProducts.size(), 1); //types
        int quantity = dummyProducts.get(0).getQuantity();
        assertEquals(quantity, 3); //quantity

        //Issue
        Response response = client.editShoppingCart(storeID, 7, 6);

        //Assert
        assertTrue(response.getIsErr());
        dummyProducts = client.showShoppingCart();
        assertEquals(dummyProducts.size(), 1); //types
        quantity = dummyProducts.get(0).getQuantity();
        assertEquals(quantity, 3); //quantity
    }
    @Test
    void editShoppingCart_SadEmptyCart()
    {
        // Prepare
        client.Register("Amit", "123");
        client.Login("Amit", "123");
        client.openStore("Bershka");
        List<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Jeans Pants", "Pants", 100.0, 10);
        List<DummyProduct> products= client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();

        //Issue
        Response response = client.editShoppingCart(storeID, productID, 6);

        //Assert
        assertTrue(response.getIsErr());
        List<DummyProduct> dummyProducts = client.showShoppingCart();
        assertEquals(dummyProducts.size(), 0); //types
    }
    @Test
    void editShoppingCart_SadPurchasePolicy()
    {
//        todo:
    }




    //endregion
    //region Purchase tests requirement 2.9

    @Test
    void Purchase_Happy()
    {
        // Prepare
        client.Register("Hadas", "123");
        client.Login("Hadas", "123");
        String store_name = "Mania Jeans";
        client.openStore(store_name);
        List<DummyStore> stores = client.showAllStores();
        Integer storeID = getStoreID(stores, store_name);
        client.addProduct(storeID, "Short Pants", "Pants", 120.0, 2);
        List<DummyProduct> products = client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();
        client.Logout();
        client.addProductToCart(storeID, productID, 1);
        String ans1 = client.showShoppingCart().get(0).getProductName();
        assertEquals(ans1, "Short Pants");

        //Issue
        Response response = client.guestPurchase("Roee", "12345678","052897878787", "sioot st. 5");
        boolean purchaseFailed = response.getIsErr();
        List<DummyProduct> cartAfter = client.showShoppingCart();
        List<DummyProduct> productsAfter = client.showStoreProducts(storeID);
        DummyProduct shortPants = products.get(0);
        DummyProduct shortPantsAfter = productsAfter.get(0);

        //Assert
        if(!purchaseFailed)
            System.out.println("purchase Succeed");
        assertFalse(purchaseFailed);
        assertEquals(cartAfter.size(), 0); //check cart is empty after purchase
        assertEquals(shortPantsAfter.getQuantity(), shortPants.getQuantity() - 1); //check decrease quantity in store

        //Try to buy the sa,e product again
        client.addProductToCart(storeID, productID, 1);
        ans1 = client.showShoppingCart().get(0).getProductName();
        assertEquals(ans1, "Short Pants");

        //Issue
        response = client.guestPurchase("Roee", "12345678","052897878787", "sioot st. 5");
        purchaseFailed = response.getIsErr();
        cartAfter = client.showShoppingCart();
        productsAfter = client.showStoreProducts(storeID);
        shortPants = products.get(0);
        shortPantsAfter = productsAfter.get(0);

        if(!purchaseFailed)
            System.out.println("purchase Succeed");
        assertFalse(purchaseFailed);
        assertEquals(cartAfter.size(), 0); //check cart is empty after purchase
//        assertEquals(shortPantsAfter.getQuantity(), shortPants.getQuantity() - 1); //check decrease quantity in store
    }
    @Test
    void Purchase_SadWrongPayingDetails()
    {
        // Prepare
        client.Register("Hadas", "123");
        client.Login("Hadas", "123");
        String store_name = "Mania Jeans";
        client.openStore(store_name);
        List<DummyStore> stores = client.showAllStores();
        Integer storeID = getStoreID(stores, store_name);
        client.addProduct(storeID, "Short Pants", "Pants", 120.0, 2);
        List<DummyProduct> products = client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();
        client.Logout();
        client.addProductToCart(storeID, productID, 1);
        String ans1 = client.showShoppingCart().get(0).getProductName();
        assertEquals(ans1, "Short Pants");

        //Issue
        Response response =client.guestPurchase("Roee", "wrong paying details","wrong phone", "sioot st. 5");
        boolean purchaseFailed = response.getIsErr();

        //Assert
        if(purchaseFailed)
            System.out.println("purchase shouldn't succeed!!!");
        assertTrue(purchaseFailed);
    }
//    @Test
//    void PurchaseParallel_HappyFailed_TwoBuyersLastProduct_10times()
//    {
//        for(int test_try = 1; test_try <= 10; test_try++) {
//            //Prepare
//            client.Register("Hadas", "123");
//            client.Login("Hadas", "123");
//            String store_name = "Mania Jeans";
//            client.openStore(store_name);
//            List<DummyStore> stores = client.showAllStores();
//            Integer storeID = getStoreID(stores, store_name);
//            client.addProduct(storeID, "Short Pants", "Pants", 120.0, 1);
//            List<DummyProduct> products = client.showStoreProducts(storeID);
//            Integer productID = products.get(0).getProductID();
//            client.Logout();
//
//
//            //Create two clients with task to buy this product
//            ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(2);
//
//            List<PurchaseTask> taskList = new ArrayList<>();
//            for (int i = 0; i < 2; i++) {
//                PurchaseTask task = new PurchaseTask("Client-" + i, storeID, productID,
//                        1, "123456", "052897878787", "sioot st. 5");
//                taskList.add(task);
//            }
//
//            //Execute all tasks and get reference to Future objects
//            List<Future<Result>> resultList = null;
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
//                Future<Result> future = resultList.get(i);
//                try {
//                    Result result = future.get();
////                System.out.println(result.getName() + ": " + result.getTimestamp());
//                    Response response = result.getResponse();
//                    System.out.println("Assert correctnes for " + result.getName() + ": response -> " + response + " ::" + result.getTimestamp());
//                    isErrs[i] = response.getIsErr();
//                } catch (InterruptedException | ExecutionException e) {
//                    e.printStackTrace();
//                }
//            }
//            //Check that one of the client failed and the other succeed.
//            assertTrue((isErrs[0] && !isErrs[1]) || (isErrs[1] && !isErrs[0]));
//            tearDown();
//            setUp();
//        }
//
//    }

//    @Test
//    void PurchaseParallel_Happy_TwoBuyersProduct()
//    {
//        //Prepare
//        client.Register("Hadas", "123");
//        client.Login("Hadas", "123");
//        String store_name = "Mania Jeans";
//        client.openStore(store_name);
//        List<DummyStore> stores = client.showAllStores();
//        Integer storeID = getStoreID(stores, store_name);
//        client.addProduct(storeID, "Short Pants", "Pants", 120.0, 2);
//        List<DummyProduct> products = client.showStoreProducts(storeID);
//        Integer productID = products.get(0).getProductID();
//        client.Logout();
//
//
//        //Create two clients with task to buy this product
//        ExecutorService executor = (ExecutorService) Executors.newFixedThreadPool(2);
//
//        List<PurchaseTask> taskList = new ArrayList<>();
//        for (int i = 0; i < 2; i++) {
//            PurchaseTask task = new PurchaseTask("Client-" + i, storeID, productID,
//                    1,"123456", "052897878787", "sioot st. 5");
//            taskList.add(task);
//        }
//
//        //Execute all tasks and get reference to Future objects
//        List<Future<Result>> resultList = null;
//
//        try {
//            resultList = executor.invokeAll(taskList);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        executor.shutdown();
//
//        System.out.println("\n========Printing the results======");
//        boolean[] isErrs = new boolean[2];
//        for (int i = 0; i < resultList.size(); i++) {
//            Future<Result> future = resultList.get(i);
//            try {
//                Result result = future.get();
////                System.out.println(result.getName() + ": " + result.getTimestamp());
//                Response response = result.getResponse();
//                System.out.println("Assert correctnes for " + result.getName() + ": response -> " + response + " ::" + result.getTimestamp());
//                isErrs[i] = response.getIsErr();
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//        }
//        //Check that one of the client failed and the other succeed.
//        assertTrue(!isErrs[0] && !isErrs[1]);
//    }
    //Todo: 2 clients wants to buy different products, sleep 10 seconds, check that they aren't wait for 20 sec

    //endregion


}