package TradingSystem.Server.DomainLayer.ShoppingComponent;


import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.Test;

import java.util.List;



import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.ConcurrentHashMap;
import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {
  
      @BeforeEach
        void setUp() {
        }  


    @Test
    void showShoppingCart() {
        TradingSystem t=TradingSystem.getInstance();
        String gust1=t.ConnectSystem().getConnID();
        t.Register(gust1, "nofet", "123");
        String NconnID = t.Login(gust1, "nofet", "123").getConnID();
        t.AddStore(1, NconnID, "Store1");

        t.AddProductToStore(1, NconnID, 1, "computer", "Technology", 3000.0,20);
        t.AddProductToStore(1, NconnID, 1, "Bag", "Beauty", 100.0,50);
        t.AddProductToStore(1, NconnID, 1, "Bed", "Fun", 4500.0,30);

        t.AddStore(1, NconnID, "Store2");
        t.AddProductToStore(1, NconnID, 2, "computer", "Technology", 3500.0,20);
        t.AddProductToStore(1, NconnID, 2, "Bag", "Beauty", 50.0,50);
        t.AddProductToStore(1, NconnID, 2, "Bed", "Fun", 4000.0,30);

        ShoppingCart SC1=new ShoppingCart(1);
        SC1.addProductToBag(1,1,3);
        SC1.addProductToBag(2,2,2);
        SC1.addProductToBag(2,3,1);
        List<DummyProduct> L1= SC1.ShowShoppingCart();

        ShoppingCart SC2=new ShoppingCart(1);
        List<DummyProduct> L2= SC2.ShowShoppingCart();

        //happy
        assertTrue(L1.size()==3);
        for (DummyProduct DP:L1
             ) {
            if(DP.getProductID()==1){
                assertTrue(DP.getProductName().equals("computer")&&DP.getCategory().equals("Technology")&&DP.getStoreName().equals("Store1")&&DP.getPrice()==3000.0&&DP.getQuantity()==3);
            }
            if(DP.getProductID()==2){
                assertTrue(DP.getProductName().equals("Bag")&&DP.getCategory().equals("Beauty")&&DP.getStoreName().equals("Store2")&&DP.getPrice()==50.0&&DP.getQuantity()==2);
            }
            if(DP.getProductID()==3){
                assertTrue(DP.getProductName().equals("Bed")&&DP.getCategory().equals("Fun")&&DP.getStoreName().equals("Store2")&&DP.getPrice()==4000.0&&DP.getQuantity()==1);
            }
        }
        //sad_cartIsEmpty
        assertTrue(L2.isEmpty());
}
        

    @Test
    void addProductToBag() {
        t.AddStore(3, NconnID, "NofetStore");
        t.AddProductToStore(3, NconnID, 2, "computer", "Technology", 3000.0,20);
        t.AddProductToStore(3, NconnID, 2, "Bag", "Beauty", 100.0,50);
        t.AddProductToStore(3, NconnID, 2, "Bed", "Fun", 4000.0,30);

        ShoppingCart SC=new ShoppingCart(3);
        Response res1=SC.addProductToBag(2,1,10);
        Response res2=SC.addProductToBag(2,2,60);
        Response res3=SC.addProductToBag(2,7,10);
        Response res4=SC.addProductToBag(2,3,10);

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
}