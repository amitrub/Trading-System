package TradingSystem.Acceptence_test;

import TradingSystem.Client.Client_Driver;
import TradingSystem.Client.Client_Interface;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.*;

public class SubscriberTests {

    Client_Interface client = Client_Driver.getClient();
    TradingSystemImpl tradingSystemImpl = TradingSystemImpl.getInstance();

    @BeforeEach
    void setUp() {
      //  this.client = new Client();
        client.clearSystem();
        client.connectSystem();
    }

    @AfterEach
    void tearDown() {
        client.exitSystem();
        client.clearSystem();
    }

    Integer getStoreID(List<DummyStore> stores, String storename)
    {
        for (int i=0; i<stores.size(); i++)
        {
            if(stores.get(i).getName().equals(storename))
                return stores.get(i).getId();
        }
        return -1;
    }

    //region requirement 3.1: logout Tests
    @Test
    void logoutHappy(){
        client.connectSystem();
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
        // Prepare
        client.Register("Hadas", "123");
        client.Login("Hadas", "123");
        String store_name = "Mania Jeans";
        client.openStore(store_name);
        List<DummyStore> stores = client.showAllStores();
        Integer storeID = getStoreID(stores, store_name);
        client.addProduct(storeID, "Short Pants", "Pants", 120.0, 2);
        List<DummyProduct> products = client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();
        client.addProductToCart(storeID, productID, 1);

        client.subscriberPurchase( "12345678", "052897878787", "sioot st. 5");
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
    //region requirement 3.4: User Purchase
    //case 3.4.1 simple purchase
    @Test
    void Purchase_Happy() {
        // Prepare
        client.Register("Hadas", "123");
        client.Login("Hadas", "123");
        String store_name = "Mania Jeans";
        client.openStore(store_name);
        List<DummyStore> stores = client.showAllStores();
        Integer storeID = getStoreID(stores, store_name);
        client.addProduct(storeID, "Short Pants", "Pants", 120.0, 2);
        List<DummyProduct> products = client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();
//        client.Logout();
        client.addProductToCart(storeID, productID, 1);
        String ans1 = client.showShoppingCart().get(0).getProductName();
        assertEquals(ans1, "Short Pants");

        //Issue
        boolean purchaseFailed = client.subscriberPurchase( "12345678",
                "052897878787", "sioot st. 5");
        List<DummyProduct> cartAfter = client.showShoppingCart();
        List<DummyProduct> productsAfter = client.showStoreProducts(storeID);
        DummyProduct shortPants = products.get(0);
        DummyProduct shortPantsAfter = productsAfter.get(0);

        //Assert
        if(!purchaseFailed)
            System.out.println("purchase Succeed");
        assertFalse(purchaseFailed);
        assertEquals(cartAfter.size(), 0); //check cart is empty after purchase
        assertEquals(shortPantsAfter.getQuantity(), shortPants.getQuantity() - 1); //check decrease quantity in store
    }

    //case 3.4.2 input doesn't fit
    @Test
    void Purchase_Sad() {
        // Prepare
        client.Register("Hadas", "123");
        client.Login("Hadas", "123");
        String store_name = "Mania Jeans";
        client.openStore(store_name);
        List<DummyStore> stores = client.showAllStores();
        Integer storeID = getStoreID(stores, store_name);
        client.addProduct(storeID, "Short Pants", "Pants", 120.0, 2);
        List<DummyProduct> products = client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();
//        client.Logout();
        client.addProductToCart(storeID, productID, 1);
        String ans1 = client.showShoppingCart().get(0).getProductName();
        assertEquals(ans1, "Short Pants");

//        //Issue, not valid credit number and phone number
//        boolean purchaseFailed = client.subscriberPurchase( "1", "7", "sioot st. 5");
//
//        //Assert
//        assertTrue(purchaseFailed);
    }
    // endregion
    //region requirement 3.7: User History Tests
    //case 3.7.1 have parchuses
    @Test
    void showUsersHistory_Happy() {
        client.Register("Hadas", "123");
        client.Login("Hadas", "123");
        String store_name = "Mania Jeans";
        client.openStore(store_name);
        List<DummyStore> stores = client.showAllStores();
        Integer storeID = getStoreID(stores, store_name);
        client.addProduct(storeID, "Short Pants", "Pants", 120.0, 2);
        List<DummyProduct> products = client.showStoreProducts(storeID);
        Integer productID = products.get(0).getProductID();
        client.addProductToCart(storeID, productID, 1);
        client.subscriberPurchase( "12345678", "052897878787", "sioot st. 5");

        Response response = client.showUserHistory();
        assertFalse(response.getIsErr());
        assertEquals(response.returnHistoryList().size(), 1);
    }
    //case 3.7.2 no history for this user
    @Test
    void showUserHistory_Sad_NoHistory() {
        // Prepare
        client.Register("Hadas", "123");
        client.Login("Hadas", "123");
        String store_name = "Mania Jeans";
        client.openStore(store_name);
        List<DummyStore> stores = client.showAllStores();
        Integer storeID = getStoreID(stores, store_name);
        client.addProduct(storeID, "Short Pants", "Pants", 120.0, 2);

        Response response = client.showUserHistory();
        assertTrue(response.getIsErr());
    }
    //endregion

    //TODO: user purchase need to add to history



}
