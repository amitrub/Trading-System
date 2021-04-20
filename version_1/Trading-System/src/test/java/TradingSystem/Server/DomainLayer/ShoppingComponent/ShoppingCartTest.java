package TradingSystem.Server.DomainLayer.ShoppingComponent;

import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.NewResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    @Test
    void addProductToBag() {

        TradingSystem t=TradingSystem.getInstance();
        String gust1=t.ConnectSystem().getConnID();
        t.Register(gust1, "nofet", "123");
        String NconnID = t.Login(gust1, "nofet", "123").getConnID();
        t.AddStore(3, NconnID, "NofetStore");
        t.AddProductToStore(3, NconnID, 2, "computer", "Technology", 3000.0,20);
        t.AddProductToStore(3, NconnID, 2, "Bag", "Beauty", 100.0,50);
        t.AddProductToStore(3, NconnID, 2, "Bed", "Fun", 4000.0,30);

        ShoppingCart SC=new ShoppingCart(3);
        NewResponse res1=SC.addProductToBag(2,1,10);
        NewResponse res2=SC.addProductToBag(2,2,60);
        NewResponse res3=SC.addProductToBag(2,7,10);
        NewResponse res4=SC.addProductToBag(2,3,10);

        //happy
        assertTrue(res1.getMessage().equals("The product added successfully"));
        //sad_productNotInTheStock
        assertTrue(res2.getMessage().equals("The product or quantity is not in stock"));
        //sad_storeNotSellTheProduct
        assertTrue(res3.getMessage().equals("The product or quantity is not in stock"));
       /*TODO
        //sad_againstTheStorePolicy
        assertTrue(res4.getMessage().equals("Adding the product is against the store policy"));
        */

    }
}