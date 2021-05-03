package TradingSystem.Server.DomainLayer.StoreComponent;

import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingBag;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.AndComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.OrComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Expressions.XorComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.NumOfProductsForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.PriceForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.QuantityForGetSale;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystem;
import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {

    Store store;
    TradingSystemImpl tradingSystem = TradingSystemImpl.getInstance();

    @BeforeEach
    void setUp() {
        store = new Store("store1",1);
        store.AddProductToStore( "computer", 3000.0, "Technology",5);
        store.AddProductToStore("Bag" ,100.0, "Beauty",5);
        store.AddProductToStore("IPed",  2500.0, "Technology", 5);
        store.AddProductToStore( "MakeUp",  40.0, "Beauty",5);
        store.AddProductToStore( "Ball",  60.0, "Fun",5);
    }

    @AfterEach
    void tearDown() {
        store = null;
    }

    //region search tests
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
        Product p1=new Product(1,"1","1",1.0);
        Product p2=new Product(2,"2","2",1.0);
        Product p3=new Product(3,"3","3",1.0);
        Product p4=new Product(4,"4","4",1.0);
        ConcurrentHashMap<Product,Integer> PSH1=new ConcurrentHashMap<>();
        PSH1.put(p1,3);
        PSH1.put(p2,2);
        PSH1.put(p4,5);
        ConcurrentHashMap<Product,Integer> PSH2=new ConcurrentHashMap<>();
        PSH2.put(p1,10);
        PSH2.put(p2,2);
        PSH2.put(p3,8);
        ShoppingBag SB1=new ShoppingBag(1,1);
        ShoppingBag SB2=new ShoppingBag(2,2);
        ShoppingHistory SH1=new ShoppingHistory(SB1,PSH1);
        ShoppingHistory SH2=new ShoppingHistory(SB2,PSH2);
        store.addHistory(SH1);
        store.addHistory(SH2);
        List<DummyShoppingHistory> list = store.ShowStoreHistory();
        assertTrue(list.size()==2);

        Set<Integer > DP1exist=new HashSet<>();
        Set<Integer > DP2exist=new HashSet<>();
        for (DummyShoppingHistory DSH:list
        ) {
            if(DSH.getStoreID()==1){
                Set<DummyProduct> DP1=DSH.getProducts().keySet();
                for (DummyProduct DP:DP1
                ) {
                    DP1exist.add(DP.getProductID());
                    System.out.println(DP.getProductID()+" "+DP.getProductName()+" "+ DP.getQuantity()+ " "+ DP.getPrice()+" "+DP.getCategory()+" "+DP.getStoreID()+" "+DP.getStoreID());
                }
            }
            if(DSH.getStoreID()==2){
                Set<DummyProduct> DP2=DSH.getProducts().keySet();
                for (DummyProduct DP:DP2
                ) {
                    DP2exist.add(DP.getProductID());
                    System.out.println(DP.getProductID()+" "+DP.getProductName()+" "+ DP.getQuantity()+ " "+ DP.getPrice()+" "+DP.getCategory()+" "+DP.getStoreID()+" "+DP.getStoreID());
                }
            }
        }
        assertTrue(DP1exist.contains(1)&&DP1exist.contains(2)&&DP1exist.contains(4));
        assertTrue(DP2exist.contains(1)&&DP2exist.contains(2)&&DP2exist.contains(3));

    }

    @Test
    void showStoreEmpty() {
        Store s1=new Store("s1",1);
        List<DummyShoppingHistory> list =s1.ShowStoreHistory();
        assertTrue(list.isEmpty());
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