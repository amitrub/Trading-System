package TradingSystem.Server.DomainLayer.StoreComponent;

import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {

    @Test
    void searchProduct()
    {
        Store s=new Store("store1",1,null,null);
        s.AddProductToStore( "computer", 3000.0, "Technology",0);
        s.AddProductToStore("Bag" ,100.0, "Beauty",0);
        s.AddProductToStore("IPed",  2500.0, "Technology", 0);
        s.AddProductToStore( "MakeUp",  40.0, "Beauty",0);
        s.AddProductToStore( "Ball",  60.0, "Fun",0);

        List<DummyProduct> L1=s.SearchProduct("computer",null,-1,-1);
        assertTrue(L1.get(0).getProductName().equals("computer"));

        List<DummyProduct> L2=s.SearchProduct(null,"Technology",-1,-1);
        assertTrue(L2.get(0).getCategory().equals("Technology")&&L2.get(1).getCategory().equals("Technology"));

        List<DummyProduct> L3=s.SearchProduct(null,null,0,50);
        assertTrue(L3.get(0).getProductName().equals("MakeUp"));

        List<DummyProduct> L4=s.SearchProduct(null,"Fun",0,50);
        assertTrue(L4.isEmpty());
    }

}