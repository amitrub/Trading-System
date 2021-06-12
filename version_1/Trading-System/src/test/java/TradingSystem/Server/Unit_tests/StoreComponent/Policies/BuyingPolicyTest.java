package TradingSystem.Server.Unit_tests.StoreComponent.Policies;

import TradingSystem.Server.DataLayer.Data_Modules.Expressions.DataBuyingPolicy;
import TradingSystem.Server.DataLayer.Services.Data_Controller;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.BuyingPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.AndComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.Conditioning;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.OrComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimitForCategory;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimitForProduct;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimitForStore;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class BuyingPolicyTest {


    @Autowired
    TradingSystemImpl tradingSystem;
    @Autowired
    Data_Controller controller;

    Store store;
    BuyingPolicy BC;
    String EconnID;
    Integer EuserId;
    Integer storeID;

    @BeforeEach
    void setUp() {
        tradingSystem.ClearSystem();
        String guest1= tradingSystem.ConnectSystem().returnConnID();
        tradingSystem.Register(guest1, "Elinor", "123");
        Response res= tradingSystem.Login(guest1, "Elinor", "123");
        EconnID= res.returnConnID();
        EuserId=res.returnUserID();
        tradingSystem.AddStore(EuserId, EconnID, "store1");
        storeID = tradingSystem.getStoreIDByName("store1");
        store = tradingSystem.stores.get(storeID);
        BC = store.getBuyingPolicy();
        store.AddProductToStore( "computer", 3000.0, "Technology",5);
        store.AddProductToStore("Bag" ,100.0, "Beauty",5);
        store.AddProductToStore("IPed",  2500.0, "Technology", 5);
        store.AddProductToStore( "MakeUp",  40.0, "Beauty",5);
        store.AddProductToStore( "Ball",  60.0, "Fun",5);
    }

    @AfterEach
    void tearDown() {
        store = null;
        BC = null;
        tradingSystem.Initialization();
    }

    //region Simple buying rules
    @Test
    void HappyProductRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimitForProduct exp = new QuantityLimitForProduct(1, productID1);
        BC.setExp(exp);
//        BuyingPolicy b=new BuyingPolicy(store.getId(),exp);
//        store.setBuyingPolicy(b);
//        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        tradingSystem.addBuyingPolicy(EuserId,EconnID,storeID,exp);
        products.put(productID1, 1);
        products.put(productID2, 3);
        boolean isLegal = store.checkBuyingPolicy(2, products);
        assertTrue(isLegal);
    }
//    @Test
//    void TestTimeOut() throws InterruptedException {
//        Integer productID1 = store.getProductID("computer");
//        Integer productID2 = store.getProductID("Bag");
//        QuantityLimitForProduct exp = new QuantityLimitForProduct(1, productID1);
//        BC.setExp(exp);
//        BuyingPolicy b=new BuyingPolicy(store.getId(),exp);
////        store.setBuyingPolicy(b);
////        tradingSystem.AddStoreToList(store);
//        controller.AddBuyingPolicy(new DataBuyingPolicy(b));
//        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
//        products.put(productID1, 1);
//        products.put(productID2, 3);
//        boolean isLegal = store.checkBuyingPolicy(2, products);
//        assertTrue(isLegal);
//    }

    @Test
    void SadProductRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimitForProduct exp = new QuantityLimitForProduct(1, productID1);
        BC.setExp(exp);
//        BuyingPolicy b=new BuyingPolicy(store.getId(),exp);
//        store.setBuyingPolicy(b);
//        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 3);
        Boolean isLegal = store.checkBuyingPolicy(2, products);
        assertFalse(isLegal);
    }

    @Test
    void HappyCategoryRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimitForCategory exp = new QuantityLimitForCategory(1, "Technology");
        BC.setExp(exp);
//        BuyingPolicy b=new BuyingPolicy(store.getId(),exp);
//        store.setBuyingPolicy(b);
//        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 1);
        products.put(productID2, 3);
        Boolean isLegal = store.checkBuyingPolicy(2, products);
        assertTrue(isLegal);
    }

    @Test
    void SadCategoryRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimitForCategory exp = new QuantityLimitForCategory(1, "Technology");
        BC.setExp(exp);
//        BuyingPolicy b=new BuyingPolicy(store.getId(),exp);
//        store.setBuyingPolicy(b);
//        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 3);
        Boolean isLegal = store.checkBuyingPolicy(2, products);
        assertFalse(isLegal);
    }

    @Test
    void HappyBagRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimitForStore exp = new QuantityLimitForStore(4, store.getId());
        BC.setExp(exp);
