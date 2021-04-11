package TradingSystem;

import TradingSystem.Client.Client;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GuestTests {

    Client client;

    @BeforeEach
    void setUp() {
        this.client = new Client();
        client.connectSystem();
    }

    @AfterEach
    void tearDown() {
        client.exitSystem();
    }

    //Checked
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
        assertTrue(respondID1 == -1 && !this.client.getConnID().equals(""));

        int respondID2 = client.Register("Dani", "qwerty");
        assertTrue(respondID2 == -1 && !this.client.getConnID().equals(""));
    }

    @Test
    void registerDuplicateUserName() {   //duplicate userName
        int respondID1 = client.Register("Avi", "qwerty");
        assertTrue(respondID1 == -1 && !this.client.getConnID().equals(""));
        int respondID2 = client.Register("Avi", "qqq");
        assertTrue(respondID2 == -1 && this.client.getConnID().equals(""));
    }

    /*
    @Test
    void registerPassword() {    //password is too short
        int respondID = client.Register("Lior", "1");
        assertTrue(respondID == -1);
    }
     */

    //endregion
    //region Login Tests
    @Test
    void loginHappy(){
        int guestID = client.Register("Yossi", "qwerty");
        String guestConnID = this.client.getConnID();
        int subscriberID = client.Login("Yossi", "qwerty");
        assertTrue(guestID != subscriberID && !guestConnID.equals(this.client.getConnID()));
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

    //Not Checked
    //region Search Tests
    @Test
    void searchTest(){
        Integer founderID = client.Register("Shani", "qwerty");
        client.openStore("H&M");
        ArrayList<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Simple Dress", "Dress", 120.0, 50);
        client.addProduct(storeID, "Evening Dress", "Dress", 250.0, 50);
        client.addProduct(storeID, "Jeans Dress", "Dress", 90.0, 50);
        client.addProduct(storeID, "Basic T-shirt", "Tops", 120.0, 50);
        client.addProduct(storeID, "Stripe Shirt", "Tops", 120.0, 50);

        //Search by product name
        ArrayList<DummyProduct> searchProducts1 = client.Search("Product Name","Jeans", "50.0","100.0","1","5");
        assertEquals(searchProducts1.size(),1);
        String ans1 = searchProducts1.get(0).getProductName();
        assertEquals("Jeans Dress", ans1);


        //search by product category
        ArrayList<DummyProduct> searchProducts2 = client.Search("Product Category", "Tops", "30.0","150.0","1", "5");
        assertEquals(searchProducts2.size(),2);
        String ans2 = searchProducts2.get(0).getProductName();
        assertEquals("Basic T-shirt", ans2);
        String ans3 = searchProducts2.get(1).getProductName();
        assertEquals("Stripe Shirt", ans3);

        //search by product category and price
        ArrayList<DummyProduct> searchProducts3 = client.Search("Product Category", "Tops", "100.0","150.0","1", "5");
        assertEquals(searchProducts2.size(),1);
        String ans4 = searchProducts3.get(0).getProductName();
        assertEquals("Stripe Shirt", ans4);

        //sad search - there isn't products that match the search
        ArrayList<DummyProduct> searchProducts4 = client.Search("Product Category", "Tops", "150.0","200.0","1", "5");
        assertEquals(searchProducts2.size(),0);
    }
//    @Test
//    void searchByName() {
//    }
//
//    @Test
//    void searchByCategory() {
//    }
    //endregion
    //region Stores Tests
    @Test
    void showStoreProducts() {
        Integer founderID = client.Register("Or", "qwerty");
        client.openStore("Renuar");
        ArrayList<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Simple Dress", "Dress", 120.0, 20);
        client.addProduct(storeID, "Evening Dress", "Dress", 250.0, 20);
        ArrayList<DummyProduct> products= client.showStoreProducts(storeID);
        assertEquals(products.size(), 2);
        assertEquals("Simple Dress", products.get(0).getProductName());
        assertEquals("Evening Dress", products.get(1).getProductName());
    }

    @Test
    void showAllStores() {
        Integer founderID = client.Register("Reut", "qwerty");
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
    void addProductToCart() {
        Integer founderID = client.Register("Hadas", "qwerty");
        client.openStore("Mania Jeans");
        ArrayList<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Short Pants", "Pants", 120.0, 2);
        ArrayList<DummyProduct> products = client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();

        //sad add - the quantity of the wanted product doesn't exist
        client.addProductToCart(storeID, productID, 3);
        assertEquals(client.showShoopingCart().size(), 0);

        //happy add
        client.addProductToCart(storeID, productID, 1);
        assertEquals(client.showShoopingCart().size(), 1);
        String ans1 = client.showShoopingCart().get(0).getProductName();
        assertEquals(ans1, "Short Pants");
    }

    @Test
    void showShoopingCart() {
        Integer founderID = client.Register("Amit", "qwerty");
        client.openStore("Bershka");
        ArrayList<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Jeans Pants", "Pants", 100.0, 10);

        //sad show - there isn't products in the shopping cart
        ArrayList<DummyProduct> ans1 = client.showShoopingCart();
        assertEquals(ans1.size(), 0);

        //happy show
        ArrayList<DummyProduct> products= client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();
        client.addProductToCart(storeID, productID , 3);
        assertEquals(client.showShoopingCart().size(), 3);
    }
    //endregion

}
