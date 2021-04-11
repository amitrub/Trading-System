package TradingSystem.Server.DomainLayer.TradingSystemComponent;

import TradingSystem.Server.ServiceLayer.DummyObject.Response;

public class Temp {
    public static void main(String[] args) {
        TradingSystem t = TradingSystem.getInstance();
        String gust1=t.connectSystem().getConnID();
        t.Register(gust1, "nofet", "123");
        String NconnID = t.Login(gust1, "nofet", "123").getConnID();
        t.AddStore(2, NconnID, "NofetStore");
        t.AddProductToStore(2, NconnID, 2, "computer", "Technology", 3000.0);
        t.AddQuantityProduct(2, NconnID, 2, 1, 100);
        String gust2=t.connectSystem().getConnID();
        t.Register(gust2, "elinor", "123");
        String EconnID = t.Login(gust2, "elinor", "123").getConnID();
        String gust3=t.connectSystem().getConnID();
        t.Register(gust3, "roee", "123");
        String RconnID = t.Login(gust3, "roee", "123").getConnID();
        Response r1=t.AddNewOwner(2,NconnID,2,3);
        Response r2=t.AddNewManager(2,NconnID,2,4);
        Response r3= t.AddProductToStore(3, EconnID, 2, "bag", "Technology", 150.0);
        Response r4= t.AddProductToStore(4, RconnID, 2, "box", "Technology", 100.0);
        System.out.println(r1.getMessage());
        System.out.println(r2.getMessage());
        System.out.println(r3.getMessage());
        System.out.println(r4.getMessage());


      //  t.AddStore(2, connID, "store2");
      //  t.AddProductToStore(2, connID, 2, "computer", "Technology", 4000.0);
        //t.AddQuantityProduct(2, connID, 2, 2, 200);
      // t.EditProduct(2, connID, 1, 1, "computer", "Technology", 3500.0);
       // t.RemoveProduct(2, 1, 1, connID);
      //  t.StoreHistory(2, 2, connID);
    }
}
		