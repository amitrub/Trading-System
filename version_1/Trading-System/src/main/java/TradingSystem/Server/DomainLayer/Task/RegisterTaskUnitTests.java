package TradingSystem.Server.DomainLayer.Task;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImplRubin;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

import static TradingSystem.Server.ServiceLayer.Configuration.*;

public class RegisterTaskUnitTests implements Callable<ResultUnitTests> {

    private static TradingSystemImplRubin tradingSystem;

    public static void setTradingSystem(TradingSystemImplRubin tradingSystem) {
        RegisterTaskUnitTests.tradingSystem = tradingSystem;
    }

    private String name;
    private Integer userID;
    private String connID;

    public RegisterTaskUnitTests(String name) {
        this.name = name;
        System.out.printf(ANSI_YELLOW + "%s: Trying connect to system... ", this.name);
        connID = tradingSystem.ConnectSystem().returnConnID();
    }

    @Override
    public ResultUnitTests call() throws Exception {
        System.out.printf(ANSI_GREEN + "%s: Staring Register...\n", this.name);
        Response res = tradingSystem.Register(connID, name,"123");
        userID = res.returnUserID();
        connID = res.returnConnID();
        System.out.printf(ANSI_GREEN + "%s: Finish Register: id=%d connID=%s\n", this.name, userID, connID);
        System.out.println(ANSI_RESET);

        return new ResultUnitTests(this.name, LocalDateTime.now().toString(), res);
    }

}
