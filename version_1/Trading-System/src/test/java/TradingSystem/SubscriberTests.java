package TradingSystem;

import TradingSystem.Client.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class SubscriberTests {

    Client client;


    @BeforeEach
    void setUp() {
        client = new Client();
        client.connectSystem();
    }
    @AfterEach
    void tearDown() {
        client.exitSystem();
    }

    //region logout Tests
    @Test
    void logoutHappy(){
        client.Register("Gal", "123");
        client.Login("Gal", "123");
        int respondID = client.Logout();
        assertEquals(respondID, -1);
    }
    //endregion
    //region Add Store Tests
    @Test
    void openStore() {
        client.Register("Lior", "123");
        client.Login("Lior", "123");
        boolean b1 = client.openStore("American Eagle");
        assertFalse(b1);
        assertEquals(client.showAllStores().size(), 1);

        //sad add - duplicate store name
        boolean b2 = client.openStore("American Eagle");
        assertTrue(b2);

        //different clients can open store with the same name?
        client.Logout();
        client.Register("Fani", "123");
        client.Login("Fani", "123");
        boolean b3 = client.openStore("American Eagle");
        assertTrue(b3);
    }
    //endregion
    //region User History Tests


    //todo after implement purchase
    @Test
    void showAllUsersHistory() {

    }
    //endregion
    //region Write Comment


    /*
    //todo there isn't access to the comments
    @Test
    void writeComment() {
        client.Login("Sapir", "123");
        client.openStore("Fox");
        ArrayList<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Short Pants", "Pants", 120.0, 2);
        ArrayList<DummyProduct> products= client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();
        client.writeComment(storeID, productID, 3, "The product is nice");

    }
    //endregion

     */

}
