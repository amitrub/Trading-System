package TradingSystem.Server.Unit_tests.ShoppingComponent;

import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingBag;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingCart;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimitForProduct;
import TradingSystem.Server.DomainLayer.StoreComponent.Product;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import TradingSystem.Server.DomainLayer.UserComponent.PermissionEnum;
import TradingSystem.Server.DomainLayer.UserComponent.User;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.LinkedList;
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


    String NconnID;
    int NuserId ,storeID1;
    ShoppingCart SC1;


    @BeforeEach
    void setUp() {
        tradingSystem.ClearSystem();
        String guest1= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(guest1, "nofet", "123");
        Response res= tradingSystem.Login(guest1, "nofet", "123");
        NconnID= res.returnConnID();
        NuserId=res.returnUserID();
        User Nofet = tradingSystem.subscribers.get(NuserId);
        SC1 = Nofet.getShoppingCart();
        tradingSystem.AddStore(NuserId, NconnID, "NofetStore1");
        for(Store store: tradingSystem.stores.values()){
            if(store.getName().equals("NofetStore1")){
                storeID1=store.getId();
            }
        }
        tradingSystem.AddProductToStore(NuserId, NconnID, storeID1, "computer", "Technology", 3000.0,20);
        tradingSystem.AddProductToStore(NuserId, NconnID, storeID1, "Bag", "Beauty", 100.0,50);
        tradingSystem.AddProductToStore(NuserId, NconnID, storeID1, "Bed", "Fun", 4500.0,30);
    }

    //requirement 2.7
    @Test
    void addProductToBag() {
        Store Nstore = tradingSystem.stores.get(storeID1);
        Integer productID1 = Nstore.getProductID("computer");
        Integer productID2 = Nstore.getProductID("Bag");
        Integer productID3 = Nstore.getProductID("Bed");
        Response res1=SC1.addProductToBag(storeID1,productID1,10, false);
        Response res2=SC1.addProductToBag(storeID1,productID2,60, false);
        Response res3=SC1.addProductToBag(storeID1,7,10, false);
        QuantityLimitForProduct exp = new QuantityLimitForProduct(10, productID3);
        tradingSystem.addBuyingPolicy(NuserId, NconnID, storeID1, exp);
        Response res4=SC1.addProductToBag(storeID1,productID3,12,false);



        //happy
        assertFalse(res1.getIsErr());

        //sad_productNotInTheStock
        assertTrue(res2.getIsErr());

        //sad_storeNotSellTheProduct
        assertTrue(res3.getIsErr());

        //sad_againstTheStorePolicy
        assertTrue(res4.getIsErr());
    }

    @Test
    void NotAddSpacialProductToBag() {
        Store Nstore = tradingSystem.stores.get(storeID1);
        Integer productID1 = Nstore.getProductID("computer");
        tradingSystem.subscriberBidding(NuserId,NconnID,storeID1,productID1,5,1);
        tradingSystem.ResponseForSubmissionBidding(NuserId,NconnID,storeID1,productID1,3,NuserId,3,1);
        tradingSystem.AddProductToCart(NconnID,NuserId,productID1,3);
        boolean prodExist=tradingSystem.subscribers.get(NuserId).getShoppingCart().getShoppingBags().get(storeID1).getProducts().keySet().contains(productID1);
        assertFalse(prodExist);
    }

    //requirement 2.8
    @Test
    void editProductQuantityFromCart() {
        Store Nstore = tradingSystem.stores.get(storeID1);
        Integer productID1 = Nstore.getProductID("computer");
        Integer productID2 = Nstore.getProductID("Bag");
        Integer productID3 = Nstore.getProductID("Bed");
        Response res0 = SC1.editProductQuantityFromCart(storeID1,productID1,5);
        SC1.addProductToBag(storeID1,productID1,3, false);
        SC1.addProductToBag(storeID1,productID2,2, false);
        Response res1 = SC1.editProductQuantityFromCart(storeID1,productID1,5);
        Response res2 = SC1.editProductQuantityFromCart(storeID1,productID3,1);
        QuantityLimitForProduct exp = new QuantityLimitForProduct(10, productID1);
        tradingSystem.addBuyingPolicy(NuserId, NconnID, storeID1, exp);
        Response res5 = SC1.editProductQuantityFromCart(storeID1,productID1,15);

        //happy
        assertFalse(res1.getIsErr());

        //sad_shoppingCartEmpty
        assertTrue(res0.getIsErr());

        //sad_productNotInTheCart
        assertTrue(res2.getIsErr());

        //sad_productAgainstThePolicy
        assertTrue(res5.getIsErr());
    }

    @Test
    void NotEditSpecialProductQuantityFromCart() {
        Store Nstore = tradingSystem.stores.get(storeID1);
        Integer productID1 = Nstore.getProductID("computer");
        tradingSystem.subscriberBidding(NuserId,NconnID,storeID1,productID1,5,1);
        tradingSystem.ResponseForSubmissionBidding(NuserId,NconnID,storeID1,productID1,3,NuserId,3,1);
        tradingSystem.editProductQuantityFromCart(NconnID,NuserId,productID1,4);
        int productQuantity=tradingSystem.subscribers.get(NuserId).getShoppingCart().getShoppingBags().get(storeID1).getQuantityOfSpacialProducts().get(productID1);
        assertFalse(productQuantity==3);
    }

    //requirement 2.8
    @Test
    void removeProductFromCart() {
        SC1.addProductToBag(storeID1,1,3, false);
        SC1.addProductToBag(storeID1,2,2, false);
        SC1.addProductToBag(storeID1,3,1, false);

        Response res1= SC1.RemoveProductFromCart(storeID1,1);
        Response res2= SC1.RemoveProductFromCart(storeID1,1);
        Response res3= SC1.RemoveProductFromCart(storeID1,4);

        //happy
        assertFalse(res1.getIsErr());
        //sad_productNotInTheCart
        assertTrue(res2.getIsErr());
        //sad_productNotInTheStore
        assertTrue(res3.getIsErr());

    }

    //requirement 2.8
    @Test
    void showShoppingCart() {
        SC1.addProductToBag(storeID1,1,3, false);
        SC1.addProductToBag(storeID1,2,2, false);
        SC1.addProductToBag(storeID1,3,1, false);

        List<DummyProduct> L1= SC1.ShowShoppingCart();
        ShoppingCart SC2 = new ShoppingCart(3);

        List<DummyProduct> L2= SC2.ShowShoppingCart();

        //happy
        assertTrue(L1.size()==3);
        for (DummyProduct DP:L1) {
            if(DP.getProductID()==1){
                assertTrue(DP.getProductName().equals("computer")&&DP.getCategory().equals("Technology")&&DP.getStoreName().equals("NofetStore1")&&DP.getPrice()==3000.0&&DP.getQuantity()==3);
            }
            if(DP.getProductID()==2){
                assertTrue(DP.getProductName().equals("Bag")&&DP.getCategory().equals("Beauty")&&DP.getStoreName().equals("NofetStore1")&&DP.getPrice()==100.0&&DP.getQuantity()==2);
            }
            if(DP.getProductID()==3){
                assertTrue(DP.getProductName().equals("Bed")&&DP.getCategory().equals("Fun")&&DP.getStoreName().equals("NofetStore1")&&DP.getPrice()==4500.0&&DP.getQuantity()==1);
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
        shoppingBagsList1.put(4, shoppingBag4);
        ShoppingCart shoppingCart2 = new ShoppingCart(2, shoppingBagsList2);

        shoppingCart1.mergeToMyCart(shoppingCart2);
        assertEquals(shoppingCart1.getShoppingBags().size(),4);
    }

    //region requirement 8.3: Bidding
    @Test
     void productAddedAfterFinishApprove(){
        String guest1= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(guest1, "user1", "123");
        Response res = tradingSystem.Login(guest1, "user1", "123");
        List<DummyProduct> list0=tradingSystem.subscribers.get(res.returnUserID()).ShowSpecialProductInShoppingCart();
        tradingSystem.subscriberBidding(res.returnUserID(),res.returnConnID(),storeID1,1,2700,1);
        tradingSystem.ResponseForSubmissionBidding(NuserId,NconnID,storeID1,1,2700,res.returnUserID(),1,1);
        List<DummyProduct> list1=tradingSystem.subscribers.get(res.returnUserID()).ShowSpecialProductInShoppingCart();
        assertEquals(list0.size()+1,list1.size());
    }

    @Test
    void productNotAddedAfterTempApprove(){
        String guest1= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(guest1, "user1", "123");
        Response resUser = tradingSystem.Login(guest1, "user1", "123");

        String guest2= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(guest2, "Manager", "123");
        Response resManager = tradingSystem.Login(guest2, "Manager", "123");

        tradingSystem.AddNewManager(NuserId,NconnID,storeID1,resManager.returnUserID());
        LinkedList<PermissionEnum.Permission> list = new LinkedList<>();
        list.add(PermissionEnum.Permission.RequestBidding);
        tradingSystem.EditManagerPermissions(NuserId, NconnID, storeID1, resManager.returnUserID(), list);

        List<DummyProduct> list0=tradingSystem.subscribers.get(resUser.returnUserID()).ShowSpecialProductInShoppingCart();
        tradingSystem.subscriberBidding(resUser.returnUserID(),resUser.returnConnID(),storeID1,1,2700,1);
        tradingSystem.ResponseForSubmissionBidding(NuserId,NconnID,storeID1,1,2700,resUser.returnUserID(),1,1);
        List<DummyProduct> list1=tradingSystem.subscribers.get(resUser.returnUserID()).ShowSpecialProductInShoppingCart();

        assertEquals(list0.size(),list1.size());
    }

    @Test
    void specialProductRemoved(){
        String guest1= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(guest1, "user3", "123");
        Response res = tradingSystem.Login(guest1, "user3", "123");
        List<DummyProduct> list0=tradingSystem.subscribers.get(res.returnUserID()).ShowSpecialProductInShoppingCart();

        Response r1=tradingSystem.subscriberBidding(res.returnUserID(),res.returnConnID(),storeID1,1,2700,1);
        Response r2=tradingSystem.ResponseForSubmissionBidding(NuserId,NconnID,storeID1,1,2700,res.returnUserID(),1,1);
        List<DummyProduct> list1=tradingSystem.subscribers.get(res.returnUserID()).ShowSpecialProductInShoppingCart();

        tradingSystem.removeSpecialProductFromCart(res.returnConnID(),storeID1,1);
        List<DummyProduct> list2=tradingSystem.subscribers.get(res.returnUserID()).ShowSpecialProductInShoppingCart();

        assertTrue(list0.size()+1==list1.size()&&list0.size()==list2.size());
    }

    @Test
    void calculateSpecialPrice(){
        String guest1= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(guest1, "user4", "123");
        Response res = tradingSystem.Login(guest1, "user4", "123");
        tradingSystem.subscriberBidding(res.returnUserID(),res.returnConnID(),storeID1,1,2700,1);
        tradingSystem.subscriberBidding(res.returnUserID(),res.returnConnID(),storeID1,2,70,1);
        tradingSystem.subscriberBidding(res.returnUserID(),res.returnConnID(),storeID1,3,4000,1);
        tradingSystem.ResponseForSubmissionBidding(NuserId,NconnID,storeID1,1,2700,res.returnUserID(),1,1);
        tradingSystem.ResponseForSubmissionBidding(NuserId,NconnID,storeID1,2,70,res.returnUserID(),1,1);
        tradingSystem.ResponseForSubmissionBidding(NuserId,NconnID,storeID1,3,4000,res.returnUserID(),1,1);
        User user = tradingSystem.subscribers.get(res.returnUserID());
        ShoppingCart SC = user.getShoppingCart();
        ShoppingBag SB=null;
        for (Integer key:SC.getShoppingBags().keySet()
             ) {
            if(key==storeID1){
                SB=SC.getShoppingBags().get(key);
            }
        }
        int price=0;
        if(SB!=null){
            price=SB.calculateSpacialPrices();
        }
        assertEquals(price,6770);
    }

    //endregion

}