package TradingSystem.Acceptence_tests;

import TradingSystem.Client.Client;
import TradingSystem.Client.Client_Driver;
import TradingSystem.Client.Client_Interface;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AdminTests {

    //Client_Interface client= Client_Driver.getClient();
    Client client = new Client();
    Integer storeID;
    Integer userID;


    @BeforeEach
    public void setUp() {
        client.clearSystem();
        client.connectSystem();
        userID = client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        storeID = client.getStoreIDByName("Store").returnStoreID();
        client.addProduct(storeID, "Sneakers", "Shoes", 80.0, 25);
        client.addProduct(storeID, "Boots", "Shoes", 100.0, 20);
        Integer productID1 = client.getProductIDByName("Sneakers", storeID).returnProduct();
        client.Logout();

        client.Register("Nofet", "123");
        Response res = client.Login("Nofet", "123");
        client.addProductToCart(storeID, 1, 5);
        client.subscriberPurchase("123456789", "4", "2022", "123", "123456789", "Rager 101", "Beer Sheva", "Israel", "8458527");

        client.Login("amit", "qweasd");
    }

    @AfterEach
    void tearDown() {
        client.clearSystem();
    }


    //region requirement 6.4: Purchase history
    @Test
    void HappyStoreHistory() {
        List<DummyShoppingHistory> history = client.adminStoreHistory(storeID).returnHistoryList();
        assertEquals(history.size(), 1);
    }

    @Test
    void HappyUserHistory() {
        //client.Login("amit", "qweasd");
        List<DummyShoppingHistory> history = client.adminUserHistory(userID).returnHistoryList();
        assertEquals(history.size(), 1);
    }

    @Test
    void HappyAllStores() {
        //client.Login("amit", "qweasd");
        List<DummyShoppingHistory> history = client.AdminAllStores().returnHistoryList();
        assertEquals(history.size(), 1);
    }

    @Test
    void HappyAllUsers() {
        //client.Login("amit", "qweasd");
        List<DummyShoppingHistory> history = client.AdminAllUsers().returnHistoryList();
        assertEquals(history.size(), 1);
    }

    @Test
    void SadAdminUser() {
        client.Login("elinor", "123");
        List<DummyShoppingHistory> history = client.adminStoreHistory(storeID).returnHistoryList();
        assertEquals(history.size(), 0);
    }

    @Test
    void SadStoreId() {
        //client.Login("amit", "qweasd");
        List<DummyShoppingHistory> history = client.adminStoreHistory(storeID + 1).returnHistoryList();
        assertEquals(history.size(), 0);
    }

    @Test
    void SadUserId() {
        //client.Login("amit", "qweasd");
        List<DummyShoppingHistory> history = client.adminUserHistory(userID + 1).returnHistoryList();
        assertEquals(history.size(), 0);
    }
    //endregion

    // region requirement 6.6 : get daily income for system
    @Test
    void HappyDailyIncomeForSystem() {
        //client.Login("amit", "qweasd");
        Response r2 = client.AdminDailyIncomeForSystem();
        Integer DailyIncome = (Integer) r2.getReturnObject().get("DailyIncome");
        Assertions.assertEquals(DailyIncome, 0);
    }

    @Test
    void SadNotAdmin() {
        client.Login("Elinor", "123");
        Response r2 = client.AdminDailyIncomeForSystem();
        Assertions.assertTrue(r2.getIsErr());
    }

    //endregion
}
