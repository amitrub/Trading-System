package TradingSystem.Acceptence_test;

import TradingSystem.Client.Client;
import TradingSystem.Server.ServiceLayer.DummyObject.Response;

import java.time.LocalDateTime;
import java.util.concurrent.Callable;

import static TradingSystem.Server.ServiceLayer.Configuration.*;

public class PurchaseTask implements Callable<Result> {

    private String name;
    private Client client;
    private int storeID;
    private int productID;
    private int quantity;
    private String creditCard;
    private String phoneNumber;
    private String adress;

    public PurchaseTask(String name, int storeID, int productID, int quantity, String creditCard, String phoneNumber, String adress) {
        this.name = name;
        this.client = new Client();
        this.storeID = storeID;
        this.productID = productID;
        this.quantity = quantity;
        this.creditCard = creditCard;
        this.phoneNumber = phoneNumber;
        this.adress = adress;
        System.out.printf(ANSI_YELLOW + "%s: Trying connect to system... ", this.name);
        this.client.connectSystem();
        if(this.client.getConnID().equals(""))
            System.out.println("Failed!!");
        else
            System.out.printf("Connection succeed! connID=%s", this.client.getConnID());
        System.out.println(ANSI_RESET);
    }

    @Override
    public Result call() throws Exception {
        //Register parallel check
        System.out.printf(ANSI_GREEN + "%s: Staring Register...\n", this.name);
        int id = client.Register(this.name, "1234");
        System.out.printf(ANSI_GREEN + "%s: Finish Register: id=%d connID=%s\n", this.name, id, this.client.getConnID());
        System.out.println(ANSI_RESET);

        //Prepare
        client.addProductToCart(this.storeID, this.productID, this.quantity);

        //Issue
        Response response = client.guestPurchase(this.name, this.creditCard, this.phoneNumber, this.adress);
        System.out.println(client.getUserName() + ": (Purchase task parallel) response: " + response);

        //Results to assert
        return new Result(this.name, LocalDateTime.now().toString(), response);
    }
}
