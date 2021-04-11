package User;

import TradingSystem.Client.Client;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserTests {

    Client client;


    @BeforeEach
    void setUp()
    {
        client = new Client();
    }

    @Test
    void registerHappy() {
        int respondID1 = client.Register("Roee", "1234");
        assertTrue(respondID1 != -1);

        int respondID2 = client.Register("Dani", "qwerty");
        assertTrue(respondID2 != -1);
        //connID is empty??
    }

    @Test
    void registerDuplicateUserName() {   //duplicate userName
        int respondID1 = client.Register("Avi", "qwerty");
        assertTrue(respondID1 != -1);
        int respondID2 = client.Register("Avi", "qqq");
        assertTrue(respondID2 == -1);
    }

    /*
    @Test
    void registerPassword() {    //password is too short
        int respondID = client.Register("Lior", "1");
        assertTrue(respondID == -1);
    }
     */

    @Test
    void loginHappy(){
        int respondID1 = client.Register("Yossi", "qwerty");
        client.Logout();
        int respondID2 = client.Login("Yossi", "qwerty");
        assertEquals(respondID1, respondID2);
    }

    @Test
    void loginIncorrectPassword(){
        client.Register("Shai", "qwerty");
        client.Logout();
        int respondID = client.Login("Shai", "qwe");
        assertEquals(respondID, -1);
    }

    @Test
    void loginIncorrectUserName(){
        client.Register("Elinor", "qwerty");
        client.Logout();
        int respondID = client.Login("Eli", "qwerty");
        assertEquals(respondID, -1);
    }

    @Test
    void logoutHappy(){
        client.Register("Shir", "qwerty");
        int respondID = client.Logout();
        assertEquals(respondID, -1);
    }

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

    @Test
    void openStore() {
        client.Register("Nofet", "qwerty");
        boolean b1 = client.openStore("American Eagle");
        assertFalse(b1);
        assertEquals(client.showAllStores().size(), 1);
        String ans1 = client.showAllStores().get(0).getName();
        assertEquals(ans1, "American Eagle");

        //sad add - duplicate store name
        boolean b2 = client.openStore("American Eagle");
        assertTrue(b2);

        //different clients can open store with the same name
        client.Logout();
        client.Register("Hadar", "qwerty");
        boolean b3 = client.openStore("American Eagle");
        assertTrue(b3);    //maybe false
    }

    /*
    @Test
    void showAllUsersHistory() {
        
    }


    @Test
    void writeComment() {
        Integer founderID = client.Register("Karin", "qwerty");
        client.openStore("Fox");
        ArrayList<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Short Pants", "Pants", 120.0, 2);
        ArrayList<DummyProduct> products= client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();
        client.writeComment(storeID, productID, 3, "The product is nice");
        //need to access inventory in store
    }

     */

    @Test
    void addProduct() {
        client.Register("Ori", "qwerty");
        client.openStore("Scoop");
        ArrayList<DummyStore> stores = client.showAllStores();
        Integer storeID = stores.get(0).getId();

        //happy add
        boolean b1 = client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        assertFalse(b1);
        ArrayList<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        assertEquals(storeProducts1.size(), 1);
        assertEquals(storeProducts1.get(0).getProductName(), "Arma Heels");

        //sad add - product price illegal
        boolean b2 = client.addProduct(storeID, "Classic Heels", "Heels", -50.0, 25);
        assertFalse(b2);   //maybe true
        ArrayList<DummyProduct> storeProducts2 = client.showStoreProducts(storeID);
        assertEquals(storeProducts2.size(), 1);
        assertEquals(storeProducts2.get(0).getProductName(), "Arma Heels");

        //sad add - product name is taken
        boolean b3 = client.addProduct(storeID, "Arma Heels", "Heels", 60.0, 25);
        assertFalse(b3);   //maybe true
        ArrayList<DummyProduct> storeProducts3 = client.showStoreProducts(storeID);
        assertEquals(storeProducts3.size(), 1);
        assertEquals(storeProducts3.get(0).getProductName(), "Arma Heels");

        //sad add - product quantity is illegal
        boolean b4 = client.addProduct(storeID, "Short Heels", "Heels", 60.0, -10);
        assertFalse(b4);   //maybe true
        ArrayList<DummyProduct> storeProducts4 = client.showStoreProducts(storeID);
        assertEquals(storeProducts4.size(), 1);
        assertEquals(storeProducts4.get(0).getProductName(), "Arma Heels");
    }

    @Test
    void removeProduct() {
        client.Register("Oriya", "qwerty");
        client.openStore("Grass");
        ArrayList<DummyStore> stores = client.showAllStores();
        Integer storeID = stores.get(0).getId();
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        ArrayList<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        Integer productID = storeProducts1.get(0).getProductID();

        //happy remove
        boolean b1 = client.removeProduct(storeID, productID);
        assertFalse(b1);
        ArrayList<DummyProduct> storeProducts2 = client.showStoreProducts(storeID);
        assertEquals(storeProducts2.size(), 0);

        //bad remove - the product doesn't exist
        boolean b2 = client.removeProduct(storeID, productID);
        assertTrue(b2);
    }

    @Test
    void editProduct() {
        client.Register("Oriyan", "qwerty");
        client.openStore("To-go");
        ArrayList<DummyStore> stores = client.showAllStores();
        Integer storeID = stores.get(0).getId();
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        ArrayList<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        Integer productID = storeProducts1.get(0).getProductID();

        //happy edit
        boolean b1 = client.editProduct(storeID, productID, "Arma Heels", "Heels", 100.0,25);
        assertFalse(b1);
        ArrayList<DummyProduct> storeProducts2 = client.showStoreProducts(storeID);
        Double newPrice = storeProducts2.get(0).getPrice();
        assertEquals(newPrice, 100.0, 0.0);

        //bad edit - the product doesn't exist in the system
        client.removeProduct(storeID, productID);
        boolean b2 = client.editProduct(storeID, productID, "Arma Heels", "Heels", 120.0,25);
        assertTrue(b2);
    }
}
