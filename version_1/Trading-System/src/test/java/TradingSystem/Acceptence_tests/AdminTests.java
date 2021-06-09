package TradingSystem.Acceptence_tests;

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
        client.subscriberPurchase("123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
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

    // region requirement 6.6 : get daily income for system
    @Test
    void HappyDailyIncomeForSystem(){
       //AddPurchace?
        client.clearSystem();
        client.connectSystem();
        client.Login("amit", "qweasd");
        Response r2=client.AdminDailyIncomeForSystem();
        Double DailyIncome=(Double) r2.getReturnObject().get("DailyIncome");
        Assertions.assertEquals(DailyIncome, 0);
    }

    @Test
    void SadNotAdmin(){
        Response r2=client.AdminDailyIncomeForSystem();
        Assertions.assertTrue(r2.getIsErr());
    }

    //endregion



}
/*
client.openStore("Tmp1");
        DummyStore store1=null;
        for (DummyStore s:client.showAllStores().getStores()
        ) {
            if (s.getName().equals("Tmp1")){
                store1=s;
            }
        }
        if(store1==null){
            store1=client.showAllStores().getStores().get(0);
        }
        Product p1=new Product(store1.getId(),"Tmp1",1,"1","1",2.0);
        Product p2=new Product(store1.getId(),"Tmp1",2,"2","2",4.0);
        Product p3=new Product(store1.getId(),"Tmp1",3,"3","3",13.0);

        LinkedList<Product> PSH1=new LinkedList<>();
        PSH1.add(p1);
        PSH1.add(p2);
        PSH1.add(p3);
        ShoppingBag SB1=new ShoppingBag(1,store1.getId());
        SB1.setFinalPrice(17.0);

        client.clearSystem();
        client.connectSystem();
        client.Login("amit", "qweasd");
        client.openStore("Tmp2");
        DummyStore store2=null;
        for (DummyStore s:client.showAllStores().getStores()
        ) {
            if (s.getName().equals("Tmp2")){
                store2=s;
            }
        }
        if(store2==null){
            store2=client.showAllStores().getStores().get(0);
        }
        Product ps2=new Product(store2.getId(),"Tmp2",2,"2","2",4.0);
        Product ps3=new Product(store2.getId(),"Tmp2",3,"3","3",13.0);
        Product ps4=new Product(store2.getId(),"Tmp2",4,"4","4",21.0);

        LinkedList<Product> PSH2=new LinkedList<>();
        PSH1.add(ps2);
        PSH1.add(ps3);
        PSH1.add(ps4);

        ShoppingBag SB2=new ShoppingBag(2,store2.getId());
        SB2.setFinalPrice(33.0);
        ShoppingHistory SH1=new ShoppingHistory(SB1,PSH1);
        ShoppingHistory SH2=new ShoppingHistory(SB2,PSH2);

 */