//        BuyingPolicy b=new BuyingPolicy(store.getId(),exp);
//        store.setBuyingPolicy(b);
//        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 1);
        products.put(productID2, 3);
        Boolean isLegal = store.checkBuyingPolicy(2, products);
        assertTrue(isLegal);
    }

    @Test
    void SadBagRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimitForStore exp = new QuantityLimitForStore(4, store.getId());
        BC.setExp(exp);
//        BuyingPolicy b=new BuyingPolicy(store.getId(),exp);
//        store.setBuyingPolicy(b);
//        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 3);
        Boolean isLegal = store.checkBuyingPolicy(2, products);
        assertFalse(isLegal);
    }

    @Test
    void HappyUserRule() {
    }

    @Test
    void SadUserRule() {
    }
    //endregion

    //region Assembly of buying rules tests
    @Test
    void HappyAndBuying() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimitForProduct exp1 = new QuantityLimitForProduct(5, productID1);
        QuantityLimitForProduct exp2 = new QuantityLimitForProduct(4, productID2);
        AndComposite andExpression = new AndComposite();
        andExpression.add(exp1);
        andExpression.add(exp2);
        BC.setExp(andExpression);
//        BuyingPolicy b=new BuyingPolicy(store.getId(),andExpression);
//        store.setBuyingPolicy(b);
//        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 1);
        products.put(productID2, 3);
        //TODO check age
        Boolean isLegal = store.checkBuyingPolicy(2, products);
        assertTrue(isLegal);
    }

    @Test
    void SadAndBuying() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimitForProduct exp1 = new QuantityLimitForProduct(5, productID1);
        QuantityLimitForProduct exp2 = new QuantityLimitForProduct(4, productID2);
        AndComposite andExpression = new AndComposite();
        andExpression.add(exp1);
        andExpression.add(exp2);
        BC.setExp(andExpression);
//        BuyingPolicy b=new BuyingPolicy(store.getId(),andExpression);
//        store.setBuyingPolicy(b);
//        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 6);
        products.put(productID2, 7);
        //TODO check age
        Boolean isLegal = store.checkBuyingPolicy(2, products);
        assertFalse(isLegal);
    }

    @Test
    void HappyOrBuying() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimitForProduct exp1 = new QuantityLimitForProduct(5, productID1);
        QuantityLimitForProduct exp2 = new QuantityLimitForProduct(4, productID2);
        OrComposite orComposite = new OrComposite();
        orComposite.add(exp1);
        orComposite.add(exp2);
        BC.setExp(orComposite);
//        BuyingPolicy b=new BuyingPolicy(store.getId(),orComposite);
//        store.setBuyingPolicy(b);
//        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 3);
        products.put(productID2, 2);
        Boolean isLegal = store.checkBuyingPolicy(2, products);
        assertFalse(isLegal);
    }

    @Test
    void SadOrBuying() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimitForProduct exp1 = new QuantityLimitForProduct(5, productID1);
        QuantityLimitForProduct exp2 = new QuantityLimitForProduct(4, productID2);
        OrComposite orComposite = new OrComposite();
        orComposite.add(exp1);
        orComposite.add(exp2);
        BC.setExp(orComposite);
//        BuyingPolicy b=new BuyingPolicy(store.getId(),orComposite);
//        store.setBuyingPolicy(b);
//        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 6);
        products.put(productID2, 2);
        Boolean isLegal = store.checkBuyingPolicy(2, products);
        assertFalse(!isLegal);
    }

    @Test
    void HappyConditioningBuying() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimitForProduct exp1 = new QuantityLimitForProduct(5, productID1);
        QuantityLimitForProduct exp2 = new QuantityLimitForProduct(4, productID2);
        Conditioning conditioning = new Conditioning();
        conditioning.setCondIf(exp1);
        conditioning.setCond(exp2);
        BC.setExp(conditioning);
//        BuyingPolicy b=new BuyingPolicy(store.getId(),conditioning);
//        store.setBuyingPolicy(b);
//        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 6);
        products.put(productID2, 2);
        Boolean isLegal = store.checkBuyingPolicy(2, products);
        assertTrue(isLegal);
    }

    @Test
    void SadConditioningBuying() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimitForProduct exp1 = new QuantityLimitForProduct(5, productID1);
        QuantityLimitForProduct exp2 = new QuantityLimitForProduct(4, productID2);
        Conditioning conditioning = new Conditioning();
        conditioning.setCondIf(exp1);
        conditioning.setCond(exp2);
        BC.setExp(conditioning);
//        BuyingPolicy b=new BuyingPolicy(store.getId(),conditioning);
//        store.setBuyingPolicy(b);
//        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 6);
        products.put(productID2, 8);
        Boolean isLegal = store.checkBuyingPolicy(2, products);
        assertFalse(isLegal);
    }
    //endregion
}