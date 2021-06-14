package TradingSystem.Acceptence_tests;

import TradingSystem.Client.Client;
import TradingSystem.Client.Client_Driver;
import TradingSystem.Client.Client_Interface;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubscriberTests {

    //Client_Interface client = Client_Driver.getClient();
    Client client = new Client();
    Integer storeID;


    @BeforeEach
    void setUp() {
        client.clearSystem();
        client.connectSystem();
        client.Register("Nofet","123");

        client.Register("Elinor", "123");
        client.Login("Elinor", "123");
        client.openStore("Store");
        storeID = client.getStoreIDByName("Store").returnStoreID();
        client.addProduct(storeID,"prod","product",20.0,10);
    }

    @AfterEach
    void tearDown() {
        client.exitSystem();
        client.clearSystem();
    }


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
        client.Logout();
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
        client.Logout();
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
    //TODO
    /*
    @Test
    void writeComment() {
        client.Login("Nofet","123");
        client.addProductToCart(storeID, 1, 1);
        client.subscriberPurchase( "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        //Issue
        Response response = client.writeComment(storeID, 1, 3, "The product is nice");
        assertFalse(response.getIsErr());
        client.Logout();
    }
     */
    //case: 3.3.2, trying comment on product sub didn't buy
    @Test
    void sad_didntBuy_writeComment() {
        //Issue
        client.Login("Nofet", "123");
        Response response = client.writeComment(storeID, 2, 3, "The product is nice");
        assertTrue(response.getIsErr());
        client.Logout();
    }
    //endregion
    //region requirement 3.4: User Purchase
//TODO
/*
    //case 3.4.1 simple purchase
    @Test
    void Purchase_Happy() {
        client.Login("Nofet","123");
        client.addProductToCart(storeID, 1, 1);
        List ans1 = client.showShoppingCart().returnProductList();
        assertEquals(ans1.size(), 1);

        //Issue
        DummyProduct product1 = new DummyProduct((Map<String, Object>)client.showStoreProducts(storeID).returnProductList().get(0));
        Integer preQuantity = product1.getQuantity();
        Response response = client.subscriberPurchase( "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        List<DummyProduct> cartAfter = client.showShoppingCart().returnProductList();
        List<DummyProduct> productsAfter = client.showStoreProducts(storeID).returnProductList();
        DummyProduct product = new DummyProduct((Map<String, Object>)productsAfter.get(0));
        //Assert
        assertFalse(response.getIsErr());
        assertEquals(cartAfter.size(), 0); //check cart is empty after purchase
        assertEquals(product.getQuantity(), preQuantity - 1); //check decrease quantity in store
        client.Logout();
    }
 */

    //case 3.4.2 input doesn't fit
    @Test
    void Purchase_Sad() {
        // Prepare
        client.Login("Nofet","123");
        client.addProductToCart(storeID, 1, 1);

        //Issue, not valid month
        Response response = client.subscriberPurchase( "123456789", "20","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        //Assert
        assertTrue(response.getIsErr());
        client.Logout();
    }
    // endregion
    //region requirement 3.7: User History Tests
    /*
    //case 3.7.1 have purchases
    @Test
    void showUsersHistory_Happy() {
        client.Login("Nofet","123");
        client.addProductToCart(storeID, 1, 1);
        client.subscriberPurchase( "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        client.addProductToCart(storeID, 1, 1);
        client.subscriberPurchase( "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        client.Login("Nofet","123");
        Response response = client.showUserHistory();
        assertFalse(response.getIsErr());
        assertEquals(response.returnHistoryList().size(), 2);
    }
     */
    //case 3.7.2 no history for this user
    @Test
    void showUserHistory_Sad_NoHistory() {
        client.connectSystem();
        client.Register("Avi","123");
        client.Login("Avi","123");
        Response response = client.showUserHistory();
        assertTrue(response.getIsErr());
        client.Logout();
    }
    //endregion

    //region requirement 3.8
    // Subscriber Bidding
    /*
    @Test
    void HappySubscriberBidding() {
        client.Login("Nofet","123");
        Response r8 = client.submissionBidding( storeID, 1, 1, 3);
        assertFalse(r8.getIsErr());
        client.Logout();
    }
     */
    @Test
    void SadSubscriberBidding_unsubscribe() {
        client.Logout();
        Response r = client.submissionBidding(-1, 1, 1, 3);
        assertTrue(r.getIsErr());
    }

    @Test
    void SadSubscriberBidding_storeNotExist() {
        client.Login("Nofet","123");
        Response r = client.submissionBidding(-1, 1, 1, 3);
        assertTrue(r.getIsErr());
        client.Logout();
    }

    @Test
    void SadSubscriberBidding_productNotExist() {
        client.Login("Nofet","123");
        Response r = client.submissionBidding(storeID, -1, 1, 3);
        assertTrue(r.getIsErr());
        client.Logout();
    }

    @Test
    void SadSubscriberBidding_productInTheCartAlready() {
        client.Login("Nofet","123");
        client.addProductToCart(storeID, 1, 3);
        Response r = client.submissionBidding(storeID, 1, 1, 3);
        client.Logout();
    }

    @Test
    void SadSubscriberBidding_priceNotInRange() {
        client.Login("Nofet","123");
        Response r1 = client.submissionBidding(storeID, 1, 1, -3);
        assertTrue(r1.getIsErr());
        Response r2 = client.submissionBidding(storeID, 1, 1, 17);
        client.Logout();
    }

    @Test
    void SadSubscriberBidding_NegativeQuantity() {
        client.Login("Nofet","123");
        Response r1 = client.submissionBidding(storeID, 1, -1, 3);
        assertTrue(r1.getIsErr());
        client.Logout();
    }

    @Test
    void SadSubscriberBidding_BidAlreadyExist() {
        client.Login("Nofet","123");
        client.submissionBidding(storeID, 1, 1, 3);
        Response r1 = client.submissionBidding(storeID, 1, 1, 3);
        assertTrue(r1.getIsErr());
        client.Logout();
    }

    @Test
    void SadProductAlreadyInCart() {
        client.Login("Nofet","123");
        client.addProductToCart(storeID, 1, 3);
        Response r =client.submissionBidding(storeID, 1, 1, 3);
        assertTrue(r.getIsErr());
        client.Logout();
    }

    @Test
    void SadRemoveSpecialProduct() {
        client.Login("Nofet","123");
        client.addProductToCart(storeID, 1, 3);
        Response r =client.submissionBidding(storeID, 1, 1, 3);
        assertTrue(r.getIsErr());
        client.Logout();
    }
    //endregion

}
