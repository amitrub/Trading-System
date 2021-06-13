package TradingSystem.Server.DomainLayer.Task;

import TradingSystem.Server.DomainLayer.TradingSystemComponent.TradingSystemImpl;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

import static TradingSystem.Server.ServiceLayer.Configuration.ANSI_GREEN;
import static TradingSystem.Server.ServiceLayer.Configuration.ANSI_RESET;

public class PurchaseTaskUnitTests implements Callable<ResultUnitTests> {

    private static TradingSystemImpl tradingSystem;

    public static void setTradingSystem(TradingSystemImpl tradingSystem) {
        PurchaseTaskUnitTests.tradingSystem = tradingSystem;
    }

    private String connID;
    private String name;
    private int storeID;
    private int productID;
    private int quantity;
    private String creditCard;
    private String month;
    private String year;
    private String cvv;
    private String ID;
    private String address;
    private String city;
    private String country;
    private String zip;

    public PurchaseTaskUnitTests(String name, int storeID, int productID, int quantity, String creditCard, String month, String year, String cvv, String ID, String address, String city, String country, String zip) {
        this.name = name;
        this.storeID = storeID;
        this.productID = productID;
        this.quantity = quantity;
        this.creditCard = creditCard;
        this.month = month;
        this.year = year;
        this.cvv = cvv;
        this.ID = ID;
        this.address = address;
        this.city = city;
        this.country = country;
        this.zip = zip;
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
        Response response = tradingSystem.guestPurchase(connID, name, creditCard, month, year, cvv, ID, address,city,country,zip);
        System.out.println(name + ": (Purchase task parallel) response: " + response);

        //Results to assert
        return new ResultUnitTests(this.name, LocalDateTime.now().toString(), response);
    }
}
