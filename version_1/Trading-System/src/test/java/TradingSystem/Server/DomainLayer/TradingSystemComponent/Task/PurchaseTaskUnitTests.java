package TradingSystem.Server.DomainLayer.TradingSystemComponent.Task;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

import static TradingSystem.Server.ServiceLayer.Configuration.ANSI_GREEN;
import static TradingSystem.Server.ServiceLayer.Configuration.ANSI_RESET;

public class PurchaseTaskUnitTests implements Callable<ResultUnitTests> {

    private String connID;
    private String name;
    private TradingSystemImpl tradingSystem = TradingSystemImpl.getInstance();
    private int storeID;
    private int productID;
    private int quantity;
    private String creditCard;
    private String phoneNumber;
    private String address;

    public PurchaseTaskUnitTests(String name, int storeID, int productID, int quantity, String creditCard, String phoneNumber, String address) {
        this.name = name;
        this.storeID = storeID;
        this.productID = productID;
        this.quantity = quantity;
        this.creditCard = creditCard;
        this.phoneNumber = phoneNumber;
        this.address = address;
        connID= tradingSystem.ConnectSystem().returnConnID();
    }

    @Override
    public ResultUnitTests call() throws Exception {
        System.out.printf(ANSI_GREEN + "%s: Staring Register...\n", this.name);
        Response res = tradingSystem.Register(connID, name, "123");
        Integer id = res.returnUserID();
        System.out.printf(ANSI_GREEN + "%s: Finish Register: id=%d connID=%s\n", this.name, id, this.connID);
        System.out.println(ANSI_RESET);
        //connID= tradingSystem.Login(connID,name,"123").returnConnID();

        //Prepare
        tradingSystem.AddProductToCart(connID, storeID, productID, quantity);

        //Issue
        Response response = tradingSystem.guestPurchase(connID, name, creditCard, phoneNumber, address);
        System.out.println(name + ": (Purchase task parallel) response: " + response);

        //Results to assert
        return new ResultUnitTests(this.name, LocalDateTime.now().toString(), response);
    }
}