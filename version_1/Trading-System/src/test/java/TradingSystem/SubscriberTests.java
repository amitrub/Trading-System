package TradingSystem;

import TradingSystem.Client.Client;
import TradingSystem.Server.DomainLayer.StoreComponent.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.DiscountPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class SubscriberTests {

    Client client;
    Integer ownerID;
    Store store;
    Integer client1ID;
    Integer client2ID;
    Integer client3ID;
    Integer client4ID;
    Integer client5ID;
    Integer client6ID;
    Integer client7ID;


    @BeforeEach
    void setUp() {
        client = new Client();
        ownerID = client.Register("Gal", "1234");
        //client.openStore("Factory54");
        DiscountPolicy DP = new DiscountPolicy();
        BuyingPolicy BP = new BuyingPolicy();
        store = new Store("Factory54", ownerID, DP, BP);

        client1ID = client.Register("Lior", "123");
        client2ID = client.Register("Sapir", "123");
        client3ID = client.Register("Roni", "123");
        client4ID = client.Register("Efrat", "123");
        client5ID = client.Register("Yasmin", "123");
        client6ID = client.Register("Eden", "123");
        client7ID = client.Register("Sharon", "123");
    }
    @AfterEach
    void tearDown() {
        client.exitSystem();
    }

    //region logout Tests
    @Test
    void logoutHappy(){
        client.Register("Shir", "qwerty");
        int respondID = client.Logout();
        assertEquals(respondID, -1);
    }
    //endregion
    //region Add Store Tests
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
    //endregion
    //region User History Tests
    @Test
    void showAllUsersHistory() {

    }
    //endregion
    //region Write Comment
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
    //endregion

}
