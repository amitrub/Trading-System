package User;

import TradingSystem.Client.Client;
import TradingSystem.Server.DomainLayer.StoreComponent.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.DiscountPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class UserTests {

    Client client;


    @BeforeEach
    void setUp() {
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
        int respondID2 = client.Login("Yossi", "qwerty");
        assertEquals(respondID1, respondID2);
        assertTrue(client.isLogin());
    }

    @Test
    void loginIncorrectPassword(){
        client.Register("Shai", "qwerty");
        int respondID = client.Login("Shai", "qwe");
        assertEquals(respondID, -1);
    }

    @Test
    void loginIncorrectUserName(){
        client.Register("Elinor", "qwerty");
        int respondID = client.Login("Eli", "qwerty");
        assertEquals(respondID, -1);
    }

    @Test
    void logoutHappy(){
        client.Register("Shir", "qwerty");
        int respondID = client.Logout();
        assertEquals(respondID, -1);
        assertFalse(client.isLogin());
    }

    @Test
    void searchTest(){
        int respondID1 = client.Register("Dana", "qwerty");
        client.Login("Dana", "qwerty");
        DiscountPolicy DP = new DiscountPolicy();
        BuyingPolicy BP = new BuyingPolicy();
        Store store = new Store("Pull&Beer", respondID1, DP, BP);
        store.addNewProduct(respondID1, "T-shirt", 150.0, "Basic");
        store.addNewProduct(respondID1, "skirt", 100.0, "Sport");
        store.addNewProduct(respondID1, "pants", 140.5, "Sport");


        ArrayList<DummyProduct> searchProducts1 = client.Search("Product Category","145", "200", "1", "5");
        assertEquals(searchProducts1.size(), 1);

        ArrayList<DummyProduct> searchProducts2 = client.Search("Product Name", "0", "150", "1", "5");
        assertEquals(searchProducts2.size(), 3);

    }

}
