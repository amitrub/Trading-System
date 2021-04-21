package TradingSystem;

import TradingSystem.Client.Client;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class SubscriberTests {

    Client client;
    TradingSystem tradingSystem = TradingSystem.getInstance();

    @BeforeEach
    void setUp() {
        this.client = new Client();
        client.clearSystem();
        client.connectSystem();
    }

    @AfterEach
    void tearDown() {
        client.exitSystem();
        client.clearSystem();
    }

    //region requirement 3.1: logout Tests
    @Test
    void logoutHappy(){
        client.Register("Gal", "123");
        client.Login("Gal", "123");
        Response respone = client.Logout();
        assertEquals(client.getUserID(), -1); //-1 means client is a guest in the system now
        assertFalse(respone.getIsErr());
    }
    //endregion
    //region open store tests
    @Test
    void openStore_Happy() {
        client.Register("Lee", "123");
        client.Login("Lee", "123");
        Integer preSize = client.showAllStores().size();

        boolean b1 = client.openStore("Mania1");
        assertFalse(b1);
        assertEquals(preSize+1, client.showAllStores().size());
    }

    @Test
    void openStore_SadDuplicateName() {
        client.Register("Lin", "123");
        client.Login("Lin", "123");
        //Integer preSize = client.showAllStores().size();

        client.openStore("Mania2");
        boolean b1 = client.openStore("Mania2");
        //Integer newSize = client.showAllStores().size();
        assertTrue(b1);
        //assertEquals(preSize, newSize);
    }

    //    @Test
//    void openStore() {
//        client.Register("Lior", "123");
//        client.Login("Lior", "123");
//        boolean b1 = client.openStore("American Eagle11");
//        assertFalse(b1);
//
//        //sad add - duplicate store name
//        boolean b2 = client.openStore("American Eagle122");
//        assertTrue(b2);
//    }
    //endregion

    /*
    //region User History Tests
    
    //todo after implement purchase
    @Test
    void showUsersHistory_Happy() {

    }

    @Test
    void showUserHistory_Sad() {
    }

    //endregion
     */
    /*
    //region Write Comment

    //endregion
     */
    
    

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
