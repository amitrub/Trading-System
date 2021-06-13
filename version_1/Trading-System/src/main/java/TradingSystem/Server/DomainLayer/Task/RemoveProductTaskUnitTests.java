package TradingSystem.Server.DomainLayer.Task;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

import static TradingSystem.Server.ServiceLayer.Configuration.*;

public class RemoveProductTaskUnitTests  implements Callable<ResultUnitTests> {

    private static TradingSystemImpl tradingSystem;

    public static void setTradingSystem(TradingSystemImpl tradingSystem) {
        RemoveProductTaskUnitTests.tradingSystem = tradingSystem;
    }

    private String name;
    private int storeID;
    private int productID;
    private int ownerID;
    private String connID;

    public RemoveProductTaskUnitTests(String name, int storeID, int productID) {
        this.name = name;
        this.storeID = storeID;
        this.productID = productID;
        connID = tradingSystem.ConnectSystem().returnConnID();
        System.out.printf(ANSI_YELLOW + "%s: Store Owner is trying to remove product... " + ANSI_RESET, this.name);
    }

    public ResultUnitTests call() throws Exception {
        System.out.printf(ANSI_GREEN + "%s: Staring Register...\n", this.name);
        Response res = tradingSystem.Register(connID, name, "123");
        ownerID = res.returnUserID();
        Response response = tradingSystem.RemoveProduct(ownerID,storeID,productID,connID);
        System.out.println("owner with id " + ownerID + ": (RemoveProduct task parallel) response: " + response);

        //Results to assert
        return new ResultUnitTests(this.name, LocalDateTime.now().toString(), response);
    }
}
