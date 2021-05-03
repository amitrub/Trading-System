package TradingSystem.Server.DomainLayer.StoreComponent;

import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingBag;
import TradingSystem.Server.DomainLayer.ShoppingComponent.ShoppingHistory;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyProduct;
import TradingSystem.Server.ServiceLayer.DummyObject.DummyShoppingHistory;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

class StoreTest {
    Store s=new Store("store1",1,null,null);
    @Test
    void searchProduct() {
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
        s.addHistory(SH1);
        s.addHistory(SH2);
        List<DummyShoppingHistory> list =s.ShowStoreHistory();
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

}