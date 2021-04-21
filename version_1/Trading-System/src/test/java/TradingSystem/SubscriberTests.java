package TradingSystem;

import TradingSystem.Client.Client;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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
    //region requirement 3.2: open store tests
    //case 3.2.1
    @Test
    void openStore_Happy() {
        client.Register("Lee", "123");
        client.Login("Lee", "123");
        Integer preSize = client.showAllStores().size();

        Response response = client.openStore("Mania1");
        assertFalse(response.getIsErr());
        assertEquals(preSize+1, client.showAllStores().size());
    }
    //case 3.2.2
    @Test
    void openStore_SadDuplicateName() {
        client.Register("Lin", "123");
        client.Login("Lin", "123");
        //Integer preSize = client.showAllStores().size();

        client.openStore("Mania2");
        Response response = client.openStore("Mania2");
        assertTrue(response.getIsErr());
    }

    //case 3.2.3
        @Test
    void sad_openStoreNotRegistered() {
        client.Register("Lior", "123");
//        client.Login("Lior", "123");
        Response response = client.openStore("American Eagle11");
        assertTrue(response.getIsErr());
    }
    //endregion
    //region requirement 3.3: Write Comment
    //case: 3.3.1
    @Test
    void writeComment() {
        client.Register("Sapir", "123");
        client.Login("Sapir", "123");
        client.openStore("Fox");
        List<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Short Pants", "Pants", 120.0, 2);
        List<DummyProduct> products= client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();
       //TODO: to buy this product before trying to comment it
        Response response = client.writeComment(storeID, productID, 3, "The product is nice");
        assertFalse(response.getIsErr());
    }
    //case: 3.3.2, trying comment on product sub didn't buy
    @Test
    void sad_didntBuy_writeComment() {
        client.Register("Sapir", "123");
        client.Login("Sapir", "123");
        client.openStore("Fox");
        List<DummyStore> store = client.showAllStores();
        Integer storeID = store.get(0).getId();
        client.addProduct(storeID, "Short Pants", "Pants", 120.0, 2);
        List<DummyProduct> products= client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();
        Response response = client.writeComment(storeID, productID, 3, "The product is nice");
        assertTrue(response.getIsErr());
    }
    //endregion
    //region requirement 3.7: User History Tests

    //todo after implement purchase
    @Test
    void showUsersHistory_Happy() {

    }

    @Test
    void showUserHistory_Sad() {
    }
    //endregion

}
