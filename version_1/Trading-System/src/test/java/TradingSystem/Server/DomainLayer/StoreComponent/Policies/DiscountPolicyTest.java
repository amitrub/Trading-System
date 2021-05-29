package TradingSystem.Server.DomainLayer.StoreComponent.Policies;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.*;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.NumOfProductsForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.PriceForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.QuantityForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.*;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.XorDecision.Cheaper;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.XorDecision.Decision;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.matchers.Or;
import java.io.Serializable;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.json.JSONString;
import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class DiscountPolicyTest {

    @Autowired
    TradingSystemImplRubin tradingSystem;

    Store store;
    DiscountPolicy DC;
    BuyingPolicy BP;

    @BeforeEach
    void setUp() {
        store = new Store("Store1",1, DC, BP);
        DC = new DiscountPolicy(store.getId(),null);
        BP = new BuyingPolicy(store.getId(),null);
        store.AddProductToStore( "computer", 3000.0, "Technology",5);
        store.AddProductToStore("Bag" ,100.0, "Beauty",5);
        store.AddProductToStore("IPed",  2500.0, "Technology", 5);
        store.AddProductToStore( "MakeUp",  40.0, "Beauty",5);
        store.AddProductToStore( "Ball",  60.0, "Fun",5);
    }

    @AfterEach
    void tearDown() {
        store = null;
        DC = null;
        BP = null;
    }

    //region Conditional discount tests
    @Test
    void HappyStoreSaleTest() {
        PriceForGetSale exp = new PriceForGetSale( 100.0);
        StoreSale sale = new StoreSale(exp, store.getId(), 10.0);
        DC.AddSale(sale);
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 1);
        products.put(productID2, 1);
        Double newPrice = DC.calculatePrice(products,2,3100.0);
        assertEquals(2790.0, newPrice, 0);
    }

    @Test
    void SadStoreSaleTest() {
        PriceForGetSale exp = new PriceForGetSale( 5000.0);
        StoreSale sale = new StoreSale(exp, store.getId(), 10.0);
        DC.AddSale(sale);
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 1);
        products.put(productID2, 1);
        Double newPrice = DC.calculatePrice(products,2,3100.0);
        assertEquals(3100.0, newPrice, 0);
    }

    @Test
    void HappyProductSaleTest() {
        tradingSystem.AddStoreToList(store);
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        NumOfProductsForGetSale exp = new NumOfProductsForGetSale( 1);
        ProductSale sale = new ProductSale(exp,productID1,20.0);
        DC.AddSale(sale);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = DC.calculatePrice(products, 2 , 6100.0);
        assertEquals(4900.0, newPrice,0);
    }

    @Test
    void SadProductSaleTest() {
        tradingSystem.AddStoreToList(store);
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        NumOfProductsForGetSale exp = new NumOfProductsForGetSale( 3);
        ProductSale sale = new ProductSale(exp,productID1,20.0);
        DC.AddSale(sale);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = DC.calculatePrice(products, 2 , 6100.0);
        assertEquals(6100.0, newPrice,0);
    }

    @Test
    void HappyCategorySaleTest() {
        tradingSystem.AddStoreToList(store);
        Integer productID1 = store.getProductID("Ball");
        Integer productID2 = store.getProductID("Bag");
        QuantityForGetSale exp = new QuantityForGetSale( productID1, 2);
        CategorySale sale = new CategorySale(exp, "Fun", 50.0);
       // sale.setExpression(exp);
        DC.AddSale(sale);
        ConcurrentHashMap<Integer, Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = DC.calculatePrice(products, 2, 220.0);
        assertEquals(160.0, newPrice,0);
    }

    @Test
    void SadCategorySaleTest() {
        tradingSystem.AddStoreToList(store);
        Integer productID1 = store.getProductID("Ball");
        Integer productID2 = store.getProductID("Bag");
        QuantityForGetSale exp = new QuantityForGetSale( productID1, 4);
        CategorySale sale = new CategorySale(exp, "Fun", 50.0);
       // sale.setExpression(exp);
        DC.AddSale(sale);
        ConcurrentHashMap<Integer, Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = DC.calculatePrice(products, 2, 220.0);
        assertEquals(220.0, newPrice,0);
    }
    //endregion

    //region Discount rules - tests
    @Test
    void HappyAndRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        PriceForGetSale exp1 = new PriceForGetSale( 1000.0);
        QuantityForGetSale exp2 = new QuantityForGetSale(productID1,2);
        AndComposite andExpression = new AndComposite();
        andExpression.add(exp1);
        andExpression.add(exp2);
        StoreSale sale = new StoreSale(andExpression, store.getId(), 50.0);
        //sale.setExpression(andExpression);
        DC.AddSale(sale);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = DC.calculatePrice(products,2,6100.0);
        assertEquals(3050.0, newPrice,0);
    }

    @Test
    void SadAndRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        PriceForGetSale exp1 = new PriceForGetSale( 1000.0);
        QuantityForGetSale exp2 = new QuantityForGetSale(productID1,5);
        AndComposite andExpression = new AndComposite();
        andExpression.add(exp1);
        andExpression.add(exp2);
        StoreSale sale = new StoreSale(andExpression, store.getId(), 50.0);
       // sale.setExpression(andExpression);
        DC.AddSale(sale);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = DC.calculatePrice(products,2,6100.0);
        assertEquals(6100.0, newPrice,0);
    }

    @Test
    void HappyOrRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        PriceForGetSale exp1 = new PriceForGetSale( 10000.0);
        QuantityForGetSale exp2 = new QuantityForGetSale(productID1,2);
        OrComposite andExpression = new OrComposite();
        andExpression.add(exp1);
        andExpression.add(exp2);
        StoreSale sale = new StoreSale(andExpression, store.getId(), 50.0);
        //sale.setExpression(andExpression);
        DC.AddSale(sale);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = DC.calculatePrice(products,2,6100.0);
        assertEquals(3050.0, newPrice,0);
    }

    @Test
    void SadOrRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        PriceForGetSale exp1 = new PriceForGetSale( 10000.0);
        QuantityForGetSale exp2 = new QuantityForGetSale(productID1,5);
        OrComposite andExpression = new OrComposite();
        andExpression.add(exp1);
        andExpression.add(exp2);
        StoreSale sale = new StoreSale(andExpression, store.getId(), 50.0);
        //sale.setExpression(andExpression);
        DC.AddSale(sale);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = DC.calculatePrice(products,2,6100.0);
        assertEquals(6100.0, newPrice,0);
    }

    @Test
    void HappyXorRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        PriceForGetSale exp1 = new PriceForGetSale( 1000.0);
        ProductSale PS1=new ProductSale(productID1,15.0);
        PS1.setExpression(exp1);
        QuantityForGetSale exp2 = new QuantityForGetSale(productID2,2);
        ProductSale PS2=new ProductSale(productID2,15.0);
        PS2.setExpression(exp2);
        Cheaper c=new Cheaper();
        XorComposite xorExpression = new XorComposite();
        xorExpression.add(PS1);
        xorExpression.add(PS2);
        xorExpression.setDes(c);
        DiscountPolicy dc=new DiscountPolicy(store.getId(),xorExpression);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 2);
        tradingSystem.AddStoreToList(store);
        Double newPrice = dc.calculatePrice(products,2,6200.0);
        assertEquals(6170.0, newPrice,0);
    }

    @Test
    void SadXorRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        PriceForGetSale exp1 = new PriceForGetSale( 6000.0);
        ProductSale PS1=new ProductSale(productID1,15.0);
        PS1.setExpression(exp1);
        QuantityForGetSale exp2 = new QuantityForGetSale(productID2,3);
        ProductSale PS2=new ProductSale(productID2,15.0);
        PS2.setExpression(exp2);
        Cheaper c=new Cheaper();
        XorComposite xorExpression = new XorComposite();
        xorExpression.add(PS1);
        xorExpression.add(PS2);
        xorExpression.setDes(c);
        DiscountPolicy dc=new DiscountPolicy(store.getId(),xorExpression);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 1);
        products.put(productID2, 2);
        tradingSystem.AddStoreToList(store);
        Double newPrice = dc.calculatePrice(products,2,3200.0);
        assertEquals(3200.0, newPrice,0);
    }

    //endregion

}