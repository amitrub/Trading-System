package TradingSystem.Server.DomainLayer.ShoppingComponent;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void mergeToMyCart() {
        ConcurrentHashMap<Integer, ShoppingBag> shoppingBagsList1 = new ConcurrentHashMap<>();
        ShoppingBag shoppingBag = new ShoppingBag(1,1);
        ShoppingBag shoppingBag2 = new ShoppingBag(1,2);
        shoppingBagsList1.put(1, shoppingBag);
        shoppingBagsList1.put(2, shoppingBag2);
        ShoppingCart shoppingCart1 = new ShoppingCart(1, shoppingBagsList1);

        ConcurrentHashMap<Integer, ShoppingBag> shoppingBagsList2 = new ConcurrentHashMap<>();
        ShoppingBag shoppingBag3 = new ShoppingBag(2,3);
        ShoppingBag shoppingBag4 = new ShoppingBag(2,4);
        shoppingBagsList1.put(3, shoppingBag3);
        shoppingBagsList1.put(4, shoppingBag3);
        ShoppingCart shoppingCart2 = new ShoppingCart(2, shoppingBagsList2);

        shoppingCart1.mergeToMyCart(shoppingCart2);
        assertEquals(shoppingCart1.getShoppingBags().size(),4);
    }

    @Test
    void purchase() {
//        TODO
    }

    @Test
    void editProductQuantityFromCart() {
        TradingSystem t=TradingSystem.getInstance();
        String gust1=t.ConnectSystem().getConnID();
        t.Register(gust1, "nofet", "123");
        String NconnID = t.Login(gust1, "nofet", "123").getConnID();

        t.AddStore(1, NconnID, "Store1");

        t.AddProductToStore(1, NconnID, 1, "computer", "Technology", 3000.0,20);
        t.AddProductToStore(1, NconnID, 1, "Bag", "Beauty", 100.0,50);
        t.AddProductToStore(1, NconnID, 1, "Bed", "Fun", 4500.0,30);


        ShoppingCart SC1=new ShoppingCart(1);
        Response res0= SC1.editProductQuantityFromCart(1,1,5);
        SC1.addProductToBag(1,1,3);
        SC1.addProductToBag(1,2,2);
        Response res1= SC1.editProductQuantityFromCart(1,1,5);
        Response res2= SC1.editProductQuantityFromCart(1,2,60);
        Response res3= SC1.editProductQuantityFromCart(2,2,60);
        Response res4= SC1.editProductQuantityFromCart(1,3,60);
        //Response res5= SC1.editProductQuantityFromCart(1,3,60);

        //happy
        assertTrue(res1.getMessage().equals("The quantity of the product update successfully"));
        //sad_shoppingCartEmpty
        assertTrue(res0.getMessage().equals("The shoppingCart empty, cannot be edited"));
        //sad_productNotInTheCart
        assertTrue(res2.getMessage().equals("The product isn't in the stock, so it cannot be edited"));
        //sad_productNotInTheCart
        assertTrue(res3.getMessage().equals("The product isn't in the shoppingCart, so it cannot be edited"));
        //sad_productNotInTheStore
        assertTrue(res4.getMessage().equals("The product isn't in the shoppingCart, so it cannot be edited"));
        /*
        //sad_productAgainstThePolicy
        assertTrue(res5.getMessage().equals("The quantity of the product is against tha store policy, so it cannot be edited"));
        */
    }
}