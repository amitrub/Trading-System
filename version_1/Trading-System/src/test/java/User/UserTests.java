package User;

import TradingSystem.Client.Client;
import TradingSystem.Server.DomainLayer.StoreComponent.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.DiscountPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
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
        Store store = new Store("H&M", founderID);
        store.addNewProduct(founderID, "Simple Dress",120.0 ,"Dress");
        store.addNewProduct(founderID, "Evening Dress", 250.0, "Dress");
        store.addNewProduct(founderID, "Jeans Dress", 90.0, "Dress");
        store.addNewProduct(founderID, "Basic T-shirt", 50.0, "Tops");
        store.addNewProduct(founderID, "Stripe Shirt", 130.0, "Tops");

        //Search by product name
        ArrayList<DummyProduct> searchProducts1 = client.Search("Product Name","Jeans", "50.0","100.0","1","5");
        assertEquals(searchProducts1.size(),1);
        Integer ans1 = store.getProductID("Jeans Dress");
        Integer ans2 = searchProducts1.get(0).getProductID();
        assertEquals(ans1, ans2);


        //search by product category
        ArrayList<DummyProduct> searchProducts2 = client.Search("Product Category", "Tops", "30.0","150.0","1", "5");
        assertEquals(searchProducts2.size(),2);
        Integer ans3 = store.getProductID("Basic T-shirt");
        Integer ans4 = searchProducts2.get(0).getProductID();
        assertEquals(ans3, ans4);
        Integer ans5 = store.getProductID("Stripe Shirt");
        Integer ans6 = searchProducts2.get(1).getProductID();
        assertEquals(ans5, ans6);

        //search by product category and price
        ArrayList<DummyProduct> searchProducts23 = client.Search("Product Category", "Tops", "100.0","150.0","1", "5");
        assertEquals(searchProducts2.size(),1);
        Integer ans7 = store.getProductID("Stripe Shirt");
        Integer ans8 = searchProducts2.get(0).getProductID();
        assertEquals(ans7, ans8);
    }

    @Test
    void showStoreProducts() {
        Integer founderID = client.Register("Or", "qwerty");
        Store store = new Store("Renuar", founderID);
        store.addNewProduct(founderID, "Simple Dress",120.0 ,"Dress");
        store.addNewProduct(founderID, "Evening Dress", 250.0, "Dress");
        ArrayList<DummyProduct> products= client.showStoreProducts(store.getId());
        assertEquals(products.size(), 2);
        Integer ans1 = store.getProductID("Simple Dress");
        Integer ans2 = products.get(0).getProductID();
        assertEquals(ans1, ans2);
        Integer ans3 = store.getProductID("Evening Dress");
        Integer ans4 = products.get(1).getProductID();
        assertEquals(ans3, ans4);
    }

    @Test
    void showAllStores() {
        Integer founderID = client.Register("Reut", "qwerty");
        Store store1 = new Store("Castro", founderID);
        Store store2 = new Store("Urbanica", founderID);
        Store store3 = new Store("Zara", founderID);
        ArrayList<DummyStore> stores = client.showAllStores();
        assertEquals(stores.size(), 3);
        Integer ans1 = store1.getId();
        Integer ans2 = stores.get(0).getId();
        assertEquals(ans1, ans2);
        Integer ans3 = store2.getId();
        Integer ans4 = stores.get(1).getId();
        assertEquals(ans3, ans4);
        Integer ans5 = store3.getId();
        Integer ans6 = stores.get(2).getId();
        assertEquals(ans5, ans6);
    }

    @Test
    void addProductToCart() {
        Integer founderID = client.Register("Hadas", "qwerty");
        Store store = new Store("Mania Jeans", founderID);
        store.addNewProduct(founderID, "Jeans Pants",120.0 ,"Pants");
        store.addProductToInventory(founderID,store.getProductID("Jeans Pants"), 10);
        client.addProductToCart(store.getId(),store.getProductID("Jeans Pants"), 3);
        assertEquals(client.showShoopingCart().size(), 1);
        Integer ans1 = client.showShoopingCart().get(0).getProductID();
        Integer ans2 = store.getProductID("Jeans Pants");
        assertEquals(ans1, ans2);
    }

    /*
    @Test
    void showShoopingCart() {
    }
     */

    @Test
    void openStore() {
        client.Register("Nofet", "qwerty");
        Boolean b1 = client.openStore("American Eagle");
        assertFalse(b1);   //maybe true?
        assertEquals(client.showAllStores().size(), 1);
        String ans1 = client.showAllStores().get(0).getName();
        assertEquals(ans1, "American Eagle");

        //Boolean b2 = client.openStore("American Eagle");
        //assertTrue(b2);    //maybe false

        client.Logout();
        client.Register("Hadar", "qwerty");
        //Boolean b3 = client.openStore("American Eagle");
        //assertTrue(b3);    //maybe false
    }

    @Test
    void showUserHistory() {
        
    }

    @Test
    void writeComment() {
        
    }

    @Test
    void addProduct() {
        
    }

    @Test
    void removeProduct() {
        
    }

    @Test
    void editProduct() {
        
    }

    @Test
    void showStoreHistory() {

    }
}
