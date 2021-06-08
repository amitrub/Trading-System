package TradingSystem.Acceptence_tests;

import TradingSystem.Client.Client_Driver;
import TradingSystem.Client.Client_Interface;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimitForProduct;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.LimitExp.QuantityLimitForStore;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.PriceForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.SaleExp.QuantityForGetSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.MaxComposite;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.ProductSale;
import TradingSystem.Server.DomainLayer.StoreComponent.Policies.Sales.StoreSale;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyStore;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PoliciesTests {

    Client_Interface client = Client_Driver.getClient();
    Integer storeID;

    //region other functions
    Integer getStoreID(List<DummyStore> stores, String storeName)
    {
        for (int i=0; i<stores.size(); i++)
        {
            if(stores.get(i).getName().equals(storeName))
                return stores.get(i).getId();
        }
        return -1;
    }

    Integer getProductID(List<DummyProduct> storeProducts, String productName)
    {
        for (int i=0; i<storeProducts.size(); i++)
        {
            if(storeProducts.get(i).getProductName().equals(productName))
                return storeProducts.get(i).getProductID();
        }
        return -1;
    }
    //endregion

    @BeforeEach
    void setUp() {
        client.clearSystem();
        client.connectSystem();
        client.Register("elinor", "123");
        client.Login("elinor", "123");
        client.openStore("Adidas");
        storeID = getStoreID(client.showAllStores().getStores(),"Adidas");
        client.addProduct(storeID, "Black T-Shirt", "Tops", 80.0, 25);
        client.addProduct(storeID, "White T-Shirt", "Tops", 100.0, 25);
    }

    @AfterEach
    void tearDown() {
        client.exitSystem();
        client.clearSystem();
    }

    //region requirement 4.2.1: Add Discount Policy
    @Test
    void Happy_AddDiscountPolicy() {
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID2 = getProductID(storeProducts1,"White T-Shirt");

        PriceForGetSale exp1 = new PriceForGetSale( 100);
        PriceForGetSale exp2 = new PriceForGetSale( 200);
        StoreSale sale = new StoreSale(exp1, storeID, 50);
        ProductSale sale2 = new ProductSale(exp2,productID2,50);
        MaxComposite max = new MaxComposite();
        max.add(sale);
        max.add(sale2);
        Response res = client.addDiscountPolicy(storeID, max);
        assertFalse(res.getIsErr());
    }

    @Test
    void Sad_AddDiscountPolicy1() {
        //illegal productID
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID2 = getProductID(storeProducts1,"White T-Shirt");
        QuantityForGetSale quantityExp = new QuantityForGetSale(productID2+1,2);
        ProductSale sale = new ProductSale(quantityExp, productID2+1, 20);
        Response res = client.addDiscountPolicy(storeID, sale);
        assertTrue(res.getIsErr());
    }

    @Test
    void Sad_AddDiscountPolicy2() {
        //illegal discount percentage
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID2 = getProductID(storeProducts1,"White T-Shirt");
        QuantityForGetSale quantityExp = new QuantityForGetSale(productID2,2);
        ProductSale sale = new ProductSale(quantityExp, productID2, 200);
        Response res = client.addDiscountPolicy(storeID, sale);
        assertTrue(res.getIsErr());
    }

    @Test
    void Sad_AddDiscountPolicy3() {
        //no permission to add policy
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID2 = getProductID(storeProducts1,"White T-Shirt");
        QuantityForGetSale quantityExp = new QuantityForGetSale(productID2,2);
        ProductSale sale = new ProductSale(quantityExp, productID2, 50);
        client.Logout();

        client.Register("reut", "123");
        client.Login("reut", "123");
        Response res = client.addDiscountPolicy(storeID, sale);
        assertTrue(res.getIsErr());
    }
    //endregion

    //region requirement 4.2.2: Add Buying Policy
    @Test
    void Happy_AddBuyingPolicy() {
        QuantityLimitForStore quantityExp = new QuantityLimitForStore(2, storeID);
        Response res = client.addBuyingPolicy(storeID, quantityExp);
        assertFalse(res.getIsErr());
    }

    @Test
    void Sad_AddBuyingPolicy1() {
        //illegal productID
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID2 = getProductID(storeProducts1,"White T-Shirt");
        QuantityLimitForProduct quantity = new QuantityLimitForProduct(2, productID2+1);
        Response res = client.addBuyingPolicy(storeID, quantity);
        assertTrue(res.getIsErr());
    }

    @Test
    void Sad_AddBuyingPolicy2() {
        //illegal discount percentage
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID2 = getProductID(storeProducts1,"White T-Shirt");
        QuantityLimitForProduct quantity = new QuantityLimitForProduct(-5, productID2);
        Response res = client.addBuyingPolicy(storeID, quantity);
        assertTrue(res.getIsErr());
    }

    @Test
    void Sad_AddBuyingPolicy3() {
        //no permission to add policy
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID2 = getProductID(storeProducts1,"White T-Shirt");
        QuantityLimitForProduct quantity = new QuantityLimitForProduct(5, productID2);
        client.Logout();

        client.Register("reut", "123");
        client.Login("reut", "123");
        Response res = client.addBuyingPolicy(storeID, quantity);
        assertTrue(res.getIsErr());
    }
    //endregion

    //region requirement 4.2.3: Remove Discount Policy
    @Test
    void Happy_RemoveDiscountPolicy() {
        PriceForGetSale exp = new PriceForGetSale( 100);
        StoreSale sale = new StoreSale(exp, storeID, 50);
        client.addDiscountPolicy(storeID, sale);

        Response res = client.removeDiscountPolicy(storeID);
        assertFalse(res.getIsErr());
    }

    @Test
    void Sad_RemoveDiscountPolicy1() {
        //the policy doesn't exist
        Response res = client.removeDiscountPolicy(storeID);
        assertTrue(res.getIsErr());
    }

    @Test
    void Sad_RemoveDiscountPolicy2() {
        //no permission to remove policy
        PriceForGetSale exp = new PriceForGetSale( 100);
        StoreSale sale = new StoreSale(exp, storeID, 50);
        client.addDiscountPolicy(storeID, sale);
        client.Logout();

        client.Login("reut", "123");
        client.Register("reut", "123");
        Response res = client.removeDiscountPolicy(storeID);
        assertTrue(res.getIsErr());
    }
    //endregion

    //region requirement 4.2.4: Remove Buying Policy
    @Test
    void Happy_RemoveBuyingPolicy() {
        QuantityLimitForStore quantityExp = new QuantityLimitForStore(2, storeID);
        client.addBuyingPolicy(storeID, quantityExp);

        Response res = client.removeBuyingPolicy(storeID);
        assertFalse(res.getIsErr());
    }

    @Test
    void Sad_RemoveBuyingPolicy1() {
        //the policy doesn't exist
        Response res = client.removeBuyingPolicy(storeID);
        assertTrue(res.getIsErr());
    }

    @Test
    void Sad_RemoveBuyingPolicy2() {
        //no permission to remove policy
        QuantityLimitForStore quantityExp = new QuantityLimitForStore(2, storeID);
        client.addBuyingPolicy(storeID, quantityExp);
        client.Logout();

        client.Register("reut", "123");
        client.Login("reur","123");
        Response res = client.removeBuyingPolicy(storeID);
        assertTrue(res.getIsErr());
    }
    //endregion

    //region requirement 4.2.5: Get Information of Policies
    @Test
    void Happy_GetInfo() {
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID2 = getProductID(storeProducts1,"White T-Shirt");
        QuantityForGetSale quantityExp1 = new QuantityForGetSale(productID2,2);
        ProductSale sale = new ProductSale(quantityExp1, productID2, 50);
        client.addDiscountPolicy(storeID, sale);
        QuantityLimitForStore quantityExp2 = new QuantityLimitForStore(5, storeID);
        client.addBuyingPolicy(storeID, quantityExp2);

        Response res = client.getPoliciesInfo(storeID);
        assertFalse(res.getIsErr());
    }

    @Test
    void Sad_NoPermissionToInfo() {
        List<DummyProduct> storeProducts1 = client.showStoreProducts(storeID).returnProductList();
        Integer productID2 = getProductID(storeProducts1,"White T-Shirt");
        QuantityForGetSale quantityExp = new QuantityForGetSale(productID2,2);
        ProductSale sale = new ProductSale(quantityExp, productID2, 50);
        client.addDiscountPolicy(storeID, sale);
        client.Logout();

        client.Register("reut", "123");
        client.Login("reut", "123");
        Response res = client.getPoliciesInfo(storeID);
        assertTrue(res.getIsErr());
    }

    @Test
    void Sad_EmptyPoliciesInfo() {
        Response res = client.getPoliciesInfo(storeID);
        assertFalse(res.getIsErr());
    }

    //endregion

}
