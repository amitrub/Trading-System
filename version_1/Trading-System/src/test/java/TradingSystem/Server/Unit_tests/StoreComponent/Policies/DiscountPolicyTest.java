package TradingSystem.Server.Unit_tests.StoreComponent.Policies;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.DiscountPolicy;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.*;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.NumOfProductsForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.PriceForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.QuantityForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.*;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.XorDecision.Cheaper;
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

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class DiscountPolicyTest {

    @Autowired
    TradingSystemImpl tradingSystem;

    Store store;
    DiscountPolicy DC;
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
        DC = store.getDiscountPolicy();
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
        //tradingSystem.Initialization();
        tradingSystem.ClearSystem();
    }

    //region Conditional discount tests
    @Test
    void HappyStoreSaleTest() {
        PriceForGetSale exp = new PriceForGetSale( 100);
        StoreSale sale = new StoreSale(exp, store.getId(), 10);
        DC.AddSale(sale);
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 1);
        products.put(productID2, 1);
        Double newPrice = DC.calculatePrice(products,2,3100.0);
       // DiscountPolicy discountPolicy= new DiscountPolicy(storeID,sale);
        tradingSystem.addDiscountPolicy(EuserId,EconnID,storeID,sale);
        assertEquals(2790.0, newPrice, 0);
    }

    @Test
    void Happyupload(){
        System.out.println("--------------------HELLLLLLLLLLLO---------------");
        Store store=tradingSystem.stores.get(storeID);
        DiscountPolicy discountPolicy=store.getDiscountPolicy();
        System.out.println("Discount policy--------"+discountPolicy.toString());
    }

    @Test
    void SadStoreSaleTest() {
        PriceForGetSale exp = new PriceForGetSale( 5000);
        StoreSale sale = new StoreSale(exp, store.getId(), 10);
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
        ProductSale sale = new ProductSale(exp,productID1,20);
        DC.AddSale(sale);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = DC.calculatePrice(products, 2 , 6100.0);
        tradingSystem.addDiscountPolicy(EuserId,EconnID,storeID,sale);
        assertEquals(4900.0, newPrice,0);
    }

    @Test
    void SadProductSaleTest() {
        tradingSystem.AddStoreToList(store);
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        NumOfProductsForGetSale exp = new NumOfProductsForGetSale( 3);
        ProductSale sale = new ProductSale(exp,productID1,20);
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
        CategorySale sale = new CategorySale(exp, "Fun", 50);
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
        CategorySale sale = new CategorySale(exp, "Fun", 50);
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
        PriceForGetSale exp1 = new PriceForGetSale( 1000);
        QuantityForGetSale exp2 = new QuantityForGetSale(productID1,2);
        AndComposite andExpression = new AndComposite();
        andExpression.add(exp1);
        andExpression.add(exp2);
        StoreSale sale = new StoreSale(andExpression, store.getId(), 50);
        //sale.setExpression(andExpression);
        DC.AddSale(sale);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = DC.calculatePrice(products,2,6100.0);
        tradingSystem.addDiscountPolicy(EuserId,EconnID,storeID,sale);
        assertEquals(3050.0, newPrice,0);
    }

    @Test
    void SadAndRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        PriceForGetSale exp1 = new PriceForGetSale( 1000);
        QuantityForGetSale exp2 = new QuantityForGetSale(productID1,5);
        AndComposite andExpression = new AndComposite();
        andExpression.add(exp1);
        andExpression.add(exp2);
        StoreSale sale = new StoreSale(andExpression, store.getId(), 50);
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
        PriceForGetSale exp1 = new PriceForGetSale( 10000);
        QuantityForGetSale exp2 = new QuantityForGetSale(productID1,2);
        OrComposite andExpression = new OrComposite();
        andExpression.add(exp1);
        andExpression.add(exp2);
        StoreSale sale = new StoreSale(andExpression, store.getId(), 50);
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
        PriceForGetSale exp1 = new PriceForGetSale( 10000);
        QuantityForGetSale exp2 = new QuantityForGetSale(productID1,5);
        OrComposite andExpression = new OrComposite();
        andExpression.add(exp1);
        andExpression.add(exp2);
        StoreSale sale = new StoreSale(andExpression, store.getId(), 50);
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
        PriceForGetSale exp1 = new PriceForGetSale( 1000);
        ProductSale PS1=new ProductSale(productID1,15);
        PS1.setExpression(exp1);
        QuantityForGetSale exp2 = new QuantityForGetSale(productID2,2);
        ProductSale PS2=new ProductSale(productID2,15);
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
        PriceForGetSale exp1 = new PriceForGetSale( 6000);
        ProductSale PS1=new ProductSale(productID1,15);
        PS1.setExpression(exp1);
        QuantityForGetSale exp2 = new QuantityForGetSale(productID2,3);
        ProductSale PS2=new ProductSale(productID2,15);
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

//    @Test
//    void basicSenario() {
    //    tradingSystem.ShowBuyingPolicyBuildingTree("",1,2);

//        Integer productID1 = store.getProductID("computer");
//        Integer productID2 = store.getProductID("Bag");
//        PriceForGetSale exp1 = new PriceForGetSale( 6000);
//        ProductSale PS1=new ProductSale(productID1,15);
//        PS1.setExpression(exp1);
//        QuantityForGetSale exp2 = new QuantityForGetSale(productID2,3);
//        ProductSale PS2=new ProductSale(productID2,15);
//        PS2.setExpression(exp2);
//        Cheaper c=new Cheaper();
//        XorComposite xorExpression = new XorComposite();
//        xorExpression.add(PS1);
//        xorExpression.add(PS2);
//        xorExpression.setDes(c);
//        DiscountPolicy dc=new DiscountPolicy(store.getId(),xorExpression);
//        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
//        products.put(productID1, 1);
//        products.put(productID2, 2);

//        DummyMaxSale Max =new DummyMaxSale(1);
//
//        DummyStoreSale storeSale = new DummyStoreSale(2,56, 50);
//        DummyAndExpression andExpression = new DummyAndExpression(4);
//
//        DummyProductSale productSale=new DummyProductSale(3,4,10);
//        DummyOrExpression Or=new DummyOrExpression(5);
//
//        DummyPriceForGetSale exp1 = new DummyPriceForGetSale( 6,1000);
//        DummyQuantityForGetSale exp2 = new DummyQuantityForGetSale(7,3,2);
//
//        DummyPriceForGetSale exp11=new DummyPriceForGetSale(8,10000);
//        DummyNumOfProductsForGetSale exp22=new DummyNumOfProductsForGetSale(9,10);
//
//
//        Max.setSale(1,productSale);
//        Max.setSale(1,storeSale);
//
//        Max.setExpression(2,andExpression);
//        Max.setExpression(3,Or);
//
//        Max.setExpression(4,exp1);
//        Max.setExpression(4,exp2);
//
//        Max.setExpression(5,exp11);
//        Max.setExpression(5,exp22);
//
//// }

}