package TradingSystem;

import TradingSystem.Client.Client;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;


import java.util.List;

public class AdminTests {

    Client client;
    Integer storeID;
    Integer userID;


    @BeforeEach
    void setUp() {
        client = new Client();
        client.clearSystem();
        client.connectSystem();

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        storeID = getStoreID(client.showAllStores(),"Store");
        client.addProduct(storeID, "Sneakers", "Shoes", 80.0, 25);
        client.addProduct(storeID, "Boots", "Shoes", 100.0, 20);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        Integer productID1= getProductID(storeProducts1, "Sneakers");
        client.Logout();

        userID = client.Register("Reut", "123");
        client.Login("Reut", "123");
        client.addProductToCart(storeID, productID1, 5);
        client.subscriberPurchase("123456789", "0521111111", "Tel Aviv");
        client.Logout();
    }
    @AfterEach
    void tearDown() {
        client.exitSystem();
    }

    //region other functions
    Integer getStoreID(List<DummyStore> stores, String storeName)
    {
        for (int i=0; i<stores.size(); i++)
        {
            if(stores.get(i).getName().equals(storeName))
                return stores.get(i).getId();
        }
        return -1;
    }

    Integer getProductID(List<DummyProduct> storeProducts, String productName)
    {
        for (int i=0; i<storeProducts.size(); i++)
        {
            if(storeProducts.get(i).getProductName().equals(productName))
                return storeProducts.get(i).getProductID();
        }
        return -1;
    }
    //endregion

    //region requirement 6.4: Purchase history
    @Test
    void HappyStoreHistory() {
        client.Login("amit", "qweasd");
        List<DummyShoppingHistory> history = client.adminStoreHistory(storeID);
        assertEquals(history.size(), 1);
    }

    @Test
    void HappyUserHistory() {
        client.Login("amit", "qweasd");
        List<DummyShoppingHistory> history = client.adminUserHistory(userID);
        assertEquals(history.size(), 1);
    }

    @Test
    void HappyAllStores() {
        client.Login("amit", "qweasd");
        List<DummyShoppingHistory> history = client.AdminAllStores();
        assertEquals(history.size(), 1);
    }

    @Test
    void HappyAllUsers() {
        client.Login("amit", "qweasd");
        List<DummyShoppingHistory> history = client.AdminAllUsers();
        assertEquals(history.size(), 1);
    }

    @Test
    void SadAdminUser() {
        client.Login("elinor", "123");
        List<DummyShoppingHistory> history = client.adminStoreHistory(storeID);
        assertEquals(history.size(), 0);
    }

    @Test
    void SadStoreId() {
        client.Login("amit", "qweasd");
        List<DummyShoppingHistory> history = client.adminStoreHistory(storeID+1);
        assertEquals(history.size(), 0);
    }

    @Test
    void SadUserId() {
        client.Login("amit", "qweasd");
        List<DummyShoppingHistory> history = client.adminUserHistory(userID+1);
        assertEquals(history.size(), 0);
    }
    //endregion
}
