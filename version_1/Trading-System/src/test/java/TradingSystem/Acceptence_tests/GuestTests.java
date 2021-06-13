package TradingSystem.Acceptence_tests;

import TradingSystem.Client.Client;
import TradingSystem.Client.ClientProxy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimitForProduct;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GuestTests {

    //ClientProxy client=new ClientProxy();
    Client client;
    Integer storeID;
    Integer productID1;

    @BeforeEach
    void setUp() {
        this.client = new Client();
        client.clearSystem();
        client.connectSystem();
        client.Register("Elinor", "123");
        client.Login("Elinor", "123");
        client.openStore("Adidas");
        storeID = client.getStoreIDByName("Adidas").returnStoreID();

        client.addProduct(storeID, "Simple Dress", "Dress", 120.0, 20);
        client.addProduct(storeID, "Evening Dress", "Dress", 250.0, 20);
        client.addProduct(storeID, "Jeans Dress", "Dress", 90.0, 50);
        client.addProduct(storeID, "Basic T-shirt", "Tops", 120.0, 50);
        client.addProduct(storeID, "Stripe Shirt", "Tops", 120.0, 50);

        productID1 = client.getProductIDByName("Simple Dress", storeID).returnProduct();
    }

    @AfterEach
    void tearDown() {
        client.exitSystem();
        client.clearSystem();
    }


    //region system Tests requirement 2.1-2.2
    /**
     * @requirement 2.1
     */
    @Test
    void connectionTest() {
        // setUp
        // this.client = new Client();
        assertNotEquals(this.client.getConnID(), "");
    }

    /**
     * @requirement 2.2
     */
    @Test
    void exitTest() {
        // Will work just without the tearDown!!!
//        client.exitSystem();
//        assertEquals(this.client.getConnID(), "");
    }

    //endregion
    //region Register Tests requirement 2.3

    /**
     * @requirement 2.3 register to the system
     */
    @Test
    void registerHappy() {
        int respondID1 = client.Register("Roee", "1234");
        assertTrue(respondID1 != -1 && !this.client.getConnID().equals(""));

        int respondID2 = client.Register("Dani", "qwerty");
        assertTrue(respondID2 != -1 && !this.client.getConnID().equals(""));
    }
    @Test
    void registerDuplicateUserName() {   //duplicate userName
        int respondID1 = client.Register("Avi", "qwerty");
        assertTrue(respondID1 != -1 && !this.client.getConnID().equals(""));
        int respondID2 = client.Register("Avi", "qqq");
        assertTrue(respondID2 == -1 && this.client.getConnID().equals(""));
    }

    //endregion
    //region Login Tests requirement 2.4

    /**
     * @requirement 2.4 login tests
     */
    @Test
    void loginHappy(){
        int guestID = client.Register("Yossi", "qwerty");
        String guestConnID = this.client.getConnID();
        int subscriberID = client.Login("Yossi", "qwerty").returnUserID();
        assertTrue(guestID == subscriberID && !guestConnID.equals(this.client.getConnID()));
    }
    @Test
    void loginIncorrectPassword(){
        client.Register("Yossi", "qwerty");
        int subscriberID = client.Login("Yossi", "qwe").returnUserID();
        assertTrue(subscriberID == -1 && this.client.getConnID().equals(""));
    }
    @Test
    void loginIncorrectUserName(){
        int respondID = client.Login("Eli", "qwerty").returnUserID();
        assertTrue(respondID == -1 && this.client.getConnID().equals(""));
    }

    //endregion
    //region Stores Tests: requirement 2.5
    /**
     * @requirement 2.5 show all stores, products in store
     */
    @Test
    void showAllStores_Happy() {
        Integer preSize = client.showAllStores().getStores().size();
        client.openStore("Adidason");
        Response response = client.showAllStores();
        Integer newSize = response.getStores().size();
        assertTrue(newSize == preSize+1);
    }
    @Test
    void showAllStoresSadNoStores() {
        //case: no stores at all
        List<DummyStore> stores1 = client.showAllStores().getStores();
        assertEquals(stores1.size(), 2);
    }
    @Test
    void showProductsOnSpecificStore() {
        List<DummyProduct> products= client.showStoreProducts(storeID).returnProductList();
        assertEquals(products.size(), 2);
    }
    //endregion
    //region Search Tests requirement 2.6

    /**
     * @requirement 2.6 search products
     */
    @Test
    void search_ProductName(){
        //2.6.1 Search by product name exist
        List<DummyProduct> searchProducts1 = client.Search("Product Name","Jeans Dress", "50","100","1","5").returnProductList();
        assertEquals(searchProducts1.size(),1);

        //2.6.2 Search by product name doesnt exist
        List<DummyProduct> searchNoProducts = client.Search("Product Name","blabla", "50","100","1","5").returnProductList();
        assertEquals(searchNoProducts.size(),0);
    }
    @Test
    void searchTest_ProductCategory() {
        //2.6.3 search by product category exist
        List<DummyProduct> searchProducts2 = client.Search("Product Category", "Tops", "30","150","1", "5").returnProductList();
        assertEquals(searchProducts2.size(),2);

        //2.6.4 search by product category exist
        List<DummyProduct> searchNoProducts = client.Search("Product Category", "blabla", "30","150","1", "5").returnProductList();
        assertEquals(searchNoProducts.size(),0);
    }
    @Test
    void searchTest_ProductCategoryAndPrice() {
        //2.6.5 search by product category and price
        List<DummyProduct> searchProducts3 = client.Search("Product Category", "Tops", "100","150","1", "5").returnProductList();
        assertEquals(searchProducts3.size(),2);
    }
    @Test
    void search_Sad_emptySearchList() {
        //2.6.6 sad search - there isn't products that match the search
        List<DummyProduct> searchProducts4 = client.Search("Product Category", "Tops", "150","200","1", "5").returnProductList();
        assertEquals(searchProducts4.size(),0);
    }

    //endregion
    //region Shopping Cart Tests requirement 2.7-2.8

    /**
     * @requirement  2.7
     */
    @Test
    void addProductToCart_Happy() {
        Response response = client.addProductToCart(storeID, productID1, 1);
        assertFalse(response.getIsErr());
        assertEquals(client.showShoppingCart().returnProductList().size(), 1);
        DummyProduct product = new DummyProduct((Map<String, Object>)client.showShoppingCart().returnProductList().get(0));
        String ans1 =  product.getProductName();
        assertEquals(ans1, "Simple Dress");
    }
    @Test
    void addProductToCart_SadQuantity() {
        Response response = client.addProductToCart(storeID, productID1, 26);
        assertTrue(response.getIsErr());
        assertEquals(client.showShoppingCart().returnProductList().size(), 0);
    }

    @Test
    void addProductToCart_SadStoreWithoutThisProduct() {
        Response response = client.addProductToCart(storeID, 10, 3);
        assertTrue(response.getIsErr());
        assertEquals(client.showShoppingCart().returnProductList().size(), 0);
    }
    @Test
    void addProductToCart_SadBuyingPolicy() {
        QuantityLimitForProduct exp = new QuantityLimitForProduct(2, productID1);
        client.addBuyingPolicy(storeID, exp);

        Response res = client.addProductToCart(storeID, productID1, 3);
        assertEquals(client.showShoppingCart().returnProductList().size(), 0);
        assertTrue(res.getIsErr());
    }
    @Test
    void NotAddSpacialProductToBag() {
        client.submissionBidding(storeID,productID1,3,1);
        client.ResponseForSubmissionBidding(storeID,productID1,client.getUserID(),3,1,1);
        Response r=client.addProductToCart(storeID,productID1,3);
        assertTrue(r.getIsErr());
    }

    /**
     * @requirement 2.8.1 show cart
     */
    @Test
    void showShoppingCart_Happy() {
        client.addProductToCart(storeID, productID1 , 3);
        List<DummyProduct> dummyProducts = client.showShoppingCart().returnProductList();
        assertEquals(dummyProducts.size(), 1); //types
        DummyProduct product = new DummyProduct((Map<String, Object>) dummyProducts.get(0));
        int quantity = product.getQuantity();
        assertEquals(quantity, 3); //quantity
    }
    @Test
    void showShoppingCart_Sad() {
        List<DummyProduct> ans1 = client.showShoppingCart().returnProductList();
        assertEquals(ans1.size(), 0);
    }


    /**
     * @requirement 2.8.2 remove from cart
     */
    @Test
    void removeFromShoppingCart_Happy() {
        // Prepare
        client.addProductToCart(storeID, productID1, 3);

        //Issue
        Response response = client.removeFromShoppingCart(storeID, productID1);

        //Assert
        assertFalse(response.getIsErr());
        Integer newSize = client.showShoppingCart().returnProductList().size();
        assertTrue(newSize == 0);
    }
    @Test
    void removeFromShoppingCart_Sad() {
        // Prepare
        client.addProductToCart(storeID, productID1, 3);

        //Issue
        Response response = client.removeFromShoppingCart(storeID, 8);

        //Assert
        assertTrue(response.getIsErr());
        Integer size = client.showShoppingCart().returnProductList().size();
        assertTrue(size == 1);
    }

    /**
     * @requirement 2.8.3 Edit cart
     */
    @Test
    void editShoppingCart_Happy() {
        // Prepare
        client.addProductToCart(storeID, productID1 , 3);

        //Issue
        Response response = client.editShoppingCart(storeID, productID1, 6);

        //Assert
        assertFalse(response.getIsErr());
        Integer size = client.showShoppingCart().returnProductList().size();
        assertTrue(size == 1); //types
        DummyProduct product = new DummyProduct((Map<String, Object>) client.showShoppingCart().returnProductList().get(0));
        Integer quantity = product.getQuantity();
        assertTrue(quantity == 6); //quantity
    }
    @Test
    void editShoppingCart_SadProductNoInStore() {
        // Prepare
        client.addProductToCart(storeID, productID1, 3);

        //Issue
        Response response = client.editShoppingCart(storeID, 7, 6);

        //Assert
        assertTrue(response.getIsErr());
        Integer size = client.showShoppingCart().returnProductList().size();
        assertTrue(size == 1); //types
        DummyProduct product = new DummyProduct((Map<String, Object>) client.showShoppingCart().returnProductList().get(0));
        Integer quantity = product.getQuantity();
        assertTrue(quantity == 3); //quantity
    }
    @Test
    void editShoppingCart_SadEmptyCart() {
        //Issue
        Response response = client.editShoppingCart(storeID, productID1, 6);

        //Assert
        assertTrue(response.getIsErr());
        List<DummyProduct> dummyProducts = client.showShoppingCart().returnProductList();
        assertEquals(dummyProducts.size(), 0); //types
    }
    @Test
    void editShoppingCart_SadPurchasePolicy() {
        //prepare
        QuantityLimitForProduct exp = new QuantityLimitForProduct(2, productID1);
        client.addBuyingPolicy(storeID, exp);
        client.addProductToCart(storeID, productID1, 1);

        //Issue
        Response res = client.editShoppingCart(storeID, productID1, 5);
        DummyProduct product = new DummyProduct((Map<String, Object>) client.showShoppingCart().returnProductList().get(0));
        assertEquals(product.getQuantity(), 1);
        assertTrue(res.getIsErr());
    }

    @Test
    void editShoppingCart_SadSpecialProduct()
    {
        client.submissionBidding(storeID,productID1,3,1);
        client.ResponseForSubmissionBidding(storeID,productID1,client.getUserID(),3,1,1);
        Response r=client.editShoppingCart(storeID,productID1,3);
        assertTrue(r.getIsErr());
    }



    //endregion
    //region Purchase tests requirement 2.9
    @Test
    void Purchase_Happy() {
        // Prepare
        client.Logout();
        client.connectSystem();
        client.addProductToCart(storeID, productID1, 1);
        DummyProduct product = new DummyProduct((Map<String, Object>)client.showStoreProducts(storeID).returnProductList().get(0));
        Integer preQuantity = product.getQuantity();

        //Issue
        Response response = client.guestPurchase("Roee","123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        assertFalse(response.getIsErr());
        List<DummyProduct> cartAfter = client.showShoppingCart().returnProductList();
        DummyProduct product2 = new DummyProduct((Map<String, Object>)client.showStoreProducts(storeID).returnProductList().get(0));

        //Assert
        assertEquals(cartAfter.size(), 0); //check cart is empty after purchase
        assertEquals(product2.getQuantity(), preQuantity-1);

        //Try to buy the same product again
        DummyProduct product3 = new DummyProduct((Map<String, Object>)client.showStoreProducts(storeID).returnProductList().get(0));
        preQuantity =product3.getQuantity();

        client.addProductToCart(storeID, productID1, 1);
        String productName = product3.getProductName();
        assertEquals(productName, "Simple Dress");

        //Issue
        response = client.guestPurchase("Roee", "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        assertFalse(response.getIsErr());
        cartAfter = client.showShoppingCart().returnProductList();
        DummyProduct product4 = new DummyProduct((Map<String, Object>)client.showStoreProducts(storeID).returnProductList().get(0));


        //Assert
        assertEquals(cartAfter.size(), 0); //check cart is empty after purchase
        assertEquals(product4.getQuantity(), preQuantity-1); //check decrease quantity in store
    }
    @Test
    void Purchase_SadWrongPayingDetails() {
        // Prepare
        client.Logout();
        client.connectSystem();
        client.addProductToCart(storeID, productID1, 1);

        //Issue
        Response response =client.guestPurchase("Roee", "123456789", "20","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");
        List<DummyProduct> productsInBag = client.showShoppingCart().returnProductList();

        //Assert
        assertTrue(response.getIsErr());
        assertEquals(productsInBag.size(), 1);
    }

    //endregion

}
