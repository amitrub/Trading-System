package TradingSystem.Server.DomainLayer.StoreComponent.Policies;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.AndComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.AgeLimit;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimit;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Limits.ProductLimit;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class BuyingPolicyTest {

    TradingSystem tradingSystem = TradingSystem.getInstance();
    Store store;

    @BeforeEach
    void setUp() {
        store = new Store("Store1",1);
        store.AddProductToStore( "computer", 3000.0, "Technology",5);
        store.AddProductToStore("Bag" ,100.0, "Beauty",5);
        store.AddProductToStore("IPed",  2500.0, "Technology", 5);
        store.AddProductToStore( "MakeUp",  40.0, "Beauty",5);
        store.AddProductToStore( "Ball",  60.0, "Fun",5);
    }

    @AfterEach
    void tearDown() {
        store = null;
        tradingSystem.Initialization();
    }

    //region Simple buying rules
    @Test
    void HappyProductRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimit exp = new QuantityLimit(1, 1);
        store.addLimitToPolicy(null,productID1,exp);
        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 1);
        products.put(productID2, 3);
        Boolean isLegal = store.checkEntitlement(2, products);
        assertTrue(isLegal);
    }

    @Test
    void SadProductRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimit exp = new QuantityLimit(1, 1);
        store.addLimitToPolicy(null,productID1,exp);
        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 3);
        Boolean isLegal = store.checkEntitlement(2, products);
        assertFalse(isLegal);
    }

    @Test
    void HappyCategoryRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimit exp = new QuantityLimit(1, 1);
        store.addLimitToPolicy("Technology",-1,exp);
        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 1);
        products.put(productID2, 3);
        Boolean isLegal = store.checkEntitlement(2, products);
        assertTrue(isLegal);
    }

    @Test
    void SadCategoryRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimit exp = new QuantityLimit(1, 1);
        store.addLimitToPolicy("Technology",-1,exp);
        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 3);
        Boolean isLegal = store.checkEntitlement(2, products);
        assertFalse(isLegal);
    }

    @Test
    void HappyBagRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimit exp = new QuantityLimit(1, 4);
        store.addLimitToPolicy(null,-1,exp);
        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 1);
        products.put(productID2, 3);
        Boolean isLegal = store.checkEntitlement(2, products);
        assertTrue(isLegal);
    }

    @Test
    void SadBagRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimit exp = new QuantityLimit(1, 4);
        store.addLimitToPolicy(null,-1,exp);
        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 3);
        Boolean isLegal = store.checkEntitlement(2, products);
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
        QuantityLimit exp1 = new QuantityLimit(1, 4);
        AgeLimit exp2 = new AgeLimit(2, 20);
        AndComposite andExpression = new AndComposite(1);
        andExpression.add(exp1);
        andExpression.add(exp2);
        store.addLimitToPolicy(null,-1,andExpression);
        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 1);
        products.put(productID2, 3);
        //TODO check age
        Boolean isLegal = store.checkEntitlement(2, products);
        assertTrue(isLegal);
    }

    @Test
    void SadAndBuying() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        QuantityLimit exp1 = new QuantityLimit(1, 4);
        AgeLimit exp2 = new AgeLimit(2, 20);
        AndComposite andExpression = new AndComposite(1);
        andExpression.add(exp1);
        andExpression.add(exp2);
        store.addLimitToPolicy(null,-1,andExpression);
        tradingSystem.AddStoreToList(store);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 3);
        //TODO check age
        Boolean isLegal = store.checkEntitlement(2, products);
        assertTrue(isLegal);
    }

    @Test
    void HappyOrBuying() {
    }

    @Test
    void SadOrBuying() {
    }

    @Test
    void HappyXorBuying() {
    }

    @Test
    void SadXorBuying() {
    }

    @Test
    void HappyConditioningBuying() {
    }

    @Test
    void SadConditioningBuying() {
    }
    //endregion
}