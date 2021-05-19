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

    @BeforeEach
    void setUp() {
      //  this.client = new Client();
        client.clearSystem();
        client.connectSystem();
        client.Register("Elinor", "123");
        client.Login("Elinor", "123");
    }

    @AfterEach
    void tearDown() {
        client.exitSystem();
        client.clearSystem();
    }

    //region other functions
    Integer getStoreID(List<DummyStore> stores, String storename) {
        for (int i=0; i<stores.size(); i++)
        {
            if(stores.get(i).getName().equals(storename))
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

    //region requirement 3.1: logout Tests
    @Test
    void logoutHappy(){
        Response response = client.Logout();
        assertEquals(client.getUserID(), -1); //-1 means client is a guest in the system now
        assertFalse(response.getIsErr());
    }
    //endregion
    //region requirement 3.2: open store tests
    //case 3.2.1
    @Test
    void openStore_Happy() {
        Integer preSize = client.showAllStores().getStores().size();

        Response response = client.openStore("Mania");
        assertFalse(response.getIsErr());
        assertEquals(preSize+1, client.showAllStores().getStores().size());
    }
    //case 3.2.2
    @Test
    void openStore_SadDuplicateName() {
        client.openStore("Mania");
        Integer preSize = client.showAllStores().getStores().size();
        Response response = client.openStore("Mania");
        Integer newSize = client.showAllStores().getStores().size();
        assertTrue(response.getIsErr());
        assertEquals(preSize, newSize);
    }

    //case 3.2.3
    @Test
    void sad_openStoreNotRegistered() {
        client.Logout();
        Response response = client.openStore("American Eagle11");
        assertTrue(response.getIsErr());
    }
    //endregion
    //region requirement 3.3: Write Comment
    //case: 3.3.1
    @Test
    void writeComment() {
        // Prepare
        Integer storeID = setUpBeforePurchase();
        List<DummyProduct> products = client.showStoreProducts(storeID).returnProductList();
        Integer productID = getProductID(products, "Short Pants");
        client.addProductToCart(storeID, productID, 1);
        client.subscriberPurchase( "12345678", "052897878787", "sioot st. 5");

        //Issue
        Response response = client.writeComment(storeID, productID, 3, "The product is nice");
        assertFalse(response.getIsErr());
    }
    //case: 3.3.2, trying comment on product sub didn't buy
    @Test
    void sad_didntBuy_writeComment() {
        //prepare
        Integer storeID = setUpBeforePurchase();
        List<DummyProduct> products= client.showStoreProducts(storeID).returnProductList();
        Integer productID = getProductID(products, "Short Pants");

        //Issue
        Response response = client.writeComment(storeID, productID, 3, "The product is nice");
        assertTrue(response.getIsErr());
    }
    //endregion
    //region requirement 3.4: User Purchase

    Integer setUpBeforePurchase(){
        client.openStore("Adidas");
        List<DummyStore> stores = client.showAllStores().getStores();
        Integer storeID = getStoreID(stores, "Adidas");
        client.addProduct(storeID, "Short Pants", "Pants", 120.0, 2);
        return storeID;
    }

    //case 3.4.1 simple purchase
    @Test
    void Purchase_Happy() {
        // Prepare
        Integer storeID = setUpBeforePurchase();
        List<DummyProduct> products = client.showStoreProducts(storeID).returnProductList();
        Integer productID = getProductID(products, "Short Pants");

        client.addProductToCart(storeID, productID, 1);
        String ans1 = client.showShoppingCart().returnProductList().get(0).getProductName();
        assertEquals(ans1, "Short Pants");

        //Issue
        Integer preQuantity = client.showStoreProducts(storeID).returnProductList().get(0).getQuantity();
        Response response = client.subscriberPurchase( "12345678",
                "052897878787", "sioot st. 5");
        List<DummyProduct> cartAfter = client.showShoppingCart().returnProductList();
        List<DummyProduct> productsAfter = client.showStoreProducts(storeID).returnProductList();

        //Assert
        assertFalse(response.getIsErr());
        assertEquals(cartAfter.size(), 0); //check cart is empty after purchase
        assertEquals(productsAfter.get(0).getQuantity(), preQuantity - 1); //check decrease quantity in store
    }

    //case 3.4.2 input doesn't fit
    @Test
    void Purchase_Sad() {
        // Prepare
        Integer storeID = setUpBeforePurchase();
        List<DummyProduct> products = client.showStoreProducts(storeID).returnProductList();
        Integer productID = products.get(0).getProductID();

        client.addProductToCart(storeID, productID, 1);
        String ans1 = client.showShoppingCart().returnProductList().get(0).getProductName();
        assertEquals(ans1, "Short Pants");

        //Issue, not valid credit number and phone number
        Response response = client.subscriberPurchase( "123456", "0534550335", "sioot st. 5");

        //Assert
        assertTrue(response.getIsErr());
    }
    // endregion
    //region requirement 3.7: User History Tests
    //case 3.7.1 have purchases
    @Test
    void showUsersHistory_Happy() {
        Integer storeID = setUpBeforePurchase();
        List<DummyProduct> products = client.showStoreProducts(storeID).returnProductList();
        Integer productID = getProductID(products, "Short Pants");
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
        setUpBeforePurchase();

        Response response = client.showUserHistory();
        assertTrue(response.getIsErr());
    }
    //endregion



}
