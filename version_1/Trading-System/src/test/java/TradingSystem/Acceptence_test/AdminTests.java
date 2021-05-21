package TradingSystem.Acceptence_test;

import TradingSystem.Client.Client_Driver;
import TradingSystem.Client.Client_Interface;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingBag;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AdminTests {

    Client_Interface client= Client_Driver.getClient();
    Integer storeID;
    Integer userID;


    @BeforeEach
    public void setUp() {
    //    client = new Client();
        client.clearSystem();
        client.connectSystem();
        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        storeID = getStoreID(client.showAllStores().getStores(),"Store");
        client.addProduct(storeID, "Sneakers", "Shoes", 80.0, 25);
        client.addProduct(storeID, "Boots", "Shoes", 100.0, 20);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
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
        client.connectSystem();
        client.Login("amit", "qweasd");
        List<DummyShoppingHistory> history = client.adminStoreHistory(storeID).returnHistoryList();
        assertEquals(history.size(), 1);
    }

    @Test
    void HappyUserHistory() {
        client.connectSystem();
        client.Login("amit", "qweasd");
        List<DummyShoppingHistory> history = client.adminUserHistory(userID).returnHistoryList();
        assertEquals(history.size(), 1);
    }

    @Test
    void HappyAllStores() {
        client.connectSystem();
        client.Login("amit", "qweasd");
        List<DummyShoppingHistory> history = client.AdminAllStores().returnHistoryList();
        assertEquals(history.size(), 1);
    }

    @Test
    void HappyAllUsers() {
        client.connectSystem();
        client.Login("amit", "qweasd");
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
        client.Login("amit", "qweasd");
        List<DummyShoppingHistory> history = client.adminStoreHistory(storeID+1).returnHistoryList();
        assertEquals(history.size(), 0);
    }

    @Test
    void SadUserId() {
        client.Login("amit", "qweasd");
        List<DummyShoppingHistory> history = client.adminUserHistory(userID+1).returnHistoryList();
        assertEquals(history.size(), 0);
    }
    //endregion
/*
    // region requirement 6.6 : get daily income for system

    @Test
    void SadDailyIncomeForStore_StoreNotExistInTheSystem(){
        Response res= tradingSystemImpl.getDailyIncomeForStore(ElinorID,100,EconnID);
        Assertions.assertEquals(res.getMessage(),"getDailyIncomeForStore: The store " + 100 + " doesn't exist in the system");
    }

    @Test
    void HappyDailyIncomeForSystem(){
        String tmpConn= tradingSystemImpl.ConnectSystem().returnConnID();
        Response r1=tradingSystemImpl.Login(tmpConn,"amit","qweasd");

        Store tmp=tradingSystemImpl.stores.get(storeid);
        Product p1=new Product(NofetStore,"NofetStore",1,"1","1",2.0);
        Product p2=new Product(NofetStore,"NofetStore",2,"2","2",4.0);
        Product p3=new Product(NofetStore,"NofetStore",3,"3","3",13.0);
        Product p4=new Product(NofetStore,"NofetStore",4,"4","4",21.0);

        LinkedList<Product> PSH1=new LinkedList<>();
        PSH1.add(p1);
        PSH1.add(p2);
        PSH1.add(p3);

        ShoppingBag SB1=new ShoppingBag(1,NofetStore);
        SB1.setFinalPrice(20.0);

        ShoppingHistory SH1=new ShoppingHistory(SB1,PSH1);
        tmp.addHistory(SH1);

        Response r2=tradingSystemImpl.getDailyIncomeForSystem(r1.returnUserID(),r1.returnConnID());
        Double DailyIncome=(Double) r2.getReturnObject().get("DailyIncome");
        Assertions.assertEquals(DailyIncome, 70.0);
    }

    //endregion
*/
}

