package TradingSystem.Server.DomainLayer.ShoppingComponent;



import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
class ShoppingCartTest {

    @Autowired
    TradingSystemImplRubin tradingSystem;

    String guest1= tradingSystem.ConnectSystem().returnConnID();
    String NconnID;
    int NuserId ,storeID1,storeID2;
    ShoppingCart SC1;
    ShoppingCart SC2;


    @BeforeEach
    void setUp() {
        tradingSystem.Register(guest1, "nofet", "123");
        Response res= tradingSystem.Login(guest1, "nofet", "123");
        NconnID= res.returnConnID();
        NuserId=res.returnUserID();
        SC1=new ShoppingCart(NuserId);
        SC2=new ShoppingCart(NuserId);
        tradingSystem.AddStore(NuserId, NconnID, "NofetStore1");
        tradingSystem.AddStore(NuserId, NconnID, "NofetStore2");
        for(Store store: tradingSystem.stores.values()){
            if(store.getName().equals("NofetStore1")){
                storeID1=store.getId();
            }
            if(store.getName().equals("NofetStore2")){
                storeID2=store.getId();
            }
        }
        tradingSystem.AddProductToStore(NuserId, NconnID, storeID1, "computer", "Technology", 3000.0,20);
        tradingSystem.AddProductToStore(NuserId, NconnID, storeID1, "Bag", "Beauty", 100.0,50);
        tradingSystem.AddProductToStore(NuserId, NconnID, storeID1, "Bed", "Fun", 4500.0,30);


        tradingSystem.AddProductToStore(NuserId, NconnID, storeID2, "computer", "Technology", 3500.0,20);
        tradingSystem.AddProductToStore(NuserId, NconnID, storeID2, "Bag", "Beauty", 50.0,50);
        tradingSystem.AddProductToStore(NuserId, NconnID, storeID2, "Bed", "Fun", 4000.0,30);
    }

    //requirement 2.7
    @Test
    void addProductToBag() {
        Store Nstore = tradingSystem.stores.get(storeID1);
        Integer productID1 = Nstore.getProductID("computer");
        Integer productID2 = Nstore.getProductID("Bag");
        Response res1=SC1.addProductToBag(storeID1,productID1,10, true);
        Response res2=SC1.addProductToBag(storeID1,productID2,60, true);
        Response res3=SC1.addProductToBag(storeID1,7,10, true);
        //QuantityLimitForProduct exp = new QuantityLimitForProduct(10, productID1);
        //tradingSystem.addBuyingPolicy(NuserId, NconnID, storeID2, exp);
        //Response res4=SC1.addProductToBag(storeID2,productID1,12);

        //happy
        assertFalse(res1.getIsErr());

        //sad_productNotInTheStock
        assertTrue(res2.getIsErr());

        //sad_storeNotSellTheProduct
        assertTrue(res3.getIsErr());

        //sad_againstTheStorePolicy
        //assertTrue(res4.getIsErr());

    }

    //requirement 2.8
    @Test
    void editProductQuantityFromCart() {
        Store Nstore = tradingSystem.stores.get(storeID1);
        Integer productID1 = Nstore.getProductID("computer");
        Integer productID2 = Nstore.getProductID("Bag");
        Integer productID3 = Nstore.getProductID("Bed");
        Response res0 = SC1.editProductQuantityFromCart(storeID1,productID1,5);
        SC1.addProductToBag(storeID1,productID1,3, true);
        SC1.addProductToBag(storeID1,productID2,2, true);
        Response res1 = SC1.editProductQuantityFromCart(storeID1,productID1,5);
        Response res2 = SC1.editProductQuantityFromCart(storeID1,productID3,1);
        //QuantityLimitForProduct exp = new QuantityLimitForProduct(10, productID1);
        //tradingSystem.addBuyingPolicy(NuserId, NconnID, storeID2, exp);
        //Response res5 = SC1.editProductQuantityFromCart(storeID1,productID1,15);

        //happy
        assertFalse(res1.getIsErr());

        //sad_shoppingCartEmpty
        assertTrue(res0.getIsErr());

        //sad_productNotInTheCart
        assertTrue(res2.getIsErr());

        //sad_productAgainstThePolicy
        //assertTrue(res5.getIsErr());
    }

    //requirement 2.8
    @Test
    void removeProductFromCart() {
        SC1.addProductToBag(storeID1,1,3, true);
        SC1.addProductToBag(storeID2,2,2, true);
        SC1.addProductToBag(storeID2,3,1, true);

        Response res1= SC1.RemoveProductFromCart(storeID1,1);
        Response res2= SC1.RemoveProductFromCart(storeID2,1);
        Response res3= SC1.RemoveProductFromCart(storeID2,4);

        //happy
    //    assertTrue(res1.getMessage().equals("product remove successfully"));
        //sad_productNotInTheCart
//        assertTrue(res2.getMessage().equals("product that does not exist in the cart cannot be removed"));
        //sad_productNotInTheStore
    //    assertTrue(res3.getMessage().equals("product that does not exist in the cart cannot be removed"));

    }

    //requirement 2.8
    @Test
    void showShoppingCart() {
        SC1.addProductToBag(storeID1,1,3, true);
        SC1.addProductToBag(storeID2,2,2, true);
        SC1.addProductToBag(storeID2,3,1, true);

        List<DummyProduct> L1= SC1.ShowShoppingCart();

        List<DummyProduct> L2= SC2.ShowShoppingCart();

        //happy
        assertTrue(L1.size()==3);
        for (DummyProduct DP:L1
             ) {
            if(DP.getProductID()==1){
                assertTrue(DP.getProductName().equals("computer")&&DP.getCategory().equals("Technology")&&DP.getStoreName().equals("NofetStore1")&&DP.getPrice()==3000.0&&DP.getQuantity()==3);
            }
            if(DP.getProductID()==2){
                assertTrue(DP.getProductName().equals("Bag")&&DP.getCategory().equals("Beauty")&&DP.getStoreName().equals("NofetStore2")&&DP.getPrice()==50.0&&DP.getQuantity()==2);
            }
            if(DP.getProductID()==3){
                assertTrue(DP.getProductName().equals("Bed")&&DP.getCategory().equals("Fun")&&DP.getStoreName().equals("NofetStore2")&&DP.getPrice()==4000.0&&DP.getQuantity()==1);
            }
        }
        //sad_cartIsEmpty
        assertTrue(L2.isEmpty());
}

    //requirement 2.8
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

}