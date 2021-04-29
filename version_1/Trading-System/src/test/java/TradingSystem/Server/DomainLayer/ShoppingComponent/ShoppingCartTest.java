package TradingSystem.Server.DomainLayer.ShoppingComponent;



import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.Test;

import java.util.List;


import org.junit.jupiter.api.BeforeEach;

import java.util.concurrent.ConcurrentHashMap;
import static org.junit.jupiter.api.Assertions.*;

class ShoppingCartTest {

    TradingSystemImpl t= TradingSystemImpl.getInstance();
    String gust1=t.ConnectSystem().returnConnID();
    String NconnID;
    int NuserId ,storeID1,storeID2;
    ShoppingCart SC1;
    ShoppingCart SC2;

    @BeforeEach
    void setUp() {
        t.Register(gust1, "nofet", "123");
        Response res=t.Login(gust1, "nofet", "123");
        NconnID= res.returnConnID();
        NuserId=res.returnUserID();
        SC1=new ShoppingCart(NuserId);
        SC2=new ShoppingCart(NuserId);
        t.AddStore(NuserId, NconnID, "NofetStore1");
        t.AddStore(NuserId, NconnID, "NofetStore2");
        for(Store store: t.stores.values()){
            if(store.getName().equals("NofetStore1")){
                storeID1=store.getId();
            }
            if(store.getName().equals("NofetStore2")){
                storeID2=store.getId();
            }
        }
        t.AddProductToStore(NuserId, NconnID, storeID1, "computer", "Technology", 3000.0,20);
        t.AddProductToStore(NuserId, NconnID, storeID1, "Bag", "Beauty", 100.0,50);
        t.AddProductToStore(NuserId, NconnID, storeID1, "Bed", "Fun", 4500.0,30);


        t.AddProductToStore(NuserId, NconnID, storeID2, "computer", "Technology", 3500.0,20);
        t.AddProductToStore(NuserId, NconnID, storeID2, "Bag", "Beauty", 50.0,50);
        t.AddProductToStore(NuserId, NconnID, storeID2, "Bed", "Fun", 4000.0,30);
    }

    @Test
    void addProductToBag() {
        Response res1=SC1.addProductToBag(storeID1,1,10);
        Response res2=SC1.addProductToBag(storeID1,2,60);
        Response res3=SC1.addProductToBag(storeID1,7,10);
        //Response res4=SC1.addProductToBag(2,3,10);

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
    void editProductQuantityFromCart() {

        Response res0= SC1.editProductQuantityFromCart(storeID1,1,5);
        Response r1=SC1.addProductToBag(storeID1,1,3);
        Response r2=SC1.addProductToBag(storeID1,2,2);
        Response res1= SC1.editProductQuantityFromCart(storeID1,1,5);
        Response res2= SC1.editProductQuantityFromCart(storeID1,2,60);
        Response res3= SC1.editProductQuantityFromCart(storeID2,2,60);
        Response res4= SC1.editProductQuantityFromCart(storeID1,3,60);
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

    @Test
    void removeProductFromCart() {
        SC1.addProductToBag(storeID1,1,3);
        SC1.addProductToBag(storeID2,2,2);
        SC1.addProductToBag(storeID2,3,1);

        Response res1= SC1.RemoveProductFromCart(storeID1,1);
        Response res2= SC1.RemoveProductFromCart(storeID2,1);
        Response res3= SC1.RemoveProductFromCart(storeID2,4);

        //happy
        assertTrue(res1.getMessage().equals("product remove successfully"));
        //sad_productNotInTheCart
        assertTrue(res2.getMessage().equals("product that does not exist in the cart cannot be removed"));
        //sad_productNotInTheStore
        assertTrue(res3.getMessage().equals("product that does not exist in the cart cannot be removed"));

    }

    @Test
    void showShoppingCart() {
        SC1.addProductToBag(storeID1,1,3);
        SC1.addProductToBag(storeID2,2,2);
        SC1.addProductToBag(storeID2,3,1);

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