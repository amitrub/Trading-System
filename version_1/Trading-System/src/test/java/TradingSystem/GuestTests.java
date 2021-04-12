package TradingSystem;

import TradingSystem.Client.Client;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
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

    Client client;
    TradingSystem tradingSystem = TradingSystem.getInstance();

    @BeforeEach
    void setUp() {
        this.client = new Client();
        client.connectSystem();
    }

    @AfterEach
    void tearDown() {
        client.exitSystem();
        tradingSystem.Initialization();
    }

    //region system Tests
    @Test
    void connectionTest() {
        // setUp
        // this.client = new Client();
        // client.connectSystem();
        assertNotEquals(this.client.getConnID(), "");
    }
    @Test
    void exitTest() {
        // Will work just without the tearDown!!!
//        client.exitSystem();
//        assertEquals(this.client.getConnID(), "");
    }
    //endregion
    //region Register Tests
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
    void registerShortPassword() {
        int respondID = client.Register("Lior", "1");
        assertTrue(respondID == -1);
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

    //endregion
    //region Login Tests
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
    //region Search Tests
    @Test
    void search_ProductName(){
        client.Register("Shani", "123");
        client.Login("Shani", "123");
        client.openStore("H&M");
        ArrayList<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Simple Dress", "Dress", 120.0, 50);
        client.addProduct(storeID, "Evening Dress", "Dress", 250.0, 50);
        client.addProduct(storeID, "Jeans Dress", "Dress", 90.0, 50);
        client.addProduct(storeID, "Basic T-shirt", "Tops", 120.0, 50);
        client.addProduct(storeID, "Stripe Shirt", "Tops", 120.0, 50);

        //Search by product name
        ArrayList<DummyProduct> searchProducts1 = client.Search("Product Name","Jeans Dress", "50.0","100.0","1","5");
        assertEquals(searchProducts1.size(),1);

    }

    @Test
    void searchTest_ProductCategory() {
        client.Register("Shalom", "123");
        client.Login("Shalom", "123");
        client.openStore("H&O");
        ArrayList<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Simple Dress", "Dress", 120.0, 50);
        client.addProduct(storeID, "Evening Dress", "Dress", 250.0, 50);
        client.addProduct(storeID, "Jeans Dress", "Dress", 90.0, 50);
        client.addProduct(storeID, "Basic T-shirt", "Tops", 120.0, 50);
        client.addProduct(storeID, "Stripe Shirt", "Tops", 120.0, 50);

        //search by product category
        ArrayList<DummyProduct> searchProducts2 = client.Search("Product Category", "Tops", "30.0","150.0","1", "5");
        assertEquals(searchProducts2.size(),2);
    }

    @Test
    void searchTest_ProductCategoryAndPrice() {
        client.Register("Shaya", "123");
        client.Login("Shaya", "123");
        client.openStore("H&L");
        ArrayList<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Simple Dress", "Dress", 120.0, 50);
        client.addProduct(storeID, "Evening Dress", "Dress", 250.0, 50);
        client.addProduct(storeID, "Jeans Dress", "Dress", 90.0, 50);
        client.addProduct(storeID, "Basic T-shirt", "Tops", 120.0, 50);
        client.addProduct(storeID, "Stripe Shirt", "Tops", 120.0, 50);

        //search by product category and price
        ArrayList<DummyProduct> searchProducts3 = client.Search("Product Category", "Tops", "100.0","150.0","1", "5");
        assertEquals(searchProducts3.size(),1);
    }

    @Test
    void search_Sad() {
        client.Register("Lital", "123");
        client.Login("Lital", "123");
        client.openStore("H&V");
        ArrayList<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Simple Dress", "Dress", 120.0, 50);
        client.addProduct(storeID, "Evening Dress", "Dress", 250.0, 50);
        client.addProduct(storeID, "Jeans Dress", "Dress", 90.0, 50);
        client.addProduct(storeID, "Basic T-shirt", "Tops", 120.0, 50);
        client.addProduct(storeID, "Stripe Shirt", "Tops", 120.0, 50);

        //sad search - there isn't products that match the search
        ArrayList<DummyProduct> searchProducts4 = client.Search("Product Category", "Tops", "150.0","200.0","1", "5");
        assertEquals(searchProducts4.size(),0);
    }
    //endregion
    //region Stores Tests
    @Test
    void showStoreProducts() {
        //todo - maybe there is problem with the function showAllStores
        client.Register("Or", "123");
        client.Login("Or", "123");
        client.openStore("Renuar");
        ArrayList<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Simple Dress", "Dress", 120.0, 20);
        client.addProduct(storeID, "Evening Dress", "Dress", 250.0, 20);
        ArrayList<DummyProduct> products= client.showStoreProducts(storeID);
        assertEquals(products.size(), 2);
    }

    @Test
    void showAllStores() {
        //todo - shows one more store - why??
        client.Register("Reut", "123");
        client.Login("Reut", "123");
        ArrayList<DummyStore> stores1 = client.showAllStores();
        assertEquals(stores1.size(), 0);

        client.openStore("Castro");
        client.openStore("Urbanica");
        client.openStore("Zara");
        ArrayList<DummyStore> stores2 = client.showAllStores();
        assertEquals(stores2.size(), 3);
    }

    //endregion
    //region Shopping Cart Tests
    @Test
    void addProductToCart_Happy() {
        client.Register("Hadas", "123");
        client.Login("Hadas", "123");
        client.openStore("Mania Jeans");
        ArrayList<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Short Pants", "Pants", 120.0, 2);
        ArrayList<DummyProduct> products = client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();

        client.addProductToCart(storeID, productID, 1);
        assertEquals(client.showShoopingCart().size(), 1);
        String ans1 = client.showShoopingCart().get(0).getProductName();
        assertEquals(ans1, "Short Pants");
    }

    @Test
    void addProductToCart_SadQuantity() {
        client.Register("Liat", "123");
        client.Login("Liat", "123");
        client.openStore("Mango");
        ArrayList<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Short Pants", "Pants", 120.0, 2);
        ArrayList<DummyProduct> products = client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();

        client.addProductToCart(storeID, productID, 3);
        assertEquals(client.showShoopingCart().size(), 0);
    }

    @Test
    void showShoopingCart_Happy() {
        client.Register("Amit", "123");
        client.Login("Amit", "123");
        client.openStore("Bershka");
        ArrayList<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Jeans Pants", "Pants", 100.0, 10);

        ArrayList<DummyProduct> products= client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();
        client.addProductToCart(storeID, productID , 3);
        assertEquals(client.showShoopingCart().size(), 3);
    }

    @Test
    void showShoopingCart_Sad() {
        client.Register("Dana", "123");
        client.Login("Dana", "123");
        client.openStore("Cocktail");

        ArrayList<DummyProduct> ans1 = client.showShoopingCart();
        assertEquals(ans1.size(), 0);
    }

    @Test
    void editShoppingCart_HappyRemove()
    {

    }

    @Test
    void editShoppingCart_HappyQuantity()
    {

    }

    @Test
    void editShoppingCart_SadQauntity()
    {
    }

    @Test
    void editShoppingCart_SadEmptyCart()
    {

    }
    //endregion
    //region Purchase tests

    @Test
    void Purchase_Happy() {
        client.Register("Hadas", "123");
        client.Login("Hadas", "123");
        client.openStore("Mania Jeans");
        ArrayList<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Short Pants", "Pants", 120.0, 2);
        ArrayList<DummyProduct> products = client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();

        client.Logout();

        client.addProductToCart(storeID, productID, 1);
        assertEquals(client.showShoopingCart().size(), 1);
        String ans1 = client.showShoopingCart().get(0).getProductName();
        assertEquals(ans1, "Short Pants");
    }

    @Test
    void Purchase_SadEmptyCart() {
    }

    @Test
    void Purchase_SadPaying() {
    }

    //endregion
}
