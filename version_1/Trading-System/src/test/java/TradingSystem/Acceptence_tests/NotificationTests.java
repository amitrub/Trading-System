package TradingSystem.Acceptence_tests;

import TradingSystem.Client.Client;
import TradingSystem.Client.Client_Driver;
import TradingSystem.Client.Client_Interface;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NotificationTests {

    //Client_Interface client = Client_Driver.getClient();
    Client client;
    Integer userID;
    Integer storeID;
    Integer productID;
    Integer userId2;

    @BeforeEach
    void setUp() {
        client = new Client();
        client.clearSystem();
        client.connectSystem();
        userID = client.Register("Elinor", "123");
        client.Login("Elinor", "123");
        Response response1 = client.openStore("Adidas");
        storeID = response1.returnStoreID();
        Response response2 = client.addProduct(storeID, "Sneakers", "Shoes", 250, 20);
        productID = (Integer) response2.getReturnObject().get("productID");
        client.Logout();

        userId2 = client.Register("Hadas", "123");
    }

    @AfterEach
    void tearDown() {
        client.clearSystem();
    }

    @Test
    void PurchaseAlert() {
        client.Login("Hadas", "123");
        client.addProductToCart(storeID, productID, 2);
        client.subscriberPurchase("123","2","2022","123","123456","Rager 101","Beer Sheva","Israel","123");
        client.Logout();

        //check that elinor has a message;
        Response response = client.Login("Elinor", "123");
        try {
            List<String> notConnectedMessages = (List<String>) response.getReturnObject().get("messages");
            assertEquals(notConnectedMessages.size(), 1);
        }
        catch (Exception e){
            System.out.println("Problem!");
        }
    }

    @Test
    void Add_and_RemoveOwnerAlert() {
        client.Login("Elinor", "123");
        client.addOwner(storeID, userId2);
        client.removeOwner(storeID, userId2);
        client.Logout();

        //check that hadas has messages;
        Response response = client.Login("Hadas", "123");
        try {
            List<String> notConnectedMessages = (List<String>) response.getReturnObject().get("messages");
            assertEquals(notConnectedMessages.size(), 2);
        }
        catch (Exception e){
            System.out.println("Problem!");
        }
    }

//    @Test
//    void WriteCommentAlert() {
//        client.Login("Hadas", "123");
//        client.addProductToCart(storeID, productID, 2);
//        client.subscriberPurchase("123","2","2022","123","123456","Rager 101","Beer Sheva","Israel","123");
//        client.writeComment(storeID, productID, 3.0, "Nice");
//        client.Logout();
//
//        //check that elinor has a messages;
//        Response response = client.Login("Elinor", "123");
//        try {
//            List<String> notConnectedMessages = (List<String>) response.getReturnObject().get("messages");
//            assertEquals(notConnectedMessages.size(), 2);
//        }
//        catch (Exception e){
//            System.out.println("Problem!");
//        }
//    }
}
