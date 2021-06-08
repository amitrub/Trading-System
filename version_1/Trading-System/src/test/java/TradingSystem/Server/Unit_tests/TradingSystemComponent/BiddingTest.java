package TradingSystem.Server.Unit_tests.TradingSystemComponent;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.OrComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimitForProduct;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BiddingTest {

    @Autowired
    TradingSystemImplRubin tradingSystem;

    String EconnID;
    String NconnID;
    int ElinorID;
    int NofetID;
    int NofetStore;

    @Before
    public void setUp(){
        NconnID = tradingSystem.ConnectSystem().returnConnID();
        Response r1= tradingSystem.Register(NconnID, "nofet", "123");
        NofetID= r1.returnUserID();
        NconnID= tradingSystem.Login(NconnID, "nofet", "123").returnConnID();

        EconnID = tradingSystem.ConnectSystem().returnConnID();
        Response r2= tradingSystem.Register(EconnID, "elinor", "123");
        ElinorID= r2.returnUserID();
        EconnID= tradingSystem.Login(EconnID, "elinor", "123").returnConnID();

        tradingSystem.AddStore(NofetID,NconnID,"NofetStore");
        NofetStore = tradingSystem.getStoreIDByName("NofetStore");
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"test1","test1",10,20);
        tradingSystem.AddProductToStore(NofetID,NconnID,NofetStore,"test2","test1",7,20);

    }

    //region requirement 3.8
    // Subscriber Bidding
    @Test
    public void HappySubscriberBidding() {
        tradingSystem.AddProductToCart(NconnID,NofetStore,1,3);

        Response r8=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        assertFalse(r8.getIsErr());
    }

    @Test
    public void SadSubscriberBidding_unsubscribe() {
        Response r1=tradingSystem.subscriberBidding(-1,"",1,1,1,1);
        assertTrue(r1.getIsErr());
    }

    @Test
    public void SadSubscriberBidding_storeNotExist() {
        Response r2=tradingSystem.subscriberBidding(NofetID,NconnID,-1,1,1,1);
        assertTrue(r2.getIsErr());
    }

    @Test
    public void SadSubscriberBidding_productNotExist() {
        Response r3=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,-1,1,1);
        Assertions.assertTrue(r3.getIsErr());
    }

    @Test
    public void SadSubscriberBidding_productInTheCartAlready() {
        tradingSystem.AddProductToCart(NconnID,NofetStore,1,3);
        Response r4=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,1,1,1);
        assertTrue(r4.getIsErr());
    }

    @Test
    public void SadSubscriberBidding_priceNotInRange() {
        Response r5=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,-1,1);
        assertTrue(r5.getIsErr());

        Response r6=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,70,1);
        assertTrue(r6.getIsErr());
    }

    @Test
    public void SadSubscriberBidding_NegativeQuantity() {
        tradingSystem.AddProductToCart(NconnID,NofetStore,1,3);

        Response r7=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,3,-1);
        assertTrue(r7.getIsErr());
    }

    @Test
    public void SadSubscriberBidding_BidAlreadyExist() {
        tradingSystem.AddProductToCart(NconnID,NofetStore,1,3);

        Response r8=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        assertFalse(r8.getIsErr());
        Response r9=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        assertTrue(r9.getIsErr());

    }

    //endregion

    //region requirement 3.8
    //Response to subscriber bidding
    @Test
    public void HappyResponseToSubscriberBidding() {
        tradingSystem.AddProductToCart(NconnID,NofetStore,1,3);

        tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,2,2);
        Response r8=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,2,NofetID,2,2);
        assertFalse(r8.getIsErr());
    }

    @Test
    public void SadUnsubscribe() {
        Response r0=tradingSystem.ResponseForSubmissionBidding(-1,"",1,1,1,1,1,2);
        Assertions.assertTrue(r0.getIsErr());

        Response r1=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,1,1,1,1,1,2);
        Assertions.assertTrue(r1.getIsErr());
    }

    @Test
    public void SadStoreNotExist() {
        Response r2=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,-1,-1,1,ElinorID,1,2);
        assertTrue(r2.getIsErr());
    }

    @Test
    public void SadProductNotExist() {
        Response r3=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,-1,1,ElinorID,1,2);
        assertTrue(r3.getIsErr());
    }

    @Test
    public void SadBideHadResponseAlready() {
        tradingSystem.AddProductToCart(NconnID,NofetStore,1,3);

        tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,2,2);
        Response r8=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,2,NofetID,2,2);
        assertFalse(r8.getIsErr());
        Response r9=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,3,NofetID,1,2);
        assertTrue(r9.getIsErr());
    }

    @Test
    public void SadPriceNotInRange() {
        tradingSystem.AddProductToCart(NconnID,NofetStore,1,3);

        Response r5=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,-1,ElinorID,1,2);
        assertTrue(r5.getIsErr());

        Response r6=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,70,ElinorID,1,2);
        assertTrue(r6.getIsErr());
    }

    @Test
    public void SadNegativeQuantity() {
        tradingSystem.AddProductToCart(NconnID,NofetStore,1,3);

        Response r7=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,3,ElinorID,-1,2);
        assertTrue(r7.getIsErr());

    }

    @Test
    public void showBids() {
        tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,1,3,2);
        tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        Response r=tradingSystem.ShowBids(NofetID,NconnID,NofetStore);
        assertFalse(r.getIsErr());
    }

    //endregion

    //region requirement 3.8
    // Add special product

    @Test
    public void HappyAddSpecialProduct() {
        Response r8=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        assertFalse(r8.getIsErr());
    }

    @Test
    public void SadProductExistInCart() {
        Response r1=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,1,1);
        tradingSystem.AddProductToCart(NconnID,NofetStore,2,3);
        Response r2=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,3,NofetID,3,2);
        assertFalse(r1.getIsErr());
        assertTrue(r2.getIsErr());
    }

    @Test
    public void SadProductsNotInStock() {
        Response r1=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        Response r2=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,3,NofetID,30,2);
        assertFalse(r1.getIsErr());
        assertTrue(r2.getIsErr());
    }

    @Test
    public void SadAddSpecialProduct_productNotExist() {
        tradingSystem.RemoveProduct(NofetID,NofetStore,2,NconnID);
        Response r8=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        assertTrue(r8.getIsErr());
    }

    @Test
    public void SadProductNotInStore() {
        tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,2,3,2);
        tradingSystem.RemoveProduct(NofetID,NofetStore,2,NconnID);
        Response r=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,2,3,NofetID,30,2);
        assertTrue(r.getIsErr());
    }

    @Test
    public void SadProductAgainstThePolicy() {
        Store s= tradingSystem.stores.get(NofetStore);
        Integer productID1 =s.getProductID("test1");
        QuantityLimitForProduct exp1 = new QuantityLimitForProduct(5, productID1);
        OrComposite or = new OrComposite();
        or.add(exp1);
        BuyingPolicy b=new BuyingPolicy(s.getId(),or);
        tradingSystem.stores.get(NofetStore).setBuyingPolicy(b);
        Response r1=tradingSystem.subscriberBidding(NofetID,NconnID,NofetStore,productID1,3,2);
        Response r2=tradingSystem.ResponseForSubmissionBidding(NofetID,NconnID,NofetStore,productID1,3,NofetID,6,2);
        assertFalse(r1.getIsErr());
        assertTrue(r2.getIsErr());
    }
    //endregion
}
