package TradingSystem.Server.DomainLayer.ShoppingComponent;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.NewResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    @Test
    void removeProductFromCart() {
        TradingSystem t=TradingSystem.getInstance();
        String gust1=t.ConnectSystem().getConnID();
        t.Register(gust1, "nofet", "123");
        String NconnID = t.Login(gust1, "nofet", "123").getConnID();

        t.AddStore(1, NconnID, "Store1");

        t.AddProductToStore(1, NconnID, 1, "computer", "Technology", 3000.0,20);
        t.AddProductToStore(1, NconnID, 1, "Bag", "Beauty", 100.0,50);
        t.AddProductToStore(1, NconnID, 1, "Bed", "Fun", 4500.0,30);


        ShoppingCart SC1=new ShoppingCart(1);
        SC1.addProductToBag(1,1,3);
        SC1.addProductToBag(2,2,2);
        SC1.addProductToBag(2,3,1);
        NewResponse res1= SC1.RemoveProductFromCart(1,1);
        NewResponse res2= SC1.RemoveProductFromCart(1,1);
        NewResponse res3= SC1.RemoveProductFromCart(1,4);

        //happy
        assertTrue(res1.getMessage().equals("product remove successfully"));
        //sad_productNotInTheCart
        assertTrue(res2.getMessage().equals("product that does not exist in the cart cannot be removed"));
        //sad_productNotInTheStore
        assertTrue(res3.getMessage().equals("product that does not exist in the cart cannot be removed"));



    }
}