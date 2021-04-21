package TradingSystem;

import TradingSystem.Client.Client;
import TradingSystem.Server.DomainLayer.StoreComponent.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.DiscountPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class OwnerTests {

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



    //region requirement 4.1: Add Product Tests
    @Test
    void HappyAddProduct() {
        client.Register("Gal", "123");
        client.Login("Gal", "123");
        client.openStore("Scoop");
        Integer storeID = getStoreID(client.showAllStores(),"Scoop");

        //happy add
        boolean b1 = client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        assertEquals(storeProducts1.size(), 1);
        assertFalse(b1);
    }

    @Test
    void SadAddPrice() {
        client.Register("Lior", "123");
        client.Login("Lior", "123");
        client.openStore("Shoes");
        Integer storeID =  getStoreID(client.showAllStores(),"Shoes");

        //sad add - product price illegal
        boolean b2 = client.addProduct(storeID, "Classic Heels", "Heels", -50.0, 25);
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID);
        assertEquals(storeProducts2.size(), 0);
        assertTrue(b2);
    }

    @Test
    void SadAddNameTaken() {
        client.Register("Ori", "123");
        client.Login("Ori", "123");
        client.openStore("Ice Cube");
        Integer storeID = getStoreID(client.showAllStores(),"Ice Cube");
        client.addProduct(storeID, "Arma Heels", "Heels", 60.0, 25);

        boolean b3 = client.addProduct(storeID, "Arma Heels", "Heels", 200.0, 25);
        List<DummyProduct> storeProducts3 = client.showStoreProducts(storeID);
        assertEquals(storeProducts3.size(), 1);
        assertTrue(b3);
    }

    @Test
    void SadAddQuantity() {
        client.Register("Sapir", "123");
        client.Login("Sapir", "123");
        client.openStore("To-Go");
        Integer storeID = getStoreID(client.showAllStores(),"To-Go");

        //sad add - product quantity is illegal
        boolean b4 = client.addProduct(storeID, "Short Heels", "Heels", 60.0, -10);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        assertEquals(storeProducts1.size(), 0);
        assertTrue(b4);
    }

    //endregion
    //region requirement 4.1: Remove Product Tests
    @Test
    void HappyRemove() {
        client.Register("Oriya", "123");
        client.Login("Oriya", "123");
        client.openStore("Ran Sport");
        Integer storeID = getStoreID(client.showAllStores(), "Ran Sport");
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        Integer productID = getProductID(storeProducts1,"Arma Heels");
        Integer preSize = client.showStoreProducts(storeID).size();

        //happy remove
        boolean b1 = client.removeProduct(storeID, productID);
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID);
        assertEquals(storeProducts2.size(), preSize-1);
        assertFalse(b1);
    }

    @Test
    void BadRemove() {
        client.Register("Oriyan", "123");
        client.Login("Oriyaמ", "123");
        client.openStore("Mega Sport");
        Integer storeID = getStoreID(client.showAllStores(), "Mega Sport");
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        Integer productID = getProductID(storeProducts1,"Arma Heels");
        client.removeProduct(storeID, productID);
        Integer preSize = client.showStoreProducts(storeID).size();

        //bad remove - the product doesn't exist
        boolean b2 = client.removeProduct(storeID, productID);
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID);
        Integer newSize = storeProducts2.size();
        assertEquals(newSize, preSize);
        assertTrue(b2);
    }
    //endregion
    //region requirement 4.1: Edit Product Tests
    @Test
    void HappyEditPrice() {
        client.Register("Shani", "123");
        client.Login("Shani", "123");
        client.openStore("WeShoes");
        Integer storeID = getStoreID(client.showAllStores(), "WeShoes");
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        Integer productID = getProductID(storeProducts1,"Arma Heels");

        //happy edit price
        boolean b1 = client.editProduct(storeID, productID, "Arma Heels", "Heels", 100.0,25);
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID);
        assertEquals(storeProducts2.get(0).getPrice(), 100.0, 0);
        assertEquals(storeProducts2.size(), 1);
        assertFalse(b1);
    }

    @Test
    void HappyEditQuantity() {
        client.Register("Sha", "123");
        client.Login("Sha", "123");
        client.openStore("WeShoes");
        Integer storeID = getStoreID(client.showAllStores(), "WeShoes");
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        Integer productID = getProductID(storeProducts1,"Arma Heels");

        //happy edit quantity
        boolean b1 = client.editProduct(storeID, productID, "Arma Heels", "Heels", 80.0,35);
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID);
        assertEquals(storeProducts2.get(0).getQuantity(), 35);
        assertEquals(storeProducts2.size(), 1);
        assertFalse(b1);
    }

    @Test
    void SadEditPrice() {
        client.Register("Shira", "123");
        client.Login("Shira", "123");
        client.openStore("Gali");
        Integer storeID = getStoreID(client.showAllStores(), "Gali");
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        Integer productID = getProductID(storeProducts1,"Arma Heels");

        //sad edit
        boolean b2 = client.editProduct(storeID, productID, "Arma Heels", "Heels", -120.0,25);
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID);
        assertEquals(storeProducts2.get(0).getPrice(), 80.0, 0);
        assertEquals(storeProducts2.size(), 1);
        assertTrue(b2);
    }

    @Test
    void SadEditQuantity() {
        client.Register("Lin", "123");
        client.Login("Lin", "123");
        client.openStore("Gali");
        Integer storeID = getStoreID(client.showAllStores(), "Gali");
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        Integer productID = getProductID(storeProducts1,"Arma Heels");
        Integer preSize = client.showStoreProducts(storeID).size();

        //sad edit
        boolean b2 = client.editProduct(storeID, productID, "Arma Heels", "Heels", 120.0,-25);
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID);
        Integer newSize = storeProducts2.size();
        assertEquals(storeProducts2.get(0).getQuantity(), 25);
        assertEquals(newSize, preSize);
        assertTrue(b2);
    }

    @Test
    void SadEditNonExist() {
        client.Register("Din", "123");
        client.Login("Din", "123");
        client.openStore("Gali");
        Integer storeID = getStoreID(client.showAllStores(), "Gali");
        client.addProduct(storeID, "Arma Heels", "Heels", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        Integer productID = getProductID(storeProducts1,"Arma Heels");
        client.removeProduct(storeID, productID);
        Integer preSize = client.showStoreProducts(storeID).size();

        //sad edit
        boolean b2 = client.editProduct(storeID, productID, "Arma Heels", "Heels", 120.0,25);
        List<DummyProduct> storeProducts2 = client.showStoreProducts(storeID);
        Integer newSize = storeProducts2.size();
        assertEquals(newSize, preSize);
        assertTrue(b2);
    }

    //endregion

    //region requirement 4.2: Add Policies tests - version 2
    @Test
    void HappyAddBuyingPolicy() {
    }

    @Test
    void SadAddBuyingPolicy() {
    }

    @Test
    void HappyAddDiscountPolicy() {
    }

    @Test
    void SadAddDiscountPolicy() {
    }
    //endregion
    //region requirement 4.2: Edit Policies tests - version 2

    //endregion
    //region requirement 4.2: Get Policies information tests - version 2

    //endregion

    //region requirement 4.3: Add owner tests
    @Test
    void HappyAddOwner() {
        Integer newOwnerID = client.Register("nofet", "123");
        client.Login("nofet", "123");
        client.Logout();

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores(), "Store");

        boolean b1 = client.addOwner(storeID, newOwnerID);
        client.Logout();
        client.Login("nofet", "123");
        List<DummyStore> owners = client.showOwnerStores();
        assertFalse(b1);
        assertEquals(owners.size(), 1);
    }

    @Test
    void SadAddIsNotOwner() {
        client.Register("nofet", "123");
        client.Login("nofet", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores(), "Store");
        client.Logout();

        Integer id1 = client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.Logout();

        client.Register("roee", "123");
        client.Login("roee", "123");

        boolean b1 = client.addOwner(storeID, id1);
        client.Logout();
        client.Login("elinor", "123");
        List<DummyStore> owners = client.showOwnerStores();
        assertTrue(b1);
        assertEquals(owners.size(), 0);
    }

    @Test
    void SadAddIllegalIDOwner() {
        Integer id = client.Register("nofet", "123");
        client.Login("nofet", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores(), "Store");

        boolean b1 = client.addOwner(storeID, id+1);
        List<DummyStore> owners = client.showOwnerStores();
        assertTrue(b1);
        assertEquals(owners.size(), 1);
    }

    @Test
    void SadAddDoubleAppointmentOwner() {
        Integer newOwnerID = client.Register("nofet", "123");
        client.Login("nofet", "123");
        client.Logout();

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores(), "Store");

        client.addOwner(storeID, newOwnerID);
        boolean b1 = client.addOwner(storeID, newOwnerID);
        client.Logout();
        client.Login("nofet", "123");
        List<DummyStore> owners = client.showOwnerStores();
        assertTrue(b1);
        assertEquals(owners.size(), 1);
    }
    //endregion

    //region requirement 4.4: Remove owner tests - version 2
    @Test
    void HappyRemoveOwner() {
        /*
        String gust1 = tradingSystem.connectSystem().getConnID();
        int NofetId=tradingSystem.Register(gust1, "nofet", "123").getUserID();
        String NofetConnID=tradingSystem.Login(gust1, "nofet", "123").getConnID();
        tradingSystem.AddStore(NofetId, NofetConnID, "NofetStore");

        String gust2 = tradingSystem.connectSystem().getConnID();
        int ElinorId=tradingSystem.Register(gust2, "elinor", "123").getUserID();
        String ElinorConnID = tradingSystem.Login(gust2, "elinor", "123").getConnID();

        tradingSystem.AddNewManager(NofetId,NofetConnID,2,ElinorId);
        Response res= tradingSystem.RemoveManager(NofetId,NofetConnID,2,ElinorId);
        assertEquals(res.getMessage(), "The manager removed successfully");

         */
    }

    @Test
    void SadRemoveIsNotOwner() {
        /*
        String gust1 = tradingSystem.connectSystem().getConnID();
        int NofetId=tradingSystem.Register(gust1, "nofet", "123").getUserID();
        String NofetConnID=tradingSystem.Login(gust1, "nofet", "123").getConnID();
        tradingSystem.AddStore(NofetId, NofetConnID, "NofetStore");

        String gust2 = tradingSystem.connectSystem().getConnID();
        int ElinorId=tradingSystem.Register(gust2, "elinor", "123").getUserID();
        String ElinorConnID = tradingSystem.Login(gust2, "elinor", "123").getConnID();

        String gust3 = tradingSystem.connectSystem().getConnID();
        int RoeeId=tradingSystem.Register(gust3, "Roee", "123").getUserID();
        String RoeeConnID = tradingSystem.Login(gust3, "Roee", "123").getConnID();

        tradingSystem.AddNewOwner(NofetId,NofetConnID,2,ElinorId);
        tradingSystem.AddNewManager(NofetId,NofetConnID,2,RoeeId);

        Response res= tradingSystem.RemoveManager(ElinorId,ElinorConnID,2,RoeeId);
        assertEquals(res.getMessage(), "The user " + ElinorId + " is not the one who appointed the manager");
         */
    }
    //endregion

    //region requirement 4.5: Add manager tests
    @Test
    void HappyAddManager() {
        Integer newManagerID = client.Register("nofet", "123");
        client.Login("nofet", "123");
        client.Logout();

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores(), "Store");

        boolean b1 = client.addManager(storeID, newManagerID);
        assertFalse(b1);
       }

    @Test
    void SadAddIsNotManager() {
        client.Register("nofet", "123");
        client.Login("nofet", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores(), "Store");
        client.Logout();

        Integer id1 = client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.Logout();

        client.Register("roee", "123");
        client.Login("roee", "123");

        boolean b1 = client.addManager(storeID, id1);
        assertTrue(b1);
    }

    @Test
    void SadAddIllegalIDManager() {
        Integer id = client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores(), "Store");

        boolean b1 = client.addManager(storeID, id+1);
        assertTrue(b1);
    }

    @Test
    void SadAddDoubleAppointmentManager() {
        Integer id1 = client.Register("nofet", "123");
        client.Login("nofet", "123");
        client.Logout();

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores(), "Store");

        client.addManager(storeID, id1);
        boolean b1 = client.addManager(storeID, id1);
        assertTrue(b1);
    }

    //endregion

    //region requirement 4.6: Edit manager Permissions tests

    //endregion

    //region requirement 4.7: Remove manager tests
    @Test
    void HappyRemoveManager() {
        Integer newManagerID = client.Register("nofet", "123");
        client.Login("nofet", "123");
        client.Logout();

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores(), "Store");
        client.addManager(storeID, newManagerID);

        boolean b1 = client.removeManager(storeID, newManagerID);
        assertFalse(b1);
    }

    @Test
    void SadRemoveManagerNoPermission() {
        Integer newManagerID1 = client.Register("nofet", "123");
        client.Login("nofet", "123");
        client.Logout();

        Integer newManagerID2 = client.Register("roee", "123");
        client.Login("roee", "123");
        client.Logout();


        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores(), "Store");
        client.addManager(storeID, newManagerID1);
        client.addManager(storeID, newManagerID2);
        client.Logout();

        client.Login("roee", "123");
        boolean b1 = client.removeManager(storeID, newManagerID1);
        assertTrue(b1);
    }

    @Test
    void SadRemoveManagerNoManager() {
        Integer newManagerID = client.Register("nofet", "123");
        client.Login("nofet", "123");
        client.Logout();

        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Store");
        Integer storeID = getStoreID(client.showAllStores(), "Store");

        boolean b1 = client.removeManager(storeID, newManagerID);
        assertTrue(b1);
    }


    //endregion

    //region requirement 4.9: Information on officials tests
    @Test
    void HappyShowOfficialsInfo() {
    }

    @Test
    void SadShowOfficialsInfo() {
    }
    //endregion

    //region requirement 4.11: Store history tests
    @Test
    void HappyShowStoreHistory() {
        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Asos");
        Integer storeID = getStoreID(client.showAllStores(),"Asos");
        client.addProduct(storeID, "Sneakers", "Shoes", 80.0, 25);
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID);
        Integer productID = getProductID(storeProducts1,"Sneakers");
        client.Logout();

        client.Register("Yasmin", "123");
        client.Login("Yasmin", "123");
        client.addProductToCart(storeID, productID, 2);
        client.subscriberPurchase("123456789", "0501234567", "Kiryat Gat");
        client.Logout();

        client.Login("elinor", "123");
        List<DummyShoppingHistory> history = client.showStoreHistory(storeID);
        assertEquals(history.size(), 1);
    }

    @Test
    void SadShowStoreHistory() {
        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Asos");
        Integer storeID = getStoreID(client.showAllStores(),"Asos");
        client.addProduct(storeID, "Sneakers", "Shoes", 80.0, 25);

        List<DummyShoppingHistory> history = client.showStoreHistory(storeID);
        assertEquals(history.size(), 0);
    }
    //endregion

}
