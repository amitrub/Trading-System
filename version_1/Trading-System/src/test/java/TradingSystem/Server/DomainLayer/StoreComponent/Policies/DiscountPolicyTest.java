package TradingSystem.Server.DomainLayer.StoreComponent.Policies;

import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.*;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.NumOfProductsForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.PriceForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.QuantityForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.CategorySale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.ProductSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.SimpleSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.StoreSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class DiscountPolicyTest {

    TradingSystem tradingSystem = TradingSystem.getInstance();
    Store store;
    DiscountPolicy DC;
    BuyingPolicy BP;

    @BeforeEach
    void setUp() {
        store = new Store("Store1",1, DC, BP);
        DC = new DiscountPolicy(store.getId());
        BP = new BuyingPolicy(store.getId());
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
        StoreSale sale = new StoreSale(1, store.getId(), 10.0);
        PriceForGetSale exp = new PriceForGetSale(1, 100.0);
        sale.setExpression(exp);
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
        StoreSale sale = new StoreSale(1, store.getId(), 10.0);
        PriceForGetSale exp = new PriceForGetSale(1, 5000.0);
        sale.setExpression(exp);
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
        ProductSale sale = new ProductSale(1,productID1,20.0);
        NumOfProductsForGetSale exp = new NumOfProductsForGetSale(1, 1);
        sale.setExpression(exp);
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
        ProductSale sale = new ProductSale(1,productID1,20.0);
        NumOfProductsForGetSale exp = new NumOfProductsForGetSale(1, 3);
        sale.setExpression(exp);
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
        CategorySale sale = new CategorySale(1, "Fun", 50.0);
        QuantityForGetSale exp = new QuantityForGetSale(1, productID1, 2);
        sale.setExpression(exp);
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
        CategorySale sale = new CategorySale(1, "Fun", 50.0);
        QuantityForGetSale exp = new QuantityForGetSale(1, productID1, 4);
        sale.setExpression(exp);
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
        StoreSale sale = new StoreSale(1, store.getId(), 50.0);
        PriceForGetSale exp1 = new PriceForGetSale(1, 1000.0);
        QuantityForGetSale exp2 = new QuantityForGetSale(2,productID1,2);
        AndComposite andExpression = new AndComposite(1);
        andExpression.add(exp1);
        andExpression.add(exp2);
        sale.setExpression(andExpression);
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
        StoreSale sale = new StoreSale(1, store.getId(), 50.0);
        PriceForGetSale exp1 = new PriceForGetSale(1, 1000.0);
        QuantityForGetSale exp2 = new QuantityForGetSale(2,productID1,5);
        AndComposite andExpression = new AndComposite(1);
        andExpression.add(exp1);
        andExpression.add(exp2);
        sale.setExpression(andExpression);
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
        StoreSale sale = new StoreSale(1, store.getId(), 50.0);
        PriceForGetSale exp1 = new PriceForGetSale(1, 10000.0);
        QuantityForGetSale exp2 = new QuantityForGetSale(2,productID1,2);
        OrComposite andExpression = new OrComposite(1);
        andExpression.add(exp1);
        andExpression.add(exp2);
        sale.setExpression(andExpression);
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
        StoreSale sale = new StoreSale(1, store.getId(), 50.0);
        PriceForGetSale exp1 = new PriceForGetSale(1, 10000.0);
        QuantityForGetSale exp2 = new QuantityForGetSale(2,productID1,5);
        OrComposite andExpression = new OrComposite(1);
        andExpression.add(exp1);
        andExpression.add(exp2);
        sale.setExpression(andExpression);
        DC.AddSale(sale);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = DC.calculatePrice(products,2,6100.0);
        assertEquals(6100.0, newPrice,0);
    }

    @Test
    void HappyXorRule() {
    }

    @Test
    void SadXorRule() {
    }

    //endregion
}