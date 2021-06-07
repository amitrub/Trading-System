package TradingSystem.Server.Unit_tests.StoreComponent;

import TradingSystem.Server.DomainLayer.StoreComponent.Store;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class StoreTest {

    @Autowired
    TradingSystemImplRubin tradingSystem;

    Store store;
    String EconnID;
    Integer EuserId;
    Integer storeID;
    String NconnID;
    int NofetID;

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
        //store = new Store("store1",1);
        store = tradingSystem.stores.get(storeID);
        store.AddProductToStore( "computer", 3000.0, "Technology",5);
        store.AddProductToStore("Bag" ,100.0, "Beauty",5);
        store.AddProductToStore("IPed",  2500.0, "Technology", 5);
        store.AddProductToStore( "MakeUp",  40.0, "Beauty",5);
        store.AddProductToStore( "Ball",  60.0, "Fun",5);

        NconnID = tradingSystem.ConnectSystem().returnConnID();
        Response r1= tradingSystem.Register(NconnID, "nofet", "123");
        NofetID= r1.returnUserID();
        NconnID= tradingSystem.Login(NconnID, "nofet", "123").returnConnID();


    }

    @AfterEach
    void tearDown() {
        store = null;
    }

    void setUpBeforePurchase(){
        tradingSystem.AddProductToStore(EuserId, EconnID, storeID, "computer", "Technology", 3000.0,20);
        tradingSystem.AddProductToStore(EuserId, EconnID, storeID, "Bag", "Beauty", 100.0,50);
        tradingSystem.AddProductToStore(EuserId, EconnID, storeID, "Bed", "Fun", 4500.0,30);
    }

    //region search tests

    //requirement 2.6
    @Test
    void searchProduct() {
        List<DummyProduct> L1= store.SearchProduct("computer",null,-1,-1);
        assertTrue(L1.get(0).getProductName().equals("computer"));

        List<DummyProduct> L2= store.SearchProduct(null,"Technology",-1,-1);
        assertTrue(L2.get(0).getCategory().equals("Technology")&&L2.get(1).getCategory().equals("Technology"));

        List<DummyProduct> L3= store.SearchProduct(null,null,0,50);
        assertTrue(L3.get(0).getProductName().equals("MakeUp"));

        List<DummyProduct> L4= store.SearchProduct(null,"Fun",0,50);
        assertTrue(L4.isEmpty());
    }

    //endregion

    //region history tests
    @Test
    void showStoreHistorySuccess() {
        Integer productID1 = store.getProductID("computer");
        tradingSystem.AddProductToCart(EconnID,storeID, productID1,5);
        tradingSystem.subscriberPurchase(EuserId, EconnID, "123456","4","2022","123","123456","Rager","Beer Sheva","Israel","123");
        List<DummyShoppingHistory> list = store.ShowStoreHistory();
        assertEquals(list.size(), 1);
        assertTrue(list.get(0).getProducts().get(0).getProductName().equals("computer"));
    }

    @Test
    void showStoreEmpty() {
        List<DummyShoppingHistory> list =store.ShowStoreHistory();
        assertTrue(list.isEmpty());
    }
    //endregion

    //region Write Comment tests

    // requirement 3.3
    @Test
    public void WriteCommentSuccess() {
        setUpBeforePurchase();
        Integer productID1 = store.getProductID("computer");
        tradingSystem.AddProductToCart(NconnID, storeID, productID1, 1);
        tradingSystem.subscriberPurchase(NofetID, NconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        Response response = tradingSystem.WriteComment(NofetID,NconnID, storeID, productID1, "Amazing");
        Assertions.assertFalse(response.getIsErr());
        Integer size = store.getProduct(productID1).getComments().size();
        Assertions.assertEquals(size, 1);
    }

    // requirement 3.3
    @Test
    public void WriteCommentWrongStoreID() {
        setUpBeforePurchase();
        Integer productID1 = store.getProductID("computer");
        tradingSystem.AddProductToCart(NconnID, storeID, productID1, 1);
        tradingSystem.subscriberPurchase(NofetID, NconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        Response response = tradingSystem.WriteComment(NofetID,NconnID, 100, productID1, "Amazing");
        assertTrue(response.getIsErr());
        Integer size = store.getProduct(productID1).getComments().size();
        Assertions.assertEquals(size, 0);
    }

    // requirement 3.3
    @Test
    public void WriteCommentNotInHistory() {
        setUpBeforePurchase();
        Integer productID1 = store.getProductID("computer");

        Response response = tradingSystem.WriteComment(NofetID,NconnID, storeID, productID1, "Amazing");
        assertTrue(response.getIsErr());
        Integer size = store.getProduct(productID1).getComments().size();
        Assertions.assertEquals(size, 0);

    }

    // requirement 3.3
    @Test
    public void WriteCommentExistComment() {
        setUpBeforePurchase();
        Integer productID1 = store.getProductID("computer");
        tradingSystem.AddProductToCart(NconnID, storeID, productID1, 1);
        tradingSystem.subscriberPurchase(NofetID, NconnID, "123456789", "4","2022" , "123", "123456789", "Rager 101","Beer Sheva","Israel","8458527");

        tradingSystem.WriteComment(NofetID,NconnID, storeID, productID1, "Amazing");
        Response response = tradingSystem.WriteComment(NofetID,NconnID, storeID, productID1, "WTF");
        assertTrue(response.getIsErr());
        Integer size = store.getProduct(productID1).getComments().size();
        Assertions.assertEquals(size, 1);
    }

    //endregion




/*
    //region Conditional discount tests
    @Test
    void HappyStoreSaleTest() {
        PriceForGetSale exp = new PriceForGetSale(1, 100.0);
        store.addSaleToPolicy(null, -1, 10.0, exp);
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 1);
        products.put(productID2, 1);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(2790.0, newPrice, 0);
    }

    @Test
    void SadStoreSaleTest() {
        PriceForGetSale exp = new PriceForGetSale(1, 5000.0);
        store.addSaleToPolicy(null, -1, 10.0, exp);
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 1);
        products.put(productID2, 1);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(3100.0, newPrice, 0);
    }

    @Test
    void HappyProductSaleTest() {
        tradingSystem.AddStoreToList(store);
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        NumOfProductsForGetSale exp = new NumOfProductsForGetSale(1, 1);
        store.addSaleToPolicy(null, productID1, 20.0, exp);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(4900.0, newPrice,0);
    }

    @Test
    void SadProductSaleTest() {
        tradingSystem.AddStoreToList(store);
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        NumOfProductsForGetSale exp = new NumOfProductsForGetSale(1, 3);
        store.addSaleToPolicy(null, productID1, 20.0, exp);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(6100.0, newPrice,0);
    }

    @Test
    void HappyCategorySaleTest() {
        tradingSystem.AddStoreToList(store);
        Integer productID1 = store.getProductID("Ball");
        Integer productID2 = store.getProductID("Bag");
        QuantityForGetSale exp = new QuantityForGetSale(1, productID1, 2);
        store.addSaleToPolicy("Fun", -1, 50.0, exp);
        ConcurrentHashMap<Integer, Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(160.0, newPrice,0);
    }

    @Test
    void SadCategorySaleTest() {
        tradingSystem.AddStoreToList(store);
        Integer productID1 = store.getProductID("Ball");
        Integer productID2 = store.getProductID("Bag");
        QuantityForGetSale exp = new QuantityForGetSale(1, productID1, 4);
        store.addSaleToPolicy("Fun", -1, 50.0, exp);
        ConcurrentHashMap<Integer, Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(220.0, newPrice,0);
    }
    //endregion

    //region Discount rules tests
    @Test
    void HappyAndRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        PriceForGetSale exp1 = new PriceForGetSale(1, 1000.0);
        QuantityForGetSale exp2 = new QuantityForGetSale(2,productID1,2);
        AndComposite andExpression = new AndComposite(1);
        andExpression.add(exp1);
        andExpression.add(exp2);
        store.addSaleToPolicy(null, -1, 50.0, andExpression);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(3050.0, newPrice,0);
    }

    @Test
    void SadAndRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        PriceForGetSale exp1 = new PriceForGetSale(1, 1000.0);
        QuantityForGetSale exp2 = new QuantityForGetSale(2,productID1,5);
        AndComposite andExpression = new AndComposite(1);
        andExpression.add(exp1);
        andExpression.add(exp2);
        store.addSaleToPolicy(null, -1, 50.0, andExpression);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(6100.0, newPrice,0);
    }

    @Test
    void HappyOrRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        PriceForGetSale exp1 = new PriceForGetSale(1, 10000.0);
        QuantityForGetSale exp2 = new QuantityForGetSale(2,productID1,2);
        OrComposite orExpression = new OrComposite(1);
        orExpression.add(exp1);
        orExpression.add(exp2);
        store.addSaleToPolicy(null, -1, 50.0, orExpression);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(3050.0, newPrice,0);
    }

    @Test
    void SadOrRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        PriceForGetSale exp1 = new PriceForGetSale(1, 10000.0);
        QuantityForGetSale exp2 = new QuantityForGetSale(2,productID1,5);
        OrComposite orExpression = new OrComposite(1);
        orExpression.add(exp1);
        orExpression.add(exp2);
        store.addSaleToPolicy(null, -1, 50.0, orExpression);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(6100.0, newPrice,0);
    }

    @Test
    void HappyXorRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        //StoreSale sale = new StoreSale(1, store.getId(), 50.0);
        PriceForGetSale exp1 = new PriceForGetSale(1, 8000.0);
        QuantityForGetSale exp2 = new QuantityForGetSale(2,productID1,2);
        XorComposite xorComposite = new XorComposite(1);
        xorComposite.add(exp1);
        xorComposite.add(exp2);
        //sale.setExpression(xorComposite);
        //DC.AddSale(sale);
        store.addSaleToPolicy(null, -1, 50.0, xorComposite);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        //Double newPrice = DC.calculatePrice(products,2,6100.0);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(3050.0, newPrice,0);
    }

    @Test
    void SadXorRule() {
        Integer productID1 = store.getProductID("computer");
        Integer productID2 = store.getProductID("Bag");
        //StoreSale sale = new StoreSale(1, store.getId(), 50.0);
        PriceForGetSale exp1 = new PriceForGetSale(1, 1000.0);
        QuantityForGetSale exp2 = new QuantityForGetSale(2,productID1,2);
        XorComposite xorComposite = new XorComposite(1);
        xorComposite.add(exp1);
        xorComposite.add(exp2);
        //sale.setExpression(xorComposite);
        //DC.AddSale(sale);
        store.addSaleToPolicy(null, -1, 50.0, xorComposite);
        ConcurrentHashMap<Integer,Integer> products = new ConcurrentHashMap<>();
        products.put(productID1, 2);
        products.put(productID2, 1);
        //Double newPrice = DC.calculatePrice(products,2,6100.0);
        Double newPrice = store.calculateBugPrice(2,products);
        assertEquals(6100.0, newPrice,0);
    }

    //endregion

    //region Simple buying rules tests
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


 */
}