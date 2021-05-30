package TradingSystem.Server.DomainLayer.TradingSystemComponent.Task;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

import static TradingSystem.Server.ServiceLayer.Configuration.*;

public class AddManagerTaskUnitTests implements Callable<ResultUnitTests> {

    //@Autowired
    public static TradingSystemImplRubin tradingSystem;

    public static void setTradingSystem(TradingSystemImplRubin tradingSystem) {
        AddManagerTaskUnitTests.tradingSystem = tradingSystem;
    }

    private String name;
    private int userID;
    private String connID;
    private String pass;
    private int storeID;
    private int newManagerID;

    public AddManagerTaskUnitTests(String name, String pass, int storeID, int newManagerID, Integer userID, String connID) {
        this.name = name;
        this.pass = pass;
        this.storeID = storeID;
        this.newManagerID = newManagerID;
        this.userID = userID;
        this.connID = connID;
        System.out.printf(ANSI_YELLOW + "%s: Add manager task constructor... \n" + ANSI_RESET, this.name);
    }

    @Override
    public ResultUnitTests call() throws Exception {
        //System.out.printf(ANSI_GREEN + "%s: Staring Register...\n", this.name);
        //Response res1 = tradingSystem.Register(connID, name, "123");
        //userID = res1.returnUserID();
        //Response res2 = tradingSystem.Login(connID, name, "123");
        //connID = res2.returnConnID();

        //Issue
        Response response = tradingSystem.AddNewManager(userID, connID, storeID, newManagerID);
        System.out.println("Manager with id: " + newManagerID + ": (Add manager task parallel) response: " + response);

        //Results to assert
        return new ResultUnitTests(this.name, LocalDateTime.now().toString(), response);

    }
}
