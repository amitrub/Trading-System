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
    Client client;

    @BeforeEach
    void setUp() {
        client = new Client();
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
        Integer productID = client.getProductIDByName("Short Pants", storeID).returnProduct();;
        client.addProductToCart(storeID, productID, 1);
        client.subscriberPurchase( "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        //Issue
        Response response = client.writeComment(storeID, productID, 3, "The product is nice");
        assertFalse(response.getIsErr());
    }
    //case: 3.3.2, trying comment on product sub didn't buy
    @Test
    void sad_didntBuy_writeComment() {
        //prepare
        Integer storeID = setUpBeforePurchase();
        Integer productID = client.getProductIDByName("Short Pants", storeID).returnProduct();

        //Issue
        Response response = client.writeComment(storeID, productID, 3, "The product is nice");
        assertTrue(response.getIsErr());
    }
    //endregion
    //region requirement 3.4: User Purchase

    Integer setUpBeforePurchase(){
        client.openStore("Adidas");
        Integer storeID = client.getStoreIDByName("Adidas").returnStoreID();
        client.addProduct(storeID, "Short Pants", "Pants", 120.0, 2);
        return storeID;
    }

    //case 3.4.1 simple purchase
    @Test
    void Purchase_Happy() {
        // Prepare
        Integer storeID = setUpBeforePurchase();
        Integer productID = client.getProductIDByName("Short Pants", storeID).returnProduct();

        client.addProductToCart(storeID, productID, 1);
        String ans1 = client.showShoppingCart().returnProductList().get(0).getProductName();
        assertEquals(ans1, "Short Pants");

        //Issue
        Integer preQuantity = client.showStoreProducts(storeID).returnProductList().get(0).getQuantity();
        Response response = client.subscriberPurchase( "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        List<DummyProduct> cartAfter = client.showShoppingCart().returnProductList();
        List<DummyProduct> productsAfter = client.showStoreProducts(storeID).returnProductList();
        DummyProduct product = new DummyProduct((Map<String, Object>)productsAfter.get(0));
        //Assert
        assertFalse(response.getIsErr());
        assertEquals(cartAfter.size(), 0); //check cart is empty after purchase
        assertEquals(product.getQuantity(), preQuantity - 1); //check decrease quantity in store
    }

    //case 3.4.2 input doesn't fit
    @Test
    void Purchase_Sad() {
        // Prepare
        Integer storeID = setUpBeforePurchase();
        Integer productID = client.getProductIDByName("Short Pants", storeID).returnProduct();;

        client.addProductToCart(storeID, productID, 1);
        String ans1 = client.showShoppingCart().returnProductList().get(0).getProductName();
        assertEquals(ans1, "Short Pants");

        //Issue, not valid month
        Response response = client.subscriberPurchase( "123456789", "20","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        //Assert
        assertTrue(response.getIsErr());
    }
    // endregion
    //region requirement 3.7: User History Tests
    //case 3.7.1 have purchases
    @Test
    void showUsersHistory_Happy() {
        Integer storeID = setUpBeforePurchase();
        Integer productID = client.getProductIDByName("Short Pants", storeID).returnProduct();
        client.addProductToCart(storeID, productID, 1);
        client.subscriberPurchase( "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        client.addProductToCart(storeID, productID, 1);
        client.subscriberPurchase( "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        Response response = client.showUserHistory();
        assertFalse(response.getIsErr());
        assertEquals(response.returnHistoryList().size(), 2);
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

    //region requirement 3.8
    // Subscriber Bidding
    @Test
    void HappySubscriberBidding() {
        client.openStore("deme6");
        DummyStore store=null;
        for (DummyStore s:client.showAllStores().getStores()
        ) {
            if (s.getName().equals("deme6")){
                store=s;
            }
        }
        if(store==null){
            store=client.showAllStores().getStores().get(0);
        }
        Integer storeID=store.getId();
        client.addProduct(storeID, "1", "1", 10, 20);
        client.addProduct(storeID, "2", "1", 7, 20);
        Response r8 = client.submissionBidding( storeID, 1, 1, 3);
        assertFalse(r8.getIsErr());
        System.out.println(r8.getMessage());
    }

    @Test
    void SadSubscriberBidding_unsubscribe() {
        client.Logout();
        Response r = client.submissionBidding(-1, 1, 1, 3);
        assertTrue(r.getIsErr());
        System.out.println(r.getMessage());
    }

    @Test
    void SadSubscriberBidding_storeNotExist() {
        Response r = client.submissionBidding(-1, 1, 1, 3);
        assertTrue(r.getIsErr());
        System.out.println(r.getMessage());
    }

    @Test
    void SadSubscriberBidding_productNotExist() {
        client.openStore("deme1");
        DummyStore store=null;
        for (DummyStore s:client.showAllStores().getStores()
        ) {
            if (s.getName().equals("deme1")){
                store=s;
            }
        }
        if(store==null){
            store=client.showAllStores().getStores().get(0);
        }
        Integer storeID=store.getId();
        Response r = client.submissionBidding(storeID, -1, 1, 3);
        assertTrue(r.getIsErr());
        System.out.println(r.getMessage());
    }

    @Test
    void SadSubscriberBidding_productInTheCartAlready() {
        client.openStore("deme2");
        DummyStore store=null;
        for (DummyStore s:client.showAllStores().getStores()
        ) {
            if (s.getName().equals("deme2")){
                store=s;
            }
        }
        if(store==null){
            store=client.showAllStores().getStores().get(0);
        }
        Integer storeID=store.getId();
        client.addProduct(storeID, "1", "1", 10, 20);
        client.addProduct(storeID, "2", "1", 7, 20);
        client.addProductToCart(storeID, 1, 3);
        Response r = client.submissionBidding(storeID, 1, 1, 3);
        assertTrue(r.getIsErr());
        System.out.println(r.getMessage());
    }

    @Test
    void SadSubscriberBidding_priceNotInRange() {
        client.openStore("deme3");
        DummyStore store=null;
        for (DummyStore s:client.showAllStores().getStores()
        ) {
            if (s.getName().equals("deme3")){
                store=s;
            }
        }
        if(store==null){
            store=client.showAllStores().getStores().get(0);
        }
        Integer storeID=store.getId();
        client.addProduct(storeID, "1", "1", 10, 20);
        client.addProduct(storeID, "2", "1", 7, 20);
        Response r1 = client.submissionBidding(storeID, 1, 1, -3);
        assertTrue(r1.getIsErr());
        System.out.println(r1.getMessage());
        Response r2 = client.submissionBidding(storeID, 1, 1, 17);
        assertTrue(r2.getIsErr());
        System.out.println(r2.getMessage());
    }

    @Test
    void SadSubscriberBidding_NegativeQuantity() {
        client.openStore("deme4");
        DummyStore store=null;
        for (DummyStore s:client.showAllStores().getStores()
             ) {
            if (s.getName().equals("deme4")){
                store=s;
            }
        }
        if(store==null){
            store=client.showAllStores().getStores().get(0);
        }
        Integer storeID=store.getId();
        client.addProduct(storeID, "1", "1", 10, 20);
        client.addProduct(storeID, "2", "1", 7, 20);
        Response r1 = client.submissionBidding(storeID, 1, -1, 3);
        assertTrue(r1.getIsErr());
        System.out.println(r1.getMessage());
    }

    @Test
    void SadSubscriberBidding_BidAlreadyExist() {
        client.openStore("deme5");
        DummyStore store=null;
        for (DummyStore s:client.showAllStores().getStores()
        ) {
            if (s.getName().equals("deme5")){
                store=s;
            }
        }
        if(store==null){
            store=client.showAllStores().getStores().get(0);
        }
        Integer storeID=store.getId();
        client.addProduct(storeID, "1", "1", 10, 20);
        client.addProduct(storeID, "2", "1", 7, 20);
        Response r2=client.submissionBidding(storeID, 1, 1, 3);
        Response r1 = client.submissionBidding(storeID, 1, 1, 3);
        assertTrue(!r2.getIsErr()&&r1.getIsErr());
        System.out.println(r1.getMessage());
    }

    @Test
    void SadProductAlreadyInCart() {
        client.openStore("deme9");
        DummyStore store=null;
        for (DummyStore s:client.showAllStores().getStores()
        ) {
            if (s.getName().equals("deme9")){
                store=s;
            }
        }
        if(store==null){
            store=client.showAllStores().getStores().get(0);
        }
        Integer storeID=store.getId();
        client.addProduct(storeID, "1", "1", 10, 20);
        client.addProductToCart(storeID, 1, 3);
        Response r =client.submissionBidding(storeID, 1, 1, 3);
        assertTrue(r.getIsErr());
        System.out.println(r.getMessage());
    }

    @Test
    void SadRemoveSpecialProduct() {
        client.openStore("deme2");
        DummyStore store=null;
        for (DummyStore s:client.showAllStores().getStores()
        ) {
            if (s.getName().equals("deme2")){
                store=s;
            }
        }
        if(store==null){
            store=client.showAllStores().getStores().get(0);
        }
        Integer storeID=store.getId();
        client.addProduct(storeID, "1", "1", 10, 20);
        client.addProductToCart(storeID, 1, 3);
        Response r =client.submissionBidding(storeID, 1, 1, 3);
        assertTrue(r.getIsErr());
        System.out.println(r.getMessage());
    }
    //endregion

    //region requirement 4.12

    //endregion

    //region requirement 6.6

    //endregion

}
